package com.anydone.desk.customertickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CustomerTicketPresenterImpl extends BasePresenter<CustomerTicketContract.CustomerTicketView>
        implements CustomerTicketContract.CustomerTicketPresenter {

    private static final String TAG = "CustomerTicketPresenter";
    private CustomerTicketRepository customerTicketRepository;

    @Inject
    public CustomerTicketPresenterImpl(CustomerTicketRepository customerTicketRepository) {
        this.customerTicketRepository = customerTicketRepository;
    }

    @Override
    public void getCustomerTickets(String customerId, long from, long to, int page) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getCustomerTickets(token,
                serviceId, customerId, from, to, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get customer tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getCustomerTicketFail("Get customer" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getCustomerTicketFail((getTicketsBaseResponse.getMsg()));
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "customer tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                saveTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getCustomerTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> customerTickets = TicketRepo.getInstance().getCustomerTickets();
        if (CollectionUtils.isEmpty(customerTickets)) {
            saveTickets(ticketsList);
        } else {
            TicketRepo.getInstance().deleteCustomerTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {

                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete customer tickets");
                }
            });

            saveTickets(ticketsList);
        }
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.CUSTOMER,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getCustomerTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getCustomerTicketFail("failed to save customer tickets");
                        GlobalUtils.showLog(TAG, "failed to save customer tickets");
                    }
                });
    }
}
