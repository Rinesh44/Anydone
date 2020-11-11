package com.treeleaf.anydone.serviceprovider.suggestedTicketPreview;

import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

public class SuggestedTicketPreviewContract {
    public interface SuggestedTicketPreviewView extends BaseView {
        void acceptTicketSuggestionSuccess(String ticketSuggestionId);

        void acceptTicketSuggestionFail(String msg);

        void rejectTicketSuggestionSuccess(String ticketSuggestionId);

        void rejectTicketSuggestionFail(String msg);

        void getTicketHistorySuccess(TicketProto.TicketSuggestion ticketSuggestion);

        void getTicketHistoryFail(String msg);
    }

    public interface SuggestedTicketPreviewPresenter extends Presenter
            <SuggestedTicketPreviewContract.SuggestedTicketPreviewView> {
        void acceptTicketSuggestion(String ticketSuggestionId);

        void rejectTicketSuggestion(String ticketSuggestionId);

        void getTicketHistory(String suggestionId);

    }

}
