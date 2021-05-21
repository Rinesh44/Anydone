package com.anydone.desk.injection;

import android.app.Activity;

import com.anydone.desk.AnyDoneServiceProviderApplication;
import com.anydone.desk.injection.component.ActivityComponent;
import com.anydone.desk.injection.module.ActivityModule;

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
