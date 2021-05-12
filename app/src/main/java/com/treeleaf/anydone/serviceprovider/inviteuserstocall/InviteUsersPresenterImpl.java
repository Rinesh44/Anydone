package com.treeleaf.anydone.serviceprovider.inviteuserstocall;


import com.orhanobut.hawk.Hawk;
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

    @Override
    public ArrayList<AddedParticipantsForCall> transform(List<AssignEmployee> assignEmployees) {
        ArrayList<AddedParticipantsForCall> addedParticipantsForCalls = new ArrayList<>();
        for (AssignEmployee assignEmployee : assignEmployees) {
            AddedParticipantsForCall addedParticipantsForCall = new AddedParticipantsForCall();
            addedParticipantsForCall.setAccountId(assignEmployee.getAccountId());
            addedParticipantsForCall.setEmployeeId(assignEmployee.getEmployeeId());
            addedParticipantsForCall.setFullAccountName(assignEmployee.getName());
            addedParticipantsForCall.setProfileUrl(assignEmployee.getEmployeeImageUrl());
            addedParticipantsForCalls.add(addedParticipantsForCall);
        }
        return addedParticipantsForCalls;
    }

}