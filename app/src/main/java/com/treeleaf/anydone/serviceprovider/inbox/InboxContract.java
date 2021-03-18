package com.treeleaf.anydone.serviceprovider.inbox;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;

import java.util.List;

public class InboxContract {
    public interface InboxView extends BaseView {
        void getServicesSuccess();

        void getServicesFail(String msg);

        void getInboxMessageSuccess();

        void getInboxMessageFail(String msg);

        void onMuteNotificationSuccess(String inboxId);

        void onMuteNotificationFail(String msg);

        void onUnMuteSuccess(String inboxId);

        void onUnMuteFail(String msg);

        void onConversationLeaveSuccess(Inbox inbox);

        void onConversationLeaveFail(String msg);

        void onConversationDeleteSuccess(Inbox inbox);

        void onConversationDeleteFail(String msg);

        void onConvertToGroupSuccess(Inbox inbox);

        void onConvertToGroupFail(String msg);

        void onJoinGroupSuccess(String inboxId);

        void onJoinGroupFail(String msg);

        void searchInboxSuccess(List<Inbox> inboxList);

        void searchInboxFail(String msg);

    }

    public interface InboxPresenter extends Presenter<InboxContract.InboxView> {
        void getServices();

        void getInboxMessages(boolean showProgress, long to);

        void muteInboxNotification(String inboxId, boolean mentions);

        void unMuteNotification(String inboxId);

        void leaveConversation(Inbox inbox);

        void leaveAndDeleteConversation(Inbox inbox);

        void convertToGroup(Inbox inbox);

        void joinGroup(String inboxId);

        void searchInbox(String query);
    }
}
