package com.treeleaf.anydone.serviceprovider.addticket;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class AddTicketPresenterImpl extends BasePresenter<AddTicketContract.AddTicketView>
        implements AddTicketContract.AddTicketPresenter {
    private static final String TAG = "AddTicketPresenterImpl";
    private AddTicketRepository addTicketRepository;

    @Inject
    public AddTicketPresenterImpl(AddTicketRepository addTicketRepository) {
        this.addTicketRepository = addTicketRepository;
    }

    @Override
    public void createTicket(String title, String description, String customerId, String customerEmail,
                             String customerPhone, String customerName, List<String> tags,
                             String assignedEmployeeId, int priority) {

        if (!validateCredentials(title, customerName)) {
            return;
        }

        getView().showProgressBar("Please wait...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        UserProto.Customer customer;
        if (customerId != null) {
            customer = UserProto.Customer.newBuilder()
                    .setCustomerId(customerId)
                    .setEmail(customerEmail)
                    .setPhone(customerPhone)
                    .setFullName(customerName)
                    .build();
        } else {
            customer = UserProto.Customer.newBuilder()
                    .setEmail(customerEmail)
                    .setPhone(customerPhone)
                    .setFullName(customerName)
                    .build();
        }

        List<TicketProto.TicketTag> tagList = new ArrayList<>();
        for (String tagId : tags
        ) {
            TicketProto.TicketTag tag = TicketProto.TicketTag.newBuilder()
                    .setTagId(tagId)
                    .build();

            tagList.add(tag);
        }


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
                    .setCustomer(customer)
                    .setCustomerType(TicketProto.CustomerType.EXTERNAL_CUSTOMER)
                    .setPriority(getTicketPriority(priority))
                    .setService(service)
                    .setEmployeeAssigned(employeeAssigned)
                    .addAllTags(tagList)
                    .build();
        } else {
            ticket = TicketProto.Ticket.newBuilder()
                    .setTitle(title)
                    .setDescription(description)
                    .setCustomer(customer)
                    .setCustomerType(TicketProto.CustomerType.EXTERNAL_CUSTOMER)
                    .setPriority(getTicketPriority(priority))
                    .setService(service)
                    .addAllTags(tagList)
                    .build();
        }

        ticketObservable = addTicketRepository.createTicket(token, ticket);

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
        TicketRepo.getInstance().saveTicket(ticketPb, Constants.ASSIGNED, new Repo.Callback() {
            @Override
            public void success(Object o) {
                Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, true);
                getView().onCreateTicketSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save ticket");
            }
        });
    }

    private boolean validateCredentials(String summary, String customerName) {

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
}
