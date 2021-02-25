package com.treeleaf.januswebrtc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialArcMotion;
import com.google.android.material.transition.MaterialContainerTransform;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.DrawMetadata;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Picture;
import com.treeleaf.freedrawingdemo.freedrawing.drawmetadata.Position;
import com.treeleaf.januswebrtc.draw.CaptureDrawParam;

import java.util.Random;

import static com.treeleaf.januswebrtc.Const.PICTURE_COUNT_MAX;

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

    public static Picture updatePictureContents(Picture newPicture) {
        Picture oldPicture = new Picture(newPicture.getPictureIndex());
        oldPicture.setPictureIndex(newPicture.getPictureIndex());
        oldPicture.setPictureId(newPicture.getPictureId());
        oldPicture.setBitmap(newPicture.getBitmap());
        oldPicture.setPictureOwnerId(newPicture.getPictureOwnerId());
        oldPicture.setAccountId(newPicture.getPictureOwnerId());
        oldPicture.setRequestedForCollab(newPicture.isRequestedForCollab());//
        oldPicture.setNewArrival(newPicture.isNewArrival());
        oldPicture.setOnScreen(newPicture.isOnScreen());
        return oldPicture;
    }

    public static Picture updatePictureContents(Picture oldPicture, Picture newPicture) {
        oldPicture.setPictureIndex(newPicture.getPictureIndex());
        oldPicture.setPictureId(newPicture.getPictureId());
        oldPicture.setBitmap(newPicture.getBitmap());
        oldPicture.setPictureOwnerId(newPicture.getPictureOwnerId());
        oldPicture.setAccountId(newPicture.getPictureOwnerId());
        oldPicture.setRequestedForCollab(newPicture.isRequestedForCollab());//
        oldPicture.setNewArrival(newPicture.isNewArrival());
        oldPicture.setOnScreen(newPicture.isOnScreen());
        return oldPicture;
    }


    public static float[] adjustPixelResolution(int localWidth, int localHeight,
                                                int remoteWidth, int remoteHeight,
                                                float x, float y) {
        float x_ = ((float) remoteWidth / (float) localWidth) * x;
        float y_ = ((float) remoteHeight / (float) localHeight) * y;
        return new float[]{x_, y_};
    }

    public static float adjustXPixelResolutionInLocalDevice(int localWidth, int remoteWidth, float x) {
        float x_ = ((float) localWidth / (float) remoteWidth) * x;
        return x_;
    }

    public static float adjustYPixelResolutionInLocalDevice(int localHeight, int remoteHeight, float y) {
        float y_ = ((float) localHeight / (float) remoteHeight) * y;
        return y_;
    }

    public static float normalizeXCoordinatePrePublish(float localX, int localWidth) {
        float x = localX / (float) localWidth;
        return x;
    }

    public static float normalizeYCoordinatePrePublish(float localY, int localHeight) {
        float y = localY / (float) localHeight;
        return y;
    }

    public static float normalizeXCoordinatePostPublish(float remoteX, int localWidth) {
        float x = remoteX * (float) localWidth;
        return x;
    }

    public static float normalizeYCoordinatePostPublish(float remoteY, int localHeight) {
        float y = remoteY * (float) localHeight;
        return y;
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

    public static void materialContainerTransformVisibilityNoHide(View startView, View endView, ViewGroup rootView) {
        MaterialContainerTransform materialContainerTransform = new MaterialContainerTransform();
        materialContainerTransform.setStartView(startView);
        materialContainerTransform.setEndView(endView);
        materialContainerTransform.addTarget(endView);
        materialContainerTransform.setPathMotion(new MaterialArcMotion());
        materialContainerTransform.setDuration(650);
        materialContainerTransform.setScrimColor(Color.TRANSPARENT);

        TransitionManager.beginDelayedTransition(rootView, materialContainerTransform);
//        startView.setVisibility(View.GONE);
        endView.setVisibility(View.VISIBLE);
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static boolean isPictureCountExceed(int currentCount) {
        return currentCount > PICTURE_COUNT_MAX;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static Bitmap downSampleBitmap(Bitmap receivedBitmap) {
        //resize remote image
        Bitmap bitmap = receivedBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap convertedBitmap;
        try {
            convertedBitmap = getResizedBitmap(bitmap, 800);
            return convertedBitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
