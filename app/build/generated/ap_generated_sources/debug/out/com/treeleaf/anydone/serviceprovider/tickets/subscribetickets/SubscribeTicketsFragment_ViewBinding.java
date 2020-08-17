// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets.subscribetickets;

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

public class SubscribeTicketsFragment_ViewBinding implements Unbinder {
  private SubscribeTicketsFragment target;

  private View view7f09013a;

  @UiThread
  public SubscribeTicketsFragment_ViewBinding(final SubscribeTicketsFragment target, View source) {
    this.target = target;

    View view;
    target.rvSubscribeTickets = Utils.findRequiredViewAsType(source, R.id.rv_subscribe_tickets, "field 'rvSubscribeTickets'", RecyclerView.class);
    target.swipeRefreshLayout = Utils.findRequiredViewAsType(source, R.id.swipe_refresh_subscribe_tickets, "field 'swipeRefreshLayout'", SwipeRefreshLayout.class);
    target.ivDataNotFound = Utils.findRequiredViewAsType(source, R.id.iv_data_not_found, "field 'ivDataNotFound'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.fab_subscribe, "field 'fabSubscribe' and method 'subscribe'");
    target.fabSubscribe = Utils.castView(view, R.id.fab_subscribe, "field 'fabSubscribe'", FloatingActionButton.class);
    view7f09013a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.subscribe();
      }
    });
    target.progressBar = Utils.findRequiredViewAsType(source, R.id.pb_search, "field 'progressBar'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SubscribeTicketsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rvSubscribeTickets = null;
    target.swipeRefreshLayout = null;
    target.ivDataNotFound = null;
    target.fabSubscribe = null;
    target.progressBar = null;

    view7f09013a.setOnClickListener(null);
    view7f09013a = null;
  }
}
