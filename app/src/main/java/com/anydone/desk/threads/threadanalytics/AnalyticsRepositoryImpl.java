package com.anydone.desk.threads.threadanalytics;

import com.anydone.desk.rest.service.AnyDoneService;

public class AnalyticsRepositoryImpl  implements AnalyticsRepository{
    AnyDoneService anyDoneService;

    public AnalyticsRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
