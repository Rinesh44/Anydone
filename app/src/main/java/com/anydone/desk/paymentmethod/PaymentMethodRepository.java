package com.anydone.desk.paymentmethod;

import com.treeleaf.anydone.rpc.PaymentRpcProto;

import io.reactivex.Observable;

public interface PaymentMethodRepository {
    Observable<PaymentRpcProto.PaymentBaseResponse> getPaymentCards(String token);

    Observable<PaymentRpcProto.PaymentBaseResponse> deleteCard(String token, String refId);

    Observable<PaymentRpcProto.PaymentBaseResponse> setPrimaryCard(String token, String refId);
}
