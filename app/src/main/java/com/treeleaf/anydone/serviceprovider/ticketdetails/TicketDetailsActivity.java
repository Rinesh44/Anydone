package com.treeleaf.anydone.serviceprovider.ticketdetails;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.linkshare.LinkShareActivity;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallMvpBaseActivity;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.TICKET_STARTED;

public class TicketDetailsActivity extends VideoCallMvpBaseActivity<TicketDetailsPresenterImpl> implements
        TicketDetailsContract.TicketDetailsView {
    private static final String TAG = "TicketDetailsActivity";
    private static final int NUM_PAGES = 2;
    private static final String MQTT = "MQTT_EVENT_CHECK";
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_problem_stat)
    TextView tvToolbarProblemStat;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.ic_video_call)
    ImageView ivVideoCall;

    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;
    private BottomSheetDialog linkShareBottomSheet;
    private RelativeLayout rlCopy;
    private RelativeLayout rlSms;
    private RelativeLayout rlEmail;
    private RelativeLayout rlOther;
    private String ticketType;

    String shareLink = "";
    private long ticketId;
    private String ticketStatus;

    @Override
    protected int getLayout() {
        return R.layout.activity_ticket_details;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        ticketType = i.getStringExtra("selected_ticket_type");
        ticketStatus = i.getStringExtra("selected_ticket_status");
        ticketId = i.getLongExtra("selected_ticket_id", 0);
        String ticketTitle = i.getStringExtra("ticket_desc");
        String serviceName = i.getStringExtra("selected_ticket_name");
        ArrayList<String> serviceProfileUri = i.getStringArrayListExtra("selected_ticket_icon_uri");
        setUpToolbar(ticketId, ticketTitle);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        createLinkShareBottomSheet();

        if (ticketStatus != null && ticketStatus.equalsIgnoreCase(TICKET_STARTED)) {
            ivVideoCall.setVisibility(View.VISIBLE);
        }

        super.setReferenceId(ticketId);
        super.setRtcContext(Constants.RTC_CONTEXT_TICKET);
        super.setServiceName(serviceName);
        super.setServiceProfileUri(serviceProfileUri);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createLinkShareBottomSheet() {
        linkShareBottomSheet = new BottomSheetDialog(Objects.requireNonNull(getContext()),
                R.style.BottomSheetDialog);
        @SuppressLint("InflateParams") View view = getLayoutInflater()
                .inflate(R.layout.bottom_sheet_link, null);

        linkShareBottomSheet.setContentView(view);
        rlCopy = view.findViewById(R.id.rl_copy);
        rlSms = view.findViewById(R.id.rl_sms);
        rlEmail = view.findViewById(R.id.rl_email);
        rlOther = view.findViewById(R.id.rl_other);

        rlCopy.setOnClickListener(v -> {
            if (!shareLink.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) Objects.requireNonNull(getContext())
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("copied_text", shareLink);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                linkShareBottomSheet.dismiss();
                Toast.makeText(this, "Link copied", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No link found", Toast.LENGTH_SHORT).show();
            }
        });

        rlOther.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        rlSms.setOnClickListener(v -> {
            linkShareBottomSheet.dismiss();
            Intent i = new Intent(this, LinkShareActivity.class);
            i.putExtra("ticket_id", ticketId);
            i.putExtra("is_email", false);
            startActivity(i);
        });

        rlEmail.setOnClickListener(v -> {
            linkShareBottomSheet.dismiss();
            Intent i = new Intent(this, LinkShareActivity.class);
            i.putExtra("ticket_id", ticketId);
            i.putExtra("is_email", true);
            startActivity(i);
        });
    }

    @OnClick(R.id.iv_share)
    public void share() {
        presenter.getShareLink(String.valueOf(ticketId));
        if (linkShareBottomSheet.isShowing()) {
            linkShareBottomSheet.dismiss();
        } else {
            linkShareBottomSheet.show();
        }
    }

    @OnClick(R.id.ic_video_call)
    public void startVideoCall() {
        checkConnection();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ticket_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    @Override
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {

    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setUpToolbar(long ticketId, String problemStat) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText("#" + ticketId);
        tvToolbarProblemStat.setText(problemStat);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            outsideClickListener.onOutsideClick(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onLinkShareSuccess(String link) {
        shareLink = link;
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

    public interface OnOutsideClickListener {
        void onOutsideClick(MotionEvent event);
    }

    public void setOutSideTouchListener(OnOutsideClickListener listener) {
        outsideClickListener = listener;
    }

    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentManager fragmentManager,
                                @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new TicketConversationFragment();

                case 1:
                    return new TicketTimelineFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}