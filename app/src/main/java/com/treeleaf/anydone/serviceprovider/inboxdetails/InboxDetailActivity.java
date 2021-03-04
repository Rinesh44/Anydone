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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxConversation.InboxConversationFragment;
import com.treeleaf.anydone.serviceprovider.inboxdetails.inboxtimeline.InboxTimelineFragment;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.OnInboxEditListener;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.anydone.serviceprovider.videocallreceive.VideoCallMvpBaseActivity;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

import static com.treeleaf.januswebrtc.Const.CONSUMER_TYPE;
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

    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;
    Inbox inbox;

    private Account userAccount;
    private String customerId;
    private String accountType = SERVICE_PROVIDER_TYPE;//default is service provider
    private InboxConversationFragment inboxConversationFragment;

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
        String inboxId = i.getStringExtra("inbox_id");
        inbox = InboxRepo.getInstance().getInboxById(inboxId);
        setUpToolbar(inbox);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        userAccount = AccountRepo.getInstance().getAccount();




//        if (customer != null && localAccountId.equals(customer.getCustomerId())
//                && !customerName.isEmpty()) {
//            accountType = CONSUMER_TYPE;
//        } else
//            accountType = SERVICE_PROVIDER_TYPE;
        accountType = CONSUMER_TYPE;

        super.setReferenceId(Long.parseLong(inboxId));
        super.setRtcContext(Constants.RTC_CONTEXT_INBOX);
        super.setServiceName("some service name");
        super.setServiceProfileUri(new ArrayList<String>(
                Arrays.asList("Buenos Aires", "Córdoba", "La Plata")));
        super.setAccountType(accountType);

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
        super.onBackPressed();
        inboxConversationFragment.unSubscribeMqttTopics();
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
            if (inbox.isValid() && inbox.isSelfInbox()) return 1;
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

    public interface MqttDelegate {
        void unSubscribeMqttTopics();
    }

}
