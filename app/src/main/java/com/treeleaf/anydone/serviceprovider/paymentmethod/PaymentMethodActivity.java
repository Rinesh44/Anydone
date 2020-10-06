package com.treeleaf.anydone.serviceprovider.paymentmethod;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.addpaymentcard.AddCardActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentMethodActivity extends AppCompatActivity {
    @BindView(R.id.rv_payment_methods)
    RecyclerView rvPaymentMethod;
    @BindView(R.id.tv_add_credit_debit_card)
    TextView tvAddCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        setToolbar();
        ButterKnife.bind(this);

    }

    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SpannableStringBuilder str = new SpannableStringBuilder("Payment Methods");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @OnClick(R.id.tv_add_credit_debit_card)
    public void onAddCardClick() {
        startActivity(new Intent(PaymentMethodActivity.this, AddCardActivity.class));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}