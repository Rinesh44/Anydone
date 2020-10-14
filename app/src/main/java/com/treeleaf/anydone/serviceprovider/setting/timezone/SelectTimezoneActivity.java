package com.treeleaf.anydone.serviceprovider.setting.timezone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.adapters.TimezoneAdapter;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.model.TimezoneResult;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class SelectTimezoneActivity extends MvpBaseActivity<TimezonePresenterImpl> implements
        TimezoneContract.TimezoneView {
    private static final String TAG = "SelectTimezoneActivity";
    @BindView(R.id.et_search_timezone)
    EditText etSearchTimezone;
    @BindView(R.id.rv_timezone)
    RecyclerView rvTimezone;
    @BindView(R.id.pb_progress)
    ProgressBar progressDialog;

    private TimezoneAdapter adapter;
    private TimezoneResult selectedTimezone;

    @Override
    protected int getLayout() {
        return R.layout.activity_select_timezone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbar();

        List<TimezoneResult> timezoneResults = getTimezoneList();
        setUpRecyclerView(timezoneResults);

        UiUtils.showKeyboard(this, etSearchTimezone);
        etSearchTimezone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private List<TimezoneResult> getTimezoneList() {
        List<TimezoneResult> timezoneResults = new ArrayList<>();
        String[] timezones = TimeZone.getAvailableIDs();
        for (String timezone : timezones
        ) {
            TimeZone timezone1 = TimeZone.getTimeZone(timezone);
            String zone = timezone1.getID().substring(timezone1.getID().lastIndexOf("/") + 1);
            String gmt = timezone1.getDisplayName(false, TimeZone.SHORT);
            String timezoneConcatenated = timezone1.getDisplayName() + " " + "(" + gmt + ")";

            TimezoneResult timezoneResult = new TimezoneResult();
            timezoneResult.setId(timezone);
            timezoneResult.setZone(zone.replace("_", " "));
            timezoneResult.setTimezone(timezoneConcatenated);

            timezoneResults.add(timezoneResult);
        }

        return timezoneResults;
    }


    private void setToolbar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.white_bg));

        SpannableStringBuilder str = new SpannableStringBuilder("Select time zone");
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setUpRecyclerView(List<TimezoneResult> timezoneResults) {
        rvTimezone.setLayoutManager(new LinearLayoutManager(this));

        adapter = new TimezoneAdapter(timezoneResults, this);
        adapter.setOnItemClickListener(timezoneResult -> {
            selectedTimezone = timezoneResult;
            String token = Hawk.get(Constants.TOKEN);

            GlobalUtils.showLog(TAG, "sending timezone id: " + timezoneResult.getId());
            try {
                presenter.addTimezone(token, timezoneResult.getId());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });
        rvTimezone.setAdapter(adapter);
    }

    @Override
    public void onTimezoneAddSuccess() {
        Intent intent = new Intent();
        intent.putExtra("zone", selectedTimezone.getZone());
        intent.putExtra("timezone", selectedTimezone.getTimezone());
        intent.putExtra("id", selectedTimezone.getId());

        GlobalUtils.showLog(TAG, "zone:" + selectedTimezone.getZone());
        GlobalUtils.showLog(TAG, "timezone: " + selectedTimezone.getTimezone());
        String gmt = getGmtFromTimezone(selectedTimezone.getTimezone());
        Hawk.put(Constants.TIMEZONE_GMT, gmt);
        setResult(2, intent);
        finish();
    }

    private String getGmtFromTimezone(String timezone) {
        Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(timezone);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    @Override
    public void onTimezoneAddFail(String msg) {
        if (msg.equalsIgnoreCase(Constants.AUTHORIZATION_FAILED)) {
            UiUtils.showToast(this, msg);
            onAuthorizationFailed(this);
        } else {
            UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), msg);
        }
    }

    @Override
    public void showProgressBar(String message) {
        progressDialog.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }
}