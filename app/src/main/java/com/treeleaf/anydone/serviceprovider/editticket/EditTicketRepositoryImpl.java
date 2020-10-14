package com.treeleaf.anydone.serviceprovider.editticket;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;

public class EditTicketRepositoryImpl implements EditTicketRepository {
    AnyDoneService anyDoneService;
    String token = Hawk.get(Constants.TOKEN);

    public EditTicketRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
