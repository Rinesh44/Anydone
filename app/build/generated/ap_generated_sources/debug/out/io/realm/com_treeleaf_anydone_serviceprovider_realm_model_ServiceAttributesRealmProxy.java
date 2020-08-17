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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface {

    static final class ServiceAttributesColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long serviceIdIndex;
        long nameIndex;
        long serviceAttributeTypeIndex;
        long createdAtIndex;
        long valueIndex;
        long dateIndex;

        ServiceAttributesColumnInfo(OsSchemaInfo schemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("ServiceAttributes");
            this.serviceIdIndex = addColumnDetails("serviceId", "serviceId", objectSchemaInfo);
            this.nameIndex = addColumnDetails("name", "name", objectSchemaInfo);
            this.serviceAttributeTypeIndex = addColumnDetails("serviceAttributeType", "serviceAttributeType", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.valueIndex = addColumnDetails("value", "value", objectSchemaInfo);
            this.dateIndex = addColumnDetails("date", "date", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceAttributesColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceAttributesColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceAttributesColumnInfo src = (ServiceAttributesColumnInfo) rawSrc;
            final ServiceAttributesColumnInfo dst = (ServiceAttributesColumnInfo) rawDst;
            dst.serviceIdIndex = src.serviceIdIndex;
            dst.nameIndex = src.nameIndex;
            dst.serviceAttributeTypeIndex = src.serviceAttributeTypeIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.valueIndex = src.valueIndex;
            dst.dateIndex = src.dateIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceAttributesColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceAttributesColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
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
    public String realmGet$name() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nameIndex);
    }

    @Override
    public void realmSet$name(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.nameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$serviceAttributeType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceAttributeTypeIndex);
    }

    @Override
    public void realmSet$serviceAttributeType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceAttributeTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceAttributeTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceAttributeTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceAttributeTypeIndex, value);
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
    public String realmGet$value() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.valueIndex);
    }

    @Override
    public void realmSet$value(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.valueIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.valueIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.valueIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.valueIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$date() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.dateIndex);
    }

    @Override
    public void realmSet$date(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.dateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.dateIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("ServiceAttributes", 6, 0);
        builder.addPersistedProperty("serviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("name", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceAttributeType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("value", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("date", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceAttributesColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceAttributesColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "ServiceAttributes";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "ServiceAttributes";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes obj = realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class, true, excludeFields);

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) obj;
        if (json.has("serviceId")) {
            if (json.isNull("serviceId")) {
                objProxy.realmSet$serviceId(null);
            } else {
                objProxy.realmSet$serviceId((String) json.getString("serviceId"));
            }
        }
        if (json.has("name")) {
            if (json.isNull("name")) {
                objProxy.realmSet$name(null);
            } else {
                objProxy.realmSet$name((String) json.getString("name"));
            }
        }
        if (json.has("serviceAttributeType")) {
            if (json.isNull("serviceAttributeType")) {
                objProxy.realmSet$serviceAttributeType(null);
            } else {
                objProxy.realmSet$serviceAttributeType((String) json.getString("serviceAttributeType"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("value")) {
            if (json.isNull("value")) {
                objProxy.realmSet$value(null);
            } else {
                objProxy.realmSet$value((String) json.getString("value"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'date' to null.");
            } else {
                objProxy.realmSet$date((long) json.getLong("date"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes obj = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("serviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceId(null);
                }
            } else if (name.equals("name")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$name((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$name(null);
                }
            } else if (name.equals("serviceAttributeType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceAttributeType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceAttributeType(null);
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("value")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$value((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$value(null);
                }
            } else if (name.equals("date")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$date((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'date' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes copyOrUpdate(Realm realm, ServiceAttributesColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes copy(Realm realm, ServiceAttributesColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.nameIndex, realmObjectSource.realmGet$name());
        builder.addString(columnInfo.serviceAttributeTypeIndex, realmObjectSource.realmGet$serviceAttributeType());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addString(columnInfo.valueIndex, realmObjectSource.realmGet$value());
        builder.addInteger(columnInfo.dateIndex, realmObjectSource.realmGet$date());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long tableNativePtr = table.getNativePtr();
        ServiceAttributesColumnInfo columnInfo = (ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        }
        String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        }
        String realmGet$serviceAttributeType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceAttributeType();
        if (realmGet$serviceAttributeType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, realmGet$serviceAttributeType, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$value();
        if (realmGet$value != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valueIndex, rowIndex, realmGet$value, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.dateIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$date(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long tableNativePtr = table.getNativePtr();
        ServiceAttributesColumnInfo columnInfo = (ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            }
            String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$name();
            if (realmGet$name != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
            }
            String realmGet$serviceAttributeType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceAttributeType();
            if (realmGet$serviceAttributeType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, realmGet$serviceAttributeType, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$value();
            if (realmGet$value != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valueIndex, rowIndex, realmGet$value, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.dateIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$date(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long tableNativePtr = table.getNativePtr();
        ServiceAttributesColumnInfo columnInfo = (ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
        }
        String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
        }
        String realmGet$serviceAttributeType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceAttributeType();
        if (realmGet$serviceAttributeType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, realmGet$serviceAttributeType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$value();
        if (realmGet$value != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.valueIndex, rowIndex, realmGet$value, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.valueIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.dateIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$date(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        long tableNativePtr = table.getNativePtr();
        ServiceAttributesColumnInfo columnInfo = (ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
            }
            String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$name();
            if (realmGet$name != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
            }
            String realmGet$serviceAttributeType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$serviceAttributeType();
            if (realmGet$serviceAttributeType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, realmGet$serviceAttributeType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceAttributeTypeIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$value();
            if (realmGet$value != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.valueIndex, rowIndex, realmGet$value, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.valueIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.dateIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) object).realmGet$date(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$serviceId(realmSource.realmGet$serviceId());
        unmanagedCopy.realmSet$name(realmSource.realmGet$name());
        unmanagedCopy.realmSet$serviceAttributeType(realmSource.realmGet$serviceAttributeType());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$value(realmSource.realmGet$value());
        unmanagedCopy.realmSet$date(realmSource.realmGet$date());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ServiceAttributes = proxy[");
        stringBuilder.append("{serviceId:");
        stringBuilder.append(realmGet$serviceId() != null ? realmGet$serviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(realmGet$name() != null ? realmGet$name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceAttributeType:");
        stringBuilder.append(realmGet$serviceAttributeType() != null ? realmGet$serviceAttributeType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{value:");
        stringBuilder.append(realmGet$value() != null ? realmGet$value() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(realmGet$date());
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy aServiceAttributes = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aServiceAttributes.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aServiceAttributes.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aServiceAttributes.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
