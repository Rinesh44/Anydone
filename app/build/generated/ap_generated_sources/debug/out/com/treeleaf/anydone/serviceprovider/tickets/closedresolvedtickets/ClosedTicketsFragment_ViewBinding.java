// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets.closedresolvedtickets;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ClosedTicketsFragment_ViewBinding implements Unbinder {
  private ClosedTicketsFragment target;

  @UiThread
  public ClosedTicketsFragment_ViewBinding(ClosedTicketsFragment target, View source) {
    this.target = target;

    target.rvClosedTickets = Utils.findRequiredViewAsType(source, R.id.rv_closed_tickets, "field 'rvClosedTickets'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_closed_tickets, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.pb_search, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ClosedTicketsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvClosedTickets = null;
    target.swipeRefreshLayout = null;
    target.ivDataNotFound = null;
    target.progressBar = null;
  }
}
