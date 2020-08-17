// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.landing;

import android.view.View;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LandingActivity_ViewBinding implements Unbinder {
  private LandingActivity target;

  @UiThread
  public LandingActivity_ViewBinding(LandingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LandingActivity_ViewBinding(LandingActivity target, View source) {
    this.target = target;

    target.bottomNavigationView = Utils.findRequiredViewAsType(source, R.id.bottom_navigation_view, "field 'bottomNavigationView'", BottomNavigationView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LandingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.bottomNavigationView = null;
  }
}
