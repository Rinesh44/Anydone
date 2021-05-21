package com.anydone.desk.inbox;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.model.Participant;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.realm.repo.ParticipantRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Retrofit;

public class InboxPresenterImpl extends BasePresenter<InboxContract.InboxView> implements
        InboxContract.InboxPresenter {
    private static final String TAG = "InboxPresenterImpl";
    private InboxRepository inboxRepository;

    @Inject
    public InboxPresenterImpl(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @Override
    public void getServices() {
        Observable<ServiceRpcProto.ServiceBaseResponse> servicesObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);

        servicesObservable = service.getServices(token);
        addSubscription(servicesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ServiceRpcProto.ServiceBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ServiceRpcProto.ServiceBaseResponse
                                                       getServicesBaseResponse) {
                                GlobalUtils.showLog(TAG, "get services response: "
                                        + getServicesBaseResponse);

                                if (getServicesBaseResponse.getError()) {
                                    getView().getServicesFail(getServicesBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        getServicesBaseResponse.getServicesList())) {
                                    saveAvailableServices(getServicesBaseResponse.
                                            getServicesList());
                                } else {
                                    getView().getServicesFail("Services Not found");
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getServicesFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }


    @Override
    public void getInboxMessages(boolean showProgress, long to) {
        GlobalUtils.showLog(TAG, "get inbox messages called()");
        if (showProgress)
            getView().showProgressBar("Please wait");

        Observable<InboxRpcProto.InboxBaseResponse> inboxBaseResponseObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);

        inboxBaseResponseObservable = anyDoneService.getInboxList(token, 0,
                100, "DESC");

        addSubscription(inboxBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                            @Override
                            public void onNext(@NonNull InboxRpcProto.InboxBaseResponse
                                                       inboxBaseResponse) {
                                GlobalUtils.showLog(TAG, "get inbox list response: "
                                        + inboxBaseResponse);

                                getView().hideProgressBar();

                                if (inboxBaseResponse.getError()) {
                                    getView().getInboxMessageFail(
                                            inboxBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "inbox list size: " +
                                        inboxBaseResponse.getInboxResponse().getInboxList().size());
                                if (!CollectionUtils.isEmpty(
                                        inboxBaseResponse.getInboxResponse().getInboxList())) {
                                    saveInboxList(inboxBaseResponse.getInboxResponse().getInboxList());
                                } else {
                                    getView().getInboxMessageFail("Not found");
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getInboxMessageFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveInboxList(List<InboxProto.Inbox> inboxList) {
/*        for (InboxProto.Inbox inbox : inboxList
        ) {
            GlobalUtils.showLog(TAG, "last msg:  " + inbox.getMessage().getText());
            GlobalUtils.showLog(TAG, "unread count: " + inbox.getUnreadMsgCount());
        }*/

        InboxRepo.getInstance().saveInboxes(inboxList,
                false, new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getInboxMessageSuccess();
                    }

                    @Override
                    public void fail() {
                        GlobalUtils.showLog(TAG,
                                "error on saving inbox list");
                    }
                });
    }

    private void saveAvailableServices(List<ServiceProto.Service> availableServicesList) {
        AvailableServicesRepo.getInstance().saveAvailableServices(availableServicesList,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getServicesSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getServicesFail("failed to get services");
                    }
                });
    }


    @Override
    public void leaveConversation(Inbox inbox) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.leaveConversation(token, String.valueOf(inbox.getInboxId()));

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "leave inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onConversationLeaveFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        Account user = AccountRepo.getInstance().getAccount();
                        Participant participant = ParticipantRepo.getInstance().getParticipantByEmployeeAccountId(user.getAccountId());
                        GlobalUtils.showLog(TAG, "check if contains participant: " + participant.getEmployee().getName());
                        Realm realm = Realm.getDefaultInstance();
                        RealmList<Participant> newParticipants = inbox.getParticipantList();
                        GlobalUtils.showLog(TAG, "before size: " + newParticipants.size());


                        realm.executeTransaction(realm1 -> {
                            Participant toRemove = null;
                            for (Participant self : newParticipants
                            ) {
                                if (self.getEmployee().getAccountId().equals(participant.getEmployee().getAccountId())) {
                                    toRemove = self;
                                }
                            }
                            newParticipants.remove(toRemove);
                        });

                        GlobalUtils.showLog(TAG, "after size: " + newParticipants.size());
                        InboxRepo.getInstance().leaveGroup(inbox.getInboxId(), newParticipants, new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                getView().onConversationLeaveSuccess(inbox);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to leave group");
                            }
                        });

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

    public void leaveAndDeleteConversation(Inbox inbox) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.leaveAndDeleteInbox(token, String.valueOf(inbox.getInboxId()));

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "leave and delete inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onConversationDeleteFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        getView().onConversationDeleteSuccess(inbox);
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
    public void convertToGroup(Inbox inbox, String groupName) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> participantObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<InboxProto.InboxParticipant> participants = new ArrayList<>();
        for (Participant participant : inbox.getParticipantList()
        ) {
            UserProto.EmployeeProfile profile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(participant.getEmployee().getEmployeeId())
                    .build();

            UserProto.User user = UserProto.User.newBuilder()
                    .setAccountType(AnydoneProto.AccountType.EMPLOYEE)
                    .setEmployee(profile)
                    .build();

            InboxProto.InboxParticipant.InboxRole role = InboxProto.InboxParticipant.InboxRole.valueOf(participant.getRole());
            GlobalUtils.showLog(TAG, "role check: " + role);
            InboxProto.InboxParticipant participantAssigned = InboxProto.InboxParticipant.newBuilder()
                    .setUser(user)
                    .setParticipantId(participant.getParticipantId())
                    .setRole(role)
                    .build();

            participants.add(participantAssigned);
        }

        InboxProto.Inbox.InboxType inboxType = InboxProto.Inbox.InboxType.PRIVATE_GROUP;
        InboxProto.Inbox inboxPb = InboxProto.Inbox.newBuilder()
                .setSubject(groupName)
                .setType(inboxType)
//                .setServiceId(inbox.getServiceId())
                .addAllParticipants(participants)
                .build();

        GlobalUtils.showLog(TAG, "inbox check: " + inboxPb);

        participantObservable = service.updateInbox(token, String.valueOf(inbox.getInboxId()), inboxPb);

        addSubscription(participantObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "convert to group response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onConvertToGroupFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        InboxRepo.getInstance().convertInboxTypeToPrivateGroup(inbox.getInboxId(),
                                groupName, new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        getView().onConvertToGroupSuccess(inbox);
                                    }

                                    @Override
                                    public void fail() {
                                        GlobalUtils.showLog(TAG, "failed to convert to group");
                                    }
                                });
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
    public void joinGroup(String inboxId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.joinGroup(token, inboxId);

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "join group response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onJoinGroupFail(inboxBaseResponse.getMsg());
                            return;
                        }


                        InboxRepo.getInstance().saveInbox(inboxBaseResponse.getInbox(), new Repo.Callback() {
                            @Override
                            public void success(Object o) {
                                getView().onJoinGroupSuccess(inboxId);
                            }

                            @Override
                            public void fail() {
                                GlobalUtils.showLog(TAG, "failed to save joined inbox det");
                            }
                        });
