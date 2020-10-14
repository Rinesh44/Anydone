package com.treeleaf.anydone.serviceprovider.setting.currency;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mynameismidori.currencypicker.ExtendedCurrency;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.CurrencyAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;

public class SelectCurrencyActivity extends MvpBaseActivity<CurrencyPresenterImpl> implements
        CurrencyContract.CurrencyView {
    private static final String TAG = "SelectCurrencyActivity";
    @BindView(R.id.et_search_currency)
    EditText etSearchCurrency;
    @BindView(R.id.rv_currency)
    RecyclerView rvCurrency;
    @BindView(R.id.pb_progress)
    ProgressBar progressDialog;

    private String selectedCurrencyName;
    private String selectedCurrencyCode;
    private int selectedCurrencyFlag;

    private CurrencyAdapter adapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_select_currency;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        List<ExtendedCurrency> currencyResultsModified = getModifiedList();
        setUpRecyclerView(currencyResultsModified);
        UiUtils.showKeyboard(this, etSearchCurrency);

        etSearchCurrency.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private List<ExtendedCurrency> getModifiedList() {
        List<ExtendedCurrency> currencyResults = ExtendedCurrency.getAllCurrencies();
        List<ExtendedCurrency> currencyResultsModified = new ArrayList<>(currencyResults);
        for (ExtendedCurrency currency : currencyResults
        ) {
            if (currency.getCode().toLowerCase().equalsIgnoreCase("npr")) {
                int index = currencyResults.indexOf(currency);
                ExtendedCurrency extendedCurrency = new ExtendedCurrency();
                extendedCurrency.setCode(currency.getCode());
                extendedCurrency.setName(currency.getName());
                extendedCurrency.setSymbol(currency.getSymbol());
                extendedCurrency.setFlag(R.drawable.flag_nepal);
                currencyResultsModified.set(index, extendedCurrency);
            }
        }

        return currencyResultsModified;
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Select Currency");
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
    public void onAddCurrencySuccess() {
        Intent intent = new Intent();
        intent.putExtra("currency", selectedCurrencyCode);
        intent.putExtra("currency_name", selectedCurrencyName);
        GlobalUtils.showLog(TAG, "flag id: " + selectedCurrencyFlag);
        if (selectedCurrencyFlag != 0) {
            intent.putExtra("currency_flag", selectedCurrencyFlag);
        } else {
            intent.putExtra("currency_flag", -1);
        }
        setResult(2, intent);
        finish();
    }

    @Override
    public void onAddCurrencyFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
    }

    @Override
    public void showProgressBar(String message) {
        progressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.setVisibility(View.GONE);
        }
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

    private void setUpRecyclerView(List<ExtendedCurrency> currencyResults) {
        rvCurrency.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CurrencyAdapter(currencyResults, this);
        adapter.setOnItemClickListener(currencyResult -> {
            selectedCurrencyName = currencyResult.getName();
            selectedCurrencyCode = currencyResult.getCode();
            selectedCurrencyFlag = currencyResult.getFlag();
            String token = Hawk.get(Constants.TOKEN);
            try {
                presenter.addCurrency(token, selectedCurrencyCode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        rvCurrency.setAdapter(adapter);
    }
}