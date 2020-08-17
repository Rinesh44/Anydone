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
public class com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Employee
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface {

    static final class EmployeeColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long employeeIdIndex;
        long accountIdIndex;
        long createdAtIndex;
        long employeeImageUrlIndex;

        EmployeeColumnInfo(OsSchemaInfo schemaInfo) {
            super(4);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Employee");
            this.employeeIdIndex = addColumnDetails("employeeId", "employeeId", objectSchemaInfo);
            this.accountIdIndex = addColumnDetails("accountId", "accountId", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.employeeImageUrlIndex = addColumnDetails("employeeImageUrl", "employeeImageUrl", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        EmployeeColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new EmployeeColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final EmployeeColumnInfo src = (EmployeeColumnInfo) rawSrc;
            final EmployeeColumnInfo dst = (EmployeeColumnInfo) rawDst;
            dst.employeeIdIndex = src.employeeIdIndex;
            dst.accountIdIndex = src.accountIdIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.employeeImageUrlIndex = src.employeeImageUrlIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private EmployeeColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Employee> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (EmployeeColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Employee>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$employeeId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.employeeIdIndex);
    }

    @Override
    public void realmSet$employeeId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'employeeId' cannot be changed after object was created.");
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
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.accountIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.accountIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.accountIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.accountIdIndex, value);
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
    public String realmGet$employeeImageUrl() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.employeeImageUrlIndex);
    }

    @Override
    public void realmSet$employeeImageUrl(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.employeeImageUrlIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.employeeImageUrlIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.employeeImageUrlIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.employeeImageUrlIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Employee", 4, 0);
        builder.addPersistedProperty("employeeId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("accountId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("employeeImageUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static EmployeeColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new EmployeeColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Employee";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Employee";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Employee createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.Employee obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
            EmployeeColumnInfo columnInfo = (EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
            long pkColumnIndex = columnInfo.employeeIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("employeeId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("employeeId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("employeeId")) {
                if (json.isNull("employeeId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class, json.getString("employeeId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'employeeId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) obj;
        if (json.has("accountId")) {
            if (json.isNull("accountId")) {
                objProxy.realmSet$accountId(null);
            } else {
                objProxy.realmSet$accountId((String) json.getString("accountId"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("employeeImageUrl")) {
            if (json.isNull("employeeImageUrl")) {
                objProxy.realmSet$employeeImageUrl(null);
            } else {
                objProxy.realmSet$employeeImageUrl((String) json.getString("employeeImageUrl"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Employee createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Employee obj = new com.treeleaf.anydone.serviceprovider.realm.model.Employee();
        final com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("employeeId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$employeeId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$employeeId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("accountId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$accountId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$accountId(null);
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("employeeImageUrl")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$employeeImageUrl((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$employeeImageUrl(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'employeeId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Employee copyOrUpdate(Realm realm, EmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Employee object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Employee realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
            long pkColumnIndex = columnInfo.employeeIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeId();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Employee copy(Realm realm, EmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Employee newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.employeeIdIndex, realmObjectSource.realmGet$employeeId());
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addString(columnInfo.employeeImageUrlIndex, realmObjectSource.realmGet$employeeImageUrl());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Employee object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long tableNativePtr = table.getNativePtr();
        EmployeeColumnInfo columnInfo = (EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long pkColumnIndex = columnInfo.employeeIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeId();
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
        String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$accountId();
        if (realmGet$accountId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$employeeImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeImageUrl();
        if (realmGet$employeeImageUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, realmGet$employeeImageUrl, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long tableNativePtr = table.getNativePtr();
        EmployeeColumnInfo columnInfo = (EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long pkColumnIndex = columnInfo.employeeIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Employee object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Employee) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeId();
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
            String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$accountId();
            if (realmGet$accountId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$employeeImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeImageUrl();
            if (realmGet$employeeImageUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, realmGet$employeeImageUrl, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Employee object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long tableNativePtr = table.getNativePtr();
        EmployeeColumnInfo columnInfo = (EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long pkColumnIndex = columnInfo.employeeIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeId();
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
        String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$accountId();
        if (realmGet$accountId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.accountIdIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$employeeImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeImageUrl();
        if (realmGet$employeeImageUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, realmGet$employeeImageUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long tableNativePtr = table.getNativePtr();
        EmployeeColumnInfo columnInfo = (EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        long pkColumnIndex = columnInfo.employeeIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Employee object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Employee) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeId();
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
            String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$accountId();
            if (realmGet$accountId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.accountIdIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$employeeImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) object).realmGet$employeeImageUrl();
            if (realmGet$employeeImageUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, realmGet$employeeImageUrl, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.employeeImageUrlIndex, rowIndex, false);
            }
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Employee createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Employee realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Employee unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Employee();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$employeeId(realmSource.realmGet$employeeId());
        unmanagedCopy.realmSet$accountId(realmSource.realmGet$accountId());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$employeeImageUrl(realmSource.realmGet$employeeImageUrl());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Employee update(Realm realm, EmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Employee realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Employee newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.employeeIdIndex, realmObjectSource.realmGet$employeeId());
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addString(columnInfo.employeeImageUrlIndex, realmObjectSource.realmGet$employeeImageUrl());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Employee = proxy[");
        stringBuilder.append("{employeeId:");
        stringBuilder.append(realmGet$employeeId() != null ? realmGet$employeeId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{accountId:");
        stringBuilder.append(realmGet$accountId() != null ? realmGet$accountId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{employeeImageUrl:");
        stringBuilder.append(realmGet$employeeImageUrl() != null ? realmGet$employeeImageUrl() : "null");
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
        com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy aEmployee = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aEmployee.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aEmployee.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aEmployee.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
