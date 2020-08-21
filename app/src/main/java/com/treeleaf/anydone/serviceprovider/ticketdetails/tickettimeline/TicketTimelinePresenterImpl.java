package com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
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
import io.realm.RealmList;

public class TicketTimelinePresenterImpl extends BasePresenter<TicketTimelineContract.TicketTimelineView>
        implements TicketTimelineContract.TicketTimelinePresenter {
    private static final String TAG = "TicketTimelinePresenter";
    private TicketTimelineRepository ticketTimelineRepository;

    @Inject
    public TicketTimelinePresenterImpl(TicketTimelineRepository ticketTimelineRepository) {
        this.ticketTimelineRepository = ticketTimelineRepository;
    }


    @Override
    public void getTicketTimeline(long ticketId) {
        getView().showProgressBar("Please wait...");
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
}
