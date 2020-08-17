// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import net.cachapa.expandablelayout.ExpandableLayout;

public class ActivityFragment_ViewBinding implements Unbinder {
  private ActivityFragment target;

  @UiThread
  public ActivityFragment_ViewBinding(ActivityFragment target, View source) {
    this.target = target;

    target.llActivities = Utils.findRequiredViewAsType(source, R.id.ll_activities, "field 'llActivities'", LinearLayout.class);
    target.tvAssignedEmployeeDropDown = Utils.findRequiredViewAsType(source, R.id.tv_assigned_employee_dropdown, "field 'tvAssignedEmployeeDropDown'", TextView.class);
    target.tvActivityDropDown = Utils.findRequiredViewAsType(source, R.id.tv_activity_dropdown, "field 'tvActivityDropDown'", TextView.class);
    target.elActivities = Utils.findRequiredViewAsType(source, R.id.expandable_layout_activities, "field 'elActivities'", ExpandableLayout.class);
    target.elEmployee = Utils.findRequiredViewAsType(source, R.id.expandable_layout_employee, "field 'elEmployee'", ExpandableLayout.class);
    target.ivDropdownEmployee = Utils.findRequiredViewAsType(source, R.id.iv_dropdown_employee, "field 'ivDropdownEmployee'", ImageView.class);
    target.ivDropdownActivity = Utils.findRequiredViewAsType(source, R.id.iv_dropdown_activity, "field 'ivDropdownActivity'", ImageView.class);
    target.llAssignedEmployee = Utils.findRequiredViewAsType(source, R.id.ll_assigned_employee, "field 'llAssignedEmployee'", LinearLayout.class);
    target.llAssignedEmployeeTop = Utils.findRequiredViewAsType(source, R.id.ll_assined_employee_top, "field 'llAssignedEmployeeTop'", LinearLayout.class);
    target.tvElapsedTime = Utils.findRequiredViewAsType(source, R.id.tv_elapsed_time, "field 'tvElapsedTime'", TextView.class);
    target.btnMarkComplete = Utils.findRequiredViewAsType(source, R.id.btn_mark_complete, "field 'btnMarkComplete'", MaterialButton.class);
    target.mBottomSheet = Utils.findRequiredViewAsType(source, R.id.bottom_sheet_profile, "field 'mBottomSheet'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ActivityFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.llActivities = null;
    target.tvAssignedEmployeeDropDown = null;
    target.tvActivityDropDown = null;
    target.elActivities = null;
    target.elEmployee = null;
    target.ivDropdownEmployee = null;
    target.ivDropdownActivity = null;
    target.llAssignedEmployee = null;
    target.llAssignedEmployeeTop = null;
    target.tvElapsedTime = null;
    target.btnMarkComplete = null;
    target.mBottomSheet = null;
  }
}
