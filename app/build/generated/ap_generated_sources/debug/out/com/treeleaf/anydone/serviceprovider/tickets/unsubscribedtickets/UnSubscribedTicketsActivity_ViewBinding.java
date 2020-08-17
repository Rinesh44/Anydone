// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets.unsubscribedtickets;

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

public class UnSubscribedTicketsActivity_ViewBinding implements Unbinder {
  private UnSubscribedTicketsActivity target;

  @UiThread
  public UnSubscribedTicketsActivity_ViewBinding(UnSubscribedTicketsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UnSubscribedTicketsActivity_ViewBinding(UnSubscribedTicketsActivity target, View source) {
    this.target = target;

    target.rvSubscribeableTickets = Utils.findRequiredViewAsType(source, R.id.rv_unsubscribed_tickets, "field 'rvSubscribeableTickets'", RecyclerView.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    UnSubscribedTicketsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvSubscribeableTickets = null;
    target.ivDataNotFound = null;
  }
}
