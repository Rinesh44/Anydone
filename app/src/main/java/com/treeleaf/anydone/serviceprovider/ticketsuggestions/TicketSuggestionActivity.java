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

    private static final String TAG = "AssignEmployeeActivity";
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

    private ProgressDialog progress;
    List<String> suggestedTicketIds = new ArrayList<>();
    private SuggestedTicketAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_ticket_suggestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<TicketSuggestion> ticketSuggestionList = TicketSuggestionRepo.getInstance().getAllTicketSuggestions();
        setUpRecyclerView(ticketSuggestionList);
        ivBack.setOnClickListener(v -> onBackPressed());
        tvAcceptAll.setOnClickListener(v -> {
            if (suggestedTicketIds.isEmpty()) {
                Toast.makeText(TicketSuggestionActivity.this,
                        "Please select ticket suggestions to accept", Toast.LENGTH_SHORT).show();
            } else {
                presenter.acceptTicketSuggestion(suggestedTicketIds);
            }
        });

        tvRejectAll.setOnClickListener(v -> {
            if (suggestedTicketIds.isEmpty()) {
                Toast.makeText(TicketSuggestionActivity.this,
                        "Please select ticket suggestions to reject", Toast.LENGTH_SHORT).show();
            } else {
                presenter.rejectTicketSuggestion(suggestedTicketIds);
            }
        });

        cbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> adapter.selectAll(isChecked));

    }

    private void setUpRecyclerView(List<TicketSuggestion> ticketSuggestionList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvSuggestedTickets.setLayoutManager(mLayoutManager);

        adapter = new SuggestedTicketAdapter(ticketSuggestionList, this);
        rvSuggestedTickets.setAdapter(adapter);

        adapter.setOnItemClickListener(new SuggestedTicketAdapter.OnItemClickListener() {
            @Override
            public void onItemAdd(String ticketSuggestionId) {
                GlobalUtils.showLog(TAG, "item add listen");
                suggestedTicketIds.add(ticketSuggestionId);
                GlobalUtils.showLog(TAG, "suggested ticket list size: " + suggestedTicketIds.size());
                if (suggestedTicketIds.size() > 0) {
                    enableAcceptReject();
                }

                StringBuilder selectedTicketCount = new StringBuilder(String.valueOf(suggestedTicketIds.size()));
                selectedTicketCount.append(" selected");
                tvSelectedTicketCount.setText(selectedTicketCount);
            }

            @Override
            public void onItemRemove(String ticketSuggestionId) {
                GlobalUtils.showLog(TAG, "item remove listen");
                suggestedTicketIds.remove(ticketSuggestionId);
                GlobalUtils.showLog(TAG, "suggested ticket list size: " + suggestedTicketIds.size());
                if (suggestedTicketIds.size() == 0) {
                    disableAcceptReject();
                }

                StringBuilder selectedTicketCount = new StringBuilder(String.valueOf(suggestedTicketIds.size()));
                selectedTicketCount.append(" selected");
                tvSelectedTicketCount.setText(selectedTicketCount);
            }

            @Override
            public void onAccept(String ticketSuggestionId) {
                showAcceptDialog(ticketSuggestionId);
            }

            @Override
            public void onReject(String ticketSuggestionId) {
                showRejectDialog(ticketSuggestionId);
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


    private void showAcceptDialog(String ticketSuggestionId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Accept ticket suggestion?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.acceptTicketSuggestion(ticketSuggestionId);
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

    private void showRejectDialog(String ticketSuggestionId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Reject ticket suggestion?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Ok",
                (dialog, id) -> {
                    presenter.rejectTicketSuggestion(ticketSuggestionId);
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
    public void acceptTicketSuggestionSuccess() {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestedTicketIds);
        adapter.notifyDataSetChanged();
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
    public void rejectTicketSuggestionSuccess() {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestedTicketIds);
        adapter.notifyDataSetChanged();
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
    public void acceptParticularTicketSuggestionSuccess(String suggestionId) {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void rejectParticularTicketSuggestionSuccess(String suggestionId) {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void rejectParticularTicketSuggestionFail(String suggestionId) {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void acceptParticularTicketSuggestionFail(String suggestionId) {
        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(suggestionId);
        adapter.notifyDataSetChanged();
    }
}