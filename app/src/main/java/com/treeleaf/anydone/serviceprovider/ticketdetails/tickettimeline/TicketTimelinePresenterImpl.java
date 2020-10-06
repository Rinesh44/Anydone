package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class TicketTimelinePresenterImpl extends BasePresenter<TicketTimelineContract.TicketTimelineView>
        implements TicketTimelineContract.TicketTimelinePresenter {
    private static final String TAG = "TicketTimelinePresenter";
    private TicketTimelineRepository ticketTimelineRepository;

    @Inject
    public TicketTimelinePresenterImpl(TicketTimelineRepository ticketTimelineRepository) {
        this.ticketTimelineRepository = ticketTimelineRepository;
    }

    @Override
    public void assignTicket(long ticketId, String employeeId) {
        getView().showProgressEmployee();
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        String token = Hawk.get(Constants.TOKEN);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(String.valueOf(employeeId))
                .build();

        TicketProto.EmployeeAssigned employeeAssigned = TicketProto.EmployeeAssigned.newBuilder()
                .setAssignedTo(employeeProfile)
                .setAssignedAt(System.currentTimeMillis())
                .build();

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setEmployeeAssigned(employeeAssigned)
                .build();

        GlobalUtils.showLog(TAG, "employee assinged check:" + employeeAssigned);

        getTicketsObservable = ticketTimelineRepository.assignTicket(token, ticketId, ticket);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "assign tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressEmployee();
                                if (getTicketsBaseResponse == null) {
                                    getView().assignFail("assign ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().assignFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().assignSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().assignFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void enableBot(String ticketId) {
        getView().showProgressBar("Enabling bot...");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> rtcBaseResponseObservable;
        String token = Hawk.get(Constants.TOKEN);

        rtcBaseResponseObservable = ticketTimelineRepository.enableBot(token, ticketId);

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
    public void disableBot(String ticketId) {
        getView().showProgressBar("Disabling bot...");
        Observable<RtcServiceRpcProto.RtcServiceBaseResponse> rtcBaseResponseObservable;
        String token = Hawk.get(Constants.TOKEN);

        rtcBaseResponseObservable = ticketTimelineRepository.disableBot(token, ticketId);

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

    @Override
    public void getEmployees() {
        Observable<UserRpcProto.UserBaseResponse> employeeObservable;
        String token = Hawk.get(Constants.TOKEN);

        employeeObservable = ticketTimelineRepository.findEmployees(token);

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
    public void getTicketTimeline(long ticketId) {
//        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        ticketObservable = ticketTimelineRepository.getTicketTimeline(token,
                ticketId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse timelineResponse) {
                        GlobalUtils.showLog(TAG, "timeline response:"
                                + timelineResponse);
                        getView().hideProgressBar();

                        if (timelineResponse == null) {
                            getView().geTicketTimelineFail("Failed to get timeline");
                            return;
                        }

                        if (timelineResponse.getError()) {
                            getView().geTicketTimelineFail(timelineResponse.getMsg());
                            return;
                        }

                        AssignEmployee assignedEmployee = ProtoMapper.
                                transformAssignedEmployee(timelineResponse.getTicket().
                                        getEmployeeAssigned());

                        getView().getTicketTimelineSuccess(assignedEmployee);

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
    }

    @Override
    public void getCustomerDetails(long ticketId) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        getView().setCustomerDetails(tickets.getCustomer());
    }

    @Override
    public void getAssignedEmployees(long ticketId) {
        Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);
        getView().setAssignedEmployee(tickets.getAssignedEmployee());
    }

    @Override
    public void unAssignContributor(long ticketId, String contributorId) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(contributorId)
                .build();

        TicketProto.TicketContributor ticketContributor = TicketProto.TicketContributor.newBuilder()
                .setEmployee(employeeProfile)
                .build();

        GlobalUtils.showLog(TAG, "delete contributor: " + ticketContributor);

        ticketObservable = ticketTimelineRepository.unAssignContributor(token,
                String.valueOf(ticketId), ticketContributor);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse timelineResponse) {
                        GlobalUtils.showLog(TAG, "unassign contributor response:"
                                + timelineResponse);
                        getView().hideProgressBar();

                        if (timelineResponse == null) {
                            getView().onContributorUnAssignFail("Failed to unassign contributor");
                            return;
                        }

                        if (timelineResponse.getError()) {
                            getView().onContributorUnAssignFail(timelineResponse.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().removeContributor(ticketId, contributorId,
                                new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        getView().onContributorUnAssignSuccess(contributorId);
                                    }

                                    @Override
                                    public void fail() {
                                        getView().onContributorUnAssignFail("failed to " +
                                                "remove contributor");
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
    }

    @Override
    public void closeTicket(long ticketId) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        ticketObservable = ticketTimelineRepository.closeTicket(token,
                ticketId, "close ticket");

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse closeTicketResponse) {
                        GlobalUtils.showLog(TAG, "close ticket response:"
                                + closeTicketResponse);
                        getView().hideProgressBar();

                        if (closeTicketResponse == null) {
                            getView().onTicketCloseFail("Failed to close ticket");
                            return;
                        }

                        if (closeTicketResponse.getError()) {
                            getView().onTicketCloseFail(closeTicketResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.REFETCH_TICKET_STAT, true);
                        getView().onTicketCloseSuccess();
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
    }

    @Override
    public void reopenTicket(long ticketId) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        ticketObservable = ticketTimelineRepository.reopenTicket(token,
                ticketId, "reopen ticket");

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse reopenTicketResponse) {
                        GlobalUtils.showLog(TAG, "reopen ticket response:"
                                + reopenTicketResponse);
                        getView().hideProgressBar();

                        if (reopenTicketResponse == null) {
                            getView().onTicketReopenFail("Failed to reopen ticket");
                            return;
                        }

                        if (reopenTicketResponse.getError()) {
                            getView().onTicketReopenFail(reopenTicketResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.REFETCH_TICKET_STAT, true);
                        getView().onTicketReopenSuccess();

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
    }

    @Override
    public void resolveTicket(long ticketId) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        ticketObservable = ticketTimelineRepository.resolveTicket(token,
                ticketId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse resolveTicketResponse) {
                        GlobalUtils.showLog(TAG, "resolve ticket response:"
                                + resolveTicketResponse);
                        getView().hideProgressBar();

                        if (resolveTicketResponse == null) {
                            getView().onTicketResolveFail("Failed to resolve ticket");
                            return;
                        }

                        if (resolveTicketResponse.getError()) {
                            getView().onTicketResolveFail(resolveTicketResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.REFETCH_TICKET_STAT, true);
                        getView().onTicketResolveSuccess();
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
    }


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
