package com.treeleaf.anydone.serviceprovider.searchconversation;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.Conversation;

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
