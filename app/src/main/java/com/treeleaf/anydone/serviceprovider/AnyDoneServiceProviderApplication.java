package com.treeleaf.anydone.serviceprovider;

import android.app.Application;
import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.injection.component.ApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.component.DaggerApplicationComponent;
import com.treeleaf.anydone.serviceprovider.injection.module.ApplicationModule;
import com.treeleaf.anydone.serviceprovider.utils.LocaleHelper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

public class AnyDoneServiceProviderApplication extends Application {
    private static final String TAG = "AnyDoneConsumerApplicat";
    private static Context context;

    private ApplicationComponent applicationComponent;

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

        Hawk.init(this).build();

    /*    RealmInspectorModulesProvider realmInspectorModulesProvider = RealmInspectorModulesProvider.builder(this)
                .withDeleteIfMigrationNeeded(true)
                .build();

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(realmInspectorModulesProvider)
                    .build());
        }*/

        initializeRealm();
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


    private void initializeRealm() {
        RealmUtils.init(this);
    }

}
