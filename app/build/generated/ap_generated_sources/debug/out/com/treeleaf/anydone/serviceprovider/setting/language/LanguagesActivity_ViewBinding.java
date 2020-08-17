// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.language;

import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LanguagesActivity_ViewBinding implements Unbinder {
  private LanguagesActivity target;

  @UiThread
  public LanguagesActivity_ViewBinding(LanguagesActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LanguagesActivity_ViewBinding(LanguagesActivity target, View source) {
    this.target = target;

    target.rgLanguages = Utils.findRequiredViewAsType(source, R.id.rg_languages, "field 'rgLanguages'", RadioGroup.class);
    target.rbEnglish = Utils.findRequiredViewAsType(source, R.id.rb_english, "field 'rbEnglish'", RadioButton.class);
    target.rbNepali = Utils.findRequiredViewAsType(source, R.id.rb_nepali, "field 'rbNepali'", RadioButton.class);
    target.rbHebrew = Utils.findRequiredViewAsType(source, R.id.rb_hebrew, "field 'rbHebrew'", RadioButton.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LanguagesActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rgLanguages = null;
    target.rbEnglish = null;
    target.rbNepali = null;
    target.rbHebrew = null;
  }
}
