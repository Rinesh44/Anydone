package com.anydone.desk.threads;

import com.anydone.desk.realm.repo.ConversationThreadLabelRepo;
import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.anydone.desk.base.presenter.BasePresenter;
import com.anydone.desk.realm.repo.Repo;
import com.anydone.desk.realm.repo.ThreadRepo;
import com.anydone.desk.realm.repo.TicketSuggestionRepo;
import com.anydone.desk.rest.service.AnyDoneService;
import com.anydone.desk.utils.Constants;
import com.anydone.desk.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ThreadPresenterImpl extends BasePresenter<ThreadContract.ThreadView> implements
        ThreadContract.ThreadPresenter {
    private static final String TAG = "ThreadPresenterImpl";
    private ThreadRepository threadRepository;

    @Inject
    public ThreadPresenterImpl(ThreadRepository threadRepository) {
        this.threadRepository = threadRepository;
    }

    @Override
    public void getConversationThreads(boolean showProgress) {
        if (showProgress)
            getView().showProgressBar("Please wait");
        Observable<ConversationRpcProto.ConversationBaseResponse> threadBaseResponseObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);
        String service = Hawk.get(Constants.SELECTED_SERVICE);

        threadBaseResponseObservable = anyDoneService.getConversationThreads(token, service);
        addSubscription(threadBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse
                                                       conversationThreadBaseResponse) {
                                GlobalUtils.showLog(TAG, "get conversation threads response: "
                                        + conversationThreadBaseResponse);

                                getView().hideProgressBar();

                                if (conversationThreadBaseResponse.getError()) {
                                    getView().getConversationThreadFail(
                                            conversationThreadBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "conversation thread size: " +
                                        conversationThreadBaseResponse.getConversationsList().size());
                                if (!CollectionUtils.isEmpty(
                                        conversationThreadBaseResponse.getConversationsList())) {
                                    saveConversationThreads(conversationThreadBaseResponse
                                            .getConversationsList());
                                } else {
                                    getView().getConversationThreadFail("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getConversationThreadFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveConversationThreads(List<ConversationProto.ConversationThread> conversationsList) {
        ThreadRepo.getInstance().saveThreads(conversationsList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getConversationThreadSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "error on saving conversation threads");
            }
        });
    }

    @Override
    public void getTicketSuggestions() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = service.getTicketSuggestions(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(@NonNull TicketServiceRpcProto.TicketBaseResponse
                                                       ticketSuggestionResponse) {
                                GlobalUtils.showLog(TAG, "ticket suggestions response: "
                                        + ticketSuggestionResponse);

                                if (ticketSuggestionResponse.getError()) {
                                    getView().getTicketSuggestionFail(ticketSuggestionResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        ticketSuggestionResponse.getTicketSuggestionsList())) {
                                    TicketSuggestionRepo.getInstance().saveTicketSuggestionList(
                                            ticketSuggestionResponse.getTicketSuggestionsList(),
                                            ticketSuggestionResponse.getEstimatedTime(), new Repo.Callback() {
                                                @Override
                                                public void success(Object o) {
                                                    GlobalUtils.showLog(TAG, "ticket suggestions saved");
                                                    getView().getTicketSuggestionSuccess();
                                                }

                                                @Override
                                                public void fail() {
                                                    GlobalUtils.showLog(TAG, "failed to save ticket" +
                                                            "suggestions");
                                                }
                                            });

                                } else {
                                    getView().onNoTicketSuggestion();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().getTicketSuggestionFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                            }
                        })
        );
    }

    @Override
    public void getConversationLabels() {
        Observable<ConversationRpcProto.ConversationBaseResponse> conversationObservable;
        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);

        String selectedService = Hawk.get(Constants.SELECTED_SERVICE);
        conversationObservable = service.getConversationLabels(token, selectedService);

        addSubscription(conversationObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                    @Override
                    public void onNext(@NonNull ConversationRpcProto.ConversationBaseResponse conversationBaseResponse) {
                        GlobalUtils.showLog(TAG, "get conversation label response:"
                                + conversationBaseResponse.getLabelsList());

                        if (conversationBaseResponse.getError()) {
                            getView().getConversationLabelFail(conversationBaseResponse.getMsg());
                            return;
                        }

                        saveConversationLabels(conversationBaseResponse.getLabelsList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getView().hideProgressBar();
                        getView().onFailure(e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

    private void saveConversationLabels(List<ConversationProto.ConversationLabel> employeesList) {
        ConversationThreadLabelRepo.getInstance().saveLabelList(employeesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved conversation labels");
                getView().getConversationLabelSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save conversation " +
                        "labels");
            }
        });
    }

}
