// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.currency;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SelectCurrencyActivity_ViewBinding implements Unbinder {
  private SelectCurrencyActivity target;

  @UiThread
  public SelectCurrencyActivity_ViewBinding(SelectCurrencyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SelectCurrencyActivity_ViewBinding(SelectCurrencyActivity target, View source) {
    this.target = target;

    target.etSearchCurrency = Utils.findRequiredViewAsType(source, R.id.et_search_currency, "field 'etSearchCurrency'", EditText.class);
    target.rvCurrency = Utils.findRequiredViewAsType(source, R.id.rv_currency, "field 'rvCurrency'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SelectCurrencyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etSearchCurrency = null;
    target.rvCurrency = null;
  }
}
