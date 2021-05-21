package com.anydone.desk.inboxdetails.inboxtimeline;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.model.Participant;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.realm.repo.ParticipantRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Retrofit;

public class InboxTimelinePresenterImpl extends BasePresenter<InboxTimelineContract.InboxTimelineView>
        implements InboxTimelineContract.InboxTimelinePresenter {
    private static final String TAG = "InboxTimelinePresenterI";
    private InboxTimelineRepository inboxTimelineRepository;


    @Inject
    public InboxTimelinePresenterImpl(InboxTimelineRepository inboxTimelineRepository) {
        this.inboxTimelineRepository = inboxTimelineRepository;
    }

    @Override
    public void addParticipants(String inboxId, List<String> employeeIds) {

    }

    @Override
    public void getInboxById(String inboxId) {

    }

    @Override
    public void deleteParticipant(String inboxId, List<Participant> participants) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(String.valueOf(inboxId));
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> participantObservable;
        String token = Hawk.get(Constants.TOKEN);

        GlobalUtils.showLog(TAG, "inboxId: check: " + inboxId);

        List<InboxProto.InboxParticipant> participantPb = new ArrayList<>();
        for (Participant participant : participants
        ) {

            UserProto.EmployeeProfile profile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(participant.getEmployee().getEmployeeId())
                    .build();

            UserProto.User user = UserProto.User.newBuilder()
                    .setAccountType(AnydoneProto.AccountType.EMPLOYEE)
                    .setEmployee(profile)
                    .build();

        /*    Participant participant = ParticipantRepo.getInstance().getParticipantByEmployeeId(employeeId);
            GlobalUtils.showLog(TAG, "participants: " + participant.getEmployee().getName());
            GlobalUtils.showLog(TAG, "participants ids: " + participant.getParticipantId());
            InboxProto.InboxParticipant.InboxRole role = InboxProto.InboxParticipant.InboxRole.valueOf(participant.getRole());
            GlobalUtils.showLog(TAG, "role check; " + role);*/

            InboxProto.InboxParticipant.InboxRole role = InboxProto.InboxParticipant.InboxRole.valueOf(participant.getRole());

            InboxProto.InboxParticipant participantAssigned = InboxProto.InboxParticipant.newBuilder()
                    .setUser(user)
                    .setRole(role)
                    .setParticipantId(participant.getParticipantId())
                    .build();

            participantPb.add(participantAssigned);
        }

        InboxProto.Inbox.InboxType inboxType = InboxProto.Inbox.InboxType.valueOf(inbox.getInboxType());
        InboxProto.Inbox inboxPb = InboxProto.Inbox.newBuilder()
                .setSubject(inbox.getSubject())
                .setType(inboxType)
//                .setServiceId(inbox.getServiceId())
                .addAllParticipants(participantPb)
                .build();

        GlobalUtils.showLog(TAG, "inbox check: " + inboxPb);

        participantObservable = service.updateInbox(token, String.valueOf(inboxId), inboxPb);

        addSubscription(participantObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "update inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().addParticipantFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        saveInboxDetails(inboxBaseResponse.getInbox());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().deleteParticipantFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void leaveConversation(String inboxId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.leaveConversation(token, String.valueOf(inboxId));

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
                        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
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
                                getView().onConversationLeaveSuccess();
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


    @Override
    public void convertToGroup(Inbox inbox, String subject) {
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
                .setSubject(subject)
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
                            getView().convertToGroupFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        InboxRepo.getInstance().convertInboxTypeToPrivateGroup(inbox.getInboxId(),
                                subject, new Repo.Callback() {
                                    @Override
                                    public void success(Object o) {
                                        getView().convertToGroupSuccess();
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
    public void leaveAndDeleteConversation(String inboxId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        inboxObservable = service.leaveAndDeleteInbox(token, String.valueOf(inboxId));

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "leave  and delete inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().onConversationDeleteFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        InboxRepo.getInstance().deleteInbox(inboxId);
                        getView().onConversationDeleteSuccess();
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
                        getView().onMuteNotificationSuccess();
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

        InboxProto.UpdateInboxNotificationRequest updateInboxNotificationRequest = InboxProto.UpdateInboxNotificationRequest.newBuilder()
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
                        getView().onUnMuteSuccess();
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
    public void updateParticipantNotification(String inboxId, String participantId,
                                              List<Participant> participantList, boolean mute) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        Map<String, InboxProto.InboxNotificationType> participantMap = new HashMap<>();
        if (mute) {
            for (Participant participant : participantList
            ) {
                if (!participant.getParticipantId().equalsIgnoreCase(participantId)) {
                    Account userAccount = AccountRepo.getInstance().getAccount();
                    if (!userAccount.getAccountId().equalsIgnoreCase(participant.getEmployee().getAccountId())) {
                        switch (participant.getNotificationType()) {
                            case "EVERY_NEW_MESSAGE_INBOX_NOTIFICATION":
                                participantMap.put(participant.getParticipantId(),
                                        InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION);
                                break;

                            case "MUTED_INBOX_NOTIFICATION":
                                participantMap.put(participant.getParticipantId(),
                                        InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION);
                                break;
                        }
                    }
                } else {
                    participantMap.put(participant.getParticipantId(),
                            InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION);
                }
            }
        } else {
            for (Participant participant : participantList
            ) {
                if (!participant.getParticipantId().equalsIgnoreCase(participantId)) {
                    Account userAccount = AccountRepo.getInstance().getAccount();
                    if (!userAccount.getAccountId().equalsIgnoreCase(participant.getEmployee().getAccountId())) {
                        switch (participant.getNotificationType()) {
                            case "EVERY_NEW_MESSAGE_INBOX_NOTIFICATION":
                                participantMap.put(participant.getParticipantId(),
                                        InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION);
                                break;

                            case "MUTED_INBOX_NOTIFICATION":
                                participantMap.put(participant.getParticipantId(),
                                        InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION);
                                break;
                        }
                    }
                } else {
                    participantMap.put(participant.getParticipantId(),
                            InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION);
                }
            }
        }
        InboxProto.UpdateInboxNotificationRequest updateInboxNotificationRequest =
                InboxProto.UpdateInboxNotificationRequest.newBuilder()
                        .setInboxId(inboxId)
                        .putAllParticipant(participantMap)
                        .build();

        GlobalUtils.showLog(TAG, "check update noti: " + updateInboxNotificationRequest);

        inboxObservable = service.updateParticipantNotification(token, updateInboxNotificationRequest);

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "update participant noti response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().updateParticipantNotificationFail(inboxBaseResponse.getMsg());
                            return;
                        }

                        String notificationType;
                        if (mute) {
                            notificationType = InboxProto.InboxNotificationType.MUTED_INBOX_NOTIFICATION.name();
                        } else {
                            notificationType = InboxProto.InboxNotificationType.EVERY_NEW_MESSAGE_INBOX_NOTIFICATION.name();
                        }
                        getView().updateParticipantNotificationSuccess(participantId, notificationType);
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

    private void saveInboxDetails(InboxProto.Inbox inbox) {
        InboxRepo.getInstance().saveInbox(inbox, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().deleteParticipantSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save updated participants");
            }
        });
    }
}
