// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.timezone;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectTimezoneActivity_ViewBinding implements Unbinder {
  private SelectTimezoneActivity target;

  @UiThread
  public SelectTimezoneActivity_ViewBinding(SelectTimezoneActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SelectTimezoneActivity_ViewBinding(SelectTimezoneActivity target, View source) {
    this.target = target;

    target.etSearchTimezone = Utils.findRequiredViewAsType(source, R.id.et_search_timezone, "field 'etSearchTimezone'", EditText.class);
    target.rvTimezone = Utils.findRequiredViewAsType(source, R.id.rv_timezone, "field 'rvTimezone'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectTimezoneActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearchTimezone = null;
    target.rvTimezone = null;
  }
}
