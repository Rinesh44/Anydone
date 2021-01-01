package com.treeleaf.anydone.serviceprovider.ownedtickets;

import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.model.Priority;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class OwnedTicketPresenterImpl extends BasePresenter<OwnedTicketContract.OwnedTicketView>
        implements OwnedTicketContract.OwnedTicketPresenter {

    private static final String TAG = "OwnedTicketPresenterImp";
    private OwnedTicketRepository ownedTicketRepository;

    @Inject
    public OwnedTicketPresenterImpl(OwnedTicketRepository ownedTicketRepository) {
        this.ownedTicketRepository = ownedTicketRepository;
    }


    @Override
    public void getOwnedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getOwnedTickets(token,
                serviceId, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get owned tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getOwnedTicketFail("Get owned" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getOwnedTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "owned tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                saveOwnedTicket(getTicketsBaseResponse.getTicketsList());
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getOwnedTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveOwnedTicket(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> ownedTickets = TicketRepo.getInstance().getOwnedTickets();
        if (!CollectionUtils.isEmpty(ownedTickets)) {
            TicketRepo.getInstance().deleteOpenTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete owned tickets");
                }
            });
        }

        saveTickets(ticketsList);
    }

    @Override
    public void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String filterUrl = getFilterUrl(searchQuery, from, to, ticketState, priorityNum);

        if (!filterUrl.isEmpty()) {
            getView().showProgressBar("Filtering...");
            ticketBaseResponseObservable = service.filterTickets(token, filterUrl);
            addSubscription(ticketBaseResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(
                            new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                                @Override
                                public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                           filterTicketBaseResponse) {
                                    GlobalUtils.showLog(TAG, "filter open ticket response: "
                                            + filterTicketBaseResponse);

                                    getView().hideProgressBar();
                                    if (filterTicketBaseResponse == null) {
                                        getView().filterTicketsFailed("Filter open ticket failed");
                                        return;
                                    }

                                    if (filterTicketBaseResponse.getError()) {
                                        getView().filterTicketsFailed(filterTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterTicketBaseResponse.getTicketsList())) {
                                        List<Tickets> filteredTickets = TicketRepo.
                                                getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(), Constants.OPEN);
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
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.OWNED,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getOwnedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getOwnedTicketFail("failed to save owned tickets");
                        GlobalUtils.showLog(TAG, "failed to save owned tickets");
                    }
                });
    }

    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private String getFilterUrl(String query, long from, long to, int status, int priority) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/me/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1) {
            Toast.makeText(getContext(), "Please enter filter terms", Toast.LENGTH_SHORT).show();
            return "";
        }

        if (!query.isEmpty()) {
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
        if (priority != -1) {
            filterUrlBuilder.append("&priority=");
            filterUrlBuilder.append(priority);
        }
        return filterUrlBuilder.toString();
    }
}
