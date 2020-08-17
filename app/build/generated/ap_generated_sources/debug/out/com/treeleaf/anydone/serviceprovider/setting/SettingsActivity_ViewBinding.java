// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting;

import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingsActivity_ViewBinding implements Unbinder {
  private SettingsActivity target;

  private View view7f090289;

  private View view7f090296;

  private View view7f09028c;

  private View view7f090299;

  private View view7f0902af;

  @UiThread
  public SettingsActivity_ViewBinding(SettingsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingsActivity_ViewBinding(final SettingsActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.rl_change_password, "field 'rlChangePassword' and method 'changePassword'");
    target.rlChangePassword = Utils.castView(view, R.id.rl_change_password, "field 'rlChangePassword'", RelativeLayout.class);
    view7f090289 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.changePassword();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_language, "field 'rlLanguage' and method 'languageSettings'");
    target.rlLanguage = Utils.castView(view, R.id.rl_language, "field 'rlLanguage'", RelativeLayout.class);
    view7f090296 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.languageSettings();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_currency, "field 'rlCurrency' and method 'currencySettings'");
    target.rlCurrency = Utils.castView(view, R.id.rl_currency, "field 'rlCurrency'", RelativeLayout.class);
    view7f09028c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.currencySettings();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_location, "field 'rlLocation' and method 'locationSettings'");
    target.rlLocation = Utils.castView(view, R.id.rl_location, "field 'rlLocation'", RelativeLayout.class);
    view7f090299 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.locationSettings();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_timezone, "field 'rlTimezone' and method 'timezoneSettings'");
    target.rlTimezone = Utils.castView(view, R.id.rl_timezone, "field 'rlTimezone'", RelativeLayout.class);
    view7f0902af = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.timezoneSettings();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlChangePassword = null;
    target.rlLanguage = null;
    target.rlCurrency = null;
    target.rlLocation = null;
    target.rlTimezone = null;

    view7f090289.setOnClickListener(null);
    view7f090289 = null;
    view7f090296.setOnClickListener(null);
    view7f090296 = null;
    view7f09028c.setOnClickListener(null);
    view7f09028c = null;
    view7f090299.setOnClickListener(null);
    view7f090299 = null;
    view7f0902af.setOnClickListener(null);
    view7f0902af = null;
  }
}
