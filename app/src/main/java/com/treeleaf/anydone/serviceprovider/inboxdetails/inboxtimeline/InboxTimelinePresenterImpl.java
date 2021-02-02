package com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.NotificationProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
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
    public void deleteParticipant(String inboxId, List<String> participantIds) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(String.valueOf(inboxId));
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> participantObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<InboxProto.InboxParticipant> participants = new ArrayList<>();
        for (String employeeId : participantIds
        ) {
            GlobalUtils.showLog(TAG, "employeeIds: " + employeeId);

            UserProto.EmployeeProfile profile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(employeeId)
                    .build();

            UserProto.User user = UserProto.User.newBuilder()
                    .setAccountType(AnydoneProto.AccountType.EMPLOYEE)
                    .setEmployee(profile)
                    .build();

            InboxProto.InboxParticipant participantAssigned = InboxProto.InboxParticipant.newBuilder()
                    .setUser(user)
                    .build();

            participants.add(participantAssigned);
        }

        InboxProto.Inbox inboxPb = InboxProto.Inbox.newBuilder()
                .setSubject(inbox.getSubject())
//                .setServiceId(inbox.getServiceId())
                .addAllParticipants(participants)
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
                        getView().onFailure(e.getLocalizedMessage());
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

                        InboxRepo.getInstance().deleteInbox(inboxId);
                        getView().onConversationLeaveSuccess();
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
