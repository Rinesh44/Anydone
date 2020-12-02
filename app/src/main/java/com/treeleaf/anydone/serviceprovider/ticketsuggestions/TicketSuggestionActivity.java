package com.treeleaf.anydone.serviceprovider.ticketsuggestions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.SuggestedTicketAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketSuggestionRepo;
import com.treeleaf.anydone.serviceprovider.suggestedTicketPreview.SuggestedTicketPreviewActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class TicketSuggestionActivity extends MvpBaseActivity<TicketSuggestionPresenterImpl> implements
        TicketSuggestionContract.TicketSuggestionView {
    private static final String TAG = "TicketSuggestionActivit";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_accept_all)
    TextView tvAcceptAll;
    @BindView(R.id.tv_reject_all)
    TextView tvRejectAll;
    @BindView(R.id.rv_suggested_tickets)
    RecyclerView rvSuggestedTickets;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_selected_ticket_count)
    TextView tvSelectedTicketCount;
    @BindView(R.id.cb_select_all)
    CheckBox cbSelectAll;
    @BindView(R.id.tv_no_suggestions)
    TextView tvNoSuggestions;

    private ProgressDialog progress;
    List<TicketSuggestion> suggestedTickets = new ArrayList<>();
    private SuggestedTicketAdapter adapter;
    private List<TicketSuggestion> ticketSuggestionList;

    @Override
    protected int getLayout() {
        return R.layout.activity_ticket_suggestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ivBack.setOnClickListener(v -> onBackPressed());
        tvAcceptAll.setOnClickListener(v -> {
            if (suggestedTickets.isEmpty()) {
                Toast.makeText(TicketSuggestionActivity.this,
                        "Please select ticket suggestions to accept", Toast.LENGTH_SHORT).show();
            } else {
                String count = String.valueOf(suggestedTickets.size());
                if (Integer.parseInt(count) > 1)
                    showAcceptDialog(suggestedTickets, "Accept " + count + " selected " +
                            "suggestions?");
                else
                    showAcceptDialog(suggestedTickets, "Accept " + count + " selected " +
                            "suggestion?");
            }
        });

        tvRejectAll.setOnClickListener(v -> {
            if (suggestedTickets.isEmpty()) {
                Toast.makeText(TicketSuggestionActivity.this,
                        "Please select ticket suggestions to reject", Toast.LENGTH_SHORT).show();
            } else {
                String count = String.valueOf(suggestedTickets.size());
                if (Integer.parseInt(count) > 1)
                    showRejectDialog(suggestedTickets, "Reject " + count + " selected " +
                            "suggestions?");
                else
                    showRejectDialog(suggestedTickets, "Reject " + count + " selected " +
                            "suggestion?");
            }
        });

        cbSelectAll.setOnClickListener(v -> {
            boolean isChecked = cbSelectAll.isChecked();
            adapter.selectAll(isChecked);
            suggestedTickets.clear();
            if (isChecked) {
                suggestedTickets.addAll(ticketSuggestionList);
                enableAcceptReject();
                tvSelectedTicketCount.setVisibility(View.VISIBLE);
                setItemCount();
            } else {
                disableAcceptReject();
                tvSelectedTicketCount.setVisibility(View.GONE);
            }

        });

    /*    cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            adapter.selectAll(isChecked);
            if (isChecked) {
                suggestedTickets.clear();
                suggestedTickets.addAll(ticketSuggestionList);
                enableAcceptReject();
            } else {
                suggestedTickets.clear();
                disableAcceptReject();
            }
            setItemCount();
        });*/

    }

    private void setUpRecyclerView(List<TicketSuggestion> ticketSuggestionList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSuggestedTickets.setLayoutManager(mLayoutManager);

        adapter = new SuggestedTicketAdapter(ticketSuggestionList, this);
        rvSuggestedTickets.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuggestedTicketAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(TicketSuggestion ticketSuggestion) {
                GlobalUtils.showLog(TAG, "item add listen");
                suggestedTickets.add(ticketSuggestion);
                GlobalUtils.showLog(TAG, "suggested ticket list size: " + suggestedTickets.size());
                if (suggestedTickets.size() > 0) {
                    enableAcceptReject();
                }

                tvSelectedTicketCount.setVisibility(View.VISIBLE);
                setItemCount();
            }

            @Override
            public void onItemRemove(TicketSuggestion ticketSuggestion) {
                GlobalUtils.showLog(TAG, "item remove listen");
                suggestedTickets.remove(ticketSuggestion);
                GlobalUtils.showLog(TAG, "suggested ticket list size: " + suggestedTickets.size());
                if (suggestedTickets.size() == 0) {
                    disableAcceptReject();
                }

                setItemCount();
                if (suggestedTickets.size() == 0) tvSelectedTicketCount.setVisibility(View.GONE);
                if (cbSelectAll.isChecked()) cbSelectAll.setChecked(false);
            }

            @Override
            public void onAccept(TicketSuggestion ticketSuggestion) {
                showAcceptDialog(ticketSuggestion);
            }

            @Override
            public void onReject(TicketSuggestion ticketSuggestion) {
                showRejectDialog(ticketSuggestion);
            }

            @Override
            public void showHistory(String ticketSuggestionId, String msgId, String customerName,
                                    String customerImage, String messageText, long sentAt) {
                Intent i = new Intent(TicketSuggestionActivity.this, SuggestedTicketPreviewActivity.class);
                i.putExtra("suggestion_id", ticketSuggestionId);
                i.putExtra("msg_id", msgId);
                i.putExtra("customer_name", customerName);
                i.putExtra("customer_image", customerImage);
                i.putExtra("msg_text", messageText);
                i.putExtra("sent_at", sentAt);
                startActivity(i);
            }
        });
    }

    private void setItemCount() {
        StringBuilder selectedTicketCount = new StringBuilder(String.valueOf(suggestedTickets.size()));
        selectedTicketCount.append(" selected");
        tvSelectedTicketCount.setText(selectedTicketCount);
    }


    private void showAcceptDialog(TicketSuggestion ticketSuggestion) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Accept ticket suggestion?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.acceptTicketSuggestion(ticketSuggestion);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void showRejectDialog(TicketSuggestion ticketSuggestion) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Reject ticket suggestion?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.rejectTicketSuggestion(ticketSuggestion);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void showAcceptDialog(List<TicketSuggestion> ticketSuggestions, String dialogTitle) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(dialogTitle);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.acceptTicketSuggestion(ticketSuggestions);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }

    private void showRejectDialog(List<TicketSuggestion> ticketSuggestions, String dialogTitle) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(dialogTitle);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.rejectTicketSuggestion(ticketSuggestions);
                    dialog.dismiss();
                });

        builder1.setNegativeButton(
                "Cancel",
                (dialog, id) -> dialog.dismiss());


        final AlertDialog alert11 = builder1.create();
        alert11.setOnShowListener(dialogInterface -> {
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.transparent));
            alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        alert11.show();
    }


    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        pbProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        pbProgress.setVisibility(View.GONE);
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

    private void enableAcceptReject() {
        tvAcceptAll.setClickable(true);
        tvRejectAll.setClickable(true);
        tvAcceptAll.setTextColor(getResources().getColor(R.color.service_status_green));
        tvRejectAll.setTextColor(getResources().getColor(R.color.service_status_red));
    }

    private void disableAcceptReject() {
        tvAcceptAll.setClickable(false);
        tvRejectAll.setClickable(false);
        tvAcceptAll.setTextColor(getResources().getColor(R.color.btn_disabled));
        tvRejectAll.setTextColor(getResources().getColor(R.color.btn_disabled));
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketSuggestionList = TicketSuggestionRepo.getInstance().getAllTicketSuggestions();
        setUpRecyclerView(ticketSuggestionList);
        GlobalUtils.showLog(TAG, "ticket suggesti: " + ticketSuggestionList);

    }

    @Override
    public void acceptTicketSuggestionSuccess(List<TicketSuggestion> suggestionList) {
        adapter.removeSuggestionList(suggestionList);
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionList);
        if (adapter.getItemCount() == 0) tvNoSuggestions.setVisibility(View.VISIBLE);

        Hawk.put(Constants.SUGGESTION_ACCEPTED, true);
    }

    @Override
    public void acceptTicketSuggestionFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void rejectTicketSuggestionSuccess(List<TicketSuggestion> suggestionList) {
        adapter.removeSuggestionList(suggestionList);
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionList);
        if (adapter.getItemCount() == 0) tvNoSuggestions.setVisibility(View.VISIBLE);

        Hawk.put(Constants.SUGGESTION_REJECTED, true);
    }

    @Override
    public void rejectTicketSuggestionFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void acceptParticularTicketSuggestionSuccess(TicketSuggestion suggestion) {
        adapter.removeSuggestion(suggestion);
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestion.getSuggestionId());
        if (adapter.getItemCount() == 0) tvNoSuggestions.setVisibility(View.VISIBLE);

        Hawk.put(Constants.SUGGESTION_ACCEPTED, true);
    }

    @Override
    public void rejectParticularTicketSuggestionSuccess(TicketSuggestion suggestion) {
        adapter.removeSuggestion(suggestion);
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestion.getSuggestionId());
        if (adapter.getItemCount() == 0) tvNoSuggestions.setVisibility(View.VISIBLE);

        Hawk.put(Constants.SUGGESTION_REJECTED, true);
    }

    @Override
    public void rejectParticularTicketSuggestionFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void acceptParticularTicketSuggestionFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }
}