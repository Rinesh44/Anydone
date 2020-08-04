package com.treeleaf.anydone.serviceprovider.mqtt;

import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;

/**
 * Created by Dipak Malla
 * Email dpakmalla@gmail.com
 * Created on 3/7/19.
 */
public abstract class TreeleafMqttCallback implements MqttCallbackExtended {
    private CallBackHook hook;
    private static final String TAG = "TreeleafMqttCallback";

    public void setHook(CallBackHook hook) {
        this.hook = hook;
    }

    @Override
    public void connectionLost(Throwable cause) {
        if (null != hook) {
            this.hook.onConnectionLost();
        }
        GlobalUtils.showLog(TAG, "Connection lost: " + cause);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        if (null != hook) {
            this.hook.onDeliveryComplete(token);
        }
    }

    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (null != hook) {
            GlobalUtils.showLog(TAG, "callback connect complete");
            this.hook.onConnected(reconnect, serverURI);
        }
    }

    @FunctionalInterface
    public interface CallBackHook {
        void onConnectionLost();

        default void onConnected(boolean reconnect, String serverURI) {
            GlobalUtils.showLog(TAG, "connection hook connected");
        }

        default void onDeliveryComplete(IMqttDeliveryToken token) {
        }
    }
}
