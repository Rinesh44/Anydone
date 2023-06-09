package com.anydone.desk.addpaymentcard;

import androidx.annotation.NonNull;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.rpc.PaymentRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.AccountRepo;
import com.anydone.desk.realm.repo.CardRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;
import com.anydone.desk.utils.ValidationUtils;

import java.util.Calendar;

import javax.inject.Inject;

import dagger.internal.Preconditions;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class AddCardPresenterImpl extends BasePresenter<AddCardContract.AddCardView> implements
        AddCardContract.AddCardPresenter {

    private static final String TAG = "AddCardPresenterImpl";
    private AddCardRepository addCardRepository;


    @Inject
    public AddCardPresenterImpl(AddCardRepository addCardRepository) {
        this.addCardRepository = addCardRepository;
    }

    @Override
    public void addCard(@NonNull String cardNumber,
                        @NonNull String cardHolderName,
                        @NonNull String month,
                        @NonNull String year,
                        @NonNull String CVV,
                        @NonNull String streetAddress,
                        @NonNull String city, @NonNull String state, @NonNull String zipCode,
                        @NonNull String cardType,
                        boolean isDefault) {
        Preconditions.checkNotNull(getView(), "View is not attached");
        Preconditions.checkNotNull(cardHolderName, "CardHolderName cannot be null");
        Preconditions.checkNotNull(cardNumber, "Card Number cannot be null");
        Preconditions.checkNotNull(month, "month cannot be null");
        Preconditions.checkNotNull(year, "year cannot be null");

        if (!validateCredentials(cardNumber, cardHolderName, month, year, CVV)) {
            return;
        }

        getView().showProgressBar(null);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<PaymentRpcProto.PaymentBaseResponse> addCardObservable;

        String userAccountId = AccountRepo.getInstance().getAccount().getAccountId();

        UserProto.Address billingAddress = UserProto.Address.newBuilder()
                .setZip(zipCode)
                .setState(state)
                .setStreet(streetAddress)
                .setCity(city)
                .build();

        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1, 0, 0, 0);
        long expiryDate = cal.getTimeInMillis();
        GlobalUtils.showLog(TAG, "expiry date check: " + expiryDate);

        PaymentProto.Card card = PaymentProto.Card.newBuilder()
                .setAccountId(userAccountId)
                .setCardHolderName(cardHolderName)
                .setCardNumber(cardNumber)
                .setExpiryDate(expiryDate)
                .setCvc(Integer.parseInt(CVV))
                .setCardType(cardType)
                .setBillingAddress(billingAddress)
                .setIsDefault(isDefault)
                .build();

        String token = Hawk.get(Constants.TOKEN);
        GlobalUtils.showLog(TAG, "card det: " + card);
        addCardObservable = service.addPaymentCard(token, card);

        addSubscription(addCardObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PaymentRpcProto.PaymentBaseResponse>() {
                    @Override
                    public void onNext(PaymentRpcProto.PaymentBaseResponse addCardResponse) {

                        getView().hideProgressBar();

                        GlobalUtils.showLog(TAG, "add card response: " + addCardResponse);

                        if (addCardResponse == null) {
                            getView().onAddCardFail("Failed to add card");
                            return;
                        }

                        if (addCardResponse.getError()) {
                            getView().onAddCardFail(addCardResponse.getMsg());
                            return;
                        }

                        saveCard(addCardResponse.getCard());
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                }));
    }

    private void saveCard(PaymentProto.Card card) {
        if (card.getIsDefault()) {
            CardRepo.getInstance().removeCardAsPrimary();
        }
        CardRepo.getInstance().saveCard(card, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().onAddCardSuccess();
            }

            @Override
            public void fail() {
                getView().onAddCardFail("Failed to add card");
            }
        });
    }


    private boolean validateCredentials(String cardNumber, String cardHolderName, String month,
                                        String year,
                                        String cvv) {

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

        return true;
    }
}
