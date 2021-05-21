package com.treeleaf.januswebrtc;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.DrawMetadata;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.MetaDataUpdateListener;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Picture;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Position;
import com.treeleaf.freedrawingdemo.freedrawing.util.DrawPadUtil;
import com.treeleaf.freedrawingdemo.freedrawing.util.ForwardTouchesView;
import com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView;
import com.treeleaf.januswebrtc.JoineeListAdapter.Mode;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionEvents;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionParameters;
import com.treeleaf.januswebrtc.audio.AppRTCAudioManager;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;
import com.treeleaf.januswebrtc.rest.ApiClient;
import com.treeleaf.januswebrtc.utils.GlideBlurTransformation;

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
import org.webrtc.VideoRenderer;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.TRUE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.HIDE_THIS_VIEW;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ALL;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ONE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_THIS_VIEW;
import static com.treeleaf.januswebrtc.Const.CALLEE_NAME;
import static com.treeleaf.januswebrtc.Const.CALLEE_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.CALLER_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.CONSUMER_TYPE;
import static com.treeleaf.januswebrtc.Const.JANUS_API_KEY;
import static com.treeleaf.januswebrtc.Const.JANUS_API_SECRET;
import static com.treeleaf.januswebrtc.Const.JANUS_PARTICIPANT_ID;
import static com.treeleaf.januswebrtc.Const.JANUS_ROOM_NUMBER;
import static com.treeleaf.januswebrtc.Const.JANUS_URL;
import static com.treeleaf.januswebrtc.Const.JOINEE_LOCAL;
import static com.treeleaf.januswebrtc.Const.KEY_DIRECT_CALL_ACCEPT;
import static com.treeleaf.januswebrtc.Const.KEY_IS_INVITATION;
import static com.treeleaf.januswebrtc.Const.KEY_LAUNCHED_FROM_NOTIFICATION;
import static com.treeleaf.januswebrtc.Const.KEY_MULTIPLE_CALL;
import static com.treeleaf.januswebrtc.Const.KEY_RUNNING_ON;
import static com.treeleaf.januswebrtc.Const.LOCAL_JOINEE_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.LOCAL_JOINEE_NAME;
import static com.treeleaf.januswebrtc.Const.LOCAL_JOINEE_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.LOCAL_LOG;
import static com.treeleaf.januswebrtc.Const.PICTURE_EXCEED_MSG;
import static com.treeleaf.januswebrtc.Const.RMEOTE_LOG;
import static com.treeleaf.januswebrtc.Const.SERVER;
import static com.treeleaf.januswebrtc.Const.SERVICE_PROVIDER_TYPE;
import static com.treeleaf.januswebrtc.audio.AppRTCAudioManager.loudSpeakerOn;

