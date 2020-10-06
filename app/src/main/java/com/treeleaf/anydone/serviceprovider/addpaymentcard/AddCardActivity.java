package com.treeleaf.anydone.serviceprovider.addpaymentcard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.ImageButton;

import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

public class AddCardActivity extends MvpBaseActivity<AddCardPresenterImpl> implements
        AddCardContract.AddCardView {
    @BindView(R.id.il_card_number)
    TextInputLayout ilCardNumber;
    @BindView(R.id.et_card_number)
    TextInputEditText etCardNumber;
    @BindView(R.id.il_name)
    TextInputLayout ilName;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.il_month)
    TextInputLayout ilMonth;
    @BindView(R.id.et_month)
    TextInputEditText etMonth;
    @BindView(R.id.il_year)
    TextInputLayout ilYear;
    @BindView(R.id.et_year)
    TextInputEditText etYear;
    @BindView(R.id.il_cvv)
    TextInputLayout ilCvv;
    @BindView(R.id.et_cvv)
    TextInputEditText etCvv;
    @BindView(R.id.il_street_address)
    TextInputLayout ilStreetAddress;
    @BindView(R.id.et_street_address)
    TextInputEditText etStreetAddress;
    @BindView(R.id.il_city)
    TextInputLayout ilCity;
    @BindView(R.id.et_city)
    TextInputEditText etCity;
    @BindView(R.id.il_state)
    TextInputLayout ilState;
    @BindView(R.id.et_state)
    TextInputEditText etState;
    @BindView(R.id.btn_save)
    MaterialButton btnSave;
    @BindView(R.id.sw_make_primary)
    SwitchCompat swPrimaryCard;
    @BindView(R.id.ib_cvv_info)
    ImageButton ibCvvInfo;

    private ArrayList<String> cardPatterns = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.activity_add_card;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        createCardPatterns();
        ibCvvInfo.setOnClickListener(v -> showCVVInfoDialog());

        etCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                formatCardNumbers(s);
            }
        });

        btnSave.setOnClickListener(v -> {
            hideKeyBoard();
            presenter.addCard(UiUtils.getString(etName),
                    UiUtils.getString(etCardNumber),
                    UiUtils.getString(etMonth),
                    UiUtils.getString(etYear),
                    UiUtils.getString(etCvv),
                    UiUtils.getString(etStreetAddress),
                    UiUtils.getString(etCity),
                    UiUtils.getString(etState),
                    swPrimaryCard.isChecked());
        });

    }

    private void formatCardNumbers(Editable s) {
        final char space = ' ';
        // Remove spacing char
        if (s.length() > 0 && (s.length() % 5) == 0) {
            final char c = s.charAt(s.length() - 1);
            if (space == c) {
                s.delete(s.length() - 1, s.length());
            }
        }
        // Insert char where needed.
        if (s.length() > 0 && (s.length() % 5) == 0) {
            char c = s.charAt(s.length() - 1);
            // Only if its a digit where there should be a space we insert a space
            if (Character.isDigit(c) && TextUtils.split(s.toString(),
                    String.valueOf(space)).length <= 3) {
                s.insert(s.length() - 1, String.valueOf(space));
            }
        }

        detectCardType(s);
    }

    private void detectCardType(Editable s) {
        String ccNum = s.toString().replace(" ", "");
        for (String p : cardPatterns) {
            if (ccNum.matches(p)) {
                addCardDrawable(p);
                break;
            } else {
                etCardNumber.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, 0, 0);
            }
        }
    }

    private void addCardDrawable(String p) {
        switch (p) {
            case Constants.VISA:
                etCardNumber.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_visacard_icon, 0);
                break;

            case Constants.MASTERCARD:
                etCardNumber.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_mastercard_icon, 0);
                break;

            default:
                break;
        }
    }

    private void createCardPatterns() {
        String ptVisa = Constants.VISA;
        cardPatterns.add(ptVisa);
        String ptMasterCard = Constants.MASTERCARD;
        cardPatterns.add(ptMasterCard);
        String ptAmeExp = Constants.AMERICAN_EXPRESS;
        cardPatterns.add(ptAmeExp);
        String ptDinClb = Constants.DINERS_CLUB;
        cardPatterns.add(ptDinClb);
        String ptDiscover = Constants.DISCOVER;
        cardPatterns.add(ptDiscover);
        String ptJcb = Constants.JCB;
        cardPatterns.add(ptJcb);
    }

    private void showCVVInfoDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_cvv_info);
        dialog.show();
    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Add Card");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showInvalidCardHolderNameError() {
        etName.requestFocus();
        ilName.setErrorEnabled(true);
        ilName.setError("Invalid Card Holder Name");

        onInvalidCardHolderName();
    }

    @Override
    public void showInvalidCardNumberError() {
        etCardNumber.requestFocus();
        ilCardNumber.setErrorEnabled(true);
        ilCardNumber.setError("Invalid Card Number");

        onInvalidCardNumber();
    }

    @Override
    public void showInvalidMonthError() {
        etMonth.requestFocus();
        ilMonth.setErrorEnabled(true);
        ilMonth.setError("Invalid Month");

        onInvalidMonth();
    }

    @Override
    public void showInvalidYeaError() {
        etYear.requestFocus();
        ilYear.setErrorEnabled(true);
        ilYear.setError("Invalid Year");

        onInvalidYear();
    }

    @Override
    public void showInvalidCVVError() {
        etCvv.requestFocus();
        ilCvv.setErrorEnabled(true);
        ilCvv.setError("Invalid CVV");

        onInvalidCVV();
    }

    @Override
    public void showInvalidStreetAddressError() {
        etStreetAddress.requestFocus();
        ilStreetAddress.setErrorEnabled(true);
        ilStreetAddress.setError("Invalid Street Address");

        onInvalidStreetAddress();
    }

    @Override
    public void showInvalidCityError() {
        etCity.requestFocus();
        ilCity.setErrorEnabled(true);
        ilCity.setError("Invalid City");

        onInvalidCity();
    }

    @Override
    public void showInvalidStateError() {
        etState.requestFocus();
        ilState.setErrorEnabled(true);
        ilState.setError("Invalid State");

        onInvalidState();
    }


    @Override
    public void onInvalidCardHolderName() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidCardNumber() {
        ilName.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidMonth() {
        ilCardNumber.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidYear() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidCVV() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }


    @Override
    public void onInvalidStreetAddress() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidCity() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilState.setErrorEnabled(false);
    }

    @Override
    public void onInvalidState() {
        ilCardNumber.setErrorEnabled(false);
        ilMonth.setErrorEnabled(false);
        ilYear.setErrorEnabled(false);
        ilCvv.setErrorEnabled(false);
        ilName.setErrorEnabled(false);
        ilStreetAddress.setErrorEnabled(false);
        ilCity.setErrorEnabled(false);
    }

    @Override
    public void onAddCardSuccess() {
        finish();
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
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return AddCardActivity.this;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
