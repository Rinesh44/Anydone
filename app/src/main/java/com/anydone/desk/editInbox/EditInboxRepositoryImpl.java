package com.anydone.desk.editInbox;

import com.anydone.desk.rest.service.AnyDoneService;

public class EditInboxRepositoryImpl implements EditInboxRepository {
    AnyDoneService anyDoneService;

    public EditInboxRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
