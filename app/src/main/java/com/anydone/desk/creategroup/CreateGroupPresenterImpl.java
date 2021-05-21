package com.anydone.desk.creategroup;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.RtcProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Inbox;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.InboxRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ProtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CreateGroupPresenterImpl extends BasePresenter<CreateGroupContract.CreateGroupView>
        implements CreateGroupContract.CreateGroupPresenter {
    private static final String TAG = "CreateGroupPresenterImp";
    private CreateGroupRepository createGroupRepository;

    @Inject
    public CreateGroupPresenterImpl(CreateGroupRepository createGroupRepository) {
        this.createGroupRepository = createGroupRepository;
    }

    @Override
    public void createGroup(List<String> participants, String message, String subject, boolean
            isGroup, boolean isPrivate) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> inboxObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<InboxProto.InboxParticipant> participantsPb = new ArrayList<>();
        InboxProto.Inbox inboxPb;
        for (String employeeId : participants
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

            participantsPb.add(participantAssigned);
        }

        if (isGroup) {
            if (isPrivate) {
                inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                        .setSubject(subject)
                        .setType(InboxProto.Inbox.InboxType.PRIVATE_GROUP)
                        .addAllParticipants(participantsPb)
                        .setCreatedAt(System.currentTimeMillis())
                        .build();
            } else {
                inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                        .setSubject(subject)
                        .setType(InboxProto.Inbox.InboxType.PUBLIC_GROUP)
                        .addAllParticipants(participantsPb)
                        .setCreatedAt(System.currentTimeMillis())
                        .build();
            }
        } else {
            RtcProto.TextMessage textMessage = RtcProto.TextMessage.newBuilder()
                    .setMessage((message))
                    .setTextMessageType(RtcProto.TextMessage.TextMessageType.TEXT_TYPE)
                    .build();

            RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                    .setClientId(UUID.randomUUID().toString().replace("-", ""))
                    .setText(textMessage)
                    .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                    .build();

            if (subject != null && !subject.isEmpty()) {
                inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                        .setSubject(subject)
                        .setMessage(rtcMessage)
                        .setType(InboxProto.Inbox.InboxType.DIRECT_MESSAGE)
                        .addAllParticipants(participantsPb)
                        .setCreatedAt(System.currentTimeMillis())
                        .build();
            } else {
                inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                        .addAllParticipants(participantsPb)
                        .setMessage(rtcMessage)
                        .setType(InboxProto.Inbox.InboxType.DIRECT_MESSAGE)
                        .setCreatedAt(System.currentTimeMillis())
                        .build();
            }

        }

/*        if ((subject == null || subject.isEmpty()) && (message != null && !message.isEmpty())) {

            RtcProto.TextMessage textMessage = RtcProto.TextMessage.newBuilder()
                    .setMessage((message))
                    .setTextMessageType(RtcProto.TextMessage.TextMessageType.TEXT_TYPE)
                    .build();

            RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                    .setClientId(UUID.randomUUID().toString().replace("-", ""))
                    .setText(textMessage)
                    .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                    .build();

            inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                    .setMessage(rtcMessage)
                    .addAllParticipants(participantsPb)
                    .setCreatedAt(System.currentTimeMillis())
                    .build();

        } else if (message == null || message.isEmpty() && (subject != null && !subject.isEmpty())) {
            inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                    .setSubject(subject)
                    .addAllParticipants(participantsPb)
                    .setCreatedAt(System.currentTimeMillis())
                    .build();
        } else if (!message.isEmpty()) {
            RtcProto.TextMessage textMessage = RtcProto.TextMessage.newBuilder()
                    .setMessage((message))
                    .setTextMessageType(RtcProto.TextMessage.TextMessageType.TEXT_TYPE)
                    .build();

            RtcProto.RtcMessage rtcMessage = RtcProto.RtcMessage.newBuilder()
                    .setClientId(UUID.randomUUID().toString().replace("-", ""))
                    .setText(textMessage)
                    .setRtcMessageType(RtcProto.RtcMessageType.TEXT_RTC_MESSAGE)
                    .build();

            inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                    .setSubject(subject)
                    .setMessage(rtcMessage)
                    .addAllParticipants(participantsPb)
                    .setCreatedAt(System.currentTimeMillis())
                    .build();
        } else {
            inboxPb = InboxProto.Inbox.newBuilder()
//                    .setServiceId(selectedService)
                    .addAllParticipants(participantsPb)
                    .setCreatedAt(System.currentTimeMillis())
                    .build();
        }*/

        GlobalUtils.showLog(TAG, "inbox check: " + inboxPb);
        inboxObservable = service.createGroup(token, inboxPb);

        addSubscription(inboxObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                    @Override
                    public void onNext(@NonNull InboxRpcProto.InboxBaseResponse inboxBaseResponse) {
                        GlobalUtils.showLog(TAG, "create inbox response:"
                                + inboxBaseResponse);

                        getView().hideProgressBar();

                        if (inboxBaseResponse.getError()) {
                            getView().createGroupFail(inboxBaseResponse.getMsg());
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
    public void findParticipants() {
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
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse getEmployeeResponse) {
                        GlobalUtils.showLog(TAG, "find employees response:"
                                + getEmployeeResponse);

                        getView().hideProgressBar();

                        if (getEmployeeResponse.getError()) {
                            getView().getParticipantFail(getEmployeeResponse.getMsg());
                            return;
                        }

                        GlobalUtils.showLog(TAG, "get all emp check: " +
                                getEmployeeResponse.getEmployeesList().size());
                        List<AssignEmployee> assignEmployeeList = ProtoMapper
                                .transformEmployee(getEmployeeResponse.getEmployeesList());
                        saveEmployees(getEmployeeResponse.getEmployeesList());
                        Account userAccount = AccountRepo.getInstance().getAccount();
                        AssignEmployee self = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(userAccount.getAccountId());

                        assignEmployeeList.remove(self);
                        getView().getParticipantSuccess(assignEmployeeList);

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
    public void searchSubjects(String text) {
        GlobalUtils.showLog(TAG, "search subjects called()");
        Observable<InboxRpcProto.InboxBaseResponse> inboxBaseResponseObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);

        inboxBaseResponseObservable = anyDoneService.getExistingSubject(token, text);

        addSubscription(inboxBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                            @Override
                            public void onNext(@NonNull InboxRpcProto.InboxBaseResponse
                                                       inboxBaseResponse) {
                                GlobalUtils.showLog(TAG, "search subject response: "
                                        + inboxBaseResponse);

                                if (inboxBaseResponse.getError()) {
                                    getView().getSubjectFail(
                                            inboxBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "searched inbox list size: " +
                                        inboxBaseResponse.getInboxResponse().getInboxList().size());
                                List<Inbox> inboxList = ProtoMapper.transformInbox
                                        (inboxBaseResponse.getInboxResponse().getInboxList());

                                getView().getSubjectSuccess(inboxList);
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getSubjectFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveInboxDetails(InboxProto.Inbox inbox) {
        InboxRepo.getInstance().saveInbox(inbox, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().createGroupSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save ");
            }
        });
    }

    private void saveEmployees(List<UserProto.EmployeeProfile> employeesList) {
        AssignEmployeeRepo.getInstance().saveAssignEmployeeList(employeesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved assign employees");
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save assign employees");
            }
        });
    }
}
