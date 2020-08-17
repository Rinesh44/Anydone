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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Service
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface {

    static final class ServiceColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long serviceIdIndex;
        long nameIndex;
        long descIndex;
        long serviceTypeIndex;
        long serviceIconUrlIndex;
        long createdAtIndex;
        long serviceAttributesListIndex;

        ServiceColumnInfo(OsSchemaInfo schemaInfo) {
            super(7);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Service");
            this.serviceIdIndex = addColumnDetails("serviceId", "serviceId", objectSchemaInfo);
            this.nameIndex = addColumnDetails("name", "name", objectSchemaInfo);
            this.descIndex = addColumnDetails("desc", "desc", objectSchemaInfo);
            this.serviceTypeIndex = addColumnDetails("serviceType", "serviceType", objectSchemaInfo);
            this.serviceIconUrlIndex = addColumnDetails("serviceIconUrl", "serviceIconUrl", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.serviceAttributesListIndex = addColumnDetails("serviceAttributesList", "serviceAttributesList", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceColumnInfo src = (ServiceColumnInfo) rawSrc;
            final ServiceColumnInfo dst = (ServiceColumnInfo) rawDst;
            dst.serviceIdIndex = src.serviceIdIndex;
            dst.nameIndex = src.nameIndex;
            dst.descIndex = src.descIndex;
            dst.serviceTypeIndex = src.serviceTypeIndex;
            dst.serviceIconUrlIndex = src.serviceIconUrlIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.serviceAttributesListIndex = src.serviceAttributesListIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Service> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Service>(this);
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
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'serviceId' cannot be changed after object was created.");
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
    public String realmGet$desc() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descIndex);
    }

    @Override
    public void realmSet$desc(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.descIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$serviceType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceTypeIndex);
    }

    @Override
    public void realmSet$serviceType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$serviceIconUrl() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceIconUrlIndex);
    }

    @Override
    public void realmSet$serviceIconUrl(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceIconUrlIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceIconUrlIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceIconUrlIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceIconUrlIndex, value);
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
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> realmGet$serviceAttributesList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (serviceAttributesListRealmList != null) {
            return serviceAttributesListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceAttributesListIndex);
            serviceAttributesListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class, osList, proxyState.getRealm$realm());
            return serviceAttributesListRealmList;
        }
    }

    @Override
    public void realmSet$serviceAttributesList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("serviceAttributesList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceAttributesListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Service", 7, 0);
        builder.addPersistedProperty("serviceId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("name", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("desc", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceIconUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedLinkProperty("serviceAttributesList", RealmFieldType.LIST, "ServiceAttributes");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Service";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Service";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Service createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(1);
        com.treeleaf.anydone.serviceprovider.realm.model.Service obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
            ServiceColumnInfo columnInfo = (ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
            long pkColumnIndex = columnInfo.serviceIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("serviceId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("serviceId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("serviceAttributesList")) {
                excludeFields.add("serviceAttributesList");
            }
            if (json.has("serviceId")) {
                if (json.isNull("serviceId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Service.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Service.class, json.getString("serviceId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'serviceId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) obj;
        if (json.has("name")) {
            if (json.isNull("name")) {
                objProxy.realmSet$name(null);
            } else {
                objProxy.realmSet$name((String) json.getString("name"));
            }
        }
        if (json.has("desc")) {
            if (json.isNull("desc")) {
                objProxy.realmSet$desc(null);
            } else {
                objProxy.realmSet$desc((String) json.getString("desc"));
            }
        }
        if (json.has("serviceType")) {
            if (json.isNull("serviceType")) {
                objProxy.realmSet$serviceType(null);
            } else {
                objProxy.realmSet$serviceType((String) json.getString("serviceType"));
            }
        }
        if (json.has("serviceIconUrl")) {
            if (json.isNull("serviceIconUrl")) {
                objProxy.realmSet$serviceIconUrl(null);
            } else {
                objProxy.realmSet$serviceIconUrl((String) json.getString("serviceIconUrl"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("serviceAttributesList")) {
            if (json.isNull("serviceAttributesList")) {
                objProxy.realmSet$serviceAttributesList(null);
            } else {
                objProxy.realmGet$serviceAttributesList().clear();
                JSONArray array = json.getJSONArray("serviceAttributesList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$serviceAttributesList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Service createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Service obj = new com.treeleaf.anydone.serviceprovider.realm.model.Service();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) obj;
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
                jsonHasPrimaryKey = true;
            } else if (name.equals("name")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$name((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$name(null);
                }
            } else if (name.equals("desc")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$desc((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$desc(null);
                }
            } else if (name.equals("serviceType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceType(null);
                }
            } else if (name.equals("serviceIconUrl")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceIconUrl((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceIconUrl(null);
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("serviceAttributesList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$serviceAttributesList(null);
                } else {
                    objProxy.realmSet$serviceAttributesList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$serviceAttributesList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'serviceId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Service copyOrUpdate(Realm realm, ServiceColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Service object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Service) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Service realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
            long pkColumnIndex = columnInfo.serviceIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceId();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Service copy(Realm realm, ServiceColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Service newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Service) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.nameIndex, realmObjectSource.realmGet$name());
        builder.addString(columnInfo.descIndex, realmObjectSource.realmGet$desc());
        builder.addString(columnInfo.serviceTypeIndex, realmObjectSource.realmGet$serviceType());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = realmObjectSource.realmGet$serviceAttributesList();
        if (serviceAttributesListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListRealmList = realmObjectCopy.realmGet$serviceAttributesList();
            serviceAttributesListRealmList.clear();
            for (int i = 0; i < serviceAttributesListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem = serviceAttributesListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes cacheserviceAttributesList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cache.get(serviceAttributesListItem);
                if (cacheserviceAttributesList != null) {
                    serviceAttributesListRealmList.add(cacheserviceAttributesList);
                } else {
                    serviceAttributesListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class), serviceAttributesListItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Service object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long tableNativePtr = table.getNativePtr();
        ServiceColumnInfo columnInfo = (ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long pkColumnIndex = columnInfo.serviceIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceId();
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
        String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        }
        String realmGet$desc = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$desc();
        if (realmGet$desc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descIndex, rowIndex, realmGet$desc, false);
        }
        String realmGet$serviceType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceType();
        if (realmGet$serviceType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, realmGet$serviceType, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$createdAt(), false);

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceAttributesList();
        if (serviceAttributesListList != null) {
            OsList serviceAttributesListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceAttributesListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem : serviceAttributesListList) {
                Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                if (cacheItemIndexserviceAttributesList == null) {
                    cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, serviceAttributesListItem, cache);
                }
                serviceAttributesListOsList.addRow(cacheItemIndexserviceAttributesList);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long tableNativePtr = table.getNativePtr();
        ServiceColumnInfo columnInfo = (ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long pkColumnIndex = columnInfo.serviceIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Service object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Service) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceId();
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
            String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$name();
            if (realmGet$name != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
            }
            String realmGet$desc = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$desc();
            if (realmGet$desc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descIndex, rowIndex, realmGet$desc, false);
            }
            String realmGet$serviceType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceType();
            if (realmGet$serviceType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, realmGet$serviceType, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$createdAt(), false);

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceAttributesList();
            if (serviceAttributesListList != null) {
                OsList serviceAttributesListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceAttributesListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem : serviceAttributesListList) {
                    Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                    if (cacheItemIndexserviceAttributesList == null) {
                        cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, serviceAttributesListItem, cache);
                    }
                    serviceAttributesListOsList.addRow(cacheItemIndexserviceAttributesList);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Service object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long tableNativePtr = table.getNativePtr();
        ServiceColumnInfo columnInfo = (ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long pkColumnIndex = columnInfo.serviceIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceId();
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
        String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$name();
        if (realmGet$name != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
        }
        String realmGet$desc = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$desc();
        if (realmGet$desc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descIndex, rowIndex, realmGet$desc, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descIndex, rowIndex, false);
        }
        String realmGet$serviceType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceType();
        if (realmGet$serviceType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, realmGet$serviceType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$createdAt(), false);

        OsList serviceAttributesListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceAttributesListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceAttributesList();
        if (serviceAttributesListList != null && serviceAttributesListList.size() == serviceAttributesListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = serviceAttributesListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem = serviceAttributesListList.get(i);
                Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                if (cacheItemIndexserviceAttributesList == null) {
                    cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, serviceAttributesListItem, cache);
                }
                serviceAttributesListOsList.setRow(i, cacheItemIndexserviceAttributesList);
            }
        } else {
            serviceAttributesListOsList.removeAll();
            if (serviceAttributesListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem : serviceAttributesListList) {
                    Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                    if (cacheItemIndexserviceAttributesList == null) {
                        cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, serviceAttributesListItem, cache);
                    }
                    serviceAttributesListOsList.addRow(cacheItemIndexserviceAttributesList);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long tableNativePtr = table.getNativePtr();
        ServiceColumnInfo columnInfo = (ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        long pkColumnIndex = columnInfo.serviceIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Service object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Service) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceId();
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
            String realmGet$name = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$name();
            if (realmGet$name != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nameIndex, rowIndex, realmGet$name, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nameIndex, rowIndex, false);
            }
            String realmGet$desc = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$desc();
            if (realmGet$desc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descIndex, rowIndex, realmGet$desc, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.descIndex, rowIndex, false);
            }
            String realmGet$serviceType = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceType();
            if (realmGet$serviceType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, realmGet$serviceType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceTypeIndex, rowIndex, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$createdAt(), false);

            OsList serviceAttributesListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceAttributesListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) object).realmGet$serviceAttributesList();
            if (serviceAttributesListList != null && serviceAttributesListList.size() == serviceAttributesListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = serviceAttributesListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem = serviceAttributesListList.get(i);
                    Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                    if (cacheItemIndexserviceAttributesList == null) {
                        cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, serviceAttributesListItem, cache);
                    }
                    serviceAttributesListOsList.setRow(i, cacheItemIndexserviceAttributesList);
                }
            } else {
                serviceAttributesListOsList.removeAll();
                if (serviceAttributesListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem : serviceAttributesListList) {
                        Long cacheItemIndexserviceAttributesList = cache.get(serviceAttributesListItem);
                        if (cacheItemIndexserviceAttributesList == null) {
                            cacheItemIndexserviceAttributesList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, serviceAttributesListItem, cache);
                        }
                        serviceAttributesListOsList.addRow(cacheItemIndexserviceAttributesList);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Service createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Service realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Service unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Service();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Service) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Service) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$serviceId(realmSource.realmGet$serviceId());
        unmanagedCopy.realmSet$name(realmSource.realmGet$name());
        unmanagedCopy.realmSet$desc(realmSource.realmGet$desc());
        unmanagedCopy.realmSet$serviceType(realmSource.realmGet$serviceType());
        unmanagedCopy.realmSet$serviceIconUrl(realmSource.realmGet$serviceIconUrl());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());

        // Deep copy of serviceAttributesList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$serviceAttributesList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> managedserviceAttributesListList = realmSource.realmGet$serviceAttributesList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> unmanagedserviceAttributesListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>();
            unmanagedCopy.realmSet$serviceAttributesList(unmanagedserviceAttributesListList);
            int nextDepth = currentDepth + 1;
            int size = managedserviceAttributesListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createDetachedCopy(managedserviceAttributesListList.get(i), nextDepth, maxDepth, cache);
                unmanagedserviceAttributesListList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Service update(Realm realm, ServiceColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Service realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Service newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.nameIndex, realmObjectSource.realmGet$name());
        builder.addString(columnInfo.descIndex, realmObjectSource.realmGet$desc());
        builder.addString(columnInfo.serviceTypeIndex, realmObjectSource.realmGet$serviceType());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListList = realmObjectSource.realmGet$serviceAttributesList();
        if (serviceAttributesListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> serviceAttributesListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>();
            for (int i = 0; i < serviceAttributesListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes serviceAttributesListItem = serviceAttributesListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes cacheserviceAttributesList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cache.get(serviceAttributesListItem);
                if (cacheserviceAttributesList != null) {
                    serviceAttributesListManagedCopy.add(cacheserviceAttributesList);
                } else {
                    serviceAttributesListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class), serviceAttributesListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.serviceAttributesListIndex, serviceAttributesListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.serviceAttributesListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>());
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
        StringBuilder stringBuilder = new StringBuilder("Service = proxy[");
        stringBuilder.append("{serviceId:");
        stringBuilder.append(realmGet$serviceId() != null ? realmGet$serviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{name:");
        stringBuilder.append(realmGet$name() != null ? realmGet$name() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{desc:");
        stringBuilder.append(realmGet$desc() != null ? realmGet$desc() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceType:");
        stringBuilder.append(realmGet$serviceType() != null ? realmGet$serviceType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceIconUrl:");
        stringBuilder.append(realmGet$serviceIconUrl() != null ? realmGet$serviceIconUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceAttributesList:");
        stringBuilder.append("RealmList<ServiceAttributes>[").append(realmGet$serviceAttributesList().size()).append("]");
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy aService = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aService.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aService.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aService.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
