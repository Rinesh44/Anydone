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
public class com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface {

    static final class ServiceRequestColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long serviceOrderIdIndex;
        long serviceIdIndex;
        long serviceNameIndex;
        long problemStatementIndex;
        long languageIndex;
        long statusIndex;
        long serviceIconUrlIndex;
        long typeIndex;
        long createdAtIndex;
        long acceptedAtIndex;
        long startedAtIndex;
        long completedAtIndex;
        long closedAtIndex;
        long isRemoteIndex;
        long serviceProviderIndex;
        long attributeListIndex;

        ServiceRequestColumnInfo(OsSchemaInfo schemaInfo) {
            super(16);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("ServiceRequest");
            this.serviceOrderIdIndex = addColumnDetails("serviceOrderId", "serviceOrderId", objectSchemaInfo);
            this.serviceIdIndex = addColumnDetails("serviceId", "serviceId", objectSchemaInfo);
            this.serviceNameIndex = addColumnDetails("serviceName", "serviceName", objectSchemaInfo);
            this.problemStatementIndex = addColumnDetails("problemStatement", "problemStatement", objectSchemaInfo);
            this.languageIndex = addColumnDetails("language", "language", objectSchemaInfo);
            this.statusIndex = addColumnDetails("status", "status", objectSchemaInfo);
            this.serviceIconUrlIndex = addColumnDetails("serviceIconUrl", "serviceIconUrl", objectSchemaInfo);
            this.typeIndex = addColumnDetails("type", "type", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.acceptedAtIndex = addColumnDetails("acceptedAt", "acceptedAt", objectSchemaInfo);
            this.startedAtIndex = addColumnDetails("startedAt", "startedAt", objectSchemaInfo);
            this.completedAtIndex = addColumnDetails("completedAt", "completedAt", objectSchemaInfo);
            this.closedAtIndex = addColumnDetails("closedAt", "closedAt", objectSchemaInfo);
            this.isRemoteIndex = addColumnDetails("isRemote", "isRemote", objectSchemaInfo);
            this.serviceProviderIndex = addColumnDetails("serviceProvider", "serviceProvider", objectSchemaInfo);
            this.attributeListIndex = addColumnDetails("attributeList", "attributeList", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ServiceRequestColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ServiceRequestColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ServiceRequestColumnInfo src = (ServiceRequestColumnInfo) rawSrc;
            final ServiceRequestColumnInfo dst = (ServiceRequestColumnInfo) rawDst;
            dst.serviceOrderIdIndex = src.serviceOrderIdIndex;
            dst.serviceIdIndex = src.serviceIdIndex;
            dst.serviceNameIndex = src.serviceNameIndex;
            dst.problemStatementIndex = src.problemStatementIndex;
            dst.languageIndex = src.languageIndex;
            dst.statusIndex = src.statusIndex;
            dst.serviceIconUrlIndex = src.serviceIconUrlIndex;
            dst.typeIndex = src.typeIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.acceptedAtIndex = src.acceptedAtIndex;
            dst.startedAtIndex = src.startedAtIndex;
            dst.completedAtIndex = src.completedAtIndex;
            dst.closedAtIndex = src.closedAtIndex;
            dst.isRemoteIndex = src.isRemoteIndex;
            dst.serviceProviderIndex = src.serviceProviderIndex;
            dst.attributeListIndex = src.attributeListIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ServiceRequestColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ServiceRequestColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$serviceOrderId() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.serviceOrderIdIndex);
    }

    @Override
    public void realmSet$serviceOrderId(long value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'serviceOrderId' cannot be changed after object was created.");
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
    public String realmGet$serviceName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.serviceNameIndex);
    }

