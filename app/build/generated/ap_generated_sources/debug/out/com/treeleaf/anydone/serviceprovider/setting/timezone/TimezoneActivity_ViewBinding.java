// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.timezone;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TimezoneActivity_ViewBinding implements Unbinder {
  private TimezoneActivity target;

  @UiThread
  public TimezoneActivity_ViewBinding(TimezoneActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TimezoneActivity_ViewBinding(TimezoneActivity target, View source) {
    this.target = target;

    target.tvZone = Utils.findRequiredViewAsType(source, R.id.tv_zone, "field 'tvZone'", TextView.class);
    target.tvTimezone = Utils.findRequiredViewAsType(source, R.id.tv_timezone, "field 'tvTimezone'", TextView.class);
    target.rlTimezone = Utils.findRequiredViewAsType(source, R.id.rl_timezone, "field 'rlTimezone'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    TimezoneActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvZone = null;
    target.tvTimezone = null;
    target.rlTimezone = null;
  }
}
