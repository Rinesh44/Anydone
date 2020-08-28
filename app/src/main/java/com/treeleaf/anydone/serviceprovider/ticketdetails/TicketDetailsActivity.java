package com.treeleaf.anydone.serviceprovider.ticketdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.ticketdetails.ticketconversation.TicketConversationFragment;
import com.treeleaf.anydone.serviceprovider.ticketdetails.tickettimeline.TicketTimelineFragment;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.ClientActivity;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;

import java.math.BigInteger;
import java.util.Objects;

import butterknife.BindView;

public class TicketDetailsActivity extends MvpBaseActivity<TicketDetailsPresenterImpl> implements
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

    public OnOutsideClickListener outsideClickListener;
    private FragmentStateAdapter pagerAdapter;

    Callback.HostActivityCallback hostActivityCallbackServer;

    private Account userAccount;
    private String accountId, accountName, accountPicture, rtcMessageId;
    private ServerActivity.VideoCallListener videoCallListenerServer;

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
        long ticketId = i.getLongExtra("selected_ticket_id", 0);
        String ticketTitle = i.getStringExtra("ticket_desc");
        setUpToolbar(ticketId, ticketTitle);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        userAccount = AccountRepo.getInstance().getAccount();
        accountId = userAccount.getAccountId();
        accountName = userAccount.getFullName();
        accountPicture = userAccount.getProfilePic();

        hostActivityCallbackServer = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
            }

            @Override
            public void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

            }

            @Override
            public void passJoineeReceivedCallback(Callback.AudioVideoCallbackListener videoCallListener) {
                videoCallListenerServer = (ServerActivity.VideoCallListener) videoCallListener;
            }

            @Override
            public void notifyHostHangUp() {
            }

            @Override
            public void notifySubscriberLeft() {
                presenter.publishParticipantLeftEvent(accountId, accountName, accountPicture, ticketId);
            }

            @Override
            public void onPublisherVideoStarted() {
                presenter.publishSubscriberJoinEvent(accountId, accountName, accountPicture, ticketId);
            }

        };
    }

    public void onVideoRoomInitiationSuccess(SignalingProto.BroadcastVideoCall broadcastVideoCall,
                                             boolean videoBroadcastPublish) {
        Log.d(MQTT, "onVideoRoomInitiationSuccess");
        rtcMessageId = broadcastVideoCall.getRtcMessageId();
        String janusServerUrl = broadcastVideoCall.getAvConnectDetails().getBaseUrl();
        String janusApiKey = broadcastVideoCall.getAvConnectDetails().getApiKey();
        String janusApiSecret = broadcastVideoCall.getAvConnectDetails().getApiSecret();
        String roomNumber = broadcastVideoCall.getRoomId();
        String participantId = broadcastVideoCall.getParticipantId();

        String calleeName = broadcastVideoCall.getSenderAccount().getFullName();
        String calleeProfileUrl = broadcastVideoCall.getSenderAccount().getProfilePic();

        ServerActivity.launch(this, janusServerUrl, janusApiKey, janusApiSecret,
                roomNumber, participantId, hostActivityCallbackServer, calleeName, calleeProfileUrl);

    }

    public void onImageReceivedFromConsumer(int width, int height, long captureTime, byte[] convertedBytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (videoCallListenerServer != null) {
                    videoCallListenerServer.onImageReceivedForDrawing(width, height, captureTime, convertedBytes);
                }
            }
        });

    }

    public void onImageDrawDiscard() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onImageReceivedFromConsumer");
                if (videoCallListenerServer != null)
                    videoCallListenerServer.onImageDrawDiscard();
            }
        });
    }

    public void onVideoRoomJoinSuccess(SignalingProto.VideoCallJoinResponse videoCallJoinResponse) {
        Log.d(MQTT, "onVideoRoomJoinSuccess");
        if (videoCallListenerServer != null) {
            UserProto.Account account = videoCallJoinResponse.getSenderAccount();
            videoCallListenerServer.onJoineeReceived(account.getFullName(),
                    account.getProfilePic(), account.getAccountId());
        }
    }

    public void onParticipantLeft(SignalingProto.ParticipantLeft participantLeft) {
        Log.d(MQTT, "onParticipantLeft");
        if (videoCallListenerServer != null) {
            UserProto.Account account = participantLeft.getSenderAccount();
            videoCallListenerServer.onJoineeRemoved(account.getAccountId());
        }
    }

    public void onHostHangUp(SignalingProto.VideoRoomHostLeft videoRoomHostLeft) {
        Log.d(MQTT, "onHostHangUp");
        if (videoCallListenerServer != null)
            videoCallListenerServer.onHostTerminateCall();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
//            case R.id.action_video_call:
//                return true;
        }
        return false;
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