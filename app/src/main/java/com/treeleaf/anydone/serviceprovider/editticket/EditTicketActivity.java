package com.treeleaf.anydone.serviceprovider.editticket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTicketActivity extends MvpBaseActivity<EditTicketPresenterImpl>
        implements EditTicketContract.EditTicketView {
    @BindView(R.id.et_editor)
    EditText etEditor;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_estimated_time_editor)
    LinearLayout llEstimatedTimeEditor;
    @BindView(R.id.tv_1_hr)
    TextView tv1Hr;
    @BindView(R.id.tv_2_hr)
    TextView tv2Hr;
    @BindView(R.id.tv_1_day)
    TextView tv1Day;
    @BindView(R.id.tv_1_week)
    TextView tv1Week;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    private String ticketId;
    private String type;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_ticket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setUpToolbar();
        Intent i = getIntent();
        type = i.getStringExtra("type");
        String text = i.getStringExtra("text");
        ticketId = i.getStringExtra("ticket_id");
        etEditor.setText(text);

        etEditor.requestFocus();
        etEditor.setSelection(etEditor.getText().length());
        UiUtils.showKeyboardForced(this);

        if (type != null) {
            switch (type) {
                case "title":
                    tvToolbarTitle.setText("Edit Ticket Title");
                    llEstimatedTimeEditor.setVisibility(View.GONE);
                    break;

                case "desc":
                    tvToolbarTitle.setText("Edit Description");
                    llEstimatedTimeEditor.setVisibility(View.GONE);
                    break;

                case "estimated_time":
                    tvToolbarTitle.setText("Edit Estimated Time");
                    llEstimatedTimeEditor.setVisibility(View.VISIBLE);
                    break;
            }
        }

        tvDone.setOnClickListener(v -> {
            switch (type) {
                case "title":
                    presenter.editTicketTitle(ticketId, UiUtils.getString(etEditor));
                    break;

                case "desc":
                    presenter.editTicketDesc(ticketId, UiUtils.getString(etEditor));
                    break;

                case "estimated_time":
                    presenter.editTicketEstimatedTime(ticketId, UiUtils.getString(etEditor));
                    break;
            }

            UiUtils.hideKeyboardForced(this);
        });

        tv1Hr.setOnClickListener(v -> {
            etEditor.setText(tv1Hr.getText().toString().trim());
            etEditor.setSelection(etEditor.getText().length());
        });

        tv2Hr.setOnClickListener(v -> {
            etEditor.setText(tv2Hr.getText().toString().trim());
            etEditor.setSelection(etEditor.getText().length());
        });

        tv1Day.setOnClickListener(v -> {
            etEditor.setText(tv1Day.getText().toString().trim());
            etEditor.setSelection(etEditor.getText().length());
        });

        tv1Week.setOnClickListener(v -> {
            etEditor.setText(tv1Week.getText().toString().trim());
            etEditor.setSelection(etEditor.getText().length());
        });
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        UiUtils.hideKeyboardForced(this);
        return true;
    }

    @Override
    public void onTitleEditSuccess() {
        Intent intent = new Intent();
        intent.putExtra("type", "title");
        setResult(2, intent);
        finish();
    }

    @Override
    public void onTitleEditFail(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, message);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
    }

    @Override
    public void onDescEditSuccess() {
        Intent intent = new Intent();
        intent.putExtra("type", "desc");
        setResult(2, intent);
        finish();
    }

    @Override
    public void onDescEditFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void onEstimatedTimeEditSuccess() {
        Intent intent = new Intent();
        intent.putExtra("type", "estimated_time");
        setResult(2, intent);
        finish();
    }

    @Override
    public void onEstimatedTimeEditFail(String message) {
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, message);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
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
        if (message.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, Constants.SERVER_ERROR);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, message, Banner.TOP, 2000).show();
    }

    @Override
    public Context getContext() {
        return this;
    }
}