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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface {

    static final class ServiceProviderColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long serviceProviderIdIndex;
        long accountIdIndex;
        long emailIndex;
        long phoneIndex;
        long profilePicIndex;
        long fullNameIndex;
        long profileIdIndex;
        long typeIndex;
        long noOfRatingIndex;
        long avgRatingIndex;

        ServiceProviderColumnInfo(OsSchemaInfo schemaInfo) {
            super(10);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("ServiceProvider");
            this.serviceProviderIdIndex = addColumnDetails("serviceProviderId", "serviceProviderId", objectSchemaInfo);
            this.accountIdIndex = addColumnDetails("accountId", "accountId", objectSchemaInfo);
            this.emailIndex = addColumnDetails("email", "email", objectSchemaInfo);
            this.phoneIndex = addColumnDetails("phone", "phone", objectSchemaInfo);
            this.profilePicIndex = addColumnDetails("profilePic", "profilePic", objectSchemaInfo);
            this.fullNameIndex = addColumnDetails("fullName", "fullName", objectSchemaInfo);
            this.profileIdIndex = addColumnDetails("profileId", "profileId", objectSchemaInfo);
            this.typeIndex = addColumnDetails("type", "type", objectSchemaInfo);
            this.noOfRatingIndex = addColumnDetails("noOfRating", "noOfRating", objectSchemaInfo);
            this.avgRatingIndex = addColumnDetails("avgRating", "avgRating", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceProviderColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceProviderColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceProviderColumnInfo src = (ServiceProviderColumnInfo) rawSrc;
            final ServiceProviderColumnInfo dst = (ServiceProviderColumnInfo) rawDst;
            dst.serviceProviderIdIndex = src.serviceProviderIdIndex;
            dst.accountIdIndex = src.accountIdIndex;
            dst.emailIndex = src.emailIndex;
            dst.phoneIndex = src.phoneIndex;
            dst.profilePicIndex = src.profilePicIndex;
            dst.fullNameIndex = src.fullNameIndex;
            dst.profileIdIndex = src.profileIdIndex;
            dst.typeIndex = src.typeIndex;
            dst.noOfRatingIndex = src.noOfRatingIndex;
            dst.avgRatingIndex = src.avgRatingIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceProviderColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider> proxyState;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceProviderColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$serviceProviderId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceProviderIdIndex);
    }

    @Override
    public void realmSet$serviceProviderId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceProviderIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceProviderIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceProviderIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceProviderIdIndex, value);
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
    public String realmGet$type() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.typeIndex);
    }

    @Override
    public void realmSet$type(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.typeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.typeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.typeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.typeIndex, value);
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

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("ServiceProvider", 10, 0);
        builder.addPersistedProperty("serviceProviderId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("accountId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("email", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("phone", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profilePic", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fullName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("profileId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("type", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("noOfRating", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("avgRating", RealmFieldType.FLOAT, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceProviderColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceProviderColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "ServiceProvider";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "ServiceProvider";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = Collections.<String> emptyList();
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider obj = realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class, true, excludeFields);

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) obj;
        if (json.has("serviceProviderId")) {
            if (json.isNull("serviceProviderId")) {
                objProxy.realmSet$serviceProviderId(null);
            } else {
                objProxy.realmSet$serviceProviderId((String) json.getString("serviceProviderId"));
            }
        }
        if (json.has("accountId")) {
            if (json.isNull("accountId")) {
                objProxy.realmSet$accountId(null);
            } else {
                objProxy.realmSet$accountId((String) json.getString("accountId"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                objProxy.realmSet$email(null);
            } else {
                objProxy.realmSet$email((String) json.getString("email"));
            }
        }
        if (json.has("phone")) {
            if (json.isNull("phone")) {
                objProxy.realmSet$phone(null);
            } else {
                objProxy.realmSet$phone((String) json.getString("phone"));
            }
        }
        if (json.has("profilePic")) {
            if (json.isNull("profilePic")) {
                objProxy.realmSet$profilePic(null);
            } else {
                objProxy.realmSet$profilePic((String) json.getString("profilePic"));
            }
        }
        if (json.has("fullName")) {
            if (json.isNull("fullName")) {
                objProxy.realmSet$fullName(null);
            } else {
                objProxy.realmSet$fullName((String) json.getString("fullName"));
            }
        }
        if (json.has("profileId")) {
            if (json.isNull("profileId")) {
                objProxy.realmSet$profileId(null);
            } else {
                objProxy.realmSet$profileId((String) json.getString("profileId"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                objProxy.realmSet$type(null);
            } else {
                objProxy.realmSet$type((String) json.getString("type"));
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
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        final com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider obj = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("serviceProviderId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceProviderId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceProviderId(null);
                }
            } else if (name.equals("accountId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$accountId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$accountId(null);
                }
            } else if (name.equals("email")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$email((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$email(null);
                }
            } else if (name.equals("phone")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$phone((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$phone(null);
                }
            } else if (name.equals("profilePic")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profilePic((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profilePic(null);
                }
            } else if (name.equals("fullName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fullName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fullName(null);
                }
            } else if (name.equals("profileId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$profileId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$profileId(null);
                }
            } else if (name.equals("type")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$type((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$type(null);
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
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider copyOrUpdate(Realm realm, ServiceProviderColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cachedRealmObject;
        }

        return copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider copy(Realm realm, ServiceProviderColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.serviceProviderIdIndex, realmObjectSource.realmGet$serviceProviderId());
        builder.addString(columnInfo.accountIdIndex, realmObjectSource.realmGet$accountId());
        builder.addString(columnInfo.emailIndex, realmObjectSource.realmGet$email());
        builder.addString(columnInfo.phoneIndex, realmObjectSource.realmGet$phone());
        builder.addString(columnInfo.profilePicIndex, realmObjectSource.realmGet$profilePic());
        builder.addString(columnInfo.fullNameIndex, realmObjectSource.realmGet$fullName());
        builder.addString(columnInfo.profileIdIndex, realmObjectSource.realmGet$profileId());
        builder.addString(columnInfo.typeIndex, realmObjectSource.realmGet$type());
        builder.addInteger(columnInfo.noOfRatingIndex, realmObjectSource.realmGet$noOfRating());
        builder.addFloat(columnInfo.avgRatingIndex, realmObjectSource.realmGet$avgRating());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long tableNativePtr = table.getNativePtr();
        ServiceProviderColumnInfo columnInfo = (ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$serviceProviderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$serviceProviderId();
        if (realmGet$serviceProviderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, realmGet$serviceProviderId, false);
        }
        String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$accountId();
        if (realmGet$accountId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        }
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        }
        String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profileId();
        if (realmGet$profileId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
        }
        String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$noOfRating(), false);
        Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$avgRating(), false);
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long tableNativePtr = table.getNativePtr();
        ServiceProviderColumnInfo columnInfo = (ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$serviceProviderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$serviceProviderId();
            if (realmGet$serviceProviderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, realmGet$serviceProviderId, false);
            }
            String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$accountId();
            if (realmGet$accountId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            }
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            }
            String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profileId();
            if (realmGet$profileId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
            }
            String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$noOfRating(), false);
            Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$avgRating(), false);
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long tableNativePtr = table.getNativePtr();
        ServiceProviderColumnInfo columnInfo = (ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long rowIndex = OsObject.createRow(table);
        cache.put(object, rowIndex);
        String realmGet$serviceProviderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$serviceProviderId();
        if (realmGet$serviceProviderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, realmGet$serviceProviderId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, false);
        }
        String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$accountId();
        if (realmGet$accountId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.accountIdIndex, rowIndex, false);
        }
        String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$email();
        if (realmGet$email != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
        }
        String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$phone();
        if (realmGet$phone != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
        }
        String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profilePic();
        if (realmGet$profilePic != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
        }
        String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$fullName();
        if (realmGet$fullName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
        }
        String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profileId();
        if (realmGet$profileId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.profileIdIndex, rowIndex, false);
        }
        String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$noOfRating(), false);
        Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$avgRating(), false);
        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        long tableNativePtr = table.getNativePtr();
        ServiceProviderColumnInfo columnInfo = (ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = OsObject.createRow(table);
            cache.put(object, rowIndex);
            String realmGet$serviceProviderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$serviceProviderId();
            if (realmGet$serviceProviderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, realmGet$serviceProviderId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceProviderIdIndex, rowIndex, false);
            }
            String realmGet$accountId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$accountId();
            if (realmGet$accountId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.accountIdIndex, rowIndex, realmGet$accountId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.accountIdIndex, rowIndex, false);
            }
            String realmGet$email = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$email();
            if (realmGet$email != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.emailIndex, rowIndex, realmGet$email, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.emailIndex, rowIndex, false);
            }
            String realmGet$phone = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$phone();
            if (realmGet$phone != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.phoneIndex, rowIndex, realmGet$phone, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.phoneIndex, rowIndex, false);
            }
            String realmGet$profilePic = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profilePic();
            if (realmGet$profilePic != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profilePicIndex, rowIndex, realmGet$profilePic, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profilePicIndex, rowIndex, false);
            }
            String realmGet$fullName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$fullName();
            if (realmGet$fullName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fullNameIndex, rowIndex, realmGet$fullName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fullNameIndex, rowIndex, false);
            }
            String realmGet$profileId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$profileId();
            if (realmGet$profileId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.profileIdIndex, rowIndex, realmGet$profileId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.profileIdIndex, rowIndex, false);
            }
            String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.noOfRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$noOfRating(), false);
            Table.nativeSetFloat(tableNativePtr, columnInfo.avgRatingIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) object).realmGet$avgRating(), false);
        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$serviceProviderId(realmSource.realmGet$serviceProviderId());
        unmanagedCopy.realmSet$accountId(realmSource.realmGet$accountId());
        unmanagedCopy.realmSet$email(realmSource.realmGet$email());
        unmanagedCopy.realmSet$phone(realmSource.realmGet$phone());
        unmanagedCopy.realmSet$profilePic(realmSource.realmGet$profilePic());
        unmanagedCopy.realmSet$fullName(realmSource.realmGet$fullName());
        unmanagedCopy.realmSet$profileId(realmSource.realmGet$profileId());
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$noOfRating(realmSource.realmGet$noOfRating());
        unmanagedCopy.realmSet$avgRating(realmSource.realmGet$avgRating());

        return unmanagedObject;
    }

    @Override
    @SuppressWarnings("ArrayToString")
    public String toString() {
        if (!RealmObject.isValid(this)) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("ServiceProvider = proxy[");
        stringBuilder.append("{serviceProviderId:");
        stringBuilder.append(realmGet$serviceProviderId() != null ? realmGet$serviceProviderId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{accountId:");
        stringBuilder.append(realmGet$accountId() != null ? realmGet$accountId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(realmGet$email() != null ? realmGet$email() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phone:");
        stringBuilder.append(realmGet$phone() != null ? realmGet$phone() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profilePic:");
        stringBuilder.append(realmGet$profilePic() != null ? realmGet$profilePic() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fullName:");
        stringBuilder.append(realmGet$fullName() != null ? realmGet$fullName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profileId:");
        stringBuilder.append(realmGet$profileId() != null ? realmGet$profileId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{noOfRating:");
        stringBuilder.append(realmGet$noOfRating());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{avgRating:");
        stringBuilder.append(realmGet$avgRating());
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy aServiceProvider = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aServiceProvider.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aServiceProvider.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aServiceProvider.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
