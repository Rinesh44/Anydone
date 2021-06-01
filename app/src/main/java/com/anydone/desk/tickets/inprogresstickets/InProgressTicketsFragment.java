package com.anydone.desk.tickets.inprogresstickets;

import android.app.AlertDialog;
import android.content.Context;
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
import com.anydone.desk.R;
import com.anydone.desk.adapters.TicketsAdapter;
import com.anydone.desk.base.fragment.BaseFragment;
import com.anydone.desk.injection.component.ApplicationComponent;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.AssignEmployee;
import com.anydone.desk.realm.model.Customer;
import com.anydone.desk.realm.model.Tickets;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.TicketRepo;
import com.anydone.desk.ticketdetails.TicketDetailsActivity;
import com.anydone.desk.tickets.TicketsFragment;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

public class InProgressTicketsFragment extends BaseFragment<InProgressTicketPresenterImpl>
        implements InProgressTicketContract.InProgressTicketsView,
        TicketsFragment.InProgressListListener {
    private static final String TAG = "InProgressTicketsFragme";
    @BindView(R.id.rv_subscribe_tickets)
    RecyclerView rvInProgressTickets;
 /*   @BindView(R.id.swipe_refresh_subscribe_tickets)
    SwipeRefreshLayout swipeRefreshLayout;*/
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    /*    @BindView(R.id.fab_backlog)
        FloatingActionButton fabSubscribe;*/
    @BindView(R.id.pb_search)
    ProgressBar progressBar;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.btn_reload)
    MaterialButton btnReload;

    private Unbinder unbinder;
    private OnInProgressTicketsListener onInProgressTicketsListener;
    private TicketsAdapter adapter;
    private int unsubscribedTicketPos;
    private boolean fetchList = false;
    private List<Tickets> inProgressTickets;
    private Account userAccount;
    private String localAccountId;
    private int closeTicketPos, resolveTicketPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TicketsFragment mFragment = (TicketsFragment) getParentFragment();
        assert mFragment != null;
        mFragment.setInProgressListListener(this);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        if (fetchList) {
        inProgressTickets = TicketRepo.getInstance().getInProgressTickets();

//        swipeRefreshLayout.setDistanceToTriggerSync(400);
        if (CollectionUtils.isEmpty(inProgressTickets)) {
            GlobalUtils.showLog(TAG, "in progress tickets empty");
            presenter.getInProgressTickets(true, 0, System.currentTimeMillis(),
                    100);
        } else {
            setCount(inProgressTickets.size());
            setUpRecyclerView(inProgressTickets);
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
        rvInProgressTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvInProgressTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            btnReload.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext(), rvInProgressTickets);
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
                    /*for (AssignEmployee employee : ticket.getContributorList()) {
                        if (!localAccountId.equals(employee.getAccountId())) {
                            builder.append(employee.getName());
                            builder.append(", ");
                            employeeProfileUris.add(employee.getEmployeeImageUrl());
                        }
                    }*/
                    String assignedEmployeeList = builder.toString().trim();
                    String callees = GlobalUtils.removeLastCharater(assignedEmployeeList);

                    Intent i = new Intent(getActivity(), TicketDetailsActivity.class);
                    i.putExtra("selected_ticket_id", ticket.getTicketId());
                    i.putExtra("ticket_desc", ticket.getTitle());
                    i.putExtra("selected_ticket_type", Constants.IN_PROGRESS);
                    i.putExtra("selected_ticket_index", ticket.getTicketIndex());
                    i.putExtra("selected_ticket_name", callees);
                    i.putExtra("selected_ticket_status", ticket.getTicketStatus());
                    i.putStringArrayListExtra("selected_ticket_icon_uri", employeeProfileUris);
                    startActivity(i);
                } else {
                    Banner.make(Objects.requireNonNull
                                    (getActivity()).getWindow().getDecorView().getRootView(),
                            getActivity(), Banner.INFO,
                            "Some of our features are not supported in your device. " +
                                    "Sorry for inconvenience",
                            Banner.TOP, 2000).show();
                }
            });

            adapter.setOnCloseListener((id, pos) -> {
                closeTicketPos = pos;
                showCloseTicket(id);
            });

            adapter.setOnResolveListener((id, pos) -> {
                resolveTicketPos = pos;
                showResolveTicket(id);
            });

            rvInProgressTickets.setAdapter(adapter);
        } else {
            GlobalUtils.showLog(TAG, "data not found");
            rvInProgressTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
            btnReload.setVisibility(View.VISIBLE);
        }

 /*       rvInProgressTickets.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
        });*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
/*        swipeRefreshLayout.setOnRefreshListener(
                () -> {
                    GlobalUtils.showLog(TAG, "swipe refresh in-progress called");

                    presenter.getInProgressTickets(false, 0,
                            System.currentTimeMillis(), 100);

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        //Do something after 1 sec
                        if (swipeRefreshLayout != null)
                            swipeRefreshLayout.setRefreshing(false);
                    }, 1000);
                }
        );*/
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

 /*   @Override
    public void showSubscribeTickets(List<Tickets> subscribeTicketList) {
        setUpRecyclerView(subscribeTicketList);
    }*/