    @Override
    public void realmSet$serviceName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.serviceNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.serviceNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.serviceNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.serviceNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$problemStatement() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.problemStatementIndex);
    }

    @Override
    public void realmSet$problemStatement(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.problemStatementIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.problemStatementIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.problemStatementIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.problemStatementIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$language() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.languageIndex);
    }

    @Override
    public void realmSet$language(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.languageIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.languageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.languageIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.languageIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$status() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.statusIndex);
    }

    @Override
    public void realmSet$status(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.statusIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.statusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.statusIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.statusIndex, value);
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
    public long realmGet$acceptedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.acceptedAtIndex);
    }

    @Override
    public void realmSet$acceptedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.acceptedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.acceptedAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$startedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.startedAtIndex);
    }

    @Override
    public void realmSet$startedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.startedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.startedAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$completedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.completedAtIndex);
    }

    @Override
    public void realmSet$completedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.completedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.completedAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$closedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.closedAtIndex);
    }

    @Override
    public void realmSet$closedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.closedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.closedAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$isRemote() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.isRemoteIndex);
    }

    @Override
    public void realmSet$isRemote(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.isRemoteIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.isRemoteIndex, value);
    }

    @Override
    public com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider realmGet$serviceProvider() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNullLink(columnInfo.serviceProviderIndex)) {
            return null;
        }
        return proxyState.getRealm$realm().get(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class, proxyState.getRow$realm().getLink(columnInfo.serviceProviderIndex), false, Collections.<String>emptyList());
    }

    @Override
    public void realmSet$serviceProvider(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("serviceProvider")) {
                return;
            }
            if (value != null && !RealmObject.isManaged(value)) {
                value = ((Realm) proxyState.getRealm$realm()).copyToRealm(value);
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                // Table#nullifyLink() does not support default value. Just using Row.
                row.nullifyLink(columnInfo.serviceProviderIndex);
                return;
            }
            proxyState.checkValidObject(value);
            row.getTable().setLink(columnInfo.serviceProviderIndex, row.getIndex(), ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex(), true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().nullifyLink(columnInfo.serviceProviderIndex);
            return;
        }
        proxyState.checkValidObject(value);
        proxyState.getRow$realm().setLink(columnInfo.serviceProviderIndex, ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex());
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> realmGet$attributeList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (attributeListRealmList != null) {
            return attributeListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.attributeListIndex);
            attributeListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class, osList, proxyState.getRealm$realm());
            return attributeListRealmList;
        }
    }

    @Override
    public void realmSet$attributeList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("attributeList")) {
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
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.attributeListIndex);
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
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("ServiceRequest", 16, 0);
        builder.addPersistedProperty("serviceOrderId", RealmFieldType.INTEGER, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("serviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("problemStatement", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("language", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("status", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceIconUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("type", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("acceptedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("startedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("completedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("closedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("isRemote", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedLinkProperty("serviceProvider", RealmFieldType.OBJECT, "ServiceProvider");
        builder.addPersistedLinkProperty("attributeList", RealmFieldType.LIST, "ServiceAttributes");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ServiceRequestColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ServiceRequestColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "ServiceRequest";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "ServiceRequest";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(2);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
            ServiceRequestColumnInfo columnInfo = (ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
            long pkColumnIndex = columnInfo.serviceOrderIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("serviceOrderId")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("serviceOrderId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("serviceProvider")) {
                excludeFields.add("serviceProvider");
            }
            if (json.has("attributeList")) {
                excludeFields.add("attributeList");
            }
            if (json.has("serviceOrderId")) {
                if (json.isNull("serviceOrderId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class, json.getLong("serviceOrderId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'serviceOrderId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) obj;
        if (json.has("serviceId")) {
            if (json.isNull("serviceId")) {
                objProxy.realmSet$serviceId(null);
            } else {
                objProxy.realmSet$serviceId((String) json.getString("serviceId"));
            }
        }
        if (json.has("serviceName")) {
            if (json.isNull("serviceName")) {
                objProxy.realmSet$serviceName(null);
            } else {
                objProxy.realmSet$serviceName((String) json.getString("serviceName"));
            }
        }
        if (json.has("problemStatement")) {
            if (json.isNull("problemStatement")) {
                objProxy.realmSet$problemStatement(null);
            } else {
                objProxy.realmSet$problemStatement((String) json.getString("problemStatement"));
            }
        }
        if (json.has("language")) {
            if (json.isNull("language")) {
                objProxy.realmSet$language(null);
            } else {
                objProxy.realmSet$language((String) json.getString("language"));
            }
        }
        if (json.has("status")) {
            if (json.isNull("status")) {
                objProxy.realmSet$status(null);
            } else {
                objProxy.realmSet$status((String) json.getString("status"));
            }
        }
        if (json.has("serviceIconUrl")) {
            if (json.isNull("serviceIconUrl")) {
                objProxy.realmSet$serviceIconUrl(null);
            } else {
                objProxy.realmSet$serviceIconUrl((String) json.getString("serviceIconUrl"));
            }
        }
        if (json.has("type")) {
            if (json.isNull("type")) {
                objProxy.realmSet$type(null);
            } else {
                objProxy.realmSet$type((String) json.getString("type"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("acceptedAt")) {
            if (json.isNull("acceptedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'acceptedAt' to null.");
            } else {
                objProxy.realmSet$acceptedAt((long) json.getLong("acceptedAt"));
            }
        }
        if (json.has("startedAt")) {
            if (json.isNull("startedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'startedAt' to null.");
            } else {
                objProxy.realmSet$startedAt((long) json.getLong("startedAt"));
            }
        }
        if (json.has("completedAt")) {
            if (json.isNull("completedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'completedAt' to null.");
            } else {
                objProxy.realmSet$completedAt((long) json.getLong("completedAt"));
            }
        }
        if (json.has("closedAt")) {
            if (json.isNull("closedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'closedAt' to null.");
            } else {
                objProxy.realmSet$closedAt((long) json.getLong("closedAt"));
            }
        }
        if (json.has("isRemote")) {
            if (json.isNull("isRemote")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'isRemote' to null.");
            } else {
                objProxy.realmSet$isRemote((boolean) json.getBoolean("isRemote"));
            }
        }
        if (json.has("serviceProvider")) {
            if (json.isNull("serviceProvider")) {
                objProxy.realmSet$serviceProvider(null);
            } else {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("serviceProvider"), update);
                objProxy.realmSet$serviceProvider(serviceProviderObj);
            }
        }
        if (json.has("attributeList")) {
            if (json.isNull("attributeList")) {
                objProxy.realmSet$attributeList(null);
            } else {
                objProxy.realmGet$attributeList().clear();
                JSONArray array = json.getJSONArray("attributeList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$attributeList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest obj = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest();
        final com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("serviceOrderId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceOrderId((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'serviceOrderId' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("serviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceId(null);
                }
            } else if (name.equals("serviceName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceName(null);
                }
            } else if (name.equals("problemStatement")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$problemStatement((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$problemStatement(null);
                }
            } else if (name.equals("language")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$language((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$language(null);
                }
            } else if (name.equals("status")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$status((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$status(null);
                }
            } else if (name.equals("serviceIconUrl")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceIconUrl((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceIconUrl(null);
                }
            } else if (name.equals("type")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$type((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$type(null);
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("acceptedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$acceptedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'acceptedAt' to null.");
                }
            } else if (name.equals("startedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$startedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'startedAt' to null.");
                }
            } else if (name.equals("completedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$completedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'completedAt' to null.");
                }
            } else if (name.equals("closedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$closedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'closedAt' to null.");
                }
            } else if (name.equals("isRemote")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$isRemote((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'isRemote' to null.");
                }
            } else if (name.equals("serviceProvider")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$serviceProvider(null);
                } else {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createUsingJsonStream(realm, reader);
                    objProxy.realmSet$serviceProvider(serviceProviderObj);
                }
            } else if (name.equals("attributeList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$attributeList(null);
                } else {
                    objProxy.realmSet$attributeList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$attributeList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'serviceOrderId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest copyOrUpdate(Realm realm, ServiceRequestColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
            long pkColumnIndex = columnInfo.serviceOrderIdIndex;
            long rowIndex = table.findFirstLong(pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest copy(Realm realm, ServiceRequestColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.serviceOrderIdIndex, realmObjectSource.realmGet$serviceOrderId());
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.serviceNameIndex, realmObjectSource.realmGet$serviceName());
        builder.addString(columnInfo.problemStatementIndex, realmObjectSource.realmGet$problemStatement());
        builder.addString(columnInfo.languageIndex, realmObjectSource.realmGet$language());
        builder.addString(columnInfo.statusIndex, realmObjectSource.realmGet$status());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addString(columnInfo.typeIndex, realmObjectSource.realmGet$type());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.acceptedAtIndex, realmObjectSource.realmGet$acceptedAt());
        builder.addInteger(columnInfo.startedAtIndex, realmObjectSource.realmGet$startedAt());
        builder.addInteger(columnInfo.completedAtIndex, realmObjectSource.realmGet$completedAt());
        builder.addInteger(columnInfo.closedAtIndex, realmObjectSource.realmGet$closedAt());
        builder.addBoolean(columnInfo.isRemoteIndex, realmObjectSource.realmGet$isRemote());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = realmObjectSource.realmGet$serviceProvider();
        if (serviceProviderObj == null) {
            realmObjectCopy.realmSet$serviceProvider(null);
        } else {
            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider cacheserviceProvider = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cache.get(serviceProviderObj);
            if (cacheserviceProvider != null) {
                realmObjectCopy.realmSet$serviceProvider(cacheserviceProvider);
            } else {
                realmObjectCopy.realmSet$serviceProvider(com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class), serviceProviderObj, update, cache, flags));
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = realmObjectSource.realmGet$attributeList();
        if (attributeListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListRealmList = realmObjectCopy.realmGet$attributeList();
            attributeListRealmList.clear();
            for (int i = 0; i < attributeListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem = attributeListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes cacheattributeList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cache.get(attributeListItem);
                if (cacheattributeList != null) {
                    attributeListRealmList.add(cacheattributeList);
                } else {
                    attributeListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class), attributeListItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long tableNativePtr = table.getNativePtr();
        ServiceRequestColumnInfo columnInfo = (ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long pkColumnIndex = columnInfo.serviceOrderIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        }
        String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceName();
        if (realmGet$serviceName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
        }
        String realmGet$problemStatement = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$problemStatement();
        if (realmGet$problemStatement != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, realmGet$problemStatement, false);
        }
        String realmGet$language = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$language();
        if (realmGet$language != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.languageIndex, rowIndex, realmGet$language, false);
        }
        String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$status();
        if (realmGet$status != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        }
        String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.acceptedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$acceptedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.startedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$startedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.completedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$completedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.closedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$closedAt(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isRemoteIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$isRemote(), false);

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceProvider();
        if (serviceProviderObj != null) {
            Long cacheserviceProvider = cache.get(serviceProviderObj);
            if (cacheserviceProvider == null) {
                cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, serviceProviderObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$attributeList();
        if (attributeListList != null) {
            OsList attributeListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.attributeListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem : attributeListList) {
                Long cacheItemIndexattributeList = cache.get(attributeListItem);
                if (cacheItemIndexattributeList == null) {
                    cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, attributeListItem, cache);
                }
                attributeListOsList.addRow(cacheItemIndexattributeList);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long tableNativePtr = table.getNativePtr();
        ServiceRequestColumnInfo columnInfo = (ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long pkColumnIndex = columnInfo.serviceOrderIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            }
            String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceName();
            if (realmGet$serviceName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
            }
            String realmGet$problemStatement = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$problemStatement();
            if (realmGet$problemStatement != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, realmGet$problemStatement, false);
            }
            String realmGet$language = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$language();
            if (realmGet$language != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.languageIndex, rowIndex, realmGet$language, false);
            }
            String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$status();
            if (realmGet$status != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            }
            String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.acceptedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$acceptedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.startedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$startedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.completedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$completedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.closedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$closedAt(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isRemoteIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$isRemote(), false);

            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceProvider();
            if (serviceProviderObj != null) {
                Long cacheserviceProvider = cache.get(serviceProviderObj);
                if (cacheserviceProvider == null) {
                    cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, serviceProviderObj, cache);
                }
                table.setLink(columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$attributeList();
            if (attributeListList != null) {
                OsList attributeListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.attributeListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem : attributeListList) {
                    Long cacheItemIndexattributeList = cache.get(attributeListItem);
                    if (cacheItemIndexattributeList == null) {
                        cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, attributeListItem, cache);
                    }
                    attributeListOsList.addRow(cacheItemIndexattributeList);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long tableNativePtr = table.getNativePtr();
        ServiceRequestColumnInfo columnInfo = (ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long pkColumnIndex = columnInfo.serviceOrderIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
        }
        cache.put(object, rowIndex);
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
        }
        String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceName();
        if (realmGet$serviceName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, false);
        }
        String realmGet$problemStatement = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$problemStatement();
        if (realmGet$problemStatement != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, realmGet$problemStatement, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, false);
        }
        String realmGet$language = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$language();
        if (realmGet$language != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.languageIndex, rowIndex, realmGet$language, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.languageIndex, rowIndex, false);
        }
        String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$status();
        if (realmGet$status != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.statusIndex, rowIndex, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
        }
        String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$type();
        if (realmGet$type != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$createdAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.acceptedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$acceptedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.startedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$startedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.completedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$completedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.closedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$closedAt(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.isRemoteIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$isRemote(), false);

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceProvider();
        if (serviceProviderObj != null) {
            Long cacheserviceProvider = cache.get(serviceProviderObj);
            if (cacheserviceProvider == null) {
                cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, serviceProviderObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex);
        }

        OsList attributeListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.attributeListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$attributeList();
        if (attributeListList != null && attributeListList.size() == attributeListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = attributeListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem = attributeListList.get(i);
                Long cacheItemIndexattributeList = cache.get(attributeListItem);
                if (cacheItemIndexattributeList == null) {
                    cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, attributeListItem, cache);
                }
                attributeListOsList.setRow(i, cacheItemIndexattributeList);
            }
        } else {
            attributeListOsList.removeAll();
            if (attributeListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem : attributeListList) {
                    Long cacheItemIndexattributeList = cache.get(attributeListItem);
                    if (cacheItemIndexattributeList == null) {
                        cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, attributeListItem, cache);
                    }
                    attributeListOsList.addRow(cacheItemIndexattributeList);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long tableNativePtr = table.getNativePtr();
        ServiceRequestColumnInfo columnInfo = (ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        long pkColumnIndex = columnInfo.serviceOrderIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceOrderId());
            }
            cache.put(object, rowIndex);
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
            }
            String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceName();
            if (realmGet$serviceName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, false);
            }
            String realmGet$problemStatement = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$problemStatement();
            if (realmGet$problemStatement != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, realmGet$problemStatement, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.problemStatementIndex, rowIndex, false);
            }
            String realmGet$language = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$language();
            if (realmGet$language != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.languageIndex, rowIndex, realmGet$language, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.languageIndex, rowIndex, false);
            }
            String realmGet$status = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$status();
            if (realmGet$status != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.statusIndex, rowIndex, realmGet$status, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.statusIndex, rowIndex, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
            }
            String realmGet$type = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$type();
            if (realmGet$type != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.typeIndex, rowIndex, realmGet$type, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.typeIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$createdAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.acceptedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$acceptedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.startedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$startedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.completedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$completedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.closedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$closedAt(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.isRemoteIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$isRemote(), false);

            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$serviceProvider();
            if (serviceProviderObj != null) {
                Long cacheserviceProvider = cache.get(serviceProviderObj);
                if (cacheserviceProvider == null) {
                    cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, serviceProviderObj, cache);
                }
                Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
            } else {
                Table.nativeNullifyLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex);
            }

            OsList attributeListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.attributeListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) object).realmGet$attributeList();
            if (attributeListList != null && attributeListList.size() == attributeListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = attributeListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem = attributeListList.get(i);
                    Long cacheItemIndexattributeList = cache.get(attributeListItem);
                    if (cacheItemIndexattributeList == null) {
                        cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, attributeListItem, cache);
                    }
                    attributeListOsList.setRow(i, cacheItemIndexattributeList);
                }
            } else {
                attributeListOsList.removeAll();
                if (attributeListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem : attributeListList) {
                        Long cacheItemIndexattributeList = cache.get(attributeListItem);
                        if (cacheItemIndexattributeList == null) {
                            cacheItemIndexattributeList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, attributeListItem, cache);
                        }
                        attributeListOsList.addRow(cacheItemIndexattributeList);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$serviceOrderId(realmSource.realmGet$serviceOrderId());
        unmanagedCopy.realmSet$serviceId(realmSource.realmGet$serviceId());
        unmanagedCopy.realmSet$serviceName(realmSource.realmGet$serviceName());
        unmanagedCopy.realmSet$problemStatement(realmSource.realmGet$problemStatement());
        unmanagedCopy.realmSet$language(realmSource.realmGet$language());
        unmanagedCopy.realmSet$status(realmSource.realmGet$status());
        unmanagedCopy.realmSet$serviceIconUrl(realmSource.realmGet$serviceIconUrl());
        unmanagedCopy.realmSet$type(realmSource.realmGet$type());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$acceptedAt(realmSource.realmGet$acceptedAt());
        unmanagedCopy.realmSet$startedAt(realmSource.realmGet$startedAt());
        unmanagedCopy.realmSet$completedAt(realmSource.realmGet$completedAt());
        unmanagedCopy.realmSet$closedAt(realmSource.realmGet$closedAt());
        unmanagedCopy.realmSet$isRemote(realmSource.realmGet$isRemote());

        // Deep copy of serviceProvider
        unmanagedCopy.realmSet$serviceProvider(com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createDetachedCopy(realmSource.realmGet$serviceProvider(), currentDepth + 1, maxDepth, cache));

        // Deep copy of attributeList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$attributeList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> managedattributeListList = realmSource.realmGet$attributeList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> unmanagedattributeListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>();
            unmanagedCopy.realmSet$attributeList(unmanagedattributeListList);
            int nextDepth = currentDepth + 1;
            int size = managedattributeListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createDetachedCopy(managedattributeListList.get(i), nextDepth, maxDepth, cache);
                unmanagedattributeListList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest update(Realm realm, ServiceRequestColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest realmObject, com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addInteger(columnInfo.serviceOrderIdIndex, realmObjectSource.realmGet$serviceOrderId());
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.serviceNameIndex, realmObjectSource.realmGet$serviceName());
        builder.addString(columnInfo.problemStatementIndex, realmObjectSource.realmGet$problemStatement());
        builder.addString(columnInfo.languageIndex, realmObjectSource.realmGet$language());
        builder.addString(columnInfo.statusIndex, realmObjectSource.realmGet$status());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addString(columnInfo.typeIndex, realmObjectSource.realmGet$type());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addInteger(columnInfo.acceptedAtIndex, realmObjectSource.realmGet$acceptedAt());
        builder.addInteger(columnInfo.startedAtIndex, realmObjectSource.realmGet$startedAt());
        builder.addInteger(columnInfo.completedAtIndex, realmObjectSource.realmGet$completedAt());
        builder.addInteger(columnInfo.closedAtIndex, realmObjectSource.realmGet$closedAt());
        builder.addBoolean(columnInfo.isRemoteIndex, realmObjectSource.realmGet$isRemote());

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = realmObjectSource.realmGet$serviceProvider();
        if (serviceProviderObj == null) {
            builder.addNull(columnInfo.serviceProviderIndex);
        } else {
            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider cacheserviceProvider = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) cache.get(serviceProviderObj);
            if (cacheserviceProvider != null) {
                builder.addObject(columnInfo.serviceProviderIndex, cacheserviceProvider);
            } else {
                builder.addObject(columnInfo.serviceProviderIndex, com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class), serviceProviderObj, true, cache, flags));
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListList = realmObjectSource.realmGet$attributeList();
        if (attributeListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> attributeListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>();
            for (int i = 0; i < attributeListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes attributeListItem = attributeListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes cacheattributeList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) cache.get(attributeListItem);
                if (cacheattributeList != null) {
                    attributeListManagedCopy.add(cacheattributeList);
                } else {
                    attributeListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class), attributeListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.attributeListIndex, attributeListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.attributeListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes>());
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
        StringBuilder stringBuilder = new StringBuilder("ServiceRequest = proxy[");
        stringBuilder.append("{serviceOrderId:");
        stringBuilder.append(realmGet$serviceOrderId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceId:");
        stringBuilder.append(realmGet$serviceId() != null ? realmGet$serviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceName:");
        stringBuilder.append(realmGet$serviceName() != null ? realmGet$serviceName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{problemStatement:");
        stringBuilder.append(realmGet$problemStatement() != null ? realmGet$problemStatement() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{language:");
        stringBuilder.append(realmGet$language() != null ? realmGet$language() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{status:");
        stringBuilder.append(realmGet$status() != null ? realmGet$status() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceIconUrl:");
        stringBuilder.append(realmGet$serviceIconUrl() != null ? realmGet$serviceIconUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{type:");
        stringBuilder.append(realmGet$type() != null ? realmGet$type() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{acceptedAt:");
        stringBuilder.append(realmGet$acceptedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{startedAt:");
        stringBuilder.append(realmGet$startedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{completedAt:");
        stringBuilder.append(realmGet$completedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{closedAt:");
        stringBuilder.append(realmGet$closedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{isRemote:");
        stringBuilder.append(realmGet$isRemote());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceProvider:");
        stringBuilder.append(realmGet$serviceProvider() != null ? "ServiceProvider" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{attributeList:");
        stringBuilder.append("RealmList<ServiceAttributes>[").append(realmGet$attributeList().size()).append("]");
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
        com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy aServiceRequest = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aServiceRequest.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aServiceRequest.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aServiceRequest.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
