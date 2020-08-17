// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequests;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.viewpager.widget.ViewPager;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceRequestFragment_ViewBinding implements Unbinder {
  private ServiceRequestFragment target;

  private View view7f0901bf;

  @UiThread
  public ServiceRequestFragment_ViewBinding(final ServiceRequestFragment target, View source) {
    this.target = target;

    View view;
    target.mTabs = Utils.findRequiredViewAsType(source, R.id.tabs, "field 'mTabs'", TabLayout.class);
    target.mViewpager = Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'mViewpager'", ViewPager.class);
    target.llBottomSheet = Utils.findRequiredViewAsType(source, R.id.bottom_sheet, "field 'llBottomSheet'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_filter, "field 'ivFilter' and method 'filterRequests'");
    target.ivFilter = Utils.castView(view, R.id.iv_filter, "field 'ivFilter'", ImageView.class);
    view7f0901bf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.filterRequests();
      }
    });
    target.pbSearch = Utils.findRequiredViewAsType(source, R.id.pb_search, "field 'pbSearch'", ProgressBar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServiceRequestFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mTabs = null;
    target.mViewpager = null;
    target.llBottomSheet = null;
    target.ivFilter = null;
    target.pbSearch = null;

    view7f0901bf.setOnClickListener(null);
    view7f0901bf = null;
  }
}