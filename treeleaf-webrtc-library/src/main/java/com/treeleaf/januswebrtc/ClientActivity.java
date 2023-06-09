package com.treeleaf.januswebrtc;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
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
import com.treeleaf.januswebrtc.tickets.ViewPagerAdapter;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsActivityLogFragment;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsAttachmentFragment;
import com.treeleaf.januswebrtc.tickets.fragments.TicketsConversationFragment;
import com.treeleaf.januswebrtc.utils.GlideBlurTransformation;
import com.treeleaf.januswebrtc.utils.NonSwipeableViewPager;

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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.TRUE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.HIDE_THIS_VIEW;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ALL;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ONE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_THIS_VIEW;
import static com.treeleaf.januswebrtc.Const.CALLEE_NAME;
import static com.treeleaf.januswebrtc.Const.CALLEE_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.CALLER_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.CLIENT;
import static com.treeleaf.januswebrtc.Const.PUBLISHER;
import static com.treeleaf.januswebrtc.Const.JANUS_API_KEY;
import static com.treeleaf.januswebrtc.Const.JANUS_API_SECRET;
import static com.treeleaf.januswebrtc.Const.JANUS_CREDENTIALS_SET;
import static com.treeleaf.januswebrtc.Const.JANUS_URL;
import static com.treeleaf.januswebrtc.Const.JOINEE_LOCAL;
import static com.treeleaf.januswebrtc.Const.JOINEE_REMOTE;
import static com.treeleaf.januswebrtc.Const.KEY_MULTIPLE_CALL;
import static com.treeleaf.januswebrtc.Const.KEY_RUNNING_ON;
import static com.treeleaf.januswebrtc.Const.LOCAL_LOG;
import static com.treeleaf.januswebrtc.Const.PICTURE_EXCEED_MSG;
import static com.treeleaf.januswebrtc.Const.RMEOTE_LOG;
import static com.treeleaf.januswebrtc.Const.SERVER;
import static com.treeleaf.januswebrtc.Const.SUBSCRIBER;
import static com.treeleaf.januswebrtc.audio.AppRTCAudioManager.loudSpeakerOn;

