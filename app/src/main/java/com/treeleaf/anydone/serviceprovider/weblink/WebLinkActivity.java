package com.treeleaf.anydone.serviceprovider.weblink;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.treeleaf.anydone.serviceprovider.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebLinkActivity extends AppCompatActivity {

    @BindView(R.id.wv_anydone)
    WebView mAnydoneWeb;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_link);

        ButterKnife.bind(this);

        String link = getIntent().getStringExtra("link");
        if (link != null && !link.isEmpty()) {
            mAnydoneWeb.setWebViewClient(new WebViewClient());
            mAnydoneWeb.loadUrl(link);

            WebSettings webSettings = mAnydoneWeb.getSettings();
            webSettings.setJavaScriptEnabled(true);
        } else {
            Toast.makeText(this, "Unsupported link", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (mAnydoneWeb.canGoBack()) {
            mAnydoneWeb.goBack();
        } else {
            super.onBackPressed();
        }

    }
}