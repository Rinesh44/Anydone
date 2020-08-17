// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequests.open;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OpenRequestFragment_ViewBinding implements Unbinder {
  private OpenRequestFragment target;

  @UiThread
  public OpenRequestFragment_ViewBinding(OpenRequestFragment target, View source) {
    this.target = target;

    target.rvOpenRequests = Utils.findRequiredViewAsType(source, R.id.rv_open_requests, "field 'rvOpenRequests'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_closed, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    OpenRequestFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvOpenRequests = null;
    target.swipeRefreshLayout = null;
    target.ivDataNotFound = null;
  }
}