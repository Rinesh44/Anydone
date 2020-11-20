package com.treeleaf.anydone.serviceprovider.realm;

import io.reactivex.annotations.NonNull;
import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class AnydoneRealmMigration implements RealmMigration {
    public static String REALM_NAME = "default.realm";
    public static int CURRENT_VERSION = 1;

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

    /*    if (oldVersion == 1) {
            schema.get("TicketSuggestion")
                    .addField("dummy", String.class);
            oldVersion++;
        }*/
    }
}
