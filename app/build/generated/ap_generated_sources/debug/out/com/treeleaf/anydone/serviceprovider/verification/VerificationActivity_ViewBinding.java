// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.verification;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VerificationActivity_ViewBinding implements Unbinder {
  private VerificationActivity target;

  private View view7f0903e5;

  @UiThread
  public VerificationActivity_ViewBinding(VerificationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VerificationActivity_ViewBinding(final VerificationActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.tv_resend_code, "field 'tvResendCode' and method 'onClickResendCode'");
    target.tvResendCode = Utils.castView(view, R.id.tv_resend_code, "field 'tvResendCode'", TextView.class);
    view7f0903e5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClickResendCode();
      }
    });
    target.llResendCode = Utils.findRequiredViewAsType(source, R.id.ll_resend_code, "field 'llResendCode'", LinearLayout.class);
    target.llOtpTimer = Utils.findRequiredViewAsType(source, R.id.ll_otp_timer, "field 'llOtpTimer'", LinearLayout.class);
    target.tvCodeExpiresIn = Utils.findRequiredViewAsType(source, R.id.tv_code_expires_in, "field 'tvCodeExpiresIn'", TextView.class);
    target.tvUserEmailPhone = Utils.findRequiredViewAsType(source, R.id.tv_user_email_phone, "field 'tvUserEmailPhone'", TextView.class);
    target.etPin = Utils.findRequiredViewAsType(source, R.id.txt_pin_entry, "field 'etPin'", PinEntryEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VerificationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvResendCode = null;
    target.llResendCode = null;
    target.llOtpTimer = null;
    target.tvCodeExpiresIn = null;
    target.tvUserEmailPhone = null;
    target.etPin = null;

    view7f0903e5.setOnClickListener(null);
    view7f0903e5 = null;
  }
}
