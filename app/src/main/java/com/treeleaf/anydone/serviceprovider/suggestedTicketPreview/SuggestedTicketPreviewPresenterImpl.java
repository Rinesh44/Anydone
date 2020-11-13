package com.treeleaf.anydone.serviceprovider.suggestedTicketPreview;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.januswebrtc.Const;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class SuggestedTicketPreviewPresenterImpl extends BasePresenter<SuggestedTicketPreviewContract.SuggestedTicketPreviewView>
        implements SuggestedTicketPreviewContract.SuggestedTicketPreviewPresenter {
    private static final String TAG = "SuggestedTicketPreviewP";
    private SuggestedTicketPreviewRepository suggestedTicketPreviewRepository;

    @Inject
    public SuggestedTicketPreviewPresenterImpl(SuggestedTicketPreviewRepository suggestedTicketPreviewRepository) {
        this.suggestedTicketPreviewRepository = suggestedTicketPreviewRepository;
    }

    @Override
    public void acceptTicketSuggestion(String ticketSuggestionId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                .setSuggestionId(ticketSuggestionId)
                .build();

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addSuggestions(ticketSuggestion)
                .build();

        GlobalUtils.showLog(TAG, "ticket suggestion check: " + ticketSuggestionReq);

        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        ticketObservable = service.acceptTicketSuggestion(token, serviceId, ticketSuggestionReq);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse ticketBaseResponse) {
                        GlobalUtils.showLog(TAG, "accept suggestions response:"
                                + ticketBaseResponse);

                        getView().hideProgressBar();

                        if (ticketBaseResponse == null) {
                            getView().acceptTicketSuggestionFail("Failed to accept suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().acceptTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().acceptTicketSuggestionSuccess(ticketSuggestionId);
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

    @Override
    public void rejectTicketSuggestion(String ticketSuggestionId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                .setSuggestionId(ticketSuggestionId)
                .build();

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addSuggestions(ticketSuggestion)
                .build();

        GlobalUtils.showLog(TAG, "ticket suggestion check: " + ticketSuggestionReq);

        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        ticketObservable = service.rejectTicketSuggestion(token, serviceId, ticketSuggestionReq);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse ticketBaseResponse) {
                        GlobalUtils.showLog(TAG, "reject suggestions response:"
                                + ticketBaseResponse);

                        getView().hideProgressBar();

                        if (ticketBaseResponse == null) {
                            getView().rejectTicketSuggestionFail("Failed to reject suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().rejectTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().rejectTicketSuggestionSuccess(ticketSuggestionId);
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

    @Override
    public void getTicketHistory(String suggestionId) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        ticketObservable = service.getTicketSuggestionById(token, suggestionId);

        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                    @Override
                    public void onNext(TicketServiceRpcProto.TicketBaseResponse ticketBaseResponse) {
                        GlobalUtils.showLog(TAG, "get ticket history response:"
                                + ticketBaseResponse);

                        getView().hideProgressBar();

                        if (ticketBaseResponse == null) {
                            getView().getTicketHistoryFail("Failed to ticket history");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().getTicketHistoryFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().getTicketHistorySuccess(ticketBaseResponse.getTicketSuggestion());
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
}
