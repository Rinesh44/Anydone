package com.treeleaf.anydone.serviceprovider.opentickets;

import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
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

public class OpenTicketPresenterImpl extends BasePresenter<OpenTicketContract.OpenTicketView>
        implements OpenTicketContract.OpenTicketPresenter {

    private static final String TAG = "OpenTicketPresenterImpl";
    private OpenTicketRepository openTicketRepository;

    @Inject
    public OpenTicketPresenterImpl(OpenTicketRepository openTicketRepository) {
        this.openTicketRepository = openTicketRepository;
    }

    @Override
    public void getOpenTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getOpenTickets(token,
                serviceId, page);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get open tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getOpenTicketFail("Get open" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getOpenTicketFail(getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "open tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (getTicketsBaseResponse.getTicketsList().size() > 0)
                                    saveOpenTickets(getTicketsBaseResponse.getTicketsList());
                                else getView().getOpenTicketFail("No tickets found");

                         /*       for (TicketProto.Ticket ticketpb : getTicketsBaseResponse.getTicketsList()
                                ) {
                                    if (ticketpb.getTicketIndex() == 163) {
                                        GlobalUtils.showLog(TAG, "check ticket index 163: " + ticketpb);
                                    }
                                }*/

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getOpenTicketFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterTickets(String searchQuery, long from, long to, int ticketState, Priority priority,
                              AssignEmployee selectedEmp,
                              TicketCategory selectedTicketType, Tags selectedTeam,
                              Service selectedService) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String filterUrl = getFilterUrl(searchQuery, from, to, ticketState, priorityNum,
                selectedEmp, selectedTicketType, selectedTeam, selectedService);

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

    private void saveOpenTickets(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> openTickets = TicketRepo.getInstance().getOpenTickets();
        if (!CollectionUtils.isEmpty(openTickets)) {
            TicketRepo.getInstance().deleteOpenTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete open tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.OPEN,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getOpenTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getOpenTicketFail("Tickets not found");
                        GlobalUtils.showLog(TAG, "failed to save open tickets");
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

    private String getFilterUrl(String query, long from, long to, int status, int priority,
                                AssignEmployee selectedEmp, TicketCategory
                                        selectedTicketCategory, Tags selectedTeam,
                                Service selectedService) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/me/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1
                && selectedEmp == null && selectedTicketCategory == null && selectedTeam == null &&
                selectedService == null) {
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

        if (selectedEmp != null && !selectedEmp.getEmployeeId().isEmpty()) {
            filterUrlBuilder.append("&employeeId=");
            filterUrlBuilder.append(selectedEmp.getEmployeeId());
        }

        if (selectedTicketCategory != null && !selectedTicketCategory.getCategoryId().isEmpty()) {
            filterUrlBuilder.append("&type=");
            filterUrlBuilder.append(selectedTicketCategory.getCategoryId());
        }

        if (selectedTeam != null && !selectedTeam.getTagId().isEmpty()) {
            filterUrlBuilder.append("&team=");
            filterUrlBuilder.append(selectedTeam.getTagId());
        }

        filterUrlBuilder.append("&sort=DESC");
        return filterUrlBuilder.toString();
    }

}
