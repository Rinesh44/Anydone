package com.anydone.desk.editticket;

import com.orhanobut.hawk.Hawk;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;

public class EditTicketRepositoryImpl implements EditTicketRepository {
    AnyDoneService anyDoneService;
    String token = Hawk.get(Constants.TOKEN);

    public EditTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
