package com.anydone.desk.forgotpassword.verifyCode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.forgotpassword.resetpassword.ResetPasswordActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.UiUtils;
import com.anydone.desk.utils.ValidationUtils;

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
    @BindView(R.id.pb_progress)
    ProgressBar progress;

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
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
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
        showToastMessage(Constants.SERVER_ERROR);
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
