package com.treeleaf.anydone.serviceprovider.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.NotificationRpcProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.NOTIFICATION_CLIENT_ID;
import static com.treeleaf.anydone.serviceprovider.utils.Constants.NOTIFICATION_LOCAL_ACCOUNT_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_CONTEXT;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_REFERENCE_ID;

public class NotificationCancelListener extends BroadcastReceiver {

    public static final String TAG = NotificationCancelListener.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(intent.getExtras().getInt("id"));//TODO: for some reason cancelling notification with id is not working
        ForegroundNotificationService.removeCallNotification(context);

        //TODO: uncomment this later
        /**
         * TODO: uncomment this later
         * make api in order to notify call is cancelled by receiver
         */
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String clientId = intent.getExtras().getString(NOTIFICATION_CLIENT_ID);
        String refId = intent.getExtras().getString(NOTIFICATION_REFERENCE_ID);
        String localAccountId = intent.getExtras().getString(NOTIFICATION_LOCAL_ACCOUNT_ID);
        String callerContext = intent.getExtras().getString(NOTIFICATION_CALLER_CONTEXT);




        SignalingProto.Receiver rtcServiceBaseRequest = RtcServiceRpcProto.RtcServiceBaseRequest.newBuilder()
                .setac
                .build();

        String token = Hawk.get(Constants.TOKEN);
        service.declineCallNotification(token, callerContext, rtcServiceBaseRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RtcServiceRpcProto.RtcServiceBaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {





                    }

                    @Override
                    public void onNext(RtcServiceRpcProto.RtcServiceBaseResponse rtcServiceBaseResponse) {
                        Log.d(TAG, "call decline api: " + rtcServiceBaseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });

    }
}
