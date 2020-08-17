// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.picklocation;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PickLocationActivity_ViewBinding implements Unbinder {
  private PickLocationActivity target;

  @UiThread
  public PickLocationActivity_ViewBinding(PickLocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PickLocationActivity_ViewBinding(PickLocationActivity target, View source) {
    this.target = target;

    target.rvPickLocation = Utils.findRequiredViewAsType(source, R.id.rv_select_location, "field 'rvPickLocation'", RecyclerView.class);
    target.etSearchLocation = Utils.findRequiredViewAsType(source, R.id.et_search_location, "field 'etSearchLocation'", EditText.class);
    target.ivPickLocation = Utils.findRequiredViewAsType(source, R.id.iv_pick_location, "field 'ivPickLocation'", ImageView.class);
    target.ivClear = Utils.findRequiredViewAsType(source, R.id.iv_clear, "field 'ivClear'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PickLocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvPickLocation = null;
    target.etSearchLocation = null;
    target.ivPickLocation = null;
    target.ivClear = null;
  }
}
