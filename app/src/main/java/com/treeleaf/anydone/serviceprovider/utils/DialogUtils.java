package com.treeleaf.anydone.serviceprovider.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.LightingColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class DialogUtils extends DialogFragment {

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_POSITIVE_BUTTON_TITLE = "positive_button_title";
    public static final String EXTRA_NEGATIVE_BUTTON_TITLE = "negative_button_title";
    public static final String EXTRA_CANCEALABLE = "canceleable";
    private static AlertDialog.Builder alertDialog;
    private DialogCallBack dialogCallBack;
    private Context mContext;

    public static DialogUtils getInstance(
            Context context,
            String title,
            String message,
            String positiveButtonTitle,
            String negativeButtonTitle,
            boolean cancelable, DialogCallBack dialogCallBack) {
        DialogUtils dialogUtils = new DialogUtils();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TITLE, title);
        bundle.putString(EXTRA_MESSAGE, message);
        bundle.putString(EXTRA_POSITIVE_BUTTON_TITLE, positiveButtonTitle);
        bundle.putString(EXTRA_NEGATIVE_BUTTON_TITLE, negativeButtonTitle);
        bundle.putBoolean(EXTRA_CANCEALABLE, cancelable);
        dialogUtils.setContext(context);
        dialogUtils.setArguments(bundle);
        dialogUtils.setCallBack(dialogCallBack);
        return dialogUtils;
    }

    public void setCallBack(DialogCallBack callBack) {
        this.dialogCallBack = callBack;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle(bundle.getString(EXTRA_TITLE));
        alertDialog.setMessage(bundle.getString(EXTRA_MESSAGE));
        alertDialog.setCancelable(bundle.getBoolean(EXTRA_CANCEALABLE));
        alertDialog.setPositiveButton(bundle.getString(EXTRA_POSITIVE_BUTTON_TITLE),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallBack != null) {
                            dialogCallBack.onPositiveButtonClicked();
                        }
                        dialog.cancel();
                    }
                });

        //TODO: you can add if condition
        /*alertDialog.setNegativeButton(bundle.getString(EXTRA_NEGATIVE_BUTTON_TITLE),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialogCallBack != null)
                            dialogCallBack.onNegativeButtonClicked();
                        dialog.cancel();
                    }
                });*/

        return alertDialog.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setBackgroundColor(getResources().getColor(com.treeleaf.januswebrtc.R.color.colorTransparent));
        ((AlertDialog) getDialog()).getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        ((AlertDialog) getDialog()).getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        ((AlertDialog) getDialog()).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources()
                .getColor(com.treeleaf.januswebrtc.R.color.colorPrimary));
        (getDialog()).getWindow().getDecorView().getBackground()
                .setColorFilter(new LightingColorFilter(0xFF000000,
                        getResources().getColor(com.treeleaf.januswebrtc.R.color.colorTransparent)));
    }

    public static class Builder {

        private Context context;
        private String title;
        private String message;
        private String positiveButtonTitle;
        private String negativeButtonTitle;
        private Boolean canceleable;
        private DialogCallBack dialogCallBack;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setPositiveButtonTitle(String positiveButtonTitle) {
            this.positiveButtonTitle = positiveButtonTitle;
            return this;
        }

        public Builder setNegativeButtonTitle(String negativeButtonTitle) {
            this.negativeButtonTitle = negativeButtonTitle;
            return this;
        }

        public Builder setCanceleable(Boolean canceleable) {
            this.canceleable = canceleable;
            return this;
        }

        public Builder setDialogCallback(DialogCallBack dialogCallBack) {
            this.dialogCallBack = dialogCallBack;
            return this;
        }

        public DialogUtils build() {
            return DialogUtils.getInstance(context, title, message, positiveButtonTitle,
                    negativeButtonTitle, canceleable, dialogCallBack);
        }
    }

    public interface DialogCallBack {

        void onPositiveButtonClicked();

        void onNegativeButtonClicked();

    }

}


