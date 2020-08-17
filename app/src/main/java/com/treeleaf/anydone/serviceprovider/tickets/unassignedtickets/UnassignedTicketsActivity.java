package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketsAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Employee;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.EmployeeRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class UnassignedTicketsActivity extends MvpBaseActivity<UnassignedTicketPresenterImpl>
        implements UnassignedTicketsContract.UnassignedView {

    private static final String TAG = "UnassignedTicketsActivi";
    private ProgressDialog progress;
    @BindView(R.id.rv_assignable_tickets)
    RecyclerView rvAssignableTickets;
    private TicketsAdapter adapter;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    private int assignTicketPos;

    @Override
    protected int getLayout() {
        return R.layout.activity_unassigned_tickets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        List<Tickets> assignableTickets = TicketRepo.getInstance().getAssignableTickets();

        if (CollectionUtils.isEmpty(assignableTickets)) {
            presenter.getAssignableTickets(0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(assignableTickets);
        }
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvAssignableTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvAssignableTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(this, TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                startActivity(i);
            });

            adapter.setOnAssignListener((id, pos) -> {
                assignTicketPos = pos;
                showAssignTicketDialog(id);
            });
            rvAssignableTickets.setAdapter(adapter);
        } else {
            rvAssignableTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void showAssignTicketDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Self assign ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    Employee employee = EmployeeRepo.getInstance().getEmployee();
                    GlobalUtils.showLog(TAG, "ticket Id check: " + ticketId);
                    GlobalUtils.showLog(TAG, "employee Id check: " + employee.getEmployeeId());
                    presenter.assignTicket(Long.parseLong(ticketId), employee.getEmployeeId());
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

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Assign Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void getAssignableTicketSuccess() {
        List<Tickets> assignableTickets = TicketRepo.getInstance().getAssignableTickets();

        if (CollectionUtils.isEmpty(assignableTickets)) {
            ivDataNotFound.setVisibility(View.VISIBLE);
        } else {
            setUpRecyclerView(assignableTickets);
        }
    }

    @Override
    public void getAssignableTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void assignSuccess() {
        adapter.deleteItem(assignTicketPos);
        Hawk.put(Constants.FETCH__ASSIGNED_LIST, true);
    }

    @Override
    public void assignFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return this;
    }
}