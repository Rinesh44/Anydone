package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.protobuf.ByteString;
import com.orhanobut.hawk.Hawk;
import com.shasin.notificationbanner.Banner;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.base.activity.MvpBaseActivity;
import com.treeleaf.anydone.serviceprovider.landing.LandingActivity;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceRequestRepo;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.ServiceRequestDetailFragment;
import com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment.ActivityFragment;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.ClientActivity;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;

import java.math.BigInteger;
import java.util.Objects;

import butterknife.BindView;

public class ServiceRequestDetailActivity extends MvpBaseActivity
        <ServiceRequestDetailActivityPresenterImpl> implements
        ServiceRequestDetailActivityContract.ServiceRequestDetailActivityView,
        View.OnClickListener {
    private static final String TAG = "ServiceRequestDetail";
    private static final String MQTT = "MQTT_EVENT_CHECK";
    @BindView(R.id.pager)
    ViewPager2 viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_problem_stat)
    TextView tvToolbarProblemStat;
    private ProgressDialog progress;
    private ServiceRequest serviceRequest;

    public OnOutsideClickListener outsideClickListener;
    /**
     * The number of pages (wizard steps) to show in thisActivity demo.
     */
    private static final int NUM_PAGES = 2;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    private long serviceRequestId;

    Callback.HostActivityCallback hostActivityCallbackClient, hostActivityCallbackServer;

    private String janusBaseUrl, apiKey, apiSecret;
    private Account userAccount;
    private String accountId, accountName, accountPicture, rtcMessageId;
    private String serviceName, serviceProfileUri;
    private ClientActivity.VideoCallListener videoCallListenerClient;
    private ServerActivity.VideoCallListener videoCallListenerServer;
    private boolean paymentSuccess = false;
    private boolean videoBroadCastPublish = false;
    private RestChannel.Role mRole;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (serviceRequest.getStatus().equalsIgnoreCase(OrderServiceProto
                .ServiceOrderState.STARTED_SERVICE_ORDER.name()) ||
                serviceRequest.getStatus().equalsIgnoreCase(OrderServiceProto.ServiceOrderState
                        .ACCEPTED_SERVICE_ORDER.name())) {
            getMenuInflater().inflate(R.menu.menu_service_details, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            //TODO: will be needed later
           /* case R.id.action_video_call:
                presenter.checkConnection(TreeleafMqttClient.mqttClient);
                return true;*/
        }
        return false;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_request_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBroadCastPublish = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        setValuesFromIntent();
        userAccount = AccountRepo.getInstance().getAccount();
        accountId = userAccount.getAccountId();
        accountName = userAccount.getFullName();
        accountPicture = userAccount.getProfilePic();
        hostActivityCallbackClient = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {
                presenter.fetchJanusServerUrl(Hawk.get(Constants.TOKEN));
            }

            @Override
            public void specifyRole(RestChannel.Role role) {
                mRole = role;
            }

            @Override
            public void onPublisherVideoStarted() {

            }

            @Override
            public void passCapturedImageFrame(Bitmap bitmap) {

            }

            @Override
            public void showProgressBarUntilMqttResponse() {

            }

            @Override
            public void discardDraw() {

            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
                videoBroadCastPublish = true;
                presenter.publishVideoBroadCastMessage(accountId, accountName, accountPicture,
                        serviceRequestId, String.valueOf(sessionId), String.valueOf(roomId),
                        String.valueOf(participantId), janusBaseUrl, apiSecret, apiKey);

            }

            @Override
            public void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

            }

            @Override
            public void passJoineeReceivedCallback(ClientActivity.VideoCallListener callback) {
                videoCallListenerClient = callback;
            }

            @Override
            public void passJoineeReceivedCallback(ServerActivity.VideoCallListener videoCallListener) {

            }

            @Override
            public void notifyHostHangUp() {
                presenter.publishHostHangUpEvent(accountId, accountName, accountPicture,
                        serviceRequestId, rtcMessageId, videoBroadCastPublish);
            }

            @Override
            public void notifySubscriberLeft() {

            }
        };

        hostActivityCallbackServer = new Callback.HostActivityCallback() {

            @Override
            public void fetchJanusServerInfo() {

            }

            @Override
            public void specifyRole(RestChannel.Role role) {
                mRole = role;
            }

            @Override
            public void passJanusServerInfo(BigInteger sessionId,
                                            BigInteger roomId, BigInteger participantId) {
            }

            @Override
            public void onServiceProviderAudioPublished(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

            }

            @Override
            public void passJoineeReceivedCallback(ClientActivity.VideoCallListener callback) {
                videoCallListenerClient = callback;
            }

            @Override
            public void passJoineeReceivedCallback(ServerActivity.VideoCallListener videoCallListener) {
                videoCallListenerServer = videoCallListener;
            }

            @Override
            public void notifyHostHangUp() {
            }

            @Override
            public void notifySubscriberLeft() {
                presenter.publishParticipantLeftEvent(accountId, accountName, accountPicture, serviceRequestId);
            }

            @Override
            public void onPublisherVideoStarted() {
                presenter.publishSubscriberJoinEvent(accountId, accountName, accountPicture, serviceRequestId);
            }

            @Override
            public void passCapturedImageFrame(Bitmap bitmap) {

            }

            @Override
            public void showProgressBarUntilMqttResponse() {

            }

            @Override
            public void discardDraw() {

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

//    public void onImageReceivedFromConsumer(ByteString byteString){
//        byte[] convertedBytes = byteString.toByteArray();
//
//        Bitmap compressedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
////        videoCallListenerServer.
//    }

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
    protected void injectDagger() {
        getActivityComponent().inject(this);
    }

    private void setUpToolbar(String serviceName, String problemStat) {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tvToolbarTitle.setText(serviceName.replace("_", " "));
        tvToolbarProblemStat.setText(problemStat);
    }

    private void setValuesFromIntent() {
        Intent i = getIntent();
        serviceRequestId = i.getLongExtra("selected_service_id", -1);
        serviceName = i.getStringExtra("selected_service_name");
        serviceProfileUri = i.getStringExtra("selected_service_icon_uri");
        serviceRequest = ServiceRequestRepo.getInstance().
                getServiceRequestById(serviceRequestId);
        paymentSuccess = i.getBooleanExtra("payment_success", false);
        setUpToolbar(serviceRequest.getServiceName(), serviceRequest.getProblemStatement());
        GlobalUtils.showLog(TAG, "problem stat;:" + serviceRequest.getProblemStatement());
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onUrlFetchSuccess(String janusBaseUrl, String apiKey, String apiSecret) {
        this.janusBaseUrl = janusBaseUrl;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        Log.d(TAG, "janus server info: " + janusBaseUrl + apiKey + apiSecret);
        videoCallListenerClient.onJanusCredentialsReceived(janusBaseUrl, apiKey,
                apiSecret, serviceName, serviceProfileUri);
    }

    @Override
    public void onUrlFetchFail(String msg) {
        videoCallListenerClient.onJanusCredentialsFailure();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServiceRequestDetailActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuccess() {
        ClientActivity.launch(ServiceRequestDetailActivity.this,
                false, hostActivityCallbackClient,
                serviceName, serviceProfileUri);
    }

    @Override
    public void onConnectionFail(String msg) {
        Banner.make(Objects.requireNonNull(this).getWindow().getDecorView().getRootView(),
                this, Banner.ERROR, msg, Banner.TOP, 2000).show();
    }

    @Override
    public void showProgressBar(String message) {
        progress = ProgressDialog.show(this, null, message, true);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    @Override
    public void onFailure(String message) {
        showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this;
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
                    return new ServiceRequestDetailFragment();

                case 1:
                    return new ActivityFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        UiUtils.hideKeyboardForced(this);

        if (paymentSuccess) {
            Intent i = new Intent(ServiceRequestDetailActivity.this,
                    LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
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

}
