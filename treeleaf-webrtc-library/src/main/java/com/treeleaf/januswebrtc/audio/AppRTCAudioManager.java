package com.treeleaf.januswebrtc.audio;


import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import org.webrtc.ThreadUtils;

import static android.media.AudioManager.MODE_IN_CALL;

public class AppRTCAudioManager {
    private static final String TAG = AppRTCAudioManager.class.getSimpleName();
    public static Boolean loudSpeakerOn;
    private AudioManager audioManager;

    private int savedAudioMode = AudioManager.MODE_INVALID;

    public static AppRTCAudioManager create(Context context) {
        loudSpeakerOn = false;
        return new AppRTCAudioManager(context);
    }

    private AppRTCAudioManager(Context context) {
        Log.d(TAG, "ctor");
        ThreadUtils.checkIsOnMainThread();
        audioManager = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
    }

    public void start() {
        Log.d(TAG, "start");
        ThreadUtils.checkIsOnMainThread();
        Log.d(TAG, "AudioManager starts...");
        audioManager.setMode(loudSpeakerOn ? AudioManager.MODE_IN_COMMUNICATION : MODE_IN_CALL);
        Log.d(TAG, "AudioManager started");

        audioManager.setSpeakerphoneOn(loudSpeakerOn);
        audioManager.setBluetoothScoOn(true);
    }

    public void toggleSpeaker() {
        loudSpeakerOn = !loudSpeakerOn;
        start();
    }

    public void stop() {
        Log.d(TAG, "stop");
        ThreadUtils.checkIsOnMainThread();
        audioManager.setMode(savedAudioMode);
        Log.d(TAG, "AudioManager stopped");
    }

}
