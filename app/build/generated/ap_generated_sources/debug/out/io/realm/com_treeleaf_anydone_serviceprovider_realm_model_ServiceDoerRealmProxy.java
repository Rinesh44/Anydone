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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface {

    static final class ServiceDoerColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long accountIdIndex;
        long profileIdIndex;
        long emailIndex;
        long fullNameIndex;
        long profilePicIndex;
        long phoneIndex;
        long genderIndex;
        long noOfRatingIndex;
        long avgRatingIndex;
        long assignedAtIndex;

        ServiceDoerColumnInfo(OsSchemaInfo schemaInfo) {
            super(10);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("ServiceDoer");
            this.accountIdIndex = addColumnDetails("accountId", "accountId", objectSchemaInfo);
            this.profileIdIndex = addColumnDetails("profileId", "profileId", objectSchemaInfo);
            this.emailIndex = addColumnDetails("email", "email", objectSchemaInfo);
            this.fullNameIndex = addColumnDetails("fullName", "fullName", objectSchemaInfo);
            this.profilePicIndex = addColumnDetails("profilePic", "profilePic", objectSchemaInfo);
            this.phoneIndex = addColumnDetails("phone", "phone", objectSchemaInfo);
            this.genderIndex = addColumnDetails("gender", "gender", objectSchemaInfo);
            this.noOfRatingIndex = addColumnDetails("noOfRating", "noOfRating", objectSchemaInfo);
            this.avgRatingIndex = addColumnDetails("avgRating", "avgRating", objectSchemaInfo);
            this.assignedAtIndex = addColumnDetails("assignedAt", "assignedAt", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceDoerColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceDoerColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceDoerColumnInfo src = (ServiceDoerColumnInfo) rawSrc;
            final ServiceDoerColumnInfo dst = (ServiceDoerColumnInfo) rawDst;
            dst.accountIdIndex = src.accountIdIndex;
            dst.profileIdIndex = src.profileIdIndex;
            dst.emailIndex = src.emailIndex;
            dst.fullNameIndex = src.fullNameIndex;
            dst.profilePicIndex = src.profilePicIndex;
            dst.phoneIndex = src.phoneIndex;
            dst.genderIndex = src.genderIndex;
            dst.noOfRatingIndex = src.noOfRatingIndex;
            dst.avgRatingIndex = src.avgRatingIndex;
            dst.assignedAtIndex = src.assignedAtIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceDoerColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceDoerColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>(this);
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
    public String realmGet$profileId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.profileIdIndex);
    }

    @Override
    public void realmSet$profileId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.profileIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.profileIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.profileIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.profileIdIndex, value);
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
    public int realmGet$noOfRating() {
        proxyState.getRealm$realm().checkIfValid();
        return (int) proxyState.getRow$realm().getLong(columnInfo.noOfRatingIndex);
    }

    @Override
    public void realmSet$noOfRating(int value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.noOfRatingIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.noOfRatingIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public float realmGet$avgRating() {
        proxyState.getRealm$realm().checkIfValid();
        return (float) proxyState.getRow$realm().getFloat(columnInfo.avgRatingIndex);
    }

    @Override
    public void realmSet$avgRating(float value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setFloat(columnInfo.avgRatingIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setFloat(columnInfo.avgRatingIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$assignedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.assignedAtIndex);
    }

    @Override
    public void realmSet$assignedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.assignedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.assignedAtIndex, value);
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("ServiceDoer", 10, 0);
        builder.addPersistedProperty("accountId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profileId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profilePic", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("phone", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("gender", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("noOfRating", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("avgRating", RealmFieldType.FLOAT, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("assignedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceDoerColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceDoerColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "ServiceDoer";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "ServiceDoer";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
            ServiceDoerColumnInfo columnInfo = (ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
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
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("accountId")) {
                if (json.isNull("accountId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class, json.getString("accountId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'accountId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) obj;
        if (json.has("profileId")) {
            if (json.isNull("profileId")) {
                objProxy.realmSet$profileId(null);
            } else {
                objProxy.realmSet$profileId((String) json.getString("profileId"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                objProxy.realmSet$email(null);
            } else {
                objProxy.realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                objProxy.realmSet$fullName(null);
            } else {
                objProxy.realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("profilePic")) {
            if (json.isNull("profilePic")) {
                objProxy.realmSet$profilePic(null);
            } else {
                objProxy.realmSet$profilePic((String) json.getString("profilePic"));
            }
        }
        if (json.has("phone")) {
            if (json.isNull("phone")) {
                objProxy.realmSet$phone(null);
            } else {
                objProxy.realmSet$phone((String) json.getString("phone"));
            }
        }
        if (json.has("gender")) {
            if (json.isNull("gender")) {
                objProxy.realmSet$gender(null);
            } else {
                objProxy.realmSet$gender((String) json.getString("gender"));
            }
        }
        if (json.has("noOfRating")) {
            if (json.isNull("noOfRating")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'noOfRating' to null.");
            } else {
                objProxy.realmSet$noOfRating((int) json.getInt("noOfRating"));
            }
        }
        if (json.has("avgRating")) {
            if (json.isNull("avgRating")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'avgRating' to null.");
            } else {
                objProxy.realmSet$avgRating((float) json.getDouble("avgRating"));
            }
        }
        if (json.has("assignedAt")) {
            if (json.isNull("assignedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'assignedAt' to null.");
            } else {
                objProxy.realmSet$assignedAt((long) json.getLong("assignedAt"));
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer obj = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) obj;
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
            } else if (name.equals("profileId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profileId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profileId(null);
                }
            } else if (name.equals("email")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$email((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$email(null);
                }
            } else if (name.equals("fullName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fullName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fullName(null);
                }
            } else if (name.equals("profilePic")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profilePic((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profilePic(null);
                }
            } else if (name.equals("phone")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$phone((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$phone(null);
                }
            } else if (name.equals("gender")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$gender((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$gender(null);
                }
            } else if (name.equals("noOfRating")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$noOfRating((int) reader.nextInt());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'noOfRating' to null.");
                }
            } else if (name.equals("avgRating")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$avgRating((float) reader.nextDouble());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'avgRating' to null.");
                }
            } else if (name.equals("assignedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$assignedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'assignedAt' to null.");
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

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer copyOrUpdate(Realm realm, ServiceDoerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
            long pkColumnIndex = columnInfo.accountIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$accountId();
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
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer copy(Realm realm, ServiceDoerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addString(columnInfo.profileIdIndex, realmObjectSource.realmGet$profileId());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.genderIndex, realmObjectSource.realmGet$gender());
        builder.addInteger(columnInfo.noOfRatingIndex, realmObjectSource.realmGet$noOfRating());
        builder.addFloat(columnInfo.avgRatingIndex, realmObjectSource.realmGet$avgRating());
        builder.addInteger(columnInfo.assignedAtIndex, realmObjectSource.realmGet$assignedAt());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long tableNativePtr = table.getNativePtr();
        ServiceDoerColumnInfo columnInfo = (ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$accountId();
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
        String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profileId();
        if (realmGet$profileId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        }
        String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$noOfRating(), false);
        Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$avgRating(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.assignedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$assignedAt(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long tableNativePtr = table.getNativePtr();
        ServiceDoerColumnInfo columnInfo = (ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$accountId();
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
            String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profileId();
            if (realmGet$profileId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            }
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            }
            String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$gender();
            if (realmGet$gender != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$noOfRating(), false);
            Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$avgRating(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.assignedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$assignedAt(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long tableNativePtr = table.getNativePtr();
        ServiceDoerColumnInfo columnInfo = (ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$accountId();
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
        String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profileId();
        if (realmGet$profileId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profileIdIndex, rowIndex, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
        }
        String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$gender();
        if (realmGet$gender != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$noOfRating(), false);
        Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$avgRating(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.assignedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$assignedAt(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long tableNativePtr = table.getNativePtr();
        ServiceDoerColumnInfo columnInfo = (ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        long pkColumnIndex = columnInfo.accountIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$accountId();
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
            String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profileId();
            if (realmGet$profileId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profileIdIndex, rowIndex, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
            }
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
            }
            String realmGet$gender = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$gender();
            if (realmGet$gender != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.genderIndex, rowIndex, realmGet$gender, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.genderIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$noOfRating(), false);
            Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$avgRating(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.assignedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) object).realmGet$assignedAt(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$accountId(realmSource.realmGet$accountId());
        unmanagedCopy.realmSet$profileId(realmSource.realmGet$profileId());
        unmanagedCopy.realmSet$email(realmSource.realmGet$email());
        unmanagedCopy.realmSet$fullName(realmSource.realmGet$fullName());
        unmanagedCopy.realmSet$profilePic(realmSource.realmGet$profilePic());
        unmanagedCopy.realmSet$phone(realmSource.realmGet$phone());
        unmanagedCopy.realmSet$gender(realmSource.realmGet$gender());
        unmanagedCopy.realmSet$noOfRating(realmSource.realmGet$noOfRating());
        unmanagedCopy.realmSet$avgRating(realmSource.realmGet$avgRating());
        unmanagedCopy.realmSet$assignedAt(realmSource.realmGet$assignedAt());

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer update(Realm realm, ServiceDoerColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer realmObject, com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addString(columnInfo.profileIdIndex, realmObjectSource.realmGet$profileId());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.genderIndex, realmObjectSource.realmGet$gender());
        builder.addInteger(columnInfo.noOfRatingIndex, realmObjectSource.realmGet$noOfRating());
        builder.addFloat(columnInfo.avgRatingIndex, realmObjectSource.realmGet$avgRating());
        builder.addInteger(columnInfo.assignedAtIndex, realmObjectSource.realmGet$assignedAt());

        builder.updateExistingObject();
        return realmObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ServiceDoer = proxy[");
        stringBuilder.append("{accountId:");
        stringBuilder.append(realmGet$accountId() != null ? realmGet$accountId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profileId:");
        stringBuilder.append(realmGet$profileId() != null ? realmGet$profileId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profilePic:");
        stringBuilder.append(realmGet$profilePic() != null ? realmGet$profilePic() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phone:");
        stringBuilder.append(realmGet$phone() != null ? realmGet$phone() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gender:");
        stringBuilder.append(realmGet$gender() != null ? realmGet$gender() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{noOfRating:");
        stringBuilder.append(realmGet$noOfRating());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avgRating:");
        stringBuilder.append(realmGet$avgRating());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{assignedAt:");
        stringBuilder.append(realmGet$assignedAt());
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy aServiceDoer = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aServiceDoer.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aServiceDoer.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aServiceDoer.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
