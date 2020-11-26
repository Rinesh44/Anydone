package com.treeleaf.anydone.serviceprovider.addticket;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Label;
import com.treeleaf.anydone.serviceprovider.realm.model.Tags;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.LabelRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TagRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

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

    @Inject
    public AddTicketPresenterImpl(AddTicketRepository addTicketRepository) {
        this.addTicketRepository = addTicketRepository;
    }

    @Override
    public void createTicket(String ticketType, String title, String description, String customerId,
                             String customerEmail, String customerPhone, String customerName,
                             String customerPic, List<String> tags, List<Label> ticketLabels,
                             String estimatedTime, String assignedEmployeeId, int priority,
                             TicketProto.TicketSource ticketSource, boolean customerAsSelf,
                             String refId) {

        TicketProto.CustomerType customerType = TicketProto.CustomerType.EXTERNAL_CUSTOMER;
        if (!validateCredentials(title, customerName, ticketType)) {
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

        GlobalUtils.showLog(TAG, "sent ticket det: " + ticket);
        ticketObservable = anyDoneService.createTicket(token, ticket);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse createTicketResponse) {
                        GlobalUtils.showLog(TAG, "create ticket response:"
                                + createTicketResponse);

                        getView().hideProgressBar();
                        if (createTicketResponse == null) {
                            getView().onCreateTicketFail("Failed to create ticket");
                            return;
                        }

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

    private boolean validateCredentials(String summary, String customerName, String ticketType) {

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
