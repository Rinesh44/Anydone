package com.treeleaf.anydone.serviceprovider.setting.language;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;

public class LanguagesActivity extends MvpBaseActivity<LanguagePresenterImpl> implements
        LanguageContract.LanguageView {
    private static final String TAG = "LanguagesActivity";
    @BindView(R.id.rg_languages)
    RadioGroup rgLanguages;
    @BindView(R.id.rb_english)
    RadioButton rbEnglish;
    @BindView(R.id.rb_nepali)
    RadioButton rbNepali;
    /*   @BindView(R.id.rb_hebrew)
       RadioButton rbHebrew;*/
    @BindView(R.id.pb_progress)
    ProgressBar progress;

    String selectedLanguage;
    private Context context;
    private Resources resources;
    private boolean languageChange = false;

    @Override
    protected int getLayout() {
        return R.layout.activity_languages;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        selectedLanguage = Hawk.get(Constants.SELECTED_LANGUAGE);
        if (LocaleHelper.getLanguage(LanguagesActivity.this)
                .equalsIgnoreCase("ne")) {
            rbNepali.setChecked(true);
        } else if (LocaleHelper.getLanguage(LanguagesActivity.this)
                .equalsIgnoreCase("en")) {
            rbEnglish.setChecked(true);
        } else {
//            rbHebrew.setChecked(true);
        }

        if (selectedLanguage != null) {
            setLocale(selectedLanguage);
        }

        rgLanguages.setOnCheckedChangeListener((radioGroup, i) -> {
            languageChange = true;
            int englishLangId = rbEnglish.getId();
            int nepaliLangId = rbNepali.getId();
//            int hebrewLangId = rbHebrew.getId();

            if (i == englishLangId) {
                presenter.changeLanguage("en");

            } else if (i == nepaliLangId) {
                presenter.changeLanguage("ne");
            } else {
                //hebrew

            }

        });

    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Language");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (languageChange) {
            Intent i = new Intent(LanguagesActivity.this, LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            finish();
        }
    }

    @Override
    public void onLanguageChangedSuccess(String language) {
        context = LocaleHelper.setLocale(LanguagesActivity.this, "en");
        resources = context.getResources();
        Hawk.put(Constants.SELECTED_LANGUAGE, language);
        selectedLanguage = Hawk.get(Constants.SELECTED_LANGUAGE);
        if (selectedLanguage != null) {
            GlobalUtils.showLog(TAG, "language not null");
            LocaleHelper.setLocale(context, selectedLanguage);
//            setLocale(selectedLanguage);
        }
    }

    @Override
    public void onLanguageChangeFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

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
        return this;
    }
}