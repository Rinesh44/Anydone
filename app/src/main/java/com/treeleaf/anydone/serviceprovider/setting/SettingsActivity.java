package com.treeleaf.anydone.serviceprovider.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.RelativeLayout;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.changepassword.ChangePasswordActivity;
import com.treeleaf.anydone.serviceprovider.setting.currency.CurrencyActivity;
import com.treeleaf.anydone.serviceprovider.setting.language.LanguagesActivity;
import com.treeleaf.anydone.serviceprovider.setting.location.showLocation.ShowLocationActivity;
import com.treeleaf.anydone.serviceprovider.setting.timezone.TimezoneActivity;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends MvpBaseActivity<SettingsPresenterImpl>
        implements SettingsContract.SettingsView {
    @BindView(R.id.rl_change_password)
    RelativeLayout rlChangePassword;
    /*    @BindView(R.id.rl_notification)
        RelativeLayout rlNotification;*/
    @BindView(R.id.rl_language)
    RelativeLayout rlLanguage;
    /*    @BindView(R.id.rl_video_settings)
        RelativeLayout rlVideoSettings;*/
    @BindView(R.id.rl_currency)
    RelativeLayout rlCurrency;
    @BindView(R.id.rl_location)
    RelativeLayout rlLocation;
    @BindView(R.id.rl_timezone)
    RelativeLayout rlTimezone;

    private long mLastClickTime = 0;

    @Override
    protected int getLayout() {
        return R.layout.activity_settings;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.setting));
        setToolbar();

    }

    @OnClick(R.id.rl_change_password)
    void changePassword() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
    }


 /*   @OnClick(R.id.rl_notification)
    void notificationSettings() {
    }*/


    @OnClick(R.id.rl_language)
    void languageSettings() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(SettingsActivity.this, LanguagesActivity.class));
    }

/*    @OnClick(R.id.rl_video_settings)
    void videoSettings() {
        startActivity(new Intent(SettingsActivity.this, VideoSettingActivity.class));
    }*/

    @OnClick(R.id.rl_currency)
    void currencySettings() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(SettingsActivity.this, CurrencyActivity.class));
    }

    @OnClick(R.id.rl_location)
    void locationSettings() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(SettingsActivity.this, ShowLocationActivity.class));
    }

    @OnClick(R.id.rl_timezone)
    void timezoneSettings() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        startActivity(new Intent(SettingsActivity.this, TimezoneActivity.class));
    }


    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Settings");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @Override
    protected void injectDagger() {
        getActivityComponent().inject(SettingsActivity.this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }


    @Override
    public void showProgressBar(String message) {

    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showToast(this, message);
    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void onFailure(String message) {

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
}
