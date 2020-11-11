package com.treeleaf.anydone.serviceprovider.ticketsuggestions;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TicketSuggestionPresenterImpl extends BasePresenter<TicketSuggestionContract.TicketSuggestionView>
        implements TicketSuggestionContract.TicketSuggestionPresenter {

    private static final String TAG = "TicketSuggestionPresent";
    private TicketSuggestionRepository ticketSuggestionRepository;

    @Inject
    public TicketSuggestionPresenterImpl(TicketSuggestionRepository ticketSuggestionRepository) {
        this.ticketSuggestionRepository = ticketSuggestionRepository;
    }

    @Override
    public void acceptTicketSuggestion(List<String> ticketSuggestionIds) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.TicketSuggestion> suggestions = new ArrayList<>();
        for (String suggestionId : ticketSuggestionIds
        ) {
            GlobalUtils.showLog(TAG, "suggestion ids: " + suggestionId);
            TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                    .setSuggestionId(suggestionId)
                    .build();

            suggestions.add(ticketSuggestion);
        }

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addAllSuggestions(suggestions)
                .build();

        GlobalUtils.showLog(TAG, "ticket suggestion check: " + ticketSuggestionReq);

        ticketObservable = service.acceptTicketSuggestion(token, ticketSuggestionReq);

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

                        getView().acceptTicketSuggestionSuccess();
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
    public void rejectTicketSuggestion(List<String> ticketSuggestionIds) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.TicketSuggestion> suggestions = new ArrayList<>();
        for (String suggestionId : ticketSuggestionIds
        ) {
            GlobalUtils.showLog(TAG, "suggestion ids: " + suggestionId);
            TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                    .setSuggestionId(suggestionId)
                    .build();

            suggestions.add(ticketSuggestion);
        }

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addAllSuggestions(suggestions)
                .build();

        GlobalUtils.showLog(TAG, "ticket suggestion check: " + ticketSuggestionReq);

        ticketObservable = service.rejectTicketSuggestion(token, ticketSuggestionReq);

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

                        getView().rejectTicketSuggestionSuccess();
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

        ticketObservable = service.acceptTicketSuggestion(token, ticketSuggestionReq);

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
                            getView().acceptParticularTicketSuggestionFail("Failed to accept suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().acceptParticularTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().acceptParticularTicketSuggestionSuccess(ticketSuggestionId);
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

        ticketObservable = service.rejectTicketSuggestion(token, ticketSuggestionReq);

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
                            getView().rejectParticularTicketSuggestionFail("Failed to reject suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().rejectParticularTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().rejectParticularTicketSuggestionSuccess(ticketSuggestionId);
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
