package com.anydone.desk.threads.threadusers;

import com.anydone.desk.rest.service.AnyDoneService;

public class UsersRepositoryImpl implements UsersRepository {
    AnyDoneService anyDoneService;

    public UsersRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }
}
