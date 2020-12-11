package com.treeleaf.anydone.serviceprovider.billing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.treeleaf.anydone.serviceprovider.paymentmethod.PaymentMethodActivity;
import com.treeleaf.anydone.serviceprovider.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillingActivity extends AppCompatActivity {

    @BindView(R.id.rl_payment_method)
    RelativeLayout rlPaymentMethod;
    @BindView(R.id.rl_transactions)
    RelativeLayout rlTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        setUpToolbar();
        ButterKnife.bind(this);
    }

    private void setUpToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableStringBuilder str = new SpannableStringBuilder("Billing");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @OnClick(R.id.rl_payment_method)
    public void onClickPaymentMethod() {
        startActivity(new Intent(BillingActivity.this, PaymentMethodActivity.class));
    }


    @OnClick(R.id.rl_transactions)
    public void onClickTransactions() {
        Toast.makeText(this, "go to transaction activity", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}