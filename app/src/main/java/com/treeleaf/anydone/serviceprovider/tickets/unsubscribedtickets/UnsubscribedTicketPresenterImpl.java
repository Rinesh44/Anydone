package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
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

public class UnsubscribedTicketPresenterImpl extends BasePresenter<UnsubscribedTicketContract.UnsubscribedView>
        implements UnsubscribedTicketContract.UnsubscribedPresenter {

    private static final String TAG = "UnsubscribedTicketPrese";
    private UnsubscribedTicketRepository unsubscribedTicketRepository;

    @Inject
    public UnsubscribedTicketPresenterImpl(UnsubscribedTicketRepository unsubscribedTicketRepository) {
        this.unsubscribedTicketRepository = unsubscribedTicketRepository;
    }

    @Override
    public void getSubscribeableTickets(long from, long to, int pageSize) {
        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = unsubscribedTicketRepository.getSubscribeableTickets(token, from, to, pageSize);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get subscribeable tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getSubscribeableTicketFail("Get subscribeable tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getSubscribeableTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                saveSubscribeableTicketsToRealm(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getSubscribeableTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void subscribe(long ticketId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = unsubscribedTicketRepository.subscribe(token, ticketId);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "subscribe tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().onSubscribeFail("subscribe ticket failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().onSubscribeFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                getView().onSubscribeSuccess();
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onSubscribeFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterTickets(String searchQuery, long from, long to, int ticketState) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String filterUrl = getFilterUrl(searchQuery, from, to, ticketState);

        ticketBaseResponseObservable = service.filterTickets(token, filterUrl);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter subscribeable ticket response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().filterTicketsFailed("Filter subscribeable ticket failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().filterTicketsFailed(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        filterTicketBaseResponse.getTicketsList())) {
                                    List<Tickets> filteredTickets = TicketRepo.
                                            getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(),
                                            Constants.SUBSCRIBEABLE);
                                    getView().updateTickets(filteredTickets);
                                } else {
                                    getView().filterTicketsFailed("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().filterTicketsFailed(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveSubscribeableTicketsToRealm(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.SUBSCRIBEABLE, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getSubscribeableTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save subscribeable tickets");
                getView().getSubscribeableTicketFail("Failed to save subscribeable tickets");
            }
        });
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

    private String getFilterUrl(String query, long from, long to, int status) {
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/subscribable?");
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
}
