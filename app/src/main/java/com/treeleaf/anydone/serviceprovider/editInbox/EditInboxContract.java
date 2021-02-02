package com.treeleaf.anydone.serviceprovider.editInbox;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class EditInboxContract {

    public interface EditInboxView extends BaseView {

        void onSubjectEditSuccess();

        void onSubjectEditFail(String msg);
    }

    public interface EditInboxPresenter extends Presenter<EditInboxContract.EditInboxView> {

        void editInboxSubject(String inboxId, String subject);

    }
}
