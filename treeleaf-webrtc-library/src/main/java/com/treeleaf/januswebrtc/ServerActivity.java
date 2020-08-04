package com.treeleaf.januswebrtc;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionEvents;
import com.treeleaf.januswebrtc.PeerConnectionClient.PeerConnectionParameters;

import org.json.JSONObject;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.SessionDescription;
import org.webrtc.StatsReport;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;

import java.math.BigInteger;

import static com.treeleaf.januswebrtc.Const.JANUS_API_KEY;
import static com.treeleaf.januswebrtc.Const.JANUS_API_SECRET;
import static com.treeleaf.januswebrtc.Const.JANUS_URL;
import static com.treeleaf.januswebrtc.Const.SERVER;

public class ServerActivity extends PermissionHandlerActivity implements Callback.JanusRTCInterface, Callback.ApiCallback,
        PeerConnectionEvents, Callback.IceConnectionChangeEvents {
    private static final String TAG = ServerActivity.class.getSimpleName();

    private PeerConnectionClient peerConnectionClient;
    private PeerConnectionParameters peerConnectionParameters;

    private SurfaceViewRenderer localRender;
    private SurfaceViewRenderer remoteRender;
    private VideoCapturer videoCapturer;
    private EglBase rootEglBase;
    private RestChannel mRestChannel;
    LinearLayout rootView;
    String roomNumber;
    private ProgressDialog progressDialog;


    public static void launch(Context context, String janusServerUrl, String apiKey, String apiSecret) {
        Intent intent = new Intent(context, ServerActivity.class);
        intent.putExtra(JANUS_URL, janusServerUrl);
        intent.putExtra(JANUS_API_KEY, apiKey);
        intent.putExtra(JANUS_API_SECRET, apiSecret);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        rootView = (LinearLayout) findViewById(R.id.activity_server);
        localRender = (SurfaceViewRenderer) findViewById(R.id.local_video_view);
        remoteRender = (SurfaceViewRenderer) findViewById(R.id.remote_video_view);
        setUpProgressDialog();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomNumber = extras.getString("extra_room_number");
        }


        String baseUrl = getIntent().getStringExtra(JANUS_URL);
        String apiKey = getIntent().getStringExtra(JANUS_API_KEY);
        String apiSecret = getIntent().getStringExtra(JANUS_API_SECRET);
        mRestChannel = new RestChannel(this, baseUrl, apiKey, apiSecret);
        mRestChannel.setDelegate(this);
        mRestChannel.setApiCallback(this);

        permissionsGranted = checkPermission();
        if (permissionsGranted) {
            onPermissionGranted();
        }
    }

    private void setUpProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Finding remote peer...");
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (permissionsGranted)
            peerConnectionClient.startVideoSource();
    }

    @Override
    public void onPermissionGranted() {
        mRestChannel.initConnection(SERVER);
        createLocalRender();
        remoteRender.init(rootEglBase.getEglBaseContext(), null);
        peerConnectionParameters = new PeerConnectionParameters(false, 360, 480, 20, "H264", true, 0, "opus", false, false, false, false, false);
        peerConnectionClient = PeerConnectionClient.getInstance();
        peerConnectionClient.createPeerConnectionFactory(this, peerConnectionParameters, this);
        peerConnectionClient.setIceConnectionChangeEventNotifier(this);
        showProgressBar("Starting room configurations...");
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
            if (enumerator.isFrontFacing(deviceName)) {
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
        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender, videoCapturer, handleId, "client");
        peerConnectionClient.createOffer(handleId);
    }

    // interface JanusRTCInterface
    @Override
    public void onPublisherJoined(final BigInteger handleId) {
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
        Toast.makeText(this, "Active publisher not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void subscriberHandleRemoteJsep(BigInteger handleId, JSONObject jsep) {
        /**
         * after subscriber is joined.
         */
        updateProgressMessage("Starting peer connection...");

        videoCapturer = createVideoCapturer();
        peerConnectionClient.createPeerConnection(rootEglBase.getEglBaseContext(), localRender, videoCapturer, handleId, "server");


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
    public BigInteger getRoomNumber() {
        return new BigInteger(roomNumber);
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

    }

    @Override
    public void showVideoCallStartView(boolean visible) {

    }

    @Override
    public void updateProgressMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null)
                    progressDialog.setMessage(message);
            }
        });
    }

    @Override
    public void restError(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ServerActivity.this, message, Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        });
    }

    @Override
    public void streamUnpublished(BigInteger roomId, BigInteger publisherId) {
        //publisher unpublished the stream, publisherId is id of publisher who unpublished
        peerConnectionClient.close();
    }

    @Override
    public void janusServerConfigurationSuccess(BigInteger sessionId, BigInteger roomId, BigInteger participantId) {

    }

    @Override
    public void startCreatingOffer(BigInteger handleId) {

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
        updateProgressMessage("Create answer...");
        mRestChannel.subscriberCreateAnswer(handleId, sdp);
    }

    @Override
    public void onIceCandidate(IceCandidate candidate, BigInteger handleId) {
        Log.e(TAG, "=========onIceCandidate========");
        if (candidate != null) {
            mRestChannel.trickleCandidate(handleId, candidate);
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
        finish();
    }

    @Override
    public void onPeerConnectionStatsReady(StatsReport[] reports) {

    }

    @Override
    public void onPeerConnectionError(String description) {
        Toast.makeText(this, description, Toast.LENGTH_SHORT).show();
        hideProgressBar();
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

}
