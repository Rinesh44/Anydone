package com.treeleaf.anydone.serviceprovider.servicerequestdetail.activityFragment;

import com.treeleaf.anydone.serviceprovider.base.presenter.BasePresenter;
import com.treeleaf.anydone.entities.ServiceProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ServiceRequestRepo;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;

import javax.inject.Inject;

import io.realm.RealmList;

public class ActivityPresenterImpl extends BasePresenter<ActivityContract.ActivityView>
        implements ActivityContract.ActivityPresenter {

    private static final String TAG = "ActivityPresenterImpl";
    private ActivityRepository activityRepository;

    @Inject
    public ActivityPresenterImpl(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    @Override
    public void getRequest(long requestId) {
        ServiceRequest serviceRequest = ServiceRequestRepo.getInstance()
                .getServiceRequestById(requestId);
        Account account = AccountRepo.getInstance().getAccount();
        if (serviceRequest != null) {
            String location = getLocation(serviceRequest);
            boolean isDateRange = checkIfDateRange(serviceRequest.getAttributeList());
            String date = "";
            if (isDateRange) {
                String fromDate = getFromDate(serviceRequest);
                String toDate = getToDate(serviceRequest);
                GlobalUtils.showLog(TAG, "from date: " + fromDate);
                GlobalUtils.showLog(TAG, "to date: " + toDate);
                if (!fromDate.isEmpty() && !toDate.isEmpty()) {
                    date = fromDate + " to " + toDate;
                }
            } else {
                date = getDate(serviceRequest);
            }

            String time = getTime(serviceRequest);
            getView().setRequestValues(account.getFullName(), account.getProfilePic(),
                    GlobalUtils.getDateTimeline(serviceRequest.getCreatedAt()),
                    location, date, time, serviceRequest.getStatus(),
                    serviceRequest.getServiceProvider(),
                    serviceRequest.getStartedAt(),
                    serviceRequest.getCompletedAt(),
                    serviceRequest.getAcceptedAt(),
                    serviceRequest.getClosedAt());

        } else {
            getView().getRequestFailed("Failed to fetch service request");
        }
    }


    private String getLocation(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getName().equals("location")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getName().equals("from")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getFromDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getServiceAttributeType()
                    .equalsIgnoreCase(ServiceProto.ServiceAttributeType.DATE_TIME_ATTRIBUTE.name())
                    && attribute.getName().equals("from")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getToDate(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getServiceAttributeType()
                    .equalsIgnoreCase(ServiceProto.ServiceAttributeType.DATE_TIME_ATTRIBUTE.name())
                    && attribute.getName().equals("to")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private String getTime(ServiceRequest serviceRequest) {
        for (ServiceAttributes attribute : serviceRequest.getAttributeList()
        ) {
            if (attribute.getName().equals("time")) {
                return attribute.getValue();
            }
        }
        return "";
    }

    private boolean checkIfDateRange(RealmList<ServiceAttributes> attributeList) {
        for (ServiceAttributes attribute : attributeList
        ) {
            if (attribute.getName().equalsIgnoreCase("to")) {
                return true;
            }
        }

        return false;
    }

}
