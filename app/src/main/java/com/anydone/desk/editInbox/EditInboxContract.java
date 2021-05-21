package com.anydone.desk.editInbox;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class EditInboxContract {

    public interface EditInboxView extends BaseView {

        void onSubjectEditSuccess();

        void onSubjectEditFail(String msg);
    }

    public interface EditInboxPresenter extends Presenter<EditInboxContract.EditInboxView> {

        void editInboxSubject(String inboxId, String subject);

    }
}
