package com.treeleaf.januswebrtc;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
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
import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.DrawMetadata;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.MetaDataUpdateListener;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Position;
import com.treeleaf.freedrawingdemo.freedrawing.util.DrawPadUtil;
import com.treeleaf.freedrawingdemo.freedrawing.util.ForwardTouchesView;
import com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionParameters;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionEvents;
import com.treeleaf.januswebrtc.audio.AppRTCAudioManager;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;
import com.treeleaf.januswebrtc.JoineeListAdapter.Mode;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ALL;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.SHOW_ONE;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.TYPE.LOCAL;
import static com.treeleaf.freedrawingdemo.freedrawing.util.TreeleafDrawPadView.TYPE.REMOTE;
import static com.treeleaf.januswebrtc.Const.CALLEE_NAME;
import static com.treeleaf.januswebrtc.Const.CALLEE_PROFILE_URL;
import static com.treeleaf.januswebrtc.Const.CLIENT;
import static com.treeleaf.januswebrtc.Const.JANUS_API_KEY;
import static com.treeleaf.januswebrtc.Const.JANUS_API_SECRET;
import static com.treeleaf.januswebrtc.Const.JANUS_CREDENTIALS_SET;
import static com.treeleaf.januswebrtc.Const.JANUS_URL;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.TRUE;
import static com.treeleaf.januswebrtc.Const.JOINEE_REMOTE;
import static com.treeleaf.januswebrtc.Const.MQTT_DISCONNECTED;
import static com.treeleaf.januswebrtc.Const.SERVER;

