package com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class ActivityRepositoryImpl implements ActivityRepository {
    AnyDoneService service;

    public ActivityRepositoryImpl(AnyDoneService service) {
        this.service = service;
    }

}
