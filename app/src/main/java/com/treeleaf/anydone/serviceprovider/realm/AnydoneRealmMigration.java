package com.treeleaf.anydone.serviceprovider.realm;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

public class AnydoneRealmMigration implements RealmMigration {
    public static String REALM_NAME = "default.realm";
    public static int CURRENT_VERSION = 1;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }
}
