package com.treeleaf.anydone.serviceprovider.forgotpassword;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;

public class ForgotPasswordActivity extends MvpBaseActivity<ForgotPasswordPresenterImpl> implements
        ForgotPasswordContract.ForgotPasswordView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.il_phone)
    TextInputLayout ilPhone;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.btnSendResetCode)
    MaterialButton btnSendCode;

    private ProgressDialog progress;

    @Override
    protected int getLayout() {
        return R.layout.activity_forgot_password;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ivBack.setOnClickListener(v -> finish());

        etPhone.setText(Hawk.get(Constants.EMAIL_PHONE, ""));
        etPhone.requestFocus();
        btnSendCode.setOnClickListener(v -> {
            hideKeyBoard();
            presenter.sendResetCode(Objects.requireNonNull
                    (etPhone.getText()).toString().toLowerCase());
        });

    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showInvalidEmailPhoneError() {
        ilPhone.setErrorEnabled(true);
        ilPhone.setError("Invalid Email/Phone");
    }

    @Override
    public void onSendResetCodeSuccess(String accountId) {
        Intent i = new Intent(ForgotPasswordActivity.this,
                ResetPasswordActivity.class);
        i.putExtra("email_phone", UiUtils.getString(etPhone));
        i.putExtra("account_id", accountId);
        startActivity(i);
    }

    @Override
    public void onSendResetCodeFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onFailure(String message) {
        showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
