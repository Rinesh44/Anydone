package com.treeleaf.anydone.serviceprovider.addpaymentcard;

import androidx.annotation.NonNull;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.utils.ValidationUtils;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class AddCardPresenterImpl extends BasePresenter<AddCardContract.AddCardView> implements
        AddCardContract.AddCardPresenter {

    private AddCardRepository addCardRepository;

    @Inject
    public AddCardPresenterImpl(AddCardRepository addCardRepository) {
        this.addCardRepository = addCardRepository;
    }

    @Override
    public void addCard(@NonNull String cardHolderName, @NonNull String cardNumber,
                        @NonNull String month, @NonNull String year, @NonNull String CVV,
                        @NonNull String streetAddress,
                        @NonNull String city, @NonNull String state, boolean primaryCard) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(cardHolderName, "CardHolderName cannot be null");
        Preconditions.checkNotNull(cardNumber, "Card Number cannot be null");
        Preconditions.checkNotNull(month, "Month cannot be null");
        Preconditions.checkNotNull(year, "Year cannot be null");
        Preconditions.checkNotNull(streetAddress, "Street Address cannot be null");
        Preconditions.checkNotNull(city, "City cannot be null");
        Preconditions.checkNotNull(state, "State cannot be null");

        if (!validateCredentials(cardHolderName, cardNumber, month, year, CVV, streetAddress,
                city, state)) {
            return;
        }

        getView().showProgressBar(null);
        Observable<Boolean> addCardObservable;
        addCardObservable = addCardRepository.addCard(cardHolderName, cardNumber, month, year, CVV,
                streetAddress, city,
                state, primaryCard);

        addSubscription(addCardObservable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean addCardSuccess) {
                        if (addCardSuccess) {
                            //todo show msg
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                        getView().onAddCardSuccess();
                    }
                }));
    }


    private boolean validateCredentials(String cardHolderName, String cardNumber, String month,
                                        String year, String cvv, String streetAddress,
                                        String city, String state) {

        if (ValidationUtils.isEmpty(cardHolderName)) {
            getView().showInvalidCardHolderNameError();
            return false;
        }

        if (ValidationUtils.isEmpty(cardNumber) || cardNumber.length() != 19) {
            getView().showInvalidCardNumberError();
            return false;
        }

        if (ValidationUtils.isEmpty(month) || Double.parseDouble(month) > 12) {
            getView().showInvalidMonthError();
            return false;
        }

        if (ValidationUtils.isEmpty(year) || year.length() != 4) {
            getView().showInvalidYeaError();
            return false;
        }

        if (ValidationUtils.isEmpty(cvv) || cvv.length() != 3) {
            getView().showInvalidCVVError();
            return false;
        }

        if (ValidationUtils.isEmpty(streetAddress)) {
            getView().showInvalidStreetAddressError();
            return false;
        }

        if (ValidationUtils.isEmpty(city)) {
            getView().showInvalidCityError();
            return false;
        }

        if (ValidationUtils.isEmpty(state)) {
            getView().showInvalidStateError();
            return false;
        }

        return true;
    }
}
