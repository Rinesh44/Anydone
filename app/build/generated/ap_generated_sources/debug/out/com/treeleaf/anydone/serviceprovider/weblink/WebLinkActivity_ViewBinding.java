// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.weblink;

import android.view.View;
import android.webkit.WebView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WebLinkActivity_ViewBinding implements Unbinder {
  private WebLinkActivity target;

  @UiThread
  public WebLinkActivity_ViewBinding(WebLinkActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public WebLinkActivity_ViewBinding(WebLinkActivity target, View source) {
    this.target = target;

    target.mAnydoneWeb = Utils.findRequiredViewAsType(source, R.id.wv_anydone, "field 'mAnydoneWeb'", WebView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    WebLinkActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mAnydoneWeb = null;
  }
}
