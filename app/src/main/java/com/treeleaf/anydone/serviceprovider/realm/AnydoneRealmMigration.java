package com.treeleaf.anydone.serviceprovider.realm;

import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class AnydoneRealmMigration implements RealmMigration {
    public static String REALM_NAME = "default.realm";
    public static int CURRENT_VERSION = 1;

    @Override
    public void migrate(@NonNull DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 1) {
            Objects.requireNonNull(schema.get("Tickets"))
                    .addField("ticketIndex", long.class);
            oldVersion++;
        }

        if (oldVersion == 2) {
            schema.create("DependentTicket")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addField("summary", String.class)
                    .addField("index", long.class)
                    .addField("createdAt", long.class)
                    .addField("serviceId", String.class);
            oldVersion++;
        }

        if (oldVersion == 3) {
            schema.get("Tickets")
                    .addRealmObjectField("dependentTicket", schema.get("DependentTicket"));
            oldVersion++;
        }

        if (oldVersion == 4) {
            schema.create("Attachment")
                    .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("title", String.class)
                    .addField("type", int.class)
                    .addField("url", String.class)
                    .addField("createdAt", long.class)
                    .addField("updatedAt", long.class);
            oldVersion++;
        }

        if (oldVersion == 5) {
            schema.get("Tickets")
                    .addRealmListField("attachmentList", schema.get("Attachment"));
            oldVersion++;
        }

        if (oldVersion == 6) {
            schema.get("KGraph")
                    .addField("prevId", String.class);
            oldVersion++;
        }

        if (oldVersion == 7) {
            schema.get("Conversation").addRealmListField("attachmentRealmList",
                    schema.get("Attachment"));
            oldVersion++;
        }
    }
}
