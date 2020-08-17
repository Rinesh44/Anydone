// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.location.showLocation;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShowLocationActivity_ViewBinding implements Unbinder {
  private ShowLocationActivity target;

  @UiThread
  public ShowLocationActivity_ViewBinding(ShowLocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShowLocationActivity_ViewBinding(ShowLocationActivity target, View source) {
    this.target = target;

    target.btnAddLocation = Utils.findRequiredViewAsType(source, R.id.btn_add_location, "field 'btnAddLocation'", MaterialButton.class);
    target.rvLocations = Utils.findRequiredViewAsType(source, R.id.rv_locations, "field 'rvLocations'", RecyclerView.class);
    target.rlLocationView = Utils.findRequiredViewAsType(source, R.id.rl_location_view, "field 'rlLocationView'", RelativeLayout.class);
    target.rlEmptyView = Utils.findRequiredViewAsType(source, R.id.rl_empty_view, "field 'rlEmptyView'", RelativeLayout.class);
    target.tvAddLocation = Utils.findRequiredViewAsType(source, R.id.tv_add_location, "field 'tvAddLocation'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowLocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnAddLocation = null;
    target.rvLocations = null;
    target.rlLocationView = null;
    target.rlEmptyView = null;
    target.tvAddLocation = null;
  }
}
