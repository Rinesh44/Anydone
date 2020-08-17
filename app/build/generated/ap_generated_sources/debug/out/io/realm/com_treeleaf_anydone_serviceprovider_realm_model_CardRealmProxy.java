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
public class com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Card
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface {

    static final class CardColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long cardNumberIndex;
        long cardHolderNameIndex;
        long monthIndex;
        long yearIndex;
        long cvvIndex;
        long streetAddressIndex;
        long cityIndex;
        long stateIndex;
        long primaryIndex;

        CardColumnInfo(OsSchemaInfo schemaInfo) {
            super(9);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Card");
            this.cardNumberIndex = addColumnDetails("cardNumber", "cardNumber", objectSchemaInfo);
            this.cardHolderNameIndex = addColumnDetails("cardHolderName", "cardHolderName", objectSchemaInfo);
            this.monthIndex = addColumnDetails("month", "month", objectSchemaInfo);
            this.yearIndex = addColumnDetails("year", "year", objectSchemaInfo);
            this.cvvIndex = addColumnDetails("cvv", "cvv", objectSchemaInfo);
            this.streetAddressIndex = addColumnDetails("streetAddress", "streetAddress", objectSchemaInfo);
            this.cityIndex = addColumnDetails("city", "city", objectSchemaInfo);
            this.stateIndex = addColumnDetails("state", "state", objectSchemaInfo);
            this.primaryIndex = addColumnDetails("primary", "primary", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        CardColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new CardColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final CardColumnInfo src = (CardColumnInfo) rawSrc;
            final CardColumnInfo dst = (CardColumnInfo) rawDst;
            dst.cardNumberIndex = src.cardNumberIndex;
            dst.cardHolderNameIndex = src.cardHolderNameIndex;
            dst.monthIndex = src.monthIndex;
            dst.yearIndex = src.yearIndex;
            dst.cvvIndex = src.cvvIndex;
            dst.streetAddressIndex = src.streetAddressIndex;
            dst.cityIndex = src.cityIndex;
            dst.stateIndex = src.stateIndex;
            dst.primaryIndex = src.primaryIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private CardColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Card> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (CardColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Card>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cardNumber() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cardNumberIndex);
    }

    @Override
    public void realmSet$cardNumber(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'cardNumber' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cardHolderName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cardHolderNameIndex);
    }

    @Override
    public void realmSet$cardHolderName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cardHolderNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.cardHolderNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cardHolderNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cardHolderNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$month() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.monthIndex);
    }

    @Override
    public void realmSet$month(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.monthIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.monthIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.monthIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.monthIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$year() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.yearIndex);
    }

    @Override
    public void realmSet$year(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.yearIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.yearIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.yearIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.yearIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$cvv() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cvvIndex);
    }

    @Override
    public void realmSet$cvv(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cvvIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.cvvIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cvvIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cvvIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$streetAddress() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.streetAddressIndex);
    }

    @Override
    public void realmSet$streetAddress(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.streetAddressIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.streetAddressIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.streetAddressIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.streetAddressIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$city() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.cityIndex);
    }

    @Override
    public void realmSet$city(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.cityIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.cityIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.cityIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.cityIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$state() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.stateIndex);
    }

    @Override
    public void realmSet$state(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.stateIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.stateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.stateIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.stateIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$primary() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.primaryIndex);
    }

    @Override
    public void realmSet$primary(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.primaryIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.primaryIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Card", 9, 0);
        builder.addPersistedProperty("cardNumber", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("cardHolderName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("month", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("year", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("cvv", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("streetAddress", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("city", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("state", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("primary", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static CardColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new CardColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Card";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Card";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Card createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.Card obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
            CardColumnInfo columnInfo = (CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
            long pkColumnIndex = columnInfo.cardNumberIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("cardNumber")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("cardNumber"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("cardNumber")) {
                if (json.isNull("cardNumber")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Card.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Card.class, json.getString("cardNumber"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'cardNumber'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) obj;
        if (json.has("cardHolderName")) {
            if (json.isNull("cardHolderName")) {
                objProxy.realmSet$cardHolderName(null);
            } else {
                objProxy.realmSet$cardHolderName((String) json.getString("cardHolderName"));
            }
        }
        if (json.has("month")) {
            if (json.isNull("month")) {
                objProxy.realmSet$month(null);
            } else {
                objProxy.realmSet$month((String) json.getString("month"));
            }
        }
        if (json.has("year")) {
            if (json.isNull("year")) {
                objProxy.realmSet$year(null);
            } else {
                objProxy.realmSet$year((String) json.getString("year"));
            }
        }
        if (json.has("cvv")) {
            if (json.isNull("cvv")) {
                objProxy.realmSet$cvv(null);
            } else {
                objProxy.realmSet$cvv((String) json.getString("cvv"));
            }
        }
        if (json.has("streetAddress")) {
            if (json.isNull("streetAddress")) {
                objProxy.realmSet$streetAddress(null);
            } else {
                objProxy.realmSet$streetAddress((String) json.getString("streetAddress"));
            }
        }
        if (json.has("city")) {
            if (json.isNull("city")) {
                objProxy.realmSet$city(null);
            } else {
                objProxy.realmSet$city((String) json.getString("city"));
            }
        }
        if (json.has("state")) {
            if (json.isNull("state")) {
                objProxy.realmSet$state(null);
            } else {
                objProxy.realmSet$state((String) json.getString("state"));
            }
        }
        if (json.has("primary")) {
            if (json.isNull("primary")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'primary' to null.");
            } else {
                objProxy.realmSet$primary((boolean) json.getBoolean("primary"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Card createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Card obj = new com.treeleaf.anydone.serviceprovider.realm.model.Card();
        final com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("cardNumber")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cardNumber((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cardNumber(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("cardHolderName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cardHolderName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cardHolderName(null);
                }
            } else if (name.equals("month")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$month((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$month(null);
                }
            } else if (name.equals("year")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$year((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$year(null);
                }
            } else if (name.equals("cvv")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$cvv((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$cvv(null);
                }
            } else if (name.equals("streetAddress")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$streetAddress((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$streetAddress(null);
                }
            } else if (name.equals("city")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$city((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$city(null);
                }
            } else if (name.equals("state")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$state((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$state(null);
                }
            } else if (name.equals("primary")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$primary((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'primary' to null.");
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'cardNumber'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Card copyOrUpdate(Realm realm, CardColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Card object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Card) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Card realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
            long pkColumnIndex = columnInfo.cardNumberIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardNumber();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Card copy(Realm realm, CardColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Card newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Card) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.cardNumberIndex, realmObjectSource.realmGet$cardNumber());
        builder.addString(columnInfo.cardHolderNameIndex, realmObjectSource.realmGet$cardHolderName());
        builder.addString(columnInfo.monthIndex, realmObjectSource.realmGet$month());
        builder.addString(columnInfo.yearIndex, realmObjectSource.realmGet$year());
        builder.addString(columnInfo.cvvIndex, realmObjectSource.realmGet$cvv());
        builder.addString(columnInfo.streetAddressIndex, realmObjectSource.realmGet$streetAddress());
        builder.addString(columnInfo.cityIndex, realmObjectSource.realmGet$city());
        builder.addString(columnInfo.stateIndex, realmObjectSource.realmGet$state());
        builder.addBoolean(columnInfo.primaryIndex, realmObjectSource.realmGet$primary());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Card object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long tableNativePtr = table.getNativePtr();
        CardColumnInfo columnInfo = (CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long pkColumnIndex = columnInfo.cardNumberIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardNumber();
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
        String realmGet$cardHolderName = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardHolderName();
        if (realmGet$cardHolderName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, realmGet$cardHolderName, false);
        }
        String realmGet$month = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$month();
        if (realmGet$month != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.monthIndex, rowIndex, realmGet$month, false);
        }
        String realmGet$year = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$year();
        if (realmGet$year != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.yearIndex, rowIndex, realmGet$year, false);
        }
        String realmGet$cvv = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cvv();
        if (realmGet$cvv != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cvvIndex, rowIndex, realmGet$cvv, false);
        }
        String realmGet$streetAddress = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$streetAddress();
        if (realmGet$streetAddress != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, realmGet$streetAddress, false);
        }
        String realmGet$city = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$city();
        if (realmGet$city != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cityIndex, rowIndex, realmGet$city, false);
        }
        String realmGet$state = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$state();
        if (realmGet$state != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.stateIndex, rowIndex, realmGet$state, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.primaryIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$primary(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long tableNativePtr = table.getNativePtr();
        CardColumnInfo columnInfo = (CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long pkColumnIndex = columnInfo.cardNumberIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Card object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Card) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardNumber();
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
            String realmGet$cardHolderName = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardHolderName();
            if (realmGet$cardHolderName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, realmGet$cardHolderName, false);
            }
            String realmGet$month = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$month();
            if (realmGet$month != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.monthIndex, rowIndex, realmGet$month, false);
            }
            String realmGet$year = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$year();
            if (realmGet$year != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.yearIndex, rowIndex, realmGet$year, false);
            }
            String realmGet$cvv = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cvv();
            if (realmGet$cvv != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cvvIndex, rowIndex, realmGet$cvv, false);
            }
            String realmGet$streetAddress = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$streetAddress();
            if (realmGet$streetAddress != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, realmGet$streetAddress, false);
            }
            String realmGet$city = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$city();
            if (realmGet$city != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cityIndex, rowIndex, realmGet$city, false);
            }
            String realmGet$state = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$state();
            if (realmGet$state != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.stateIndex, rowIndex, realmGet$state, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.primaryIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$primary(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Card object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long tableNativePtr = table.getNativePtr();
        CardColumnInfo columnInfo = (CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long pkColumnIndex = columnInfo.cardNumberIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardNumber();
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
        String realmGet$cardHolderName = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardHolderName();
        if (realmGet$cardHolderName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, realmGet$cardHolderName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, false);
        }
        String realmGet$month = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$month();
        if (realmGet$month != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.monthIndex, rowIndex, realmGet$month, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.monthIndex, rowIndex, false);
        }
        String realmGet$year = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$year();
        if (realmGet$year != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.yearIndex, rowIndex, realmGet$year, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.yearIndex, rowIndex, false);
        }
        String realmGet$cvv = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cvv();
        if (realmGet$cvv != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cvvIndex, rowIndex, realmGet$cvv, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cvvIndex, rowIndex, false);
        }
        String realmGet$streetAddress = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$streetAddress();
        if (realmGet$streetAddress != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, realmGet$streetAddress, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, false);
        }
        String realmGet$city = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$city();
        if (realmGet$city != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.cityIndex, rowIndex, realmGet$city, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.cityIndex, rowIndex, false);
        }
        String realmGet$state = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$state();
        if (realmGet$state != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.stateIndex, rowIndex, realmGet$state, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.stateIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.primaryIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$primary(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long tableNativePtr = table.getNativePtr();
        CardColumnInfo columnInfo = (CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        long pkColumnIndex = columnInfo.cardNumberIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Card object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Card) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardNumber();
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
            String realmGet$cardHolderName = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cardHolderName();
            if (realmGet$cardHolderName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, realmGet$cardHolderName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cardHolderNameIndex, rowIndex, false);
            }
            String realmGet$month = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$month();
            if (realmGet$month != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.monthIndex, rowIndex, realmGet$month, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.monthIndex, rowIndex, false);
            }
            String realmGet$year = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$year();
            if (realmGet$year != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.yearIndex, rowIndex, realmGet$year, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.yearIndex, rowIndex, false);
            }
            String realmGet$cvv = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$cvv();
            if (realmGet$cvv != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cvvIndex, rowIndex, realmGet$cvv, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cvvIndex, rowIndex, false);
            }
            String realmGet$streetAddress = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$streetAddress();
            if (realmGet$streetAddress != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, realmGet$streetAddress, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.streetAddressIndex, rowIndex, false);
            }
            String realmGet$city = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$city();
            if (realmGet$city != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.cityIndex, rowIndex, realmGet$city, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.cityIndex, rowIndex, false);
            }
            String realmGet$state = ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$state();
            if (realmGet$state != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.stateIndex, rowIndex, realmGet$state, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.stateIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.primaryIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) object).realmGet$primary(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Card createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Card realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Card unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Card();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Card) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Card) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$cardNumber(realmSource.realmGet$cardNumber());
        unmanagedCopy.realmSet$cardHolderName(realmSource.realmGet$cardHolderName());
        unmanagedCopy.realmSet$month(realmSource.realmGet$month());
        unmanagedCopy.realmSet$year(realmSource.realmGet$year());
        unmanagedCopy.realmSet$cvv(realmSource.realmGet$cvv());
        unmanagedCopy.realmSet$streetAddress(realmSource.realmGet$streetAddress());
        unmanagedCopy.realmSet$city(realmSource.realmGet$city());
        unmanagedCopy.realmSet$state(realmSource.realmGet$state());
        unmanagedCopy.realmSet$primary(realmSource.realmGet$primary());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Card update(Realm realm, CardColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Card realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Card newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.cardNumberIndex, realmObjectSource.realmGet$cardNumber());
        builder.addString(columnInfo.cardHolderNameIndex, realmObjectSource.realmGet$cardHolderName());
        builder.addString(columnInfo.monthIndex, realmObjectSource.realmGet$month());
        builder.addString(columnInfo.yearIndex, realmObjectSource.realmGet$year());
        builder.addString(columnInfo.cvvIndex, realmObjectSource.realmGet$cvv());
        builder.addString(columnInfo.streetAddressIndex, realmObjectSource.realmGet$streetAddress());
        builder.addString(columnInfo.cityIndex, realmObjectSource.realmGet$city());
        builder.addString(columnInfo.stateIndex, realmObjectSource.realmGet$state());
        builder.addBoolean(columnInfo.primaryIndex, realmObjectSource.realmGet$primary());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("Card = proxy[");
        stringBuilder.append("{cardNumber:");
        stringBuilder.append(realmGet$cardNumber() != null ? realmGet$cardNumber() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cardHolderName:");
        stringBuilder.append(realmGet$cardHolderName() != null ? realmGet$cardHolderName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{month:");
        stringBuilder.append(realmGet$month() != null ? realmGet$month() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{year:");
        stringBuilder.append(realmGet$year() != null ? realmGet$year() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{cvv:");
        stringBuilder.append(realmGet$cvv() != null ? realmGet$cvv() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{streetAddress:");
        stringBuilder.append(realmGet$streetAddress() != null ? realmGet$streetAddress() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{city:");
        stringBuilder.append(realmGet$city() != null ? realmGet$city() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{state:");
        stringBuilder.append(realmGet$state() != null ? realmGet$state() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{primary:");
        stringBuilder.append(realmGet$primary());
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
        com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy aCard = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aCard.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aCard.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aCard.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
