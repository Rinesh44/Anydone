// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.location;

import android.view.View;
import android.widget.AutoCompleteTextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.appcompat.widget.AppCompatSpinner;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddLocationActivity_ViewBinding implements Unbinder {
  private AddLocationActivity target;

  @UiThread
  public AddLocationActivity_ViewBinding(AddLocationActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AddLocationActivity_ViewBinding(AddLocationActivity target, View source) {
    this.target = target;

    target.etSearchLocation = Utils.findRequiredViewAsType(source, R.id.et_search_location, "field 'etSearchLocation'", AutoCompleteTextView.class);
    target.spLocationType = Utils.findRequiredViewAsType(source, R.id.sp_location_type, "field 'spLocationType'", AppCompatSpinner.class);
    target.etLocation = Utils.findRequiredViewAsType(source, R.id.et_location, "field 'etLocation'", TextInputEditText.class);
    target.btnAddLocation = Utils.findRequiredViewAsType(source, R.id.btn_add_location, "field 'btnAddLocation'", MaterialButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AddLocationActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearchLocation = null;
    target.spLocationType = null;
    target.etLocation = null;
    target.btnAddLocation = null;
  }
}
