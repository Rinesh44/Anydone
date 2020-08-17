// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.currency;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CurrencyActivity_ViewBinding implements Unbinder {
  private CurrencyActivity target;

  @UiThread
  public CurrencyActivity_ViewBinding(CurrencyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CurrencyActivity_ViewBinding(CurrencyActivity target, View source) {
    this.target = target;

    target.rlCurrency = Utils.findRequiredViewAsType(source, R.id.rl_currency, "field 'rlCurrency'", RelativeLayout.class);
    target.tvCurrency = Utils.findRequiredViewAsType(source, R.id.tv_currency, "field 'tvCurrency'", TextView.class);
    target.tvCurrencyWords = Utils.findRequiredViewAsType(source, R.id.tv_currency_words, "field 'tvCurrencyWords'", TextView.class);
    target.ivFlag = Utils.findRequiredViewAsType(source, R.id.iv_flag, "field 'ivFlag'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    CurrencyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.rlCurrency = null;
    target.tvCurrency = null;
    target.tvCurrencyWords = null;
    target.ivFlag = null;
  }
}
