// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets.assignedtickets;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AssignedTicketsFragment_ViewBinding implements Unbinder {
  private AssignedTicketsFragment target;

  private View view7f090136;

  @UiThread
  public AssignedTicketsFragment_ViewBinding(final AssignedTicketsFragment target, View source) {
    this.target = target;

    View view;
    target.rvOpenTickets = Utils.findRequiredViewAsType(source, R.id.rv_open_tickets, "field 'rvOpenTickets'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_open_tickets, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.fab_assign, "field 'fabAssign' and method 'gotoAssignableTicketList'");
    target.fabAssign = Utils.castView(view, R.id.fab_assign, "field 'fabAssign'", FloatingActionButton.class);
    view7f090136 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.gotoAssignableTicketList();
      }
    });
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.pb_search, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AssignedTicketsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvOpenTickets = null;
    target.swipeRefreshLayout = null;
    target.ivDataNotFound = null;
    target.fabAssign = null;
    target.progressBar = null;

    view7f090136.setOnClickListener(null);
    view7f090136 = null;
  }
}
