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
public class com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Customer
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface {

    static final class CustomerColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long customerIdIndex;
        long fullNameIndex;
        long phoneIndex;
        long emailIndex;
        long profilePicIndex;

        CustomerColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Customer");
            this.customerIdIndex = addColumnDetails("customerId", "customerId", objectSchemaInfo);
            this.fullNameIndex = addColumnDetails("fullName", "fullName", objectSchemaInfo);
            this.phoneIndex = addColumnDetails("phone", "phone", objectSchemaInfo);
            this.emailIndex = addColumnDetails("email", "email", objectSchemaInfo);
            this.profilePicIndex = addColumnDetails("profilePic", "profilePic", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CustomerColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CustomerColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CustomerColumnInfo src = (CustomerColumnInfo) rawSrc;
            final CustomerColumnInfo dst = (CustomerColumnInfo) rawDst;
            dst.customerIdIndex = src.customerIdIndex;
            dst.fullNameIndex = src.fullNameIndex;
            dst.phoneIndex = src.phoneIndex;
            dst.emailIndex = src.emailIndex;
            dst.profilePicIndex = src.profilePicIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CustomerColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Customer> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CustomerColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Customer>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$customerId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.customerIdIndex);
    }

    @Override
    public void realmSet$customerId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'customerId' cannot be changed after object was created.");
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

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Customer", 5, 0);
        builder.addPersistedProperty("customerId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("phone", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profilePic", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CustomerColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CustomerColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Customer";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Customer";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Customer createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.Customer obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
            CustomerColumnInfo columnInfo = (CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
            long pkColumnIndex = columnInfo.customerIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("customerId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("customerId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("customerId")) {
                if (json.isNull("customerId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class, json.getString("customerId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'customerId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) obj;
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                objProxy.realmSet$fullName(null);
            } else {
                objProxy.realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("phone")) {
            if (json.isNull("phone")) {
                objProxy.realmSet$phone(null);
            } else {
                objProxy.realmSet$phone((String) json.getString("phone"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                objProxy.realmSet$email(null);
            } else {
                objProxy.realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("profilePic")) {
            if (json.isNull("profilePic")) {
                objProxy.realmSet$profilePic(null);
            } else {
                objProxy.realmSet$profilePic((String) json.getString("profilePic"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Customer createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Customer obj = new com.treeleaf.anydone.serviceprovider.realm.model.Customer();
        final com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("customerId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$customerId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$customerId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("fullName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fullName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fullName(null);
                }
            } else if (name.equals("phone")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$phone((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$phone(null);
                }
            } else if (name.equals("email")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$email((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$email(null);
                }
            } else if (name.equals("profilePic")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profilePic((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profilePic(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'customerId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Customer copyOrUpdate(Realm realm, CustomerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Customer object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Customer realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
            long pkColumnIndex = columnInfo.customerIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$customerId();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Customer copy(Realm realm, CustomerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Customer newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.customerIdIndex, realmObjectSource.realmGet$customerId());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Customer object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long tableNativePtr = table.getNativePtr();
        CustomerColumnInfo columnInfo = (CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long pkColumnIndex = columnInfo.customerIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$customerId();
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
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long tableNativePtr = table.getNativePtr();
        CustomerColumnInfo columnInfo = (CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long pkColumnIndex = columnInfo.customerIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Customer object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Customer) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$customerId();
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
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Customer object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long tableNativePtr = table.getNativePtr();
        CustomerColumnInfo columnInfo = (CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long pkColumnIndex = columnInfo.customerIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$customerId();
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
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long tableNativePtr = table.getNativePtr();
        CustomerColumnInfo columnInfo = (CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        long pkColumnIndex = columnInfo.customerIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Customer object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Customer) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$customerId();
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
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
            }
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Customer createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Customer realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Customer unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Customer();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$customerId(realmSource.realmGet$customerId());
        unmanagedCopy.realmSet$fullName(realmSource.realmGet$fullName());
        unmanagedCopy.realmSet$phone(realmSource.realmGet$phone());
        unmanagedCopy.realmSet$email(realmSource.realmGet$email());
        unmanagedCopy.realmSet$profilePic(realmSource.realmGet$profilePic());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Customer update(Realm realm, CustomerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Customer realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Customer newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.customerIdIndex, realmObjectSource.realmGet$customerId());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Customer = proxy[");
        stringBuilder.append("{customerId:");
        stringBuilder.append(realmGet$customerId() != null ? realmGet$customerId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phone:");
        stringBuilder.append(realmGet$phone() != null ? realmGet$phone() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profilePic:");
        stringBuilder.append(realmGet$profilePic() != null ? realmGet$profilePic() : "null");
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
        com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy aCustomer = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCustomer.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCustomer.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCustomer.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