public class ClientActivity extends PermissionHandlerActivity implements Callback.JanusRTCInterface,
        Callback.ApiCallback, PeerConnectionEvents, Callback.ConnectionEvents {
    private static final String TAG = ClientActivity.class.getSimpleName();

    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer localRender;
    private VideoCapturer videoCapturer;
    private EglBase rootEglBase;
    private RestChannel mRestChannel;
    private TextView tvRoomNumber, tvParticipantNumber;
    private ProgressDialog progressDialog;
    private View layoutDraw;
    private ImageView fabStartDraw, imgMaximizeDraw;
    private ImageView fabDiscardDraw, fabMinimizeDraw;
    private ImageView imageViewCaptureImage;
    private ImageView imageVideoToggle, imageAudioToggle, imageScreenShot, imageSwitchCamera,
            imageEndCall, ivScreenShotImage, ibToggleJoineeList, ivSignalStrength;
    private TreeleafDrawPadView treeleafDrawPadView;
    private ForwardTouchesView forwardTouchesView;
    private ConstraintLayout clCallSettings, clCallOtions;
    private RelativeLayout rlScreenShotImage;
    private RelativeLayout rlJoineeList, rlClientRoot;
    private View viewVideoCallStart;
    private ImageView ivCalleeProfile, ivTerminateCall;
    private TextView tvCalleeName, tvReconnecting;
    private EglRenderer.FrameListener frameListener;

    private Boolean videoOff = false;
    private Boolean audioOff = false;
    private Boolean justScreenShot = false;
    private Boolean showFullList = false;
    private Boolean credentialsReceived;
    private boolean isJoineePresent;
    private boolean callTerminated = false;
    private boolean remoteImage;
    private boolean isDrawMinized = false;

    private String calleeName;
    private ArrayList<String> calleeProfile;
    private String mLocalAccountId;

    private RecyclerView rvJoinee;
    private JoineeListAdapter joineeListAdapter;
    private static Callback.HostActivityCallback mhostActivityCallback;
    private static Callback.DrawCallBack mDrawCallback;
    private VideoCallListener videoCallListener;
    private Callback.DrawPadEventListener clientDrawingPadEventListener;
    private Handler handler;
    private Runnable runnable;
    private int timerDelay = 10000;
    private String roomNumber;
    private AppRTCAudioManager audioManager;
    private DrawMetadata drawMetaDataLocal;
    private MetaDataUpdateListener metaDataUpdateListener;
    private CaptureDrawParam captureDrawParam;

    private int localDeviceHeight, localDeviceWidth;
    private int remoteDeviceHeight, remoteDeviceWidth;
    private Mode mode = Mode.VIDEO_STREAM;
    private RelativeLayout rlMultipleCalleeView;
    private ImageView ivCalleeFirst, ivCalleeSecond;
    private FrameLayout flExtraCalleePic;
    private TextView tvExtraCalleeNumber;
    private CardView cvSingleCalleeView;
    private TextView tvDrawCoordinate;

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
                              Callback.DrawCallBack drawCallBack, String calleeName, ArrayList<String> callProfileUrl) {
        mhostActivityCallback = hostActivityCallBack;
        mDrawCallback = drawCallBack;
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
        tvParticipantNumber = findViewById(R.id.tv_participant_number);
        layoutDraw = findViewById(R.id.layout_draw);
        fabStartDraw = findViewById(R.id.fab_start_draw);
        fabDiscardDraw = findViewById(R.id.fab_discard_draw);
        fabMinimizeDraw = findViewById(R.id.fab_minimize_draw);
        imgMaximizeDraw = findViewById(R.id.img_maximize_draw);
        imageViewCaptureImage = findViewById(R.id.iv_captured_image);
        treeleafDrawPadView = findViewById(R.id.treeleaf_draw_pad);
        localRender = findViewById(R.id.local_video_view);
        clCallSettings = findViewById(R.id.cl_call_setting);
        clCallOtions = findViewById(R.id.cl_call_options);
        imageVideoToggle = findViewById(R.id.image_video);
        tvDrawCoordinate = findViewById(R.id.tv_draw_coordinate);
        imageAudioToggle = findViewById(R.id.image_mic);
        imageScreenShot = findViewById(R.id.image_screenshot);
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

        imageVideoToggle.setOnClickListener(videoToggleClickListener);
        imageAudioToggle.setOnClickListener(audioToggleClickListener);
        imageScreenShot.setOnClickListener(screenShotClickListener);
        imageSwitchCamera.setOnClickListener(switchCameraClickListener);
        imageEndCall.setOnClickListener(endCallClickListener);
        ivTerminateCall.setOnClickListener(endCallClickListener);
        ibToggleJoineeList.setOnClickListener(joineeListToggleListener);

        forwardTouchesView = findViewById(R.id.forward_touch);

        forwardTouchesView.setForwardTo(treeleafDrawPadView);
        fabStartDraw.setOnClickListener(startDrawClickListener);
        fabDiscardDraw.setOnClickListener(discardDrawClickListener);
        fabMinimizeDraw.setOnClickListener(minimizeDrawClickListener);
        imgMaximizeDraw.setOnClickListener(maximizeDrawClickListener);

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
                            imageViewCaptureImage.setImageBitmap(bitmap);
                            if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                                mDrawCallback.onHoldDraw();
                                mDrawCallback.onNewImageFrameCaptured(bitmap);
                            }
                        }

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
            public void onMqttConnectionChanged(String status) {
                tvReconnecting.setVisibility(status.equals(MQTT_DISCONNECTED) ? VISIBLE : GONE);
            }

            @Override
            public void onJoineeReceived(String joineeName, String joineedProfileUrl, String accountId, String joineeType) {
                //add new joinee information in view
                Log.d(TAG, "onjoineereceived");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        joineeListAdapter.addNewJoinee(new Joinee(joineeName, joineedProfileUrl, accountId), showFullList);
                        if (joineeType.equals(JOINEE_REMOTE)) {
                            treeleafDrawPadView.addNewDrawView(accountId, ClientActivity.this);
                        }
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

        };

        clientDrawingPadEventListener = new Callback.DrawPadEventListener() {

            @Override
            public void onDrawNewImageCaptured(int width, int height, long captureTime, byte[] convertedBytes, String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap receivedBitmap = BitmapFactory.decodeByteArray(convertedBytes, 0, convertedBytes.length);
                        imageViewCaptureImage.setImageBitmap(receivedBitmap);
                        treeleafDrawPadView.addViewToDrawOver(imageViewCaptureImage);
                        treeleafDrawPadView.showLocalAndRemoteViews();
                    }
                });

                if (mDrawCallback != null) {
                    localDeviceWidth = VideoCallUtil.getDeviceResolution(ClientActivity.this)[0];
                    localDeviceHeight = VideoCallUtil.getDeviceResolution(ClientActivity.this)[1];
                    remoteDeviceWidth = width;
                    remoteDeviceHeight = height;
                    mDrawCallback.onNewImageAcknowledge(localDeviceWidth, localDeviceHeight, System.currentTimeMillis());
                }
            }

            @Override
            public void onDrawRemoteDeviceConfigReceived(int width, int height, long timeStamp, String accountId) {
                remoteDeviceWidth = width;
                remoteDeviceHeight = height;
                localDeviceWidth = VideoCallUtil.getDeviceResolution(ClientActivity.this)[0];
                localDeviceHeight = VideoCallUtil.getDeviceResolution(ClientActivity.this)[1];
//                TextView tvRemoteConfig = findViewById(R.id.tv_remote_config);
//                TextView tvLocalConfig = findViewById(R.id.tv_local_config);
//                tvRemoteConfig.setText(remoteDeviceWidth + " X " + remoteDeviceHeight);
//                tvLocalConfig.setText(localDeviceWidth + " X " + localDeviceHeight);
            }

            @Override
            public void onDrawDisplayCapturedImage(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mode = Mode.IMAGE_DRAW;
                        joineeListAdapter.notifyDataSetChanged();
                        showHideDrawView(true);
                        remoteImage = true;
                        showHideDiscardDrawButton();
                    }
                });
            }

            @Override
            public void onDrawDiscard(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DrawPadUtil.hideKeyboard(findViewById(android.R.id.content).getRootView(), ClientActivity.this);
                        showHideDrawView(false);
                        joineeListAdapter.makeAllJoineesVisible();
                        mode = Mode.VIDEO_STREAM;
                        joineeListAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onDrawHideProgress() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                    }
                });
            }

            @Override
            public void onDrawShowProgress() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressBar("Please Wait...");
                    }
                });
            }

            @Override
            public void onDrawTouchDown(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteTouchDown(accountId);
                        joineeListAdapter.highlightCurrentDrawer(accountId, true,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor());
                    }
                });

            }

            @Override
            public void onDrawTouchMove(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteTouchMove(accountId);
                    }
                });
            }

            @Override
            public void onDrawTouchUp(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteTouchUp(accountId);
                        joineeListAdapter.highlightCurrentDrawer(accountId, false,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor());
                    }
                });

            }

            @Override
            public void onDrawReceiveNewTextField(float x, float y, String editTextFieldId, String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteAddEditText(x, y, editTextFieldId,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor(), accountId);
                        highlightDrawerForTextEdit(accountId,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor());
                    }
                });

            }

            @Override
            public void onDrawReceiveNewTextChange(String text, String id, String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteChangedEditText(text, id, accountId);
                        highlightDrawerForTextEdit(accountId,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor());
                    }
                });

            }

            @Override
            public void onDrawReceiveEdiTextRemove(String editTextId, String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteRemoveEditText(editTextId, accountId);
                        highlightDrawerForTextEdit(accountId,
                                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata().getTextColor());
                    }
                });

            }

            /**
             * this callback has be called every time draw coordinates is changed
             * and should be called everytime before onDrawTouchDown and onDrawTouchMove
             * @param x
             * @param y
             */
            @Override
            public void onDrawNewDrawCoordinatesReceived(Float x, Float y, String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvDrawCoordinate.setText(x + ", " + y);
                    }
                });
                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId).getDrawMetadata()
                        .setCurrentDrawPosition(new Position(x, y));
            }

            @Override
            public void onDrawParamChanged(CaptureDrawParam captureDrawParam, String accountId) {
                treeleafDrawPadView.getRemoteDrawerFromAccountId(accountId)
                        .setDrawMetadata(VideoCallUtil.getDrawMetaData(captureDrawParam));
            }

            @Override
            public void onDrawCanvasCleared(String accountId) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        treeleafDrawPadView.onRemoteClearCanvas(accountId);
                    }
                });
            }

        };

        setUpRecyclerView();
        setUpNetworkStrengthHandler();
        if (mhostActivityCallback != null) {
            mhostActivityCallback.passJoineeReceivedCallback(videoCallListener);
            mhostActivityCallback.passDrawPadEventListenerCallback(clientDrawingPadEventListener);
            mhostActivityCallback.fetchJanusServerInfo();
            mhostActivityCallback.specifyRole(RestChannel.Role.CLIENT);
        }


        //if condition here
        credentialsReceived = getIntent().getBooleanExtra(JANUS_CREDENTIALS_SET, false);
        calleeName = getIntent().getStringExtra(CALLEE_NAME);
        calleeProfile = getIntent().getStringArrayListExtra(CALLEE_PROFILE_URL);
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

        drawMetaDataLocal = new DrawMetadata();

        metaDataUpdateListener = new MetaDataUpdateListener() {
            @Override
            public void setDrawMode(TreeleafDrawPadView.DrawMode drawMode) {
                drawMetaDataLocal.setDrawMode(drawMode);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                }
            }

            @Override
            public void setBrushWidth(Float width) {
                drawMetaDataLocal.setBrushWidth(width);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onDrawParamChanged(captureDrawParam);
                }
            }

            @Override
            public void setBrushOpacity(Integer opacity) {
                drawMetaDataLocal.setBrushOpacity(opacity);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onDrawParamChanged(captureDrawParam);
                }
            }

            @Override
            public void setBrushColor(Integer color) {
                drawMetaDataLocal.setBrushColor(color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onDrawParamChanged(captureDrawParam);
                }
            }

            @Override
            public void setTextColor(Integer color) {
                drawMetaDataLocal.setTextColor(color);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onDrawParamChanged(captureDrawParam);
                }
            }

            @Override
            public void onClearCanvasPressed(Boolean pressed) {
                drawMetaDataLocal.setClearCanvas(pressed);
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onDrawCanvasCleared();
                }
            }

            @Override
            public void onStartDrawing(float x, float y) {
                Log.d(TAG, "onStartDrawing: " + x + " " + y);
                drawMetaDataLocal.setCurrentDrawPosition(new Position(x, y));
                isJoineePresent = joineeListAdapter.isJoineePresent();
                if (mDrawCallback != null && isJoineePresent) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onStartDraw(x, y);
                }
                if (mLocalAccountId != null)
                    joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, true, drawMetaDataLocal.getTextColor());
            }

            @Override
            public void onEndDrawing() {
                Log.d(TAG, "onEndDrawing: ");
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onClientTouchUp();
                }
                if (mLocalAccountId != null)
                    joineeListAdapter.highlightCurrentDrawer(mLocalAccountId, false, drawMetaDataLocal.getTextColor());
            }

            @Override
            public void onReceiveNewDrawingPosition(float x, float y) {
                Log.d(TAG, "onReceiveNewDrawingPosition: " + x + " " + y);
                tvDrawCoordinate.setText(x + ", " + y);
                drawMetaDataLocal.setCurrentDrawPosition(new Position(x, y));
                if (mDrawCallback != null && isJoineePresent) {
                    captureDrawParam = VideoCallUtil.getCaptureDrawParams(drawMetaDataLocal);
                    mDrawCallback.onClientTouchMove(captureDrawParam);
                }

            }

            @Override
            public void onReceiveNewTextField(float x, float y, String editTextFieldId) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent()) {
                    mDrawCallback.onReceiveNewTextField(x, y, editTextFieldId);
                }
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetaDataLocal.getTextColor());
            }

            @Override
            public void onReceiveNewTextChange(String text, String id) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent())
                    mDrawCallback.onReceiveNewTextChange(text, id);
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetaDataLocal.getTextColor());
            }

            @Override
            public void onReceiveEdiTextRemove(String editTextId) {
                if (mDrawCallback != null && joineeListAdapter.isJoineePresent())
                    mDrawCallback.onReceiveEdiTextRemove(editTextId);
                if (mLocalAccountId != null)
                    highlightDrawerForTextEdit(mLocalAccountId, drawMetaDataLocal.getTextColor());
            }

        };
        treeleafDrawPadView.setMetaDataUpdateListener(metaDataUpdateListener);

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
        if (calleeProfile != null && !calleeProfile.isEmpty()) {
            if (calleeProfile.size() == 1) {
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
                cvSingleCalleeView.setVisibility(GONE);
                rlMultipleCalleeView.setVisibility(VISIBLE);
                displayTwoProfileIcons(calleeProfile);
                flExtraCalleePic.setVisibility(GONE);
            } else if (calleeProfile.size() > 2) {
                cvSingleCalleeView.setVisibility(GONE);
                rlMultipleCalleeView.setVisibility(VISIBLE);
                displayTwoProfileIcons(calleeProfile);
                flExtraCalleePic.setVisibility(VISIBLE);
                tvExtraCalleeNumber.setText("+" + (calleeProfile.size() - 2));
            } else {
                cvSingleCalleeView.setVisibility(VISIBLE);
                rlMultipleCalleeView.setVisibility(GONE);
                flExtraCalleePic.setVisibility(GONE);
            }
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
                viewVideoCallStart.setVisibility(visible ? View.VISIBLE : GONE);
            }
        });
    }

    @Override
    public void onPublisherVideoStarted() {
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
                        String accountId = joinee.getAccountId();
                        if (mode.equals(Mode.IMAGE_DRAW)) {
                            treeleafDrawPadView.switchDrawPadOfIndividualUsers((mLocalAccountId.equals(accountId) ? LOCAL : REMOTE),
                                    joinee.isSoloDrawing() ? SHOW_ALL : SHOW_ONE, accountId);
                            joineeListAdapter.onJoineeItemClicked(accountId);
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
            peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender, videoCapturer,
                    handleId, "client", true);
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
        /**
         * after subscriber is joined.
         */

        videoCapturer = createVideoCapturer();
        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), null, videoCapturer,
                handleId, SERVER, false);

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
        mRestChannel.subscriberCreateHandle(participantId);
    }

    @Override
    public BigInteger getRoomNumber() {
        return new BigInteger(roomNumber);
    }

    @Override
    public BigInteger getParticipantId() {
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
//                connection.videoTrack.addRenderer(new VideoRenderer(remoteRender));
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
    }

    View.OnClickListener startDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isDrawMinized) {
                maximizeCurrentDrawing();
            } else {
                remoteImage = false;
                mode = Mode.IMAGE_DRAW;
                joineeListAdapter.notifyDataSetChanged();
                showHideDrawView(true);
                showHideDiscardDrawButton();
                //take screenshot

                localRender.addFrameListener(frameListener, 1);
                treeleafDrawPadView.addViewToDrawOver(imageViewCaptureImage);
                treeleafDrawPadView.showLocalAndRemoteViews();
            }
        }
    };

    View.OnClickListener discardDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mode = Mode.VIDEO_STREAM;
            joineeListAdapter.notifyDataSetChanged();
            DrawPadUtil.hideKeyboard(v.getRootView(), ClientActivity.this);
            showHideDrawView(false);
            joineeListAdapter.makeAllJoineesVisible();
            if (mDrawCallback != null) {
                mDrawCallback.onDiscardDraw();//TODO: uncomment this later
                mDrawCallback.onHoldDraw();
            }
        }
    };

    private void switchDrawModeAndVideoMode(boolean drawMode) {
        showHideDiscardDrawButton();
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
            minimizeCurrentDrawing();
        }
    };

    View.OnClickListener maximizeDrawClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            maximizeCurrentDrawing();
        }
    };

    private void minimizeCurrentDrawing() {
        mode = Mode.VIDEO_STREAM;
        joineeListAdapter.notifyDataSetChanged();
        isDrawMinized = true;
        switchDrawModeAndVideoMode(false);
        VideoCallUtil.materialContainerTransformVisibility(layoutDraw, imgMaximizeDraw, rlClientRoot);
    }

    private void maximizeCurrentDrawing() {
        mode = Mode.IMAGE_DRAW;
        joineeListAdapter.notifyDataSetChanged();
        isDrawMinized = false;
        switchDrawModeAndVideoMode(true);
        VideoCallUtil.materialContainerTransformVisibility(imgMaximizeDraw, layoutDraw, rlClientRoot);
    }

    private void showHideDrawView(Boolean showDrawView) {
        layoutDraw.setVisibility(showDrawView ? View.VISIBLE : GONE);
        fabDiscardDraw.setVisibility(showDrawView ? View.VISIBLE : GONE);

        clCallSettings.setVisibility(showDrawView ? GONE : View.VISIBLE);
        clCallOtions.setVisibility(showDrawView ? GONE : View.VISIBLE);

        if (!showDrawView) {
            imgMaximizeDraw.setVisibility(GONE);
            isDrawMinized = false;
        }

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

    private void showHideDiscardDrawButton() {
        //dont show discard button if image comes from remote
        fabDiscardDraw.setVisibility(remoteImage ? GONE : View.VISIBLE);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!callTerminated) {
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
                    if (audioManager != null) {
                        audioManager.stop();
                        audioManager = null;
                    }
                    callTerminated = true;
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

    public interface VideoCallListener extends Callback.AudioVideoCallbackListener {

        void onJanusCredentialsReceived(String baseUrl, String apiKey, String apiSecret,
                                        String calleeName, ArrayList<String> calleeProfile, String localAccountId);

        void onJanusCredentialsFailure();

    }

}