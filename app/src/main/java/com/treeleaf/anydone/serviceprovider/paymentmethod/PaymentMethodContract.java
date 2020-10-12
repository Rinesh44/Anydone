package com.treeleaf.anydone.serviceprovider.paymentmethod;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;

import org.json.JSONException;

public class PaymentMethodContract {
    public interface PaymentMethodView extends BaseView {

        void getPaymentCardSuccess();

        void getPaymentCardFail(String msg);

        void onMakeCardPrimarySuccess(String refId);

        void onMakeCardPrimaryFail(String msg);

        void onCardDeleteSuccess(String refId, int pos);

        void onCardDeleteFail(String msg);

    }

    public interface PaymentMethodPresenter extends Presenter<PaymentMethodView> {
        void getPaymentCards();

        void makeCardPrimary(String refId) throws JSONException;

        void deleteCard(String refId, int pos);
    }
}
