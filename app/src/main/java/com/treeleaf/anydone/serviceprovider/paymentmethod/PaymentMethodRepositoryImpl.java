package com.treeleaf.anydone.serviceprovider.paymentmethod;

import com.treeleaf.anydone.rpc.PaymentRpcProto;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;

import io.reactivex.Observable;

public class PaymentMethodRepositoryImpl implements PaymentMethodRepository {
    private AnyDoneService anyDoneService;

    public PaymentMethodRepositoryImpl(AnyDoneService anyDoneService) {
        this.anyDoneService = anyDoneService;
    }

    @Override
    public Observable<PaymentRpcProto.PaymentBaseResponse> getPaymentCards(String token) {
        return anyDoneService.getPaymentCards(token);
    }

    @Override
    public Observable<PaymentRpcProto.PaymentBaseResponse> deleteCard(String token, String refId) {
        return anyDoneService.deletePaymentCard(token, refId);
    }

    @Override
    public Observable<PaymentRpcProto.PaymentBaseResponse> setPrimaryCard(String token, String refId) {
        return anyDoneService.setPaymentCardAsPrimary(token, refId);
    }
}
