package com.treeleaf.januswebrtc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.EglRenderer;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;

import java.math.BigInteger;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.freedrawingdemo.freedrawing.util.ForwardTouchesView;
import com.treeleaf.freedrawingdemo.freedrawing.util.GeneralUtil;
import com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionParameters;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionEvents;

import static com.treeleaf.januswebrtc.Const.CALLEE_NAME;
import static com.treeleaf.januswebrtc.Const.CALLEE_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.CLIENT;
import static com.treeleaf.januswebrtc.Const.JANUS_API_KEY;
import static com.treeleaf.januswebrtc.Const.JANUS_API_SECRET;
import static com.treeleaf.januswebrtc.Const.JANUS_CREDENTIALS_SET;
import static com.treeleaf.januswebrtc.Const.JANUS_URL;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.TRUE;

public class ClientActivity extends PermissionHandlerActivity implements Callback.JanusRTCInterface, Callback.ApiCallback, PeerConnectionEvents, Callback.IceConnectionChangeEvents {
    private static final String TAG = ClientActivity.class.getSimpleName();

    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer localRender;
    private VideoCapturer videoCapturer;
    private EglBase rootEglBase;
    private RestChannel mRestChannel;
    private TextView tvRoomNumber;
    private ProgressDialog progressDialog;
    private View layoutDraw;
    private ImageView fabStartDraw;
    private ImageView fabDiscardDraw;
    private ImageView imageViewCaptureImageLocal;
    private ImageView imageVideoToggle, imageAudioToggle, imageScreenShot, imageSwitchCamera,
            imageEndCall, ivScreenShotImage, ibToggleJoineeList, ivSignalStrength;
    private TreeleafDrawPadView treeleafDrawPadViewLocal;
    private ForwardTouchesView forwardTouchesView;
    private ConstraintLayout clCallSettings, clCallOtions;
    private RelativeLayout rlScreenShotImage;
    private RelativeLayout rlJoineeList;
    private View viewVideoCallStart;
    private ImageView ivCalleeProfile, ivTerminateCall;
    private TextView tvCalleeName;
    private EglRenderer.FrameListener frameListener;

    private Boolean videoOff = false;
    private Boolean audioOff = false;
    private Boolean justScreenShot = false;
    private Boolean showFullList = false;

    private String calleeName, calleeProfile;

    private RecyclerView rvJoinee;
    private JoineeListAdapter joineeListAdapter;
    private static Callback.HostActivityCallback mhostActivityCallback;
    private VideoCallListener videoCallListener;
    private boolean callTerminated = false;
    private Handler handler;
    private Runnable runnable;
    private int timerDelay = 10000;
    private Boolean credentialsReceived;

    public static void launch(Context context, boolean credentialsAvailable, String janusServerUrl, String apiKey, String apiSecret,
                              String calleeName, String callProfileUrl) {
        Intent intent = new Intent(context, ClientActivity.class);
        intent.putExtra(JANUS_CREDENTIALS_SET, credentialsAvailable);
        intent.putExtra(JANUS_URL, janusServerUrl);
        intent.putExtra(JANUS_API_KEY, apiKey);
        intent.putExtra(JANUS_API_SECRET, apiSecret);
        intent.putExtra(CALLEE_NAME, calleeName);
        intent.putExtra(CALLEE_PROFILE_URL, callProfileUrl);
        context.startActivity(intent);
    }

