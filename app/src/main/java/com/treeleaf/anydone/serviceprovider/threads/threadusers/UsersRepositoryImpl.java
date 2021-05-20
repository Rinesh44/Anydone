package com.treeleaf.anydone.serviceprovider.threads.threadusers;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

public class UsersRepositoryImpl implements UsersRepository {
    AnyDoneService anyDoneService;

    public UsersRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
