// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.changepassword;

import android.view.View;
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

public class ChangePasswordActivity_ViewBinding implements Unbinder {
  private ChangePasswordActivity target;

  @UiThread
  public ChangePasswordActivity_ViewBinding(ChangePasswordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ChangePasswordActivity_ViewBinding(ChangePasswordActivity target, View source) {
    this.target = target;

    target.ilOldPassword = Utils.findRequiredViewAsType(source, R.id.il_old_password, "field 'ilOldPassword'", TextInputLayout.class);
    target.etOldPassword = Utils.findRequiredViewAsType(source, R.id.et_old_password, "field 'etOldPassword'", TextInputEditText.class);
    target.ilNewPassword = Utils.findRequiredViewAsType(source, R.id.il_new_password, "field 'ilNewPassword'", TextInputLayout.class);
    target.etNewPassword = Utils.findRequiredViewAsType(source, R.id.et_new_password, "field 'etNewPassword'", TextInputEditText.class);
    target.ilConfirmPassword = Utils.findRequiredViewAsType(source, R.id.il_confirm_password, "field 'ilConfirmPassword'", TextInputLayout.class);
    target.etConfirmPassword = Utils.findRequiredViewAsType(source, R.id.et_confirm_password, "field 'etConfirmPassword'", TextInputEditText.class);
    target.btnSave = Utils.findRequiredViewAsType(source, R.id.btn_save, "field 'btnSave'", MaterialButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ChangePasswordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ilOldPassword = null;
    target.etOldPassword = null;
    target.ilNewPassword = null;
    target.etNewPassword = null;
    target.ilConfirmPassword = null;
    target.etConfirmPassword = null;
    target.btnSave = null;
  }
}
