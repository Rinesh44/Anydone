// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.aboutus;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AboutUsActivity_ViewBinding implements Unbinder {
  private AboutUsActivity target;

  @UiThread
  public AboutUsActivity_ViewBinding(AboutUsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AboutUsActivity_ViewBinding(AboutUsActivity target, View source) {
    this.target = target;

    target.tvPrivacy = Utils.findRequiredViewAsType(source, R.id.tv_privacy, "field 'tvPrivacy'", TextView.class);
    target.tvOpenSource = Utils.findRequiredViewAsType(source, R.id.tv_open_src_library, "field 'tvOpenSource'", TextView.class);
    target.tvTermsAndConditions = Utils.findRequiredViewAsType(source, R.id.tv_terms_n_conditions, "field 'tvTermsAndConditions'", TextView.class);
    target.ivFacebook = Utils.findRequiredViewAsType(source, R.id.iv_facebook, "field 'ivFacebook'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AboutUsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvPrivacy = null;
    target.tvOpenSource = null;
    target.tvTermsAndConditions = null;
    target.ivFacebook = null;
  }
}
