package com.treeleaf.anydone.serviceprovider.editInbox;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class EditInboxRepositoryImpl implements EditInboxRepository {
    AnyDoneService anyDoneService;

    public EditInboxRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
