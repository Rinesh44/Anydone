package com.treeleaf.anydone.serviceprovider.setting.video;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.treeleaf.anydone.serviceprovider.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoSettingActivity extends AppCompatActivity {
    @BindView(R.id.tv_use_camera2)
    TextView tvUseCamera2;
    @BindView(R.id.tv_disable_audio_processing)
    TextView tvDisableAudioProcessing;
    @BindView(R.id.cb_use_camera2)
    CheckBox cbUseCamera2;
    @BindView(R.id.cb_disable_audio_processing)
    CheckBox cbDisableAudioProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_setting);

        ButterKnife.bind(this);
        setToolbar();

        tvUseCamera2.setOnClickListener(v -> {
            if (!cbUseCamera2.isChecked()) {
                cbUseCamera2.setChecked(true);
            } else {
                cbUseCamera2.setChecked(false);
            }
        });

        tvDisableAudioProcessing.setOnClickListener(v -> {
            if (!cbDisableAudioProcessing.isChecked()) {
                cbDisableAudioProcessing.setChecked(true);
            } else {
                cbDisableAudioProcessing.setChecked(false);
            }
        });
    }


    private void setToolbar() {
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Video setting");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0,
                str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}