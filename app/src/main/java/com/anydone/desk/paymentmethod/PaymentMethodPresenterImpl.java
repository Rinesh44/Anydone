package com.anydone.desk.paymentmethod;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.PaymentProto;
import com.treeleaf.anydone.rpc.PaymentRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.CardRepo;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PaymentMethodPresenterImpl extends BasePresenter<PaymentMethodContract.PaymentMethodView>
        implements PaymentMethodContract.PaymentMethodPresenter {

    private static final String TAG = "PaymentMethodPresenterI";

    private PaymentMethodRepository paymentMethodRepository;

    @Inject
    public PaymentMethodPresenterImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public void getPaymentCards() {
        getView().showProgressBar("Please wait");
        Observable<PaymentRpcProto.PaymentBaseResponse> paymentObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String token = Hawk.get(Constants.TOKEN);

        paymentObservable = service.getPaymentCards(token);
        addSubscription(paymentObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<PaymentRpcProto.PaymentBaseResponse>() {
                            @Override
                            public void onNext(PaymentRpcProto.PaymentBaseResponse
                                                       paymentBaseResponse) {
                                GlobalUtils.showLog(TAG, "get payment cards response: "
                                        + paymentBaseResponse);

                                getView().hideProgressBar();
                                if (paymentBaseResponse == null) {
                                    getView().getPaymentCardFail("error on getting cards");
                                    return;
                                }

                                if (paymentBaseResponse.getError()) {
                                    getView().getPaymentCardFail(paymentBaseResponse.getMsg());
                                    return;
                                }

                                saveCardList(paymentBaseResponse.getCardsList());

                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getPaymentCardFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void makeCardPrimary(String refId) throws JSONException {
        getView().showProgressBar("Please wait...");

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstanceJSON();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        JSONObject idObj = new JSONObject();
        idObj.put("refId", refId);

        GlobalUtils.showLog(TAG, "payload check: " + idObj.toString());
        Observable<PaymentRpcProto.PaymentBaseResponse> makeLocationDefaultObservable =
                service.setPaymentCardAsPrimary(token, idObj.toString());

        addSubscription(makeLocationDefaultObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PaymentRpcProto.PaymentBaseResponse>() {
                    @Override
                    public void onNext(PaymentRpcProto.PaymentBaseResponse paymentBaseResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "make card def response: "
                                + paymentBaseResponse);

                        if (paymentBaseResponse == null) {
                            getView().onMakeCardPrimaryFail("Failed to set def card");
                            return;
                        }

                        if (paymentBaseResponse.getError()) {
                            getView().onMakeCardPrimaryFail(paymentBaseResponse.getMsg());
                            return;
                        }

                        getView().onMakeCardPrimarySuccess(refId);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onMakeCardPrimaryFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    @Override
    public void deleteCard(String refId, int pos) {
        getView().showProgressBar("Please wait...");

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<PaymentRpcProto.PaymentBaseResponse> paymentObservable =
                service.deletePaymentCard(token, refId);

        addSubscription(paymentObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<PaymentRpcProto.PaymentBaseResponse>() {
                    @Override
                    public void onNext(PaymentRpcProto.PaymentBaseResponse paymentBaseResponse) {
                        getView().hideProgressBar();
                        GlobalUtils.showLog(TAG, "delete card response: "
                                + paymentBaseResponse);


                        if (paymentBaseResponse == null) {
                            getView().onCardDeleteFail("Failed to delete card");
                            return;
                        }

                        if (paymentBaseResponse.getError()) {
                            getView().onCardDeleteFail(paymentBaseResponse.getMsg());
                            return;
                        }

                        getView().onCardDeleteSuccess(refId, pos);
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideProgressBar();
                        getView().onCardDeleteFail(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        getView().hideProgressBar();
                    }
                })
        );
    }

    private void saveCardList(List<PaymentProto.Card> cardsList) {
        CardRepo.getInstance().saveCardList(cardsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getPaymentCardSuccess();
            }

            @Override
            public void fail() {
                getView().getPaymentCardFail("Failed to save cards");
            }
        });
    }
}
