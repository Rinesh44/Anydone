package com.anydone.desk.threads;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

import java.util.List;

import io.realm.RealmList;

public class ThreadContract {

    public interface ThreadView extends BaseView {
        void getConversationThreadSuccess();

        void getConversationThreadFail(String msg);

        void getTicketSuggestionSuccess();

        void onNoTicketSuggestion();

        void getTicketSuggestionFail(String msg);

        void getConversationLabelSuccess();

        void getConversationLabelFail(String msg);

        void filterMessagesFail(String msg);

        void filterMessagesSuccess();
    }

    public interface ThreadPresenter extends Presenter<ThreadView> {
        void getConversationThreads(boolean showProgress);

        void getTicketSuggestions();

        void getConversationLabels();

        void filterMessages(String searchQuery, long from, long to, boolean followUp,
                            boolean isImportant, RealmList<String> sources, RealmList<String> labels,
                            boolean showProgress);
    }
}
