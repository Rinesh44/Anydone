package io.realm;


import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import io.realm.ImportFlag;
import io.realm.ProxyUtils;
import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsList;
import io.realm.internal.OsObject;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.Property;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Row;
import io.realm.internal.Table;
import io.realm.internal.android.JsonUtils;
import io.realm.internal.objectstore.OsObjectBuilder;
import io.realm.log.RealmLog;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("all")
public class com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Account
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface {

    static final class AccountColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long accountIdIndex;
        long fullNameIndex;
        long emailIndex;
        long phoneIndex;
        long countryCodeIndex;
        long profilePicIndex;
        long accountTypeIndex;
        long statusIndex;
        long genderIndex;
        long currencyCodeIndex;
        long timezoneIndex;
        long addressIndex;
        long isEmailVerifiedIndex;
        long isPhoneVerifiedIndex;
        long isKycVerifiedIndex;
        long isFirstLoginIndex;
        long createdAtIndex;
        long updatedAtIndex;
        long locationRealmListIndex;

        AccountColumnInfo(OsSchemaInfo schemaInfo) {
            super(19);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Account");
            this.accountIdIndex = addColumnDetails("accountId", "accountId", objectSchemaInfo);
            this.fullNameIndex = addColumnDetails("fullName", "fullName", objectSchemaInfo);
            this.emailIndex = addColumnDetails("email", "email", objectSchemaInfo);
            this.phoneIndex = addColumnDetails("phone", "phone", objectSchemaInfo);
            this.countryCodeIndex = addColumnDetails("countryCode", "countryCode", objectSchemaInfo);
            this.profilePicIndex = addColumnDetails("profilePic", "profilePic", objectSchemaInfo);
            this.accountTypeIndex = addColumnDetails("accountType", "accountType", objectSchemaInfo);
            this.statusIndex = addColumnDetails("status", "status", objectSchemaInfo);
            this.genderIndex = addColumnDetails("gender", "gender", objectSchemaInfo);
            this.currencyCodeIndex = addColumnDetails("currencyCode", "currencyCode", objectSchemaInfo);
            this.timezoneIndex = addColumnDetails("timezone", "timezone", objectSchemaInfo);
            this.addressIndex = addColumnDetails("address", "address", objectSchemaInfo);
            this.isEmailVerifiedIndex = addColumnDetails("isEmailVerified", "isEmailVerified", objectSchemaInfo);
            this.isPhoneVerifiedIndex = addColumnDetails("isPhoneVerified", "isPhoneVerified", objectSchemaInfo);
            this.isKycVerifiedIndex = addColumnDetails("isKycVerified", "isKycVerified", objectSchemaInfo);
            this.isFirstLoginIndex = addColumnDetails("isFirstLogin", "isFirstLogin", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.updatedAtIndex = addColumnDetails("updatedAt", "updatedAt", objectSchemaInfo);
            this.locationRealmListIndex = addColumnDetails("locationRealmList", "locationRealmList", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        AccountColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new AccountColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final AccountColumnInfo src = (AccountColumnInfo) rawSrc;
            final AccountColumnInfo dst = (AccountColumnInfo) rawDst;
            dst.accountIdIndex = src.accountIdIndex;
            dst.fullNameIndex = src.fullNameIndex;
            dst.emailIndex = src.emailIndex;
            dst.phoneIndex = src.phoneIndex;
            dst.countryCodeIndex = src.countryCodeIndex;
            dst.profilePicIndex = src.profilePicIndex;
            dst.accountTypeIndex = src.accountTypeIndex;
            dst.statusIndex = src.statusIndex;
            dst.genderIndex = src.genderIndex;
            dst.currencyCodeIndex = src.currencyCodeIndex;
            dst.timezoneIndex = src.timezoneIndex;
            dst.addressIndex = src.addressIndex;
            dst.isEmailVerifiedIndex = src.isEmailVerifiedIndex;
            dst.isPhoneVerifiedIndex = src.isPhoneVerifiedIndex;
            dst.isKycVerifiedIndex = src.isKycVerifiedIndex;
            dst.isFirstLoginIndex = src.isFirstLoginIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.updatedAtIndex = src.updatedAtIndex;
            dst.locationRealmListIndex = src.locationRealmListIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private AccountColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Account> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (AccountColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Account>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$accountId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.accountIdIndex);
    }

    @Override
    public void realmSet$accountId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'accountId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fullName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fullNameIndex);
    }

    @Override
    public void realmSet$fullName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fullNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fullNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fullNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fullNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$email() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.emailIndex);
    }

    @Override
    public void realmSet$email(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.emailIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.emailIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.emailIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.emailIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$phone() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.phoneIndex);
    }

    @Override
    public void realmSet$phone(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.phoneIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.phoneIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.phoneIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.phoneIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$countryCode() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.countryCodeIndex);
    }

    @Override
    public void realmSet$countryCode(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.countryCodeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.countryCodeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.countryCodeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.countryCodeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$profilePic() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.profilePicIndex);
    }

    @Override
    public void realmSet$profilePic(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.profilePicIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.profilePicIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.profilePicIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.profilePicIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$accountType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.accountTypeIndex);
    }

    @Override
    public void realmSet$accountType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.accountTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.accountTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.accountTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.accountTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$status() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.statusIndex);
    }

    @Override
    public void realmSet$status(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.statusIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.statusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.statusIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.statusIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$gender() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.genderIndex);
    }

    @Override
    public void realmSet$gender(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.genderIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.genderIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.genderIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.genderIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$currencyCode() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.currencyCodeIndex);
    }

    @Override
    public void realmSet$currencyCode(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.currencyCodeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.currencyCodeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.currencyCodeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.currencyCodeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$timezone() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.timezoneIndex);
    }

    @Override
    public void realmSet$timezone(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.timezoneIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.timezoneIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.timezoneIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.timezoneIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$address() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.addressIndex);
    }

    @Override
    public void realmSet$address(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.addressIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.addressIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.addressIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.addressIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isEmailVerified() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isEmailVerifiedIndex);
    }

    @Override
    public void realmSet$isEmailVerified(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isEmailVerifiedIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isEmailVerifiedIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isPhoneVerified() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isPhoneVerifiedIndex);
    }

    @Override
    public void realmSet$isPhoneVerified(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isPhoneVerifiedIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isPhoneVerifiedIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isKycVerified() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isKycVerifiedIndex);
    }

    @Override
    public void realmSet$isKycVerified(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isKycVerifiedIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isKycVerifiedIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isFirstLogin() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isFirstLoginIndex);
    }

    @Override
    public void realmSet$isFirstLogin(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isFirstLoginIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isFirstLoginIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$createdAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.createdAtIndex);
    }

    @Override
    public void realmSet$createdAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.createdAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.createdAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$updatedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.updatedAtIndex);
    }

    @Override
    public void realmSet$updatedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.updatedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.updatedAtIndex, value);
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> realmGet$locationRealmList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (locationRealmListRealmList != null) {
            return locationRealmListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.locationRealmListIndex);
            locationRealmListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>(com.treeleaf.anydone.serviceprovider.realm.model.Location.class, osList, proxyState.getRealm$realm());
            return locationRealmListRealmList;
        }
    }

    @Override
    public void realmSet$locationRealmList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("locationRealmList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.Location item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.locationRealmListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.setRow(i, ((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        } else {
            osList.removeAll();
            if (value == null) {
                return;
            }
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Account", 19, 0);
        builder.addPersistedProperty("accountId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("phone", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("countryCode", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profilePic", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("accountType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("status", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("gender", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("currencyCode", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("timezone", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("address", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("isEmailVerified", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("isPhoneVerified", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("isKycVerified", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("isFirstLogin", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("updatedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedLinkProperty("locationRealmList", RealmFieldType.LIST, "Location");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static AccountColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new AccountColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Account";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Account";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Account createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        com.treeleaf.anydone.serviceprovider.realm.model.Account obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
            AccountColumnInfo columnInfo = (AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
            long pkColumnIndex = columnInfo.accountIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("accountId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("accountId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("locationRealmList")) {
                excludeFields.add("locationRealmList");
            }
            if (json.has("accountId")) {
                if (json.isNull("accountId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Account.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Account.class, json.getString("accountId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'accountId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) obj;
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                objProxy.realmSet$fullName(null);
            } else {
                objProxy.realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                objProxy.realmSet$email(null);
            } else {
                objProxy.realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("phone")) {
            if (json.isNull("phone")) {
                objProxy.realmSet$phone(null);
            } else {
                objProxy.realmSet$phone((String) json.getString("phone"));
            }
        }
        if (json.has("countryCode")) {
            if (json.isNull("countryCode")) {
                objProxy.realmSet$countryCode(null);
            } else {
                objProxy.realmSet$countryCode((String) json.getString("countryCode"));
            }
        }
        if (json.has("profilePic")) {
            if (json.isNull("profilePic")) {
                objProxy.realmSet$profilePic(null);
            } else {
                objProxy.realmSet$profilePic((String) json.getString("profilePic"));
            }
        }
        if (json.has("accountType")) {
            if (json.isNull("accountType")) {
                objProxy.realmSet$accountType(null);
            } else {
                objProxy.realmSet$accountType((String) json.getString("accountType"));
            }
        }
        if (json.has("status")) {
            if (json.isNull("status")) {
                objProxy.realmSet$status(null);
            } else {
                objProxy.realmSet$status((String) json.getString("status"));
            }
        }
        if (json.has("gender")) {
            if (json.isNull("gender")) {
                objProxy.realmSet$gender(null);
            } else {
                objProxy.realmSet$gender((String) json.getString("gender"));
            }
        }
        if (json.has("currencyCode")) {
            if (json.isNull("currencyCode")) {
                objProxy.realmSet$currencyCode(null);
            } else {
                objProxy.realmSet$currencyCode((String) json.getString("currencyCode"));
            }
        }
        if (json.has("timezone")) {
            if (json.isNull("timezone")) {
                objProxy.realmSet$timezone(null);
            } else {
                objProxy.realmSet$timezone((String) json.getString("timezone"));
            }
        }
        if (json.has("address")) {
            if (json.isNull("address")) {
                objProxy.realmSet$address(null);
            } else {
                objProxy.realmSet$address((String) json.getString("address"));
            }
        }
        if (json.has("isEmailVerified")) {
            if (json.isNull("isEmailVerified")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isEmailVerified' to null.");
            } else {
                objProxy.realmSet$isEmailVerified((boolean) json.getBoolean("isEmailVerified"));
            }
        }
        if (json.has("isPhoneVerified")) {
            if (json.isNull("isPhoneVerified")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isPhoneVerified' to null.");
            } else {
                objProxy.realmSet$isPhoneVerified((boolean) json.getBoolean("isPhoneVerified"));
            }
        }
        if (json.has("isKycVerified")) {
            if (json.isNull("isKycVerified")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isKycVerified' to null.");
            } else {
                objProxy.realmSet$isKycVerified((boolean) json.getBoolean("isKycVerified"));
            }
        }
        if (json.has("isFirstLogin")) {
            if (json.isNull("isFirstLogin")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isFirstLogin' to null.");
            } else {
                objProxy.realmSet$isFirstLogin((boolean) json.getBoolean("isFirstLogin"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("updatedAt")) {
            if (json.isNull("updatedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'updatedAt' to null.");
            } else {
                objProxy.realmSet$updatedAt((long) json.getLong("updatedAt"));
            }
        }
        if (json.has("locationRealmList")) {
            if (json.isNull("locationRealmList")) {
                objProxy.realmSet$locationRealmList(null);
            } else {
                objProxy.realmGet$locationRealmList().clear();
                JSONArray array = json.getJSONArray("locationRealmList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Location item = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$locationRealmList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Account createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Account obj = new com.treeleaf.anydone.serviceprovider.realm.model.Account();
        final com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("accountId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$accountId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$accountId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("fullName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fullName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fullName(null);
                }
            } else if (name.equals("email")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$email((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$email(null);
                }
            } else if (name.equals("phone")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$phone((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$phone(null);
                }
            } else if (name.equals("countryCode")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$countryCode((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$countryCode(null);
                }
            } else if (name.equals("profilePic")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profilePic((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profilePic(null);
                }
            } else if (name.equals("accountType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$accountType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$accountType(null);
                }
            } else if (name.equals("status")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$status((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$status(null);
                }
            } else if (name.equals("gender")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$gender((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$gender(null);
                }
            } else if (name.equals("currencyCode")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$currencyCode((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$currencyCode(null);
                }
            } else if (name.equals("timezone")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$timezone((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$timezone(null);
                }
            } else if (name.equals("address")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$address((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$address(null);
                }
            } else if (name.equals("isEmailVerified")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isEmailVerified((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isEmailVerified' to null.");
                }
            } else if (name.equals("isPhoneVerified")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isPhoneVerified((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isPhoneVerified' to null.");
                }
            } else if (name.equals("isKycVerified")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isKycVerified((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isKycVerified' to null.");
                }
            } else if (name.equals("isFirstLogin")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isFirstLogin((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isFirstLogin' to null.");
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("updatedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$updatedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'updatedAt' to null.");
                }
            } else if (name.equals("locationRealmList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$locationRealmList(null);
                } else {
                    objProxy.realmSet$locationRealmList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.Location item = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$locationRealmList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'accountId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Account copyOrUpdate(Realm realm, AccountColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Account object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null) {
            final BaseRealm otherRealm = ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm();
            if (otherRealm.threadId != realm.threadId) {
                throw new IllegalArgumentException("Objects which belong to Realm instances in other threads cannot be copied into this Realm instance.");
            }
            if (otherRealm.getPath().equals(realm.getPath())) {
                return object;
            }
        }
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        RealmObjectProxy cachedRealmObject = cache.get(object);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Account) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Account realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
            long pkColumnIndex = columnInfo.accountIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountId();
            long rowIndex = Table.NO_MATCH;
            if (value == null) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, value);
            }
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Account copy(Realm realm, AccountColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Account newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Account) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.countryCodeIndex, realmObjectSource.realmGet$countryCode());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());
        builder.addString(columnInfo.accountTypeIndex, realmObjectSource.realmGet$accountType());
        builder.addString(columnInfo.statusIndex, realmObjectSource.realmGet$status());
        builder.addString(columnInfo.genderIndex, realmObjectSource.realmGet$gender());
        builder.addString(columnInfo.currencyCodeIndex, realmObjectSource.realmGet$currencyCode());
        builder.addString(columnInfo.timezoneIndex, realmObjectSource.realmGet$timezone());
        builder.addString(columnInfo.addressIndex, realmObjectSource.realmGet$address());
        builder.addBoolean(columnInfo.isEmailVerifiedIndex, realmObjectSource.realmGet$isEmailVerified());
        builder.addBoolean(columnInfo.isPhoneVerifiedIndex, realmObjectSource.realmGet$isPhoneVerified());
        builder.addBoolean(columnInfo.isKycVerifiedIndex, realmObjectSource.realmGet$isKycVerified());
        builder.addBoolean(columnInfo.isFirstLoginIndex, realmObjectSource.realmGet$isFirstLogin());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.updatedAtIndex, realmObjectSource.realmGet$updatedAt());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = realmObjectSource.realmGet$locationRealmList();
        if (locationRealmListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListRealmList = realmObjectCopy.realmGet$locationRealmList();
            locationRealmListRealmList.clear();
            for (int i = 0; i < locationRealmListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem = locationRealmListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Location cachelocationRealmList = (com.treeleaf.anydone.serviceprovider.realm.model.Location) cache.get(locationRealmListItem);
                if (cachelocationRealmList != null) {
                    locationRealmListRealmList.add(cachelocationRealmList);
                } else {
                    locationRealmListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class), locationRealmListItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Account object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long tableNativePtr = table.getNativePtr();
        AccountColumnInfo columnInfo = (AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        }
        String realmGet$countryCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$countryCode();
        if (realmGet$countryCode != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, realmGet$countryCode, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        }
        String realmGet$accountType = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountType();
        if (realmGet$accountType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, realmGet$accountType, false);
        }
        String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$status();
        if (realmGet$status != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
        }
        String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        }
        String realmGet$currencyCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$currencyCode();
        if (realmGet$currencyCode != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, realmGet$currencyCode, false);
        }
        String realmGet$timezone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$timezone();
        if (realmGet$timezone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timezoneIndex, rowIndex, realmGet$timezone, false);
        }
        String realmGet$address = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$address();
        if (realmGet$address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isEmailVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isEmailVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isPhoneVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isPhoneVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isKycVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isKycVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isFirstLoginIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isFirstLogin(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$updatedAt(), false);

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$locationRealmList();
        if (locationRealmListList != null) {
            OsList locationRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.locationRealmListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem : locationRealmListList) {
                Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                if (cacheItemIndexlocationRealmList == null) {
                    cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insert(realm, locationRealmListItem, cache);
                }
                locationRealmListOsList.addRow(cacheItemIndexlocationRealmList);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long tableNativePtr = table.getNativePtr();
        AccountColumnInfo columnInfo = (AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Account object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Account) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            }
            String realmGet$countryCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$countryCode();
            if (realmGet$countryCode != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, realmGet$countryCode, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            }
            String realmGet$accountType = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountType();
            if (realmGet$accountType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, realmGet$accountType, false);
            }
            String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$status();
            if (realmGet$status != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
            }
            String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$gender();
            if (realmGet$gender != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
            }
            String realmGet$currencyCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$currencyCode();
            if (realmGet$currencyCode != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, realmGet$currencyCode, false);
            }
            String realmGet$timezone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$timezone();
            if (realmGet$timezone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timezoneIndex, rowIndex, realmGet$timezone, false);
            }
            String realmGet$address = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$address();
            if (realmGet$address != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isEmailVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isEmailVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isPhoneVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isPhoneVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isKycVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isKycVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isFirstLoginIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isFirstLogin(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$updatedAt(), false);

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$locationRealmList();
            if (locationRealmListList != null) {
                OsList locationRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.locationRealmListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem : locationRealmListList) {
                    Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                    if (cacheItemIndexlocationRealmList == null) {
                        cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insert(realm, locationRealmListItem, cache);
                    }
                    locationRealmListOsList.addRow(cacheItemIndexlocationRealmList);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Account object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long tableNativePtr = table.getNativePtr();
        AccountColumnInfo columnInfo = (AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
        }
        String realmGet$countryCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$countryCode();
        if (realmGet$countryCode != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, realmGet$countryCode, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
        }
        String realmGet$accountType = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountType();
        if (realmGet$accountType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, realmGet$accountType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, false);
        }
        String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$status();
        if (realmGet$status != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.statusIndex, rowIndex, false);
        }
        String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
        }
        String realmGet$currencyCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$currencyCode();
        if (realmGet$currencyCode != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, realmGet$currencyCode, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, false);
        }
        String realmGet$timezone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$timezone();
        if (realmGet$timezone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.timezoneIndex, rowIndex, realmGet$timezone, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.timezoneIndex, rowIndex, false);
        }
        String realmGet$address = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$address();
        if (realmGet$address != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.addressIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isEmailVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isEmailVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isPhoneVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isPhoneVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isKycVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isKycVerified(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isFirstLoginIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isFirstLogin(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$updatedAt(), false);

        OsList locationRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.locationRealmListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$locationRealmList();
        if (locationRealmListList != null && locationRealmListList.size() == locationRealmListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = locationRealmListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem = locationRealmListList.get(i);
                Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                if (cacheItemIndexlocationRealmList == null) {
                    cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, locationRealmListItem, cache);
                }
                locationRealmListOsList.setRow(i, cacheItemIndexlocationRealmList);
            }
        } else {
            locationRealmListOsList.removeAll();
            if (locationRealmListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem : locationRealmListList) {
                    Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                    if (cacheItemIndexlocationRealmList == null) {
                        cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, locationRealmListItem, cache);
                    }
                    locationRealmListOsList.addRow(cacheItemIndexlocationRealmList);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long tableNativePtr = table.getNativePtr();
        AccountColumnInfo columnInfo = (AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Account object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Account) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
            }
            String realmGet$countryCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$countryCode();
            if (realmGet$countryCode != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, realmGet$countryCode, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.countryCodeIndex, rowIndex, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
            }
            String realmGet$accountType = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$accountType();
            if (realmGet$accountType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, realmGet$accountType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.accountTypeIndex, rowIndex, false);
            }
            String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$status();
            if (realmGet$status != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.statusIndex, rowIndex, false);
            }
            String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$gender();
            if (realmGet$gender != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
            }
            String realmGet$currencyCode = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$currencyCode();
            if (realmGet$currencyCode != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, realmGet$currencyCode, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.currencyCodeIndex, rowIndex, false);
            }
            String realmGet$timezone = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$timezone();
            if (realmGet$timezone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.timezoneIndex, rowIndex, realmGet$timezone, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.timezoneIndex, rowIndex, false);
            }
            String realmGet$address = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$address();
            if (realmGet$address != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.addressIndex, rowIndex, realmGet$address, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.addressIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isEmailVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isEmailVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isPhoneVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isPhoneVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isKycVerifiedIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isKycVerified(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isFirstLoginIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$isFirstLogin(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$updatedAt(), false);

            OsList locationRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.locationRealmListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) object).realmGet$locationRealmList();
            if (locationRealmListList != null && locationRealmListList.size() == locationRealmListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = locationRealmListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem = locationRealmListList.get(i);
                    Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                    if (cacheItemIndexlocationRealmList == null) {
                        cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, locationRealmListItem, cache);
                    }
                    locationRealmListOsList.setRow(i, cacheItemIndexlocationRealmList);
                }
            } else {
                locationRealmListOsList.removeAll();
                if (locationRealmListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem : locationRealmListList) {
                        Long cacheItemIndexlocationRealmList = cache.get(locationRealmListItem);
                        if (cacheItemIndexlocationRealmList == null) {
                            cacheItemIndexlocationRealmList = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, locationRealmListItem, cache);
                        }
                        locationRealmListOsList.addRow(cacheItemIndexlocationRealmList);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Account createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Account realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Account unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Account();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Account) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Account) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$accountId(realmSource.realmGet$accountId());
        unmanagedCopy.realmSet$fullName(realmSource.realmGet$fullName());
        unmanagedCopy.realmSet$email(realmSource.realmGet$email());
        unmanagedCopy.realmSet$phone(realmSource.realmGet$phone());
        unmanagedCopy.realmSet$countryCode(realmSource.realmGet$countryCode());
        unmanagedCopy.realmSet$profilePic(realmSource.realmGet$profilePic());
        unmanagedCopy.realmSet$accountType(realmSource.realmGet$accountType());
        unmanagedCopy.realmSet$status(realmSource.realmGet$status());
        unmanagedCopy.realmSet$gender(realmSource.realmGet$gender());
        unmanagedCopy.realmSet$currencyCode(realmSource.realmGet$currencyCode());
        unmanagedCopy.realmSet$timezone(realmSource.realmGet$timezone());
        unmanagedCopy.realmSet$address(realmSource.realmGet$address());
        unmanagedCopy.realmSet$isEmailVerified(realmSource.realmGet$isEmailVerified());
        unmanagedCopy.realmSet$isPhoneVerified(realmSource.realmGet$isPhoneVerified());
        unmanagedCopy.realmSet$isKycVerified(realmSource.realmGet$isKycVerified());
        unmanagedCopy.realmSet$isFirstLogin(realmSource.realmGet$isFirstLogin());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$updatedAt(realmSource.realmGet$updatedAt());

        // Deep copy of locationRealmList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$locationRealmList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> managedlocationRealmListList = realmSource.realmGet$locationRealmList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> unmanagedlocationRealmListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>();
            unmanagedCopy.realmSet$locationRealmList(unmanagedlocationRealmListList);
            int nextDepth = currentDepth + 1;
            int size = managedlocationRealmListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location item = com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createDetachedCopy(managedlocationRealmListList.get(i), nextDepth, maxDepth, cache);
                unmanagedlocationRealmListList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Account update(Realm realm, AccountColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Account realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Account newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.countryCodeIndex, realmObjectSource.realmGet$countryCode());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());
        builder.addString(columnInfo.accountTypeIndex, realmObjectSource.realmGet$accountType());
        builder.addString(columnInfo.statusIndex, realmObjectSource.realmGet$status());
        builder.addString(columnInfo.genderIndex, realmObjectSource.realmGet$gender());
        builder.addString(columnInfo.currencyCodeIndex, realmObjectSource.realmGet$currencyCode());
        builder.addString(columnInfo.timezoneIndex, realmObjectSource.realmGet$timezone());
        builder.addString(columnInfo.addressIndex, realmObjectSource.realmGet$address());
        builder.addBoolean(columnInfo.isEmailVerifiedIndex, realmObjectSource.realmGet$isEmailVerified());
        builder.addBoolean(columnInfo.isPhoneVerifiedIndex, realmObjectSource.realmGet$isPhoneVerified());
        builder.addBoolean(columnInfo.isKycVerifiedIndex, realmObjectSource.realmGet$isKycVerified());
        builder.addBoolean(columnInfo.isFirstLoginIndex, realmObjectSource.realmGet$isFirstLogin());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.updatedAtIndex, realmObjectSource.realmGet$updatedAt());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListList = realmObjectSource.realmGet$locationRealmList();
        if (locationRealmListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location> locationRealmListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>();
            for (int i = 0; i < locationRealmListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Location locationRealmListItem = locationRealmListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Location cachelocationRealmList = (com.treeleaf.anydone.serviceprovider.realm.model.Location) cache.get(locationRealmListItem);
                if (cachelocationRealmList != null) {
                    locationRealmListManagedCopy.add(cachelocationRealmList);
                } else {
                    locationRealmListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class), locationRealmListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.locationRealmListIndex, locationRealmListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.locationRealmListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Location>());
        }

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Account = proxy[");
        stringBuilder.append("{accountId:");
        stringBuilder.append(realmGet$accountId() != null ? realmGet$accountId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phone:");
        stringBuilder.append(realmGet$phone() != null ? realmGet$phone() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{countryCode:");
        stringBuilder.append(realmGet$countryCode() != null ? realmGet$countryCode() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profilePic:");
        stringBuilder.append(realmGet$profilePic() != null ? realmGet$profilePic() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{accountType:");
        stringBuilder.append(realmGet$accountType() != null ? realmGet$accountType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{status:");
        stringBuilder.append(realmGet$status() != null ? realmGet$status() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gender:");
        stringBuilder.append(realmGet$gender() != null ? realmGet$gender() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{currencyCode:");
        stringBuilder.append(realmGet$currencyCode() != null ? realmGet$currencyCode() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{timezone:");
        stringBuilder.append(realmGet$timezone() != null ? realmGet$timezone() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{address:");
        stringBuilder.append(realmGet$address() != null ? realmGet$address() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isEmailVerified:");
        stringBuilder.append(realmGet$isEmailVerified());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isPhoneVerified:");
        stringBuilder.append(realmGet$isPhoneVerified());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isKycVerified:");
        stringBuilder.append(realmGet$isKycVerified());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isFirstLogin:");
        stringBuilder.append(realmGet$isFirstLogin());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{updatedAt:");
        stringBuilder.append(realmGet$updatedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{locationRealmList:");
        stringBuilder.append("RealmList<Location>[").append(realmGet$locationRealmList().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public ProxyState<?> realmGet$proxyState() {
        return proxyState;
    }

    @Override
    public int hashCode() {
        String realmName = proxyState.getRealm$realm().getPath();
        String tableName = proxyState.getRow$realm().getTable().getName();
        long rowIndex = proxyState.getRow$realm().getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy aAccount = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aAccount.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aAccount.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aAccount.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
