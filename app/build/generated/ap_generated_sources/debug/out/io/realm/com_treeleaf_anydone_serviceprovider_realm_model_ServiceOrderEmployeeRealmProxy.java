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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface {

    static final class ServiceOrderEmployeeColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long orderIdIndex;
        long serviceIdIndex;
        long createdAtIndex;
        long updatedAtIndex;
        long serviceDoerListIndex;

        ServiceOrderEmployeeColumnInfo(OsSchemaInfo schemaInfo) {
            super(5);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("ServiceOrderEmployee");
            this.orderIdIndex = addColumnDetails("orderId", "orderId", objectSchemaInfo);
            this.serviceIdIndex = addColumnDetails("serviceId", "serviceId", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.updatedAtIndex = addColumnDetails("updatedAt", "updatedAt", objectSchemaInfo);
            this.serviceDoerListIndex = addColumnDetails("serviceDoerList", "serviceDoerList", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceOrderEmployeeColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceOrderEmployeeColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceOrderEmployeeColumnInfo src = (ServiceOrderEmployeeColumnInfo) rawSrc;
            final ServiceOrderEmployeeColumnInfo dst = (ServiceOrderEmployeeColumnInfo) rawDst;
            dst.orderIdIndex = src.orderIdIndex;
            dst.serviceIdIndex = src.serviceIdIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.updatedAtIndex = src.updatedAtIndex;
            dst.serviceDoerListIndex = src.serviceDoerListIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceOrderEmployeeColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceOrderEmployeeColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$orderId() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.orderIdIndex);
    }

    @Override
    public void realmSet$orderId(long value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'orderId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$serviceId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceIdIndex);
    }

    @Override
    public void realmSet$serviceId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceIdIndex, value);
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
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> realmGet$serviceDoerList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (serviceDoerListRealmList != null) {
            return serviceDoerListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceDoerListIndex);
            serviceDoerListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class, osList, proxyState.getRealm$realm());
            return serviceDoerListRealmList;
        }
    }

    @Override
    public void realmSet$serviceDoerList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("serviceDoerList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceDoerListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("ServiceOrderEmployee", 5, 0);
        builder.addPersistedProperty("orderId", RealmFieldType.INTEGER, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("serviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("updatedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedLinkProperty("serviceDoerList", RealmFieldType.LIST, "ServiceDoer");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceOrderEmployeeColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceOrderEmployeeColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "ServiceOrderEmployee";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "ServiceOrderEmployee";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
            ServiceOrderEmployeeColumnInfo columnInfo = (ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
            long pkColumnIndex = columnInfo.orderIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("orderId")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("orderId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("serviceDoerList")) {
                excludeFields.add("serviceDoerList");
            }
            if (json.has("orderId")) {
                if (json.isNull("orderId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class, json.getLong("orderId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'orderId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) obj;
        if (json.has("serviceId")) {
            if (json.isNull("serviceId")) {
                objProxy.realmSet$serviceId(null);
            } else {
                objProxy.realmSet$serviceId((String) json.getString("serviceId"));
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
        if (json.has("serviceDoerList")) {
            if (json.isNull("serviceDoerList")) {
                objProxy.realmSet$serviceDoerList(null);
            } else {
                objProxy.realmGet$serviceDoerList().clear();
                JSONArray array = json.getJSONArray("serviceDoerList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$serviceDoerList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee obj = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("orderId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$orderId((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'orderId' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("serviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceId(null);
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
            } else if (name.equals("serviceDoerList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$serviceDoerList(null);
                } else {
                    objProxy.realmSet$serviceDoerList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$serviceDoerList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'orderId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee copyOrUpdate(Realm realm, ServiceOrderEmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
            long pkColumnIndex = columnInfo.orderIdIndex;
            long rowIndex = table.findFirstLong(pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee copy(Realm realm, ServiceOrderEmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.orderIdIndex, realmObjectSource.realmGet$orderId());
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.updatedAtIndex, realmObjectSource.realmGet$updatedAt());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = realmObjectSource.realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListRealmList = realmObjectCopy.realmGet$serviceDoerList();
            serviceDoerListRealmList.clear();
            for (int i = 0; i < serviceDoerListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer cacheserviceDoerList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cache.get(serviceDoerListItem);
                if (cacheserviceDoerList != null) {
                    serviceDoerListRealmList.add(cacheserviceDoerList);
                } else {
                    serviceDoerListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), serviceDoerListItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long tableNativePtr = table.getNativePtr();
        ServiceOrderEmployeeColumnInfo columnInfo = (ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long pkColumnIndex = columnInfo.orderIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$updatedAt(), false);

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                if (cacheItemIndexserviceDoerList == null) {
                    cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, serviceDoerListItem, cache);
                }
                serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long tableNativePtr = table.getNativePtr();
        ServiceOrderEmployeeColumnInfo columnInfo = (ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long pkColumnIndex = columnInfo.orderIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$updatedAt(), false);

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceDoerList();
            if (serviceDoerListList != null) {
                OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long tableNativePtr = table.getNativePtr();
        ServiceOrderEmployeeColumnInfo columnInfo = (ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long pkColumnIndex = columnInfo.orderIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
        }
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$updatedAt(), false);

        OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceDoerList();
        if (serviceDoerListList != null && serviceDoerListList.size() == serviceDoerListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = serviceDoerListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                if (cacheItemIndexserviceDoerList == null) {
                    cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                }
                serviceDoerListOsList.setRow(i, cacheItemIndexserviceDoerList);
            }
        } else {
            serviceDoerListOsList.removeAll();
            if (serviceDoerListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long tableNativePtr = table.getNativePtr();
        ServiceOrderEmployeeColumnInfo columnInfo = (ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        long pkColumnIndex = columnInfo.orderIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$orderId());
            }
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.updatedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$updatedAt(), false);

            OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) object).realmGet$serviceDoerList();
            if (serviceDoerListList != null && serviceDoerListList.size() == serviceDoerListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = serviceDoerListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.setRow(i, cacheItemIndexserviceDoerList);
                }
            } else {
                serviceDoerListOsList.removeAll();
                if (serviceDoerListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                        Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                        if (cacheItemIndexserviceDoerList == null) {
                            cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                        }
                        serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$orderId(realmSource.realmGet$orderId());
        unmanagedCopy.realmSet$serviceId(realmSource.realmGet$serviceId());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$updatedAt(realmSource.realmGet$updatedAt());

        // Deep copy of serviceDoerList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$serviceDoerList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> managedserviceDoerListList = realmSource.realmGet$serviceDoerList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> unmanagedserviceDoerListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
            unmanagedCopy.realmSet$serviceDoerList(unmanagedserviceDoerListList);
            int nextDepth = currentDepth + 1;
            int size = managedserviceDoerListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createDetachedCopy(managedserviceDoerListList.get(i), nextDepth, maxDepth, cache);
                unmanagedserviceDoerListList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee update(Realm realm, ServiceOrderEmployeeColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee realmObject, com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addInteger(columnInfo.orderIdIndex, realmObjectSource.realmGet$orderId());
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.updatedAtIndex, realmObjectSource.realmGet$updatedAt());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = realmObjectSource.realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
            for (int i = 0; i < serviceDoerListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer cacheserviceDoerList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cache.get(serviceDoerListItem);
                if (cacheserviceDoerList != null) {
                    serviceDoerListManagedCopy.add(cacheserviceDoerList);
                } else {
                    serviceDoerListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), serviceDoerListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.serviceDoerListIndex, serviceDoerListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.serviceDoerListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>());
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
        StringBuilder stringBuilder = new StringBuilder("ServiceOrderEmployee = proxy[");
        stringBuilder.append("{orderId:");
        stringBuilder.append(realmGet$orderId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceId:");
        stringBuilder.append(realmGet$serviceId() != null ? realmGet$serviceId() : "null");
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
        stringBuilder.append("{serviceDoerList:");
        stringBuilder.append("RealmList<ServiceDoer>[").append(realmGet$serviceDoerList().size()).append("]");
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy aServiceOrderEmployee = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aServiceOrderEmployee.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aServiceOrderEmployee.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aServiceOrderEmployee.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
