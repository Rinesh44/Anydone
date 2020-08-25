// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.account;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

  private View view7f0902cb;

  private View view7f0902c6;

  private View view7f0902d7;

  private View view7f0902ae;

  @UiThread
  public AccountFragment_ViewBinding(final AccountFragment target, View source) {
    this.target = target;

    View view;
    target.ivProfileIcon = Utils.findRequiredViewAsType(source, R.id.iv_profile_icon, "field 'ivProfileIcon'", ImageView.class);
    target.ivProfilePicSet = Utils.findRequiredViewAsType(source, R.id.iv_profile_pic_set, "field 'ivProfilePicSet'", CircleImageView.class);
    target.tvProfile = Utils.findRequiredViewAsType(source, R.id.tv_profile, "field 'tvProfile'", TextView.class);
    target.progress = Utils.findRequiredViewAsType(source, R.id.pb_progress, "field 'progress'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.rl_profile_acc, "method 'onClickRlProfile'");
    view7f0902cb = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickRlProfile();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_logout, "method 'onClickLogout'");
    view7f0902c6 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickLogout();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_setting_acc, "method 'onClickSetting'");
    view7f0902d7 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickSetting();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_about, "method 'onClickAbout'");
    view7f0902ae = view;
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
    target.progress = null;

    view7f0902cb.setOnClickListener(null);
    view7f0902cb = null;
    view7f0902c6.setOnClickListener(null);
    view7f0902c6 = null;
    view7f0902d7.setOnClickListener(null);
    view7f0902d7 = null;
    view7f0902ae.setOnClickListener(null);
    view7f0902ae = null;
  }
}
