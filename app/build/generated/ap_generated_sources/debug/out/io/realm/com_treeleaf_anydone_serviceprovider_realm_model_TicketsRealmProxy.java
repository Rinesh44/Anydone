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
public class com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Tickets
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface {

    static final class TicketsColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long ticketIdIndex;
        long titleIndex;
        long descriptionIndex;
        long customerIndex;
        long serviceProviderIndex;
        long ticketSourceIndex;
        long tagsRealmListIndex;
        long serviceIdIndex;
        long customerTypeIndex;
        long ticketTypeIndex;
        long createdAtIndex;
        long ticketStatusIndex;
        long assignedEmployeeIndex;

        TicketsColumnInfo(OsSchemaInfo schemaInfo) {
            super(13);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Tickets");
            this.ticketIdIndex = addColumnDetails("ticketId", "ticketId", objectSchemaInfo);
            this.titleIndex = addColumnDetails("title", "title", objectSchemaInfo);
            this.descriptionIndex = addColumnDetails("description", "description", objectSchemaInfo);
            this.customerIndex = addColumnDetails("customer", "customer", objectSchemaInfo);
            this.serviceProviderIndex = addColumnDetails("serviceProvider", "serviceProvider", objectSchemaInfo);
            this.ticketSourceIndex = addColumnDetails("ticketSource", "ticketSource", objectSchemaInfo);
            this.tagsRealmListIndex = addColumnDetails("tagsRealmList", "tagsRealmList", objectSchemaInfo);
            this.serviceIdIndex = addColumnDetails("serviceId", "serviceId", objectSchemaInfo);
            this.customerTypeIndex = addColumnDetails("customerType", "customerType", objectSchemaInfo);
            this.ticketTypeIndex = addColumnDetails("ticketType", "ticketType", objectSchemaInfo);
            this.createdAtIndex = addColumnDetails("createdAt", "createdAt", objectSchemaInfo);
            this.ticketStatusIndex = addColumnDetails("ticketStatus", "ticketStatus", objectSchemaInfo);
            this.assignedEmployeeIndex = addColumnDetails("assignedEmployee", "assignedEmployee", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        TicketsColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new TicketsColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final TicketsColumnInfo src = (TicketsColumnInfo) rawSrc;
            final TicketsColumnInfo dst = (TicketsColumnInfo) rawDst;
            dst.ticketIdIndex = src.ticketIdIndex;
            dst.titleIndex = src.titleIndex;
            dst.descriptionIndex = src.descriptionIndex;
            dst.customerIndex = src.customerIndex;
            dst.serviceProviderIndex = src.serviceProviderIndex;
            dst.ticketSourceIndex = src.ticketSourceIndex;
            dst.tagsRealmListIndex = src.tagsRealmListIndex;
            dst.serviceIdIndex = src.serviceIdIndex;
            dst.customerTypeIndex = src.customerTypeIndex;
            dst.ticketTypeIndex = src.ticketTypeIndex;
            dst.createdAtIndex = src.createdAtIndex;
            dst.ticketStatusIndex = src.ticketStatusIndex;
            dst.assignedEmployeeIndex = src.assignedEmployeeIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private TicketsColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Tickets> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListRealmList;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (TicketsColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Tickets>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$ticketId() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.ticketIdIndex);
    }

    @Override
    public void realmSet$ticketId(long value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'ticketId' cannot be changed after object was created.");
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
    public String realmGet$description() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.descriptionIndex);
    }

    @Override
    public void realmSet$description(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.descriptionIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.descriptionIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.descriptionIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.descriptionIndex, value);
    }

    @Override
    public com.treeleaf.anydone.serviceprovider.realm.model.Customer realmGet$customer() {
        proxyState.getRealm$realm().checkIfValid();
        if (proxyState.getRow$realm().isNullLink(columnInfo.customerIndex)) {
            return null;
        }
        return proxyState.getRealm$realm().get(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class, proxyState.getRow$realm().getLink(columnInfo.customerIndex), false, Collections.<String>emptyList());
    }

    @Override
    public void realmSet$customer(com.treeleaf.anydone.serviceprovider.realm.model.Customer value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("customer")) {
                return;
            }
            if (value != null && !RealmObject.isManaged(value)) {
                value = ((Realm) proxyState.getRealm$realm()).copyToRealm(value);
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                // Table#nullifyLink() does not support default value. Just using Row.
                row.nullifyLink(columnInfo.customerIndex);
                return;
            }
            proxyState.checkValidObject(value);
            row.getTable().setLink(columnInfo.customerIndex, row.getIndex(), ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex(), true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().nullifyLink(columnInfo.customerIndex);
            return;
        }
        proxyState.checkValidObject(value);
        proxyState.getRow$realm().setLink(columnInfo.customerIndex, ((RealmObjectProxy) value).realmGet$proxyState().getRow$realm().getIndex());
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
    @SuppressWarnings("cast")
    public String realmGet$ticketSource() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.ticketSourceIndex);
    }

    @Override
    public void realmSet$ticketSource(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.ticketSourceIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.ticketSourceIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.ticketSourceIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.ticketSourceIndex, value);
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> realmGet$tagsRealmList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (tagsRealmListRealmList != null) {
            return tagsRealmListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.tagsRealmListIndex);
            tagsRealmListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class, osList, proxyState.getRealm$realm());
            return tagsRealmListRealmList;
        }
    }

    @Override
    public void realmSet$tagsRealmList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("tagsRealmList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.Tags item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.tagsRealmListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Tags linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.Tags linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
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
    public String realmGet$customerType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.customerTypeIndex);
    }

    @Override
    public void realmSet$customerType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.customerTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.customerTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.customerTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.customerTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$ticketType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.ticketTypeIndex);
    }

    @Override
    public void realmSet$ticketType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.ticketTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.ticketTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.ticketTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.ticketTypeIndex, value);
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
    public String realmGet$ticketStatus() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.ticketStatusIndex);
    }

    @Override
    public void realmSet$ticketStatus(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.ticketStatusIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.ticketStatusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.ticketStatusIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.ticketStatusIndex, value);
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> realmGet$assignedEmployee() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (assignedEmployeeRealmList != null) {
            return assignedEmployeeRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.assignedEmployeeIndex);
            assignedEmployeeRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class, osList, proxyState.getRealm$realm());
            return assignedEmployeeRealmList;
        }
    }

    @Override
    public void realmSet$assignedEmployee(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("assignedEmployee")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.Employee item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.assignedEmployeeIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Employee linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.Employee linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Tickets", 13, 0);
        builder.addPersistedProperty("ticketId", RealmFieldType.INTEGER, Property.PRIMARY_KEY, Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("title", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("description", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("customer", RealmFieldType.OBJECT, "Customer");
        builder.addPersistedLinkProperty("serviceProvider", RealmFieldType.OBJECT, "ServiceProvider");
        builder.addPersistedProperty("ticketSource", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("tagsRealmList", RealmFieldType.LIST, "Tags");
        builder.addPersistedProperty("serviceId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("customerType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("ticketType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("createdAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("ticketStatus", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("assignedEmployee", RealmFieldType.LIST, "Employee");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static TicketsColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new TicketsColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Tickets";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Tickets";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Tickets createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(4);
        com.treeleaf.anydone.serviceprovider.realm.model.Tickets obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
            TicketsColumnInfo columnInfo = (TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
            long pkColumnIndex = columnInfo.ticketIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (!json.isNull("ticketId")) {
                rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("ticketId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("customer")) {
                excludeFields.add("customer");
            }
            if (json.has("serviceProvider")) {
                excludeFields.add("serviceProvider");
            }
            if (json.has("tagsRealmList")) {
                excludeFields.add("tagsRealmList");
            }
            if (json.has("assignedEmployee")) {
                excludeFields.add("assignedEmployee");
            }
            if (json.has("ticketId")) {
                if (json.isNull("ticketId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class, json.getLong("ticketId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'ticketId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) obj;
        if (json.has("title")) {
            if (json.isNull("title")) {
                objProxy.realmSet$title(null);
            } else {
                objProxy.realmSet$title((String) json.getString("title"));
            }
        }
        if (json.has("description")) {
            if (json.isNull("description")) {
                objProxy.realmSet$description(null);
            } else {
                objProxy.realmSet$description((String) json.getString("description"));
            }
        }
        if (json.has("customer")) {
            if (json.isNull("customer")) {
                objProxy.realmSet$customer(null);
            } else {
                com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createOrUpdateUsingJsonObject(realm, json.getJSONObject("customer"), update);
                objProxy.realmSet$customer(customerObj);
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
        if (json.has("ticketSource")) {
            if (json.isNull("ticketSource")) {
                objProxy.realmSet$ticketSource(null);
            } else {
                objProxy.realmSet$ticketSource((String) json.getString("ticketSource"));
            }
        }
        if (json.has("tagsRealmList")) {
            if (json.isNull("tagsRealmList")) {
                objProxy.realmSet$tagsRealmList(null);
            } else {
                objProxy.realmGet$tagsRealmList().clear();
                JSONArray array = json.getJSONArray("tagsRealmList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Tags item = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$tagsRealmList().add(item);
                }
            }
        }
        if (json.has("serviceId")) {
            if (json.isNull("serviceId")) {
                objProxy.realmSet$serviceId(null);
            } else {
                objProxy.realmSet$serviceId((String) json.getString("serviceId"));
            }
        }
        if (json.has("customerType")) {
            if (json.isNull("customerType")) {
                objProxy.realmSet$customerType(null);
            } else {
                objProxy.realmSet$customerType((String) json.getString("customerType"));
            }
        }
        if (json.has("ticketType")) {
            if (json.isNull("ticketType")) {
                objProxy.realmSet$ticketType(null);
            } else {
                objProxy.realmSet$ticketType((String) json.getString("ticketType"));
            }
        }
        if (json.has("createdAt")) {
            if (json.isNull("createdAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
            } else {
                objProxy.realmSet$createdAt((long) json.getLong("createdAt"));
            }
        }
        if (json.has("ticketStatus")) {
            if (json.isNull("ticketStatus")) {
                objProxy.realmSet$ticketStatus(null);
            } else {
                objProxy.realmSet$ticketStatus((String) json.getString("ticketStatus"));
            }
        }
        if (json.has("assignedEmployee")) {
            if (json.isNull("assignedEmployee")) {
                objProxy.realmSet$assignedEmployee(null);
            } else {
                objProxy.realmGet$assignedEmployee().clear();
                JSONArray array = json.getJSONArray("assignedEmployee");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Employee item = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$assignedEmployee().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Tickets createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Tickets obj = new com.treeleaf.anydone.serviceprovider.realm.model.Tickets();
        final com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("ticketId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ticketId((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'ticketId' to null.");
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("title")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$title((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$title(null);
                }
            } else if (name.equals("description")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$description((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$description(null);
                }
            } else if (name.equals("customer")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$customer(null);
                } else {
                    com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createUsingJsonStream(realm, reader);
                    objProxy.realmSet$customer(customerObj);
                }
            } else if (name.equals("serviceProvider")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$serviceProvider(null);
                } else {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createUsingJsonStream(realm, reader);
                    objProxy.realmSet$serviceProvider(serviceProviderObj);
                }
            } else if (name.equals("ticketSource")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ticketSource((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ticketSource(null);
                }
            } else if (name.equals("tagsRealmList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$tagsRealmList(null);
                } else {
                    objProxy.realmSet$tagsRealmList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.Tags item = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$tagsRealmList().add(item);
                    }
                    reader.endArray();
                }
            } else if (name.equals("serviceId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceId(null);
                }
            } else if (name.equals("customerType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$customerType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$customerType(null);
                }
            } else if (name.equals("ticketType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ticketType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ticketType(null);
                }
            } else if (name.equals("createdAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$createdAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'createdAt' to null.");
                }
            } else if (name.equals("ticketStatus")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$ticketStatus((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$ticketStatus(null);
                }
            } else if (name.equals("assignedEmployee")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$assignedEmployee(null);
                } else {
                    objProxy.realmSet$assignedEmployee(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.Employee item = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$assignedEmployee().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'ticketId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Tickets copyOrUpdate(Realm realm, TicketsColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Tickets object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Tickets realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
            long pkColumnIndex = columnInfo.ticketIdIndex;
            long rowIndex = table.findFirstLong(pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Tickets copy(Realm realm, TicketsColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Tickets newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addInteger(columnInfo.ticketIdIndex, realmObjectSource.realmGet$ticketId());
        builder.addString(columnInfo.titleIndex, realmObjectSource.realmGet$title());
        builder.addString(columnInfo.descriptionIndex, realmObjectSource.realmGet$description());
        builder.addString(columnInfo.ticketSourceIndex, realmObjectSource.realmGet$ticketSource());
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.customerTypeIndex, realmObjectSource.realmGet$customerType());
        builder.addString(columnInfo.ticketTypeIndex, realmObjectSource.realmGet$ticketType());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addString(columnInfo.ticketStatusIndex, realmObjectSource.realmGet$ticketStatus());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = realmObjectSource.realmGet$customer();
        if (customerObj == null) {
            realmObjectCopy.realmSet$customer(null);
        } else {
            com.treeleaf.anydone.serviceprovider.realm.model.Customer cachecustomer = (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cache.get(customerObj);
            if (cachecustomer != null) {
                realmObjectCopy.realmSet$customer(cachecustomer);
            } else {
                realmObjectCopy.realmSet$customer(com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class), customerObj, update, cache, flags));
            }
        }

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

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = realmObjectSource.realmGet$tagsRealmList();
        if (tagsRealmListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListRealmList = realmObjectCopy.realmGet$tagsRealmList();
            tagsRealmListRealmList.clear();
            for (int i = 0; i < tagsRealmListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem = tagsRealmListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Tags cachetagsRealmList = (com.treeleaf.anydone.serviceprovider.realm.model.Tags) cache.get(tagsRealmListItem);
                if (cachetagsRealmList != null) {
                    tagsRealmListRealmList.add(cachetagsRealmList);
                } else {
                    tagsRealmListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.TagsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class), tagsRealmListItem, update, cache, flags));
                }
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = realmObjectSource.realmGet$assignedEmployee();
        if (assignedEmployeeList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeRealmList = realmObjectCopy.realmGet$assignedEmployee();
            assignedEmployeeRealmList.clear();
            for (int i = 0; i < assignedEmployeeList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem = assignedEmployeeList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Employee cacheassignedEmployee = (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cache.get(assignedEmployeeItem);
                if (cacheassignedEmployee != null) {
                    assignedEmployeeRealmList.add(cacheassignedEmployee);
                } else {
                    assignedEmployeeRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class), assignedEmployeeItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Tickets object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long tableNativePtr = table.getNativePtr();
        TicketsColumnInfo columnInfo = (TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long pkColumnIndex = columnInfo.ticketIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        }
        String realmGet$description = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customer();
        if (customerObj != null) {
            Long cachecustomer = cache.get(customerObj);
            if (cachecustomer == null) {
                cachecustomer = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insert(realm, customerObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.customerIndex, rowIndex, cachecustomer, false);
        }

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceProvider();
        if (serviceProviderObj != null) {
            Long cacheserviceProvider = cache.get(serviceProviderObj);
            if (cacheserviceProvider == null) {
                cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, serviceProviderObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
        }
        String realmGet$ticketSource = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketSource();
        if (realmGet$ticketSource != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, realmGet$ticketSource, false);
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$tagsRealmList();
        if (tagsRealmListList != null) {
            OsList tagsRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.tagsRealmListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem : tagsRealmListList) {
                Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                if (cacheItemIndextagsRealmList == null) {
                    cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insert(realm, tagsRealmListItem, cache);
                }
                tagsRealmListOsList.addRow(cacheItemIndextagsRealmList);
            }
        }
        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        }
        String realmGet$customerType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customerType();
        if (realmGet$customerType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, realmGet$customerType, false);
        }
        String realmGet$ticketType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketType();
        if (realmGet$ticketType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, realmGet$ticketType, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$ticketStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketStatus();
        if (realmGet$ticketStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, realmGet$ticketStatus, false);
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$assignedEmployee();
        if (assignedEmployeeList != null) {
            OsList assignedEmployeeOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.assignedEmployeeIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem : assignedEmployeeList) {
                Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                if (cacheItemIndexassignedEmployee == null) {
                    cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insert(realm, assignedEmployeeItem, cache);
                }
                assignedEmployeeOsList.addRow(cacheItemIndexassignedEmployee);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long tableNativePtr = table.getNativePtr();
        TicketsColumnInfo columnInfo = (TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long pkColumnIndex = columnInfo.ticketIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Tickets object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$title();
            if (realmGet$title != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
            }
            String realmGet$description = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$description();
            if (realmGet$description != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
            }

            com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customer();
            if (customerObj != null) {
                Long cachecustomer = cache.get(customerObj);
                if (cachecustomer == null) {
                    cachecustomer = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insert(realm, customerObj, cache);
                }
                table.setLink(columnInfo.customerIndex, rowIndex, cachecustomer, false);
            }

            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceProvider();
            if (serviceProviderObj != null) {
                Long cacheserviceProvider = cache.get(serviceProviderObj);
                if (cacheserviceProvider == null) {
                    cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, serviceProviderObj, cache);
                }
                table.setLink(columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
            }
            String realmGet$ticketSource = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketSource();
            if (realmGet$ticketSource != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, realmGet$ticketSource, false);
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$tagsRealmList();
            if (tagsRealmListList != null) {
                OsList tagsRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.tagsRealmListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem : tagsRealmListList) {
                    Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                    if (cacheItemIndextagsRealmList == null) {
                        cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insert(realm, tagsRealmListItem, cache);
                    }
                    tagsRealmListOsList.addRow(cacheItemIndextagsRealmList);
                }
            }
            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            }
            String realmGet$customerType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customerType();
            if (realmGet$customerType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, realmGet$customerType, false);
            }
            String realmGet$ticketType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketType();
            if (realmGet$ticketType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, realmGet$ticketType, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$ticketStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketStatus();
            if (realmGet$ticketStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, realmGet$ticketStatus, false);
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$assignedEmployee();
            if (assignedEmployeeList != null) {
                OsList assignedEmployeeOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.assignedEmployeeIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem : assignedEmployeeList) {
                    Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                    if (cacheItemIndexassignedEmployee == null) {
                        cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insert(realm, assignedEmployeeItem, cache);
                    }
                    assignedEmployeeOsList.addRow(cacheItemIndexassignedEmployee);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Tickets object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long tableNativePtr = table.getNativePtr();
        TicketsColumnInfo columnInfo = (TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long pkColumnIndex = columnInfo.ticketIdIndex;
        long rowIndex = Table.NO_MATCH;
        Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId();
        if (primaryKeyValue != null) {
            rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
        }
        cache.put(object, rowIndex);
        String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$title();
        if (realmGet$title != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
        }
        String realmGet$description = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$description();
        if (realmGet$description != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customer();
        if (customerObj != null) {
            Long cachecustomer = cache.get(customerObj);
            if (cachecustomer == null) {
                cachecustomer = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insertOrUpdate(realm, customerObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.customerIndex, rowIndex, cachecustomer, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.customerIndex, rowIndex);
        }

        com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceProvider();
        if (serviceProviderObj != null) {
            Long cacheserviceProvider = cache.get(serviceProviderObj);
            if (cacheserviceProvider == null) {
                cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, serviceProviderObj, cache);
            }
            Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
        } else {
            Table.nativeNullifyLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex);
        }
        String realmGet$ticketSource = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketSource();
        if (realmGet$ticketSource != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, realmGet$ticketSource, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, false);
        }

        OsList tagsRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.tagsRealmListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$tagsRealmList();
        if (tagsRealmListList != null && tagsRealmListList.size() == tagsRealmListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = tagsRealmListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem = tagsRealmListList.get(i);
                Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                if (cacheItemIndextagsRealmList == null) {
                    cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, tagsRealmListItem, cache);
                }
                tagsRealmListOsList.setRow(i, cacheItemIndextagsRealmList);
            }
        } else {
            tagsRealmListOsList.removeAll();
            if (tagsRealmListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem : tagsRealmListList) {
                    Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                    if (cacheItemIndextagsRealmList == null) {
                        cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, tagsRealmListItem, cache);
                    }
                    tagsRealmListOsList.addRow(cacheItemIndextagsRealmList);
                }
            }
        }

        String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceId();
        if (realmGet$serviceId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
        }
        String realmGet$customerType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customerType();
        if (realmGet$customerType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, realmGet$customerType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, false);
        }
        String realmGet$ticketType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketType();
        if (realmGet$ticketType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, realmGet$ticketType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$createdAt(), false);
        String realmGet$ticketStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketStatus();
        if (realmGet$ticketStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, realmGet$ticketStatus, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, false);
        }

        OsList assignedEmployeeOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.assignedEmployeeIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$assignedEmployee();
        if (assignedEmployeeList != null && assignedEmployeeList.size() == assignedEmployeeOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = assignedEmployeeList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem = assignedEmployeeList.get(i);
                Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                if (cacheItemIndexassignedEmployee == null) {
                    cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, assignedEmployeeItem, cache);
                }
                assignedEmployeeOsList.setRow(i, cacheItemIndexassignedEmployee);
            }
        } else {
            assignedEmployeeOsList.removeAll();
            if (assignedEmployeeList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem : assignedEmployeeList) {
                    Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                    if (cacheItemIndexassignedEmployee == null) {
                        cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, assignedEmployeeItem, cache);
                    }
                    assignedEmployeeOsList.addRow(cacheItemIndexassignedEmployee);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long tableNativePtr = table.getNativePtr();
        TicketsColumnInfo columnInfo = (TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        long pkColumnIndex = columnInfo.ticketIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Tickets object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            long rowIndex = Table.NO_MATCH;
            Object primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId();
            if (primaryKeyValue != null) {
                rowIndex = Table.nativeFindFirstInt(tableNativePtr, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketId());
            }
            cache.put(object, rowIndex);
            String realmGet$title = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$title();
            if (realmGet$title != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.titleIndex, rowIndex, realmGet$title, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.titleIndex, rowIndex, false);
            }
            String realmGet$description = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$description();
            if (realmGet$description != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.descriptionIndex, rowIndex, realmGet$description, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.descriptionIndex, rowIndex, false);
            }

            com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customer();
            if (customerObj != null) {
                Long cachecustomer = cache.get(customerObj);
                if (cachecustomer == null) {
                    cachecustomer = com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insertOrUpdate(realm, customerObj, cache);
                }
                Table.nativeSetLink(tableNativePtr, columnInfo.customerIndex, rowIndex, cachecustomer, false);
            } else {
                Table.nativeNullifyLink(tableNativePtr, columnInfo.customerIndex, rowIndex);
            }

            com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider serviceProviderObj = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceProvider();
            if (serviceProviderObj != null) {
                Long cacheserviceProvider = cache.get(serviceProviderObj);
                if (cacheserviceProvider == null) {
                    cacheserviceProvider = com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, serviceProviderObj, cache);
                }
                Table.nativeSetLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex, cacheserviceProvider, false);
            } else {
                Table.nativeNullifyLink(tableNativePtr, columnInfo.serviceProviderIndex, rowIndex);
            }
            String realmGet$ticketSource = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketSource();
            if (realmGet$ticketSource != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, realmGet$ticketSource, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.ticketSourceIndex, rowIndex, false);
            }

            OsList tagsRealmListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.tagsRealmListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$tagsRealmList();
            if (tagsRealmListList != null && tagsRealmListList.size() == tagsRealmListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = tagsRealmListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem = tagsRealmListList.get(i);
                    Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                    if (cacheItemIndextagsRealmList == null) {
                        cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, tagsRealmListItem, cache);
                    }
                    tagsRealmListOsList.setRow(i, cacheItemIndextagsRealmList);
                }
            } else {
                tagsRealmListOsList.removeAll();
                if (tagsRealmListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem : tagsRealmListList) {
                        Long cacheItemIndextagsRealmList = cache.get(tagsRealmListItem);
                        if (cacheItemIndextagsRealmList == null) {
                            cacheItemIndextagsRealmList = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, tagsRealmListItem, cache);
                        }
                        tagsRealmListOsList.addRow(cacheItemIndextagsRealmList);
                    }
                }
            }

            String realmGet$serviceId = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$serviceId();
            if (realmGet$serviceId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, realmGet$serviceId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIdIndex, rowIndex, false);
            }
            String realmGet$customerType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$customerType();
            if (realmGet$customerType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, realmGet$customerType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.customerTypeIndex, rowIndex, false);
            }
            String realmGet$ticketType = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketType();
            if (realmGet$ticketType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, realmGet$ticketType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.ticketTypeIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.createdAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$createdAt(), false);
            String realmGet$ticketStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$ticketStatus();
            if (realmGet$ticketStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, realmGet$ticketStatus, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.ticketStatusIndex, rowIndex, false);
            }

            OsList assignedEmployeeOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.assignedEmployeeIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = ((com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) object).realmGet$assignedEmployee();
            if (assignedEmployeeList != null && assignedEmployeeList.size() == assignedEmployeeOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = assignedEmployeeList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem = assignedEmployeeList.get(i);
                    Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                    if (cacheItemIndexassignedEmployee == null) {
                        cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, assignedEmployeeItem, cache);
                    }
                    assignedEmployeeOsList.setRow(i, cacheItemIndexassignedEmployee);
                }
            } else {
                assignedEmployeeOsList.removeAll();
                if (assignedEmployeeList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem : assignedEmployeeList) {
                        Long cacheItemIndexassignedEmployee = cache.get(assignedEmployeeItem);
                        if (cacheItemIndexassignedEmployee == null) {
                            cacheItemIndexassignedEmployee = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, assignedEmployeeItem, cache);
                        }
                        assignedEmployeeOsList.addRow(cacheItemIndexassignedEmployee);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Tickets createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Tickets realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Tickets unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Tickets();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$ticketId(realmSource.realmGet$ticketId());
        unmanagedCopy.realmSet$title(realmSource.realmGet$title());
        unmanagedCopy.realmSet$description(realmSource.realmGet$description());

        // Deep copy of customer
        unmanagedCopy.realmSet$customer(com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createDetachedCopy(realmSource.realmGet$customer(), currentDepth + 1, maxDepth, cache));

        // Deep copy of serviceProvider
        unmanagedCopy.realmSet$serviceProvider(com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createDetachedCopy(realmSource.realmGet$serviceProvider(), currentDepth + 1, maxDepth, cache));
        unmanagedCopy.realmSet$ticketSource(realmSource.realmGet$ticketSource());

        // Deep copy of tagsRealmList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$tagsRealmList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> managedtagsRealmListList = realmSource.realmGet$tagsRealmList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> unmanagedtagsRealmListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>();
            unmanagedCopy.realmSet$tagsRealmList(unmanagedtagsRealmListList);
            int nextDepth = currentDepth + 1;
            int size = managedtagsRealmListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Tags item = com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createDetachedCopy(managedtagsRealmListList.get(i), nextDepth, maxDepth, cache);
                unmanagedtagsRealmListList.add(item);
            }
        }
        unmanagedCopy.realmSet$serviceId(realmSource.realmGet$serviceId());
        unmanagedCopy.realmSet$customerType(realmSource.realmGet$customerType());
        unmanagedCopy.realmSet$ticketType(realmSource.realmGet$ticketType());
        unmanagedCopy.realmSet$createdAt(realmSource.realmGet$createdAt());
        unmanagedCopy.realmSet$ticketStatus(realmSource.realmGet$ticketStatus());

        // Deep copy of assignedEmployee
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$assignedEmployee(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> managedassignedEmployeeList = realmSource.realmGet$assignedEmployee();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> unmanagedassignedEmployeeList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>();
            unmanagedCopy.realmSet$assignedEmployee(unmanagedassignedEmployeeList);
            int nextDepth = currentDepth + 1;
            int size = managedassignedEmployeeList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Employee item = com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createDetachedCopy(managedassignedEmployeeList.get(i), nextDepth, maxDepth, cache);
                unmanagedassignedEmployeeList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Tickets update(Realm realm, TicketsColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Tickets realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Tickets newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addInteger(columnInfo.ticketIdIndex, realmObjectSource.realmGet$ticketId());
        builder.addString(columnInfo.titleIndex, realmObjectSource.realmGet$title());
        builder.addString(columnInfo.descriptionIndex, realmObjectSource.realmGet$description());

        com.treeleaf.anydone.serviceprovider.realm.model.Customer customerObj = realmObjectSource.realmGet$customer();
        if (customerObj == null) {
            builder.addNull(columnInfo.customerIndex);
        } else {
            com.treeleaf.anydone.serviceprovider.realm.model.Customer cachecustomer = (com.treeleaf.anydone.serviceprovider.realm.model.Customer) cache.get(customerObj);
            if (cachecustomer != null) {
                builder.addObject(columnInfo.customerIndex, cachecustomer);
            } else {
                builder.addObject(columnInfo.customerIndex, com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class), customerObj, true, cache, flags));
            }
        }

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
        builder.addString(columnInfo.ticketSourceIndex, realmObjectSource.realmGet$ticketSource());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListList = realmObjectSource.realmGet$tagsRealmList();
        if (tagsRealmListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> tagsRealmListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>();
            for (int i = 0; i < tagsRealmListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Tags tagsRealmListItem = tagsRealmListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Tags cachetagsRealmList = (com.treeleaf.anydone.serviceprovider.realm.model.Tags) cache.get(tagsRealmListItem);
                if (cachetagsRealmList != null) {
                    tagsRealmListManagedCopy.add(cachetagsRealmList);
                } else {
                    tagsRealmListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.TagsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class), tagsRealmListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.tagsRealmListIndex, tagsRealmListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.tagsRealmListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags>());
        }
        builder.addString(columnInfo.serviceIdIndex, realmObjectSource.realmGet$serviceId());
        builder.addString(columnInfo.customerTypeIndex, realmObjectSource.realmGet$customerType());
        builder.addString(columnInfo.ticketTypeIndex, realmObjectSource.realmGet$ticketType());
        builder.addInteger(columnInfo.createdAtIndex, realmObjectSource.realmGet$createdAt());
        builder.addString(columnInfo.ticketStatusIndex, realmObjectSource.realmGet$ticketStatus());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeList = realmObjectSource.realmGet$assignedEmployee();
        if (assignedEmployeeList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> assignedEmployeeManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>();
            for (int i = 0; i < assignedEmployeeList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Employee assignedEmployeeItem = assignedEmployeeList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Employee cacheassignedEmployee = (com.treeleaf.anydone.serviceprovider.realm.model.Employee) cache.get(assignedEmployeeItem);
                if (cacheassignedEmployee != null) {
                    assignedEmployeeManagedCopy.add(cacheassignedEmployee);
                } else {
                    assignedEmployeeManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class), assignedEmployeeItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.assignedEmployeeIndex, assignedEmployeeManagedCopy);
        } else {
            builder.addObjectList(columnInfo.assignedEmployeeIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee>());
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
        StringBuilder stringBuilder = new StringBuilder("Tickets = proxy[");
        stringBuilder.append("{ticketId:");
        stringBuilder.append(realmGet$ticketId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{title:");
        stringBuilder.append(realmGet$title() != null ? realmGet$title() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{description:");
        stringBuilder.append(realmGet$description() != null ? realmGet$description() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{customer:");
        stringBuilder.append(realmGet$customer() != null ? "Customer" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceProvider:");
        stringBuilder.append(realmGet$serviceProvider() != null ? "ServiceProvider" : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ticketSource:");
        stringBuilder.append(realmGet$ticketSource() != null ? realmGet$ticketSource() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{tagsRealmList:");
        stringBuilder.append("RealmList<Tags>[").append(realmGet$tagsRealmList().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceId:");
        stringBuilder.append(realmGet$serviceId() != null ? realmGet$serviceId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{customerType:");
        stringBuilder.append(realmGet$customerType() != null ? realmGet$customerType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ticketType:");
        stringBuilder.append(realmGet$ticketType() != null ? realmGet$ticketType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{createdAt:");
        stringBuilder.append(realmGet$createdAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{ticketStatus:");
        stringBuilder.append(realmGet$ticketStatus() != null ? realmGet$ticketStatus() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{assignedEmployee:");
        stringBuilder.append("RealmList<Employee>[").append(realmGet$assignedEmployee().size()).append("]");
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
        com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy aTickets = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aTickets.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aTickets.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aTickets.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
