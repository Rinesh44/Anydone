// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.forgotpassword.resetpassword;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class ResetPasswordActivity_ViewBinding implements Unbinder {
  private ResetPasswordActivity target;

  @UiThread
  public ResetPasswordActivity_ViewBinding(ResetPasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ResetPasswordActivity_ViewBinding(ResetPasswordActivity target, View source) {
    this.target = target;

    target.ilNewPassword = Utils.findRequiredViewAsType(source, R.id.il_new_password, "field 'ilNewPassword'", TextInputLayout.class);
    target.etNewPassword = Utils.findRequiredViewAsType(source, R.id.et_new_password, "field 'etNewPassword'", TextInputEditText.class);
    target.ilConfirmPassword = Utils.findRequiredViewAsType(source, R.id.il_confirm_password, "field 'ilConfirmPassword'", TextInputLayout.class);
    target.etConfirmPassword = Utils.findRequiredViewAsType(source, R.id.et_confirm_password, "field 'etConfirmPassword'", TextInputEditText.class);
    target.btnReset = Utils.findRequiredViewAsType(source, R.id.btn_reset, "field 'btnReset'", MaterialButton.class);
    target.ilCode = Utils.findRequiredViewAsType(source, R.id.il_code, "field 'ilCode'", TextInputLayout.class);
    target.etCode = Utils.findRequiredViewAsType(source, R.id.et_code, "field 'etCode'", TextInputEditText.class);
    target.llResendCode = Utils.findRequiredViewAsType(source, R.id.ll_resend_code, "field 'llResendCode'", LinearLayout.class);
    target.llOtpTimer = Utils.findRequiredViewAsType(source, R.id.ll_otp_timer, "field 'llOtpTimer'", LinearLayout.class);
    target.tvCodeExpiresIn = Utils.findRequiredViewAsType(source, R.id.tv_code_expires_in, "field 'tvCodeExpiresIn'", TextView.class);
    target.tvResetPassword = Utils.findRequiredViewAsType(source, R.id.tv_reset_password, "field 'tvResetPassword'", TextView.class);
    target.ilOldPassword = Utils.findRequiredViewAsType(source, R.id.il_old_password, "field 'ilOldPassword'", TextInputLayout.class);
    target.etOldPassword = Utils.findRequiredViewAsType(source, R.id.et_old_password, "field 'etOldPassword'", TextInputEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ResetPasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ilNewPassword = null;
    target.etNewPassword = null;
    target.ilConfirmPassword = null;
    target.etConfirmPassword = null;
    target.btnReset = null;
    target.ilCode = null;
    target.etCode = null;
    target.llResendCode = null;
    target.llOtpTimer = null;
    target.tvCodeExpiresIn = null;
    target.tvResetPassword = null;
    target.ilOldPassword = null;
    target.etOldPassword = null;
  }
}
