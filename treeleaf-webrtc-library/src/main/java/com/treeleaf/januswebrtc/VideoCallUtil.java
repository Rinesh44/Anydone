package com.treeleaf.januswebrtc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.DrawMetadata;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Position;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import java.util.Random;

public class VideoCallUtil {

    public static String generateTransactionString(Integer length) {
        final String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(rnd.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static String appendEndPoint(String baseUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append("/");
        return builder.toString();
    }

    public static CaptureDrawParam getCaptureDrawParams(DrawMetadata drawMetadata) {
        CaptureDrawParam captureDrawParam = new CaptureDrawParam();
        captureDrawParam.setBrushWidth(drawMetadata.getBrushWidth());
        captureDrawParam.setBrushOpacity(drawMetadata.getBrushOpacity());
        captureDrawParam.setBrushColor(drawMetadata.getBrushColor());
        captureDrawParam.setTextColor(drawMetadata.getTextColor());
        captureDrawParam.setXCoordinate(drawMetadata.getCurrentDrawPosition().getX());
        captureDrawParam.setYCoordinate(drawMetadata.getCurrentDrawPosition().getY());
        return captureDrawParam;
    }

    public static DrawMetadata getDrawMetaData(CaptureDrawParam captureDrawParam) {
        DrawMetadata drawMetadata = new DrawMetadata();
        drawMetadata.setBrushWidth(captureDrawParam.getBrushWidth());
        drawMetadata.setBrushOpacity(base255ToBase100(captureDrawParam.getBrushOpacity()));
        drawMetadata.setBrushColor(captureDrawParam.getBrushColor());
        drawMetadata.setTextColor(captureDrawParam.getTextColor());
        drawMetadata.setCurrentDrawPosition(new Position(captureDrawParam.getXCoordinate(),
                captureDrawParam.getYCoordinate()));
        return drawMetadata;
    }

    public static Integer base255ToBase100(Integer base255Val) {
        int alpha = (base255Val * 100) / 255;
        return alpha;
    }


    public static float[] adjustPixelResolution(int localWidth, int localHeight,
                                                int remoteWidth, int remoteHeight,
                                                float x, float y) {
        float x_ = ((float) remoteWidth / (float) localWidth) * x;
        float y_ = ((float) remoteHeight / (float) localHeight) * y;
        return new float[]{x_, y_};
    }

    public static int[] getDeviceResolution(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return new int[]{width, height};
    }

    public static void materialContainerTransformVisibility(View startView, View endView, ViewGroup rootView) {
        MaterialContainerTransform materialContainerTransform = new MaterialContainerTransform();
        materialContainerTransform.setStartView(startView);
        materialContainerTransform.setEndView(endView);
        materialContainerTransform.addTarget(endView);
        materialContainerTransform.setPathMotion(new MaterialArcMotion());
        materialContainerTransform.setDuration(650);
        materialContainerTransform.setScrimColor(Color.TRANSPARENT);

        TransitionManager.beginDelayedTransition(rootView, materialContainerTransform);
        startView.setVisibility(View.GONE);
        endView.setVisibility(View.VISIBLE);
    }


}