public class ClientActivity extends CommonCallActivity implements Callback.JanusRTCInterface,
        Callback.ApiCallback, PeerConnectionEvents, Callback.ConnectionEvents {
    private static final String TAG = ClientActivity.class.getSimpleName();
    private final String MAPPING_ISSUE = "MAPPING_ISSUE";

    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer localRender, remoteRender;
    private VideoCapturer videoCapturer;
    private EglBase rootEglBase;
    private RestChannel mRestChannel;
    private TextView tvRoomNumber, tvParticipantNumber;
    private ProgressDialog progressDialog;
    private View layoutDraw;
    private ImageView fabStartDraw;
    private ImageView fabDiscardDraw, fabMinimizeDraw;
    private ImageView imageViewCaptureImage;
    private ImageView imageVideoToggle, imageSpeakerSwitch, imageAudioToggle, imageScreenShot, imageInviteUser, imageSwitchCamera,
            imageEndCall, ivScreenShotImage, ibToggleJoineeList, ivSignalStrength;
    private TreeleafDrawPadView treeleafDrawPadView;
    private ForwardTouchesView forwardTouchesView;
    private LinearLayout clCallSettings, clCallOtions;
    private RelativeLayout rlScreenShotImage;
    private RelativeLayout rlJoineeList, rlClientRoot;
    private View viewVideoCallStart;
    private ImageView ivCalleeProfile, ivTerminateCall;
    private TextView tvCalleeName, tvReconnecting, tvDeviceResolution;
    private EglRenderer.FrameListener frameListener;

    private Boolean videoOff = false;
    private Boolean audioOff = false;
    private Boolean justScreenShot = false;
    private Boolean showFullList = false;
    private Boolean credentialsReceived;
    private boolean callTerminated = false;

    private String calleeName;
    private ArrayList<String> calleeProfile;
    private String mLocalAccountId, mLocalAccountName;

    private RecyclerView rvJoinee, rvPictureStack;
    private JoineeListAdapter joineeListAdapter;
    private PictureStackAdapter pictureStackAdapter;
    public static Callback.HostActivityCallback mhostActivityCallback;
    private static Callback.DrawCallBack mDrawCallback;
    private VideoCallListener videoCallListener;
    private Callback.DrawPadEventListener clientDrawingPadEventListener;
    private Handler handler;
    private Runnable runnable;
    private int timerDelay = 10000;
    private String roomNumber, localParticipantNumber;
    private AppRTCAudioManager audioManager;
    private LinkedHashMap<String, DrawMetadata> drawMetadataLocal = new LinkedHashMap<>();
    private MetaDataUpdateListener metaDataUpdateListener;
    private CaptureDrawParam captureDrawParam;

    private int localDeviceHeight, localDeviceWidth;
    private Mode mode = Mode.VIDEO_STREAM;
    private RelativeLayout rlMultipleCalleeView;
    private ImageView ivCalleeFirst, ivCalleeSecond;
    private FrameLayout flExtraCalleePic;
    private TextView tvExtraCalleeNumber;
    private CardView cvSingleCalleeView;
    private Picture currentPicture;
    private Integer localPicturesCount = 0;
    private LinkedHashMap<String, Picture> mapPictures = new LinkedHashMap<>();

    public String mCallRole = PUBLISHER;
    private boolean videoRendered = false;
    private String touchSessionId;
    private LinearLayout llMqttLog, llMqttLogRemote;
    private LinearLayout llLogLocal, llLogRemote;
    private Button btnClearMqttLogs, btnViewLocalLog, btnViewRemoteLog;
    private ScrollView svMqttLog, svMqttLogRemote;
    private ImageView ivCallProfileBlur;
    private Float prevX, prevY;
    private String logView = LOCAL_LOG;
    private String callerProfilePictureUrl;
    private MediaPlayer mediaPlayer;
    private Boolean isCallMultiple;
    private TextView tvIsCalling;
    private RelativeLayout rlCallCancel;
    private TextView tvCallTimer, tvCurrentDrawer;
    private int seconds = 0;
    private String mCallerContext;
    private Handler callTimerHandler;
    private Runnable callTimerRunnable;

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
                              Callback.DrawCallBack drawCallBack, String calleeName, ArrayList<String> callProfileUrl, String runningOn,
                              String callerAccountPicture, Boolean isCallMultiple) {
        mhostActivityCallback = hostActivityCallBack;
        mDrawCallback = drawCallBack;
        Intent intent = new Intent(context, ClientActivity.class);
        intent.putExtra(JANUS_CREDENTIALS_SET, credentialsAvailable);
        intent.putExtra(CALLEE_NAME, calleeName);
        intent.putExtra(CALLEE_PROFILE_URL, callProfileUrl);
        intent.putExtra(CALLER_PROFILE_URL, callerAccountPicture);
        intent.putExtra(KEY_RUNNING_ON, runningOn);
        intent.putExtra(KEY_MULTIPLE_CALL, isCallMultiple);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_client;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvRoomNumber = findViewById(R.id.tv_room_number);
        tvParticipantNumber = findViewById(R.id.tv_participant_number);
        layoutDraw = findViewById(R.id.layout_draw);
        fabStartDraw = findViewById(R.id.fab_start_draw);
        fabDiscardDraw = findViewById(R.id.fab_discard_draw);
        fabMinimizeDraw = findViewById(R.id.fab_minimize_draw);
        imageViewCaptureImage = findViewById(R.id.iv_captured_image);
        treeleafDrawPadView = findViewById(R.id.treeleaf_draw_pad);
        localRender = findViewById(R.id.local_video_view);
        remoteRender = findViewById(R.id.remote_video_view);
        clCallSettings = findViewById(R.id.cl_call_setting);
        clCallOtions = findViewById(R.id.cl_call_options);
        imageVideoToggle = findViewById(R.id.image_video);
        imageAudioToggle = findViewById(R.id.image_mic);
        imageSpeakerSwitch = findViewById(R.id.image_speaker_switch);
        imageScreenShot = findViewById(R.id.image_screenshot);
        imageInviteUser = findViewById(R.id.image_invite_users);
        imageSwitchCamera = findViewById(R.id.image_switch_camera);
        imageEndCall = findViewById(R.id.image_end_call);
        ivSignalStrength = findViewById(R.id.iv_signal_strength);
        rlScreenShotImage = findViewById(R.id.rl_screenshot_image);
        ivScreenShotImage = findViewById(R.id.iv_screenshot_image);
        rlClientRoot = findViewById(R.id.rl_client_root);
        rlJoineeList = findViewById(R.id.rl_joinee_list);
        ibToggleJoineeList = findViewById(R.id.ib_toggle_joinee_list);
        viewVideoCallStart = findViewById(R.id.view_video_call_start);
        tvCalleeName = findViewById(R.id.tv_callee_name);
        tvReconnecting = findViewById(R.id.tv_reconnecting);
        ivCalleeProfile = findViewById(R.id.iv_callee_profile);
        ivTerminateCall = findViewById(R.id.iv_terminate_call);

        rlMultipleCalleeView = findViewById(R.id.rl_multiple_callee_view);
        ivCalleeFirst = findViewById(R.id.iv_callee_first);
        ivCalleeSecond = findViewById(R.id.iv_callee_second);
        flExtraCalleePic = findViewById(R.id.fl_extra_callee_pic);
        tvExtraCalleeNumber = findViewById(R.id.tv_extra_callee_number);
        cvSingleCalleeView = findViewById(R.id.cv_single_callee_view);
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
        tvIsCalling = findViewById(R.id.tv_is_calling);
        rlCallCancel = findViewById(R.id.rl_call_cancel);
        tvCallTimer = findViewById(R.id.tv_call_timer);
        tvCurrentDrawer = findViewById(R.id.tv_current_drawer);

        imageVideoToggle.setOnClickListener(videoToggleClickListener);
        imageAudioToggle.setOnClickListener(audioToggleClickListener);
        imageSpeakerSwitch.setOnClickListener(speakerSwitchListener);
        imageScreenShot.setOnClickListener(screenShotClickListener);
        imageInviteUser.setOnClickListener(inviteUserClickListener);
        imageSwitchCamera.setOnClickListener(switchCameraClickListener);
        imageEndCall.setOnClickListener(endCallClickListener);
        ivTerminateCall.setOnClickListener(endCallClickListener);
        ibToggleJoineeList.setOnClickListener(joineeListToggleListener);

        forwardTouchesView = findViewById(R.id.forward_touch);

        forwardTouchesView.setForwardTo(treeleafDrawPadView);
        fabStartDraw.setOnClickListener(startDrawClickListener);
        fabDiscardDraw.setOnClickListener(discardDrawClickListener);
        fabMinimizeDraw.setOnClickListener(minimizeDrawClickListener);
        btnClearMqttLogs.setOnClickListener(clearMqttLogsClickListener);
        btnViewLocalLog.setOnClickListener(viewLocalLogClickListener);
        btnViewRemoteLog.setOnClickListener(viewRemoteLogClickListener);


        btnViewLocalLog.setBackgroundColor(getResources().getColor(R.color.color_green));
        btnViewRemoteLog.setBackgroundColor(getResources().getColor(R.color.color_red));

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
                                treeleafDrawPadView.createNewDrawCard(bitmap, ClientActivity.this,
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
                                Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        /**
                         * remove frame listener as soon as you get the captured image.
                         */
                        if (mCallRole.equals(PUBLISHER))
                            localRender.removeFrameListener(frameListener);
                        else
                            remoteRender.removeFrameListener(frameListener);

                    }
                });
            }
        };

        videoCallListener = new VideoCallListener() {

            @Override
            public void onAddParticipantToCall() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar snack = Snackbar.make(getWindow().getDecorView().getRootView().findViewById(android.R.id.content),
                                "Call invited to selected participants", Snackbar.LENGTH_LONG);
                        snack.show();
                    }
                });
            }

            @Override
            public void onMqttReponseArrived(String responseType, boolean isLocalResponse) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (isLocalResponse && logView.equals(LOCAL_LOG)) {
                            TextView textView = new TextView(ClientActivity.this);
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
                            TextView textView = new TextView(ClientActivity.this);
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
//                tvReconnecting.setVisibility(status.equals(MQTT_DISCONNECTED) ? VISIBLE : GONE);
            }

            @Override
            public void onJoineeReceived(String joineeName, String joineedProfileUrl, String accountId, String joineeType) {
                //add new joinee information in view
                Log.d(TAG, "onjoineereceived");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (joineeType.equals(JOINEE_REMOTE)) {
                            stopAudioRinging();
                            showVideoCallStartView(false);
                        }
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
            public void onJanusCredentialsReceived(String baseUrl, String apiKey, String apiSecret,
                                                   String serviceName, ArrayList<String> serviceUri, String localAccountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        credentialsReceived = true;
                        calleeName = serviceName;
                        calleeProfile = serviceUri;
                        mLocalAccountId = localAccountId;

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
            public void onParticipantLeft() {
                if (mRestChannel.isVideoRoomEmpty())
                    terminateBroadCast();

//                if (!isCallMultiple) {
//                    terminateBroadCast();
//                    return;
//                }
//
//                if (mCallRole.equals(SUBSCRIBER)) {
//                    terminateBroadCast();
//                }
            }

            @Override
            public void onCallDeclined() {
                terminateBroadCast();
            }

        };

        clientDrawingPadEventListener = new Callback.DrawPadEventListener() {

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
                                                Toast.makeText(ClientActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ClientActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                    Bitmap downSampledBitmap = VideoCallUtil.downSampleBitmap(receivedBitmap);
                                    Picture picture = createNewPicture(fromAccountId, imageId, true,
                                            true, false, downSampledBitmap);
                                    localPicturesCount++;
                                    Log.d("localpicturescount", "local pitcures count: " + localPicturesCount);
                                    addPictureToStack(picture);
                                    treeleafDrawPadView.createNewDrawCard(downSampledBitmap, ClientActivity.this,
                                            mLocalAccountId, mLocalAccountName, imageId, picture.getPictureIndex(), TreeleafDrawPadView.HIDE_THIS_VIEW);
                                    drawMetadataLocal.put(imageId, new DrawMetadata());
                                    treeleafDrawPadView.addNewRemoteDrawer(ClientActivity.this, fromAccountId, imageId, TreeleafDrawPadView.HIDE_THIS_VIEW);
                                    joineeListAdapter.updateJoineeDrawStat(fromAccountId, Joinee.JoineeDrawState.MAXIMIZED, imageId, false);
                                    if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                        mDrawCallback.onMinimizeDrawing(imageId);
                                    }
                                } else {
                                    mDrawCallback.onMaxDrawingExceed();
                                    Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(ClientActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ClientActivity.this, "Invalid image...", Toast.LENGTH_SHORT).show();
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
                                treeleafDrawPadView.createNewDrawCard(downSampledBitmap, ClientActivity.this,
                                        mLocalAccountId, mLocalAccountName, imageId, picture.getPictureIndex(), TreeleafDrawPadView.SHOW_THIS_VIEW);
                                drawMetadataLocal.put(currentPicture.getPictureId(), new DrawMetadata());
                                treeleafDrawPadView.addNewRemoteDrawer(ClientActivity.this, fromAccountId, imageId, TreeleafDrawPadView.SHOW_THIS_VIEW);
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
                                Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
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
                            treeleafDrawPadView.addNewRemoteDrawer(ClientActivity.this, fromAccountId,
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
                            Toast.makeText(ClientActivity.this,
                                    "Collab invite failed. " + fromAccountName + " exceeded maximum number of drawings.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        };

        setUpJoineeRecyclerView();
        setUpPictureStackRecyclerView();
        setUpNetworkStrengthHandler();
        setUpCallTimer();
        if (mhostActivityCallback != null) {
            mhostActivityCallback.passJoineeReceivedCallback(videoCallListener);
            mhostActivityCallback.passDrawPadEventListenerCallback(clientDrawingPadEventListener);
            mLocalAccountId = mhostActivityCallback.getLocalAccountId();
            mLocalAccountName = mhostActivityCallback.getLocalAccountName();
            mCallerContext = mhostActivityCallback.getCallerContext();
            mhostActivityCallback.fetchJanusServerInfo();
            mhostActivityCallback.specifyRole(RestChannel.Role.CLIENT);
        }


        //if condition here
        credentialsReceived = getIntent().getBooleanExtra(JANUS_CREDENTIALS_SET, false);
        calleeName = getIntent().getStringExtra(CALLEE_NAME);
        calleeProfile = getIntent().getStringArrayListExtra(CALLEE_PROFILE_URL);
        callerProfilePictureUrl = getIntent().getStringExtra(CALLER_PROFILE_URL);
        mCallRole = (getIntent().getStringExtra(KEY_RUNNING_ON) == null) ? mCallRole : getIntent().getStringExtra(KEY_RUNNING_ON);
        isCallMultiple = getIntent().getBooleanExtra(KEY_MULTIPLE_CALL, true);

        checkIfViewNeedstoHide(mCallRole);

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
        tvRoomNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyClipData(tvRoomNumber);
            }
        });
        tvParticipantNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyClipData(tvParticipantNumber);
            }
        });


        metaDataUpdateListener = new MetaDataUpdateListener() {
            @Override
            public void setDrawMode(TreeleafDrawPadView.DrawMode drawMode) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setDrawMode(drawMode);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                }
            }

            @Override
            public void setBrushWidth(Float width) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushWidth(width);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setBrushOpacity(Integer opacity) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushOpacity(opacity);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setBrushColor(Integer color) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setBrushColor(color);
                joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, false, color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void setTextColor(Integer color) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setTextColor(color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                }
            }

            @Override
            public void onClearCanvasPressed(Boolean pressed) {
                if (currentPicture == null) return;
                drawMetadataLocal.get(currentPicture.getPictureId()).setClearCanvas(pressed);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onDrawCanvasCleared(currentPicture.getPictureId());
                }
            }

            @Override
            public void onStartDrawing(float x, float y) {
                if (currentPicture == null) return;
                Log.d(TAG, "onStartDrawing: " + x + " " + y);
                drawMetadataLocal.get(currentPicture.getPictureId()).setCurrentDrawPosition(new Position(x, y));
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {//TODO: uncomment this later
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
                if (currentPicture == null) return;
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
                if (currentPicture == null) return;
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
                    if (currentPicture == null) return;
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetadataLocal.get(currentPicture.getPictureId()));
                    mDrawCallback.onReceiveNewTextField(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth),
                            VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight), editTextFieldId,
                            currentPicture.getPictureId(), captureDrawParam);
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    if (currentPicture == null) return;
                    mDrawCallback.onReceiveNewTextChange(text, id, currentPicture.getPictureId());
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    if (currentPicture == null) return;
                    mDrawCallback.onReceiveEdiTextRemove(editTextId, currentPicture.getPictureId());
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

            @Override
            public void onPointerClicked(float x, float y) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    if (currentPicture == null) return;
                    mDrawCallback.onPointerClicked(VideoCallUtil.normalizeXCoordinatePrePublish(x, localDeviceWidth),
                            VideoCallUtil.normalizeYCoordinatePrePublish(y, localDeviceHeight), currentPicture.getPictureId());
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetadataLocal.get(currentPicture.getPictureId()).getTextColor());
            }

        };
        treeleafDrawPadView.setMetaDataUpdateListener(metaDataUpdateListener);

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
                                    rlClientRoot);

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

    private void checkIfViewNeedstoHide(String runningOn) {
        localRender.setVisibility(runningOn.equals(PUBLISHER) ? VISIBLE : GONE);
        remoteRender.setVisibility(runningOn.equals(SUBSCRIBER) ? VISIBLE : GONE);
        imageVideoToggle.setVisibility(runningOn.equals(PUBLISHER) ? VISIBLE : GONE);
        imageSwitchCamera.setVisibility(runningOn.equals(PUBLISHER) ? VISIBLE : GONE);
        imageInviteUser.setVisibility(runningOn.equals(PUBLISHER) ? GONE : VISIBLE);
        imageScreenShot.setVisibility(mCallerContext.equals("RTC_CONTEXT_TICKET") ? VISIBLE : GONE);
        imageInviteUser.setVisibility(mCallerContext.equals("RTC_CONTEXT_TICKET") ? VISIBLE : GONE);
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

    private void copyClipData(TextView view) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("new clip data", view.getText().toString());
        clipboard.setPrimaryClip(clip);
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

    private void loadCallNameAndProfileIcon(String calleeName, ArrayList<String> calleeProfile) {
        tvCalleeName.setText((calleeName != null && !calleeName.isEmpty()) ? calleeName : "Unknown");
        if (calleeProfile != null) {
            if (calleeProfile.size() == 1) {

                loadBlurBackground(ivCallProfileBlur, calleeProfile.get(0));

                cvSingleCalleeView.setVisibility(VISIBLE);
                rlMultipleCalleeView.setVisibility(GONE);
                flExtraCalleePic.setVisibility(GONE);
                String imgUri = calleeProfile.get(0);
                RequestOptions options = new RequestOptions()
                        .fitCenter()
                        .placeholder(R.drawable.ic_empty_profile_holder_icon)
                        .error(R.drawable.ic_empty_profile_holder_icon);

                Glide.with(this)
                        .load(imgUri)
                        .apply(options)
                        .into(ivCalleeProfile);

            } else if (calleeProfile.size() == 2) {

                //incase of multiple callee, load own accounts profile picture as background

                loadBlurBackground(ivCallProfileBlur, callerProfilePictureUrl);

                cvSingleCalleeView.setVisibility(GONE);
                rlMultipleCalleeView.setVisibility(VISIBLE);
                displayTwoProfileIcons(calleeProfile);
                flExtraCalleePic.setVisibility(GONE);
            } else if (calleeProfile.size() > 2) {

                loadBlurBackground(ivCallProfileBlur, callerProfilePictureUrl);

                cvSingleCalleeView.setVisibility(GONE);
                rlMultipleCalleeView.setVisibility(VISIBLE);
                displayTwoProfileIcons(calleeProfile);
                flExtraCalleePic.setVisibility(VISIBLE);
                tvExtraCalleeNumber.setText("+" + (calleeProfile.size() - 2));
            } else {
                loadBlurBackground(ivCallProfileBlur, calleeProfile.get(0));
                cvSingleCalleeView.setVisibility(VISIBLE);
                rlMultipleCalleeView.setVisibility(GONE);
                flExtraCalleePic.setVisibility(GONE);
            }
        }
    }

    private void loadBlurBackground(ImageView ivCallProfileBlur, String profileUrl) {
        if (profileUrl != null && !profileUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileUrl)
                    .transform(new GlideBlurTransformation(this))
                    .into(ivCallProfileBlur);
        }
    }

    private void displayTwoProfileIcons(ArrayList<String> calleeProfile) {
        String imgUri1 = calleeProfile.get(0);
        RequestOptions options1 = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(imgUri1)
                .apply(options1)
                .into(ivCalleeFirst);

        String imgUri2 = calleeProfile.get(1);
        RequestOptions options2 = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.ic_empty_profile_holder_icon)
                .error(R.drawable.ic_empty_profile_holder_icon);

        Glide.with(this)
                .load(imgUri2)
                .apply(options2)
                .into(ivCalleeSecond);
    }

    @Override
    public void showVideoCallStartView(boolean visible) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCallerContext.equals("RTC_CONTEXT_TICKET"))
                    viewVideoCallStart.setVisibility(visible ? View.VISIBLE : GONE);
            }
        });
    }

    @Override
    public void handleVideoCallViewForSingleCall() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCallerContext.equals("RTC_CONTEXT_INBOX")) {
                    tvCallTimer.setVisibility(VISIBLE);
                    runCallTimer();
                    ivSignalStrength.bringToFront();
                    clCallSettings.bringToFront();
                    tvIsCalling.setVisibility(GONE);
                    clCallOtions.setVisibility(GONE);
                    ivTerminateCall.setVisibility(VISIBLE);
                }
                Const.CallStatus.isCallTakingPlace = true;
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
        } catch (Exception exception) {
            Log.d("mediaplayer", " mediaplayer stop failed " + exception.getLocalizedMessage());
        }
    }

    @Override
    public void onPublisherVideoStarted() {
//        stopAudioRinging();
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
                        ibToggleJoineeList.setVisibility(show ? View.VISIBLE : GONE);
                    }
                });

            }

            @Override
            public void onShowHideJoineeList(Boolean show) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rlJoineeList.setVisibility(show ? View.VISIBLE : GONE);
                    }
                });

            }
        });
        joineeListAdapter.setOnItemClickListener(new JoineeListAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position, View v, final Joinee joinee) {
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
        if (mode.equals(Mode.IMAGE_DRAW) && currentPicture != null) {
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
                if (mDrawCallback != null && currentPicture != null) {
                    mDrawCallback.onCollabInvite(joinee, currentPicture.getPictureId(), currentPicture.getBitmap());
                    mDrawCallback.onMaximizeDrawing(currentPicture.getPictureId());
                    Toast.makeText(ClientActivity.this, "Collab invite sent.", Toast.LENGTH_SHORT).show();
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
            audioManager = AppRTCAudioManager.create(this);
            audioManager.start();
            createLocalRender();
            peerConnectionParameters = new PeerConnectionParameters(false, 360, 480, 20, "H264", true, 0, "opus", false, false, false, false, false);
            peerConnectionClient = PeerConnectionClient.getInstance();
            peerConnectionClient.createPeerConnectionFactory(this, peerConnectionParameters, this);
            peerConnectionClient.setIceConnectionChangeEventNotifier(this);
            showVideoCallStartView(true);
            startAudioRinging();
            checkIfCallIsTaken();
        }
    }

    private void startAudioRinging() {
        try {
            mediaPlayer = MediaPlayer.create(ClientActivity.this, R.raw.call_outgoing_new);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            e.printStackTrace();
        }

    }


    private void createLocalRender() {
        rootEglBase = EglBase.create();
        if (mCallRole.equals(PUBLISHER)) {
            localRender.init(rootEglBase.getEglBaseContext(), null);
            localRender.setEnableHardwareScaler(true);
        } else
            remoteRender.init(rootEglBase.getEglBaseContext(), null);

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
            if (mCallRole.equals(PUBLISHER)) {
                peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender, videoCapturer,
                        handleId, "client", true);
            } else {
                peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, videoCapturer,
                        handleId, "client", false);
            }
            peerConnectionClient.createOffer(handleId);
        }

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
    public void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep) {
        /**
         * after subscriber is joined.
         */

        videoCapturer = createVideoCapturer();
        if (mCallRole.equals(PUBLISHER)) {
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
    public void onRoomCreated(final BigInteger roomNumber) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ClientActivity.this.roomNumber = roomNumber.toString();
                tvRoomNumber.setText(roomNumber.toString());
            }
        });
    }

    @Override
    public void onParticipantCreated(BigInteger participantId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvParticipantNumber.setText(participantId.toString());
            }
        });
    }

    @Override
    public void onRoomJoined(BigInteger roomNumber, String participantId) {
        stopAudioRinging();
        mRestChannel.subscriberCreateHandle(participantId);
    }

    @Override
    public void setLocalParticipantId(BigInteger localParticipantId) {
        this.localParticipantNumber = String.valueOf(localParticipantId);
        if (mhostActivityCallback != null)
            mhostActivityCallback.setLocalParticipantId(localParticipantId);
    }

    @Override
    public void onActivePublisherNotFound() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public BigInteger getRoomNumber() {
        return new BigInteger(roomNumber);
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
    public void onHangUp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Connection hung up!!");
            }
        });
        /**
         * no need to terminate broadcast in ClientActivity in case of hung up
         */
