package com.anydone.desk.threaddetails.threadtimeline;

import com.anydone.desk.realm.model.Thread;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ProtoMapper;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        employeeObservable = service.findEmployees(token);

        addSubscription(employeeObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse getEmployeeResponse) {
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
                    public void onError(@NonNull Throwable e) {
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

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        rtcBaseResponseObservable = service.enableThreadBotReply(token, threadId);

        addSubscription(rtcBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(@NonNull RtcServiceRpcProto.RtcServiceBaseResponse rtcResponse) {
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
                    public void onError(@NonNull Throwable e) {
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        rtcBaseResponseObservable = service.disableThreadBotReply(token, threadId);

        addSubscription(rtcBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onNext(@NonNull RtcServiceRpcProto.RtcServiceBaseResponse rtcResponse) {
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
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().disableBotFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void assignEmployee(String threadId, String employeeId) {
        getView().showProgressBar("");
        Observable<ConversationRpcProto.ConversationBaseResponse> getThreadObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(String.valueOf(employeeId))
                .build();

        TicketProto.EmployeeAssigned employeeAssigned = TicketProto.EmployeeAssigned.newBuilder()
                .setAssignedTo(employeeProfile)
                .setAssignedAt(System.currentTimeMillis())
                .build();

        ConversationProto.ConversationThread thread = ConversationProto.ConversationThread.newBuilder()
                .addEmployeeAssigned(employeeAssigned)
                .setConversationId(threadId)
                .build();

        GlobalUtils.showLog(TAG, "employee assinged check:" + employeeAssigned);
        GlobalUtils.showLog(TAG, "employee assinged whole:" + thread);

        getThreadObservable = service.assignEmployeeToThread(token, thread);
        addSubscription(getThreadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse
                                                       getThreadBaseResponse) {
                                GlobalUtils.showLog(TAG, "assign emp response: "
                                        + getThreadBaseResponse);

                                getView().hideProgressBar();
                                if (getThreadBaseResponse == null) {
                                    getView().assignFail("assign emp failed");
                                    return;
                                }

                                if (getThreadBaseResponse.getError()) {
                                    getView().assignFail(getThreadBaseResponse.getMsg());
                                    return;
                                }

                                ThreadRepo.getInstance().setAssignedEmployee(threadId, employeeId,
                                        new Repo.Callback() {
                                            @Override
                                            public void success(Object o) {
                                                GlobalUtils.showLog(TAG, "assigned employee on thread");
                                                getView().assignSuccess(employeeId);
                                            }

                                            @Override
                                            public void fail() {
                                                GlobalUtils.showLog(TAG, "Failed to save assigned employee on thread");
                                            }
                                        });

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
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
    public void getThreadById(String threadId) {
        Observable<ConversationRpcProto.ConversationBaseResponse> threadObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        threadObservable = service.getConversationThreadById(token, threadId);

        addSubscription(threadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                    @Override
                    public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse threadResponse) {
                        GlobalUtils.showLog(TAG, "get thread by id response:"
                                + threadResponse);

                        if (threadResponse.getError()) {
                            getView().getThreadByIdFail(threadResponse.getMsg());
                            return;
                        }

                        if (!threadResponse.getConversation().getEmployeeAssignedList().isEmpty() &&
                                threadResponse.getConversation().getEmployeeAssigned(0) != null) {
                            TicketProto.EmployeeAssigned assignedEmpPb =
                                    threadResponse.getConversation().getEmployeeAssigned(0);
                            AssignEmployee assignEmployee = ProtoMapper.transformAssignedEmployee(assignedEmpPb);
                            ThreadRepo.getInstance().setAssignedEmployee(threadId,
                                    assignEmployee.getEmployeeId(), new Repo.Callback() {
                                        @Override
                                        public void success(Object o) {
                                            GlobalUtils.showLog(TAG, "Employee assigned to thread");
                                            getView().getThreadByIdSuccess();
                                        }

                                        @Override
                                        public void fail() {

                                        }
                                    });
                        }
/*
                        getView().getThreadByIdSuccess(threadResponse.getConversation().getEmployeeAssigned(
                                threadResponse.getConversation().getEmployeeAssignedCount() - 1));*/
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().disableBotFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void getLinkedTickets(String threadId) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getLinkedTickets(token, threadId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse ticketResponse) {
                        GlobalUtils.showLog(TAG, "get linked ticket response:"
                                + ticketResponse);

                        if (ticketResponse.getError()) {
                            getView().getLinkedTicketFail(ticketResponse.getMsg());
                            return;
                        }

                        if (!ticketResponse.getTicketsList().isEmpty()) {
                            saveLinkedTickets(ticketResponse, threadId);
                        } else {
                            getView().getLinkedTicketSuccess();
                        }
/*
                        getView().getThreadByIdSuccess(threadResponse.getConversation().getEmployeeAssigned(
                                threadResponse.getConversation().getEmployeeAssignedCount() - 1));*/
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().getLinkedTicketFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void setAsImportant(String threadId, boolean isImportant) {
        getView().showProgressBar("");
        Thread thread = ThreadRepo.getInstance().getThreadById(threadId);
        ConversationProto.ConversationThread conversationThread;

        if (isImportant) {
            if (thread.isFollowUp()) {
                conversationThread =
                        ConversationProto.ConversationThread.newBuilder()
                                .setConversationId(threadId)
                                .setImportant(true)
                                .setFollowUp(true)
                                .setFollowUpDate(thread.getFollowUpDate())
                                .build();
            } else {
                conversationThread =
                        ConversationProto.ConversationThread.newBuilder()
                                .setConversationId(threadId)
                                .setImportant(true)
                                .setFollowUp(false)
                                .build();
            }
        } else {
            if (thread.isFollowUp()) {
                conversationThread =
                        ConversationProto.ConversationThread.newBuilder()
                                .setConversationId(threadId)
                                .setImportant(false)
                                .setFollowUp(true)
                                .setFollowUpDate(thread.getFollowUpDate())
                                .build();
            } else {
                conversationThread =
                        ConversationProto.ConversationThread.newBuilder()
                                .setConversationId(threadId)
                                .setImportant(false)
                                .setFollowUp(false)
                                .build();
            }
        }

        Observable<ConversationRpcProto.ConversationBaseResponse> conversationObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        conversationObservable = service.updateThread(token, conversationThread);

        addSubscription(conversationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                    @Override
                    public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse conversationResponse) {
                        GlobalUtils.showLog(TAG, "get set as important response:"
                                + conversationResponse);

                        getView().hideProgressBar();
                        if (conversationResponse.getError()) {
                            getView().setImportantFail(conversationResponse.getMsg());
                            return;
                        }

                        getView().setImportantSuccess(isImportant);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().setImportantFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }


    private void saveLinkedTickets(TicketServiceRpcProto.TicketBaseResponse ticketResponse, String
            threadId) {
        TicketRepo.getInstance().deleteLinkedTickets(new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "cleared linked tickets");
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to remove linked tickets");
            }
        });

        TicketRepo.getInstance().saveLinkedTicketList(ticketResponse.getTicketsList(),
                Constants.LINKED, threadId, new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        GlobalUtils.showLog(TAG, "saved linked tickets");
                        getView().getLinkedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG, "failed to save linked tickets");
                        getView().getLinkedTicketFail("Failed to save linked list");
                    }
                });
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
