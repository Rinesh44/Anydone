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
public class com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.KGraph
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface {

    static final class KGraphColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long idIndex;
        long prevIndex;
        long nextIndex;
        long titleIndex;
        long answerTypeIndex;
        long traverseIndex;

        KGraphColumnInfo(OsSchemaInfo schemaInfo) {
            super(6);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("KGraph");
            this.idIndex = addColumnDetails("id", "id", objectSchemaInfo);
            this.prevIndex = addColumnDetails("prev", "prev", objectSchemaInfo);
            this.nextIndex = addColumnDetails("next", "next", objectSchemaInfo);
            this.titleIndex = addColumnDetails("title", "title", objectSchemaInfo);
            this.answerTypeIndex = addColumnDetails("answerType", "answerType", objectSchemaInfo);
            this.traverseIndex = addColumnDetails("traverse", "traverse", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        KGraphColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new KGraphColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final KGraphColumnInfo src = (KGraphColumnInfo) rawSrc;
            final KGraphColumnInfo dst = (KGraphColumnInfo) rawDst;
            dst.idIndex = src.idIndex;
            dst.prevIndex = src.prevIndex;
            dst.nextIndex = src.nextIndex;
            dst.titleIndex = src.titleIndex;
            dst.answerTypeIndex = src.answerTypeIndex;
            dst.traverseIndex = src.traverseIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private KGraphColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (KGraphColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>(this);
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
    public String realmGet$prev() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.prevIndex);
    }

    @Override
    public void realmSet$prev(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.prevIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.prevIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.prevIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.prevIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$next() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.nextIndex);
    }

    @Override
    public void realmSet$next(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.nextIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.nextIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.nextIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.nextIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$title() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.titleIndex);
    }

    @Override
    public void realmSet$title(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.titleIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.titleIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.titleIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.titleIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$answerType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.answerTypeIndex);
    }

    @Override
    public void realmSet$answerType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.answerTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.answerTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.answerTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.answerTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$traverse() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.traverseIndex);
    }

    @Override
    public void realmSet$traverse(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.traverseIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.traverseIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("KGraph", 6, 0);
        builder.addPersistedProperty("id", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("prev", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("next", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("title", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("answerType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("traverse", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static KGraphColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new KGraphColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "KGraph";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "KGraph";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.KGraph createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.KGraph obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
            KGraphColumnInfo columnInfo = (KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
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
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("id")) {
                if (json.isNull("id")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class, json.getString("id"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'id'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) obj;
        if (json.has("prev")) {
            if (json.isNull("prev")) {
                objProxy.realmSet$prev(null);
            } else {
                objProxy.realmSet$prev((String) json.getString("prev"));
            }
        }
        if (json.has("next")) {
            if (json.isNull("next")) {
                objProxy.realmSet$next(null);
            } else {
                objProxy.realmSet$next((String) json.getString("next"));
            }
        }
        if (json.has("title")) {
            if (json.isNull("title")) {
                objProxy.realmSet$title(null);
            } else {
                objProxy.realmSet$title((String) json.getString("title"));
            }
        }
        if (json.has("answerType")) {
            if (json.isNull("answerType")) {
                objProxy.realmSet$answerType(null);
            } else {
                objProxy.realmSet$answerType((String) json.getString("answerType"));
            }
        }
        if (json.has("traverse")) {
            if (json.isNull("traverse")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'traverse' to null.");
            } else {
                objProxy.realmSet$traverse((boolean) json.getBoolean("traverse"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.KGraph createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.KGraph obj = new com.treeleaf.anydone.serviceprovider.realm.model.KGraph();
        final com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) obj;
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
            } else if (name.equals("prev")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$prev((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$prev(null);
                }
            } else if (name.equals("next")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$next((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$next(null);
                }
            } else if (name.equals("title")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$title((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$title(null);
                }
            } else if (name.equals("answerType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$answerType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$answerType(null);
                }
            } else if (name.equals("traverse")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$traverse((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'traverse' to null.");
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

    private static com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.KGraph copyOrUpdate(Realm realm, KGraphColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.KGraph object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.KGraph realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
            long pkColumnIndex = columnInfo.idIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$id();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.KGraph copy(Realm realm, KGraphColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.KGraph newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.idIndex, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.prevIndex, realmObjectSource.realmGet$prev());
        builder.addString(columnInfo.nextIndex, realmObjectSource.realmGet$next());
        builder.addString(columnInfo.titleIndex, realmObjectSource.realmGet$title());
        builder.addString(columnInfo.answerTypeIndex, realmObjectSource.realmGet$answerType());
        builder.addBoolean(columnInfo.traverseIndex, realmObjectSource.realmGet$traverse());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.KGraph object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long tableNativePtr = table.getNativePtr();
        KGraphColumnInfo columnInfo = (KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long pkColumnIndex = columnInfo.idIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$id();
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
        String realmGet$prev = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$prev();
        if (realmGet$prev != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.prevIndex, rowIndex, realmGet$prev, false);
        }
        String realmGet$next = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$next();
        if (realmGet$next != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nextIndex, rowIndex, realmGet$next, false);
        }
        String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        }
        String realmGet$answerType = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$answerType();
        if (realmGet$answerType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, realmGet$answerType, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.traverseIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$traverse(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long tableNativePtr = table.getNativePtr();
        KGraphColumnInfo columnInfo = (KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long pkColumnIndex = columnInfo.idIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.KGraph object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$id();
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
            String realmGet$prev = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$prev();
            if (realmGet$prev != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.prevIndex, rowIndex, realmGet$prev, false);
            }
            String realmGet$next = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$next();
            if (realmGet$next != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nextIndex, rowIndex, realmGet$next, false);
            }
            String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$title();
            if (realmGet$title != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
            }
            String realmGet$answerType = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$answerType();
            if (realmGet$answerType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, realmGet$answerType, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.traverseIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$traverse(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.KGraph object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long tableNativePtr = table.getNativePtr();
        KGraphColumnInfo columnInfo = (KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long pkColumnIndex = columnInfo.idIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$id();
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
        String realmGet$prev = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$prev();
        if (realmGet$prev != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.prevIndex, rowIndex, realmGet$prev, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.prevIndex, rowIndex, false);
        }
        String realmGet$next = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$next();
        if (realmGet$next != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.nextIndex, rowIndex, realmGet$next, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.nextIndex, rowIndex, false);
        }
        String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
        }
        String realmGet$answerType = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$answerType();
        if (realmGet$answerType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, realmGet$answerType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.traverseIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$traverse(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long tableNativePtr = table.getNativePtr();
        KGraphColumnInfo columnInfo = (KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        long pkColumnIndex = columnInfo.idIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.KGraph object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$id();
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
            String realmGet$prev = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$prev();
            if (realmGet$prev != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.prevIndex, rowIndex, realmGet$prev, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.prevIndex, rowIndex, false);
            }
            String realmGet$next = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$next();
            if (realmGet$next != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.nextIndex, rowIndex, realmGet$next, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.nextIndex, rowIndex, false);
            }
            String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$title();
            if (realmGet$title != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
            }
            String realmGet$answerType = ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$answerType();
            if (realmGet$answerType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, realmGet$answerType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.answerTypeIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.traverseIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) object).realmGet$traverse(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.KGraph createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.KGraph realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.KGraph unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.KGraph();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$id(realmSource.realmGet$id());
        unmanagedCopy.realmSet$prev(realmSource.realmGet$prev());
        unmanagedCopy.realmSet$next(realmSource.realmGet$next());
        unmanagedCopy.realmSet$title(realmSource.realmGet$title());
        unmanagedCopy.realmSet$answerType(realmSource.realmGet$answerType());
        unmanagedCopy.realmSet$traverse(realmSource.realmGet$traverse());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.KGraph update(Realm realm, KGraphColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.KGraph realmObject, com.treeleaf.anydone.serviceprovider.realm.model.KGraph newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.idIndex, realmObjectSource.realmGet$id());
        builder.addString(columnInfo.prevIndex, realmObjectSource.realmGet$prev());
        builder.addString(columnInfo.nextIndex, realmObjectSource.realmGet$next());
        builder.addString(columnInfo.titleIndex, realmObjectSource.realmGet$title());
        builder.addString(columnInfo.answerTypeIndex, realmObjectSource.realmGet$answerType());
        builder.addBoolean(columnInfo.traverseIndex, realmObjectSource.realmGet$traverse());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("KGraph = proxy[");
        stringBuilder.append("{id:");
        stringBuilder.append(realmGet$id() != null ? realmGet$id() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{prev:");
        stringBuilder.append(realmGet$prev() != null ? realmGet$prev() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{next:");
        stringBuilder.append(realmGet$next() != null ? realmGet$next() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{title:");
        stringBuilder.append(realmGet$title() != null ? realmGet$title() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{answerType:");
        stringBuilder.append(realmGet$answerType() != null ? realmGet$answerType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{traverse:");
        stringBuilder.append(realmGet$traverse());
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
        com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy aKGraph = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aKGraph.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aKGraph.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aKGraph.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
