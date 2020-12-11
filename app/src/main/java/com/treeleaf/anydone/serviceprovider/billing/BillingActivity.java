package com.treeleaf.anydone.serviceprovider.billing;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.invoice.InvoiceActivity;
import com.treeleaf.anydone.serviceprovider.paymentmethod.PaymentMethodActivity;
import com.treeleaf.anydone.serviceprovider.plans.PaymentPlans;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillingActivity extends AppCompatActivity {

    @BindView(R.id.rl_payment_method)
    RelativeLayout rlPaymentMethod;
    @BindView(R.id.rl_plans)
    RelativeLayout rlPlans;
    @BindView(R.id.rl_invoice)
    RelativeLayout rlInvoice;

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
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @OnClick(R.id.rl_payment_method)
    public void onClickPaymentMethod() {
        startActivity(new Intent(BillingActivity.this, PaymentMethodActivity.class));
    }


    @OnClick(R.id.rl_plans)
    public void onClickTransactions() {
        startActivity(new Intent(BillingActivity.this, PaymentPlans.class));
    }


    @OnClick(R.id.rl_invoice)
    public void onClickInvoice() {
        startActivity(new Intent(BillingActivity.this, InvoiceActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}