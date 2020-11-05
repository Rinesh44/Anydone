package com.treeleaf.anydone.serviceprovider.contributed;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class ContributedTicketsActivity extends MvpBaseActivity<ContributedTicketPresenterImpl>
        implements ContributedTicketContract.ContributedTicketView {
    private static final String TAG = "ContributedTicketFragme";
    @BindView(R.id.rv_contributed_tickets)
    RecyclerView rvContributedTickets;
    @BindView(R.id.swipe_refresh_contributed_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    private TicketsAdapter adapter;
    private List<Tickets> contributedTickets;
    private boolean fetchList = false;
    private Account userAccount;
    private String localAccountId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        setToolbar();
        contributedTickets = TicketRepo.getInstance().getContributedTickets();

        if (CollectionUtils.isEmpty(contributedTickets)) {
            ivDataNotFound.setVisibility(View.GONE);
            rvContributedTickets.setVisibility(View.VISIBLE);
            presenter.getContributedTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(contributedTickets);
        }

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh contributed called");

                    presenter.getContributedTickets(false, 0,
                            System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 setOnItemClickListener
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_contributed_tickets;
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvContributedTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvContributedTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ArrayList<String> employeeProfileUris = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    AssignEmployee assignedEmployee = ticket.getAssignedEmployee();
                    String assignedEmployeeName = assignedEmployee.getName();

                    Customer customer = ticket.getCustomer();
                    String customerName = customer.getFullName();

                    if (!localAccountId.equals(customer.getCustomerId()) && !customerName.isEmpty()) {
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
                    for (AssignEmployee employee : ticket.getContributorList()) {
                        if (!localAccountId.equals(employee.getAccountId())) {
                            builder.append(employee.getName());
                            builder.append(", ");
                            employeeProfileUris.add(employee.getEmployeeImageUrl());
                        }
                    }

                    String assignedEmployeeList = builder.toString().trim();
                    String callees = GlobalUtils.removeLastCharater(assignedEmployeeList);

                    Intent i = new Intent(this, TicketDetailsActivity.class);
                    i.putExtra("selected_ticket_id", ticket.getTicketId());
                    i.putExtra("selected_ticket_type", Constants.CONTRIBUTED);
                    i.putExtra("ticket_desc", ticket.getTitle());
                    i.putExtra("selected_ticket_name", callees);
                    i.putExtra("contributed", true);
                    i.putExtra("selected_ticket_status", ticket.getTicketStatus());
                    i.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);
                    startActivity(i);
                } else {
                    Banner.make(getWindow().getDecorView().getRootView(),
                            this, Banner.INFO, "Some of our features are not " +
                                    "supported in your device. " +
                                    "Sorry for inconvenience",
                            Banner.TOP, 2000).show();
                }
            });
            rvContributedTickets.setAdapter(adapter);
        } else {
            rvContributedTickets.setVisibility(View.GONE);
//            final Handler handler = new Handler();

      /*      handler.postDelayed(() -> {
                if (rvOpenTickets != null) {
                    if (rvOpenTickets.getVisibility() != View.VISIBLE)
                        ivDataNotFound.setVisibility(View.VISIBLE);
                    else ivDataNotFound.setVisibility(View.GONE);
                }
            }, 50);*/

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean fetchChanges = Hawk.get(Constants.FETCH_CONTRIBUTED_LIST, false);
        if (fetchChanges) {
            presenter.getContributedTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else {
            boolean ticketContributed = Hawk.get(Constants.TICKET_CONTRIBUTED, false);
            if (ticketContributed) {
                contributedTickets = TicketRepo.getInstance().getContributedTickets();
                setUpRecyclerView(contributedTickets);
                Hawk.put(Constants.TICKET_CONTRIBUTED, false);
            }
        }
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @Override
    public void getContributedTicketSuccess() {
        List<Tickets> contributedTickets = TicketRepo.getInstance().getContributedTickets();
        setUpRecyclerView(contributedTickets);
        Hawk.put(Constants.FETCH_CONTRIBUTED_LIST, false);
        fetchList = true;
    }

    @Override
    public void getContributedTicketFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to get contributed tickets");
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvContributedTickets.setVisibility(View.GONE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
   /*     UiUtils.showSnackBar(getContext(), Objects.requireNonNull(getActivity()).getWindow()
                .getDecorView().getRootView(), msg);*/
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvContributedTickets.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
            rvContributedTickets.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Contributed Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

