// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.setting.video;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoSettingActivity_ViewBinding implements Unbinder {
  private VideoSettingActivity target;

  @UiThread
  public VideoSettingActivity_ViewBinding(VideoSettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoSettingActivity_ViewBinding(VideoSettingActivity target, View source) {
    this.target = target;

    target.tvUseCamera2 = Utils.findRequiredViewAsType(source, R.id.tv_use_camera2, "field 'tvUseCamera2'", TextView.class);
    target.tvDisableAudioProcessing = Utils.findRequiredViewAsType(source, R.id.tv_disable_audio_processing, "field 'tvDisableAudioProcessing'", TextView.class);
    target.cbUseCamera2 = Utils.findRequiredViewAsType(source, R.id.cb_use_camera2, "field 'cbUseCamera2'", CheckBox.class);
    target.cbDisableAudioProcessing = Utils.findRequiredViewAsType(source, R.id.cb_disable_audio_processing, "field 'cbDisableAudioProcessing'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    VideoSettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tvUseCamera2 = null;
    target.tvDisableAudioProcessing = null;
    target.cbUseCamera2 = null;
    target.cbDisableAudioProcessing = null;
  }
}
