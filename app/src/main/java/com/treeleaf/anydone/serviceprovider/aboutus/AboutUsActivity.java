package com.treeleaf.anydone.serviceprovider.aboutus;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.treeleaf.anydone.serviceprovider.OpenSourceLibraryActivity;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.weblink.WebLinkActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends AppCompatActivity {
    public static final String SITE = "https://treeleaf.ai/";
    public static final String PRIVACY_POLICY = "https://anydone.com/company/v1/privacy-policy.html";
    public static final String TERMS_AND_CONDITIONS = "https://anydone.com/company/v1/terms-of-service.html";
    public static final String FB_LINK = "https://www.facebook.com/treeleafai";
    @BindView(R.id.tv_privacy)
    TextView tvPrivacy;
    @BindView(R.id.tv_open_src_library)
    TextView tvOpenSource;
    @BindView(R.id.tv_terms_n_conditions)
    TextView tvTermsAndConditions;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.tv_version_number)
    TextView tvVersionNumber;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ButterKnife.bind(this);
        setToolbar();

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionNumber.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvPrivacy.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            i.putExtra("link", PRIVACY_POLICY);
            startActivity(i);
        });

        tvOpenSource.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent i = new Intent(AboutUsActivity.this, OpenSourceLibraryActivity.class);
            startActivity(i);
        });

        tvTermsAndConditions.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            i.putExtra("link", TERMS_AND_CONDITIONS);
            startActivity(i);
        });

        ivFacebook.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                return;
            }
            mLastClickTime = SystemClock.elapsedRealtime();
            Intent i = new Intent(AboutUsActivity.this, WebLinkActivity.class);
            i.putExtra("link", FB_LINK);
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