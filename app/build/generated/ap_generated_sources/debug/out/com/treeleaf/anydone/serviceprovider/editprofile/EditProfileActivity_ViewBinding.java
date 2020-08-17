// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.editprofile;

import android.view.View;
import android.widget.Spinner;
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

public class EditProfileActivity_ViewBinding implements Unbinder {
  private EditProfileActivity target;

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public EditProfileActivity_ViewBinding(EditProfileActivity target, View source) {
    this.target = target;

    target.ilFullName = Utils.findRequiredViewAsType(source, R.id.il_full_name, "field 'ilFullName'", TextInputLayout.class);
    target.etFullName = Utils.findRequiredViewAsType(source, R.id.et_full_name, "field 'etFullName'", TextInputEditText.class);
    target.btnSave = Utils.findRequiredViewAsType(source, R.id.btn_save, "field 'btnSave'", MaterialButton.class);
    target.spnGender = Utils.findRequiredViewAsType(source, R.id.sp_gender, "field 'spnGender'", Spinner.class);
    target.ilGender = Utils.findRequiredViewAsType(source, R.id.il_gender, "field 'ilGender'", TextInputLayout.class);
    target.etGender = Utils.findRequiredViewAsType(source, R.id.et_gender, "field 'etGender'", TextInputEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    EditProfileActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.ilFullName = null;
    target.etFullName = null;
    target.btnSave = null;
    target.spnGender = null;
    target.ilGender = null;
    target.etGender = null;
  }
}
