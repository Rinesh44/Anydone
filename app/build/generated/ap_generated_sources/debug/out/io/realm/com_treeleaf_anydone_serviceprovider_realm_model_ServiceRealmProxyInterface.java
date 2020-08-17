package io.realm;


public interface com_treeleaf_anydone_serviceprovider_realm_model_ServiceRealmProxyInterface {
    public String realmGet$serviceId();
    public void realmSet$serviceId(String value);
    public String realmGet$name();
    public void realmSet$name(String value);
    public String realmGet$desc();
    public void realmSet$desc(String value);
    public String realmGet$serviceType();
    public void realmSet$serviceType(String value);
    public String realmGet$serviceIconUrl();
    public void realmSet$serviceIconUrl(String value);
    public long realmGet$createdAt();
    public void realmSet$createdAt(long value);
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> realmGet$serviceAttributesList();
    public void realmSet$serviceAttributesList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceAttributes> value);
}
