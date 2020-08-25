// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.login;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LoginActivity_ViewBinding implements Unbinder {
  private LoginActivity target;

  private View view7f09023d;

  @UiThread
  public LoginActivity_ViewBinding(LoginActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LoginActivity_ViewBinding(final LoginActivity target, View source) {
    this.target = target;

    View view;
    target.tvForgotPassword = Utils.findRequiredViewAsType(source, R.id.tv_forgot_password, "field 'tvForgotPassword'", TextView.class);
    target.etEmail = Utils.findRequiredViewAsType(source, R.id.et_email, "field 'etEmail'", TextInputEditText.class);
    target.etPassword = Utils.findRequiredViewAsType(source, R.id.et_password, "field 'etPassword'", TextInputEditText.class);
    target.ilEmailPhone = Utils.findRequiredViewAsType(source, R.id.il_email_phone, "field 'ilEmailPhone'", TextInputLayout.class);
    target.ilPassword = Utils.findRequiredViewAsType(source, R.id.il_password, "field 'ilPassword'", TextInputLayout.class);
    target.progress = Utils.findRequiredViewAsType(source, R.id.pb_progress, "field 'progress'", ProgressBar.class);
    view = Utils.findRequiredView(source, R.id.loginButton, "method 'login'");
    view7f09023d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.login();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LoginActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvForgotPassword = null;
    target.etEmail = null;
    target.etPassword = null;
    target.ilEmailPhone = null;
    target.ilPassword = null;
    target.progress = null;

    view7f09023d.setOnClickListener(null);
    view7f09023d = null;
  }
}
