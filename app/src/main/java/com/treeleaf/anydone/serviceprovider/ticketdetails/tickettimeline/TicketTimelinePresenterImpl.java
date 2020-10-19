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
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
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
import io.realm.RealmList;
import retrofit2.Retrofit;

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

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

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


        getTicketsObservable = service.assignEmployee(token, ticketId, ticket);
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        rtcBaseResponseObservable = service.enableTicketBotReply(token, ticketId);

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

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        rtcBaseResponseObservable = service.disableTicketBotReply(token, ticketId);

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
    public void editTicketPriority(String ticketId, int priority) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        Tickets tickets = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));
        TicketProto.TicketType ticketType = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(tickets.getTicketCategoryId())
                .build();

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setTitle(tickets.getTitle())
                .setPriority(getTicketPriority(priority))
                .setDescription(tickets.getDescription())
                .setType(ticketType)
                .build();

        ticketObservable = service.editTicket(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit priority response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onPriorityEditFail("Failed to edit priority");
                            return;
                        }

                        if (response.getError()) {
                            getView().onPriorityEditFail(response.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().editTicketPriority(Long.parseLong(ticketId),
                                priority);
                        getView().onPriorityEditSuccess(priority);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onPriorityEditFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void editTeam(String ticketId, List<String> tags) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        List<TicketProto.Team> teamList = setTeams(tags);

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addAllTeams(teamList)
                .build();

        ticketObservable = service.editTeam(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit team response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onEditTeamFail("Failed to edit team");
                            return;
                        }

                        if (response.getError()) {
                            getView().onEditTeamFail(response.getMsg());
                            return;
                        }

                        getView().onEditTeamSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onEditTeamFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private List<TicketProto.Team> setTeams(List<String> tags) {
        List<TicketProto.Team> teamList = new ArrayList<>();
        for (String teamId : tags
        ) {
            TicketProto.Team team = TicketProto.Team.newBuilder()
                    .setTeamId(teamId)
                    .build();

            teamList.add(team);
        }
        return teamList;
    }

    @Override
    public void editLabel(String ticketId, List<Label> labels) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        List<TicketProto.Label> labelList = setLabels(labels);


        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .addAllLabel(labelList)
                .build();

        GlobalUtils.showLog(TAG, "edited labels: " + ticket);

        ticketObservable = service.editLabel(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit label response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onEditLabelFail("Failed to edit label");
                            return;
                        }

                        if (response.getError()) {
                            getView().onEditLabelFail(response.getMsg());
                            return;
                        }

                        getView().onEditLabelSuccess();

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onEditLabelFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private List<TicketProto.Label> setLabels(List<Label> labels) {
        List<TicketProto.Label> labelList = new ArrayList<>();
        for (Label label : labels
        ) {
            TicketProto.Label labelPb = TicketProto.Label.newBuilder()
                    .setLabelId(label.getLabelId())
                    .setName(label.getName())
                    .build();

            labelList.add(labelPb);
        }

        return labelList;
    }

    @Override
    public void editTicketType(String ticketId, String ticketTypeId, String ticketType) {
        getView().showProgressBar("Please wait");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        TicketProto.TicketType ticketTypePb = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(ticketTypeId)
                .build();

        Tickets tickets = TicketRepo.getInstance().getTicketById(Long.parseLong(ticketId));
        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setTitle(tickets.getTitle())
                .setPriority(getTicketPriority(tickets.getPriority()))
                .setDescription(tickets.getDescription())
                .setType(ticketTypePb)
                .build();

        ticketObservable = service.editTicket(token, ticketId, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "edit title response:"
                                + response);

                        getView().hideProgressBar();
                        if (response == null) {
                            getView().onTicketTypeEditFail("Failed to edit ticket type");
                            return;
                        }

                        if (response.getError()) {
                            getView().onTicketTypeEditFail(response.getMsg());
                            return;
                        }

                        TicketRepo.getInstance().editTicketType(Long.parseLong(ticketId), ticketType);
                        getView().onTicketTypeEditSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onTicketTypeEditFail(e.getLocalizedMessage());
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getTicketTimeline(token,
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(contributorId)
                .build();

        TicketProto.TicketContributor ticketContributor = TicketProto.TicketContributor.newBuilder()
                .setEmployee(employeeProfile)
                .build();

        GlobalUtils.showLog(TAG, "delete contributor: " + ticketContributor);

        ticketObservable = service.deleteContributor(token,
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.closeTicket(token,
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.reopenTicket(token,
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
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.resolveTicket(token,
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

    private TicketProto.TicketPriority getTicketPriority(int priority) {
        switch (priority) {
            case 1:
                return TicketProto.TicketPriority.LOWEST_TICKET_PRIORITY;

            case 2:
                return TicketProto.TicketPriority.LOW_TICKET_PRIORITY;

            case 3:
                return TicketProto.TicketPriority.MEDIUM_TICKET_PRIORITY;

            case 4:
                return TicketProto.TicketPriority.HIGH_TICKET_PRIORITY;

            case 5:
                return TicketProto.TicketPriority.HIGHEST_TICKET_PRIORITY;
        }
        return TicketProto.TicketPriority.UNKNOWN_TICKET_PRIORITY;
    }

}
