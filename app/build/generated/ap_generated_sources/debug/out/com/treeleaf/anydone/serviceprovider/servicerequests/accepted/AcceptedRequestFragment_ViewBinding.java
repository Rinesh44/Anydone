// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequests.accepted;

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

public class AcceptedRequestFragment_ViewBinding implements Unbinder {
  private AcceptedRequestFragment target;

  @UiThread
  public AcceptedRequestFragment_ViewBinding(AcceptedRequestFragment target, View source) {
    this.target = target;

    target.rvAcceptedRequests = Utils.findRequiredViewAsType(source, R.id.rv_accepted_requests, "field 'rvAcceptedRequests'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_ongoing, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AcceptedRequestFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvAcceptedRequests = null;
    target.swipeRefreshLayout = null;
    target.ivDataNotFound = null;
  }
}
