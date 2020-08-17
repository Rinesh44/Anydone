// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.forgotpassword;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ForgotPasswordActivity_ViewBinding implements Unbinder {
  private ForgotPasswordActivity target;

  @UiThread
  public ForgotPasswordActivity_ViewBinding(ForgotPasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ForgotPasswordActivity_ViewBinding(ForgotPasswordActivity target, View source) {
    this.target = target;

    target.ivBack = Utils.findRequiredViewAsType(source, R.id.iv_back, "field 'ivBack'", ImageView.class);
    target.ilPhone = Utils.findRequiredViewAsType(source, R.id.il_phone, "field 'ilPhone'", TextInputLayout.class);
    target.etPhone = Utils.findRequiredViewAsType(source, R.id.et_phone, "field 'etPhone'", TextInputEditText.class);
    target.btnSendCode = Utils.findRequiredViewAsType(source, R.id.btnSendResetCode, "field 'btnSendCode'", MaterialButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ForgotPasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ivBack = null;
    target.ilPhone = null;
    target.etPhone = null;
    target.btnSendCode = null;
  }
}