//        terminateBroadCast();
    }

    @Override
    public void onRemoteRender(final JanusConnection connection) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mCallRole.equals(SUBSCRIBER))
                    connection.videoTrack.addRenderer(new VideoRenderer(remoteRender));
            }
        });
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
                    progressDialog.setMessage("Preparing draw...");
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
        if (!isCallMultiple) {
            terminateBroadCast();
        }
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

        if (mRestChannel.isVideoRoomEmpty())
            terminateBroadCast();

//        if (!isCallMultiple) {
//            terminateBroadCast();
//        }
    }

    View.OnClickListener startDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (videoRendered) {
                if (!VideoCallUtil.isPictureCountExceed(localPicturesCount)) {
                    if (mCallRole.equals(PUBLISHER)) {
                        mode = Mode.IMAGE_DRAW;
                        joineeListAdapter.notifyDataSetChanged();
                        showHideDrawView(true);
                        localRender.addFrameListener(frameListener, 1);
                    } else if (mCallRole.equals(SUBSCRIBER) && joineeListAdapter.isJoineePresent()) {
                        mode = Mode.IMAGE_DRAW;
                        joineeListAdapter.notifyDataSetChanged();
                        showHideDrawView(true);
                        remoteRender.addFrameListener(frameListener, 1);
                    } else {
                        Toast.makeText(ClientActivity.this, "No image to draw", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ClientActivity.this, PICTURE_EXCEED_MSG, Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(ClientActivity.this, "No image to draw", Toast.LENGTH_SHORT).show();

        }
    };

    View.OnClickListener discardDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DrawPadUtil.hideKeyboard(v.getRootView(), ClientActivity.this);
            if (currentPicture != null) {
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
        }
    };

    private void switchDrawModeAndVideoMode(boolean drawMode) {
        clCallSettings.setVisibility(drawMode ? GONE : View.VISIBLE);
        clCallOtions.setVisibility(drawMode ? GONE : View.VISIBLE);
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
            DrawPadUtil.hideKeyboard(v.getRootView(), ClientActivity.this);
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
                rlClientRoot);

    }

    private void maximizeCurrentDrawing() {
        mode = Mode.IMAGE_DRAW;
        joineeListAdapter.notifyDataSetChanged();
        switchDrawModeAndVideoMode(true);
    }

    private void showHideDrawView(Boolean showDrawView) {
        layoutDraw.setVisibility(showDrawView ? View.VISIBLE : GONE);

        clCallSettings.setVisibility(showDrawView ? GONE : View.VISIBLE);
        clCallOtions.setVisibility(showDrawView ? GONE : View.VISIBLE);


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
                if (mCallRole.equals(PUBLISHER))
                    localRender.addFrameListener(frameListener, 1);
                else
                    remoteRender.addFrameListener(frameListener, 1);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivScreenShotImage.setImageResource(android.R.color.transparent);
                    }
                }, 1800);
            } else
                Toast.makeText(ClientActivity.this, "No image to capture", Toast.LENGTH_SHORT).show();
        }
    };

    View.OnClickListener inviteUserClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mhostActivityCallback != null) {
                if (videoRendered)
                    mhostActivityCallback.inviteUsersToCall();
                else
                    Toast.makeText(ClientActivity.this, "Please wait. Stream is not loaded.", Toast.LENGTH_SHORT).show();
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!callTerminated) {
                    stopAudioRinging();
                    if (mhostActivityCallback != null) {
                        mhostActivityCallback.notifyHostHangUp();
                        mhostActivityCallback.unSubscribeDrawingMqtt();
                    }
                    if (mRestChannel != null) {
                        mRestChannel.publisherUnpublish();
                        mRestChannel.detachPlugin();
                        mRestChannel.destroySession();
                        mRestChannel.clearPendingApiCalls();
                        mRestChannel = null;
                    }
                    if (mCallRole.equals(PUBLISHER) && localRender != null)
                        localRender.release();
                    if (mCallRole.equals(SUBSCRIBER) && remoteRender != null)
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

    public interface VideoCallListener extends Callback.AudioVideoCallbackListener {

        void onJanusCredentialsReceived(String baseUrl, String apiKey, String apiSecret,
                                        String calleeName, ArrayList<String> calleeProfile, String localAccountId);

        void onJanusCredentialsFailure();

    }

}