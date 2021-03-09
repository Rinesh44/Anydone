package com.treeleaf.anydone.serviceprovider.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.inboxdetails.InboxDetailActivity;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.login.LoginActivity;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::launchActivity, 1000);
    }

    protected void launchActivity() {
        if (!Hawk.get(Constants.LOGGED_IN, false)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            if (extras != null) {
                GlobalUtils.showLog(TAG, "notification extras: " + extras);
                String inboxId = extras.getString("inboxId");
                String notificationType = extras.getString("notificationType");

                GlobalUtils.showLog(TAG, "inboxId check on splash: " + inboxId);
                if (inboxId != null && !inboxId.isEmpty()) {
                    Intent inboxIntent = new Intent(this, InboxDetailActivity.class);
                    inboxIntent.putExtra("inbox_id", inboxId);
                    inboxIntent.putExtra("notification", true);
                    startActivity(inboxIntent);
                }
            }
            startActivity(new Intent(SplashActivity.this, LandingActivity.class));
        }
        finish();
    }

}
