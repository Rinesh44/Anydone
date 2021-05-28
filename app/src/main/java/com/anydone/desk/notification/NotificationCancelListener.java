package com.anydone.desk.notification;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.entities.SignalingProto;
import com.treeleaf.anydone.rpc.RtcServiceRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.anydone.desk.utils.Constants.NOTIFICATION_CLIENT_ID;
import static com.anydone.desk.utils.Constants.NOTIFICATION_LOCAL_ACCOUNT_ID;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_INBOX;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_SERVICE_REQUEST;
import static com.anydone.desk.utils.Constants.RTC_CONTEXT_TICKET;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_CALLER_CONTEXT;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_INVITE_BY_EMPLOYEE;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_REFERENCE_ID;
import static com.treeleaf.januswebrtc.Const.NOTIFICATION_RTC_MESSAGE_ID;

public class NotificationCancelListener extends BroadcastReceiver {

    public static final String TAG = NotificationCancelListener.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(intent.getExtras().getInt("id"));//TODO: for some reason cancelling notification with id is not working
        ForegroundNotificationService.removeCallNotification(context);

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String clientId = intent.getExtras().getString(NOTIFICATION_CLIENT_ID);
        String refId = intent.getExtras().getString(NOTIFICATION_REFERENCE_ID);
        String localAccountId = intent.getExtras().getString(NOTIFICATION_LOCAL_ACCOUNT_ID);
        String callerContext = intent.getExtras().getString(NOTIFICATION_CALLER_CONTEXT);
        String inviterAccountId = intent.getExtras().getString(NOTIFICATION_INVITE_BY_EMPLOYEE);
        String rtcMessageId = intent.getExtras().getString(NOTIFICATION_RTC_MESSAGE_ID);
        Boolean isCallInvitation = (inviterAccountId != null);


        if (isCallInvitation)
            return;


        SignalingProto.ReceiverCallDeclined receiverCallDeclined = SignalingProto.ReceiverCallDeclined.newBuilder()
                .setClientId(clientId)
                .setSenderAccountId(localAccountId)
                .setRefId(refId)
                .setRtcMessageId(rtcMessageId)
                .build();

        String token = Hawk.get(Constants.TOKEN);
        String contextNumeralValue = getContextNumeralValue(callerContext);
        service.declineCallNotification(token, contextNumeralValue, receiverCallDeclined)//TODO: this is not supposed to be hard coded, tell kshitij
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


    private String getContextNumeralValue(String contextType) {
        switch (contextType) {
            case "INBOX_CONTEXT":
                return String.valueOf(AnydoneProto.ServiceContext.INBOX_CONTEXT.getNumber());
            case "TICKET_CONTEXT":
                return String.valueOf(AnydoneProto.ServiceContext.TICKET_CONTEXT.getNumber());
        }
        return String.valueOf(AnydoneProto.ServiceContext.INBOX_CONTEXT);
    }
}
