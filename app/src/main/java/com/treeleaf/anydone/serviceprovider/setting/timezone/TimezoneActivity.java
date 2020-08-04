package com.treeleaf.anydone.serviceprovider.setting.timezone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TimezoneActivity extends AppCompatActivity {
    private static final String TAG = "TimezoneActivity";
    public static final int TIMEZONE_RESULT = 8990;
    @BindView(R.id.tv_zone)
    TextView tvZone;
    @BindView(R.id.tv_timezone)
    TextView tvTimezone;
    @BindView(R.id.rl_timezone)
    RelativeLayout rlTimezone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timezone);

        setToolbar();
        ButterKnife.bind(this);

        String timezoneId = Hawk.get(Constants.TIMEZONE_ID);
        GlobalUtils.showLog(TAG, "timezoneid check: " + timezoneId);
        if (timezoneId != null) {
            TimeZone timeZone = TimeZone.getTimeZone(timezoneId);
            setTimezone(timeZone);
        } else {
            TimeZone timeZone = TimeZone.getTimeZone("Asia/Kathmandu");
            setTimezone(timeZone);
        }
        rlTimezone.setOnClickListener(v -> startActivityForResult(new
                        Intent(TimezoneActivity.this, SelectTimezoneActivity.class),
                TIMEZONE_RESULT));
    }

    @SuppressLint("SetTextI18n")
    private void setTimezone(TimeZone timeZone) {
        GlobalUtils.showLog(TAG, "timezone id check: " + timeZone.getID());
        String gmt = timeZone.getDisplayName(false, TimeZone.SHORT);
        String zone = timeZone.getID().substring(timeZone.getID().lastIndexOf("/") + 1);

        GlobalUtils.showLog(TAG, "gmt check: " + gmt);
        GlobalUtils.showLog(TAG, "zone check: " + zone);
        tvZone.setText(zone);
        tvTimezone.setText(timeZone.getDisplayName() + " " + "(" + gmt + ")");
    }


    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Time zone");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIMEZONE_RESULT && resultCode == 2) {
            if (data != null) {
                String zone = data.getStringExtra("zone");
                String timezone = data.getStringExtra("timezone");
                String id = data.getStringExtra("id");

                Hawk.put(Constants.ZONE, zone);
                Hawk.put(Constants.TIMEZONE_ID, id);
                Hawk.put(Constants.TIMEZONE, timezone);

                tvZone.setText(zone);
                tvTimezone.setText(timezone);
            }
        }
    }
}