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
public class com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Location
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface {

    static final class LocationColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long idIndex;
        long locationTypeIndex;
        long locationNameIndex;
        long isDefaultIndex;
        long latIndex;
        long lngIndex;

        LocationColumnInfo(OsSchemaInfo schemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Location");
            this.idIndex = addColumnDetails("id", "id", objectSchemaInfo);
            this.locationTypeIndex = addColumnDetails("locationType", "locationType", objectSchemaInfo);
            this.locationNameIndex = addColumnDetails("locationName", "locationName", objectSchemaInfo);
            this.isDefaultIndex = addColumnDetails("isDefault", "isDefault", objectSchemaInfo);
            this.latIndex = addColumnDetails("lat", "lat", objectSchemaInfo);
            this.lngIndex = addColumnDetails("lng", "lng", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        LocationColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new LocationColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final LocationColumnInfo src = (LocationColumnInfo) rawSrc;
            final LocationColumnInfo dst = (LocationColumnInfo) rawDst;
            dst.idIndex = src.idIndex;
            dst.locationTypeIndex = src.locationTypeIndex;
            dst.locationNameIndex = src.locationNameIndex;
            dst.isDefaultIndex = src.isDefaultIndex;
            dst.latIndex = src.latIndex;
            dst.lngIndex = src.lngIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private LocationColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Location> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (LocationColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Location>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$id() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.idIndex);
    }

    @Override
    public void realmSet$id(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'id' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$locationType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.locationTypeIndex);
    }

    @Override
    public void realmSet$locationType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.locationTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.locationTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.locationTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.locationTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$locationName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.locationNameIndex);
    }

    @Override
    public void realmSet$locationName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.locationNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.locationNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.locationNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.locationNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isDefault() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isDefaultIndex);
    }

    @Override
    public void realmSet$isDefault(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isDefaultIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isDefaultIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public double realmGet$lat() {
        proxyState.getRealm$realm().checkIfValid();
        return (double) proxyState.getRow$realm().getDouble(columnInfo.latIndex);
    }

    @Override
    public void realmSet$lat(double value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setDouble(columnInfo.latIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setDouble(columnInfo.latIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public double realmGet$lng() {
        proxyState.getRealm$realm().checkIfValid();
        return (double) proxyState.getRow$realm().getDouble(columnInfo.lngIndex);
    }

    @Override
    public void realmSet$lng(double value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setDouble(columnInfo.lngIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setDouble(columnInfo.lngIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Location", 6, 0);
        builder.addPersistedProperty("id", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("locationType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("locationName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("isDefault", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("lat", RealmFieldType.DOUBLE, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("lng", RealmFieldType.DOUBLE, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static LocationColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new LocationColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Location";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Location";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Location createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.Location obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
            LocationColumnInfo columnInfo = (LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
            long pkColumnIndex = columnInfo.idIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("id")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("id"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Location.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Location.class, json.getString("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) obj;
        if (json.has("locationType")) {
            if (json.isNull("locationType")) {
                objProxy.realmSet$locationType(null);
            } else {
                objProxy.realmSet$locationType((String) json.getString("locationType"));
            }
        }
        if (json.has("locationName")) {
            if (json.isNull("locationName")) {
                objProxy.realmSet$locationName(null);
            } else {
                objProxy.realmSet$locationName((String) json.getString("locationName"));
            }
        }
        if (json.has("isDefault")) {
            if (json.isNull("isDefault")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isDefault' to null.");
            } else {
                objProxy.realmSet$isDefault((boolean) json.getBoolean("isDefault"));
            }
        }
        if (json.has("lat")) {
            if (json.isNull("lat")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'lat' to null.");
            } else {
                objProxy.realmSet$lat((double) json.getDouble("lat"));
            }
        }
        if (json.has("lng")) {
            if (json.isNull("lng")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'lng' to null.");
            } else {
                objProxy.realmSet$lng((double) json.getDouble("lng"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Location createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Location obj = new com.treeleaf.anydone.serviceprovider.realm.model.Location();
        final com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("id")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$id((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$id(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("locationType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$locationType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$locationType(null);
                }
            } else if (name.equals("locationName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$locationName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$locationName(null);
                }
            } else if (name.equals("isDefault")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isDefault((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isDefault' to null.");
                }
            } else if (name.equals("lat")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lat((double) reader.nextDouble());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'lat' to null.");
                }
            } else if (name.equals("lng")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$lng((double) reader.nextDouble());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'lng' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Location copyOrUpdate(Realm realm, LocationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Location object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Location) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Location realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
            long pkColumnIndex = columnInfo.idIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$id();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Location copy(Realm realm, LocationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Location newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Location) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.idIndex, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.locationTypeIndex, realmObjectSource.realmGet$locationType());
        builder.addString(columnInfo.locationNameIndex, realmObjectSource.realmGet$locationName());
        builder.addBoolean(columnInfo.isDefaultIndex, realmObjectSource.realmGet$isDefault());
        builder.addDouble(columnInfo.latIndex, realmObjectSource.realmGet$lat());
        builder.addDouble(columnInfo.lngIndex, realmObjectSource.realmGet$lng());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Location object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long tableNativePtr = table.getNativePtr();
        LocationColumnInfo columnInfo = (LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long pkColumnIndex = columnInfo.idIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$id();
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
        String realmGet$locationType = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationType();
        if (realmGet$locationType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, realmGet$locationType, false);
        }
        String realmGet$locationName = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationName();
        if (realmGet$locationName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationNameIndex, rowIndex, realmGet$locationName, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isDefaultIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$isDefault(), false);
        Table.nativeSetDouble(tableNativePtr, columnInfo.latIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lat(), false);
        Table.nativeSetDouble(tableNativePtr, columnInfo.lngIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lng(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long tableNativePtr = table.getNativePtr();
        LocationColumnInfo columnInfo = (LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long pkColumnIndex = columnInfo.idIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Location object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Location) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$id();
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
            String realmGet$locationType = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationType();
            if (realmGet$locationType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, realmGet$locationType, false);
            }
            String realmGet$locationName = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationName();
            if (realmGet$locationName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationNameIndex, rowIndex, realmGet$locationName, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isDefaultIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$isDefault(), false);
            Table.nativeSetDouble(tableNativePtr, columnInfo.latIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lat(), false);
            Table.nativeSetDouble(tableNativePtr, columnInfo.lngIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lng(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Location object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long tableNativePtr = table.getNativePtr();
        LocationColumnInfo columnInfo = (LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long pkColumnIndex = columnInfo.idIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$id();
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
        String realmGet$locationType = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationType();
        if (realmGet$locationType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, realmGet$locationType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, false);
        }
        String realmGet$locationName = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationName();
        if (realmGet$locationName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationNameIndex, rowIndex, realmGet$locationName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.locationNameIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isDefaultIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$isDefault(), false);
        Table.nativeSetDouble(tableNativePtr, columnInfo.latIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lat(), false);
        Table.nativeSetDouble(tableNativePtr, columnInfo.lngIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lng(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long tableNativePtr = table.getNativePtr();
        LocationColumnInfo columnInfo = (LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        long pkColumnIndex = columnInfo.idIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Location object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Location) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$id();
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
            String realmGet$locationType = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationType();
            if (realmGet$locationType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, realmGet$locationType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.locationTypeIndex, rowIndex, false);
            }
            String realmGet$locationName = ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$locationName();
            if (realmGet$locationName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationNameIndex, rowIndex, realmGet$locationName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.locationNameIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isDefaultIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$isDefault(), false);
            Table.nativeSetDouble(tableNativePtr, columnInfo.latIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lat(), false);
            Table.nativeSetDouble(tableNativePtr, columnInfo.lngIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) object).realmGet$lng(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Location createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Location realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Location unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Location();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Location) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Location) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());
        unmanagedCopy.realmSet$locationType(realmSource.realmGet$locationType());
        unmanagedCopy.realmSet$locationName(realmSource.realmGet$locationName());
        unmanagedCopy.realmSet$isDefault(realmSource.realmGet$isDefault());
        unmanagedCopy.realmSet$lat(realmSource.realmGet$lat());
        unmanagedCopy.realmSet$lng(realmSource.realmGet$lng());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Location update(Realm realm, LocationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Location realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Location newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.idIndex, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.locationTypeIndex, realmObjectSource.realmGet$locationType());
        builder.addString(columnInfo.locationNameIndex, realmObjectSource.realmGet$locationName());
        builder.addBoolean(columnInfo.isDefaultIndex, realmObjectSource.realmGet$isDefault());
        builder.addDouble(columnInfo.latIndex, realmObjectSource.realmGet$lat());
        builder.addDouble(columnInfo.lngIndex, realmObjectSource.realmGet$lng());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Location = proxy[");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{locationType:");
        stringBuilder.append(realmGet$locationType() != null ? realmGet$locationType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{locationName:");
        stringBuilder.append(realmGet$locationName() != null ? realmGet$locationName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isDefault:");
        stringBuilder.append(realmGet$isDefault());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lat:");
        stringBuilder.append(realmGet$lat());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lng:");
        stringBuilder.append(realmGet$lng());
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
        com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy aLocation = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aLocation.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aLocation.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aLocation.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
