// Generated code from Butter Knife. Do not modify!
package com.treeleaf.anydone.serviceprovider.servicerequestdetail;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.treeleaf.anydone.serviceprovider.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ServiceRequestDetailFragment_ViewBinding implements Unbinder {
  private ServiceRequestDetailFragment target;

  private View view7f0901d8;

  private View view7f0901b2;

  private View view7f09028c;

  private View view7f09028f;

  private View view7f090389;

  private View view7f090366;

  private View view7f0903ae;

  private View view7f0901a9;

  private View view7f0901d9;

  private View view7f09038f;

  @UiThread
  public ServiceRequestDetailFragment_ViewBinding(final ServiceRequestDetailFragment target,
      View source) {
    this.target = target;

    View view;
    target.llSearchContainer = Utils.findRequiredViewAsType(source, R.id.ll_search_container, "field 'llSearchContainer'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.iv_send, "field 'ivSend' and method 'sendMessageClick'");
    target.ivSend = Utils.castView(view, R.id.iv_send, "field 'ivSend'", ImageView.class);
    view7f0901d8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sendMessageClick();
      }
    });
    target.etMessage = Utils.findRequiredViewAsType(source, R.id.et_message, "field 'etMessage'", TextInputEditText.class);
    view = Utils.findRequiredView(source, R.id.iv_clear, "field 'ivClear' and method 'clearText'");
    target.ivClear = Utils.castView(view, R.id.iv_clear, "field 'ivClear'", ImageView.class);
    view7f0901b2 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clearText();
      }
    });
    target.rvConversation = Utils.findRequiredViewAsType(source, R.id.rv_conversations, "field 'rvConversation'", RecyclerView.class);
    target.llBottomSheetMessage = Utils.findRequiredViewAsType(source, R.id.bottom_sheet, "field 'llBottomSheetMessage'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.rl_copy_holder, "field 'ivCopy' and method 'copyMessage'");
    target.ivCopy = Utils.castView(view, R.id.rl_copy_holder, "field 'ivCopy'", RelativeLayout.class);
    view7f09028c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.copyMessage();
      }
    });
    view = Utils.findRequiredView(source, R.id.rl_delete_holder, "field 'ivDelete' and method 'deleteMessage'");
    target.ivDelete = Utils.castView(view, R.id.rl_delete_holder, "field 'ivDelete'", RelativeLayout.class);
    view7f09028f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.deleteMessage();
      }
    });
    target.llAttachOptions = Utils.findRequiredViewAsType(source, R.id.ll_attach_options, "field 'llAttachOptions'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_files, "field 'tvFiles' and method 'openFiles'");
    target.tvFiles = Utils.castView(view, R.id.tv_files, "field 'tvFiles'", TextView.class);
    view7f090389 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openFiles();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_camera, "field 'tvCamera' and method 'initCamera'");
    target.tvCamera = Utils.castView(view, R.id.tv_camera, "field 'tvCamera'", TextView.class);
    view7f090366 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.initCamera();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_recorder, "field 'tvGallery' and method 'initRecorder'");
    target.tvGallery = Utils.castView(view, R.id.tv_recorder, "field 'tvGallery'", TextView.class);
    view7f0903ae = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.initRecorder();
      }
    });
    view = Utils.findRequiredView(source, R.id.iv_attachment, "field 'ivAttachment' and method 'showAttachmentOptions'");
    target.ivAttachment = Utils.castView(view, R.id.iv_attachment, "field 'ivAttachment'", ImageView.class);
    view7f0901a9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showAttachmentOptions();
      }
    });
    target.etImageDesc = Utils.findRequiredViewAsType(source, R.id.et_image_desc, "field 'etImageDesc'", EditText.class);
    target.ivCaptureView = Utils.findRequiredViewAsType(source, R.id.iv_capture_view, "field 'ivCaptureView'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.iv_send_desc, "field 'ivSendImage' and method 'sendImage'");
    target.ivSendImage = Utils.castView(view, R.id.iv_send_desc, "field 'ivSendImage'", ImageView.class);
    view7f0901d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.sendImage();
      }
    });
    target.rlRoot = Utils.findRequiredViewAsType(source, R.id.root, "field 'rlRoot'", RelativeLayout.class);
    target.clRoot = Utils.findRequiredViewAsType(source, R.id.cl_root, "field 'clRoot'", CoordinatorLayout.class);
    target.tvConnectionStatus = Utils.findRequiredViewAsType(source, R.id.tv_connection_status, "field 'tvConnectionStatus'", TextView.class);
    target.mBottomSheet = Utils.findRequiredViewAsType(source, R.id.bottom_sheet_profile, "field 'mBottomSheet'", LinearLayout.class);
    target.tvClosed = Utils.findRequiredViewAsType(source, R.id.tv_closed, "field 'tvClosed'", TextView.class);
    target.tvCancelled = Utils.findRequiredViewAsType(source, R.id.tv_cancelled, "field 'tvCancelled'", TextView.class);
    target.pbLoadData = Utils.findRequiredViewAsType(source, R.id.pb_load_data, "field 'pbLoadData'", ProgressBar.class);
    target.llBotReplying = Utils.findRequiredViewAsType(source, R.id.ll_bot_replying, "field 'llBotReplying'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_gallery, "method 'showGallery'");
    view7f09038f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showGallery();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    ServiceRequestDetailFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.llSearchContainer = null;
    target.ivSend = null;
    target.etMessage = null;
    target.ivClear = null;
    target.rvConversation = null;
    target.llBottomSheetMessage = null;
    target.ivCopy = null;
    target.ivDelete = null;
    target.llAttachOptions = null;
    target.tvFiles = null;
    target.tvCamera = null;
    target.tvGallery = null;
    target.ivAttachment = null;
    target.etImageDesc = null;
    target.ivCaptureView = null;
    target.ivSendImage = null;
    target.rlRoot = null;
    target.clRoot = null;
    target.tvConnectionStatus = null;
    target.mBottomSheet = null;
    target.tvClosed = null;
    target.tvCancelled = null;
    target.pbLoadData = null;
    target.llBotReplying = null;

    view7f0901d8.setOnClickListener(null);
    view7f0901d8 = null;
    view7f0901b2.setOnClickListener(null);
    view7f0901b2 = null;
    view7f09028c.setOnClickListener(null);
    view7f09028c = null;
    view7f09028f.setOnClickListener(null);
    view7f09028f = null;
    view7f090389.setOnClickListener(null);
    view7f090389 = null;
    view7f090366.setOnClickListener(null);
    view7f090366 = null;
    view7f0903ae.setOnClickListener(null);
    view7f0903ae = null;
    view7f0901a9.setOnClickListener(null);
    view7f0901a9 = null;
    view7f0901d9.setOnClickListener(null);
    view7f0901d9 = null;
    view7f09038f.setOnClickListener(null);
    view7f09038f = null;
  }
}
