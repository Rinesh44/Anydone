package com.treeleaf.anydone.serviceprovider.ticketsuggestions;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class TicketSuggestionRepositoryImpl implements TicketSuggestionRepository{
    AnyDoneService service;

    public TicketSuggestionRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }
}
