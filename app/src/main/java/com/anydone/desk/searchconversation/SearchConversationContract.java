package com.anydone.desk.searchconversation;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;
import com.anydone.desk.realm.model.Conversation;

import java.util.List;

public class SearchConversationContract {

    public interface SearchConversationView extends BaseView {
        void onSearchConversationSuccess(List<Conversation> conversationList);

        void onSearchConversationFail(String msg);

    }

    public interface SearchConversationPresenter extends Presenter<SearchConversationView> {
        void SearchConversations(String query, String inboxId);
    }
}
