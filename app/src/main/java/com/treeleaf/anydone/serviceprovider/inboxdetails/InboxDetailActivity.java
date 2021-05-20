package com.treeleaf.anydone.serviceprovider.inboxdetails;

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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation.InboxConversationFragment;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline.InboxTimelineFragment;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.OnInboxEditListener;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallMvpBaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.RTC_CONTEXT_INBOX;
import static com.treeleaf.januswebrtc.Const.SERVICE_PROVIDER_TYPE;

public class InboxDetailActivity extends VideoCallMvpBaseActivity<InboxDetailPresenterImpl> implements
        InboxDetailContract.InboxDetailView, OnInboxEditListener {
    private static final String TAG = "InboxDetailActivity";
    private static final int NUM_PAGES = 2;
    private static final String MQTT = "MQTT_EVENT_CHECK";
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.pb_progress)
    ProgressBar progress;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.ic_video_call)
    ImageView ivVideoCall;
    @BindView(R.id.iv_info)
    ImageView ivInfo;

    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;
    Inbox inbox;
    boolean isNotification = false;

    private Account userAccount;
    private String customerId;
    private String accountType = SERVICE_PROVIDER_TYPE;//default is service provider
    private InboxConversationFragment inboxConversationFragment;
    private String localAccountId;

    @Override
    protected int getLayout() {
        return R.layout.activity_inbox_details;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        GlobalUtils.showLog(TAG, "data from extras: " + extras.getString("inboxId"));
        String inboxId = i.getStringExtra("inbox_id");
        if (inboxId == null || inboxId.isEmpty())
            inboxId = extras.getString("inboxId");
        isNotification = i.getBooleanExtra("notification", false);

        if (extras != null && !isNotification) {
            String notification = extras.getString("notification");
            if (notification != null)
                isNotification = notification.equalsIgnoreCase("true");
        }

        GlobalUtils.showLog(TAG, "inbox id check from notification: " + inboxId);
        inbox = InboxRepo.getInstance().getInboxById(inboxId);
        if (inbox != null) {
            setUpToolbar(inbox);
            if (inbox.isLeftGroup()) ivVideoCall.setVisibility(View.GONE);

        }

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        userAccount = AccountRepo.getInstance().getAccount();
        localAccountId = userAccount.getAccountId();

        ArrayList<String> employeeProfileUris = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        setCallVisibility("SHOW");
        if (inbox != null && inbox.getParticipantList().size() >= 3) {
            setCallVisibility("HIDE");
        }

        if (inbox != null) {
            for (Participant participant : inbox.getParticipantList()) {
                if (!localAccountId.equals(participant.getEmployee().getAccountId())) {
                    builder.append(participant.getEmployee().getName());
                    builder.append(",");
                    employeeProfileUris.add(participant.getEmployee().getEmployeeImageUrl());
                }
            }
            String assignedEmployeeList = builder.toString().trim();
            String callees = GlobalUtils.removeLastCharater(assignedEmployeeList);
            accountType = SERVICE_PROVIDER_TYPE;

            super.setIsCallMultiple(inbox.getParticipantList().size() >= 3);
            super.setReferenceId(inboxId);
            super.setServiceName(callees);
            super.setServiceProfileUri(employeeProfileUris);
            super.setAccountType(accountType);
        }

        ivInfo.setOnClickListener(view -> viewPager.setCurrentItem(1, true));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (position == 1) {
                    ivInfo.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivVideoCall.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_END);

                    ivVideoCall.setLayoutParams(params);
                } else {
                    if (!inbox.isSelfInbox())
                        ivInfo.setVisibility(View.VISIBLE);
                    else ivInfo.setVisibility(View.GONE);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivVideoCall.getLayoutParams();
                    params.addRule(RelativeLayout.START_OF, R.id.iv_info);
                    params.addRule(RelativeLayout.ALIGN_PARENT_END, 0);
                    ivVideoCall.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    protected String getCallContext() {
        return RTC_CONTEXT_INBOX;
    }

    @OnClick(R.id.ic_video_call)
    public void startVideoCall() {
        checkConnection(accountType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
//            case R.id.action_video_call:
//                return true;
        }
        return false;
    }

    @OnClick(R.id.iv_back)
    public void back() {
        hideKeyBoard();
        onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_service_details, menu);
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
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(),
                Constants.SERVER_ERROR);
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setUpToolbar(Inbox inbox) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        String subject = inbox.getSubject();
        String participants = GlobalUtils.getAllParticipants(inbox);

        if (subject != null && !subject.isEmpty()) {
            tvToolbarTitle.setText(subject);
        } else {
            tvToolbarTitle.setText(participants);
        }
    }

    @Override
    public void onBackPressed() {
        GlobalUtils.showLog(TAG, "onbackpress called()");
        inboxConversationFragment.unSubscribeMqttTopics();

        GlobalUtils.showLog(TAG, "back press notification check: " + isNotification);

        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0);
        } else if (isNotification) {
            Intent i = new Intent(InboxDetailActivity.this, LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        } else super.onBackPressed();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            outsideClickListener.onOutsideClick(event);
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onSubjectEdit(String inboxId) {
        Inbox inbox = InboxRepo.getInstance().getInboxById(inboxId);
        tvToolbarTitle.setText(inbox.getSubject());
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
                    inboxConversationFragment = new InboxConversationFragment();
                    return inboxConversationFragment;

                case 1:
                    return new InboxTimelineFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            if (inbox != null && inbox.isValid() && inbox.isSelfInbox()) return 1;
            else return NUM_PAGES;
        }

    }


    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof InboxTimelineFragment) {
            ((InboxTimelineFragment) fragment).setOnSubjectChangeListener(this);
        }
        if (fragment instanceof InboxConversationFragment) {
            ((InboxConversationFragment) fragment).setOnVideoCallBackListener(this);
        }
    }

    private void setCallVisibility(String visibility) {
        boolean show = visibility.equals("SHOW");
        DrawableCompat.setTint(
                DrawableCompat.wrap(ivVideoCall.getDrawable()),
                ContextCompat.getColor(getContext(), show ? R.color.colorPrimary : R.color.selector_disabled)
        );
        ivVideoCall.setEnabled(show);

        if (inbox.isSelfInbox()) {
            ivVideoCall.setVisibility(View.GONE);
            ivInfo.setVisibility(View.GONE);
        }
    }

    public interface MqttDelegate {
        void unSubscribeMqttTopics();
    }

}
