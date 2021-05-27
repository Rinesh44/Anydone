package com.anydone.desk.tickets;

import android.widget.Toast;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.model.Priority;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Service;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.model.TicketCategory;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.AvailableServicesRepo;
import com.anydone.desk.realm.repo.CustomerRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.TagRepo;
import com.anydone.desk.realm.repo.TicketCategoryRepo;
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
    public void filterPendingTickets(String searchQuery, long from, long to,
                                     int ticketState, Priority priority, AssignEmployee selectedEmp,
                                     TicketCategory selectedTicketType, Tags selectedTeam,
                                     Service selectedService, Customer selectedCustomer) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String filterUrl = getPendingFilterUrl(searchQuery, from, to, ticketState, priorityNum,
                selectedEmp, selectedTicketType, selectedTeam, selectedService, selectedCustomer);

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
                                    GlobalUtils.showLog(TAG, "filter pending ticket response: "
                                            + filterTicketBaseResponse);

                                    getView().hideProgressBar();

                                    if (filterTicketBaseResponse.getError()) {
                                        getView().filterPendingTicketsFailed(filterTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterTicketBaseResponse.getTicketsList())) {
                                        List<Tickets> filteredPendingTickets = TicketRepo.
                                                getInstance().transformTicketProto
                                                (filterTicketBaseResponse.getTicketsList(), Constants.PENDING);

                                        getView().updatePendingTicketList(filteredPendingTickets);
                                    } else {
                                        getView().filterPendingTicketsFailed("Not found");
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    getView().hideProgressBar();
                                    getView().filterPendingTicketsFailed(e.getLocalizedMessage());
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
    public void filterInProgressTickets(String searchQuery, long from, long to, int ticketState,
                                        Priority priority, AssignEmployee selectedEmp,
                                        TicketCategory selectedTicketType, Tags selectedTeam,
                                        Service selectedService, Customer selectedCustomer) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String filterUrl = getInProgressFilterUrl(searchQuery, from, to, ticketState, priorityNum,
                selectedEmp, selectedTicketType, selectedTeam, selectedService, selectedCustomer);

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
                                    GlobalUtils.showLog(TAG, "filter subscribed ticket response: "
                                            + filterTicketBaseResponse);

                                    getView().hideProgressBar();

                                    if (filterTicketBaseResponse.getError()) {
                                        getView().filterInProgressTicketFailed(filterTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterTicketBaseResponse.getTicketsList())) {
                                        List<Tickets> filteredTickets = TicketRepo.
                                                getInstance().transformTicketProto(filterTicketBaseResponse.
                                                        getTicketsList(),
                                                Constants.SUBSCRIBED);

                                        getView().updateInProgressTicketList(filteredTickets);
                                    } else {
                                        getView().filterInProgressTicketFailed("Not found");
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    getView().hideProgressBar();
                                    getView().filterInProgressTicketFailed(e.getLocalizedMessage());
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
    public void filterClosedTickets(String searchQuery, long from, long to,
                                    int ticketState, Priority priority, AssignEmployee assignEmployee,
                                    TicketCategory ticketType, Tags tags, Service selectedService,
                                    Customer selectedCustomer) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        int priorityNum = GlobalUtils.getPriorityNum(priority);
        String filterUrl = getClosedFilterUrl(searchQuery, from, to, ticketState, priorityNum,
                assignEmployee, ticketType, tags, selectedService, selectedCustomer);

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
                                    GlobalUtils.showLog(TAG, "filter closed ticket response: "
                                            + filterTicketBaseResponse);

                                    getView().hideProgressBar();

                                    if (filterTicketBaseResponse.getError()) {
                                        getView().filterClosedTicketFailed(filterTicketBaseResponse.getMsg());
                                        return;
                                    }

                                    if (!CollectionUtils.isEmpty(
                                            filterTicketBaseResponse.getTicketsList())) {
                                        List<Tickets> filteredTickets = TicketRepo.
                                                getInstance().transformTicketProto
                                                (filterTicketBaseResponse.getTicketsList(),
                                                        Constants.CLOSED_RESOLVED);

                                        getView().updateClosedTicketList(filteredTickets);
                                    } else {
                                        getView().filterClosedTicketFailed("Not found");
                                    }
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
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
    }

    @Override
    public void findCustomers() {
        Observable<UserRpcProto.UserBaseResponse> customersObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        customersObservable = service.findCustomers(token, "",
                0, System.currentTimeMillis(), 100);

        addSubscription(customersObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<UserRpcProto.UserBaseResponse>() {
                    @Override
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse consumerResponse) {
                        GlobalUtils.showLog(TAG, "get customer response:"
                                + consumerResponse);

                        if (consumerResponse.getError()) {
                            getView().findCustomersFail(consumerResponse.getMsg());
                            return;
                        }

                        saveCustomers(consumerResponse.getCustomersList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private void saveCustomers(List<UserProto.Customer> consumersList) {
        CustomerRepo.getInstance().saveCustomerList(consumersList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved customers");
                getView().findCustomersSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save customers");
            }
        });
    }

    @Override
    public void findEmployees() {
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
                    public void onNext(@NonNull UserRpcProto.UserBaseResponse getEmployeeResponse) {
                        GlobalUtils.showLog(TAG, "find employees response:"
                                + getEmployeeResponse);

                        if (getEmployeeResponse.getError()) {
                            getView().getEmployeeFail(getEmployeeResponse.getMsg());
                            return;
                        }

                        saveEmployees(getEmployeeResponse.getEmployeesList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    @Override
    public void getServices() {
        Observable<ServiceRpcProto.ServiceBaseResponse> servicesObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        servicesObservable = service.getServices(token);
        addSubscription(servicesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ServiceRpcProto.ServiceBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ServiceRpcProto.ServiceBaseResponse
                                                       getServicesBaseResponse) {
                                GlobalUtils.showLog(TAG, "get services response: "
                                        + getServicesBaseResponse);

                                if (getServicesBaseResponse.getError()) {
                                    getView().getServiceFail(getServicesBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        getServicesBaseResponse.getServicesList())) {
                                    saveAvailableServices(getServicesBaseResponse.getServicesList());
                                } else {
                                    getView().getServiceFail("Services Not found");
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getServiceFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveAvailableServices
            (List<ServiceProto.Service> availableServicesList) {
        AvailableServicesRepo.getInstance().saveAvailableServices(availableServicesList,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getServiceSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getServiceFail("failed to get services");
                    }
                });
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

    @Override
    public void findTeams() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> tagObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        tagObservable = service.getTicketTeams(token, serviceId);

        addSubscription(tagObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse tagResponse) {
                        GlobalUtils.showLog(TAG, "get tag response:"
                                + tagResponse);

                        if (tagResponse.getError()) {
                            getView().getTeamFail(tagResponse.getMsg());
                            return;
                        }

                        saveTags(tagResponse.getTeamsList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));
    }

    private void saveTags(List<TicketProto.Team> tagsList) {
        TagRepo.getInstance().saveTags(tagsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved tags");
                getView().getTeamSuccess();
                List<Tags> tags = TagRepo.getInstance().getAllTags();
                GlobalUtils.showLog(TAG, "saved tags: " + tags);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save tags");
            }
        });
    }


    private String getPendingFilterUrl(String query, long from, long to, int status,
                                       int priority, AssignEmployee selectedEmp, TicketCategory
                                               selectedTicketCategory, Tags selectedTeam,
                                       Service selectedService, Customer selectedCustomer) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/pending/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1 && selectedEmp
                == null && selectedTicketCategory == null && selectedTeam == null && selectedService
                == null && selectedCustomer == null) {
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
            if (selectedEmp.getName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&ef=ALL");
            } else {
                filterUrlBuilder.append("&employeeId=");
                filterUrlBuilder.append(selectedEmp.getEmployeeId());
            }
        }

        if (selectedCustomer != null && !selectedCustomer.getCustomerId().isEmpty()) {
            if (!selectedCustomer.getFullName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&r=");
                filterUrlBuilder.append(selectedCustomer.getCustomerId());
            }
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
        filterUrlBuilder.append("&page=200");
        return filterUrlBuilder.toString();
    }

    private String getInProgressFilterUrl(String query, long from, long to, int status,
                                          int priority, AssignEmployee selectedEmp, TicketCategory
                                                  selectedTicketCategory, Tags selectedTeam,
                                          Service selectedService, Customer selectedCustomer) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }

        StringBuilder filterUrlBuilder = new StringBuilder("ticket/inprogress/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1 && selectedEmp
                == null && selectedTicketCategory == null && selectedTeam == null && selectedService == null
                && selectedCustomer == null) {
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
            if (selectedEmp.getName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&ef=ALL");
            } else {
                filterUrlBuilder.append("&employeeId=");
                filterUrlBuilder.append(selectedEmp.getEmployeeId());
            }
        }

        if (selectedCustomer != null && !selectedCustomer.getCustomerId().isEmpty()) {
            if (!selectedCustomer.getFullName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&r=");
                filterUrlBuilder.append(selectedCustomer.getCustomerId());
            }
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
        filterUrlBuilder.append("&page=200");
        return filterUrlBuilder.toString();
    }

    private String getClosedFilterUrl(String query, long from, long to, int status, int priority,
                                      AssignEmployee selectedEmp, TicketCategory selectedTicketCategory,
                                      Tags selectedTeam, Service selectedService, Customer selectedCustomer) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        if (selectedService != null) {
            serviceId = selectedService.getServiceId();
        }
        StringBuilder filterUrlBuilder = new StringBuilder("ticket/inactive/" + serviceId + "?");

        if (query.isEmpty() && from == 0 && to == 0 && status == -1 && priority == -1 && selectedEmp
                == null && selectedTicketCategory == null && selectedTeam == null &&
                selectedService == null && selectedCustomer == null) {
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
            if (selectedEmp.getName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&ef=ALL");
            } else {
                filterUrlBuilder.append("&employeeId=");
                filterUrlBuilder.append(selectedEmp.getEmployeeId());
            }
        }

        if (selectedCustomer != null && !selectedCustomer.getCustomerId().isEmpty()) {
            if (!selectedCustomer.getFullName().equalsIgnoreCase("all")) {
                filterUrlBuilder.append("&r=");
                filterUrlBuilder.append(selectedCustomer.getCustomerId());
            }
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
        filterUrlBuilder.append("&page=1000");
        return filterUrlBuilder.toString();
    }

    @Override
    public void findTicketTypes() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getTicketTypes(token, serviceId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "get ticket types response:"
                                + response);

                        if (response.getError()) {
                            getView().getTicketTypeFail(response.getMsg());
                            return;
                        }

                        saveTicketTypes(response.getTicketTypesList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                }));

    }

    private void saveTicketTypes(List<TicketProto.TicketType> ticketTypeList) {
        TicketCategoryRepo.getInstance().saveTicketTypeList(ticketTypeList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket types");
                getView().getTicketTypeSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save ticket types");
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
}
