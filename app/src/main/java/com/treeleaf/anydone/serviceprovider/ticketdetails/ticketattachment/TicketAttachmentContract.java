package com.treeleaf.anydone.serviceprovider.ticketdetails.ticketattachment;

import android.app.Activity;
import android.net.Uri;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Attachment;

public class TicketAttachmentContract {
    public interface TicketAttachmentView extends BaseView {
        void onUploadImageAttachmentSuccess(String url, String title);

        void onUploadImageAttachmentFail(String msg);

        void onUploadFileAttachmentSuccess(String url, String title);

        void onUploadFileAttachmentFail(String msg);

        void addAttachmentSuccess(Attachment attachment);

        void addAttachmentFail(String msg);

        void removeAttachmentSuccess(Attachment attachment);

        void removeAttachmentFail(String msg);

    }

    public interface TicketAttachmentPresenter extends Presenter<TicketAttachmentView> {
        void uploadImageAttachment(Uri uri, Activity activity, String title);

        void uploadFileAttachment(Uri uri, String title);

        void addAttachment(long ticketId, Attachment attachment);

        void removeAttachment(long ticketId, Attachment attachment);
    }
}
