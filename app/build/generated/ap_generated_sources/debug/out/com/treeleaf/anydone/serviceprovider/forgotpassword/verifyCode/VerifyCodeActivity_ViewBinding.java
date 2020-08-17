// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.forgotpassword.verifyCode;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VerifyCodeActivity_ViewBinding implements Unbinder {
  private VerifyCodeActivity target;

  @UiThread
  public VerifyCodeActivity_ViewBinding(VerifyCodeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VerifyCodeActivity_ViewBinding(VerifyCodeActivity target, View source) {
    this.target = target;

    target.tvResendCode = Utils.findRequiredViewAsType(source, R.id.tv_resend_code, "field 'tvResendCode'", TextView.class);
    target.llResendCode = Utils.findRequiredViewAsType(source, R.id.ll_resend_code, "field 'llResendCode'", LinearLayout.class);
    target.llOtpTimer = Utils.findRequiredViewAsType(source, R.id.ll_otp_timer, "field 'llOtpTimer'", LinearLayout.class);
    target.tvCodeExpiresIn = Utils.findRequiredViewAsType(source, R.id.tv_code_expires_in, "field 'tvCodeExpiresIn'", TextView.class);
    target.tvUserEmailPhone = Utils.findRequiredViewAsType(source, R.id.tv_user_email_phone, "field 'tvUserEmailPhone'", TextView.class);
    target.etPin = Utils.findRequiredViewAsType(source, R.id.txt_pin_entry, "field 'etPin'", PinEntryEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VerifyCodeActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvResendCode = null;
    target.llResendCode = null;
    target.llOtpTimer = null;
    target.tvCodeExpiresIn = null;
    target.tvUserEmailPhone = null;
    target.etPin = null;
  }
}
