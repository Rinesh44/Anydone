package com.anydone.desk.addticket;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.rpc.UserRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.DependentTicket;
import com.anydone.desk.realm.model.Label;
import com.anydone.desk.realm.model.Tags;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.AssignEmployeeRepo;
import com.anydone.desk.realm.repo.CustomerRepo;
import com.anydone.desk.realm.repo.DependentTicketRepo;
import com.anydone.desk.realm.repo.LabelRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.TagRepo;
import com.anydone.desk.realm.repo.TicketCategoryRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.EstimatedTimeHelper;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AddTicketPresenterImpl extends BasePresenter<AddTicketContract.AddTicketView>
        implements AddTicketContract.AddTicketPresenter {
    private static final String TAG = "AddTicketPresenterImpl";
    private AddTicketRepository addTicketRepository;
    private TicketProto.Ticket dependentTicketPb;

    @Inject
    public AddTicketPresenterImpl(AddTicketRepository addTicketRepository) {
        this.addTicketRepository = addTicketRepository;
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
                            getView().findCustomerFail(consumerResponse.getMsg());
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
                getView().findCustomerSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save customers");
            }
        });
    }


    @Override
    public void getTicketTypes() {
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
                            getView().getTypeFail(response.getMsg());
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

    @Override
    public void getSummarySuggestions(String summary) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getSummarySuggestions(token, serviceId,
                summary, "");

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse suggestionResponse) {
                        GlobalUtils.showLog(TAG, "get suggestion response:"
                                + suggestionResponse);

                        if (suggestionResponse.getError()) {
                            getView().getSummarySuggestionFail(suggestionResponse.getMsg());
                            return;
                        }


                        getView().getSummarySuggestionSuccess(suggestionResponse.getAutofillSuggestionRes());
//                        saveCustomers(suggestionResponse.getAutofillSuggestionRes());
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

    private void saveTicketTypes(List<TicketProto.TicketType> ticketTypeList) {
        TicketCategoryRepo.getInstance().saveTicketTypeList(ticketTypeList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket types");
                getView().getTypeSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save ticket types");
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
                            getView().findEmployeeFail(getEmployeeResponse.getMsg());
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

    private void saveEmployees(List<UserProto.EmployeeProfile> employeesList) {
        AssignEmployeeRepo.getInstance().saveAssignEmployeeList(employeesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved assign employees");
                getView().findEmployeeSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save assign employees");
            }
        });
    }

    @Override
    public void createTicket(String ticketType, String title, String description, String customerId,
                             String customerEmail, String customerPhone, String customerName,
                             String customerPic, List<String> tags, List<Label> ticketLabels,
                             String estimatedTime, String assignedEmployeeId, int priority,
                             TicketProto.TicketSource ticketSource, boolean customerAsSelf,
                             String refId, DependentTicket dependentTicket) {
        TicketProto.CustomerType customerType = TicketProto.CustomerType.EXTERNAL_CUSTOMER;
        if (!validateCredentials(title, customerName, ticketType, estimatedTime)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);

        UserProto.Customer customer;
        if (customerId != null) {
            customer = UserProto.Customer.newBuilder()
                    .setCustomerId(customerId)
                    .setEmail(customerEmail)
                    .setPhone(customerPhone)
                    .setFullName(customerName)
                    .setProfilePic(customerPic)
                    .build();

            customerType = TicketProto.CustomerType.EXTERNAL_CUSTOMER;
        } else {
            customer = UserProto.Customer.newBuilder()
                    .setEmail(customerEmail)
                    .setPhone(customerPhone)
                    .setFullName(customerName)
                    .setProfilePic(customerPic)
                    .build();
        }

        if (customerAsSelf) {
            String accountType = AccountRepo.getInstance().getAccount().getAccountType();
            if (accountType.equalsIgnoreCase("EMPLOYEE")) {
                customerType = TicketProto.CustomerType.ANYDONE_EMPLOYEE;
            } else if (accountType.equalsIgnoreCase("SERVICE_PROVIDER")) {
                customerType = TicketProto.CustomerType.ANYDONE_SERVICE_PROVIDER;
            }
        }

        GlobalUtils.showLog(TAG, "teams check" + tags);
        GlobalUtils.showLog(TAG, "customer id check: " + customerId);
        GlobalUtils.showLog(TAG, "customer type: " + customerType.name());

        List<TicketProto.Team> tagList = new ArrayList<>();
        for (String tagId : tags
        ) {
            TicketProto.Team tag = TicketProto.Team.newBuilder()
                    .setTeamId(tagId)
                    .build();

            tagList.add(tag);
        }

        List<TicketProto.Label> labelList = new ArrayList<>();
        for (Label label : ticketLabels
        ) {
            TicketProto.Label label1 = TicketProto.Label.newBuilder()
                    .setLabelId(label.getLabelId())
                    .setName(label.getName())
                    .build();

            labelList.add(label1);
        }

        TicketProto.TicketType type = TicketProto.TicketType.newBuilder()
                .setTicketTypeId(ticketType)
                .build();

        ServiceProto.Service service = ServiceProto.Service.newBuilder()
                .setServiceId(Hawk.get(Constants.SELECTED_SERVICE))
                .build();

        if (dependentTicket != null) {
            dependentTicketPb = TicketProto.Ticket.newBuilder()
                    .setTicketId(dependentTicket.getId())
                    .setTicketIndex(dependentTicket.getIndex())
                    .setTitle(dependentTicket.getSummary())
                    .build();
        }


        GlobalUtils.showLog(TAG, "Ticket title check: " + title);
        GlobalUtils.showLog(TAG, "ticket priority check: " + priority);
        GlobalUtils.showLog(TAG, "service check: " + service);

        TicketProto.Ticket ticket;
        if (assignedEmployeeId != null) {
            UserProto.EmployeeProfile employeeProfile = UserProto.EmployeeProfile.newBuilder()
                    .setEmployeeProfileId(assignedEmployeeId)
                    .build();

            TicketProto.EmployeeAssigned employeeAssigned = TicketProto.EmployeeAssigned.newBuilder()
                    .setAssignedAt(System.currentTimeMillis())
                    .setAssignedTo(employeeProfile)
                    .build();

            if (dependentTicket != null) {
                ticket = TicketProto.Ticket.newBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setType(type)
                        .setCustomer(customer)
                        .setCustomerType(customerType)
                        .setPriority(getTicketPriority(priority))
                        .setService(service)
                        .setEmployeeAssigned(employeeAssigned)
                        .setTicketSource(ticketSource)
                        .addAllTeams(tagList)
                        .addAllLabel(labelList)
                        .setRefId(refId)
                        .setEstimatedTimeDesc(estimatedTime)
                        .setDependOnTicket(dependentTicketPb)
                        .build();
            } else {
                ticket = TicketProto.Ticket.newBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setType(type)
                        .setCustomer(customer)
                        .setCustomerType(customerType)
                        .setPriority(getTicketPriority(priority))
                        .setService(service)
                        .setEmployeeAssigned(employeeAssigned)
                        .setTicketSource(ticketSource)
                        .addAllTeams(tagList)
                        .addAllLabel(labelList)
                        .setRefId(refId)
                        .setEstimatedTimeDesc(estimatedTime)
                        .build();
            }

        } else {
            if (dependentTicket != null) {
                ticket = TicketProto.Ticket.newBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setCustomer(customer)
                        .setType(type)
                        .setCustomerType(customerType)
                        .setPriority(getTicketPriority(priority))
                        .setService(service)
                        .setTicketSource(ticketSource)
                        .addAllTeams(tagList)
                        .addAllLabel(labelList)
                        .setRefId(refId)
                        .setEstimatedTimeDesc(estimatedTime)
                        .setDependOnTicket(dependentTicketPb)
                        .build();
            } else {
                ticket = TicketProto.Ticket.newBuilder()
                        .setTitle(title)
                        .setDescription(description)
                        .setCustomer(customer)
                        .setType(type)
                        .setCustomerType(customerType)
                        .setPriority(getTicketPriority(priority))
                        .setService(service)
                        .setTicketSource(ticketSource)
                        .addAllTeams(tagList)
                        .addAllLabel(labelList)
                        .setRefId(refId)
                        .setEstimatedTimeDesc(estimatedTime)
                        .build();
            }
        }


        GlobalUtils.showLog(TAG, "sent ticket det: " + ticket);
        ticketObservable = anyDoneService.createTicket(token, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse createTicketResponse) {
                        GlobalUtils.showLog(TAG, "create ticket response:"
                                + createTicketResponse);

                        getView().hideProgressBar();

                        if (createTicketResponse.getError()) {
                            getView().onCreateTicketFail(createTicketResponse.getMsg());
                            return;
                        }

                        Hawk.put(Constants.REFETCH_TICKET_STAT, true);
                        saveTicket(createTicketResponse.getTicket());

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
    public void getDependencyListTickets() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getDependencyTickets(token, serviceId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "get dependent ticket list response:"
                                + response);

                        if (response.getError()) {
                            getView().getDependencyTicketsListFail(response.getMsg());
                            return;
                        }


                        saveTickets(response.getTicketsList());
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

    @Override
    public void searchTickets(String query) {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.searchDependentTickets(token, serviceId, query);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "search dependent ticket response:"
                                + response);

                        if (response.getError()) {
                            getView().searchDependentTicketFail(response.getMsg());
                            return;
                        }

                        List<DependentTicket> ticketList = transformTickets(response.getTicketsList());
                        getView().searchDependentTicketSuccess(ticketList);
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

    private List<DependentTicket> transformTickets(List<TicketProto.Ticket> ticketsList) {
        List<DependentTicket> dependentTickets = new ArrayList<>();
        for (TicketProto.Ticket ticketPb : ticketsList
        ) {
            DependentTicket ticket = new DependentTicket();
            ticket.setServiceId(ticketPb.getService().getServiceId());
            ticket.setCreatedAt(ticketPb.getCreatedAt());
            ticket.setSummary(ticketPb.getTitle());
            ticket.setIndex(ticketPb.getTicketIndex());
            ticket.setId(ticketPb.getTicketId());
            dependentTickets.add(ticket);
        }
        return dependentTickets;
    }

    private void saveTickets(List<TicketProto.Ticket> ticketsList) {
        DependentTicketRepo.getInstance().saveTicketList(ticketsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getDependencyTicketsListSuccess();
            }

            @Override
            public void fail() {
                getView().getDependencyTicketsListFail("Failed to save tickets to db");
            }
        });
    }

    private TicketProto.TicketPriority getTicketPriority(int priority) {
        switch (priority) {
            case 1:
                return TicketProto.TicketPriority.LOWEST_TICKET_PRIORITY;

            case 2:
                return TicketProto.TicketPriority.LOW_TICKET_PRIORITY;

            case 3:
                return TicketProto.TicketPriority.MEDIUM_TICKET_PRIORITY;

            case 4:
                return TicketProto.TicketPriority.HIGH_TICKET_PRIORITY;

            case 5:
                return TicketProto.TicketPriority.HIGHEST_TICKET_PRIORITY;
        }
        return TicketProto.TicketPriority.UNKNOWN_TICKET_PRIORITY;
    }

    private void saveTicket(TicketProto.Ticket ticketPb) {
        Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, true);

        Account userAccount = AccountRepo.getInstance().getAccount();
        if (ticketPb.getEmployeeAssigned().getAssignedTo()
                .getAccount().getAccountId().equalsIgnoreCase(userAccount.getAccountId())) {
            savePendingTicket(ticketPb);
        } else {
            saveAssignableTicket(ticketPb);
        }
    }

    private void saveAssignableTicket(TicketProto.Ticket ticketPb) {
        TicketRepo.getInstance().saveTicket(ticketPb, Constants.ASSIGNABLE, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved as assignable ticket");
                getView().onCreateTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save as assignable ticket");
            }
        });
    }

    private void savePendingTicket(TicketProto.Ticket ticketPb) {
        TicketRepo.getInstance().saveTicket(ticketPb, Constants.PENDING, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved as pending ticket");
                Hawk.put(Constants.TICKET_ASSIGNED, true);
                getView().onCreateTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save pending ticket");
            }
        });
    }

    private boolean validateCredentials(String summary, String customerName, String ticketType,
                                        String estimatedTime) {

        if (ticketType == null || ValidationUtils.isEmpty(ticketType)) {
            getView().onInvalidTicketType();
            return false;
        }

        if (ValidationUtils.isEmpty(summary)) {
            getView().onInvalidSummary();
            return false;
        }

        if (ValidationUtils.isEmpty(customerName)) {
            getView().onInvalidCustomer();
            return false;
        }

        if (!EstimatedTimeHelper.validateEstimatedTime(estimatedTime)) {
            getView().onInvalidEstTime();
            return false;
        }

        return true;
    }

    @Override
    public void findTags() {
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
                            getView().findTagsFail(tagResponse.getMsg());
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

    @Override
    public void getLabels() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        ticketObservable = service.getTicketLabels(token, serviceId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse response) {
                        GlobalUtils.showLog(TAG, "get labels response:"
                                + response);

                        if (response.getError()) {
                            getView().getLabelFail(response.getMsg());
                            return;
                        }

                        saveLabels(response.getLabelsList());
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

    private void saveLabels(List<TicketProto.Label> labels) {
        LabelRepo.getInstance().saveLabelList(labels, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved labels");
                getView().getLabelSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save labels");
            }
        });
    }

    private void saveTags(List<TicketProto.Team> tagsList) {
        TagRepo.getInstance().saveTags(tagsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved tags");
                getView().findTagsSuccess();
                List<Tags> tags = TagRepo.getInstance().getAllTags();
                GlobalUtils.showLog(TAG, "saved tags: " + tags);
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save tags");
            }
        });
    }
}
