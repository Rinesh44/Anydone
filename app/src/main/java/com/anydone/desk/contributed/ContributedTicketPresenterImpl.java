package com.anydone.desk.contributed;


import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.tickets.contributedtickets.ContributedTicketRepository;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

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

public class ContributedTicketPresenterImpl extends BasePresenter
        <com.anydone.desk.contributed.ContributedTicketContract.ContributedTicketView>
        implements ContributedTicketContract.ContributedTicketPresenter {

    private static final String TAG = "ContributedTicketPresen";
    private ContributedTicketRepository contributedTicketRepository;

    @Inject
    public ContributedTicketPresenterImpl(ContributedTicketRepository contributedTicketRepository) {
        this.contributedTicketRepository = contributedTicketRepository;
    }

    private void saveContributedTickets(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> contributedTickets = TicketRepo.getInstance().getContributedTickets();
        if (!CollectionUtils.isEmpty(contributedTickets)) {
            TicketRepo.getInstance().deleteContributedTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete contributed tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.CONTRIBUTED,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getContributedTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getContributedTicketFail("failed to save contributed tickets");
                        GlobalUtils.showLog(TAG, "failed to save contributed tickets");
                    }
                });
    }

    @Override
    public void getContributedTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getContributedTickets(token,
                serviceId, from, to, page, "ASC");
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get contributed tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getContributedTicketFail("Get contributed" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getContributedTicketFail
                                            (getTicketsBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "contributed tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (getTicketsBaseResponse.getTicketsList().size() > 0)
                                    saveContributedTickets(getTicketsBaseResponse.getTicketsList());
                                else getView().getContributedTicketFail("no tickets found");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getContributedTicketFail(e.getLocalizedMessage());
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
                                public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                           filterTicketBaseResponse) {
                                    GlobalUtils.showLog(TAG, "filter contributed ticket response: "
                                            + filterTicketBaseResponse);

                                    getView().hideProgressBar();
                                    if (filterTicketBaseResponse == null) {
                                        getView().filterTicketsFailed("Filter contributed ticket failed");
                                        return;
                                    }

                                    if (filterTicketBaseResponse.getError()) {
                                        getView().filterTicketsFailed(filterTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterTicketBaseResponse.getTicketsList())) {
                                        List<Tickets> filteredTickets = TicketRepo.
                                                getInstance().transformTicketProto(filterTicketBaseResponse.getTicketsList(), Constants.SUBSCRIBEABLE);
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

    private String getExportUrl(String query, long from, long to, int status, int priority,
                                AssignEmployee selectedEmp,
                                TicketCategory selectedTicketType, Tags selectedTeam,
                                Service selectedService, String reqType, String repType) {

        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }

        StringBuilder filterUrlBuilder = new StringBuilder("ticket/report/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1
                && selectedEmp == null && selectedTicketType == null && selectedTeam == null &&
                selectedService == null) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(0);
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(System.currentTimeMillis());
            filterUrlBuilder.append("&reqType=");
            filterUrlBuilder.append(reqType);
            filterUrlBuilder.append("&repType=");
            filterUrlBuilder.append(repType);
            return filterUrlBuilder.toString();
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

        if (selectedTicketType != null && !selectedTicketType.getCategoryId().isEmpty()) {
            filterUrlBuilder.append("&type=");
            filterUrlBuilder.append(selectedTicketType.getCategoryId());
        }

        if (selectedTeam != null && !selectedTeam.getTagId().isEmpty()) {
            filterUrlBuilder.append("&team=");
            filterUrlBuilder.append(selectedTeam.getTagId());
        }

        filterUrlBuilder.append("&reqType=");
        filterUrlBuilder.append(reqType);
        filterUrlBuilder.append("&repType=");
        filterUrlBuilder.append(repType);
        filterUrlBuilder.append("&sort=DESC");
        return filterUrlBuilder.toString();
    }


    @Override
    public void export(String searchQuery, long from, long to, int ticketState, Priority priority, AssignEmployee selectedEmp, TicketCategory selectedTicketType, Tags selectedTeam, Service selectedService, String reqType, String repType) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String exportUrl = getExportUrl(searchQuery, from, to, ticketState, priorityNum,
                selectedEmp, selectedTicketType, selectedTeam, selectedService, reqType, repType);

        if (!exportUrl.isEmpty()) {
            getView().showProgressExport();
            ticketBaseResponseObservable = service.exportTickets(token, exportUrl);
            addSubscription(ticketBaseResponseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(
                            new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                                @Override
                                public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                           exportTicketBaseResponse) {
                                    GlobalUtils.showLog(TAG, "export all ticket response: "
                                            + exportTicketBaseResponse);

                                    getView().hideProgressBar();

                                    if (exportTicketBaseResponse.getError()) {
                                        getView().onExportFail(exportTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    getView().onExportSuccess(exportTicketBaseResponse.getReport().getUrl(),
                                            repType);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    getView().hideProgressBar();
                                    getView().onExportFail(e.getLocalizedMessage());
                                }

                                @Override
                                public void onComplete() {
                                    getView().hideProgressBar();
                                }
                            })
            );
        }
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

    private String getFilterUrl(String query, long from, long to, int status, int priority
            , AssignEmployee selectedEmp, TicketCategory
                                        selectedTicketCategory, Tags selectedTeam,
                                Service selectedService) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/contributed/" + serviceId + "?");

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

