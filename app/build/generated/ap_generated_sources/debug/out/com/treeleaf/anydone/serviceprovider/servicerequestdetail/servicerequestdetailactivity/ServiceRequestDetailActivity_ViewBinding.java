// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequestdetail.servicerequestdetailactivity;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceRequestDetailActivity_ViewBinding implements Unbinder {
  private ServiceRequestDetailActivity target;

  @UiThread
  public ServiceRequestDetailActivity_ViewBinding(ServiceRequestDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ServiceRequestDetailActivity_ViewBinding(ServiceRequestDetailActivity target,
      View source) {
    this.target = target;

    target.viewPager = Utils.findRequiredViewAsType(source, R.id.pager, "field 'viewPager'", ViewPager2.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'toolbar'", Toolbar.class);
    target.tvToolbarTitle = Utils.findRequiredViewAsType(source, R.id.toolbar_title, "field 'tvToolbarTitle'", TextView.class);
    target.tvToolbarProblemStat = Utils.findRequiredViewAsType(source, R.id.toolbar_problem_stat, "field 'tvToolbarProblemStat'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ServiceRequestDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.viewPager = null;
    target.toolbar = null;
    target.tvToolbarTitle = null;
    target.tvToolbarProblemStat = null;
  }
}
