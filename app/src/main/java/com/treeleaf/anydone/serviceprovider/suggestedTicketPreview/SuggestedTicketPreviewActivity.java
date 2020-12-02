package com.treeleaf.anydone.serviceprovider.suggestedTicketPreview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TicketHistoryAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.Message;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketSuggestionRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedTicketPreviewActivity extends MvpBaseActivity<SuggestedTicketPreviewPresenterImpl>
        implements SuggestedTicketPreviewContract.SuggestedTicketPreviewView {

    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.civ_customer)
    CircleImageView civCustomer;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.rv_ticket_history)
    RecyclerView rvTicketHistory;
    @BindView(R.id.tv_accept)
    TextView tvAccept;
    @BindView(R.id.tv_reject)
    TextView tvReject;

    private String ticketSuggestionId;
    private String msgId;
    private String customerName;
    private String customerImageUrl;
    private String messageText;
    private long messageSentAt;
    private TicketHistoryAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_suggested_ticket_preview;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ticketSuggestionId = i.getStringExtra("suggestion_id");
        msgId = i.getStringExtra("msg_id");
        customerImageUrl = i.getStringExtra("customer_image");
        customerName = i.getStringExtra("customer_name");
        messageText = i.getStringExtra("msg_text");
        messageSentAt = i.getLongExtra("sent_at", 0);

        presenter.getTicketHistory(ticketSuggestionId);
        tvToolbarTitle.setText(customerName);

        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(customerImageUrl)
                .apply(options)
                .into(civCustomer);

        tvAccept.setOnClickListener(v -> showAcceptDialog(ticketSuggestionId));
        tvReject.setOnClickListener(v -> showRejectDialog(ticketSuggestionId));
        ivBack.setOnClickListener(v -> onBackPressed());

    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void acceptTicketSuggestionSuccess(String ticketSuggestionId) {
        Hawk.put(Constants.SUGGESTION_ACCEPTED, true);

        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(ticketSuggestionId);
        finish();
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
    public void rejectTicketSuggestionSuccess(String ticketSuggestionId) {
        Hawk.put(Constants.SUGGESTION_REJECTED, true);

        TicketSuggestionRepo.getInstance().deleteTicketSuggestionById(ticketSuggestionId);
        finish();
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
    public void getTicketHistorySuccess(TicketProto.TicketSuggestion ticketSuggestion) {
        List<Message> messageList = transformMessages(ticketSuggestion.getMsgsList());
        //add clicked message to show on details

        Message selectedMsg = new Message();
        selectedMsg.setTimestamp(messageSentAt);
        selectedMsg.setMessageId(msgId);
        selectedMsg.setMessageText(messageText);
        messageList.add(selectedMsg);

        Collections.sort(messageList, (Comparator) (o1, o2) -> {
            Message p1 = (Message) o1;
            Message p2 = (Message) o2;
            return Long.compare(p1.getTimestamp(), p2.getTimestamp());
        });

        setUpRecyclerView(messageList);
    }

    private List<Message> transformMessages(List<TicketProto.TicketSuggestion.Message> msgListPb) {
        List<Message> messageList = new ArrayList<>();
        for (TicketProto.TicketSuggestion.Message message : msgListPb
        ) {
            Message message1 = new Message();
            message1.setMessageId(message.getMsgId());
            message1.setMessageText(message.getText());
            message1.setTimestamp(message.getTimestamp());
            messageList.add(message1);
        }

        return messageList;
    }

    @Override
    public void getTicketHistoryFail(String msg) {

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpRecyclerView(List<Message> messageList) {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvTicketHistory.setLayoutManager(mLayoutManager);

        adapter = new TicketHistoryAdapter(messageList, msgId, this);
        rvTicketHistory.setAdapter(adapter);
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

}