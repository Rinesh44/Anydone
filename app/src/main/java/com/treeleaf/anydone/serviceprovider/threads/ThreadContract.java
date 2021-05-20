package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

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
