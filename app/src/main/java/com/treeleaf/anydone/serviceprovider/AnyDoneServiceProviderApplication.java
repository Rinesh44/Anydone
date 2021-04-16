package com.treeleaf.anydone.serviceprovider;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.component.DaggerApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttCallback;
import com.treeleaf.anydone.serviceprovider.mqtt.TreeleafMqttClient;
import com.treeleaf.anydone.serviceprovider.realm.AnydoneRealmMigration;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.ios.IosEmojiProvider;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class AnyDoneServiceProviderApplication extends Application {
    private static final String TAG = "AnyDoneConsumerApplicat";
    private static Context context;

    private ApplicationComponent applicationComponent;
    public static String refreshedToken;

    public static AnyDoneServiceProviderApplication get(Context context) {
        return (AnyDoneServiceProviderApplication) context.getApplicationContext();
    }

    /**
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "en"));
    }

    /**
     * Provides the application context
     *
     * @return {@link Context Application Context}
     */
    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        setFirebaseToken();
        Hawk.init(this).build();
        initializeRealm();
        EmojiManager.install(new IosEmojiProvider());
    /*    RealmInspectorModulesProvider realmInspectorModulesProvider = RealmInspectorModulesProvider.builder(this)
                .withDeleteIfMigrationNeeded(true)
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(realmInspectorModulesProvider)
                    .build());
        }*/

        boolean isMqttConnected = Hawk.get(Constants.MQTT_CONNECTED, false);
        if (isMqttConnected) setUpMQTT();

    }

    public Application getApplicationObject() {
        return this;
    }

    public ApplicationComponent getApplicationComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }

    private void setUpMQTT() {
        String env = Hawk.get(Constants.BASE_URL);
        boolean prodEnv = !env.equalsIgnoreCase(Constants.DEV_BASE_URL);
        GlobalUtils.showLog(TAG, "prod env check: " + prodEnv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TreeleafMqttCallback callback = new TreeleafMqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    GlobalUtils.showLog(TAG, "mqtt topic: " + topic);
                    GlobalUtils.showLog(TAG, "mqtt message: " + message);
                }
            };

            callback.setHook(() -> {
                Toast.makeText(context, "mqtt connection lost", Toast.LENGTH_SHORT).show();
            });

            TreeleafMqttClient.start(getApplicationContext(), prodEnv, callback);
        }
    }


    private void initializeRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .schemaVersion(26) // Must be bumped when the schema changes
                .migration(new AnydoneRealmMigration()) // Migration to run
                .build();

        Realm.setDefaultConfiguration(config);
    }

    public void setFirebaseToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(instanceIdResult -> {
            refreshedToken = instanceIdResult.getToken();
            GlobalUtils.showLog(TAG, "refreshed token : " + refreshedToken);
        });
    }

    public static String getFirebaseToken() {
        return refreshedToken;
    }

}
