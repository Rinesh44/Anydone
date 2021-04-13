package com.treeleaf.anydone.serviceprovider.tickets.pendingtickets;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
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
import com.google.android.material.button.MaterialButton;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.fragment.BaseFragment;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.tickets.TicketsFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PendingTicketsFragment extends BaseFragment<PendingTicketPresenterImpl>
        implements PendingTicketContract.PendingTicketView,
        TicketsFragment.PendingListListener {
    private static final String TAG = "OpenTicketsFragment";
    @BindView(R.id.rv_open_tickets)
    RecyclerView rvOpenTickets;
    @BindView(R.id.swipe_refresh_open_tickets)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    /*    @BindView(R.id.fab_backlog)
        FloatingActionButton fabAssign;*/
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;

    private Unbinder unbinder;
    private TicketsAdapter adapter;
    private List<Tickets> assignedTickets;
    private boolean fetchList = false;
    private Account userAccount;
    private String localAccountId;
    private int startTicketPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setPendingListListener(this);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        if (fetchList) {
        assignedTickets = TicketRepo.getInstance().getPendingTickets();

        swipeRefreshLayout.setDistanceToTriggerSync(400);
        if (CollectionUtils.isEmpty(assignedTickets)) {
            presenter.getPendingTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(assignedTickets);
        }
//        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_open_tickets;
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
        GlobalUtils.showLog(TAG, "setup recycler view called");
        rvOpenTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvOpenTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext(), rvOpenTickets);
            adapter.setOnItemClickListener(ticket -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    ArrayList<String> employeeProfileUris = new ArrayList<>();
                    StringBuilder builder = new StringBuilder();
                    AssignEmployee assignedEmployee = ticket.getAssignedEmployee();
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
                    for (AssignEmployee employee : ticket.getContributorList()) {
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
                    i.putExtra("selected_ticket_type", Constants.PENDING);
                    i.putExtra("ticket_desc", ticket.getTitle());
                    i.putExtra("selected_ticket_name", callees);
                    i.putExtra("selected_ticket_index", ticket.getTicketIndex());
                    i.putExtra("selected_ticket_status", ticket.getTicketStatus());
                    i.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);
                    startActivity(i);
                } else {
                    Banner.make(Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                            getActivity(), Banner.INFO, "Some of our features are not supported in your device. " +
                                    "Sorry for inconvenience",
                            Banner.TOP, 2000).show();
                }
            });
            rvOpenTickets.setAdapter(adapter);


            adapter.setOnStartListener((id, pos) -> {
                startTicketPos = pos;
                showStartDialog(id);
            });

        } else {
            rvOpenTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.VISIBLE);
      /*      handler.postDelayed(() -> {
                if (rvOpenTickets != null) {
                    if (rvOpenTickets.getVisibility() != View.VISIBLE)
                        ivDataNotFound.setVisibility(View.VISIBLE);
                    else ivDataNotFound.setVisibility(View.GONE);
                }
            }, 50);*/

        }

      /*  rvOpenTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabAssign.isShown())
                    fabAssign.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabAssign.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });*/
    }

    private void showStartDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to start this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.startTicket(Long.parseLong(ticketId));
//                    presenter.reopenTicket(Long.parseLong(ticketId));
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
    public void onResume() {
        super.onResume();
        GlobalUtils.showLog(TAG, "onresume called");
        boolean fetchChanges = Hawk.get(Constants.FETCH_PENDING_LIST, false);
        boolean ticketAssigned = Hawk.get(Constants.TICKET_ASSIGNED, false);
        boolean ticketPending = Hawk.get(Constants.TICKET_PENDING, false);
        if (fetchChanges) {
            GlobalUtils.showLog(TAG, "first");
            btnReload.setVisibility(View.GONE);
            presenter.getPendingTickets(true, 0,
                    System.currentTimeMillis(), 100);
        } else if (ticketAssigned) {
            GlobalUtils.showLog(TAG, "second");
            assignedTickets = TicketRepo.getInstance().getPendingTickets();
            setUpRecyclerView(assignedTickets);
            Hawk.put(Constants.TICKET_ASSIGNED, false);
        } else if (ticketPending) {
            GlobalUtils.showLog(TAG, "third");
            assignedTickets = TicketRepo.getInstance().getPendingTickets();
            setUpRecyclerView(assignedTickets);
            Hawk.put(Constants.TICKET_PENDING, false);
        }
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
                    GlobalUtils.showLog(TAG, "swipe refresh assigned called");

                    presenter.getPendingTickets(false, 0,
                            System.currentTimeMillis(), 100);

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
    public void getPendingTicketSuccess() {
        List<Tickets> assignedTickets = TicketRepo.getInstance().getPendingTickets();
        setUpRecyclerView(assignedTickets);
        Hawk.put(Constants.FETCH_PENDING_LIST, false);
        fetchList = true;
    }

    @Override
    public void getPendingTicketFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to get assigned tickets");
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvOpenTickets.setVisibility(View.GONE);
        btnReload.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
   /*     UiUtils.showSnackBar(getContext(), Objects.requireNonNull(getActivity()).getWindow()
                .getDecorView().getRootView(), msg);*/
    }

    @Override
    public void showEmptyView() {
        GlobalUtils.showLog(TAG, "show empty view");
        rvOpenTickets.setVisibility(View.GONE);
        ivDataNotFound.setVisibility(View.VISIBLE);

        if (ivDataNotFound.getVisibility() == View.VISIBLE)
            btnReload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTicketStartSuccess(long ticketId, long estTime) {
        Hawk.put(Constants.TICKET_IN_PROGRESS, true);
        adapter.deleteItem(startTicketPos, ticketId);
        TicketRepo.getInstance().changeTicketStatusToStart(ticketId, estTime);
        TicketRepo.getInstance().setTicketEstTime(ticketId, estTime);

        rvOpenTickets.setVisibility(View.VISIBLE);
        assignedTickets = TicketRepo.getInstance().getPendingTickets();
//        assignedTickets = TicketRepo.getInstance().getPendingTickets();
        if (assignedTickets.isEmpty()) {
            ivDataNotFound.setVisibility(View.VISIBLE);
        } else {
            ivDataNotFound.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTicketStartFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to start ticket");
        btnReload.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvOpenTickets.setVisibility(View.GONE);
        btnReload.setVisibility(View.GONE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(getContext(),
                Objects.requireNonNull(getActivity()).getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

 /*   @OnClick(R.id.fab_backlog)
    void getBackLogTickets() {
        Intent i = new Intent(getActivity(), UnassignedTicketsActivity.class);
        startActivity(i);
    }*/

    @OnClick(R.id.btn_reload)
    void reload() {
        btnReload.setVisibility(View.GONE);
        ivDataNotFound.setVisibility(View.GONE);
        presenter.getPendingTickets(true, 0,
                System.currentTimeMillis(), 100);
    }

    @Override
    public void updatePendingList(List<Tickets> ticketsList) {
        GlobalUtils.showLog(TAG, "interface implemented");
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void updatePendingList() {
        btnReload.setVisibility(View.GONE);
        presenter.getPendingTickets(true, 0,
                System.currentTimeMillis(), 100);
    }

    @Override
    public void fetchList() {
        if (btnReload != null) btnReload.setVisibility(View.GONE);
        GlobalUtils.showLog(TAG, "fetch list called");
        presenter.getPendingTickets(true, 0,
                System.currentTimeMillis(), 100);
    }
}


