package com.treeleaf.anydone.serviceprovider.injection;

import android.app.Activity;

import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;
import com.treeleaf.anydone.serviceprovider.injection.component.ActivityComponent;
import com.treeleaf.anydone.serviceprovider.injection.module.ActivityModule;

public final class ActivityComponentFactory {
  private ActivityComponentFactory() {
    //no op
  }

  public static ActivityComponent create(Activity activity) {
    return AnyDoneServiceProviderApplication.get(activity)
        .getApplicationComponent()
        .plus(new ActivityModule(activity));
  }
}
