package com.treeleaf.anydone.serviceprovider.servicerequests;

import com.google.android.gms.common.util.CollectionUtils;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.model.FilterObject;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.repo.Repo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceRequestRepo;
import com.treeleaf.anydone.serviceprovider.rest.service.AnyDoneService;
import com.treeleaf.anydone.rpc.OrderServiceRpcProto;
import com.treeleaf.anydone.serviceprovider.utils.Constants;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import java.util.ArrayList;
import java.util.Calendar;
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

public class ServiceRequestPresenterImpl extends
        BasePresenter<ServiceRequestContract.ServiceRequestView>
        implements ServiceRequestContract.ServiceRequestPresenter {
    private static final String TAG = "ServiceRequestPresenter";

    private ServiceRequestRepository serviceRequestRepository;

    @Inject
    public ServiceRequestPresenterImpl(ServiceRequestRepository serviceRequestRepository) {
        this.serviceRequestRepository = serviceRequestRepository;
    }


    @Override
    public void getServiceNames(List<ServiceRequest> serviceList) {
        List<FilterObject> serviceNames = new ArrayList<>();
        List<FilterObject> serviceNamesDummy = new ArrayList<>();
        for (ServiceRequest requests : serviceList
        ) {
            if (serviceNames.size() > 0) {
                for (FilterObject obj : serviceNames
                ) {
                    if (!obj.getFilterName().contains(requests.getServiceName())) {
                        FilterObject filterObject = new FilterObject();
                        filterObject.setFilterName(requests.getServiceName());
                        filterObject.setType("service");
                        serviceNamesDummy.add(filterObject);
                    }

                    if (!obj.getFilterName().contains(requests.getProblemStatement())) {
                        FilterObject filterObject = new FilterObject();
                        filterObject.setFilterName(requests.getProblemStatement());
                        filterObject.setType("problem_stat");
                        serviceNamesDummy.add(filterObject);
                    }
                }
            } else {
                FilterObject filterObject1 = new FilterObject();
                filterObject1.setFilterName(requests.getServiceName());
                filterObject1.setType("service");
                serviceNames.add(filterObject1);

                FilterObject filterObject2 = new FilterObject();
                filterObject2.setFilterName(requests.getProblemStatement());
                filterObject2.setType("problem_stat");
                serviceNames.add(filterObject2);
            }
        }
        serviceNames.addAll(serviceNamesDummy);
        List<FilterObject> filtered = removeDuplicateData(serviceNames);
        getView().setServiceNames(filtered);
    }

    private List<FilterObject> removeDuplicateData(List<FilterObject> serviceNames) {
        List<FilterObject> filtered = new ArrayList<>(serviceNames);
        for (int i = 0; i < serviceNames.size(); i++) {
            for (int j = i + 1; j < serviceNames.size(); j++) {
                if (serviceNames.get(i).getFilterName().equalsIgnoreCase(serviceNames.get(j)
                        .getFilterName())) {
                    filtered.remove(serviceNames.get(i));
                }
            }
        }
        return filtered;
    }

    @Override
    public void getFromDateTime(String fromDate) {
        String[] dateSplit = fromDate.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1],
                Integer.parseInt(dateSplit[2])));
        getView().setFromDateTime(calendar.getTime().getTime());
    }

    @Override
    public void getTillDateTime(String tillDate) {
        String[] dateSplit = tillDate.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1],
                Integer.parseInt(dateSplit[2])));
        getView().setTillDateTime(calendar.getTime().getTime());
    }

    @Override
    public void getAcceptedServiceRequests(boolean showProgress) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceOrderObservable;

        String token = Hawk.get(Constants.TOKEN);

        getServiceOrderObservable = serviceRequestRepository.getAcceptedOrderService(token);
        addSubscription(getServiceOrderObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<OrderServiceRpcProto.OrderServiceBaseResponse>() {
                            @Override
                            public void onNext(OrderServiceRpcProto.OrderServiceBaseResponse
                                                       getOrderServiceBaseResponse) {
                                GlobalUtils.showLog(TAG, "get accepted service order response: "
                                        + getOrderServiceBaseResponse);

                                getView().hideProgressBar();
                                if (getOrderServiceBaseResponse == null) {
                                    getView().getAcceptedServiceRequestFail("Get accepted Orders failed");
                                    return;
                                }

                                if (getOrderServiceBaseResponse.getError()) {
                                    getView().getAcceptedServiceRequestFail(getOrderServiceBaseResponse.getMsg());
                                    return;
                                }

                                saveAcceptedServiceOrderToRealm(getOrderServiceBaseResponse.getServiceOrdersList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getAcceptedServiceRequestFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void getOpenServiceRequests(boolean showProgress) {
        if (showProgress) {
            getView().showProgressBar("Please wait...");
        }
        Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceOrderObservable;

        String token = Hawk.get(Constants.TOKEN);

        getServiceOrderObservable = serviceRequestRepository.getOpenOrderService(token);
        addSubscription(getServiceOrderObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<OrderServiceRpcProto.OrderServiceBaseResponse>() {
                            @Override
                            public void onNext(OrderServiceRpcProto.OrderServiceBaseResponse
                                                       getOrderServiceBaseResponse) {
                                GlobalUtils.showLog(TAG, "get open service order response: "
                                        + getOrderServiceBaseResponse);

                                getView().hideProgressBar();
                                if (getOrderServiceBaseResponse == null) {
                                    getView().getOpenServiceRequestFail("Get open Orders failed");
                                    return;
                                }

                                if (getOrderServiceBaseResponse.getError()) {
                                    getView().getOpenServiceRequestFail(getOrderServiceBaseResponse.getMsg());
                                    return;
                                }

                                saveOpenServiceOrderToRealm(getOrderServiceBaseResponse.getServiceOrdersList());
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().getOpenServiceRequestFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    @Override
    public void filterServiceRequests(String serviceName, long from, long to, String status) {
        getView().showProgressBar("Filtering...");
        Observable<OrderServiceRpcProto.OrderServiceBaseResponse> getServiceOrderObservable;

        String token = Hawk.get(Constants.TOKEN);
        Retrofit retrofit = getRetrofitInstance();
        AnyDoneService service = retrofit.create(AnyDoneService.class);
        String filterUrl = getFilterUrl(serviceName, from, to, status);

        getServiceOrderObservable = service.filterServiceRequests(token, filterUrl);
        addSubscription(getServiceOrderObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(
                        new DisposableObserver<OrderServiceRpcProto.OrderServiceBaseResponse>() {
                            @Override
                            public void onNext(OrderServiceRpcProto.OrderServiceBaseResponse
                                                       filterServiceBaseResponse) {
                                GlobalUtils.showLog(TAG, "filter service response: "
                                        + filterServiceBaseResponse);

                                getView().hideProgressBar();
                                if (filterServiceBaseResponse == null) {
                                    getView().onFilterRequestsFail("Filter service failed");
                                    return;
                                }

                                if (filterServiceBaseResponse.getError()) {
                                    getView().onFilterRequestsFail(filterServiceBaseResponse.getMsg());
                                    return;
                                }

                                if (!CollectionUtils.isEmpty(
                                        filterServiceBaseResponse.getServiceOrdersList())) {
                                    List<ServiceRequest> filteredServiceRequests = ServiceRequestRepo.
                                            getInstance().transformServiceOrderProto
                                            (filterServiceBaseResponse.getServiceOrdersList());

                                    getView().onFilterRequestsSuccess(filteredServiceRequests);
                                } else {
                                    getView().onFilterRequestsFail("Not found");
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                getView().hideProgressBar();
                                getView().onFilterRequestsFail(e.getLocalizedMessage());
                            }

                            @Override
                            public void onComplete() {
                                getView().hideProgressBar();
                            }
                        })
        );
    }

    private String getFilterUrl(String serviceName, long from, long to, String status) {
        StringBuilder filterUrlBuilder = new StringBuilder("service/order/consumer?");
        if (serviceName != null && !serviceName.isEmpty()) {
            filterUrlBuilder.append("service=");
            filterUrlBuilder.append(serviceName);
        }
        if (from != 0) {
            filterUrlBuilder.append("&from=");
            filterUrlBuilder.append(from);
        }
        if (to != 0) {
            filterUrlBuilder.append("&to=");
            filterUrlBuilder.append(to);
        }
        if (status != null && !status.isEmpty()) {
            filterUrlBuilder.append("&serviceState=");
            filterUrlBuilder.append(status);
        }
        return filterUrlBuilder.toString();
    }


    private Retrofit getRetrofitInstance() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                .build();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Override
    public void separateOngoingAndClosedRequests(List<ServiceRequest> serviceList,
                                                 int fragmentIndex, boolean filter) {
        List<ServiceRequest> onGoingRequests = new ArrayList<>();
        List<ServiceRequest> closedRequests = new ArrayList<>();
        GlobalUtils.showLog(TAG, "separate requests called");
        for (ServiceRequest request : serviceList) {
            switch (request.getStatus()) {
                case "ACCEPTED_SERVICE_ORDER":
                case "STARTED_SERVICE_ORDER":
                case "PENDING_SERVICE_ORDER":
                case "COMPLETED_SERVICE_ORDER":
                    onGoingRequests.add(request);
                    break;

                case "UNKNOWN_SERVICE_ORDER_STATE":
                case "CLOSED_SERVICE_ORDER":
                case "CANCELLED_SERVICE_ORDER":
                    closedRequests.add(request);
                    break;

                default:
                    break;
            }
        }
    }

    private void saveOpenServiceOrderToRealm
            (List<OrderServiceProto.ServiceOrder> serviceOrderList) {
        ServiceRequestRepo.getInstance().saveServiceRequest(serviceOrderList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getOpenServiceRequestSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save service order");
                getView().getOpenServiceRequestFail("Service order not found");
            }
        });
    }

    private void saveAcceptedServiceOrderToRealm
            (List<OrderServiceProto.ServiceOrder> serviceOrderList) {
        ServiceRequestRepo.getInstance().saveServiceRequest(serviceOrderList, new Repo.Callback() {
            @Override
            public void success(Object o) {
                getView().getAcceptedServiceRequestSuccess();
            }

            @Override
            public void fail() {
                GlobalUtils.showLog(TAG, "failed to save service order");
                getView().getAcceptedServiceRequestFail("Service order not found");
            }
        });
    }

}
