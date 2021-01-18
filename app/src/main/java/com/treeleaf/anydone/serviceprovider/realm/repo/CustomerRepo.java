package com.treeleaf.anydone.serviceprovider.realm.repo;

import com.treeleaf.anydone.entities.UserProto;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Customer;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;

public class CustomerRepo extends Repo {
    private static final CustomerRepo CUSTOMER_REPO;

    static {
        CUSTOMER_REPO = new CustomerRepo();
    }

    public static CustomerRepo getInstance() {
        return CUSTOMER_REPO;
    }

    public void saveCustomerList(final List<UserProto.Customer> customerList, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {
                realm1.copyToRealmOrUpdate(transformCustomers(customerList));
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private List<Customer> transformCustomers(List<UserProto.Customer> customerList) {
        List<Customer> customersList = new ArrayList<>();
        for (UserProto.Customer profile : customerList
        ) {
            Customer customer = new Customer();
            customer.setProfilePic(profile.getProfilePic());
            customer.setFullName(profile.getFullName());
            customer.setCustomerId(profile.getCustomerId());
            customer.setEmail(profile.getEmail());
            customer.setPhone(profile.getPhone());
            customersList.add(customer);
        }
        return customersList;
    }


    public List<Customer> getAllCustomers() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            Account self = AccountRepo.getInstance().getAccount();
            if (self != null) {
                return new ArrayList<>(realm.where(Customer.class)
                        .notEqualTo("customerId", self.getAccountId())
                        .findAll());
            } else
                return new ArrayList<>(realm.where(Customer.class).findAll());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public Customer getCustomerById(String customerId) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Customer.class)
                    .equalTo("customerId", customerId)
                    .findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    public List<Customer> searchCustomers(String query) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            RealmQuery<Customer> result = performSearch(query, realm);
            return result.findAll();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } finally {
            close(realm);
        }
    }

    private RealmQuery<Customer> performSearch(String searchTerm, Realm realm) {
        RealmQuery<Customer> query = realm.where(Customer.class);
        query = query
                .contains("fullName", searchTerm, Case.INSENSITIVE)
                .or()
                .contains("phone", searchTerm)
                .or()
                .contains("email", searchTerm, Case.INSENSITIVE);
        return query;
    }
}
