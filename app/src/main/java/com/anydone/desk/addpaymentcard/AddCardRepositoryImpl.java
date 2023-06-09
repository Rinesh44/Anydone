package com.anydone.desk.addpaymentcard;

import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.rpc.PaymentRpcProto;
import com.anydone.desk.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class AddCardRepositoryImpl implements AddCardRepository {
    private AnyDoneService anyDoneService;

    public AddCardRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<PaymentRpcProto.PaymentBaseResponse> addCard(String token,
                                                                   PaymentProto.Card card) {
        return anyDoneService.addPaymentCard(token, card);
    }
}
