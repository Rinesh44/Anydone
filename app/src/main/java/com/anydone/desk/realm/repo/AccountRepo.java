package com.anydone.desk.realm.repo;


import com.treeleaf.anydone.entities.AuthProto;
import com.treeleaf.anydone.entities.UserProto;
import com.anydone.desk.realm.model.Account;
import com.anydone.desk.realm.model.Location;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;

public class AccountRepo extends Repo {
    private static final AccountRepo accountRepo;

    static {
        accountRepo = new AccountRepo();
    }

    public static AccountRepo getInstance() {
        return accountRepo;
    }

    public void saveAccount(final AuthProto.LoginResponse loginResponse, boolean employee,
                            final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Account account;
                if (employee)
                    account = setEmployeeAccount(loginResponse.getUser().getEmployee(), realm1);
                else
                    account = setServiceProviderAccount(loginResponse.getUser().getServiceProvider(), realm1);
                realm1.copyToRealmOrUpdate(account);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    private Account setEmployeeAccount(UserProto.EmployeeProfile accountPb, Realm realm) {
        Account account = realm.where(Account.class)
                .equalTo(Account.ACCOUNT_ID, accountPb.getAccount().getAccountId())
                .findFirst();
        if (account != null) return account;
        return transformEmployeeAccount(realm.createObject(Account.class, accountPb.getAccount()
                .getAccountId()), accountPb, realm);
    }

    private Account setServiceProviderAccount(UserProto.ServiceProviderProfile accountPb, Realm realm) {
        Account account = realm.where(Account.class)
                .equalTo(Account.ACCOUNT_ID, accountPb.getAccount().getAccountId())
                .findFirst();
        if (account != null) return account;
        return transformServiceProviderAccount(realm.createObject(Account.class, accountPb.getAccount()
                .getAccountId()), accountPb, realm);
    }

    public void editEmployeeAccount(String accountId, UserProto.EmployeeProfile accountPb,
                                    final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Account account = realm.where(Account.class)
                        .equalTo(Account.ACCOUNT_ID, accountId)
                        .findFirst();
                if (account != null) {
                    Account accountModel = mapEditedEmployeeAccount(account, accountPb);
                    realm1.insertOrUpdate(accountModel);
                    callback.success(null);
                }
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void editServiceProviderAccount(String accountId, UserProto.ServiceProviderProfile accountPb,
                                           final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Account account = realm.where(Account.class)
                        .equalTo(Account.ACCOUNT_ID, accountId)
                        .findFirst();
                if (account != null) {
                    Account accountModel = mapEditedServiceProviderAccount(account, accountPb);
                    realm1.insertOrUpdate(accountModel);
                    callback.success(null);
                }
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void addProfilePicUrl(String profilePicUrl, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {

                Account currentAccount = getAccount();
                currentAccount.setProfilePic(profilePicUrl);

                realm1.insertOrUpdate(currentAccount);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void addEmail(String email, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();
        try {
            realm.executeTransaction(realm1 -> {

                Account currentAccount = getAccount();
                currentAccount.setEmail(email);

                realm1.insertOrUpdate(currentAccount);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }
    }

    public void addPhone(String phone, final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {

                Account currentAccount = getAccount();
                currentAccount.setPhone(phone);

                realm1.insertOrUpdate(currentAccount);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }

    }

    public void setEmailVerified(final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Account currentAccount = getAccount();
                currentAccount.setEmailVerified(true);

                realm1.insertOrUpdate(currentAccount);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }

    }

    public void setPhoneVerified(final Callback callback) {
        final Realm realm = Realm.getDefaultInstance();

        try {
            realm.executeTransaction(realm1 -> {
                Account currentAccount = getAccount();
                currentAccount.setPhoneVerified(true);

                realm1.insertOrUpdate(currentAccount);
                callback.success(null);
            });

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            callback.fail();
        } finally {
            close(realm);
        }

    }


    private Account transformEmployeeAccount(Account account, UserProto.EmployeeProfile accountPb, Realm
            realm) {
        account.setAccountType(accountPb.getAccount().getAccountType().name());
        account.setCountryCode(accountPb.getAccount().getCountryCode());
        account.setCreatedAt(accountPb.getCreatedAt());
        account.setUpdatedAt(accountPb.getUpdatedAt());
        account.setEmail(accountPb.getAccount().getEmail());
        account.setPhone(accountPb.getAccount().getPhone());
        account.setFullName(accountPb.getAccount().getFullName());
        account.setGender(accountPb.getGender().name());
        account.setEmailVerified(accountPb.getAccount().getIsEmailVerified());
        account.setPhoneVerified(accountPb.getAccount().getIsPhoneVerified());
        account.setKycVerified(accountPb.getAccount().getIsKycVerified());
        account.setProfilePic(accountPb.getAccount().getProfilePic());
        account.setStatus(accountPb.getAccount().getStatus().name());
        account.setTimezone(accountPb.getAccount().getTimezone());
        account.setAddress(accountPb.getAccount().getAddress());
        account.setCurrencyCode(accountPb.getAccount().getCurrencyCode());
   /*     account.setLocationRealmList(getLocations(realm,
                accountPb.getAccount().getLocationsList()));*/
        return account;
    }

    private Account transformServiceProviderAccount(Account account, UserProto.ServiceProviderProfile accountPb, Realm
            realm) {
        account.setAccountType(accountPb.getAccount().getAccountType().name());
        account.setCountryCode(accountPb.getAccount().getCountryCode());
        account.setCreatedAt(accountPb.getCreatedAt());
        account.setUpdatedAt(accountPb.getUpdatedAt());
        account.setEmail(accountPb.getAccount().getEmail());
        account.setPhone(accountPb.getAccount().getPhone());
        account.setFullName(accountPb.getAccount().getFullName());
//        account.setGender(accountPb.getGender().name());
        account.setEmailVerified(accountPb.getAccount().getIsEmailVerified());
        account.setPhoneVerified(accountPb.getAccount().getIsPhoneVerified());
        account.setKycVerified(accountPb.getAccount().getIsKycVerified());
        account.setProfilePic(accountPb.getAccount().getProfilePic());
        account.setStatus(accountPb.getAccount().getStatus().name());
        account.setTimezone(accountPb.getAccount().getTimezone());
        account.setAddress(accountPb.getAccount().getAddress());
        account.setCurrencyCode(accountPb.getAccount().getCurrencyCode());
        account.setLocationRealmList(getLocations(realm,
                accountPb.getAccount().getLocationsList()));
        return account;
    }

    private RealmList<Location> getLocations(Realm realm, List<UserProto.Location> locationsList) {
        RealmList<Location> locationRealmList = new RealmList<>();
        for (UserProto.Location locationPb : locationsList
        ) {
//            Location location = new Location();
            Location location = Realm.getDefaultInstance().createObject(Location.class, locationPb.getLocationId());
            location.setLat(locationPb.getLatitude());
            location.setLng(locationPb.getLongitude());
            location.setLocationType(locationPb.getLocationType().name());
            location.setLocationName(locationPb.getAddress());
            location.setDefault(locationPb.getIsDefault());
            locationRealmList.add(location);
        }
        return locationRealmList;
    }

    private Account mapEditedEmployeeAccount(Account account, UserProto.EmployeeProfile accountPb) {
        account.setAccountType(accountPb.getAccount().getAccountType().name());
        account.setCountryCode(accountPb.getAccount().getCountryCode());
        account.setCreatedAt(accountPb.getCreatedAt());
        account.setUpdatedAt(accountPb.getUpdatedAt());
        account.setFullName(accountPb.getAccount().getFullName());
        account.setGender(accountPb.getGender().name());
        account.setAddress(accountPb.getAccount().getAddress());
        account.setStatus(accountPb.getAccount().getStatus().name());
        return account;
    }

    private Account mapEditedServiceProviderAccount(Account account, UserProto.ServiceProviderProfile accountPb) {
        account.setAccountType(accountPb.getAccount().getAccountType().name());
        account.setCountryCode(accountPb.getAccount().getCountryCode());
        account.setCreatedAt(accountPb.getCreatedAt());
        account.setUpdatedAt(accountPb.getUpdatedAt());
        account.setFullName(accountPb.getAccount().getFullName());
//        account.setGender(accountPb.getGender().name());
        account.setAddress(accountPb.getAccount().getAddress());
        account.setStatus(accountPb.getAccount().getStatus().name());
        return account;
    }

    public Account getAccount() {
        final Realm realm = Realm.getDefaultInstance();
        try {
            return realm.where(Account.class).findFirst();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        } /*finally {
            close(realm);
        }*/
    }
}

