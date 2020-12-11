package com.treeleaf.anydone.serviceprovider.suggestedTicketPreview;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class SuggestedTicketPreviewRepositoryImpl implements SuggestedTicketPreviewRepository {
    AnyDoneService anyDoneService;

    public SuggestedTicketPreviewRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
