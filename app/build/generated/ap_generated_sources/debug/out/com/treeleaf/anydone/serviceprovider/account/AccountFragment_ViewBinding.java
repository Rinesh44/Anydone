// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.account;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AccountFragment_ViewBinding implements Unbinder {
  private AccountFragment target;

  private View view7f0902c4;

  private View view7f0902bf;

  private View view7f0902cf;

  private View view7f0902a9;

  @UiThread
  public AccountFragment_ViewBinding(final AccountFragment target, View source) {
    this.target = target;

    View view;
    target.ivProfileIcon = Utils.findRequiredViewAsType(source, R.id.iv_profile_icon, "field 'ivProfileIcon'", ImageView.class);
    target.ivProfilePicSet = Utils.findRequiredViewAsType(source, R.id.iv_profile_pic_set, "field 'ivProfilePicSet'", CircleImageView.class);
    target.tvProfile = Utils.findRequiredViewAsType(source, R.id.tv_profile, "field 'tvProfile'", TextView.class);
    view = Utils.findRequiredView(source, R.id.rl_profile_acc, "method 'onClickRlProfile'");
    view7f0902c4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRlProfile();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_logout, "method 'onClickLogout'");
    view7f0902bf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickLogout();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_setting_acc, "method 'onClickSetting'");
    view7f0902cf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickSetting();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_about, "method 'onClickAbout'");
    view7f0902a9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickAbout();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    AccountFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivProfileIcon = null;
    target.ivProfilePicSet = null;
    target.tvProfile = null;

    view7f0902c4.setOnClickListener(null);
    view7f0902c4 = null;
    view7f0902bf.setOnClickListener(null);
    view7f0902bf = null;
    view7f0902cf.setOnClickListener(null);
    view7f0902cf = null;
    view7f0902a9.setOnClickListener(null);
    view7f0902a9 = null;
  }
}
