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

        if (oldVersion == 8) {
            schema.get("KGraph")
                    .addField("backId", String.class)
                    .addField("backKey", String.class);
            oldVersion++;
        }


        if (oldVersion == 9) {
            schema.create("Participant")
                    .addField("participantId", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("role", String.class)
                    .addField("accountType", String.class)
                    .addRealmObjectField("employee", schema.get("AssignEmployee"));

            schema.create("Inbox")
                    .addField("inboxId", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("serviceId", String.class)
                    .addField("subject", String.class)
                    .addField("createdByUserAccountId", String.class)
                    .addField("createdByUserEmail", String.class)
                    .addField("createdByUserPhone", String.class)
                    .addField("createdByUserAccountType", String.class)
                    .addField("createdByUserProfilePic", String.class)
                    .addField("createdByUserFullName", String.class)
                    .addField("createdByAccountType", String.class)
                    .addField("createdAt", long.class)
                    .addField("updatedAt", long.class)
                    .addField("lastMsg", String.class)
                    .addField("lastMsgDate", long.class)
                    .addField("notificationType", String.class)
                    .addField("seen", boolean.class)
                    .addRealmListField("participantList", schema.get("Participant"));
            oldVersion++;
        }

        if (oldVersion == 10) {
            schema.get("Inbox")
                    .addField("lastMsgSender", String.class);
            oldVersion++;
        }

        if (oldVersion == 11) {
            schema.get("Participant")
                    .addField("notificationType", String.class);
            oldVersion++;
        }

        if (oldVersion == 12) {
            schema.get("Customer")
                    .addField("filtered", boolean.class);
            oldVersion++;
        }

        if (oldVersion == 13) {
            schema.get("Participant")
                    .addField("inboxId", String.class);
            oldVersion++;
        }

        if (oldVersion == 14) {
            schema.create("Reply")
                    .addField("parentId", String.class, FieldAttribute.PRIMARY_KEY)
                    .addRealmListField("replyList", schema.get("Conversation"));

            schema.get("Conversation")
                    .addField("isReply", boolean.class)
                    .addField("replyCount", int.class);

            oldVersion++;
        }

        if (oldVersion == 15) {
            realm.delete("Reply");

            schema.get("Conversation")
                    .addField("parentId", String.class);

            oldVersion++;
        }
    }
}
