// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.profile;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.card.MaterialCardView;
import com.treeleaf.anydone.serviceprovider.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ProfileActivity_ViewBinding implements Unbinder {
  private ProfileActivity target;

  @UiThread
  public ProfileActivity_ViewBinding(ProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ProfileActivity_ViewBinding(ProfileActivity target, View source) {
    this.target = target;

    target.civProfileImage = Utils.findRequiredViewAsType(source, R.id.profile_image, "field 'civProfileImage'", CircleImageView.class);
    target.rlEmail = Utils.findRequiredViewAsType(source, R.id.rl_profile_email, "field 'rlEmail'", RelativeLayout.class);
    target.tvEmail = Utils.findRequiredViewAsType(source, R.id.tv_email, "field 'tvEmail'", TextView.class);
    target.rlPhone = Utils.findRequiredViewAsType(source, R.id.rl_profile_phone, "field 'rlPhone'", RelativeLayout.class);
    target.tvPhone = Utils.findRequiredViewAsType(source, R.id.tv_phone, "field 'tvPhone'", TextView.class);
    target.rlGender = Utils.findRequiredViewAsType(source, R.id.rl_profile_gender, "field 'rlGender'", RelativeLayout.class);
    target.tvGender = Utils.findRequiredViewAsType(source, R.id.tv_gender, "field 'tvGender'", TextView.class);
    target.tvName = Utils.findRequiredViewAsType(source, R.id.tv_name, "field 'tvName'", TextView.class);
    target.mBottomSheet = Utils.findRequiredViewAsType(source, R.id.bottom_sheet, "field 'mBottomSheet'", MaterialCardView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.civProfileImage = null;
    target.rlEmail = null;
    target.tvEmail = null;
    target.rlPhone = null;
    target.tvPhone = null;
    target.rlGender = null;
    target.tvGender = null;
    target.tvName = null;
    target.mBottomSheet = null;
  }
}
