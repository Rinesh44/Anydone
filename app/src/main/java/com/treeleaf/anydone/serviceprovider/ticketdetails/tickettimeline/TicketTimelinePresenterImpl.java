package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmList;

public class TicketTimelinePresenterImpl extends BasePresenter<TicketTimelineContract.TicketTimelineView>
        implements TicketTimelineContract.TicketTimelinePresenter{
    private static final String TAG = "TicketTimelinePresenter";
    private TicketTimelineRepository ticketTimelineRepository;

    @Inject
    public TicketTimelinePresenterImpl(TicketTimelineRepository ticketTimelineRepository) {
        this.ticketTimelineRepository = ticketTimelineRepository;
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

                        GlobalUtils.showLog(TAG, "presenter assigned emp check: " + timelineResponse.getTicket().getEmployeesAssignedList().size());
                        if (!CollectionUtils.isEmpty(timelineResponse.getTicket().getEmployeesAssignedList())) {
                            RealmList<Employee> assignedEmployee = ProtoMapper.
                                    transformAssignedEmployee(timelineResponse.getTicket().
                                            getEmployeesAssignedList());

                            getView().getTicketTimelineSuccess(assignedEmployee);
                        }

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

}
