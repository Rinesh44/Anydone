package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.servicerequests.OnSwipeListener;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets.UnSubscribedTicketsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SubscribeTicketsFragment extends BaseFragment<SubscribeTicketPresenterImpl>
        implements SubscribeTicketContract.SubscribeTicketsView,
        TicketsFragment.SubscribedListListener {
    private static final String TAG = "SubscribeTicketsFragmen";
    @BindView(R.id.rv_subscribe_tickets)
    RecyclerView rvSubscribeTickets;
    @BindView(R.id.swipe_refresh_subscribe_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.fab_subscribe)
    FloatingActionButton fabSubscribe;
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    private Unbinder unbinder;
    private OnSwipeListener swipeListener;
    private OnSubscribeTicketsListener onSubscribeTicketsListener;
    private TicketsAdapter adapter;
    private int unsubscribedTicketPos;
    private boolean fetchList = false;
    private List<Tickets> subscribedTickets;
    private Account userAccount;
    private String localAccountId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setSubscribedListListener(this);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (fetchList) {
        subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();

        if (CollectionUtils.isEmpty(subscribedTickets)) {
            GlobalUtils.showLog(TAG, "subscribe tickets empty");
            ivDataNotFound.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(subscribedTickets);
        }
//        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_subscribe_tickets;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void injectDagger(ApplicationComponent applicationComponent) {
        applicationComponent.inject(this);
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvSubscribeTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvSubscribeTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {

                ArrayList<String> employeeProfileUris = new ArrayList<>();
                StringBuilder builder = new StringBuilder();
                Employee assignedEmployee = ticket.getAssignedEmployee();
                String assignedEmployeeName = assignedEmployee.getName();

                Customer customer = ticket.getCustomer();
                String customerName = customer.getFullName();

                if (customer != null && !localAccountId.equals(customer.getCustomerId())
                        && !customerName.isEmpty()) {
                    builder.append(customerName);
                    builder.append(", ");
                    employeeProfileUris.add(customer.getProfilePic());
                }

                if (!localAccountId.equals(assignedEmployee.getAccountId()) &&
                        assignedEmployeeName != null && !assignedEmployeeName.isEmpty()) {
                    builder.append(assignedEmployeeName);
                    builder.append(", ");
                    employeeProfileUris.add(assignedEmployee.getEmployeeImageUrl());
                }
                for (Employee employee : ticket.getContributorList()) {
                    if (!localAccountId.equals(employee.getAccountId())) {
                        builder.append(employee.getName());
                        builder.append(", ");
                        employeeProfileUris.add(employee.getEmployeeImageUrl());
                    }
                }
                String assignedEmployeeList = builder.toString().trim();
                String callees = GlobalUtils.removeLastCharater(assignedEmployeeList);


                Intent i = new Intent(getActivity(), TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                i.putExtra("ticket_desc", ticket.getTitle());
                i.putExtra("selected_ticket_type", Constants.SUBSCRIBED);
                i.putExtra("selected_ticket_name", callees);
                i.putExtra("selected_ticket_status", ticket.getTicketStatus());
                i.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);
                startActivity(i);
            });

            adapter.setOnUnsubscribeListener((id, pos) -> {
                unsubscribedTicketPos = pos;
                showUnsubscribeDialog(id);
            });

            rvSubscribeTickets.setAdapter(adapter);
        } else {
            GlobalUtils.showLog(TAG, "data not found");
            rvSubscribeTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }

        rvSubscribeTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabSubscribe.isShown())
                    fabSubscribe.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabSubscribe.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    private void showUnsubscribeDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to unsubscribe to this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.unsubscribeTicket(Long.parseLong(ticketId));
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                });


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(R.color.colorPrimary));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(android.R.color.holo_red_dark));

        });
        alert11.show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh subscribe called");

                    presenter.getSubscribedTickets(false, 0, System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

 /*   @Override
    public void showSubscribeTickets(List<Tickets> subscribeTicketList) {
        setUpRecyclerView(subscribeTicketList);
    }*/

    @OnClick(R.id.fab_subscribe)
    void subscribe() {
        Intent i = new Intent(getActivity(), UnSubscribedTicketsActivity.class);
        startActivity(i);
    }

    @Override
    public void getSubscribedTicketsSuccess() {
        List<Tickets> subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
        setUpRecyclerView(subscribedTickets);
        Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, false);
        fetchList = true;
    }


    @Override
    public void getSubscribedTicketsFail(String msg) {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvSubscribeTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);

    }

    @Override
    public void onUnsubscribeSuccess(long ticketId) {
        adapter.deleteItem(unsubscribedTicketPos, ticketId);
        Hawk.put(Constants.FETCH_SUBSCRIBEABLE_LIST, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean fetchChanges = Hawk.get(Constants.FETCH_SUBSCRIBED_LIST, false);
        if (fetchChanges) {
            GlobalUtils.showLog(TAG, "on resume fetch");
            presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
        } else {
            boolean ticketAssigned = Hawk.get(Constants.TICKET_ASSIGNED, false);
            if (ticketAssigned) {
                subscribedTickets = TicketRepo.getInstance().getSubscribedTickets();
                setUpRecyclerView(subscribedTickets);
                Hawk.put(Constants.TICKET_ASSIGNED, false);
            }
        }
    }

    @Override
    public void onUnsubscribeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);

    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvSubscribeTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvSubscribeTickets.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                message);
    }

    @Override
    public void updateSubscribedList(List<Tickets> ticketsList) {
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void updateSubscribedList() {
        presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
    }

    @Override
    public void fetchList() {
        presenter.getSubscribedTickets(true, 0, System.currentTimeMillis(), 100);
    }
}

