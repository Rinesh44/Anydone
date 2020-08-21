package io.realm;


public interface com_treeleaf_anydone_serviceprovider_realm_model_TicketsRealmProxyInterface {
    public long realmGet$ticketId();
    public void realmSet$ticketId(long value);
    public String realmGet$title();
    public void realmSet$title(String value);
    public String realmGet$description();
    public void realmSet$description(String value);
    public com.treeleaf.anydone.serviceprovider.realm.model.Customer realmGet$customer();
    public void realmSet$customer(com.treeleaf.anydone.serviceprovider.realm.model.Customer value);
    public com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider realmGet$serviceProvider();
    public void realmSet$serviceProvider(com.treeleaf.anydone.serviceprovider.realm.model.ServiceProvider value);
    public String realmGet$ticketSource();
    public void realmSet$ticketSource(String value);
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> realmGet$tagsRealmList();
    public void realmSet$tagsRealmList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Tags> value);
    public String realmGet$serviceId();
    public void realmSet$serviceId(String value);
    public String realmGet$customerType();
    public void realmSet$customerType(String value);
    public String realmGet$ticketType();
    public void realmSet$ticketType(String value);
    public long realmGet$createdAt();
    public void realmSet$createdAt(long value);
    public String realmGet$ticketStatus();
    public void realmSet$ticketStatus(String value);
    public String realmGet$createdByName();
    public void realmSet$createdByName(String value);
    public String realmGet$createdByPic();
    public void realmSet$createdByPic(String value);
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> realmGet$assignedEmployee();
    public void realmSet$assignedEmployee(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Employee> value);
}
