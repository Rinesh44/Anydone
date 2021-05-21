package com.anydone.desk.verification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.R;
import com.anydone.desk.base.activity.MvpBaseActivity;
import com.anydone.desk.landing.LandingActivity;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.UiUtils;
import com.anydone.desk.utils.ValidationUtils;

import butterknife.BindView;
import butterknife.OnClick;


public class VerificationActivity extends MvpBaseActivity<VerificationPresenterImpl>
        implements VerificationContract.VerificationView {
    private static final String TAG = "VerificationActivity";
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

    private boolean editProfile = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_verification;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startTimerCountDown();
        String emailPhone = Hawk.get(Constants.EMAIL_PHONE);
        String countryCode = Hawk.get(Constants.COUNTRY_CODE);

        editProfile = checkIfProfileEdit();

        if (emailPhone == null || countryCode == null) {
            return;
        }

        if (ValidationUtils.isNumeric(emailPhone.replace("+", ""))) {
            String formattedNumber = PhoneNumberUtils.formatNumber(emailPhone, countryCode);
            tvUserEmailPhone.setText(formattedNumber);
        } else {
            tvUserEmailPhone.setText(emailPhone);
        }

        boolean isPhoneVerification = checkIfPhoneVerification();
        boolean isEmailVerification = checkifEmailVerification();

        if (editProfile && isPhoneVerification) {
            String phone = getIntent().getStringExtra("phone");
            if (phone != null && !phone.isEmpty()) {
                tvUserEmailPhone.setText(phone);
            }
        }

        if (editProfile && isEmailVerification) {
            String email = getIntent().getStringExtra("email");
            if (email != null && !email.isEmpty()) {
                tvUserEmailPhone.setText(email);
            }
        }

        etPin.requestFocus();

        etPin.setOnPinEnteredListener(str -> {
            hideKeyBoard();
            GlobalUtils.showLog(TAG, "pin: " + UiUtils.getString(etPin));
            presenter.verify(UiUtils.getString(etPin));
        });

        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etPin.setPinBackground(getResources().getDrawable(R.drawable.pinentry_bg));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkIfProfileEdit() {
        Intent i = getIntent();
        return i.getBooleanExtra("edit_profile", false);
    }

    private boolean checkIfPhoneVerification() {
        Intent i = getIntent();
        return i.getBooleanExtra("phone_verification", false);
    }

    private boolean checkifEmailVerification() {
        Intent i = getIntent();
        return i.getBooleanExtra("email_verification", false);
    }


    private String getPasswordFromIntent() {
        Intent i = getIntent();
        return i.getStringExtra("password");
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
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }


    @Override
    public void showEmptyFieldError(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
        etPin.setPinBackground(getResources().getDrawable(R.drawable.pinentry_bg_error));
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
    public void onFailure(String message) {
        etPin.setText("");
        showToastMessage(Constants.SERVER_ERROR);
    }

    @Override
    public void onVerificationSuccess() {
        if (!editProfile) {
            String password = getPasswordFromIntent();
            String emailPhone = getEmailPhoneFromIntent();
            presenter.login(emailPhone, password);
        } else {
            Intent intent = new Intent();
            intent.putExtra("success", true);
            setResult(2, intent);
            finish();
        }
    }

    private String getEmailPhoneFromIntent() {
        Intent i = getIntent();
        return i.getStringExtra("email_phone");
    }

    @Override
    public void onVerificationFail(String msg) {
        etPin.setText("");
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
        etPin.setPinBackground(getResources().getDrawable(R.drawable.pinentry_bg_error));
    }

    @Override
    public void onResendCodeSuccess() {
        etPin.setText("");
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                "Code sent");
    }

    @Override
    public void onResendCodeFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showResendCode(boolean show) {
        if (llOtpTimer != null && llResendCode != null) {
            llResendCode.setVisibility(show ? View.VISIBLE : View.GONE);
            llOtpTimer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_resend_code)
    public void onClickResendCode() {
        presenter.resendCode();
    }

    @Override
    public void resetAllEditTexts() {
        etPin.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void cancelCountDownTimer() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    public void onLoginFail(String msg) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void onLoginSuccess() {
        Intent intent = new Intent(this, LandingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
