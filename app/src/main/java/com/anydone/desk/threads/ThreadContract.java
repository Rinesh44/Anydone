package com.anydone.desk.threads;

import com.anydone.desk.base.presenter.Presenter;
import com.anydone.desk.base.view.BaseView;

public class ThreadContract {

    public interface ThreadView extends BaseView {
        void getConversationThreadSuccess();

        void getConversationThreadFail(String msg);

        void getTicketSuggestionSuccess();

        void onNoTicketSuggestion();

        void getTicketSuggestionFail(String msg);
    }

    public interface ThreadPresenter extends Presenter<ThreadView> {
        void getConversationThreads(boolean showProgress);

        void getTicketSuggestions();
    }
}
