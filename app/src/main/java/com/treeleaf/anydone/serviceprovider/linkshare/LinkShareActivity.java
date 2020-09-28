package com.treeleaf.anydone.serviceprovider.linkshare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Tickets;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import java.util.Objects;

import butterknife.BindView;

public class LinkShareActivity extends MvpBaseActivity<LinkSharePresenterImpl> implements
        LinkShareContract.LinkShareView {
    private static final String TAG = "LinkShareActivity";

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.et_email_phone)
    EditText etEmailPhone;
    @BindView(R.id.btn_send)
    MaterialButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        long ticketId = i.getLongExtra("ticket_id", -1);
        boolean isEmail = i.getBooleanExtra("is_email", false);

        setUpEmailPhone(isEmail);
        setCustomerData(ticketId, isEmail);

        btnSend.setOnClickListener(v -> {

            if (ValidationUtils.isEmpty(etEmailPhone.getText().toString())) {
                Toast.makeText(this, "Input field cannot be empty", Toast.LENGTH_SHORT)
                        .show();
            } else {
                presenter.getShareLink(String.valueOf(ticketId), etEmailPhone.getText()
                        .toString().trim());
            }

        });
    }

    private void setUpEmailPhone(boolean isEmail) {
        if (isEmail) {
            setToolbar("Email");
            etEmailPhone.setHint("Enter Email");
            etEmailPhone.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            setToolbar("SMS");
            etEmailPhone.setHint("Enter Number");
            etEmailPhone.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED );
        }
    }

    private void setToolbar(String title) {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder(title);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    private void setCustomerData(long ticketId, boolean isEmail) {
        if (ticketId != -1) {
            Tickets tickets = TicketRepo.getInstance().getTicketById(ticketId);

            if (isEmail) {
                String customerEmail = tickets.getCustomer().getEmail();
                if (!customerEmail.isEmpty()) {
                    etEmailPhone.setText(customerEmail);
                } else {
                    etEmailPhone.requestFocus();
                    UiUtils.showKeyboard(this, etEmailPhone);
                }
            } else {
                String customerPhone = tickets.getCustomer().getPhone();
                if (!customerPhone.isEmpty()) {
                    etEmailPhone.setText(customerPhone);
                } else {
                    etEmailPhone.requestFocus();
                    UiUtils.showKeyboard(this, etEmailPhone);
                }
            }
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_link_share;
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void onLinkShareSuccess(String link) {
        Toast.makeText(this, "Link shared", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onLinkShareFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
            return;
        }
        Banner.make(getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void showProgressBar(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
