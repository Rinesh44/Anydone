package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import butterknife.BindView;

public class ResetPasswordActivity extends MvpBaseActivity<ResetPasswordPresenterImpl>
        implements ResetPasswordContract.ResetPasswordView {
    private static final String TAG = "ResetPasswordActivity";
    @BindView(R.id.il_new_password)
    TextInputLayout ilNewPassword;
    @BindView(R.id.et_new_password)
    TextInputEditText etNewPassword;
    @BindView(R.id.il_confirm_password)
    TextInputLayout ilConfirmPassword;
    @BindView(R.id.et_confirm_password)
    TextInputEditText etConfirmPassword;
    @BindView(R.id.btn_reset)
    MaterialButton btnReset;
    @BindView(R.id.il_code)
    TextInputLayout ilCode;
    @BindView(R.id.et_code)
    TextInputEditText etCode;
    @BindView(R.id.ll_resend_code)
    LinearLayout llResendCode;
    @BindView(R.id.ll_otp_timer)
    LinearLayout llOtpTimer;
    @BindView(R.id.tv_code_expires_in)
    TextView tvCodeExpiresIn;
    @BindView(R.id.tv_reset_password)
    TextView tvResetPassword;
    @BindView(R.id.il_old_password)
    TextInputLayout ilOldPassword;
    @BindView(R.id.et_old_password)
    TextInputEditText etOldPassword;


    private ProgressDialog progress;
    private CountDownTimer countDownTimer;
    private int timerInSeconds = 90;
    private boolean employeeFirstLogin;

    @Override
    protected int getLayout() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String accountId = getAccountId();
        String emailPhone = getEmailPhone();

        Intent i = getIntent();
        employeeFirstLogin = i.getBooleanExtra("employee_first_login", false);
        String oldPassword = i.getStringExtra("old_password");
        if (employeeFirstLogin) {
            tvResetPassword.setText(R.string.employee_first_login_text);
            etOldPassword.setText(oldPassword);
            etOldPassword.setFocusable(false);
            etOldPassword.setCursorVisible(false);
            etOldPassword.setClickable(false);
            ilCode.setVisibility(View.GONE);
            ilOldPassword.setVisibility(View.VISIBLE);
            llOtpTimer.setVisibility(View.GONE);
            llResendCode.setVisibility(View.GONE);
        }

        GlobalUtils.showLog(TAG, "accountId: " + accountId);

        startTimerCountDown();

        btnReset.setOnClickListener(v -> {
            hideKeyBoard();
            if (employeeFirstLogin) {
                presenter.changePassword(UiUtils.getString(etOldPassword),
                        UiUtils.getString(etNewPassword),
                        UiUtils.getString(etConfirmPassword));
            } else {
                presenter.resetPassword(emailPhone, UiUtils.getString(etNewPassword),
                        UiUtils.getString(etConfirmPassword), accountId, UiUtils.getString(etCode));
            }
        });

        llResendCode.setOnClickListener(v -> {
            hideKeyBoard();
            presenter.resendCode(emailPhone);
        });
    }

    @Override
    public void showPasswordNotMatchError() {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Confirm password did not match");
    }

    private String getEmailPhone() {
        Intent i = getIntent();
        return i.getStringExtra("email_phone");
    }

    private String getAccountId() {
        Intent i = getIntent();
        return i.getStringExtra("account_id");
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showInvalidNewPasswordError() {
        etNewPassword.requestFocus();
        ilNewPassword.setErrorEnabled(true);
        ilNewPassword.setError("Invalid New Password");

        onInvalidNewPassword();
    }

    @Override
    public void showInvalidConfirmPasswordError() {
        etConfirmPassword.requestFocus();
        ilConfirmPassword.setErrorEnabled(true);
        ilConfirmPassword.setError("Invalid Confirm Password");

        onInvalidConfirmPassword();
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
    public void onInvalidOldPassword() {
        ilNewPassword.setErrorEnabled(false);
        ilConfirmPassword.setErrorEnabled(false);
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
    public void onResetPasswordSuccess() {
        Toast.makeText(this, "Password reset success", Toast.LENGTH_LONG).show();
        Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    @Override
    public void onResetPasswordFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showResendCode(boolean show) {
        if (llResendCode != null && llOtpTimer != null) {
            llResendCode.setVisibility(show ? View.VISIBLE : View.GONE);
            llOtpTimer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelCountDownTimer();
    }

    @Override
    public void onResendCodeFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onResendCodeSuccess() {
        etCode.setText("");
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Code sent");
    }

    @Override
    public void startTimerCountDown() {
        countDownTimer = new CountDownTimer(timerInSeconds * 1000 + 1000,
                1000) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                if (tvCodeExpiresIn != null) {
                    tvCodeExpiresIn.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }

            @Override
            public void onFinish() {
                showResendCode(true);
            }
        };
        countDownTimer.start();
    }

    @Override
    public void cancelCountDownTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    public void onChangePasswordSuccess() {
        Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
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
    public void showSamePasswordError() {
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, "Old and New password cannot be same",
                Banner.BOTTOM, 2000).show();
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
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);

    }

    @Override
    public Context getContext() {
        return this;
    }
}
