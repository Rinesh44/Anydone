package com.anydone.desk.ticketsuggestions;

import com.anydone.desk.rest.service.AnyDoneService;

public class TicketSuggestionRepositoryImpl implements TicketSuggestionRepository{
    AnyDoneService service;

    public TicketSuggestionRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }
}
