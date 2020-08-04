package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.OrderServiceProto;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer;
import com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee;
import com.treeleaf.anydone.serviceprovider.utils.GlobalUtils;
import com.treeleaf.anydone.serviceprovider.utils.RealmUtils;

import io.realm.Realm;
import io.realm.RealmList;

public class ServiceOrderEmployeeRepo extends Repo {
    private static final String EXCEPTION_NULL_VALUE = "Cannot transform a null value";
    private static final ServiceOrderEmployeeRepo serviceOrderEmployeeRepo;
    private static final String TAG = "ServiceOrderEmployeeRep";

    static {
        serviceOrderEmployeeRepo = new ServiceOrderEmployeeRepo();
    }

    public static ServiceOrderEmployeeRepo getInstance() {
        return serviceOrderEmployeeRepo;
    }

    public void saveServiceOrderEmployee(final OrderServiceProto.ServiceOrder serviceOrder,
                                         final Callback callback) {
        final Realm realm = RealmUtils.getInstance().getRealm();

        try {
            realm.executeTransaction(realm1 -> {
                ServiceOrderEmployee serviceOrderEmployee =
                        transformServiceOrderEmployee(serviceOrder);
                GlobalUtils.showLog(TAG, "service order id check :  "
                        + serviceOrder.getServiceOrderId());
                realm1.copyToRealmOrUpdate(serviceOrderEmployee);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public static ServiceOrderEmployee transformServiceOrderEmployee
            (OrderServiceProto.ServiceOrder serviceOrder) {
        RealmList<ServiceDoer> serviceDoerList = new RealmList<>();
        ServiceOrderEmployee serviceOrderEmployee = new ServiceOrderEmployee();
        serviceOrderEmployee.setOrderId(serviceOrder.getServiceOrderId());
//        serviceOrderEmployee.setCreatedAt(serviceOrder.getCreatedAt());
        if (serviceOrder.getEmployeesAssignedList().size() > 0)
            serviceOrderEmployee.setCreatedAt(serviceOrder.getEmployeesAssignedList()
                    .get(0).getAssignedAt());
        serviceOrderEmployee.setUpdatedAt(serviceOrder.getUpdatedAt());
        serviceOrderEmployee.setServiceId(serviceOrder.getService().getServiceId());
        for (OrderServiceProto.EmployeesAssigned employeeProfile :
                serviceOrder.getEmployeesAssignedList()
        ) {
            ServiceDoer serviceDoer = new ServiceDoer();
            serviceDoer.setAssignedAt(employeeProfile.getAssignedAt());
            serviceDoer.setAccountId(employeeProfile.getServiceDoerAccount()
                    .getAccount().getAccountId());
            serviceDoer.setProfileId(employeeProfile.getServiceDoerAccount()
                    .getEmployeeProfileId());
            serviceDoer.setGender(employeeProfile.getServiceDoerAccount()
                    .getGender().name());
            serviceDoer.setEmail(employeeProfile.getServiceDoerAccount()
                    .getAccount().getEmail());
            serviceDoer.setPhone(employeeProfile.getServiceDoerAccount()
                    .getAccount().getPhone());
            serviceDoer.setFullName(employeeProfile.getServiceDoerAccount()
                    .getAccount().getFullName());
            serviceDoer.setProfilePic(employeeProfile.getServiceDoerAccount()
                    .getAccount().getProfilePic());

            GlobalUtils.showLog(TAG, "service doer rating check: " +
                    employeeProfile.getServiceDoerAccount().getAverageRating());
            serviceDoer.setAvgRating(employeeProfile.getServiceDoerAccount().getAverageRating());
            serviceDoer.setNoOfRating(employeeProfile.getServiceDoerAccount().getNumberOfRating());
            serviceDoerList.add(serviceDoer);
        }
        serviceOrderEmployee.setServiceDoerList(serviceDoerList);
        return serviceOrderEmployee;
    }

    public ServiceOrderEmployee getServiceOrderEmployeeById(long orderId) {
        final Realm realm = RealmUtils.getInstance().getRealm();
        try {
            return realm.where(ServiceOrderEmployee.class)
                    .equalTo("orderId", orderId).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }
}