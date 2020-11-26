package com.treeleaf.anydone.serviceprovider.ticketsuggestions;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;
import com.treeleaf.anydone.serviceprovider.realm.model.TicketSuggestion;

import java.util.List;

public class TicketSuggestionContract {

    public interface TicketSuggestionView extends BaseView {
        void acceptTicketSuggestionSuccess(List<TicketSuggestion> ticketSuggestionList);

        void acceptTicketSuggestionFail(String msg);

        void rejectTicketSuggestionSuccess(List<TicketSuggestion> ticketSuggestionList);

        void rejectTicketSuggestionFail(String msg);

        void acceptParticularTicketSuggestionSuccess(TicketSuggestion suggestion);

        void rejectParticularTicketSuggestionSuccess(TicketSuggestion suggestion);

        void rejectParticularTicketSuggestionFail(String msg);

        void acceptParticularTicketSuggestionFail(String msg);
    }

    public interface TicketSuggestionPresenter extends Presenter<TicketSuggestionView> {
        void acceptTicketSuggestion(List<TicketSuggestion> ticketSuggestionIds);

        void rejectTicketSuggestion(List<TicketSuggestion> ticketSuggestionIds);

        void acceptTicketSuggestion(TicketSuggestion ticketSuggestionId);

        void rejectTicketSuggestion(TicketSuggestion ticketSuggestionId);

    }
}
