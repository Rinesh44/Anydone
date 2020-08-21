// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.tickets;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TicketsFragment_ViewBinding implements Unbinder {
  private TicketsFragment target;

  private View view7f0901d8;

  private View view7f0900a0;

  @UiThread
  public TicketsFragment_ViewBinding(final TicketsFragment target, View source) {
    this.target = target;

    View view;
    target.mTabs = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'mTabs'", TabLayout.class);
    target.mViewpager = Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'mViewpager'", ViewPager.class);
    view = Utils.findRequiredView(source, R.id.iv_filter, "field 'ivFilter' and method 'filterRequests'");
    target.ivFilter = Utils.castView(view, R.id.iv_filter, "field 'ivFilter'", ImageView.class);
    view7f0901d8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.filterRequests();
      }
    });
    target.pbSearch = Utils.findRequiredViewAsType(source, R.id.pb_search, "field 'pbSearch'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.btn_add_ticket, "field 'btnAddTicket' and method 'addTicket'");
    target.btnAddTicket = Utils.castView(view, R.id.btn_add_ticket, "field 'btnAddTicket'", MaterialButton.class);
    view7f0900a0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.addTicket();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TicketsFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTabs = null;
    target.mViewpager = null;
    target.ivFilter = null;
    target.pbSearch = null;
    target.btnAddTicket = null;

    view7f0901d8.setOnClickListener(null);
    view7f0901d8 = null;
    view7f0900a0.setOnClickListener(null);
    view7f0900a0 = null;
  }
}
