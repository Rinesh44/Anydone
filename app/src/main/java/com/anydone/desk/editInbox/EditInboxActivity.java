package com.anydone.desk.editInbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.shasin.notificationbanner.Banner;
import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditInboxActivity extends MvpBaseActivity<EditInboxPresenterImpl> implements
        EditInboxContract.EditInboxView {
    @BindView(R.id.et_editor)
    EditText etEditor;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    private String inboxId;

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_inbox;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        setUpToolbar();
        Intent i = getIntent();
        String text = i.getStringExtra("subject");
        inboxId = i.getStringExtra("inbox_id");
        etEditor.setText(text);

        etEditor.requestFocus();
        etEditor.setSelection(etEditor.getText().length());
        UiUtils.showKeyboardForced(this);

        tvToolbarTitle.setText("Edit Subject");

        tvDone.setOnClickListener(v -> {
            presenter.editInboxSubject(inboxId, UiUtils.getString(etEditor));
            UiUtils.hideKeyboardForced(this);
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

    @Override
    public void onSubjectEditSuccess() {
        Intent intent = new Intent();
        setResult(2, intent);
        finish();
    }

    @Override
    public void onSubjectEditFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }
}

