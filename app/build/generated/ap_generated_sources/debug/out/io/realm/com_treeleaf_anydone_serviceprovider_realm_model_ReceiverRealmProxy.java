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
public class com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Receiver
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface {

    static final class ReceiverColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long receiverIdIndex;
        long senderIdIndex;
        long receiverTypeIndex;
        long messageStatusIndex;

        ReceiverColumnInfo(OsSchemaInfo schemaInfo) {
            super(4);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Receiver");
            this.receiverIdIndex = addColumnDetails("receiverId", "receiverId", objectSchemaInfo);
            this.senderIdIndex = addColumnDetails("senderId", "senderId", objectSchemaInfo);
            this.receiverTypeIndex = addColumnDetails("receiverType", "receiverType", objectSchemaInfo);
            this.messageStatusIndex = addColumnDetails("messageStatus", "messageStatus", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ReceiverColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ReceiverColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ReceiverColumnInfo src = (ReceiverColumnInfo) rawSrc;
            final ReceiverColumnInfo dst = (ReceiverColumnInfo) rawDst;
            dst.receiverIdIndex = src.receiverIdIndex;
            dst.senderIdIndex = src.senderIdIndex;
            dst.receiverTypeIndex = src.receiverTypeIndex;
            dst.messageStatusIndex = src.messageStatusIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ReceiverColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ReceiverColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$receiverId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.receiverIdIndex);
    }

    @Override
    public void realmSet$receiverId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'receiverId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$senderId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderIdIndex);
    }

    @Override
    public void realmSet$senderId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$receiverType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.receiverTypeIndex);
    }

    @Override
    public void realmSet$receiverType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.receiverTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.receiverTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.receiverTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.receiverTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$messageStatus() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.messageStatusIndex);
    }

    @Override
    public void realmSet$messageStatus(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.messageStatusIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.messageStatusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.messageStatusIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.messageStatusIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Receiver", 4, 0);
        builder.addPersistedProperty("receiverId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("receiverType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("messageStatus", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ReceiverColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ReceiverColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Receiver";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Receiver";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Receiver createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.Receiver obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
            ReceiverColumnInfo columnInfo = (ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
            long pkColumnIndex = columnInfo.receiverIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("receiverId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("receiverId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("receiverId")) {
                if (json.isNull("receiverId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class, json.getString("receiverId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'receiverId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) obj;
        if (json.has("senderId")) {
            if (json.isNull("senderId")) {
                objProxy.realmSet$senderId(null);
            } else {
                objProxy.realmSet$senderId((String) json.getString("senderId"));
            }
        }
        if (json.has("receiverType")) {
            if (json.isNull("receiverType")) {
                objProxy.realmSet$receiverType(null);
            } else {
                objProxy.realmSet$receiverType((String) json.getString("receiverType"));
            }
        }
        if (json.has("messageStatus")) {
            if (json.isNull("messageStatus")) {
                objProxy.realmSet$messageStatus(null);
            } else {
                objProxy.realmSet$messageStatus((String) json.getString("messageStatus"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Receiver createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Receiver obj = new com.treeleaf.anydone.serviceprovider.realm.model.Receiver();
        final com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("receiverId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$receiverId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$receiverId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("senderId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderId(null);
                }
            } else if (name.equals("receiverType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$receiverType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$receiverType(null);
                }
            } else if (name.equals("messageStatus")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$messageStatus((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$messageStatus(null);
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'receiverId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Receiver copyOrUpdate(Realm realm, ReceiverColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Receiver object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Receiver realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
            long pkColumnIndex = columnInfo.receiverIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverId();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Receiver copy(Realm realm, ReceiverColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Receiver newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.receiverIdIndex, realmObjectSource.realmGet$receiverId());
        builder.addString(columnInfo.senderIdIndex, realmObjectSource.realmGet$senderId());
        builder.addString(columnInfo.receiverTypeIndex, realmObjectSource.realmGet$receiverType());
        builder.addString(columnInfo.messageStatusIndex, realmObjectSource.realmGet$messageStatus());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Receiver object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long tableNativePtr = table.getNativePtr();
        ReceiverColumnInfo columnInfo = (ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long pkColumnIndex = columnInfo.receiverIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverId();
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
        String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$senderId();
        if (realmGet$senderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
        }
        String realmGet$receiverType = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverType();
        if (realmGet$receiverType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, realmGet$receiverType, false);
        }
        String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$messageStatus();
        if (realmGet$messageStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long tableNativePtr = table.getNativePtr();
        ReceiverColumnInfo columnInfo = (ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long pkColumnIndex = columnInfo.receiverIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Receiver object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverId();
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
            String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$senderId();
            if (realmGet$senderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
            }
            String realmGet$receiverType = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverType();
            if (realmGet$receiverType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, realmGet$receiverType, false);
            }
            String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$messageStatus();
            if (realmGet$messageStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Receiver object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long tableNativePtr = table.getNativePtr();
        ReceiverColumnInfo columnInfo = (ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long pkColumnIndex = columnInfo.receiverIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverId();
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
        String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$senderId();
        if (realmGet$senderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderIdIndex, rowIndex, false);
        }
        String realmGet$receiverType = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverType();
        if (realmGet$receiverType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, realmGet$receiverType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, false);
        }
        String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$messageStatus();
        if (realmGet$messageStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, false);
        }
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long tableNativePtr = table.getNativePtr();
        ReceiverColumnInfo columnInfo = (ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        long pkColumnIndex = columnInfo.receiverIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Receiver object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverId();
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
            String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$senderId();
            if (realmGet$senderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderIdIndex, rowIndex, false);
            }
            String realmGet$receiverType = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$receiverType();
            if (realmGet$receiverType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, realmGet$receiverType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.receiverTypeIndex, rowIndex, false);
            }
            String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) object).realmGet$messageStatus();
            if (realmGet$messageStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, false);
            }
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Receiver createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Receiver realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Receiver unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Receiver();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$receiverId(realmSource.realmGet$receiverId());
        unmanagedCopy.realmSet$senderId(realmSource.realmGet$senderId());
        unmanagedCopy.realmSet$receiverType(realmSource.realmGet$receiverType());
        unmanagedCopy.realmSet$messageStatus(realmSource.realmGet$messageStatus());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Receiver update(Realm realm, ReceiverColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Receiver realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Receiver newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.receiverIdIndex, realmObjectSource.realmGet$receiverId());
        builder.addString(columnInfo.senderIdIndex, realmObjectSource.realmGet$senderId());
        builder.addString(columnInfo.receiverTypeIndex, realmObjectSource.realmGet$receiverType());
        builder.addString(columnInfo.messageStatusIndex, realmObjectSource.realmGet$messageStatus());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Receiver = proxy[");
        stringBuilder.append("{receiverId:");
        stringBuilder.append(realmGet$receiverId() != null ? realmGet$receiverId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderId:");
        stringBuilder.append(realmGet$senderId() != null ? realmGet$senderId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{receiverType:");
        stringBuilder.append(realmGet$receiverType() != null ? realmGet$receiverType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{messageStatus:");
        stringBuilder.append(realmGet$messageStatus() != null ? realmGet$messageStatus() : "null");
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
        com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy aReceiver = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aReceiver.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aReceiver.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aReceiver.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
