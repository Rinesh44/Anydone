package com.treeleaf.anydone.serviceprovider.addpaymentcard;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddCardRepositoryImpl implements AddCardRepository {
    private AnyDoneService anyDoneService;

    public AddCardRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<Boolean> addCard(@NonNull String cardHolderName, @NonNull String cardNumber,
                                       @NonNull String month, @NonNull String year,
                                       @NonNull String cvv, @NonNull String streetAddress,
                                       @NonNull String city, @NonNull String state,
                                       boolean primaryCard) {
        //        TODO: replace it with retrofit method later

        return Observable.just(true);
    }
}
