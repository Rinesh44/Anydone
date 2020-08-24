package com.treeleaf.anydone.serviceprovider.tickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class TicketsPresenterImpl extends BasePresenter<TicketsContract.TicketsView> implements
        TicketsContract.TicketsPresenter {
    private static final String TAG = "TicketsPresenterImpl";

    private TicketsRepository ticketsRepository;

    @Inject
    public TicketsPresenterImpl(TicketsRepository ticketsRepository) {
        this.ticketsRepository = ticketsRepository;
    }

    @Override
    public void filterAssignedTickets(String searchQuery, long from, long to, int ticketState) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String filterUrl = getAssignedFilterUrl(searchQuery, from, to, ticketState);

        ticketBaseResponseObservable = service.filterTickets(token, filterUrl);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter assigned ticket response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().filterAssignedTicketsFailed("Filter assigned ticket failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().filterAssignedTicketsFailed(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        filterTicketBaseResponse.getTicketsList())) {
                                    List<Tickets> filteredAssignedTickets = TicketRepo.
                                            getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(),
                                            Constants.ASSIGNED);

                                    getView().updateAssignedTicketList(filteredAssignedTickets);
                                } else {
                                    getView().filterAssignedTicketsFailed("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().filterAssignedTicketsFailed(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterSubscribedTickets(String searchQuery, long from, long to, int ticketState) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String filterUrl = getSubscribedFilterUrl(searchQuery, from, to, ticketState);

        ticketBaseResponseObservable = service.filterTickets(token, filterUrl);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter subscribed ticket response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().filterSubscribedTicketFailed("Filter subscribed ticket failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().filterSubscribedTicketFailed(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        filterTicketBaseResponse.getTicketsList())) {
                                    List<Tickets> filteredTickets = TicketRepo.
                                            getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(),
                                            Constants.SUBSCRIBED);

                                    getView().updateSubscribedTicketList(filteredTickets);
                                } else {
                                    getView().filterSubscribedTicketFailed("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().filterSubscribedTicketFailed(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterClosedTickets(String searchQuery, long from, long to, int ticketState) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String filterUrl = getClosedFilterUrl(searchQuery, from, to, ticketState);

        ticketBaseResponseObservable = service.filterTickets(token, filterUrl);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter closed ticket response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().filterClosedTicketFailed("Filter closed ticket failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().filterClosedTicketFailed(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        filterTicketBaseResponse.getTicketsList())) {
                                    List<Tickets> filteredTickets = TicketRepo.
                                            getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(),
                                            Constants.CLOSED_RESOLVED);

                                    getView().updateClosedTicketList(filteredTickets);
                                } else {
                                    getView().filterClosedTicketFailed("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().filterClosedTicketFailed(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private String getAssignedFilterUrl(String query, long from, long to, int status) {
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/assigned?");
        if (query != null && !query.isEmpty()) {
            filterUrlBuilder.append("query=");
            filterUrlBuilder.append(query);
        }
        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }
        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }
        if (status != -1) {
            filterUrlBuilder.append("&state=");
            filterUrlBuilder.append(status);
        }
        return filterUrlBuilder.toString();
    }

    private String getSubscribedFilterUrl(String query, long from, long to, int status) {
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/subscribed?");
        if (query != null && !query.isEmpty()) {
            filterUrlBuilder.append("query=");
            filterUrlBuilder.append(query);
        }
        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }
        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }
        if (status != -1) {
            filterUrlBuilder.append("&state=");
            filterUrlBuilder.append(status);
        }
        return filterUrlBuilder.toString();
    }

    private String getClosedFilterUrl(String query, long from, long to, int status) {
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/inactive?");
        if (query != null && !query.isEmpty()) {
            filterUrlBuilder.append("query=");
            filterUrlBuilder.append(query);
        }
        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }
        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }
        if (status != -1) {
            filterUrlBuilder.append("&state=");
            filterUrlBuilder.append(status);
        }
        return filterUrlBuilder.toString();
    }


    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.anydone.net/")
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }
}
