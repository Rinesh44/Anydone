package com.treeleaf.anydone.serviceprovider.injection.module;

import android.app.Application;
import android.content.Context;

import com.treeleaf.anydone.serviceprovider.injection.annotations.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Provides Application level dependencies
 */
@Module public class ApplicationModule {

  protected final Application mApplication;

  public ApplicationModule(Application application) {
    mApplication = application;
  }

  @Provides Application provideApplication() {
    return mApplication;
  }

  @Provides @ApplicationContext
  Context provideContext() {
    return mApplication;
  }


}
