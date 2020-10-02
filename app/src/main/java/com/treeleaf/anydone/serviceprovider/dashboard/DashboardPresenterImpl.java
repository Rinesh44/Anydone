package com.treeleaf.anydone.serviceprovider.dashboard;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.entities.TicketProto;
import com.treeleaf.anydone.rpc.ServiceRpcProto;
import com.treeleaf.anydone.rpc.TicketServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.serviceprovider.realm.repo.AvailableServicesRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.TicketStatRepo;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;

public class DashboardPresenterImpl extends BasePresenter<DashboardContract.DashboardView>
        implements DashboardContract.DashboardPresenter {

    private static final String TAG = "DashboardPresenterImpl";
    private DashboardRepository dashboardRepository;

    @Inject
    public DashboardPresenterImpl(DashboardRepository dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public void getServices() {
        Observable<ServiceRpcProto.ServiceBaseResponse> servicesObservable;

        String token = Hawk.get(Constants.TOKEN);

        servicesObservable = dashboardRepository.getServices(token);
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
                                    getView().getServicesFail("get services failed");
                                    return;
                                }

                                if (getServicesBaseResponse.getError()) {
                                    getView().getServicesFail(getServicesBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        getServicesBaseResponse.getServicesList())) {
                                    saveAvailableServices(getServicesBaseResponse.getServicesList());
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
    public void getTicketsByDate() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = dashboardRepository.getTicketByDate(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets by date response: "
                                        + getTicketBaseResponse);

                                if (getTicketBaseResponse == null) {
                                    getView().getTicketByDateFail("get tickets by date failed");
                                    return;
                                }

                                if (getTicketBaseResponse.getError()) {
                                    getView().getTicketByDateFail(getTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByDate(getTicketBaseResponse.getTicketsByStatusesList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getTicketByDateFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void saveTicketStatByDate(List<TicketProto.TicketStatByStatus> ticketsByStatusesList) {
        TicketStatRepo.getInstance().saveTicketStatListByDate(ticketsByStatusesList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getTicketByDateSuccess();
                GlobalUtils.showLog(TAG, "get ticket stat by date success");
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while saving ticket stat by date");
            }
        });
    }

    @Override
    public void getTicketByStatus() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = dashboardRepository.getTicketByStatus(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets by status response: "
                                        + getTicketBaseResponse);

                                if (getTicketBaseResponse == null) {
                                    getView().getTicketByStatusFail("get tickets by " +
                                            "status failed");
                                    return;
                                }

                                if (getTicketBaseResponse.getError()) {
                                    getView().getTicketByStatusFail(getTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByStatues(getTicketBaseResponse.getTicketsByStatus());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getTicketByStatusFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void saveTicketStatByStatues(TicketProto.TicketStatByStatus ticketsByStatus) {
        TicketStatRepo.getInstance().saveTicketByStatus(ticketsByStatus, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket stat by status");
                getView().getTicketByStatusSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while saving ticket stat by status");
            }
        });
    }

    @Override
    public void getTicketByPriority() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = dashboardRepository.getTicketByPriority(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets by priority response: "
                                        + getTicketBaseResponse);

                                if (getTicketBaseResponse == null) {
                                    getView().getTicketByPriorityFail("get tickets by priority failed");
                                    return;
                                }

                                if (getTicketBaseResponse.getError()) {
                                    getView().getTicketByPriorityFail(getTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByPriority(getTicketBaseResponse.getTicketsByPriority());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getTicketByPriorityFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void saveTicketStatByPriority(TicketProto.TicketStatByPriority ticketsByPriority) {
        TicketStatRepo.getInstance().saveTicketByPriority(ticketsByPriority, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket stat by priority");
                getView().getTicketByPrioritySuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while saving ticke stat by priority");
            }
        });
    }

    @Override
    public void getTicketBySource() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = dashboardRepository.getTicketBySource(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets by source response: "
                                        + getTicketBaseResponse);

                                if (getTicketBaseResponse == null) {
                                    getView().getTicketBySourceFail("get tickets by source failed");
                                    return;
                                }

                                if (getTicketBaseResponse.getError()) {
                                    getView().getTicketBySourceFail(getTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatBySource(getTicketBaseResponse.getTicketsBySource());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getTicketBySourceFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    private void saveTicketStatBySource(TicketProto.TicketStatBySource ticketsBySource) {
        TicketStatRepo.getInstance().saveTicketBySource(ticketsBySource, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket stat by source");
                getView().getTicketBySourceSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while saving ticket stat by source");
            }
        });
    }

    @Override
    public void getTicketByResolveTime() {
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketObservable = dashboardRepository.getTicketByResolvedTime(token, serviceId);
        addSubscription(ticketObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       getTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "get tickets by resolved" +
                                        " time response: "
                                        + getTicketBaseResponse);

                                if (getTicketBaseResponse == null) {
                                    getView().getTicketByResolvedTimeFail(
                                            "get tickets by resolved time failed");
                                    return;
                                }

                                if (getTicketBaseResponse.getError()) {
                                    getView().getTicketByResolvedTimeFail(getTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByResolvedTime(getTicketBaseResponse.getTicketsResolveTime());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getTicketByResolvedTimeFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {

                            }
                        })
        );
    }

    @Override
    public void filterByDate(long from, long to) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketBaseResponseObservable = dashboardRepository.filterTicketByDate(token, serviceId,
                from, to);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter stat response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().onFilterByDateFail("Filter stat failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().onFilterByDateFail(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByDate(filterTicketBaseResponse.
                                        getTicketsByStatusesList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterByDateFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterBySource(long from, long to) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketBaseResponseObservable = dashboardRepository.filterTicketBySource(token, serviceId,
                from, to);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter by source response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().onFilterBySourceFail("Filter by source failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().onFilterBySourceFail(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatBySource(filterTicketBaseResponse.
                                        getTicketsBySource());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterBySourceFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterByPriority(long from, long to) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketBaseResponseObservable = dashboardRepository.filterTicketByPriority(token, serviceId,
                from, to);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter by priority response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().onFilterByPriorityFail("Filter by priority failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().onFilterByPriorityFail(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByPriority(filterTicketBaseResponse.
                                        getTicketsByPriority());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterByPriorityFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterByStatus(long from, long to) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketBaseResponseObservable = dashboardRepository.filterTicketByStatus(token, serviceId,
                from, to);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter by status response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().onFilterByStatusFail("Filter by status failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().onFilterByStatusFail(filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByStatues(filterTicketBaseResponse.
                                        getTicketsByStatus());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterByStatusFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterByResolvedTime(long from, long to) {
        getView().showProgressBar("Filtering...");
        Observable<TicketServiceRpcProto.TicketBaseResponse> ticketBaseResponseObservable;

        String token = Hawk.get(Constants.TOKEN);
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);

        ticketBaseResponseObservable = dashboardRepository.filterTicketByResolvedTime(token, serviceId,
                from, to);
        addSubscription(ticketBaseResponseObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<TicketServiceRpcProto.TicketBaseResponse>() {
                            @Override
                            public void onNext(TicketServiceRpcProto.TicketBaseResponse
                                                       filterTicketBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter by resolved time response: "
                                        + filterTicketBaseResponse);

                                getView().hideProgressBar();
                                if (filterTicketBaseResponse == null) {
                                    getView().onFilterByResolvedTimeFail(
                                            "Filter by resolved time failed");
                                    return;
                                }

                                if (filterTicketBaseResponse.getError()) {
                                    getView().onFilterByResolvedTimeFail(
                                            filterTicketBaseResponse.getMsg());
                                    return;
                                }

                                saveTicketStatByResolvedTime(filterTicketBaseResponse.
                                        getTicketsResolveTime());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterByResolvedTimeFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private String getStatFilterUrl(long from, long to) {
        String serviceId = Hawk.get(Constants.SELECTED_SERVICE);
        StringBuilder filterUrlBuilder = new StringBuilder("tickets/date/" + serviceId + "?");

        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }
        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }
        return filterUrlBuilder.toString();
    }

    private void saveTicketStatByResolvedTime(TicketProto.TicketStatResolveTime ticketsResolveTime) {
        TicketStatRepo.getInstance().saveTicketByResolvedTime(ticketsResolveTime, new Repo.Callback() {
            @Override
            public void success(Object o) {
                GlobalUtils.showLog(TAG, "saved ticket stat by resolved time");
                getView().getTicketByResolvedTimeSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "error while saving ticket stat by resolved time");
            }
        });
    }

    private void saveAvailableServices
            (List<ServiceProto.Service> availableServicesList) {
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
