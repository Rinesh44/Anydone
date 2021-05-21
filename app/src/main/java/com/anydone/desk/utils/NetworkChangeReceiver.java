package com.anydone.desk.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.eclipse.paho.client.mqttv3.MqttException;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public ConnectivityReceiverListener connectivityReceiverListener;

    public NetworkChangeReceiver(ConnectivityReceiverListener connectivityReceiverListener) {
        this.connectivityReceiverListener = connectivityReceiverListener;
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);
        if (connectivityReceiverListener != null) {
            try {
                connectivityReceiverListener.onNetworkConnectionChanged(status);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(int isConnected) throws MqttException;
    }

}
