package com.treeleaf.anydone.serviceprovider.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.weblink.WebLinkActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;
    @BindView(R.id.tv_open_src_library)
    TextView tvOpenSource;
    @BindView(R.id.tv_terms_n_conditions)
    TextView tvTermsAndConditions;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ButterKnife.bind(this);
        setToolbar();

        tvPrivacy.setOnClickListener(v -> {
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            startActivity(i);
        });

        tvOpenSource.setOnClickListener(v -> {
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            startActivity(i);
        });

        tvTermsAndConditions.setOnClickListener(v -> {
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            startActivity(i);
        });

        ivFacebook.setOnClickListener(v -> {
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            startActivity(i);
        });
    }


    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("About");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}