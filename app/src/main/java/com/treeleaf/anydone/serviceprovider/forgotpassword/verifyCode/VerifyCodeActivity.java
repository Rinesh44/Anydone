package com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword.ResetPasswordActivity;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import butterknife.BindView;

public class VerifyCodeActivity extends MvpBaseActivity<VerifyCodePresenterImpl> implements
        VerifyCodeContract.VerifyCodeView {

    @BindView(R.id.tv_resend_code)
    TextView tvResendCode;
    @BindView(R.id.ll_resend_code)
    LinearLayout llResendCode;
    @BindView(R.id.ll_otp_timer)
    LinearLayout llOtpTimer;
    @BindView(R.id.tv_code_expires_in)
    TextView tvCodeExpiresIn;
    @BindView(R.id.tv_user_email_phone)
    TextView tvUserEmailPhone;
    @BindView(R.id.txt_pin_entry)
    PinEntryEditText etPin;

    private ProgressDialog progress;
    private CountDownTimer countDownTimer;
    private int timerInSeconds = 90;
    private String emailPhone;
    private String accountId;

    @Override
    protected int getLayout() {
        return R.layout.activity_verify_code;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        emailPhone = getEmailPhone();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;
        String countryCodeValue = tm.getNetworkCountryIso();

        if (countryCodeValue != null && ValidationUtils.isNumeric
                (emailPhone.replace("+", ""))) {
            String formattedNumber = PhoneNumberUtils.formatNumber(emailPhone, countryCodeValue);
            tvUserEmailPhone.setText(formattedNumber);
        } else {
            tvUserEmailPhone.setText(emailPhone);
        }

        accountId = getAccountId();
        etPin.requestFocus();
        startTimerCountDown();

        etPin.setOnPinEnteredListener(str -> {
            Intent i = new Intent(VerifyCodeActivity.this,
                    ResetPasswordActivity.class);
            i.putExtra("email_phone", emailPhone);
            i.putExtra("account_id", accountId);
            i.putExtra("code", UiUtils.getString(etPin));
            startActivity(i);
        });

        tvResendCode.setOnClickListener(v -> presenter.resendCode(emailPhone));
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
    public void resetPinEditText() {
        etPin.setText("");
    }

    @Override
    public void showResendCode(boolean show) {
        llResendCode.setVisibility(show ? View.VISIBLE : View.GONE);
        llOtpTimer.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResendCodeFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onResendCodeSuccess() {
        resetPinEditText();
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
                tvCodeExpiresIn.setText(String.format("%02d:%02d", minutes, seconds));
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
    protected void onResume() {
        super.onResume();

        resetPinEditText();
        countDownTimer.start();
    }

    @Override
    public void onFailure(String message) {
        showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelCountDownTimer();
    }
}
