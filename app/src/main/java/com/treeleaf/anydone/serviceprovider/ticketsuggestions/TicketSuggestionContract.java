package com.treeleaf.anydone.serviceprovider.ticketsuggestions;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import java.util.List;

public class TicketSuggestionContract {

    public interface TicketSuggestionView extends BaseView {
        void acceptTicketSuggestionSuccess();

        void acceptTicketSuggestionFail(String msg);

        void rejectTicketSuggestionSuccess();

        void rejectTicketSuggestionFail(String msg);

        void acceptParticularTicketSuggestionSuccess(String suggestionId);

        void rejectParticularTicketSuggestionSuccess(String suggestionId);

        void rejectParticularTicketSuggestionFail(String suggestionId);

        void acceptParticularTicketSuggestionFail(String suggestionId);
    }

    public interface TicketSuggestionPresenter extends Presenter<TicketSuggestionView> {
        void acceptTicketSuggestion(List<String> ticketSuggestionIds);

        void rejectTicketSuggestion(List<String> ticketSuggestionIds);

        void acceptTicketSuggestion(String ticketSuggestionId);

        void rejectTicketSuggestion(String ticketSuggestionId);

    }
}
