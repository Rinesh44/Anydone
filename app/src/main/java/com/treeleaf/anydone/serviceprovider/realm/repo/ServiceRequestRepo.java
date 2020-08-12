package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.google.android.gms.common.util.CollectionUtils;
import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.ProtoMapper;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class ServiceRequestRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ServiceRequestRepo serviceRequestRepo;
    private static final String TAG = "ServiceRequestRepo";

    static {
        serviceRequestRepo = new ServiceRequestRepo();
    }

    public static ServiceRequestRepo getInstance() {
        return serviceRequestRepo;
    }

    public void saveServiceRequest(final List<OrderServiceProto.ServiceOrder> serviceOrderListPb,
                                   final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                List<ServiceRequest> serviceRequestList =
                        transformServiceOrderProto(serviceOrderListPb);
                realm1.copyToRealmOrUpdate(serviceRequestList);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public ServiceRequest getServiceRequestById(long orderId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(ServiceRequest.class)
                    .equalTo("serviceOrderId", orderId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public void closeServiceRequest(long id) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        realm.executeTransaction(realm1 -> {
            RealmResults<ServiceRequest> result = realm1.where(ServiceRequest.class)
                    .equalTo("serviceOrderId", id).findAll();
            String status = OrderServiceProto.ServiceOrderState.CLOSED_SERVICE_ORDER.name();
            result.setString("status", status);
        });
    }

    public List<ServiceRequest> transformServiceOrderProto
            (List<OrderServiceProto.ServiceOrder> serviceOrderListPb) {
        if (CollectionUtils.isEmpty(serviceOrderListPb)) {
            throw new IllegalArgumentException(EXCEPTION_NULL_VALUE);
        }

        List<ServiceRequest> serviceRequestList = new ArrayList<>();
        for (OrderServiceProto.ServiceOrder orderPb : serviceOrderListPb
        ) {
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setServiceId(orderPb.getService().getServiceId());
            serviceRequest.setServiceOrderId(orderPb.getServiceOrderId());
            serviceRequest.setLanguage(orderPb.getLanguage());
            serviceRequest.setProblemStatement(orderPb.getProblemDesc());
            serviceRequest.setServiceName(GlobalUtils.convertCase(orderPb.getService().getName()));
            serviceRequest.setServiceIconUrl(orderPb.getService().getServiceIconUrl());
            serviceRequest.setStatus(orderPb.getServiceOrderState().name());
            serviceRequest.setCreatedAt(orderPb.getCreatedAt());
            serviceRequest.setAcceptedAt(orderPb.getAcceptedAt());
            serviceRequest.setStartedAt(orderPb.getStartedAt());
            serviceRequest.setCompletedAt(orderPb.getCompletedAt());
            serviceRequest.setClosedAt(orderPb.getClosedAt());
            serviceRequest.setRemote(orderPb.getRemote());
            GlobalUtils.showLog(TAG, "service provider rating checK: " +
                    orderPb.getServiceProviderAccount().getAverageRating());
            serviceRequest.setServiceProvider(ProtoMapper.transformServiceProvider(orderPb.getServiceProviderAccount()));
            serviceRequest.setAttributeList(ProtoMapper.getServiceAttributes(orderPb.getService()));
            serviceRequestList.add(serviceRequest);
        }

        return serviceRequestList;
    }


    public List<ServiceRequest> getAllServiceRequests() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(ServiceRequest.class)
                    .sort("createdAt", Sort.DESCENDING).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<ServiceRequest> getOpenServiceRequests() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(ServiceRequest.class)
                    .equalTo("status",
                            OrderServiceProto.ServiceOrderState.PENDING_SERVICE_ORDER.name())
                    .sort("createdAt", Sort.DESCENDING).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<ServiceRequest> getAcceptedServiceRequests() {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return new ArrayList<>(realm.where(ServiceRequest.class)
                    .notEqualTo("status",
                            OrderServiceProto.ServiceOrderState.PENDING_SERVICE_ORDER.name())
                    .sort("createdAt", Sort.DESCENDING).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}