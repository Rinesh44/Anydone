package com.treeleaf.anydone.serviceprovider.assignemployee;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
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

public class AssignEmployeePresenterImpl extends BasePresenter<AssignEmployeeContract.AssignEmployeeView>
        implements AssignEmployeeContract.AssignEmployeePresenter {

    private static final String TAG = "AssignEmployeePresenter";
    private AssignEmployeeRepository assignEmployeeRepository;

    @Inject
    public AssignEmployeePresenterImpl(AssignEmployeeRepository assignEmployeeRepository) {
        this.assignEmployeeRepository = assignEmployeeRepository;
    }

    @Override
    public void assignEmployee(long ticketId, List<String> employeeIds) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> assignEmployeeObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.EmployeeAssigned> assignedEmployee = new ArrayList<>();
        for (String employeeId : employeeIds
        ) {
            UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(employeeId)
                    .build();
            TicketProto.EmployeeAssigned employeesAssigned = TicketProto.EmployeeAssigned.newBuilder()
                    .setAssignedAt(System.currentTimeMillis())
                    .setAssignedTo(employeeProfile)
                    .build();

            assignedEmployee.add(employeesAssigned);
        }

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addAllEmployeesAssigned(assignedEmployee)
                .build();

        assignEmployeeObservable = assignEmployeeRepository.assignEmployee(token, ticketId, ticket);

        addSubscription(assignEmployeeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse ticketBaseResponse) {
                        GlobalUtils.showLog(TAG, "assign employee response:"
                                + ticketBaseResponse);

                        if (ticketBaseResponse == null) {
                            getView().assignEmployeeFail("Failed to assign employee");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().assignEmployeeFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().assignEmployeeSuccess();
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
    public void getEmployees() {
        getView().showProgressBar("Please wait...");
        Observable<UserRpcProto.UserBaseResponse> employeeObservable;
        String token = Hawk.get(Constants.TOKEN);

        employeeObservable = assignEmployeeRepository.findEmployees(token);

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
                            getView().getEmployeesFail("Failed to get employee");
                            return;
                        }

                        if (getEmployeeResponse.getError()) {
                            getView().getEmployeesFail(getEmployeeResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "get al emp check: " + getEmployeeResponse.getEmployeesList().size());
                        List<Employee> assignEmployeeList = ProtoMapper
                                .transformEmployee(getEmployeeResponse.getEmployeesList());
                        getView().getEmployeesSuccess(assignEmployeeList);
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
