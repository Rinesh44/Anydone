package com.anydone.desk.addpaymentcard;

import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.rpc.PaymentRpcProto;

import io.reactivex.Observable;

public interface AddCardRepository {
    Observable<PaymentRpcProto.PaymentBaseResponse> addCard(String token,
                                                            PaymentProto.Card card);
}