public class ServerActivity extends PermissionHandlerActivity implements Callback.JanusRTCInterface, Callback.ApiCallback,
        PeerConnectionEvents, Callback.ConnectionEvents {
    private static final String TAG = ServerActivity.class.getSimpleName();
    private final String MAPPING_ISSUE = "MAPPING_ISSUE";
    private final String CALLING_MESSAGE = "is Calling...";
    private final String CALL_JOIN_MESSAGE = "is inviting you to join the call";
    public static final int SERVER_ACTIVITY_REQ = 5000;

    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer remoteRender;
    private VideoCapturer videoCapturer;
    private EglBase rootEglBase;
    private RestChannel mRestChannel;


    String roomNumber, participantId;
    private ProgressDialog progressDialog;

    private View layoutDraw;
    private ImageView fabStartDraw;
    private ImageView fabDiscardDraw, fabMinimizeDraw;
    private ImageView imageViewCaptureImage;
    private ImageView imageAudioToggle, imageSpeakerSwitch, imageScreenShot, imageInviteUser, imageEndCall, imageAcceptCall, ivScreenShotImage,
            ibToggleJoineeList, ivSignalStrength, imageVideoToggle, imageSwitchCamera;
    private TreeleafDrawPadView treeleafDrawPadView;
    private ForwardTouchesView forwardTouchesView;
    private LinearLayout llCallAcceptOptions;
    private LinearLayout clCallSettings, clCallOtions;
    private RelativeLayout rlScreenShotImage;
    private RelativeLayout rlJoineeList, rlServerRoot;
    private View viewVideoCallStart;
    private ImageView ivCalleeProfile, ivDeclineCall;
    private TextView tvCalleeName, tvConnecting, tvIsCalling,
            tvCallAccept, tvReconnecting, tvDeviceResolution;
    private LinearLayout clCallAcceptOptions;
    private EglRenderer.FrameListener frameListener;

    private Boolean videoOff = false;
    private Boolean audioOff = false;
    private Boolean justScreenShot = false;
    private Boolean showFullList = false;
    private boolean callTerminated = false;

    private String callerName, callerProfile, callerAccountId;

    private RecyclerView rvJoinee, rvPictureStack;
    private JoineeListAdapter joineeListAdapter;
    private PictureStackAdapter pictureStackAdapter;
    private static Callback.HostActivityCallback mhostActivityCallback;
    private static Callback.DrawCallBack mDrawCallback;
    private VideoCallListener videoCallListener;
    private Callback.DrawPadEventListener serverDrawingPadEventListener;

    private Handler handler;
    private Runnable runnable;
    private int timerDelay = 10000;
    private AppRTCAudioManager audioManager;
    private LinkedHashMap<String, DrawMetadata> drawMetadataLocal = new LinkedHashMap<>();
    private MetaDataUpdateListener metaDataUpdateListener;
    private CaptureDrawParam captureDrawParam;
    private int localDeviceHeight, localDeviceWidth;
    private String mLocalAccountId, mLocalAccountName;
    private Mode mode = Mode.VIDEO_STREAM;
    private Picture currentPicture;
    private Integer localPicturesCount = 0;
    private LinkedHashMap<String, Picture> mapPictures = new LinkedHashMap<>();
    public String runningOn = SERVICE_PROVIDER_TYPE;
    private boolean videoRendered = false;
    private String touchSessionId;
    private LinearLayout llMqttLog, llMqttLogRemote;
    private LinearLayout llLogLocal, llLogRemote;
    private Button btnClearMqttLogs, btnViewLocalLog, btnViewRemoteLog;
    private ScrollView svMqttLog, svMqttLogRemote;
    private ImageView ivCallProfileBlur;
    private Float prevX, prevY;
    private String logView = LOCAL_LOG;
    private MediaPlayer mediaPlayer;
    private Boolean directCallAccept;
    private Boolean launchedFromNotification;
    private Boolean isCallMultiple;
    private LinearLayout llCallCancel;
    private ImageView ivCancelCall;
    private TextView tvCallTimer, tvCurrentDrawer;
    private int seconds = 0;
    private Handler callTimerHandler;
    private Runnable callTimerRunnable;
    private String baseUrl;
    private String apiKey;
    private String apiSecret;
    private boolean isInvitation;
    private String mCallerContext;

    public static void launch(Context context, String janusServerUrl, String apiKey, String apiSecret,
                              String roomNumber, String participantId, String calleeName, String callProfileUrl) {
        Intent intent = new Intent(context, ServerActivity.class);
        intent.putExtra(JANUS_URL, janusServerUrl);
        intent.putExtra(JANUS_API_KEY, apiKey);
        intent.putExtra(JANUS_API_SECRET, apiSecret);
        intent.putExtra(JANUS_ROOM_NUMBER, roomNumber);
        intent.putExtra(JANUS_PARTICIPANT_ID, participantId);
        intent.putExtra(CALLEE_NAME, calleeName);
        intent.putExtra(CALLEE_PROFILE_URL, callProfileUrl);
        context.startActivity(intent);
    }

    public static void launch(Context context, String janusServerUrl, String apiKey, String apiSecret,
                              String roomNumber, String participantId, Callback.HostActivityCallback hostActivityCallBack,
                              Callback.DrawCallBack drawCallBack, String calleeName, String callProfileUrl, String calleeAccountId,
                              String runningOn, Boolean isCallMultiple, Boolean isInvitation) {
        mhostActivityCallback = hostActivityCallBack;
        mDrawCallback = drawCallBack;
        Intent intent = new Intent(context, ServerActivity.class);
        intent.putExtra(JANUS_URL, janusServerUrl);
        intent.putExtra(JANUS_API_KEY, apiKey);
        intent.putExtra(JANUS_API_SECRET, apiSecret);
        intent.putExtra(JANUS_ROOM_NUMBER, roomNumber);
        intent.putExtra(JANUS_PARTICIPANT_ID, participantId);
        intent.putExtra(CALLEE_NAME, calleeName);
        intent.putExtra(CALLEE_PROFILE_URL, callProfileUrl);
        intent.putExtra(CALLER_ACCOUNT_ID, calleeAccountId);
        intent.putExtra(KEY_RUNNING_ON, runningOn);
        intent.putExtra(KEY_MULTIPLE_CALL, isCallMultiple);
        intent.putExtra(KEY_IS_INVITATION, isInvitation);
        context.startActivity(intent);
    }

    public static void launchViaNotification(Activity context, Callback.HostActivityCallback hostActivityCallBack,
                                             Callback.DrawCallBack drawCallBack, String callerName, String callerProfileUrl, String callerAccountId,
                                             String runningOn, Boolean direcCallAccept, Boolean launchedFromNotification, String localAccountName,
                                             String localAccountId, String localAccountProfile, Boolean isCallMultiple, Boolean isInvitation) {
        mhostActivityCallback = hostActivityCallBack;
        mDrawCallback = drawCallBack;
        Intent intent = new Intent(context, ServerActivity.class);
        intent.putExtra(CALLEE_NAME, callerName);
        intent.putExtra(CALLEE_PROFILE_URL, callerProfileUrl);
        intent.putExtra(CALLER_ACCOUNT_ID, callerAccountId);

        intent.putExtra(LOCAL_JOINEE_NAME, localAccountName);
        intent.putExtra(LOCAL_JOINEE_PROFILE_URL, localAccountProfile);
        intent.putExtra(LOCAL_JOINEE_ACCOUNT_ID, localAccountId);

        intent.putExtra(KEY_RUNNING_ON, runningOn);
        intent.putExtra(KEY_MULTIPLE_CALL, isCallMultiple);
        intent.putExtra(KEY_IS_INVITATION, isInvitation);
        intent.putExtra(KEY_DIRECT_CALL_ACCEPT, direcCallAccept);
        intent.putExtra(KEY_LAUNCHED_FROM_NOTIFICATION, launchedFromNotification);
        context.startActivityForResult(intent, SERVER_ACTIVITY_REQ);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        openActivityInLockedScreen();

        layoutDraw = findViewById(R.id.layout_draw);
        fabStartDraw = findViewById(R.id.fab_start_draw);
        fabDiscardDraw = findViewById(R.id.fab_discard_draw);
        fabMinimizeDraw = findViewById(R.id.fab_minimize_draw);
        imageViewCaptureImage = findViewById(R.id.iv_captured_image);
        treeleafDrawPadView = findViewById(R.id.treeleaf_draw_pad);
        remoteRender = findViewById(R.id.remote_video_view);
        clCallSettings = findViewById(R.id.cl_call_setting);
        clCallOtions = findViewById(R.id.cl_call_options);
        imageVideoToggle = findViewById(R.id.image_video);
        llCallAcceptOptions = findViewById(R.id.cl_call_accept_options);
        imageAudioToggle = findViewById(R.id.image_mic);
        imageSpeakerSwitch = findViewById(R.id.image_speaker_switch);
        imageScreenShot = findViewById(R.id.image_screenshot);
        imageInviteUser = findViewById(R.id.image_invite_users);
        imageSwitchCamera = findViewById(R.id.image_switch_camera);
        imageEndCall = findViewById(R.id.image_end_call);
        ivCancelCall = findViewById(R.id.iv_cancel_call);
        ivDeclineCall = findViewById(R.id.iv_decline_call);
        imageAcceptCall = findViewById(R.id.iv_accept_call);
        ivSignalStrength = findViewById(R.id.iv_signal_strength);
        rlScreenShotImage = findViewById(R.id.rl_screenshot_image);
        ivScreenShotImage = findViewById(R.id.iv_screenshot_image);
        rlServerRoot = findViewById(R.id.rl_server_root);
        rlJoineeList = findViewById(R.id.rl_joinee_list);
        ibToggleJoineeList = findViewById(R.id.ib_toggle_joinee_list);
        viewVideoCallStart = findViewById(R.id.view_video_call_start);
        tvCalleeName = findViewById(R.id.tv_callee_name);
        tvConnecting = findViewById(R.id.tv_connecting);
        tvIsCalling = findViewById(R.id.tv_is_calling);
        tvCallAccept = findViewById(R.id.tv_call_accept);
        tvReconnecting = findViewById(R.id.tv_reconnecting);
        ivCalleeProfile = findViewById(R.id.iv_callee_profile);

        llMqttLog = findViewById(R.id.ll_mqtt_log);
        llLogLocal = findViewById(R.id.ll_log_local);
        llLogRemote = findViewById(R.id.ll_log_remote);
        llMqttLogRemote = findViewById(R.id.ll_mqtt_log_remote);
        btnClearMqttLogs = findViewById(R.id.btn_clear_mqtt_logs);
        btnViewLocalLog = findViewById(R.id.btn_view_local_log);
        btnViewRemoteLog = findViewById(R.id.btn_view_remote_log);
        svMqttLog = findViewById(R.id.sv_mqtt_log);
        svMqttLogRemote = findViewById(R.id.sv_mqtt_log_remote);
        ivCallProfileBlur = findViewById(R.id.iv_call_profile_blur);
        tvDeviceResolution = findViewById(R.id.tv_device_resolution);
        clCallAcceptOptions = findViewById(R.id.cl_call_accept_options);
        llCallCancel = findViewById(R.id.ll_call_cancel);

        tvCallTimer = findViewById(R.id.tv_call_timer);
        tvCurrentDrawer = findViewById(R.id.tv_current_drawer);

        imageVideoToggle.setOnClickListener(videoToggleClickListener);
        imageAudioToggle.setOnClickListener(audioToggleClickListener);
        imageSpeakerSwitch.setOnClickListener(speakerSwitchListener);
        imageScreenShot.setOnClickListener(screenShotClickListener);
        imageInviteUser.setOnClickListener(inviteUserClickListener);
        imageSwitchCamera.setOnClickListener(switchCameraClickListener);
        imageEndCall.setOnClickListener(endCallClickListener);
        ivCancelCall.setOnClickListener(endCallClickListener);
        ivDeclineCall.setOnClickListener(declineCallClickListener);
        imageAcceptCall.setOnClickListener(acceptCallClickListener);
        ibToggleJoineeList.setOnClickListener(joineeListToggleListener);
        btnClearMqttLogs.setOnClickListener(clearMqttLogsClickListener);
        btnViewLocalLog.setOnClickListener(viewLocalLogClickListener);
        btnViewRemoteLog.setOnClickListener(viewRemoteLogClickListener);


        btnViewLocalLog.setBackgroundColor(getResources().getColor(R.color.color_green));
        btnViewRemoteLog.setBackgroundColor(getResources().getColor(R.color.color_red));

        forwardTouchesView = findViewById(R.id.forward_touch);

        forwardTouchesView.setForwardTo(treeleafDrawPadView);
        fabStartDraw.setOnClickListener(startDrawClickListener);
        fabDiscardDraw.setOnClickListener(discardDrawClickListener);
        fabMinimizeDraw.setOnClickListener(minimizeDrawClickListener);

        setUpProgressDialog();

        frameListener = new EglRenderer.FrameListener() {
            @Override
            public void onFrame(Bitmap bitmap) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (justScreenShot) {
                            ivScreenShotImage.setImageBitmap(bitmap);
                            treeleafDrawPadView.takeAndSaveScreenShot(rlScreenShotImage);
                            justScreenShot = false;
                        } else {
                            if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                                currentPicture = createNewPicture(mLocalAccountId, DrawPadUtil.generateRandomId(), false,
                                        false, true, bitmap);
                                localPicturesCount++;
                                Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                treeleafDrawPadView.placeCurrentDrawingImage(bitmap);
                                getDrawingViewResolution();
                                treeleafDrawPadView.setOnScreenPicture(currentPicture);
                                treeleafDrawPadView.createNewDrawCard(bitmap, ServerActivity.this,
                                        mLocalAccountId, mLocalAccountName, currentPicture.getPictureId(), currentPicture.getPictureIndex(), SHOW_THIS_VIEW);
                                drawMetadataLocal.put(currentPicture.getPictureId(), new DrawMetadata());
                                treeleafDrawPadView.setLocalOnScreenDrawCard(currentPicture.getPictureId());
                                int localParticipantColor = treeleafDrawPadView.setRandomColor();
                                joineeListAdapter.resetDrawColorOfParticipants(mLocalAccountId, localParticipantColor);
                                joineeListAdapter.checkIfAllJoineesOnSamePicture(currentPicture);
                                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {//TODO: uncomment this later
                                    mDrawCallback.onCollabInvite(null, currentPicture.getPictureId(),
                                            currentPicture.getBitmap());
                                    //used separate for loop because there has to be certain
                                    //time delay between collab invite and maximize request.
                                    mDrawCallback.onMaximizeDrawing(currentPicture.getPictureId());
                                }
                            } else {
                                Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        /**
                         * remove frame listener as soon as you get the captured image.
                         */
                        remoteRender.removeFrameListener(frameListener);
                    }
                });
            }

        };

        videoCallListener = new VideoCallListener() {

            @Override
            public void checkCallHandledOnAnotherDevice() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!Const.CallStatus.isCallTakingPlace) {
                            terminateBroadCast();
                        }
                    }
                });
            }

            @Override
            public void terminateCallReceive() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        terminateBroadCast();
                    }
                });
            }

            @Override
            public void onCallerDetailFetched(String url, String key, String secret, String roomId, String participantId_) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        baseUrl = url;
                        apiKey = key;
                        apiSecret = secret;
                        roomNumber = roomId;
                        participantId = participantId_;

                        mRestChannel = new RestChannel(ServerActivity.this, baseUrl, apiKey, apiSecret);
                        mRestChannel.setDelegate(ServerActivity.this);
                        mRestChannel.setApiCallback(ServerActivity.this);

                        if (directCallAccept) {
                            //call accept from notification, accept direct
                            acceptCall();
                        }
                    }
                });

            }

            @Override
            public void onMqttReponseArrived(String responseType, boolean isLocalResponse) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isLocalResponse && logView.equals(LOCAL_LOG)) {
                            TextView textView = new TextView(ServerActivity.this);
                            textView.setText(responseType);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            llMqttLog.addView(textView, llMqttLog.getChildCount());
                            if (llMqttLog.getChildCount() > 20) {
                                llMqttLog.removeViewAt(0);
                            }
                            svMqttLog.post(new Runnable() {
                                @Override
                                public void run() {
                                    svMqttLog.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        } else if (!isLocalResponse && logView.equals(RMEOTE_LOG)) {
                            TextView textView = new TextView(ServerActivity.this);
                            textView.setText(responseType);
                            textView.setTextColor(Color.WHITE);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                            llMqttLogRemote.addView(textView, llMqttLogRemote.getChildCount());
                            if (llMqttLogRemote.getChildCount() > 20) {
                                llMqttLogRemote.removeViewAt(0);
                            }
                            svMqttLogRemote.post(new Runnable() {
                                @Override
                                public void run() {
                                    svMqttLogRemote.fullScroll(ScrollView.FOCUS_DOWN);
                                }
                            });
                        }


                    }
                });

            }

            @Override
            public void onMqttConnectionChanged(String status) {
//                tvReconnecting.setVisibility(status.equals(MQTT_DISCONNECTED) ? VISIBLE : View.GONE);
            }

            @Override
            public void onJoineeReceived(String joineeName, String joineedProfileUrl, String accountId, String joineeType) {
                //add new joinee information in view
                Log.d(TAG, "onjoineereceived");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joineeListAdapter.addNewJoinee(new Joinee(joineeName, joineedProfileUrl, accountId, joineeType.equals(JOINEE_LOCAL)), showFullList);
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
            public void onVideoViewReady() {
            }

            @Override
            public void onHostTerminateCall() {
                terminateBroadCast();
            }

            @Override
            public void onParticipantLeft() {
                if (runningOn.equals(SERVICE_PROVIDER_TYPE)) {
                    terminateBroadCast();
                }
            }

            @Override
            public void onCallDeclined() {

            }

        };

        serverDrawingPadEventListener = new Callback.DrawPadEventListener() {

            @Override
            public void onDrawDisplayCapturedImage(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode = Mode.IMAGE_DRAW;
                        joineeListAdapter.notifyDataSetChanged();
                        showHideDrawView(true);
                    }
                });
            }

            @Override
            public void onDrawDiscard(String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPicture != null) {
                            joineeListAdapter.updateJoineeDrawStat(accountId, Joinee.JoineeDrawState.CLOSED, imageId,
                                    mode.equals(Mode.IMAGE_DRAW) && currentPicture.getPictureId().equals(imageId));
                        }
                    }
                });
            }

            @Override
            public void onDrawTouchDown(String accountId, String imageId, String fullName) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteTouchDown(accountId, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            joineeListAdapter.highlightCurrentDrawer(accountId, true,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());

                        if (mode.equals(Mode.IMAGE_DRAW)) {
                            if (!currentPicture.getPictureId().equals(imageId)) {

                                if (!mapPictures.get(imageId).isClosed()) {
                                    onDrawTakesPlaceOnOldImage(imageId);
                                }
                            }

                        } else if (mode.equals(Mode.VIDEO_STREAM)) {
                            if (!mapPictures.get(imageId).isClosed()) {
                                onDrawTakesPlaceOnOldImage(imageId);
                                showSomeOneDrawingText(accountId, imageId, fullName, "drawing");
                            }
                        }

                    }
                });
            }

            @Override
            public void onDrawTouchMove(String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteTouchMove(accountId, imageId);
                    }
                });
            }

            @Override
            public void onDrawTouchUp(String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteTouchUp(accountId, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            joineeListAdapter.highlightCurrentDrawer(accountId, false,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());

                    }
                });
            }

            @Override
            public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteAddEditText(x, y, editTextFieldId,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor(), accountId, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            highlightDrawerForTextEdit(accountId,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());
                    }
                });
            }

            @Override
            public void onDrawReceiveNewTextChange(String text, String id, String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteChangedEditText(text, id, accountId, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            highlightDrawerForTextEdit(accountId,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());
                    }
                });
            }

            @Override
            public void onDrawReceiveEdiTextRemove(String editTextId, String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteRemoveEditText(editTextId, accountId, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            highlightDrawerForTextEdit(accountId,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());
                    }
                });
            }

            @Override
            public void onDrawPointerClicked(float x, float y, String accountId, String imageId, String fullName) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemotePointerClicked(x, y, accountId, fullName, imageId);
                        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null && imageId.equals(currentPicture.getPictureId()))
                            highlightDrawerForTextEdit(accountId,
                                    treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata().getTextColor());
                        if (mode.equals(Mode.VIDEO_STREAM)) {
                            if (!mapPictures.get(imageId).isClosed()) {
                                onDrawTakesPlaceOnOldImage(imageId);
                                showSomeOneDrawingText(accountId, imageId, fullName, "pointing");
                            }
                        }
                    }
                });
            }

            /**
             * this callback has be called every time draw coordinates is changed
             * and should be called everytime before onDrawTouchDown and onDrawTouchMove
             * @param x
             * @param y
             * @param imageId
             */
            @Override
            public void onDrawNewDrawCoordinatesReceived(Float x, Float y, String accountId, String imageId) {
                if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                    Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                    return;
                }
                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId).getDrawMetadata()
                        .setCurrentDrawPosition(new Position(x, y));
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId, String imageId) {
                if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                    Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                    return;
                }
                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId)
                        .setDrawMetadata(VideoCallUtil.getDrawMetaData(captureDrawParam));
            }

            @Override
            public void onDrawCanvasCleared(String accountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId, imageId) == null) {
                            Log.d(MAPPING_ISSUE, "Remote image has not received!!!");
                            return;
                        }
                        treeleafDrawPadView.onRemoteClearCanvas(accountId, imageId);
                    }
                });
            }

            @Override
            public void onDrawCollabInvite(String fromAccountId, String toAccountId, String imageId, byte[] convertedBytes) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mode.equals(Mode.IMAGE_DRAW)) {
                            //add image in stack
                            //check if its new or old picture

                            if (!currentPicture.getPictureId().equals(imageId)) {
                                if (isOldPicture(imageId)) {
                                    //show notification to old picture in stack
                                    if (mapPictures.get(imageId).isClosed()) {
                                        if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                                            Bitmap receivedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
                                            if (receivedBitmap == null) {
                                                Toast.makeText(ServerActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            Bitmap downSampledBitmap = VideoCallUtil.downSampleBitmap(receivedBitmap);
                                            Picture picture = createNewPicture(fromAccountId, imageId, true,
                                                    true, false, downSampledBitmap);
                                            localPicturesCount++;
                                            Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                            addPictureToStack(picture);
                                        } else {
                                            mDrawCallback.onMaxDrawingExceed();
                                            Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                    onCollabInviteOnOldImage(imageId);
                                    joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED, imageId, false);
                                    if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                        mDrawCallback.onMinimizeDrawing(imageId);
                                    }
                                } else if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                                    //add image to stack
                                    Bitmap receivedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
                                    if (receivedBitmap == null) {
                                        Toast.makeText(ServerActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Bitmap downSampledBitmap = VideoCallUtil.downSampleBitmap(receivedBitmap);
                                    Picture picture = createNewPicture(fromAccountId, imageId, true,
                                            true, false, downSampledBitmap);
                                    localPicturesCount++;
                                    Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                    addPictureToStack(picture);
                                    treeleafDrawPadView.createNewDrawCard(downSampledBitmap, ServerActivity.this,
                                            mLocalAccountId, mLocalAccountName, imageId, picture.getPictureIndex(), TreeleafDrawPadView.HIDE_THIS_VIEW);
                                    drawMetadataLocal.put(imageId, new DrawMetadata());
                                    treeleafDrawPadView.addNewRemoteDrawer(ServerActivity.this, fromAccountId, imageId, TreeleafDrawPadView.HIDE_THIS_VIEW);
                                    joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED, imageId, false);
                                    if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                        mDrawCallback.onMinimizeDrawing(imageId);
                                    }
                                } else {
                                    mDrawCallback.onMaxDrawingExceed();
                                    Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                                }
                            }


                        } else if (mode.equals(Mode.VIDEO_STREAM)) {
                            //check if this image is new or old.
                            //if image is new show direct if old, add to stack

                            if (isOldPicture(imageId)) {
                                //add to stack (show notification to old picture in stack)
                                if (mapPictures.get(imageId).isClosed()) {
                                    if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                                        Bitmap receivedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
                                        if (receivedBitmap == null) {
                                            Toast.makeText(ServerActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        Bitmap downSampledBitmap = VideoCallUtil.downSampleBitmap(receivedBitmap);
                                        Picture picture = createNewPicture(fromAccountId, imageId, true,
                                                true, false, downSampledBitmap);
                                        localPicturesCount++;
                                        Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                        addPictureToStack(picture);
                                    } else {
                                        mDrawCallback.onMaxDrawingExceed();
                                        Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                                onCollabInviteOnOldImage(imageId);
                                joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED, imageId, false);
                                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                    mDrawCallback.onMinimizeDrawing(imageId);
                                }
                            } else if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                                // its new picture, show direct

                                onDrawDisplayCapturedImage(fromAccountId);
                                Bitmap receivedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
                                if (receivedBitmap == null) {
                                    Toast.makeText(ServerActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Bitmap downSampledBitmap = VideoCallUtil.downSampleBitmap(receivedBitmap);
                                Picture picture = createNewPicture(fromAccountId, imageId, true,
                                        true, false, downSampledBitmap);
                                localPicturesCount++;
                                Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                currentPicture = picture;

                                treeleafDrawPadView.placeCurrentDrawingImage(downSampledBitmap);
                                getDrawingViewResolution();
                                treeleafDrawPadView.setOnScreenPicture(picture);
                                treeleafDrawPadView.createNewDrawCard(downSampledBitmap, ServerActivity.this,
                                        mLocalAccountId, mLocalAccountName, imageId, picture.getPictureIndex(), TreeleafDrawPadView.SHOW_THIS_VIEW);
                                drawMetadataLocal.put(imageId, new DrawMetadata());
                                treeleafDrawPadView.addNewRemoteDrawer(ServerActivity.this, fromAccountId, imageId, TreeleafDrawPadView.SHOW_THIS_VIEW);
                                treeleafDrawPadView.setLocalOnScreenDrawCard(picture.getPictureId());
                                int localParticipantColor = treeleafDrawPadView.setRandomColor();
                                joineeListAdapter.resetDrawColorOfParticipants(mLocalAccountId, localParticipantColor);
                                //update draw stat of joinee who sent this invite
                                joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED,
                                        imageId, mode.equals(Mode.IMAGE_DRAW) && currentPicture.getPictureId().equals(imageId));

                                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                    mDrawCallback.onMaximizeDrawing(picture.getPictureId());
                                }

                            } else {
                                mDrawCallback.onMaxDrawingExceed();
                                Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onDrawMaximize(String fromAccountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPicture != null) {
                            treeleafDrawPadView.addNewRemoteDrawer(ServerActivity.this, fromAccountId,
                                    imageId, currentPicture.getPictureId().equals(imageId) ? SHOW_THIS_VIEW : HIDE_THIS_VIEW);
                            joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED,
                                    imageId, mode.equals(Mode.IMAGE_DRAW) && currentPicture.getPictureId().equals(imageId));
                            joineeListAdapter.checkIfAllJoineesOnSamePicture(currentPicture);
                        }
                    }
                });
            }

            @Override
            public void onDrawMinimize(String fromAccountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPicture != null) {
                            joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MINIMIZED,
                                    imageId, mode.equals(Mode.IMAGE_DRAW) && currentPicture.getPictureId().equals(imageId));
                            joineeListAdapter.checkIfAllJoineesOnSamePicture(currentPicture);
                        }
                    }
                });
            }

            @Override
            public void onDrawClose(String fromAccountId, String imageId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPicture != null) {
                            joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.CLOSED,
                                    imageId, mode.equals(Mode.IMAGE_DRAW) && currentPicture.getPictureId().equals(imageId));
                        }
                    }
                });
            }

            @Override
            public void onDrawMaxDrawingExceed(String fromAccountId, String fromAccountName) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPicture != null) {
                            Toast.makeText(ServerActivity.this,
                                    "Collab invite failed. " + fromAccountName + " exceeded maximum number of drawings.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }


        };


        metaDataUpdateListener = new MetaDataUpdateListener() {
            @Override
            public void setDrawMode(TreeleafDrawPadView.DrawMode drawMode) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setDrawMode(drawMode);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                }
            }

            @Override
            public void setBrushWidth(Float width) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushWidth(width);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setBrushOpacity(Integer opacity) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushOpacity(opacity);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setBrushColor(Integer color) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushColor(color);
                joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, false, color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setTextColor(Integer color) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setTextColor(color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void onClearCanvasPressed(Boolean pressed) {
                drawMetadataLocal.get(currentPicture.getPictureId()).setClearCanvas(pressed);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onDrawCanvasCleared(currentPicture.getPictureId());
                }
            }

            @Override
            public void onStartDrawing(float x, float y) {
                Log.d(TAG, "onStartDrawing: " + x + " " + y);
                drawMetadataLocal.get(currentPicture.getPictureId()).setCurrentDrawPosition(new Position(x, y));
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    touchSessionId = DrawPadUtil.generateRandomId();
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                    captureDrawParam.setXCoordinate(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth));
                    captureDrawParam.setYCoordinate(VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight));
                    mDrawCallback.onStartDraw(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth),
                            VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight), captureDrawParam,
                            currentPicture.getPictureId(), touchSessionId);
                    prevX = captureDrawParam.getXCoordinate();
                    prevY = captureDrawParam.getYCoordinate();
                }
                if (mLocalAccountId != null)
                    joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, true, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onEndDrawing() {
                Log.d(TAG, "onEndDrawing: ");
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent() && touchSessionId != null) {
                    mDrawCallback.onClientTouchUp(currentPicture.getPictureId(), touchSessionId);
                    prevX = 0f;
                    prevY = 0f;
                }
                if (mLocalAccountId != null)
                    joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, false, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onReceiveNewDrawingPosition(float x, float y) {
                Log.d(TAG, "onReceiveNewDrawingPosition: " + x + " " + y);
                x = VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth);
                y = VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight);
                drawMetadataLocal.get(currentPicture.getPictureId()).setCurrentDrawPosition(new Position(x, y));
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                    mDrawCallback.onClientTouchMove(captureDrawParam, currentPicture.getPictureId(), prevX, prevY, touchSessionId);
                    prevX = x;
                    prevY = y;
                }

            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                    mDrawCallback.onReceiveNewTextField(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth),
                            VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight), editTextFieldId,
                            currentPicture.getPictureId(), captureDrawParam);
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId,
                            drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent())
                    mDrawCallback.onReceiveNewTextChange(text, id, currentPicture.getPictureId());
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent())
                    mDrawCallback.onReceiveEdiTextRemove(editTextId, currentPicture.getPictureId());
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onPointerClicked(float x, float y) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onPointerClicked(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth),
                            VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight), currentPicture.getPictureId());
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

        };
        treeleafDrawPadView.setMetaDataUpdateListener(metaDataUpdateListener);

        setUpJoineeRecyclerView();
        setUpPictureStackRecyclerView();
        setUpNetworkStrengthHandler();
        setUpCallTimer();

        permissionsGranted = checkPermission();
        if (permissionsGranted) {
            onPermissionGranted();
        }

        //these three info comes from notification payload
        callerName = getIntent().getStringExtra(CALLEE_NAME);
        callerProfile = getIntent().getStringExtra(CALLEE_PROFILE_URL);
        callerAccountId = getIntent().getStringExtra(CALLER_ACCOUNT_ID);

        runningOn = (getIntent().getStringExtra(KEY_RUNNING_ON) == null) ? runningOn : getIntent().getStringExtra(KEY_RUNNING_ON);
        isCallMultiple = getIntent().getBooleanExtra(KEY_MULTIPLE_CALL, true);
        isInvitation = getIntent().getBooleanExtra(KEY_IS_INVITATION, false);


        launchedFromNotification = getIntent().getBooleanExtra(KEY_LAUNCHED_FROM_NOTIFICATION, false);


        if (mhostActivityCallback != null) {
            mhostActivityCallback.passJoineeReceivedCallback(videoCallListener);
            mhostActivityCallback.passDrawPadEventListenerCallback(serverDrawingPadEventListener);
            mLocalAccountId = mhostActivityCallback.getLocalAccountId();
            mLocalAccountName = mhostActivityCallback.getLocalAccountName();
            mCallerContext = mhostActivityCallback.getCallerContext();
            mhostActivityCallback.specifyRole(RestChannel.Role.SERVER);
        }

        checkIfViewNeedstoHide(runningOn, isCallMultiple);
        loadCallNameAndProfileIcon(callerName, callerProfile);
        isCallInvitation(isInvitation);

        showVideoCallStartView(true);
        startAudioRinging();
        checkIfCallIsTaken();

        if (launchedFromNotification) {
            directCallAccept = getIntent().getBooleanExtra(KEY_DIRECT_CALL_ACCEPT, false);
            if (directCallAccept)
                handleIncomingCallUi();

            if (mhostActivityCallback != null) {
                mhostActivityCallback.fetchCallerAndJanusCredentials();
            }

            /**
             * show call joined users to view
             */
            String localJoineeName = getIntent().getStringExtra(LOCAL_JOINEE_NAME);
            String localJoineeAccountId = getIntent().getStringExtra(LOCAL_JOINEE_ACCOUNT_ID);
            String localJoineeProfileUrl = getIntent().getStringExtra(LOCAL_JOINEE_PROFILE_URL);

            joineeListAdapter.addNewJoinee(new Joinee(callerName, callerProfile, callerAccountId, false), showFullList);//local joinee
            joineeListAdapter.addNewJoinee(new Joinee(localJoineeName, localJoineeProfileUrl, localJoineeAccountId, true), showFullList);//remote joinee
            setCallingScreenOn();
            return;

        } else {

            baseUrl = getIntent().getStringExtra(JANUS_URL);
            apiKey = getIntent().getStringExtra(JANUS_API_KEY);
            apiSecret = getIntent().getStringExtra(JANUS_API_SECRET);
            roomNumber = getIntent().getStringExtra(JANUS_ROOM_NUMBER);
            participantId = getIntent().getStringExtra(JANUS_PARTICIPANT_ID);
        }

        mRestChannel = new RestChannel(this, baseUrl, apiKey, apiSecret);
        mRestChannel.setDelegate(this);
        mRestChannel.setApiCallback(this);

        setCallingScreenOn();
    }

    private void setCallingScreenOn() {
        Const.CallStatus.isCallingScreenOn = true;
        Const.CallStatus.isCallTakingPlace = false;
    }

    private void showSomeOneDrawingText(String accountId, String imageId, String fullName, String action) {
        tvCurrentDrawer.setVisibility(VISIBLE);
        tvCurrentDrawer.setText(fullName.isEmpty() ? "Someone is " + action : fullName + " is " + action);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvCurrentDrawer.setVisibility(GONE);
                tvCurrentDrawer.setText("");
            }
        }, 1000);
    }

    private void setUpCallTimer() {
        callTimerHandler = new Handler();
        callTimerRunnable = new Runnable() {
            @Override

            public void run() {
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;
                // Format the seconds into hours, minutes,
                // and seconds.
                String time
                        = String
                        .format(Locale.getDefault(),
                                "%02d:%02d:%02d", hours,
                                minutes, secs);

                // Set the text view text.
                tvCallTimer.setText(time);
                seconds++;
                // Post the code again
                // with a delay of 1 second.
                handler.postDelayed(this, 1000);
            }
        };
    }

    private void openActivityInLockedScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
            if (keyguardManager != null)
                keyguardManager.requestDismissKeyguard(this, null);
        } else {
            getWindow().addFlags(FLAG_DISMISS_KEYGUARD |
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    private void startAudioRinging() {
        try {
            mediaPlayer = MediaPlayer.create(ServerActivity.this, R.raw.call_incoming_new);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }

    }

    private void getDrawingViewResolution() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calculateTreeleafDrawPadViewResolution();
                mhostActivityCallback.sendDrawingViewResolution(localDeviceWidth, localDeviceHeight);
            }
        }, 500);
    }

    private void calculateTreeleafDrawPadViewResolution() {
        localDeviceWidth = treeleafDrawPadView.getWidth();
        localDeviceHeight = treeleafDrawPadView.getHeight();
        tvDeviceResolution.setText(new StringBuilder(localDeviceWidth + " X " + localDeviceHeight).toString());
    }

    private void onCollabInviteOnOldImage(String imageId) {
        pictureStackAdapter.onCollabInvite(imageId);
    }

    private void onDrawTakesPlaceOnOldImage(String imageId) {
//        pictureStackAdapter.onDrawOnMinimize(imageId);
        pictureStackAdapter.onCollabInvite(imageId);
    }

    private Picture createNewPicture(String fromAccountId, String imageId, boolean isRequestedForCollab,
                                     boolean isNewArrival, boolean isOnScreen, Bitmap bitmap) {

        Picture myPicture = new Picture(100);
        myPicture.setPictureId(imageId);
        myPicture.setBitmap(bitmap);
        myPicture.setPictureOwnerId(fromAccountId);
        myPicture.setRequestedForCollab(isRequestedForCollab);
        myPicture.setNewArrival(isNewArrival);
        myPicture.setOnScreen(isOnScreen);
        mapPictures.put(myPicture.getPictureId(), myPicture);
        return myPicture;
    }

    public boolean isOldPicture(String imageId) {
        return mapPictures.containsKey(imageId);
    }

    private void addPictureToStack(Picture myPicture) {
        pictureStackAdapter.addNewPicture(myPicture);
    }

    private void setUpPictureStackRecyclerView() {
        pictureStackAdapter = new PictureStackAdapter(this);
        pictureStackAdapter.setModeListener(() -> mode);
        pictureStackAdapter.setOnItemClickListener(new PictureStackAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, View v, Picture picture) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mode.equals(Mode.VIDEO_STREAM)) {

                            VideoCallUtil.materialContainerTransformVisibilityNoHide(rvPictureStack
                                            .findViewHolderForAdapterPosition(position)
                                            .itemView,
                                    layoutDraw,
                                    rlServerRoot);

                            currentPicture = VideoCallUtil.updatePictureContents(pictureStackAdapter.getPictureFromPosition(position));
                            new Handler().postDelayed(() -> pictureStackAdapter.removePicture(position), 0);

                        } else {
                            /**
                             * load the clicked image on the drawing screen and then
                             * update image stack list
                             *
                             */

                            //send minimize request to current image.
                            if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                mDrawCallback.onMinimizeDrawing(currentPicture.getPictureId());
                            }

                            Picture picture = pictureStackAdapter.getPictureFromPosition(position);
                            pictureStackAdapter.updatePicture(position, currentPicture);
                            currentPicture = VideoCallUtil.updatePictureContents(picture);
                        }

                        for (Joinee joinee : joineeListAdapter.fetchAllJoinee()) {
                            if (!joinee.isSelfAccount()) {
                                //TODO: use better way to check for null
                                if (treeleafDrawPadView.getRemoteDrawerFromAccountId(joinee.getAccountId(),
                                        currentPicture.getPictureId()) != null) {
                                    joineeListAdapter.highlightCurrentDrawer(joinee.getAccountId(), false,
                                            treeleafDrawPadView.getRemoteDrawerFromAccountId(joinee.getAccountId(),
                                                    currentPicture.getPictureId())
                                                    .getDrawMetadata().getTextColor());
                                }
                            } else {
                                joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, false,
                                        drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
                            }
                        }

                        joineeListAdapter.checkIfAllJoineesOnSamePicture(currentPicture);
                        currentPicture.setOnScreen(true);
