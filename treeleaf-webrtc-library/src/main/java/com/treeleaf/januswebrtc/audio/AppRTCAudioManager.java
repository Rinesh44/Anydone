package com.treeleaf.januswebrtc.audio;


import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import org.webrtc.ThreadUtils;

public class AppRTCAudioManager {
    private static final String TAG = AppRTCAudioManager.class.getSimpleName();

    private AudioManager audioManager;

    private int savedAudioMode = AudioManager.MODE_INVALID;

    public static AppRTCAudioManager create(Context context) {
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
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        Log.d(TAG, "AudioManager started");

        audioManager.setSpeakerphoneOn(true);
    }

    public void stop() {
        Log.d(TAG, "stop");
        ThreadUtils.checkIsOnMainThread();
        audioManager.setMode(savedAudioMode);
        Log.d(TAG, "AudioManager stopped");
    }

}
