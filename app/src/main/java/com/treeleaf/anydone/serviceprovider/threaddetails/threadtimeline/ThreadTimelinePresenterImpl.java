package com.treeleaf.anydone.serviceprovider.threaddetails.threadtimeline;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ThreadTimelinePresenterImpl extends BasePresenter<ThreadTimelineContract.ThreadTimelineView>
        implements ThreadTimelineContract.ThreadTimelinePresenter {
    private static final String TAG = "TicketTimelinePresenter";
    private ThreadTimelineRepository threadTimelineRepository;

    @Inject
    public ThreadTimelinePresenterImpl(ThreadTimelineRepository threadTimelineRepository) {
        this.threadTimelineRepository = threadTimelineRepository;
    }

    @Override
    public void getEmployees() {
        Observable<UserRpcProto.UserBaseResponse> employeeObservable;
        String token = Hawk.get(Constants.TOKEN);

        employeeObservable = threadTimelineRepository.findEmployees(token);

        addSubscription(employeeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(UserRpcProto.UserBaseResponse getEmployeeResponse) {
                        GlobalUtils.showLog(TAG, "find employees response:"
                                + getEmployeeResponse);

                        if (getEmployeeResponse == null) {
                            getView().getEmployeeFail("Failed to get employee");
                            return;
                        }

                        if (getEmployeeResponse.getError()) {
                            getView().getEmployeeFail(getEmployeeResponse.getMsg());
                            return;
                        }

                        saveEmployees(getEmployeeResponse.getEmployeesList());

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
    public void enableBot(String threadId) {
        getView().showProgressBar("Enabling bot...");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> rtcBaseResponseObservable;
        String token = Hawk.get(Constants.TOKEN);

        rtcBaseResponseObservable = threadTimelineRepository.enableBot(token, threadId);

        addSubscription(rtcBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(RtcServiceRpcProto.RtcServiceBaseResponse rtcResponse) {
                        GlobalUtils.showLog(TAG, "enable bot response:"
                                + rtcResponse);

                        getView().hideProgressBar();
                        if (rtcResponse == null) {
                            getView().enableBotFail("Failed to enable bot");
                            return;
                        }

                        if (rtcResponse.getError()) {
                            getView().enableBotFail(rtcResponse.getMsg());
                            return;
                        }

                        getView().enableBotSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().enableBotFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void disableBot(String threadId) {
        getView().showProgressBar("Disabling bot...");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> rtcBaseResponseObservable;
        String token = Hawk.get(Constants.TOKEN);

        rtcBaseResponseObservable = threadTimelineRepository.disableBot(token, threadId);

        addSubscription(rtcBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(RtcServiceRpcProto.RtcServiceBaseResponse rtcResponse) {
                        GlobalUtils.showLog(TAG, "disable bot response:"
                                + rtcResponse);

                        getView().hideProgressBar();
                        if (rtcResponse == null) {
                            getView().disableBotFail("Failed to disable bot");
                            return;
                        }

                        if (rtcResponse.getError()) {
                            getView().disableBotFail(rtcResponse.getMsg());
                            return;
                        }

                        getView().disableBotSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().disableBotFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }


/*    @Override
    public void unAssignEmployee(long ticketId, String employeeId) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(employeeId)
                .build();

        TicketProto.EmployeeAssigned employeesAssigned = TicketProto.EmployeeAssigned.newBuilder()
                .setAssignedAt(System.currentTimeMillis())
                .setAssignedTo(employeeProfile)
                .build();

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addEmployeesAssigned(employeesAssigned)
                .build();

        ticketObservable = ticketTimelineRepository.unAssignEmployee(token,
                ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse timelineResponse) {
                        GlobalUtils.showLog(TAG, "unassign employee response:"
                                + timelineResponse);
                        getView().hideProgressBar();

                        if (timelineResponse == null) {
                            getView().onEmployeeUnAssignFail("Failed to unassign employee ");
                            return;
                        }

                        if (timelineResponse.getError()) {
                            getView().onEmployeeUnAssignFail(timelineResponse.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().unAssignEmployee(ticketId, employeeId, new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                getView().onEmployeeUnAssignSuccess(employeeId);
                            }

                            @Override
                            public void fail() {
                                getView().onEmployeeUnAssignFail("failed to remove employee");
                            }
                        });


                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));

    }*/


    private void saveEmployees(List<UserProto.EmployeeProfile> employeesList) {
        AssignEmployeeRepo.getInstance().saveAssignEmployeeList(employeesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved assign employees");
                getView().getEmployeeSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save assign employees");
            }
        });
    }

}