package com.treeleaf.anydone.serviceprovider.addparticipant;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;
import retrofit2.Retrofit;

public class AddParticipantPresenterImpl extends BasePresenter<AddParticipantContract.AddParticipantView>
        implements AddParticipantContract.AddParticipantPresenter {

    private static final String TAG = "AddParticipantPresenter";
    private AddParticipantRepository addParticipantRepository;

    @Inject
    public AddParticipantPresenterImpl(AddParticipantRepository addParticipantRepository) {
        this.addParticipantRepository = addParticipantRepository;
    }

    @Override
    public void addParticipant(String inboxId, List<String> employeeIds) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(String.valueOf(inboxId));
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<InboxRpcProto.InboxBaseResponse> participantObservable;
        String token = Hawk.get(Constants.TOKEN);

        GlobalUtils.showLog(TAG, "emp count:  " + employeeIds.size());
        List<InboxProto.InboxParticipant> participants = new ArrayList<>();
        for (String employeeId : employeeIds
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
                    .setRole(InboxProto.InboxParticipant.InboxRole.INBOX_ADMIN)
                    .setUser(user)
//                    .setParticipantId(participant.getParticipantId())
                    .build();

            participants.add(participantAssigned);
        }

        for (Participant participant : inbox.getParticipantList()
        ) {
            UserProto.EmployeeProfile profile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(participant.getEmployee().getEmployeeId())
                    .build();

            UserProto.User user = UserProto.User.newBuilder()
                    .setAccountType(AnydoneProto.AccountType.EMPLOYEE)
                    .setEmployee(profile)
                    .build();

            InboxProto.InboxParticipant participantAssigned = InboxProto.InboxParticipant.newBuilder()
                    .setRole(InboxProto.InboxParticipant.InboxRole.INBOX_ADMIN)
                    .setUser(user)
                    .setParticipantId(participant.getParticipantId())
//                    .setParticipantId(participant.getParticipantId())
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

    private void saveInboxDetails(InboxProto.Inbox inbox) {
        InboxRepo.getInstance().saveInbox(inbox, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().addParticipantSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save updated participants");
            }
        });
    }

    @Override
    public void findParticipants(List<AssignEmployee> employeeList) {
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
                        assignEmployeeList.removeAll(employeeList);
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

}