//                        currentPicture.setNewArrival(false);
                        currentPicture.setRequestedForCollab(false);
                        treeleafDrawPadView.placeCurrentDrawingImage(currentPicture.getBitmap());
                        getDrawingViewResolution();
                        treeleafDrawPadView.setOnScreenPicture(currentPicture);
                        treeleafDrawPadView.showDrawingsForPicture(currentPicture.getPictureId());
                        if (currentPicture.isNewArrival())
                            treeleafDrawPadView.setRandomColor();
                        currentPicture.setNewArrival(false);
                        mode = Mode.IMAGE_DRAW;
                        switchDrawModeAndVideoMode(true);
                        joineeListAdapter.notifyDataSetChanged();
                        if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                            mDrawCallback.onMaximizeDrawing(currentPicture.getPictureId());
                        }
                    }
                });
            }
        });

        rvPictureStack = findViewById(R.id.rv_picture_stack);
        rvPictureStack.setLayoutManager(new LinearLayoutManager(this));
        rvPictureStack.setAdapter(pictureStackAdapter);
    }

    //if the call is not accepted within 40 seconds, terminate call
    private void checkIfCallIsTaken() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!Const.CallStatus.isCallTakingPlace) {
                    terminateBroadCast();
                }
            }
        }, 40000);
    }

    private void checkIfViewNeedstoHide(String runningOn, Boolean isCallMultiple) {
        imageVideoToggle.setVisibility(runningOn.equals(CONSUMER_TYPE) ? VISIBLE : GONE);
        imageSwitchCamera.setVisibility(runningOn.equals(CONSUMER_TYPE) ? VISIBLE : GONE);
//        imageInviteUser.setVisibility(runningOn.equals(CONSUMER_TYPE) ? GONE : VISIBLE);
        imageScreenShot.setVisibility(isCallMultiple ? VISIBLE : GONE);
//        imageInviteUser.setVisibility(mCallerContext.equals("RTC_CONTEXT_TICKET") ? VISIBLE : GONE);
    }

    private void highlightDrawerForTextEdit(String accountId, Integer drawColor) {
        joineeListAdapter.highlightCurrentDrawer(accountId, true, drawColor);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                joineeListAdapter.highlightCurrentDrawer(accountId, false, drawColor);
            }
        }, 500);

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
        ivSignalStrength.setImageResource(R.drawable.ic_good_signal);
    }

    private void loadCallNameAndProfileIcon(String calleeName, String calleeProfile) {
        tvCalleeName.setText((calleeName != null && !calleeName.isEmpty()) ? calleeName : "Unknown");
        if (!calleeProfile.isEmpty()) {

            loadBlurBackground(ivCallProfileBlur, calleeProfile);

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

    private void isCallInvitation(Boolean isInvitation) {
        imageAcceptCall.setImageResource(isInvitation ? R.drawable.ic_join_call : R.drawable.ic_accept_call);
        tvIsCalling.setText(isInvitation ? CALL_JOIN_MESSAGE : CALLING_MESSAGE);
        tvCallAccept.setText(isInvitation ? "Join" : "Accept");
    }

    private void loadBlurBackground(ImageView ivCallProfileBlur, String profileUrl) {
        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileUrl)
                    .transform(new GlideBlurTransformation(this))
                    .into(ivCallProfileBlur);
        }
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
        if (peerConnectionClient != null && permissionsGranted) {
            peerConnectionClient.startVideoSource();//TODO: this might not be needed
        }
    }

    @Override
    public void onPermissionGranted() {

    }

    private void acceptCall() {
        Const.CallStatus.isCallTakingPlace = true;
        mRestChannel.initConnection(SERVER);
        audioManager = AppRTCAudioManager.create(this);
        audioManager.start();
        Const.ROOM_NUMBER = new BigInteger(roomNumber);
        createRemoteRender();
        createLocalRender();
        remoteRender.init(rootEglBase.getEglBaseContext(), null);
        if (runningOn.equals(CONSUMER_TYPE))
            remoteRender.setEnableHardwareScaler(true);
        peerConnectionParameters = new PeerConnectionParameters(false, 360, 480, 20, "H264", true, 0, "opus", false, false, false, false, false);
        peerConnectionClient = PeerConnectionClient.getInstance();
        peerConnectionClient.createPeerConnectionFactory(this, peerConnectionParameters, this);
        peerConnectionClient.setIceConnectionChangeEventNotifier(this);
    }

    private void createRemoteRender() {
        rootEglBase = EglBase.create();
    }


    private void createLocalRender() {
        rootEglBase = EglBase.create();
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
        videoCapturer = createVideoCapturer();
        if (runningOn.equals(CONSUMER_TYPE)) {
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), remoteRender, videoCapturer,
                    handleId, "client", true);
        } else {
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, videoCapturer,
                    handleId, "client", false);
        }
        peerConnectionClient.createOffer(handleId);
    }

    @Override
    public void onPublisherRemoteJsep(BigInteger handleId, JSONObject jsep) {
        SessionDescription.Type type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"));
        String sdp = jsep.optString("sdp");
        SessionDescription sessionDescription = new SessionDescription(type, sdp);
        peerConnectionClient.setRemoteDescription(handleId, sessionDescription);
    }

    @Override
    public void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep) {
        /**
         * after subscriber is joined.
         */

        videoCapturer = createVideoCapturer();
        if (runningOn.equals(CONSUMER_TYPE)) {
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, videoCapturer,
                    handleId, SERVER, false);
        } else {
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), remoteRender, videoCapturer,
                    handleId, SERVER, true);
        }

        SessionDescription.Type type = SessionDescription.Type.fromCanonicalForm(jsep.optString("type"));
        String sdp = jsep.optString("sdp");
        SessionDescription sessionDescription = new SessionDescription(type, sdp);
        peerConnectionClient.subscriberHandleRemoteJsep(handleId, sessionDescription);
    }

    @Override
    public void onLeaving(BigInteger handleId) {

    }

    @Override
    public void onRoomCreated(BigInteger roomNumber) {

    }

    @Override
    public void onParticipantCreated(BigInteger participantId) {

    }

    @Override
    public void onRoomJoined(BigInteger roomNumber, String participantId) {
        mRestChannel.subscriberCreateHandle(participantId);
    }

    @Override
    public void onActivePublisherNotFound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServerActivity.this, "Active publisher not found", Toast.LENGTH_SHORT).show();
                terminateBroadCast();
            }
        });
    }

    @Override
    public BigInteger getRoomNumber() {
        return new BigInteger(roomNumber);
    }

    @Override
    public BigInteger getParticipantId() {
        return new BigInteger(participantId);
    }

    @Override
    public void showProgressBar(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    @Override
    public void hideProgressBar() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onSlowLink(BigInteger bigInteger) {
        ivSignalStrength.setImageResource(R.drawable.ic_bad_signal);
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, timerDelay);
    }

    @Override
    public void showVideoCallStartView(boolean visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isCallMultiple)
                    viewVideoCallStart.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public void handleVideoCallViewForSingleCall() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isCallMultiple) {
                    tvCallTimer.setVisibility(VISIBLE);
                    runCallTimer();
                    ivSignalStrength.bringToFront();
                    clCallSettings.bringToFront();
                    tvConnecting.setVisibility(GONE);
                    llCallAcceptOptions.setVisibility(GONE);
                    llCallCancel.setVisibility(VISIBLE);
                }
            }
        });
    }

    @Override
    public void stopAudioRinging() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPublisherVideoStarted() {
        if (mhostActivityCallback != null)
            mhostActivityCallback.onPublisherVideoStarted();
    }

    private void setUpJoineeRecyclerView() {
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
        joineeListAdapter.setOnItemClickListener(new JoineeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, View v, Joinee joinee) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mode.equals(Mode.IMAGE_DRAW)) {
                            //no need to open popup for self account
                            //if joinee's draw state is minimized or closed
                            if (joinee.isSelfAccount()) {
                                toggleViews(joinee);
                            } else {
                                if (!joinee.getJoineeDrawStateLocal().equals(Joinee.JoineeDrawState.MAXIMIZED)) {
                                    showInvitePopUp(v, joinee);
                                } else {
                                    toggleViews(joinee);
                                }
                            }
                        }
                    }
                });
            }
        });
        joineeListAdapter.setModeListener(new JoineeListAdapter.ModeListener() {
            @Override
            public Mode getCurrentMode() {
                return mode;
            }
        });
        rvJoinee = findViewById(R.id.rv_joinee);
        GridLayoutManager layoutManager = new GridLayoutManager(this, JoineeListAdapter.MAX_IN_A_ROW, GridLayoutManager.VERTICAL, false);
        rvJoinee.setLayoutManager(layoutManager);
        rvJoinee.setAdapter(joineeListAdapter);
    }

    private void toggleViews(Joinee joinee) {
        String accountId = joinee.getAccountId();
        if (mode.equals(Mode.IMAGE_DRAW)) {
            treeleafDrawPadView.switchDrawPadOfIndividualUsers((joinee.isSelfAccount() ? TreeleafDrawPadView.TYPE.LOCAL :
                            TreeleafDrawPadView.TYPE.REMOTE),
                    joinee.isSoloDrawing() ? SHOW_ALL : SHOW_ONE, accountId, currentPicture.getPictureId());
            joineeListAdapter.onJoineeItemClicked(accountId);
        }
    }

    private void showInvitePopUp(View view, Joinee joinee) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupView.findViewById(R.id.ll_toggle_drawing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleViews(joinee);
                popupWindow.dismiss();
            }
        });
        popupView.findViewById(R.id.ll_invite_to_collab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawCallback != null) {
                    mDrawCallback.onCollabInvite(joinee, currentPicture.getPictureId(), currentPicture.getBitmap());
                    mDrawCallback.onMaximizeDrawing(currentPicture.getPictureId());
                    Toast.makeText(ServerActivity.this, "Collab invite sent.", Toast.LENGTH_SHORT).show();
                }
                popupWindow.dismiss();
            }
        });

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAsDropDown(view, view.getWidth() / 2, view.getHeight() / 2);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }

    @Override
    public void updateProgressMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null)
                    progressDialog.setMessage("Preparing draw...");
            }
        });
    }

    @Override
    public void restError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServerActivity.this, message, Toast.LENGTH_SHORT).show();
                showVideoCallStartView(false);
                terminateBroadCast();
            }
        });
    }

    @Override
    public void streamUnpublished(BigInteger roomId, BigInteger publisherId) {
        //publisher unpublished the stream, publisherId is id of publisher who unpublished
        if (participantId.equals(publisherId.toString())) {
            /**
             * Host's video is unpublished
             */
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mhostActivityCallback != null && !mhostActivityCallback.isProductionEnvironment())
                        Toast.makeText(ServerActivity.this, "Host has unpublished!!", Toast.LENGTH_SHORT).show();
                    terminateBroadCast();
                }
            });
        }
    }

    @Override
    public void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {
        if (!callTerminated && mhostActivityCallback != null) {
            mhostActivityCallback.onServiceProviderAudioPublished(sessionId, roomId, participantId);
        }
    }

    @Override
    public void startCreatingOffer(BigInteger handleId) {
        offerPeerConnection(handleId);
    }

    // interface PeerConnectionClient.PeerConnectionEvents
    @Override
    public void onLocalDescription(SessionDescription sdp, BigInteger handleId) {
        Log.e(TAG, sdp.type.toString());
        mRestChannel.publisherCreateOffer(handleId, sdp);
    }

    @Override
    public void onRemoteDescription(SessionDescription sdp, BigInteger handleId) {
        Log.e(TAG, sdp.type.toString());
        mRestChannel.subscriberCreateAnswer(handleId, sdp);
    }

    @Override
    public void onIceCandidate(IceCandidate candidate, BigInteger handleId) {
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
                Toast.makeText(ServerActivity.this, description, Toast.LENGTH_SHORT).show();
                showVideoCallStartView(false);
            }
        });
        terminateBroadCast();
    }

    @Override
    public void onHangUp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServerActivity.this, "Connection hung up!!", Toast.LENGTH_SHORT).show();
            }
        });
        terminateBroadCast();
    }

    @Override
    public void onRemoteRender(final JanusConnection connection) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                remoteRender = new SurfaceViewRenderer(MainActivity.this);