/*    @OnClick(R.id.fab_backlog)
    void getBackLogTickets() {
        Intent i = new Intent(getActivity(), UnassignedTicketsActivity.class);
        startActivity(i);
    }*/

    @Override
    public void getInProgressTicketsSuccess() {
        List<Tickets> inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
        setCount(inProgressTickets.size());
        setUpRecyclerView(inProgressTickets);
        Hawk.put(Constants.FETCH_IN_PROGRESS_LIST, false);
        fetchList = true;
    }


    @Override
    public void getInProgressTicketsFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }

        ivDataNotFound.setVisibility(View.VISIBLE);
        btnReload.setVisibility(View.VISIBLE);
        rvInProgressTickets.setVisibility(View.GONE);
//        UiUtils.showSnackBar(getContext(), getActivity().getWindow().getDecorView().getRootView(), msg);

    }

    @Override
    public void showEmptyView() {
        ivDataNotFound.setVisibility(View.VISIBLE);
        rvInProgressTickets.setVisibility(View.GONE);

        if (ivDataNotFound.getVisibility() == View.VISIBLE)
            btnReload.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCloseTicketSuccess(long ticketId) {
        btnReload.setVisibility(View.GONE);
        Hawk.put(Constants.TICKET_RESOLVED, true);
        adapter.deleteItem(closeTicketPos, ticketId);
        TicketRepo.getInstance().changeTicketStatusToClosed(ticketId);

        if (inProgressTickets.isEmpty()) {
            rvInProgressTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        } else {
            rvInProgressTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
        }

        inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
        setCount(inProgressTickets.size());

        List<Tickets> closed = TicketRepo.getInstance().getClosedResolvedTickets();
        TicketsFragment parent = (TicketsFragment) getParentFragment();
        if (parent != null)
            parent.setResolvedCount(closed.size());
    }

    @Override
    public void onCloseTicketFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to close ticket");
        btnReload.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
    }

    private void setCount(int size) {
        TicketsFragment parent = (TicketsFragment) getParentFragment();
        if (parent != null)
            parent.setInProgressCount(size);
    }

    @Override
    public void onResolveTicketSuccess(long ticketId) {
        btnReload.setVisibility(View.GONE);
        Hawk.put(Constants.TICKET_RESOLVED, true);
        adapter.deleteItem(resolveTicketPos, ticketId);
        TicketRepo.getInstance().changeTicketStatusToResolved(ticketId);

        rvInProgressTickets.setVisibility(View.VISIBLE);

        inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
        setCount(inProgressTickets.size());
        if (inProgressTickets.isEmpty()) {
            rvInProgressTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        } else {
            rvInProgressTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
        }

        List<Tickets> closed = TicketRepo.getInstance().getClosedResolvedTickets();
        TicketsFragment parent = (TicketsFragment) getParentFragment();
        if (parent != null)
            parent.setResolvedCount(closed.size());
    }

    @Override
    public void onResolveTicketFail(String msg) {
        GlobalUtils.showLog(TAG, "failed to close ticket");
        btnReload.setVisibility(View.VISIBLE);
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(getContext(), msg);
            onAuthorizationFailed(getContext());
            return;
        }
    }

    @OnClick(R.id.btn_reload)
    void reload() {
        btnReload.setVisibility(View.GONE);
        ivDataNotFound.setVisibility(View.GONE);
        presenter.getInProgressTickets(true, 0,
                System.currentTimeMillis(), 100);
    }


    @Override
    public void onResume() {
        super.onResume();
        GlobalUtils.showLog(TAG, "in progress on resume called");
        boolean fetchChanges = Hawk.get(Constants.FETCH_IN_PROGRESS_LIST, false);
        boolean ticketAssigned = Hawk.get(Constants.TICKET_ASSIGNED, false);
        boolean ticketInProgress = Hawk.get(Constants.TICKET_IN_PROGRESS, false);
        if (fetchChanges) {
            GlobalUtils.showLog(TAG, "on resume fetch");
            btnReload.setVisibility(View.GONE);
            presenter.getInProgressTickets(true, 0, System.currentTimeMillis(),
                    100);
        } else if (ticketAssigned) {
            inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
            setCount(inProgressTickets.size());
            setUpRecyclerView(inProgressTickets);
            Hawk.put(Constants.TICKET_ASSIGNED, false);
        } else if (ticketInProgress) {
            inProgressTickets = TicketRepo.getInstance().getInProgressTickets();
            setCount(inProgressTickets.size());
            setUpRecyclerView(inProgressTickets);
            Hawk.put(Constants.TICKET_IN_PROGRESS, false);
        }
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
        ivDataNotFound.setVisibility(View.GONE);
        rvInProgressTickets.setVisibility(View.GONE);
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


    @Override
    public void updateInProgressList(List<Tickets> ticketsList) {
        GlobalUtils.showLog(TAG, "in progress interface implemented");
        setCount(ticketsList.size());
        setUpRecyclerView(ticketsList);
    }

    @Override
    public void updateInProgressList() {
        btnReload.setVisibility(View.GONE);
        presenter.getInProgressTickets(true, 0,
                System.currentTimeMillis(), 100);
    }

    @Override
    public void fetchList() {
        if (btnReload != null) btnReload.setVisibility(View.GONE);
        presenter.getInProgressTickets(true, 0,
                System.currentTimeMillis(), 100);
    }

    private void showCloseTicket(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to close this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.closeTicket(Long.parseLong(ticketId));
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

    private void showResolveTicket(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
        builder1.setMessage("Are you sure you want to resolve this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.resolveTicket(Long.parseLong(ticketId));
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
}

