package com.treeleaf.anydone.serviceprovider.mqtt;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.InputStream;
import java.util.UUID;

import static com.treeleaf.anydone.serviceprovider.utils.Constants.MQTT_LOG;

/**
 * Created by Dipak Malla
 * Email dpakmalla@gmail.com
 * Created on 2/15/19.
 */
public class TreeleafMqttClient {
    private static final String TAG = "TreeleafMqttClient";
    private static final String MQTT_SSL_KEY_PASSWORD = Constants.MQTT_SSL_KEY_PASSWORD;
    private static final int MAX_INFLIGHT = Constants.MAX_INFLIGHT;
    public static MqttClientPersistence mqttClientPersistence;
    public static MqttAndroidClient mqttClient;
    private static final int DEFAULT_QOS = 1;
    public static OnMQTTConnected mqttListener;
//    private static Map<String, Processor> processors = new ConcurrentHashMap<>();

    public TreeleafMqttClient() {
        //Empty on purpose
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static boolean start(final Context context, boolean prodEnv,
                                TreeleafMqttCallback callback) {
        final InputStream MQTT_CA_CERT = AnyDoneServiceProviderApplication.getContext()
                .getResources().openRawResource(AnyDoneServiceProviderApplication.getContext()
                        .getResources().getIdentifier("cacert", "raw",
                                AnyDoneServiceProviderApplication.getContext().getPackageName()));
        final InputStream MQTT_CLIENT_CERT = AnyDoneServiceProviderApplication.getContext()
                .getResources().openRawResource(AnyDoneServiceProviderApplication.getContext()
                        .getResources().getIdentifier("client_cert", "raw",
                                AnyDoneServiceProviderApplication.getContext().getPackageName()));
        final InputStream MQTT_SSL_KEY = AnyDoneServiceProviderApplication.getContext()
                .getResources().openRawResource(AnyDoneServiceProviderApplication.getContext()
                        .getResources().getIdentifier("client_key", "raw",
                                AnyDoneServiceProviderApplication.getContext().getPackageName()));
        final String MQTT_URI;
        final String MQTT_USER;
        final String MQTT_PASSWORD;
        if (prodEnv) {
            MQTT_URI = Constants.MQTT_URI_PROD;
            MQTT_USER = Constants.MQTT_USER_PROD;
            MQTT_PASSWORD = Constants.MQTT_PASSWORD_PROD;
        } else {
            MQTT_URI = Constants.MQTT_URI;
            MQTT_USER = Constants.MQTT_USER;
            MQTT_PASSWORD = Constants.MQTT_PASSWORD;
        }

        try {
            GlobalUtils.showLog(TAG, "Connecting to mqtt with URI : {}" + MQTT_URI);
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(false);
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
            connectOptions.setSocketFactory(TreeleafSSL.getSocketFactory(MQTT_CA_CERT,
                    MQTT_CLIENT_CERT, MQTT_SSL_KEY, MQTT_SSL_KEY_PASSWORD));
            connectOptions.setUserName(MQTT_USER);
            connectOptions.setPassword(MQTT_PASSWORD.toCharArray());
            GlobalUtils.showLog(TAG,
                    "Connecting mqtt with provided username and password");
            mqttClientPersistence = new MemoryPersistence();
            mqttClient = new MqttAndroidClient(context, MQTT_URI,
                    UUID.randomUUID().toString().replace("-", ""),
                    mqttClientPersistence);
            if (null != callback) {
                mqttClient.setCallback(callback);
            }
            connectOptions.setMaxInflight(MAX_INFLIGHT);
            final IMqttToken token = mqttClient.connect(connectOptions);
            GlobalUtils.showLog(TAG, "Initializing connection to mqtt on URI : {}" + MQTT_URI);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "MQTT connected");
                    if (mqttListener != null)
                        mqttListener.mqttConnected();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "Failed to connect MQTT: " +
                            exception.toString());
                    if (mqttListener != null)
                        mqttListener.mqttNotConnected();
                }
            });

            return true;
        } catch (Exception e) {
            GlobalUtils.showLog(TAG, "Error while connecting mqtt client " + e);
            return false;
        }
    }

    public static MqttAndroidClient getMqttClient() {
        return mqttClient;
    }

    public static void stop() throws MqttException {
        if (null != mqttClient) {
            try {
                mqttClient.disconnect();
            } catch (MqttException ignore) {
                //Empty on purpose
            }
            mqttClient.close();
        }
        if (null != mqttClientPersistence) {
            try {
                mqttClientPersistence.clear();
            } catch (MqttPersistenceException ignore) {
                //Empty on purpose
            }
            try {
                mqttClientPersistence.close();
            } catch (MqttPersistenceException ignore) {
                //Empty on purpose
            }
        }
    }

    public static void publish(String topic, byte[] payload, TreeleafMqttCallback callback) {
        try {
            if (!mqttClient.isConnected()) {
                GlobalUtils.showLog(TAG, "Mqtt is not connected.");
                return;
            }

            IMqttToken pubToken = mqttClient.publish(topic, payload, DEFAULT_QOS, false);

            pubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "message publish success");
                    GlobalUtils.showLog(MQTT_LOG, "publish onsuccess");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "failed to publish msg" + exception.toString());
                    GlobalUtils.showLog(MQTT_LOG, "publish onfailure");
                }
            });

            if (null != callback) {
                mqttClient.setCallback(callback);
            }

        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static void subscribe(String topic, TreeleafMqttCallback callback) throws MqttException {
        try {
            if (mqttClient != null) {
                mqttClient.subscribe(topic, DEFAULT_QOS, callback::messageArrived);
                mqttClient.setCallback(callback);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public boolean unsubscribe(String topic) {
        try {
            IMqttToken unSubToken = mqttClient.unsubscribe(topic);
            unSubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "Unsubscribe success");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "failed to unsubscribe" + exception.toString());
                }
            });
//            processors.remove(topic);

            return true;
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void disconnect() {
        try {
            if (mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
            IMqttToken disToken = mqttClient.disconnect();
            disToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    GlobalUtils.showLog(TAG, "Disconnected gracefully from MQTT: ");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    GlobalUtils.showLog(TAG, "Failed to disconnect from MQTT: ");
                    GlobalUtils.showLog(TAG, exception.toString());
                }
            });
//            processors.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*    public interface Processor {
        void process(MqttClient.Payload payload);
    }*/

    public interface OnMQTTConnected {
        void mqttConnected();

        void mqttNotConnected();
    }

    public static void setOnMqttConnectedListener(OnMQTTConnected listener) {
        mqttListener = listener;
    }
}