//                remoteRender.init(rootEglBase.getEglBaseContext(), null);
//                LinearLayout.LayoutParams params  = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                rootView.addView(remoteRender, params);
                if (runningOn.equals(SERVICE_PROVIDER_TYPE))
                    connection.videoTrack.addRenderer(new VideoRenderer(remoteRender));
            }
        });
    }

    @Override
    public void iceConnectionChangeEvent(String event) {
        updateProgressMessage("IceConnection Event: " + event);
        if (event.equals("CONNECTED"))
            hideProgressBar();
    }

    @Override
    public void onRemoteVideoTrackAdded() {
        videoRendered = true;
    }

    @Override
    public void onVideoRendered() {
        videoRendered = true;
    }

    @Override
    public void onParticipantLeftJanus(String participantId) {

    }

    View.OnClickListener startDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoRendered) {
                if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                    mode = Mode.IMAGE_DRAW;
                    joineeListAdapter.notifyDataSetChanged();
                    showHideDrawView(true);
                    remoteRender.addFrameListener(frameListener, 1);
                    //after taking screenshot
                } else {
                    Toast.makeText(ServerActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(ServerActivity.this, "No image to draw", Toast.LENGTH_SHORT).show();


        }
    };

    View.OnClickListener discardDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DrawPadUtil.hideKeyboard(v.getRootView(), ServerActivity.this);
            --localPicturesCount;
            Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
            mapPictures.get(currentPicture.getPictureId()).setClosed(true);
            mode = Mode.VIDEO_STREAM;
            currentPicture.setOnScreen(false);
            currentPicture.setRequestedForCollab(false);
            currentPicture.setNewArrival(false);
            treeleafDrawPadView.hideAllDrawings();
            treeleafDrawPadView.setOnScreenPicture(null);
            showHideDrawView(false);
            joineeListAdapter.makeAllJoineesVisible();
            if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                mDrawCallback.onDiscardDraw(currentPicture.getPictureId());//TODO: uncomment this later
            }
        }
    };

    private void switchDrawModeAndVideoMode(boolean drawMode) {
        clCallSettings.setVisibility(drawMode ? View.GONE : View.VISIBLE);
        clCallOtions.setVisibility(drawMode ? View.GONE : View.VISIBLE);
        if (drawMode) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(ALIGN_PARENT_LEFT, 0);
            params.leftMargin = (int) DrawPadUtil.convertDpToPixel(50f, this);
            rlJoineeList.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
            params.leftMargin = (int) DrawPadUtil.convertPixelsToDp(40f, this);
            rlJoineeList.setLayoutParams(params);
        }
    }

    View.OnClickListener minimizeDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DrawPadUtil.hideKeyboard(v.getRootView(), ServerActivity.this);
            if (currentPicture != null) {
                currentPicture.setOnScreen(false);
                currentPicture.setRequestedForCollab(false);
                currentPicture.setNewArrival(false);
                addPictureToStack(currentPicture);
                new Handler().postDelayed(() -> minimizeCurrentDrawing(), 0);
                mode = Mode.VIDEO_STREAM;
                treeleafDrawPadView.hideAllDrawings();
                treeleafDrawPadView.setOnScreenPicture(null);
                joineeListAdapter.notifyDataSetChanged();
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onMinimizeDrawing(currentPicture.getPictureId());
                }
            }
        }
    };

    View.OnClickListener clearMqttLogsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (logView.equals(LOCAL_LOG))
                llMqttLog.removeAllViews();
            else
                llMqttLogRemote.removeAllViews();
        }
    };

    View.OnClickListener viewLocalLogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            llLogLocal.setVisibility(VISIBLE);
            llLogRemote.setVisibility(GONE);

            logView = LOCAL_LOG;
            btnViewLocalLog.setBackgroundColor(getResources().getColor(R.color.color_green));
            btnViewRemoteLog.setBackgroundColor(getResources().getColor(R.color.color_red));
        }
    };

    View.OnClickListener viewRemoteLogClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            llLogLocal.setVisibility(GONE);
            llLogRemote.setVisibility(VISIBLE);

            logView = RMEOTE_LOG;
            btnViewLocalLog.setBackgroundColor(getResources().getColor(R.color.color_red));
            btnViewRemoteLog.setBackgroundColor(getResources().getColor(R.color.color_green));
        }
    };

    private void minimizeCurrentDrawing() {
        switchDrawModeAndVideoMode(false);
        VideoCallUtil.materialContainerTransformVisibility(layoutDraw,
                rvPictureStack.findViewHolderForLayoutPosition(rvPictureStack.getAdapter().getItemCount() - 1).itemView,
                rlServerRoot);
    }

    private void maximizeCurrentDrawing() {
        mode = Mode.IMAGE_DRAW;
        joineeListAdapter.notifyDataSetChanged();
        switchDrawModeAndVideoMode(true);
    }

    private void showHideDrawView(Boolean showDrawView) {
        layoutDraw.setVisibility(showDrawView ? View.VISIBLE : View.GONE);

        clCallSettings.setVisibility(showDrawView ? View.GONE : View.VISIBLE);
        clCallOtions.setVisibility(showDrawView ? View.GONE : View.VISIBLE);

        if (showDrawView) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(ALIGN_PARENT_LEFT, 0);
            params.leftMargin = (int) DrawPadUtil.convertDpToPixel(50f, this);
            rlJoineeList.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlJoineeList.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
            params.leftMargin = (int) DrawPadUtil.convertPixelsToDp(40f, this);
            rlJoineeList.setLayoutParams(params);
        }
    }

    View.OnClickListener videoToggleClickListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            videoOff = !videoOff;
            Toast.makeText(ServerActivity.this, videoOff ? "Video turned off. " : "Video turned on.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ServerActivity.this, audioOff ? "Audio muted. " : "Audio turned on.", Toast.LENGTH_SHORT).show();
            peerConnectionClient.setAudioEnabled(audioOff);
            imageAudioToggle.setImageDrawable(audioOff ? getResources().getDrawable(R.drawable.ic_mute_mic, getApplicationContext().getTheme()) :
                    getResources().getDrawable(R.drawable.ic_open_mic, getApplicationContext().getTheme()));

        }
    };

    View.OnClickListener speakerSwitchListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            audioManager.toggleSpeaker();
            imageSpeakerSwitch.setImageDrawable(loudSpeakerOn ? getResources().getDrawable(R.drawable.ic_loud_speaker_on, getApplicationContext().getTheme()) :
                    getResources().getDrawable(R.drawable.ic_loud_speaker_off, getApplicationContext().getTheme()));
        }
    };

    View.OnClickListener screenShotClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoRendered) {
                justScreenShot = true;
                remoteRender.addFrameListener(frameListener, 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivScreenShotImage.setImageResource(android.R.color.transparent);
                    }
                }, 1800);
            } else
                Toast.makeText(ServerActivity.this, "No image to capture", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener inviteUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mhostActivityCallback != null) {
                mhostActivityCallback.inviteUsersToCall();
            }
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
            ivCancelCall.setClickable(false);
            ivDeclineCall.setClickable(false);
            removeIncomingCallNotification();
            terminateBroadCast();
        }
    };

    View.OnClickListener declineCallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mhostActivityCallback != null) {
                mhostActivityCallback.notifyCallDecline();
            }
            imageEndCall.setClickable(false);
            ivCancelCall.setClickable(false);
            ivDeclineCall.setClickable(false);
            terminateBroadCast();
        }
    };

    View.OnClickListener acceptCallClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeIncomingCallNotification();
            handleIncomingCallUi();
            acceptCall();
        }
    };

    private void handleIncomingCallUi() {
        stopAudioRinging();
        tvIsCalling.setVisibility(View.GONE);
        tvConnecting.setVisibility(View.VISIBLE);
        llCallAcceptOptions.setVisibility(View.GONE);
        llCallCancel.setVisibility(VISIBLE);
        imageAcceptCall.setClickable(false);
        ivDeclineCall.setClickable(false);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!callTerminated) {
                    stopAudioRinging();
                    if (mhostActivityCallback != null) {
                        if (Const.CallStatus.isCallTakingPlace)
                            mhostActivityCallback.notifySubscriberLeft();//separate out
                        mhostActivityCallback.unSubscribeVideoCallMqtt();
                    }
                    if (mRestChannel != null) {
                        if (Const.CallStatus.isCallTakingPlace) {
                            mRestChannel.publisherUnpublish();//separate out
                            mRestChannel.subscriberLeave();//separate out
                            mRestChannel.detachPlugin();//separate out
                            mRestChannel.destroySession();//separate out
                        }
                        mRestChannel.clearPendingApiCalls();
                        mRestChannel = null;
                    }
                    if (remoteRender != null)
                        remoteRender.release();
                    if (peerConnectionClient != null) {
                        peerConnectionClient.close();
                        peerConnectionClient = null;
                    }
                    if (audioManager != null) {
                        audioManager.stop();
                        audioManager = null;
                    }
                    callTerminated = true;
                    Const.CallStatus.isCallingScreenOn = false;
                    ApiClient.retrofit = null;
                    callTimerHandler.removeCallbacks(callTimerRunnable);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("key_finish_server_activity", "server_activity_finish");
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();

                }
            }
        });
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

    private void runCallTimer() {
        callTimerHandler.post(callTimerRunnable);
    }

    private void removeIncomingCallNotification() {
        if (mhostActivityCallback != null)
            mhostActivityCallback.closeAVCallNotification();
    }

    public interface VideoCallListener extends Callback.AudioVideoCallbackListener {

        void onVideoViewReady();

        void onHostTerminateCall();

        void onCallerDetailFetched(String baseUrl, String apiKey, String apiSecret, String roomId, String participantId);

        void terminateCallReceive();

        void checkCallHandledOnAnotherDevice();

    }

}
