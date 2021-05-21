package com.anydone.desk.suggestedTicketPreview;

import com.anydone.desk.rest.service.AnyDoneService;

public class SuggestedTicketPreviewRepositoryImpl implements SuggestedTicketPreviewRepository {
    AnyDoneService anyDoneService;

    public SuggestedTicketPreviewRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
