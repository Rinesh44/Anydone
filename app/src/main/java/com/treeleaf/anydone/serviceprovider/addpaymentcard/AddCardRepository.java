package com.treeleaf.anydone.serviceprovider.addpaymentcard;

import androidx.annotation.NonNull;

import io.reactivex.Observable;

public interface AddCardRepository {
    Observable<Boolean> addCard(@NonNull String cardHolderName, @NonNull String cardNumber,
                                @NonNull String month, @NonNull String year,
                                @NonNull String cvv, @NonNull String streetAddress,
                                @NonNull String city, @NonNull String state,
                                boolean primaryCard);
}
