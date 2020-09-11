package com.treeleaf.anydone.serviceprovider.threads;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ConversationProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

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

        String token = Hawk.get(Constants.TOKEN);
        String service = Hawk.get(Constants.SELECTED_SERVICE);

        threadBaseResponseObservable = threadRepository.getConversationThreads(token, service);
        addSubscription(threadBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ConversationRpcProto.ConversationBaseResponse>() {
                            @Override
                            public void onNext(ConversationRpcProto.ConversationBaseResponse
                                                       conversationThreadBaseResponse) {
                                GlobalUtils.showLog(TAG, "get conversation threads response: "
                                        + conversationThreadBaseResponse);

                                getView().hideProgressBar();
                                if (conversationThreadBaseResponse == null) {
                                    getView().getConversationThreadFail("get conversation thread failed");
                                    return;
                                }

                                if (conversationThreadBaseResponse.getError()) {
                                    getView().getConversationThreadFail(conversationThreadBaseResponse.getMsg());
                                    return;
                                }


                                GlobalUtils.showLog(TAG, "conversation thread size: " + conversationThreadBaseResponse.getConversationsList().size());
                                if (!CollectionUtils.isEmpty(
                                        conversationThreadBaseResponse.getConversationsList())) {
                                    saveConversationThreads(conversationThreadBaseResponse.getConversationsList());
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
                GlobalUtils.showLog(TAG, "error on saving conversation threads");
            }
        });
    }

    @Override
    public void getServices() {
        Observable<ServiceRpcProto.ServiceBaseResponse> servicesObservable;

        String token = Hawk.get(Constants.TOKEN);

        servicesObservable = threadRepository.getServices(token);
        addSubscription(servicesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ServiceRpcProto.ServiceBaseResponse>() {
                            @Override
                            public void onNext(ServiceRpcProto.ServiceBaseResponse
                                                       getServicesBaseResponse) {
                                GlobalUtils.showLog(TAG, "get services response: "
                                        + getServicesBaseResponse);

                                if (getServicesBaseResponse == null) {
                                    getView().getServiceFail("get services failed");
                                    return;
                                }

                                if (getServicesBaseResponse.getError()) {
                                    getView().getServiceFail(getServicesBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        getServicesBaseResponse.getAvailableServicesList())) {
                                    saveAvailableServices(getServicesBaseResponse.getAvailableServicesList());
                                } else {
                                    getView().getServiceFail("Services Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getServiceFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveAvailableServices(List<ServiceProto.AvailableService> availableServicesList) {
        AvailableServicesRepo.getInstance().saveAvailableServices(availableServicesList,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getServiceSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getServiceFail("failed to get services");
                    }
                });
    }
}
