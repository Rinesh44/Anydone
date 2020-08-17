// Generated by Dagger (https://google.github.io/dagger).
package com.treeleaf.anydone.serviceprovider.injection.module;

import android.app.Activity;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class ActivityModule_ProvidesActivityFactory implements Factory<Activity> {
  private final ActivityModule module;

  public ActivityModule_ProvidesActivityFactory(ActivityModule module) {
    this.module = module;
  }

  @Override
  public Activity get() {
    return provideInstance(module);
  }

  public static Activity provideInstance(ActivityModule module) {
    return proxyProvidesActivity(module);
  }

  public static ActivityModule_ProvidesActivityFactory create(ActivityModule module) {
    return new ActivityModule_ProvidesActivityFactory(module);
  }

  public static Activity proxyProvidesActivity(ActivityModule instance) {
    return Preconditions.checkNotNull(
        instance.providesActivity(), "Cannot return null from a non-@Nullable @Provides method");
  }
}