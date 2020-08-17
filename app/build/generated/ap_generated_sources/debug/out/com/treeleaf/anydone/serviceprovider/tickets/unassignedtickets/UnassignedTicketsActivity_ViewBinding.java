// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets.unassignedtickets;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UnassignedTicketsActivity_ViewBinding implements Unbinder {
  private UnassignedTicketsActivity target;

  @UiThread
  public UnassignedTicketsActivity_ViewBinding(UnassignedTicketsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UnassignedTicketsActivity_ViewBinding(UnassignedTicketsActivity target, View source) {
    this.target = target;

    target.rvAssignableTickets = Utils.findRequiredViewAsType(source, R.id.rv_assignable_tickets, "field 'rvAssignableTickets'", RecyclerView.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UnassignedTicketsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvAssignableTickets = null;
    target.ivDataNotFound = null;
  }
}
