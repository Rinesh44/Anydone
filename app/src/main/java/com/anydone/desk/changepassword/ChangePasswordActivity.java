package com.anydone.desk.changepassword;

import android.content.Context;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shasin.notificationbanner.Banner;
import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.UiUtils;

import butterknife.BindView;

public class ChangePasswordActivity extends MvpBaseActivity<ChangePasswordPresenterImpl> implements
        ChangePasswordContract.ChangePasswordView {
    @BindView(R.id.il_old_password)
    TextInputLayout ilOldPassword;
    @BindView(R.id.et_old_password)
    TextInputEditText etOldPassword;
    @BindView(R.id.il_new_password)
    TextInputLayout ilNewPassword;
    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;
    @BindView(R.id.il_confirm_password)
    TextInputLayout ilConfirmPassword;
    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;
    @BindView(R.id.btn_save)
    MaterialButton btnSave;
    @BindView(R.id.pb_progress)
    ProgressBar progress;


    @Override
    protected int getLayout() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.change_password));
        setToolbar();

        btnSave.setOnClickListener(v -> {
            hideKeyBoard();
            presenter.changePassword(UiUtils.getString(etOldPassword),
                    UiUtils.getString(etNewPassword),
                    UiUtils.getString(etConfirmPassword));
        });
    }

    private void setToolbar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Change Password");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent()
                .inject(ChangePasswordActivity.this);
    }

    @Override
    public void showInvalidOldPasswordError() {
        etOldPassword.requestFocus();
        ilOldPassword.setErrorEnabled(true);
        ilOldPassword.setError("Invalid Old Password");
        ilOldPassword.setErrorIconDrawable(null);
        onInvalidOldPassword();
    }


    @Override
    public void showInvalidNewPasswordError() {
        etNewPassword.requestFocus();
        ilNewPassword.setErrorEnabled(true);
        ilNewPassword.setError("Invalid New Password");
        ilNewPassword.setErrorIconDrawable(null);
        onInvalidNewPassword();
    }

    @Override
    public void showInvalidConfirmPasswordError() {
        etConfirmPassword.requestFocus();
        ilConfirmPassword.setErrorEnabled(true);
        ilConfirmPassword.setError("Invalid Confirm Password");
        ilConfirmPassword.setErrorIconDrawable(null);
        onInvalidConfirmPassword();
    }


    @Override
    public void showPasswordNotMatchError() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Confirm password did not match");
    }

    @Override
    public void onInvalidOldPassword() {
        ilNewPassword.setErrorEnabled(false);
        ilConfirmPassword.setErrorEnabled(false);
    }

    @Override
    public void showSamePasswordError() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Old and New " + "Password cannot be same");
    }

    @Override
    public void onInvalidNewPassword() {
        ilConfirmPassword.setErrorEnabled(false);
    }

    @Override
    public void onInvalidConfirmPassword() {
        ilNewPassword.setErrorEnabled(false);
    }

    @Override
    public void onChangePasswordSuccess() {
        ilNewPassword.setErrorEnabled(false);
        ilConfirmPassword.setErrorEnabled(false);
        etOldPassword.setText("");
        etNewPassword.setText("");
        etConfirmPassword.setText("");
    /*    Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.SUCCESS, "Password changed!",
                Banner.BOTTOM, 2000).show();*/

        Toast.makeText(this, "Password changed!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onChangePasswordFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.BOTTOM, 2000).show();
    }


    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showToast(this, message);
    }

    @Override
    public void hideProgressBar() {
        progress.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return ChangePasswordActivity.this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
