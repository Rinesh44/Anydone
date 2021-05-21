package com.anydone.desk.ticketsuggestions;

import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.model.TicketSuggestion;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

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
    public void acceptTicketSuggestion(List<TicketSuggestion> ticketSuggestions) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.TicketSuggestion> suggestionList = new ArrayList<>();
        for (TicketSuggestion suggestions : ticketSuggestions
        ) {
            GlobalUtils.showLog(TAG, "suggestion ids: " + suggestions.getSuggestionId());
            TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                    .setSuggestionId(suggestions.getSuggestionId())
                    .build();

            suggestionList.add(ticketSuggestion);
        }

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addAllSuggestions(suggestionList)
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

                        getView().acceptTicketSuggestionSuccess(ticketSuggestions);
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
    public void rejectTicketSuggestion(List<TicketSuggestion> ticketSuggestions) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        List<TicketProto.TicketSuggestion> suggestions = new ArrayList<>();
        for (TicketSuggestion suggestion : ticketSuggestions
        ) {
            GlobalUtils.showLog(TAG, "suggestion ids: " + suggestion.getSuggestionId());
            TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                    .setSuggestionId(suggestion.getSuggestionId())
                    .build();

            suggestions.add(ticketSuggestion);
        }

        TicketProto.TicketSuggestionReq ticketSuggestionReq = TicketProto.TicketSuggestionReq.newBuilder()
                .addAllSuggestions(suggestions)
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

                        getView().rejectTicketSuggestionSuccess(ticketSuggestions);
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
    public void acceptTicketSuggestion(TicketSuggestion suggestion) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                .setSuggestionId(suggestion.getSuggestionId())
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
                            getView().acceptParticularTicketSuggestionFail("Failed to accept suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().acceptParticularTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().acceptParticularTicketSuggestionSuccess(suggestion);
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
    public void rejectTicketSuggestion(TicketSuggestion suggestion) {
        getView().showProgressBar("Please wait...");
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        String token = Hawk.get(Constants.TOKEN);

        TicketProto.TicketSuggestion ticketSuggestion = TicketProto.TicketSuggestion.newBuilder()
                .setSuggestionId(suggestion.getSuggestionId())
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
                            getView().rejectParticularTicketSuggestionFail("Failed to reject suggestions");
                            return;
                        }

                        if (ticketBaseResponse.getError()) {
                            getView().rejectParticularTicketSuggestionFail(ticketBaseResponse.getMsg());
                            return;
                        }

                        getView().rejectParticularTicketSuggestionSuccess(suggestion);
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
