package com.treeleaf.anydone.serviceprovider.alltickets;

import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Service;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketCategory;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AssignEmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;
import java.util.Objects;

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

public class AllTicketPresenterImpl extends BasePresenter
        <AllTicketContract.AllTicketView>
        implements AllTicketContract.AllTicketPresenter {

    private static final String TAG = "AllTicketPresenterImpl";
    private AllTicketRepository allTicketRepository;

    @Inject
    public AllTicketPresenterImpl(AllTicketRepository allTicketRepository) {
        this.allTicketRepository = allTicketRepository;
    }

    private void saveAllTickets(List<TicketProto.Ticket> ticketsList) {
        List<Tickets> allTickets = TicketRepo.getInstance().getAllTickets();
        if (!CollectionUtils.isEmpty(allTickets)) {
            TicketRepo.getInstance().deleteAllTickets(new Repo.Callback() {
                @Override
                public void success(Object o) {
                    GlobalUtils.showLog(TAG, "del all ticket success");
                }

                @Override
                public void fail() {
                    GlobalUtils.showLog(TAG, "failed to delete all tickets");
                }
            });
        }
        saveTickets(ticketsList);
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        TicketRepo.getInstance().saveTicketList(ticketsList, Constants.ALL,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getAllTicketSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getAllTicketFail("failed to save all tickets");
                        GlobalUtils.showLog(TAG, "failed to save all tickets");
                    }
                });
    }

    @Override
    public void getAllTickets(boolean showProgress, long from, long to, int page) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        getTicketsObservable = service.getDependencyTickets(token,
                serviceId);
        addSubscription(getTicketsObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketsBaseResponse) {
                                GlobalUtils.showLog(TAG, "get all tickets response: "
                                        + getTicketsBaseResponse);

                                getView().hideProgressBar();
                                if (getTicketsBaseResponse == null) {
                                    getView().getAllTicketFail("Get all" +
                                            " tickets failed");
                                    return;
                                }

                                if (getTicketsBaseResponse.getError()) {
                                    getView().getAllTicketFail((getTicketsBaseResponse.getMsg()));
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "service id: " + serviceId);
                                GlobalUtils.showLog(TAG, "contributed tickets Count: " +
                                        getTicketsBaseResponse.getTicketsList().size());
                                if (getTicketsBaseResponse.getTicketsList().size() > 0)
                                    saveAllTickets(getTicketsBaseResponse.getTicketsList());
                                else getView().getAllTicketFail("No tickets found");
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getAllTicketFail(e.getLocalizedMessage());
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
                              AssignEmployee selectedEmp, TicketCategory selectedTicketType,
                              Tags selectedTeam, Service selectedService) {
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
                                                getInstance().transformTicketProto(
                                                filterTicketBaseResponse.getTicketsList(),
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
    }

    @Override
    public void subscribe(long ticketId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);

        getTicketsObservable = service.subscribe(token, ticketId);
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

                                getView().onSubscribeSuccess(ticketId);
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
    public void assignTicket(long ticketId, String employeeId) {
        getView().showProgressBar("Please wait...");

        Observable<TicketServiceRpcProto.TicketBaseResponse> getTicketsObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                .setEmployeeProfileId(employeeId)
                .build();

        TicketProto.EmployeeAssigned employeeAssigned = TicketProto.EmployeeAssigned.newBuilder()
                .setAssignedTo(employeeProfile)
                .setAssignedAt(System.currentTimeMillis())
                .build();

        TicketProto.Ticket ticket = TicketProto.Ticket.newBuilder()
                .setEmployeeAssigned(employeeAssigned)
                .build();

        GlobalUtils.showLog(TAG, "employee assigned check:" + ticket);

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

                                getView().hideProgressBar();
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
    public void export(String searchQuery, long from, long to, int ticketState, Priority priority,
                       AssignEmployee selectedEmp, TicketCategory selectedTicketType,
                       Tags selectedTeam, Service selectedService, String reqType, String repType) {
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
                                public void onError(@NonNull Throwable e) {
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

    private String getFilterUrl(String query, long from, long to, int status, int priority,
                                AssignEmployee selectedEmp,
                                TicketCategory selectedTicketType, Tags selectedTeam,
                                Service selectedService) {

        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/service/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1
                && selectedEmp == null && selectedTicketType == null && selectedTeam == null &&
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

        if (selectedTicketType != null && !selectedTicketType.getCategoryId().isEmpty()) {
            filterUrlBuilder.append("&type=");
            filterUrlBuilder.append(selectedTicketType.getCategoryId());
        }

        if (selectedTeam != null && !selectedTeam.getTagId().isEmpty()) {
            filterUrlBuilder.append("&team=");
            filterUrlBuilder.append(selectedTeam.getTagId());
        }

        filterUrlBuilder.append("&sort=DESC");
        return filterUrlBuilder.toString();
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
}