    public static void launch(Context context, boolean credentialsAvailable, Callback.HostActivityCallback hostActivityCallBack,
                              String calleeName, String callProfileUrl) {
        mhostActivityCallback = hostActivityCallBack;
        Intent intent = new Intent(context, ClientActivity.class);
        intent.putExtra(JANUS_CREDENTIALS_SET, credentialsAvailable);
        intent.putExtra(CALLEE_NAME, calleeName);
        intent.putExtra(CALLEE_PROFILE_URL, callProfileUrl);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        tvRoomNumber = findViewById(R.id.tv_room_number);
        layoutDraw = findViewById(R.id.layout_draw);
        fabStartDraw = findViewById(R.id.fab_start_draw);
        fabDiscardDraw = findViewById(R.id.fab_discard_draw);
        imageViewCaptureImageLocal = findViewById(R.id.iv_captured_image_local);
        treeleafDrawPadViewLocal = findViewById(R.id.treeleaf_draw_pad_local);
        localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        clCallSettings = findViewById(R.id.cl_call_setting);
        clCallOtions = findViewById(R.id.cl_call_options);
        imageVideoToggle = findViewById(R.id.image_video);
        imageAudioToggle = findViewById(R.id.image_mic);
        imageScreenShot = findViewById(R.id.image_screenshot);
        imageSwitchCamera = findViewById(R.id.image_switch_camera);
        imageEndCall = findViewById(R.id.image_end_call);
        ivSignalStrength = findViewById(R.id.iv_signal_strength);
        rlScreenShotImage = findViewById(R.id.rl_screenshot_image);
        ivScreenShotImage = findViewById(R.id.iv_screenshot_image);
        rlJoineeList = findViewById(R.id.rl_joinee_list);
        ibToggleJoineeList = findViewById(R.id.ib_toggle_joinee_list);
        viewVideoCallStart = findViewById(R.id.view_video_call_start);
        tvCalleeName = findViewById(R.id.tv_callee_name);
        ivCalleeProfile = findViewById(R.id.iv_callee_profile);
        ivTerminateCall = findViewById(R.id.iv_terminate_call);

        imageVideoToggle.setOnClickListener(videoToggleClickListener);
        imageAudioToggle.setOnClickListener(audioToggleClickListener);
        imageScreenShot.setOnClickListener(screenShotClickListener);
        imageSwitchCamera.setOnClickListener(switchCameraClickListener);
        imageEndCall.setOnClickListener(endCallClickListener);
        ivTerminateCall.setOnClickListener(endCallClickListener);
        ibToggleJoineeList.setOnClickListener(joineeListToggleListener);

        forwardTouchesView = findViewById(R.id.forward_touch);

        forwardTouchesView.setForwardTo(treeleafDrawPadViewLocal);
        fabStartDraw.setOnClickListener(startDrawClickListener);
        fabDiscardDraw.setOnClickListener(discardDrawClickListener);

        setUpProgressDialog();


        frameListener = new EglRenderer.FrameListener() {
            @Override
            public void onFrame(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (justScreenShot) {
                            ivScreenShotImage.setImageBitmap(bitmap);
                            treeleafDrawPadViewLocal.takeAndSaveScreenShot(rlScreenShotImage);
                            justScreenShot = false;
                        } else
                            imageViewCaptureImageLocal.setImageBitmap(bitmap);

                        /**
                         * remove frame listener as soon as you get the captured image.
                         */
                        localRender.removeFrameListener(frameListener);
                    }
                });
            }
        };

        videoCallListener = new VideoCallListener() {
            @Override
            public void onJoineeReceived(String joineeName, String joineedProfileUrl, String accountId) {
                //add new joinee information in view
                Log.d(TAG, "onjoineereceived");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joineeListAdapter.addNewJoinee(new Joinee(joineeName, joineedProfileUrl, accountId), showFullList);
                    }
                });

            }

            @Override
            public void onJoineeRemoved(String accountId) {
                Log.d(TAG, "onJoineeRemoved");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joineeListAdapter.removeJoinee(accountId, showFullList);
                    }
                });

            }

            @Override
            public void onJanusCredentialsReceived(String baseUrl, String apiKey, String apiSecret,
                                                   String serviceName, String serviceUri) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        credentialsReceived = true;
                        calleeName = serviceName;
                        calleeProfile = serviceUri;

                        mRestChannel = new RestChannel(ClientActivity.this, baseUrl, apiKey, apiSecret);
                        mRestChannel.setDelegate(ClientActivity.this);
                        mRestChannel.setApiCallback(ClientActivity.this);

                        if (permissionsGranted) {
                            onPermissionGranted();
                        }
                    }
                });
            }

            @Override
            public void onJanusCredentialsFailure() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        terminateBroadCast();
                    }
                });
            }

            @Override
            public void onVideoViewReady() {
                showVideoCallStartView(false);
            }
        };
        setUpRecyclerView();
        setUpNetworkStrengthHandler();
        if (mhostActivityCallback != null) {
            mhostActivityCallback.passJoineeReceivedCallback(videoCallListener);
            mhostActivityCallback.fetchJanusServerInfo();
        }


        //if condition here
        credentialsReceived = getIntent().getBooleanExtra(JANUS_CREDENTIALS_SET, false);
        calleeName = getIntent().getStringExtra(CALLEE_NAME);
        calleeProfile = getIntent().getStringExtra(CALLEE_PROFILE_URL);
        if (credentialsReceived) {
            String baseUrl = getIntent().getStringExtra(JANUS_URL);
            String apiKey = getIntent().getStringExtra(JANUS_API_KEY);
            String apiSecret = getIntent().getStringExtra(JANUS_API_SECRET);

            mRestChannel = new RestChannel(this, baseUrl, apiKey, apiSecret);
            mRestChannel.setDelegate(this);
            mRestChannel.setApiCallback(this);
        }

        permissionsGranted = checkPermission();
        if (permissionsGranted) {
            onPermissionGranted();
        }
        loadCallNameAndProfileIcon(calleeName, calleeProfile);
    }

    private void setUpNetworkStrengthHandler() {
        runnable = new Runnable() {
            @Override
            public void run() {
                onNetworkNormalStrength();
            }
        };
        handler = new Handler();
    }

    private void onNetworkNormalStrength() {
        peerConnectionClient.changeCaptureFormat(640, 480, 30);
        ivSignalStrength.setImageResource(R.drawable.ic_good_signal);
    }

    private void loadCallNameAndProfileIcon(String calleeName, String calleeProfile) {
        tvCalleeName.setText((calleeName != null && !calleeName.isEmpty()) ? calleeName : "Unknown");
        if (!calleeProfile.isEmpty()) {
            String imgUri = calleeProfile;
            RequestOptions options = new RequestOptions()
                    .fitCenter()
                    .placeholder(R.drawable.ic_empty_profile_holder_icon)
                    .error(R.drawable.ic_empty_profile_holder_icon);

            Glide.with(this)
                    .load(imgUri)
                    .apply(options)
                    .into(ivCalleeProfile);
        }
    }

    @Override
    public void showVideoCallStartView(boolean visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewVideoCallStart.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setUpRecyclerView() {
        joineeListAdapter = new JoineeListAdapter(this);
        joineeListAdapter.setJoineeListToggleUpdate(new JoineeListAdapter.JoineeListToggleUpdate() {
            @Override
            public void onListExpandContract(Boolean expand) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ibToggleJoineeList.setImageResource(expand ? R.drawable.ic_up_arrow :
                                R.drawable.ic_down_arrow);
                    }
                });

            }

            @Override
            public void onShowHideToggleIcon(Boolean show) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ibToggleJoineeList.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

            }

            @Override
            public void onShowHideJoineeList(Boolean show) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rlJoineeList.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

            }
        });
        rvJoinee = findViewById(R.id.rv_joinee);
        GridLayoutManager layoutManager = new GridLayoutManager(this, JoineeListAdapter.MAX_IN_A_ROW, GridLayoutManager.VERTICAL, false);
        rvJoinee.setLayoutManager(layoutManager);
        rvJoinee.setAdapter(joineeListAdapter);
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsGranted && credentialsReceived) {
            peerConnectionClient.startVideoSource();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onPermissionGranted() {
        if (credentialsReceived) {
            mRestChannel.initConnection(CLIENT);
            createLocalRender();
            peerConnectionParameters = new PeerConnectionParameters(false, 360, 480, 20, "H264", true, 0, "opus", false, false, false, false, false);
            peerConnectionClient = PeerConnectionClient.getInstance();
            peerConnectionClient.createPeerConnectionFactory(this, peerConnectionParameters, this);
            peerConnectionClient.setIceConnectionChangeEventNotifier(this);
            showVideoCallStartView(true);
        }
    }


    private void createLocalRender() {
        rootEglBase = EglBase.create();
        localRender.init(rootEglBase.getEglBaseContext(), null);
        localRender.setEnableHardwareScaler(true);
    }

    private boolean useCamera2() {
        return Camera2Enumerator.isSupported(this);
    }

    private boolean captureToTexture() {
        return true;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isBackFacing(deviceName)) {
                Log.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer = null;
        if (useCamera2()) {
            Log.d(TAG, "Creating capturer using camera2 API.");
            videoCapturer = createCameraCapturer(new Camera2Enumerator(this));
        } else {
            Log.d(TAG, "Creating capturer using camera1 API.");
            videoCapturer = createCameraCapturer(new Camera1Enumerator(captureToTexture()));
        }
        if (videoCapturer == null) {
            Log.e(TAG, "Failed to open camera");
            return null;
        }
        return videoCapturer;
    }


    private void offerPeerConnection(BigInteger handleId) {
        if (!callTerminated) {
            videoCapturer = createVideoCapturer();
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender, videoCapturer, handleId, "client");
            peerConnectionClient.createOffer(handleId);
        }

    }

    @Override
    public void onPublisherJoined(final BigInteger handleId) {

    }

    @Override
    public void startCreatingOffer(BigInteger handleId) {
        updateProgressMessage("Starting peer connection...");
        offerPeerConnection(handleId);
    }

    @Override
    public void onPublisherRemoteJsep(BigInteger handleId, JSONObject jsep) {
        SessionDescription.Type type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"));
        String sdp = jsep.optString("sdp");
        SessionDescription sessionDescription = new SessionDescription(type, sdp);
        peerConnectionClient.setRemoteDescription(handleId, sessionDescription);
    }

    @Override
    public void onActivePublishserNotFound() {
        Toast.makeText(this, "Active publishser not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep) {
        SessionDescription.Type type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"));
        String sdp = jsep.optString("sdp");
        SessionDescription sessionDescription = new SessionDescription(type, sdp);
        peerConnectionClient.subscriberHandleRemoteJsep(handleId, sessionDescription);
    }

    @Override
    public void onLeaving(BigInteger handleId) {

    }

    @Override
    public void onRoomCreated(final BigInteger roomNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvRoomNumber.setText("Room number: " + roomNumber);
            }
        });
    }

    @Override
    public BigInteger getRoomNumber() {
        return null;
    }

    @Override
    public void onLocalDescription(SessionDescription sdp, BigInteger handleId) {
        Log.e(TAG, sdp.type.toString());
        updateProgressMessage("Create offer...");
        mRestChannel.publisherCreateOffer(handleId, sdp);
    }

    @Override
    public void onRemoteDescription(SessionDescription sdp, BigInteger handleId) {
        Log.e(TAG, sdp.type.toString());
        mRestChannel.subscriberCreateAnswer(handleId, sdp);
    }

    @Override
    public void onIceCandidate(final IceCandidate candidate, final BigInteger handleId) {
        Log.e(TAG, "=========onIceCandidate========");
        if (candidate != null) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRestChannel.trickleCandidate(handleId, candidate);
                }
            }, 3000);

        } else {
            mRestChannel.trickleCandidateComplete(handleId);
        }
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] candidates) {

    }

    @Override
    public void onIceConnected() {

    }

    @Override
    public void onIceDisconnected() {

    }

    @Override
    public void onPeerConnectionClosed() {
    }

    @Override
    public void onPeerConnectionStatsReady(StatsReport[] reports) {

    }

    @Override
    public void onPeerConnectionError(String description) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ClientActivity.this, description, Toast.LENGTH_SHORT).show();
                showVideoCallStartView(false);
            }
        });
        terminateBroadCast();
    }

    @Override
    public void onRemoteRender(final JanusConnection connection) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void showProgressBar(String message) {
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSlowLink() {
        peerConnectionClient.changeCaptureFormat(480, 320, 20);
        ivSignalStrength.setImageResource(R.drawable.ic_bad_signal);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, timerDelay);
    }

    @Override
    public void updateProgressMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null)
                    progressDialog.setMessage("Starting video call...");
            }
        });

    }

    @Override
    public void restError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ClientActivity.this, message, Toast.LENGTH_SHORT).show();
                showVideoCallStartView(false);
                terminateBroadCast();
            }
        });
    }

    @Override
    public void streamUnpublished(BigInteger roomId, BigInteger publisherId) {

    }

    @Override
    public void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {
        /**
         * pass session, roomid and participant id to main activity
         */
        if (!callTerminated && mhostActivityCallback != null) {
            mhostActivityCallback.passJanusServerInfo(sessionId, roomId, participantId);
        }
    }

    @Override
    public void iceConnectionChangeEvent(String event) {
        updateProgressMessage("IceConnection event: " + event);
    }

    View.OnClickListener startDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showHideDrawView(true);
            //take screenshot

            localRender.addFrameListener(frameListener, 1);
            treeleafDrawPadViewLocal.addViewToDrawOver(imageViewCaptureImageLocal);
            //after taking screenshot

        }
    };

    View.OnClickListener discardDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GeneralUtil.hideKeyboard(v.getRootView(), ClientActivity.this);
            showHideDrawView(false);
        }
    };

    private void showHideDrawView(Boolean showDrawView) {
        layoutDraw.setVisibility(showDrawView ? View.VISIBLE : View.GONE);
        fabDiscardDraw.setVisibility(showDrawView ? View.VISIBLE : View.GONE);

        clCallSettings.setVisibility(showDrawView ? View.GONE : View.VISIBLE);
        clCallOtions.setVisibility(showDrawView ? View.GONE : View.VISIBLE);

        if (showDrawView) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(ALIGN_PARENT_LEFT, 0);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, TRUE);
            rlJoineeList.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
            rlJoineeList.setLayoutParams(params);
        }

    }

    View.OnClickListener videoToggleClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            videoOff = !videoOff;
            Toast.makeText(ClientActivity.this, videoOff ? "Video turned off. " : "Video turned on.", Toast.LENGTH_SHORT).show();
            peerConnectionClient.setVideoEnabled(videoOff);
            imageVideoToggle.setImageDrawable(videoOff ? getResources().getDrawable(R.drawable.ic_close_video, getApplicationContext().getTheme()) :
                    getResources().getDrawable(R.drawable.ic_open_video, getApplicationContext().getTheme()));
        }
    };

    View.OnClickListener audioToggleClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            audioOff = !audioOff;
            Toast.makeText(ClientActivity.this, audioOff ? "Audio muted. " : "Audio turned on.", Toast.LENGTH_SHORT).show();
            peerConnectionClient.setAudioEnabled(audioOff);
            imageAudioToggle.setImageDrawable(audioOff ? getResources().getDrawable(R.drawable.ic_mute_mic, getApplicationContext().getTheme()) :
                    getResources().getDrawable(R.drawable.ic_open_mic, getApplicationContext().getTheme()));

        }
    };

    View.OnClickListener screenShotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            justScreenShot = true;
            localRender.addFrameListener(frameListener, 1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ivScreenShotImage.setImageResource(android.R.color.transparent);
                }
            }, 1800);
        }
    };

    View.OnClickListener joineeListToggleListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //update image icon of toggle
            showFullList = !showFullList;
            joineeListAdapter.toggleJoineeList(showFullList);
        }
    };

    View.OnClickListener switchCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (peerConnectionClient != null)
                peerConnectionClient.switchCamera();
        }
    };

    View.OnClickListener endCallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageEndCall.setClickable(false);
            ivTerminateCall.setClickable(false);
            terminateBroadCast();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        // Don't stop the video when using screencapture to allow user to show other apps to the remote
        // end.
        if (peerConnectionClient != null) {
            peerConnectionClient.stopVideoSource();
        }
    }

    @Override
    protected void onDestroy() {
        Thread.setDefaultUncaughtExceptionHandler(null);
        if (rootEglBase != null)
            rootEglBase.release();
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    private void terminateBroadCast() {
        callTerminated = true;
        if (mhostActivityCallback != null)
            mhostActivityCallback.notifyHostHangUp();
        if (mRestChannel != null) {
            mRestChannel.publisherUnpublish();
            mRestChannel.detachPlugin();
            mRestChannel.destroySession();
            mRestChannel.clearPendingApiCalls();
            mRestChannel = null;
        }
        if (localRender != null)
            localRender.release();
        if (peerConnectionClient != null) {
            peerConnectionClient.close();
            peerConnectionClient = null;
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_message).setTitle(R.string.alert_dialog_title);
        builder.setMessage(R.string.alert_dialog_message)
                .setCancelable(false)
                .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        terminateBroadCast();
                    }
                })
                .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        alert.setOnShowListener(dialogInterface -> {
            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                    .setBackgroundColor(getResources().getColor(R.color.colorTransparent));
            alert.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                    .setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setBackgroundColor(getResources()
                    .getColor(R.color.colorTransparent));
            alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                    .getColor(R.color.colorPrimary));

        });
        //Setting the title manually
        alert.setTitle(R.string.alert_dialog_title);
        alert.show();
        alert.getWindow().getDecorView().getBackground()
                .setColorFilter(new LightingColorFilter(0xFF000000,
                        getResources().getColor(R.color.colorTransparent)));
    }

    public interface VideoCallListener {

        void onJoineeReceived(String joineedName, String joineedProfileUrl, String accountId);

        void onJoineeRemoved(String accountId);

        void onJanusCredentialsReceived(String baseUrl, String apiKey,
                                        String apiSecret, String calleeName, String calleeProfile);

        void onJanusCredentialsFailure();

        void onVideoViewReady();

    }

}