//                        getView().onJoinGroupSuccess(inboxId);
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
    public void searchInbox(String query) {

    }

    //    @Override
    public Observable<InboxRpcProto.InboxBaseResponse> findInbox(String query) {
        GlobalUtils.showLog(TAG, "search inbox called()");
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.searchInbox(token, query);

        return inboxObservable;

//        addSubscription(inboxObservable
//                .debounce(300, TimeUnit.MILLISECONDS)
//                .distinctUntilChanged()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
//                    @Override
//                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
//                        GlobalUtils.showLog(TAG, "search inbox response:"
//                                + inboxBaseResponse);
//
//                        getView().hideProgressBar();
//
//                        if (inboxBaseResponse.getError()) {
//                            getView().searchInboxFail(inboxBaseResponse.getMsg());
//                            return;
//                        }
//
//
//                        GlobalUtils.showLog(TAG, "search inbox succeded");
//                        RealmList<Inbox> searchedList = ProtoMapper.transformInbox(inboxBaseResponse.getInboxResponse().getInboxList());
//                        GlobalUtils.showLog(TAG, "search list size: " + searchedList.size());
//                        getView().searchInboxSuccess(searchedList);
////                        getView().onJoinGroupSuccess(inboxId);
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//                        getView().hideProgressBar();
//                        getView().onFailure(e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                    }
//                }));
    }

    @Override
    public void muteInboxNotification(String inboxId, boolean mentions) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        InboxProto.UpdateInboxNotificationRequest updateInboxNotificationRequest;
        if (mentions) {
            updateInboxNotificationRequest = InboxProto.UpdateInboxNotificationRequest.newBuilder()
                    .setInboxId(inboxId)
                    .setNotificationType(InboxProto.InboxNotificationType.MENTIONS_INBOX_NOTIFICATION)
                    .build();
        } else {
            updateInboxNotificationRequest = InboxProto.UpdateInboxNotificationRequest.newBuilder()
                    .setInboxId(inboxId)
                    .setNotificationType(InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION)
                    .build();
        }

        inboxObservable = service.muteInbox(token, updateInboxNotificationRequest);

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "mute inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onMuteNotificationFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        if (mentions) {
                            InboxRepo.getInstance().changeMuteStatus(inboxId,
                                    InboxProto.InboxNotificationType.MENTIONS_INBOX_NOTIFICATION.name());
                        } else {
                            InboxRepo.getInstance().changeMuteStatus(inboxId,
                                    InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name());
                        }
                        getView().onMuteNotificationSuccess(inboxId);
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
    public void unMuteNotification(String inboxId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        InboxProto.UpdateInboxNotificationRequest updateInboxNotificationRequest =
                InboxProto.UpdateInboxNotificationRequest.newBuilder()
                        .setInboxId(inboxId)
                        .setNotificationType(InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION)
                        .build();

        inboxObservable = service.muteInbox(token, updateInboxNotificationRequest);

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "unmute inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onUnMuteFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        InboxRepo.getInstance().changeMuteStatus(inboxId,
                                InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name());
                        getView().onUnMuteSuccess(inboxId);
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
}
