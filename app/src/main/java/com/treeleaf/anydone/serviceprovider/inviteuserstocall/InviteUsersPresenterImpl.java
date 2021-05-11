package com.treeleaf.anydone.serviceprovider.inviteuserstocall;


import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class InviteUsersPresenterImpl extends BasePresenter<InviteUsersContract.InviteUsersView>
        implements InviteUsersContract.InviteUsersPresenter {

    private static final String TAG = "AddContributorPresenter";

    @Inject
    public InviteUsersPresenterImpl() {
    }

    @Override
    public void inviteContributors(long ticketId, List<String> employeeIds) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> contributorObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.TicketContributor> contributors = new ArrayList<>();
        for (String employeeId : employeeIds
        ) {
            GlobalUtils.showLog(TAG, "employeeIds: " + employeeId);
            UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(employeeId)
                    .build();
            TicketProto.TicketContributor employeesAssigned = TicketProto.TicketContributor.newBuilder()
                    .setTimestamp(System.currentTimeMillis())
                    .setEmployee(employeeProfile)
                    .build();

            contributors.add(employeesAssigned);
        }

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addAllTicketContributor(contributors)
                .build();

        GlobalUtils.showLog(TAG, "ticket check: " + ticket);

        contributorObservable = service.addContributors(token, String.valueOf(ticketId), ticket);

        addSubscription(contributorObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse ticketBaseResponse) {
                        GlobalUtils.showLog(TAG, "add contributor response:"
                                + ticketBaseResponse);

                        getView().hideProgressBar();

                        if (ticketBaseResponse == null) {
                            getView().inviteContributorsFail("Failed to add contributor");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().inviteContributorsFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().inviteContributorsSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void fetchContributors() {
        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> employeeObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        employeeObservable = service.findEmployees(token);

        addSubscription(employeeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse getEmployeeResponse) {
                        GlobalUtils.showLog(TAG, "find employees response:"
                                + getEmployeeResponse);

                        getView().hideProgressBar();
                        if (getEmployeeResponse == null) {
                            getView().fetchContributorsFail("Failed to get employee");
                            return;
                        }

                        if (getEmployeeResponse.getError()) {
                            getView().fetchContributorsFail(getEmployeeResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "get al emp check: " + getEmployeeResponse.getEmployeesList().size());
                        List<AssignEmployee> assignEmployeeList = ProtoMapper
                                .transformEmployee(getEmployeeResponse.getEmployeesList());
                        getView().fetchContributorSuccess(assignEmployeeList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }
}