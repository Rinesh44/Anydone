package io.realm;


import android.util.JsonReader;
import io.realm.ImportFlag;
import io.realm.internal.ColumnInfo;
import io.realm.internal.OsObjectSchemaInfo;
import io.realm.internal.OsSchemaInfo;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.RealmProxyMediator;
import io.realm.internal.Row;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

@io.realm.annotations.RealmModule
class DefaultRealmModuleMediator extends RealmProxyMediator {

    private static final Set<Class<? extends RealmModel>> MODEL_CLASSES;
    static {
        Set<Class<? extends RealmModel>> modelClasses = new HashSet<Class<? extends RealmModel>>(17);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class);
        modelClasses.add(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
        MODEL_CLASSES = Collections.unmodifiableSet(modelClasses);
    }

    @Override
    public Map<Class<? extends RealmModel>, OsObjectSchemaInfo> getExpectedObjectSchemaInfoMap() {
        Map<Class<? extends RealmModel>, OsObjectSchemaInfo> infoMap = new HashMap<Class<? extends RealmModel>, OsObjectSchemaInfo>(17);
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Account.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Location.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Card.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Service.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.getExpectedObjectSchemaInfo());
        infoMap.put(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class, io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.getExpectedObjectSchemaInfo());
        return infoMap;
    }

    @Override
    public ColumnInfo createColumnInfo(Class<? extends RealmModel> clazz, OsSchemaInfo schemaInfo) {
        checkClass(clazz);

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createColumnInfo(schemaInfo);
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            return io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.createColumnInfo(schemaInfo);
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public String getSimpleClassNameImpl(Class<? extends RealmModel> clazz) {
        checkClass(clazz);

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            return "Account";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            return "Location";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            return "Customer";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            return "ServiceAttributes";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            return "Card";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            return "Receiver";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            return "Employee";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            return "AssignEmployee";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            return "Conversation";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            return "ServiceRequest";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            return "ServiceOrderEmployee";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            return "Service";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            return "ServiceProvider";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            return "ServiceDoer";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            return "KGraph";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            return "Tags";
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            return "Tickets";
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E newInstance(Class<E> clazz, Object baseRealm, Row row, ColumnInfo columnInfo, boolean acceptDefaultValue, List<String> excludeFields) {
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        try {
            objectContext.set((BaseRealm) baseRealm, row, columnInfo, acceptDefaultValue, excludeFields);
            checkClass(clazz);

            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy());
            }
            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
                return clazz.cast(new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy());
            }
            throw getMissingProxyClassException(clazz);
        } finally {
            objectContext.clear();
        }
    }

    @Override
    public Set<Class<? extends RealmModel>> getModelClasses() {
        return MODEL_CLASSES;
    }

    @Override
    public <E extends RealmModel> E copyOrUpdate(Realm realm, E obj, boolean update, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.AccountColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.AccountColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Account.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Account) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.LocationColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.LocationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Location.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Location) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.CustomerColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.CustomerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Customer) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.ServiceAttributesColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.CardColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.CardColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Card.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Card) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.ReceiverColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.EmployeeColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.EmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Employee) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.AssignEmployeeColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.AssignEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.ConversationColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.ServiceRequestColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.ServiceRequestColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.ServiceOrderEmployeeColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.ServiceOrderEmployeeColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.ServiceColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.ServiceColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Service.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Service) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.ServiceProviderColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.ServiceProviderColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.KGraphColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.TagsColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.TagsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Tags) obj, update, cache, flags));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.TicketsColumnInfo columnInfo = (com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.TicketsColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class);
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.copyOrUpdate(realm, columnInfo, (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) obj, update, cache, flags));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public void insert(Realm realm, RealmModel object, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Account) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Location) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Customer) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Card) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Employee) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Service) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tags) object, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) object, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insert(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Account) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Location) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Customer) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Card) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Employee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Service) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tags) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insert(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insert(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insert(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, RealmModel obj, Map<RealmModel, Long> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((obj instanceof RealmObjectProxy) ? obj.getClass().getSuperclass() : obj.getClass());

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Account) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Location) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Customer) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Card) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Employee) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Service) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tags) obj, cache);
        } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) obj, cache);
        } else {
            throw getMissingProxyClassException(clazz);
        }
    }

    @Override
    public void insertOrUpdate(Realm realm, Collection<? extends RealmModel> objects) {
        Iterator<? extends RealmModel> iterator = objects.iterator();
        RealmModel object = null;
        Map<RealmModel, Long> cache = new HashMap<RealmModel, Long>(objects.size());
        if (iterator.hasNext()) {
            //  access the first element to figure out the clazz for the routing below
            object = iterator.next();
            // This cast is correct because obj is either
            // generated by RealmProxy or the original type extending directly from RealmObject
            @SuppressWarnings("unchecked") Class<RealmModel> clazz = (Class<RealmModel>) ((object instanceof RealmObjectProxy) ? object.getClass().getSuperclass() : object.getClass());

            if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Account) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Location) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Customer) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Card) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Employee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Service) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tags) object, cache);
            } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
                io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insertOrUpdate(realm, (com.treeleaf.anydone.serviceprovider.realm.model.Tickets) object, cache);
            } else {
                throw getMissingProxyClassException(clazz);
            }
            if (iterator.hasNext()) {
                if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
                    io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.insertOrUpdate(realm, iterator, cache);
                } else {
                    throw getMissingProxyClassException(clazz);
                }
            }
        }
    }

    @Override
    public <E extends RealmModel> E createOrUpdateUsingJsonObject(Class<E> clazz, Realm realm, JSONObject json, boolean update)
        throws JSONException {
        checkClass(clazz);

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.createOrUpdateUsingJsonObject(realm, json, update));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createUsingJsonStream(Class<E> clazz, Realm realm, JsonReader reader)
        throws IOException {
        checkClass(clazz);

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createUsingJsonStream(realm, reader));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.createUsingJsonStream(realm, reader));
        }
        throw getMissingProxyClassException(clazz);
    }

    @Override
    public <E extends RealmModel> E createDetachedCopy(E realmObject, int maxDepth, Map<RealmModel, RealmObjectProxy.CacheData<RealmModel>> cache) {
        // This cast is correct because obj is either
        // generated by RealmProxy or the original type extending directly from RealmObject
        @SuppressWarnings("unchecked") Class<E> clazz = (Class<E>) realmObject.getClass().getSuperclass();

        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Account.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AccountRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Account) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Location.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_LocationRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Location) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Customer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CustomerRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Customer) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceAttributesRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Card.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_CardRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Card) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Receiver) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Employee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_EmployeeRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Employee) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_AssignEmployeeRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.AssignEmployee) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Conversation) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRequestRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.ServiceRequest) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceOrderEmployeeRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.ServiceOrderEmployee) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Service.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Service) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceProviderRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.KGraph) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tags.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TagsRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Tags) realmObject, 0, maxDepth, cache));
        }
        if (clazz.equals(com.treeleaf.anydone.serviceprovider.realm.model.Tickets.class)) {
            return clazz.cast(io.realm.com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxy.createDetachedCopy((com.treeleaf.anydone.serviceprovider.realm.model.Tickets) realmObject, 0, maxDepth, cache));
        }
        throw getMissingProxyClassException(clazz);
    }

}
