package com.anydone.desk.setting.currency;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.orhanobut.hawk.Hawk;
import com.anydone.desk.R;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CurrencyActivity extends AppCompatActivity {
    private static final String TAG = "CurrencyActivity";
    public static final int CURRENCY_RESULT = 8979;
    @BindView(R.id.rl_currency)
    RelativeLayout rlCurrency;
    @BindView(R.id.tv_currency)
    TextView tvCurrency;
    @BindView(R.id.tv_currency_words)
    TextView tvCurrencyWords;
    @BindView(R.id.iv_flag)
    ImageView ivFlag;

    private long mLastClickTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        ButterKnife.bind(this);
        setToolbar();

        String selectedCurrency = Hawk.get(Constants.CURRENCY);
        GlobalUtils.showLog(TAG, "selected currency: " + selectedCurrency);
        if (selectedCurrency == null) {
            setDefaultCurrencyValues();
        } else {
            ExtendedCurrency currency = ExtendedCurrency.getCurrencyByISO(selectedCurrency);

            if (currency == null) {
                currency = ExtendedCurrency.getCurrencyByISO("NPR");
            }

            if (selectedCurrency.isEmpty()) {
                Hawk.put(Constants.CURRENCY, "NPR");
                tvCurrency.setText("NPR");
            } else {
                tvCurrency.setText(selectedCurrency);
            }

            tvCurrencyWords.setText(currency.getName());

            if (currency.getFlag() != -1) {
                if (currency.getCode().equalsIgnoreCase("npr")) {
                    Drawable drawable = getResources().getDrawable(R.drawable.flag_nepal);
                    ivFlag.setImageDrawable(drawable);
                } else {
                    Drawable drawable = getResources().getDrawable(currency.getFlag());
                    ivFlag.setImageDrawable(drawable);
                }
            } else {
                ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.white_bg));
            }
        }

        rlCurrency.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            startActivityForResult(new Intent
                            (CurrencyActivity.this, SelectCurrencyActivity.class),
                    CURRENCY_RESULT);
        });
    }

    private void setDefaultCurrencyValues() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        assert tm != null;

        Locale currentLocale = getResources().getConfiguration().locale;
        Currency defaultCurrency = Currency.getInstance(Locale.US);
        ExtendedCurrency currency = ExtendedCurrency.
                getCurrencyByISO(defaultCurrency.getCurrencyCode());

        tvCurrency.setText(currency.getCode());
        tvCurrencyWords.setText(currency.getName());

        Drawable drawable = getResources().getDrawable(currency.getFlag());
        ivFlag.setImageDrawable(drawable);
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Currency");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CURRENCY_RESULT && resultCode == 2) {
            if (data != null) {
                String currency = data.getStringExtra("currency");
                String currencyName = data.getStringExtra("currency_name");
                int currencyFlag = data.getIntExtra("currency_flag", -1);

                Hawk.put(Constants.CURRENCY, currency);

                tvCurrency.setText(currency);
                tvCurrencyWords.setText(currencyName);

                if (currencyFlag != -1) {
                    Drawable drawable = getResources().getDrawable(currencyFlag);
                    ivFlag.setImageDrawable(drawable);
                } else {
                    ivFlag.setImageDrawable(getResources().getDrawable(R.drawable.white_bg));
                }
            }
        }
    }
}