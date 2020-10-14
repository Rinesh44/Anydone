package com.treeleaf.anydone.serviceprovider.addpaymentcard;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.Presenter;
import com.treeleaf.anydone.serviceprovider.base.view.BaseView;


public class AddCardContract {
    public interface AddCardView extends BaseView {
//        void showInvalidCardHolderNameError();

        void showInvalidCardNumberError();

        void showInvalidMonthError();

        void showInvalidYeaError();

        void showInvalidCVVError();

//        void onInvalidCardHolderName();

        void onInvalidCardNumber();

        void onInvalidMonth();

        void onInvalidYear();

        void onInvalidCVV();

        void onAddCardSuccess();

        void onAddCardFail(String msg);
    }

    public interface AddCardPresenter extends Presenter<AddCardView> {
        void addCard(@NonNull String cardNumber,
                     @NonNull String month,
                     @NonNull String year,
                     @NonNull String CVV,
                     @NonNull String streetAddress,
                     @NonNull String city,
                     @NonNull String state,
                     @NonNull String zipCode,
                     @NonNull String cardType,
                     boolean isDefault);
    }
}
