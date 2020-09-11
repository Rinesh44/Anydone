package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.google.protobuf.ByteString;
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
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.UiUtils;
import com.treeleaf.januswebrtc.Callback;
import com.treeleaf.januswebrtc.GeneralUtils;
import com.treeleaf.januswebrtc.RestChannel;
import com.treeleaf.januswebrtc.ServerActivity;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import java.math.BigInteger;
import java.util.Objects;

import butterknife.BindView;

public class ServiceRequestDetailActivity extends MvpBaseActivity
        <ServiceRequestDetailActivityPresenterImpl> implements
        ServiceRequestDetailActivityContract.ServiceRequestDetailActivityView,
        View.OnClickListener, Callback.OnDrawEventListener {
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
    @BindView(R.id.pb_progress)
    ProgressBar progress;

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

    Callback.HostActivityCallback hostActivityCallbackServer;

    private Account userAccount;
    private String accountId, accountName, accountPicture, rtcMessageId;
    private String serviceName, serviceProfileUri;
    private ServerActivity.VideoCallListener videoCallListenerServer;
    private ServerActivity.ServerDrawingPadEventListener serverDrawingPadEventListener;
    private boolean paymentSuccess = false;
    private RestChannel.Role mRole;
    private Callback.DrawCallBack drawCallBack;

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

    private void captureImageFrame(Bitmap caputureBitmap) {
        Bitmap bitmap = caputureBitmap;
        Bitmap convertedBitmap;
        try {
            convertedBitmap = UiUtils.getResizedBitmap(bitmap, 400);
            byte[] bytes = GlobalUtils.bitmapToByteArray(convertedBitmap);
            ByteString imageByteString = ByteString.copyFrom(bytes);
            int localDeviceWidth = GeneralUtils.getDeviceResolution(ServiceRequestDetailActivity.this)[0];
            int localDeviceHeight = GeneralUtils.getDeviceResolution(ServiceRequestDetailActivity.this)[1];
            presenter.publishSendImageToRemoteEvent(accountId, accountName, accountPicture, serviceRequestId, imageByteString,
                    localDeviceWidth, localDeviceHeight, System.currentTimeMillis());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_service_request_detail;
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            public void passJoineeReceivedCallback(Callback.AudioVideoCallbackListener videoCallListener) {
                videoCallListenerServer = (ServerActivity.VideoCallListener) videoCallListener;
            }

            @Override
            public void passDrawPadEventListenerCallback(Callback.DrawPadEventListener drawPadEventListener) {
                serverDrawingPadEventListener = (ServerActivity.ServerDrawingPadEventListener) drawPadEventListener;
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

        };

        drawCallBack = new Callback.DrawCallBack() {

            @Override
            public void onNewImageAcknowledge(int width, int height, long timeStamp) {
                presenter.publishSendAckToRemoteEvent(accountId, accountName, accountPicture, serviceRequestId,
                        width, height, System.currentTimeMillis());
            }

            @Override
            public void onHoldDraw() {
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawShowProgress();//TODO: uncomment this later
            }

            @Override
            public void onDiscardDraw() {
                presenter.publishCancelDrawEvent(accountId, accountName, accountPicture, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onDrawCanvasCleared() {
                presenter.publishDrawCanvasClearEvent(accountId, accountName, accountPicture, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId) {
                presenter.publishDrawReceiveNewTextEvent(accountId, accountName, accountPicture, x, y,
                        editTextFieldId, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                presenter.publishTextFieldChangeEventEvent(accountId, accountName, accountPicture, text,
                        id, serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                presenter.publishTextFieldRemoveEventEvent(accountId, accountName, accountPicture, editTextId,
                        serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawMetaChangeEvent(accountId, accountName, accountPicture, captureDrawParam.getXCoordinate(),
                        captureDrawParam.getYCoordinate(), captureDrawParam.getBrushWidth(),
                        Float.parseFloat(captureDrawParam.getBrushOpacity().toString()),
                        captureDrawParam.getBrushColor(), serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onStartDraw(float x, float y) {
                presenter.publishDrawTouchDownEvent(accountId, accountName, accountPicture,
                        serviceRequestId, x, y, System.currentTimeMillis());
            }

            @Override
            public void onClientTouchMove(CaptureDrawParam captureDrawParam) {
                presenter.publishDrawTouchMoveEvent(accountId, accountName, accountPicture,
                        serviceRequestId, captureDrawParam.getXCoordinate(), captureDrawParam.getYCoordinate(),
                        System.currentTimeMillis());
            }

            @Override
            public void onClientTouchUp() {
                presenter.publishDrawTouchUpEvent(accountId, accountName, accountPicture,
                        serviceRequestId, System.currentTimeMillis());
            }

            @Override
            public void onNewImageFrameCaptured(Bitmap bitmap) {
                captureImageFrame(bitmap);
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
                roomNumber, participantId, hostActivityCallbackServer, drawCallBack, calleeName, calleeProfileUrl);

    }

    public void onImageReceivedFromConsumer(int width, int height, long captureTime, byte[] convertedBytes) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawNewImageCaptured(width, height, captureTime, convertedBytes);
                }
            }
        });

    }

    public void onImageDrawDiscardRemote() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "onImageReceivedFromConsumer");
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawDiscard();
            }
        });
    }

    public void onImageDrawDiscardLocal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null)
                    serverDrawingPadEventListener.onDrawHideProgress();
            }
        });
    }

    public void onImageCaptured() {
        Log.d(TAG, "onImageReceivedFromConsumer");
        if (serverDrawingPadEventListener != null)
            serverDrawingPadEventListener.onDrawHideProgress();
    }

    public void onImageAckSent() {
        if (serverDrawingPadEventListener != null)
            serverDrawingPadEventListener.onDrawDisplayCapturedImage();
    }

    public void onRemoteDeviceConfigReceived(SignalingProto.StartDrawAcknowledgement startDrawAckResponse) {
        if (serverDrawingPadEventListener != null) {
            int width = startDrawAckResponse.getBitmapWidth();
            int height = startDrawAckResponse.getBitmapHeight();
            long timeStamp = startDrawAckResponse.getCapturedTime();
            serverDrawingPadEventListener.onDrawRemoteDeviceConfigReceived(width, height, timeStamp);
            serverDrawingPadEventListener.onDrawHideProgress();
        }
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
    public void onDrawTouchDown(CaptureDrawParam captureDrawParam) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawNewDrawCoordinatesReceived(captureDrawParam.getXCoordinate(),
                    captureDrawParam.getYCoordinate());
            serverDrawingPadEventListener.onDrawTouchDown();
        }
    }

    @Override
    public void onDrawTouchMove(CaptureDrawParam captureDrawParam) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawNewDrawCoordinatesReceived(captureDrawParam.getXCoordinate(),
                    captureDrawParam.getYCoordinate());
            serverDrawingPadEventListener.onDrawTouchMove();
        }
    }

    @Override
    public void onDrawTouchUp() {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawTouchUp();
        }
    }

    @Override
    public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveNewTextField(x, y, editTextFieldId);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveNewTextChange(String text, String id) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveNewTextChange(text, id);
                }
            }
        });
    }

    @Override
    public void onDrawReceiveEdiTextRemove(String editTextId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawReceiveEdiTextRemove(editTextId);
                }
            }
        });
    }

    @Override
    public void onDrawParamChanged(CaptureDrawParam captureDrawParam) {
        if (serverDrawingPadEventListener != null) {
            serverDrawingPadEventListener.onDrawParamChanged(captureDrawParam);
        }
    }

    @Override
    public void onDrawCanvasCleared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (serverDrawingPadEventListener != null) {
                    serverDrawingPadEventListener.onDrawCanvasCleared();
                }
            }
        });

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
    public void showProgressBar(String message) {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void showToastMessage(String message) {
        UiUtils.showSnackBar(this, getWindow().getDecorView().getRootView(), message);
    }

    @Override
    public void hideProgressBar() {
        if (progress != null) {
            progress.setVisibility(View.GONE);
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
