package com.treeleaf.anydone.serviceprovider.assignedtickets;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AssignedTicketPresenterImpl extends BasePresenter<AssignedTicketContract.AssignedTicketsView>
        implements AssignedTicketContract.AssignedTicketsPresenter {
    private static final String TAG = "AssignedTicketPresenter";
    private AssignedTicketRepository assignedTicketRepository;

    @Inject
    public AssignedTicketPresenterImpl(AssignedTicketRepository assignedTicketRepository) {
        this.assignedTicketRepository = assignedTicketRepository;
    }


    @Override
    public void getAssignedTickets(boolean showProgress) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = assignedTicketRepository.getTickets(token);
        addSubscription(getTicketsObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(
                                new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                                    @Override
                                    public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                               getTicketsBaseResponse) {
                                        GlobalUtils.showLog(TAG, "get tickets response: "
                                                + getTicketsBaseResponse);

                                        getView().hideProgressBar();
                                        if (getTicketsBaseResponse == null) {
                                            getView().getAssignedTicketsFail(
                                                    "Get tickets failed");
                                            return;
                                        }

                                        if (getTicketsBaseResponse.getError()) {
                                            getView().getAssignedTicketsFail(
                                                    getTicketsBaseResponse.getMsg());
                                            return;
                                        }

//                                saveServiceOrderToRealm(getTicketsBaseResponse.getServiceOrdersList());
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        getView().hideProgressBar();
                                        getView().getAssignedTicketsFail(e.getLocalizedMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        getView().hideProgressBar();
                                    }
                                })
        );
    }

    @Override
    public void separateOpenAndClosedTickets(List<Tickets> ticketsList, int fragmentIndex,
                                             boolean filter) {

    }
}

