package com.treeleaf.anydone.serviceprovider.inbox;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.InboxProto;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.rpc.ConversationRpcProto;
import com.treeleaf.anydone.rpc.InboxRpcProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.InboxRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ThreadRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class InboxPresenterImpl extends BasePresenter<InboxContract.InboxView> implements
        InboxContract.InboxPresenter {
    private static final String TAG = "InboxPresenterImpl";
    private InboxRepository inboxRepository;

    @Inject
    public InboxPresenterImpl(InboxRepository inboxRepository) {
        this.inboxRepository = inboxRepository;
    }

    @Override
    public void getServices() {
        Observable<ServiceRpcProto.ServiceBaseResponse> servicesObservable;
        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);

        servicesObservable = service.getServices(token);
        addSubscription(servicesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<ServiceRpcProto.ServiceBaseResponse>() {
                            @Override
                            public void onNext(@NonNull ServiceRpcProto.ServiceBaseResponse
                                                       getServicesBaseResponse) {
                                GlobalUtils.showLog(TAG, "get services response: "
                                        + getServicesBaseResponse);

                                if (getServicesBaseResponse.getError()) {
                                    getView().getServicesFail(getServicesBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        getServicesBaseResponse.getServicesList())) {
                                    saveAvailableServices(getServicesBaseResponse.
                                            getServicesList());
                                } else {
                                    getView().getServicesFail("Services Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getServicesFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }


    @Override
    public void getInboxMessages(boolean showProgress) {
        if (showProgress)
            getView().showProgressBar("Please wait");

        Observable<InboxRpcProto.InboxBaseResponse> inboxBaseResponseObservable;

        Retrofit retrofit = GlobalUtils.getRetrofitInstance();
        AnyDoneService anyDoneService = retrofit.create(AnyDoneService.class);
        String token = Hawk.get(Constants.TOKEN);
        String service = Hawk.get(Constants.SELECTED_SERVICE);

        inboxBaseResponseObservable = anyDoneService.getInboxList(token, service, 0, System.currentTimeMillis(),
                100, "DESC");
        addSubscription(inboxBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<InboxRpcProto.InboxBaseResponse>() {
                            @Override
                            public void onNext(@NonNull InboxRpcProto.InboxBaseResponse
                                                       inboxBaseResponse) {
                                GlobalUtils.showLog(TAG, "get inbox list response: "
                                        + inboxBaseResponse);

                                getView().hideProgressBar();

                                if (inboxBaseResponse.getError()) {
                                    getView().getInboxMessageFail(
                                            inboxBaseResponse.getMsg());
                                    return;
                                }

                                GlobalUtils.showLog(TAG, "inbox list size: " +
                                        inboxBaseResponse.getInboxResponse().getInboxList().size());
                                if (!CollectionUtils.isEmpty(
                                        inboxBaseResponse.getInboxResponse().getInboxList())) {
                                    saveInboxList(inboxBaseResponse.getInboxResponse().getInboxList());
                                } else {
                                    getView().getInboxMessageFail("Not found");
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                getView().hideProgressBar();
                                getView().getInboxMessageFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private void saveInboxList(List<InboxProto.Inbox> inboxList) {
        InboxRepo.getInstance().saveInboxes(inboxList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getInboxMessageSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG,
                        "error on saving inbox list");
            }
        });
    }

    private void saveAvailableServices(List<ServiceProto.Service> availableServicesList) {
        AvailableServicesRepo.getInstance().saveAvailableServices(availableServicesList,
                new Repo.Callback() {
                    @Override
                    public void success(Object o) {
                        getView().getServicesSuccess();
                    }

                    @Override
                    public void fail() {
                        getView().getServicesFail("failed to get services");
                    }
                });
    }
}
