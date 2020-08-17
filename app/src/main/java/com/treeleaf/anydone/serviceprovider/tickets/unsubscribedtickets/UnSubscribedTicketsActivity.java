package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

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
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.TicketDetailsActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class UnSubscribedTicketsActivity extends MvpBaseActivity<UnsubscribedTicketPresenterImpl>
        implements UnsubscribedTicketContract.UnsubscribedView {

    private ProgressDialog progress;
    @BindView(R.id.rv_unsubscribed_tickets)
    RecyclerView rvSubscribeableTickets;
    private TicketsAdapter adapter;
    @BindView(R.id.iv_data_not_found)
    ImageView ivDataNotFound;
    private int subscribeTicketPos;

    @Override
    protected int getLayout() {
        return R.layout.activity_un_subscribed_tickets;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        List<Tickets> subscribeableTickets = TicketRepo.getInstance().getSubscribeableTickets();

        boolean fetchChanges = Hawk.get(Constants.FETCH_SUBSCRIBEABLE_LIST, false);
        if (CollectionUtils.isEmpty(subscribeableTickets) || fetchChanges) {
            presenter.getSubscribeableTickets(0, System.currentTimeMillis(), 100);
        } else {
            setUpRecyclerView(subscribeableTickets);
        }

    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Subscribe Tickets");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpRecyclerView(List<Tickets> ticketsList) {
        rvSubscribeableTickets.setLayoutManager(new LinearLayoutManager(getContext()));
        if (!CollectionUtils.isEmpty(ticketsList)) {
            rvSubscribeableTickets.setVisibility(View.VISIBLE);
            ivDataNotFound.setVisibility(View.GONE);
            adapter = new TicketsAdapter(ticketsList, getContext());
            adapter.setOnItemClickListener(ticket -> {
                Intent i = new Intent(this, TicketDetailsActivity.class);
                i.putExtra("selected_ticket_id", ticket.getTicketId());
                startActivity(i);
            });

            adapter.setOnSubscribeListener((id, pos) -> {
                subscribeTicketPos = pos;
                showSubscribeDialog(id);
            });
            rvSubscribeableTickets.setAdapter(adapter);
        } else {
            rvSubscribeableTickets.setVisibility(View.GONE);
            ivDataNotFound.setVisibility(View.VISIBLE);
        }
    }

    private void showSubscribeDialog(String ticketId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure you want to subscribe to this ticket?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                (dialog, id) -> {
                    adapter.closeSwipeLayout(ticketId);
                    dialog.dismiss();
                    presenter.subscribe(Long.parseLong(ticketId));
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
    public void getSubscribeableTicketSuccess() {
        List<Tickets> subscribeableTickets = TicketRepo.getInstance().getSubscribeableTickets();

        if (CollectionUtils.isEmpty(subscribeableTickets)) {
            ivDataNotFound.setVisibility(View.VISIBLE);
        } else {
            setUpRecyclerView(subscribeableTickets);
        }

        Hawk.put(Constants.FETCH_SUBSCRIBEABLE_LIST, false);
    }

    @Override
    public void getSubscribeableTicketFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onSubscribeSuccess() {
        adapter.deleteItem(subscribeTicketPos);
        Hawk.put(Constants.FETCH_SUBSCRIBED_LIST, true);
    }

    @Override
    public void onSubscribeFail(String msg) {
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