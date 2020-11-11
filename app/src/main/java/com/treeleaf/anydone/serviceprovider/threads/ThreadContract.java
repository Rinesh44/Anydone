package com.treeleaf.anydone.serviceprovider.threads;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;

import java.util.List;

public class ThreadContract {

    public interface ThreadView extends BaseView {
        void getConversationThreadSuccess();

        void getConversationThreadFail(String msg);

        void getServiceSuccess();

        void getServiceFail(String msg);

        void getTicketSuggestionSuccess();

        void onNoTicketSuggestion();

        void getTicketSuggestionFail(String msg);
    }

    public interface ThreadPresenter extends Presenter<ThreadView> {
        void getConversationThreads(boolean showProgress);

        void getServices();

        void getTicketSuggestions();
    }
}
