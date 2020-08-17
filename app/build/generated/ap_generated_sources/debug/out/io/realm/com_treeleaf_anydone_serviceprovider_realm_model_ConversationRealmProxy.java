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
public class com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy extends com.treeleaf.anydone.serviceprovider.realm.model.Conversation
    implements RealmObjectProxy, com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface {

    static final class ConversationColumnInfo extends ColumnInfo {
        long maxColumnIndexValue;
        long clientIdIndex;
        long conversationIdIndex;
        long senderIdIndex;
        long messageIndex;
        long messageTypeIndex;
        long senderTypeIndex;
        long senderImageUrlIndex;
        long messageStatusIndex;
        long imageDescIndex;
        long imageOrientationIndex;
        long sentAtIndex;
        long savedAtIndex;
        long refIdIndex;
        long sentIndex;
        long sendFailIndex;
        long fileNameIndex;
        long fileSizeIndex;
        long filePathIndex;
        long senderNameIndex;
        long imageUriIndex;
        long serviceIconUrlIndex;
        long serviceNameIndex;
        long problemStatIndex;
        long locationIndex;
        long dateIndex;
        long acceptedByIndex;
        long imageBitmapIndex;
        long callInitiateTimeIndex;
        long callDurationIndex;
        long kGraphBackIndex;
        long kGraphTitleIndex;
        long serviceDoerListIndex;
        long kGraphListIndex;
        long receiverListIndex;

        ConversationColumnInfo(OsSchemaInfo schemaInfo) {
            super(34);
            OsObjectSchemaInfo objectSchemaInfo = schemaInfo.getObjectSchemaInfo("Conversation");
            this.clientIdIndex = addColumnDetails("clientId", "clientId", objectSchemaInfo);
            this.conversationIdIndex = addColumnDetails("conversationId", "conversationId", objectSchemaInfo);
            this.senderIdIndex = addColumnDetails("senderId", "senderId", objectSchemaInfo);
            this.messageIndex = addColumnDetails("message", "message", objectSchemaInfo);
            this.messageTypeIndex = addColumnDetails("messageType", "messageType", objectSchemaInfo);
            this.senderTypeIndex = addColumnDetails("senderType", "senderType", objectSchemaInfo);
            this.senderImageUrlIndex = addColumnDetails("senderImageUrl", "senderImageUrl", objectSchemaInfo);
            this.messageStatusIndex = addColumnDetails("messageStatus", "messageStatus", objectSchemaInfo);
            this.imageDescIndex = addColumnDetails("imageDesc", "imageDesc", objectSchemaInfo);
            this.imageOrientationIndex = addColumnDetails("imageOrientation", "imageOrientation", objectSchemaInfo);
            this.sentAtIndex = addColumnDetails("sentAt", "sentAt", objectSchemaInfo);
            this.savedAtIndex = addColumnDetails("savedAt", "savedAt", objectSchemaInfo);
            this.refIdIndex = addColumnDetails("refId", "refId", objectSchemaInfo);
            this.sentIndex = addColumnDetails("sent", "sent", objectSchemaInfo);
            this.sendFailIndex = addColumnDetails("sendFail", "sendFail", objectSchemaInfo);
            this.fileNameIndex = addColumnDetails("fileName", "fileName", objectSchemaInfo);
            this.fileSizeIndex = addColumnDetails("fileSize", "fileSize", objectSchemaInfo);
            this.filePathIndex = addColumnDetails("filePath", "filePath", objectSchemaInfo);
            this.senderNameIndex = addColumnDetails("senderName", "senderName", objectSchemaInfo);
            this.imageUriIndex = addColumnDetails("imageUri", "imageUri", objectSchemaInfo);
            this.serviceIconUrlIndex = addColumnDetails("serviceIconUrl", "serviceIconUrl", objectSchemaInfo);
            this.serviceNameIndex = addColumnDetails("serviceName", "serviceName", objectSchemaInfo);
            this.problemStatIndex = addColumnDetails("problemStat", "problemStat", objectSchemaInfo);
            this.locationIndex = addColumnDetails("location", "location", objectSchemaInfo);
            this.dateIndex = addColumnDetails("date", "date", objectSchemaInfo);
            this.acceptedByIndex = addColumnDetails("acceptedBy", "acceptedBy", objectSchemaInfo);
            this.imageBitmapIndex = addColumnDetails("imageBitmap", "imageBitmap", objectSchemaInfo);
            this.callInitiateTimeIndex = addColumnDetails("callInitiateTime", "callInitiateTime", objectSchemaInfo);
            this.callDurationIndex = addColumnDetails("callDuration", "callDuration", objectSchemaInfo);
            this.kGraphBackIndex = addColumnDetails("kGraphBack", "kGraphBack", objectSchemaInfo);
            this.kGraphTitleIndex = addColumnDetails("kGraphTitle", "kGraphTitle", objectSchemaInfo);
            this.serviceDoerListIndex = addColumnDetails("serviceDoerList", "serviceDoerList", objectSchemaInfo);
            this.kGraphListIndex = addColumnDetails("kGraphList", "kGraphList", objectSchemaInfo);
            this.receiverListIndex = addColumnDetails("receiverList", "receiverList", objectSchemaInfo);
            this.maxColumnIndexValue = objectSchemaInfo.getMaxColumnIndex();
        }

        ConversationColumnInfo(ColumnInfo src, boolean mutable) {
            super(src, mutable);
            copy(src, this);
        }

        @Override
        protected final ColumnInfo copy(boolean mutable) {
            return new ConversationColumnInfo(this, mutable);
        }

        @Override
        protected final void copy(ColumnInfo rawSrc, ColumnInfo rawDst) {
            final ConversationColumnInfo src = (ConversationColumnInfo) rawSrc;
            final ConversationColumnInfo dst = (ConversationColumnInfo) rawDst;
            dst.clientIdIndex = src.clientIdIndex;
            dst.conversationIdIndex = src.conversationIdIndex;
            dst.senderIdIndex = src.senderIdIndex;
            dst.messageIndex = src.messageIndex;
            dst.messageTypeIndex = src.messageTypeIndex;
            dst.senderTypeIndex = src.senderTypeIndex;
            dst.senderImageUrlIndex = src.senderImageUrlIndex;
            dst.messageStatusIndex = src.messageStatusIndex;
            dst.imageDescIndex = src.imageDescIndex;
            dst.imageOrientationIndex = src.imageOrientationIndex;
            dst.sentAtIndex = src.sentAtIndex;
            dst.savedAtIndex = src.savedAtIndex;
            dst.refIdIndex = src.refIdIndex;
            dst.sentIndex = src.sentIndex;
            dst.sendFailIndex = src.sendFailIndex;
            dst.fileNameIndex = src.fileNameIndex;
            dst.fileSizeIndex = src.fileSizeIndex;
            dst.filePathIndex = src.filePathIndex;
            dst.senderNameIndex = src.senderNameIndex;
            dst.imageUriIndex = src.imageUriIndex;
            dst.serviceIconUrlIndex = src.serviceIconUrlIndex;
            dst.serviceNameIndex = src.serviceNameIndex;
            dst.problemStatIndex = src.problemStatIndex;
            dst.locationIndex = src.locationIndex;
            dst.dateIndex = src.dateIndex;
            dst.acceptedByIndex = src.acceptedByIndex;
            dst.imageBitmapIndex = src.imageBitmapIndex;
            dst.callInitiateTimeIndex = src.callInitiateTimeIndex;
            dst.callDurationIndex = src.callDurationIndex;
            dst.kGraphBackIndex = src.kGraphBackIndex;
            dst.kGraphTitleIndex = src.kGraphTitleIndex;
            dst.serviceDoerListIndex = src.serviceDoerListIndex;
            dst.kGraphListIndex = src.kGraphListIndex;
            dst.receiverListIndex = src.receiverListIndex;
            dst.maxColumnIndexValue = src.maxColumnIndexValue;
        }
    }

    private static final OsObjectSchemaInfo expectedObjectSchemaInfo = createExpectedObjectSchemaInfo();

    private ConversationColumnInfo columnInfo;
    private ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Conversation> proxyState;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListRealmList;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListRealmList;
    private RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListRealmList;

    com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy() {
        proxyState.setConstructionFinished();
    }

    @Override
    public void realm$injectObjectContext() {
        if (this.proxyState != null) {
            return;
        }
        final BaseRealm.RealmObjectContext context = BaseRealm.objectContext.get();
        this.columnInfo = (ConversationColumnInfo) context.getColumnInfo();
        this.proxyState = new ProxyState<com.treeleaf.anydone.serviceprovider.realm.model.Conversation>(this);
        proxyState.setRealm$realm(context.getRealm());
        proxyState.setRow$realm(context.getRow());
        proxyState.setAcceptDefaultValue$realm(context.getAcceptDefaultValue());
        proxyState.setExcludeFields$realm(context.getExcludeFields());
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$clientId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.clientIdIndex);
    }

    @Override
    public void realmSet$clientId(String value) {
        if (proxyState.isUnderConstruction()) {
            // default value of the primary key is always ignored.
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        throw new io.realm.exceptions.RealmException("Primary key field 'clientId' cannot be changed after object was created.");
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$conversationId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.conversationIdIndex);
    }

    @Override
    public void realmSet$conversationId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.conversationIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.conversationIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.conversationIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.conversationIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$senderId() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderIdIndex);
    }

    @Override
    public void realmSet$senderId(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderIdIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderIdIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$message() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.messageIndex);
    }

    @Override
    public void realmSet$message(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.messageIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.messageIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.messageIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.messageIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$messageType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.messageTypeIndex);
    }

    @Override
    public void realmSet$messageType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.messageTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.messageTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.messageTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.messageTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$senderType() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderTypeIndex);
    }

    @Override
    public void realmSet$senderType(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderTypeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderTypeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderTypeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderTypeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$senderImageUrl() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderImageUrlIndex);
    }

    @Override
    public void realmSet$senderImageUrl(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderImageUrlIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderImageUrlIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderImageUrlIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderImageUrlIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$messageStatus() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.messageStatusIndex);
    }

    @Override
    public void realmSet$messageStatus(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.messageStatusIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.messageStatusIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.messageStatusIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.messageStatusIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$imageDesc() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.imageDescIndex);
    }

    @Override
    public void realmSet$imageDesc(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.imageDescIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.imageDescIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.imageDescIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.imageDescIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$imageOrientation() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.imageOrientationIndex);
    }

    @Override
    public void realmSet$imageOrientation(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.imageOrientationIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.imageOrientationIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.imageOrientationIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.imageOrientationIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$sentAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.sentAtIndex);
    }

    @Override
    public void realmSet$sentAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.sentAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.sentAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$savedAt() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.savedAtIndex);
    }

    @Override
    public void realmSet$savedAt(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.savedAtIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.savedAtIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public long realmGet$refId() {
        proxyState.getRealm$realm().checkIfValid();
        return (long) proxyState.getRow$realm().getLong(columnInfo.refIdIndex);
    }

    @Override
    public void realmSet$refId(long value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setLong(columnInfo.refIdIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setLong(columnInfo.refIdIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$sent() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.sentIndex);
    }

    @Override
    public void realmSet$sent(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.sentIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.sentIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$sendFail() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.sendFailIndex);
    }

    @Override
    public void realmSet$sendFail(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.sendFailIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.sendFailIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fileName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fileNameIndex);
    }

    @Override
    public void realmSet$fileName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fileNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fileNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fileNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fileNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$fileSize() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.fileSizeIndex);
    }

    @Override
    public void realmSet$fileSize(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.fileSizeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.fileSizeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.fileSizeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.fileSizeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$filePath() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.filePathIndex);
    }

    @Override
    public void realmSet$filePath(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.filePathIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.filePathIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.filePathIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.filePathIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$senderName() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.senderNameIndex);
    }

    @Override
    public void realmSet$senderName(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.senderNameIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.senderNameIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.senderNameIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.senderNameIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$imageUri() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.imageUriIndex);
    }

    @Override
    public void realmSet$imageUri(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.imageUriIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.imageUriIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.imageUriIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.imageUriIndex, value);
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
    public String realmGet$problemStat() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.problemStatIndex);
    }

    @Override
    public void realmSet$problemStat(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.problemStatIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.problemStatIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.problemStatIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.problemStatIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$location() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.locationIndex);
    }

    @Override
    public void realmSet$location(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.locationIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.locationIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.locationIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.locationIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$date() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.dateIndex);
    }

    @Override
    public void realmSet$date(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.dateIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.dateIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.dateIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.dateIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$acceptedBy() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.acceptedByIndex);
    }

    @Override
    public void realmSet$acceptedBy(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.acceptedByIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.acceptedByIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.acceptedByIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.acceptedByIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public byte[] realmGet$imageBitmap() {
        proxyState.getRealm$realm().checkIfValid();
        return (byte[]) proxyState.getRow$realm().getBinaryByteArray(columnInfo.imageBitmapIndex);
    }

    @Override
    public void realmSet$imageBitmap(byte[] value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.imageBitmapIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setBinaryByteArray(columnInfo.imageBitmapIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.imageBitmapIndex);
            return;
        }
        proxyState.getRow$realm().setBinaryByteArray(columnInfo.imageBitmapIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$callInitiateTime() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.callInitiateTimeIndex);
    }

    @Override
    public void realmSet$callInitiateTime(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.callInitiateTimeIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.callInitiateTimeIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.callInitiateTimeIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.callInitiateTimeIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$callDuration() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.callDurationIndex);
    }

    @Override
    public void realmSet$callDuration(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.callDurationIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.callDurationIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.callDurationIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.callDurationIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public boolean realmGet$kGraphBack() {
        proxyState.getRealm$realm().checkIfValid();
        return (boolean) proxyState.getRow$realm().getBoolean(columnInfo.kGraphBackIndex);
    }

    @Override
    public void realmSet$kGraphBack(boolean value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            row.getTable().setBoolean(columnInfo.kGraphBackIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        proxyState.getRow$realm().setBoolean(columnInfo.kGraphBackIndex, value);
    }

    @Override
    @SuppressWarnings("cast")
    public String realmGet$kGraphTitle() {
        proxyState.getRealm$realm().checkIfValid();
        return (java.lang.String) proxyState.getRow$realm().getString(columnInfo.kGraphTitleIndex);
    }

    @Override
    public void realmSet$kGraphTitle(String value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            final Row row = proxyState.getRow$realm();
            if (value == null) {
                row.getTable().setNull(columnInfo.kGraphTitleIndex, row.getIndex(), true);
                return;
            }
            row.getTable().setString(columnInfo.kGraphTitleIndex, row.getIndex(), value, true);
            return;
        }

        proxyState.getRealm$realm().checkIfValid();
        if (value == null) {
            proxyState.getRow$realm().setNull(columnInfo.kGraphTitleIndex);
            return;
        }
        proxyState.getRow$realm().setString(columnInfo.kGraphTitleIndex, value);
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> realmGet$serviceDoerList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (serviceDoerListRealmList != null) {
            return serviceDoerListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceDoerListIndex);
            serviceDoerListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class, osList, proxyState.getRealm$realm());
            return serviceDoerListRealmList;
        }
    }

    @Override
    public void realmSet$serviceDoerList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("serviceDoerList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.serviceDoerListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> realmGet$kGraphList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (kGraphListRealmList != null) {
            return kGraphListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.kGraphListIndex);
            kGraphListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class, osList, proxyState.getRealm$realm());
            return kGraphListRealmList;
        }
    }

    @Override
    public void realmSet$kGraphList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("kGraphList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.KGraph item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.kGraphListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    @Override
    public RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> realmGet$receiverList() {
        proxyState.getRealm$realm().checkIfValid();
        // use the cached value if available
        if (receiverListRealmList != null) {
            return receiverListRealmList;
        } else {
            OsList osList = proxyState.getRow$realm().getModelList(columnInfo.receiverListIndex);
            receiverListRealmList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class, osList, proxyState.getRealm$realm());
            return receiverListRealmList;
        }
    }

    @Override
    public void realmSet$receiverList(RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> value) {
        if (proxyState.isUnderConstruction()) {
            if (!proxyState.getAcceptDefaultValue$realm()) {
                return;
            }
            if (proxyState.getExcludeFields$realm().contains("receiverList")) {
                return;
            }
            // if the list contains unmanaged RealmObjects, convert them to managed.
            if (value != null && !value.isManaged()) {
                final Realm realm = (Realm) proxyState.getRealm$realm();
                final RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> original = value;
                value = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>();
                for (com.treeleaf.anydone.serviceprovider.realm.model.Receiver item : original) {
                    if (item == null || RealmObject.isManaged(item)) {
                        value.add(item);
                    } else {
                        value.add(realm.copyToRealm(item));
                    }
                }
            }
        }

        proxyState.getRealm$realm().checkIfValid();
        OsList osList = proxyState.getRow$realm().getModelList(columnInfo.receiverListIndex);
        // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
        if (value != null && value.size() == osList.size()) {
            int objects = value.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver linkedObject = value.get(i);
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
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver linkedObject = value.get(i);
                proxyState.checkValidObject(linkedObject);
                osList.addRow(((RealmObjectProxy) linkedObject).realmGet$proxyState().getRow$realm().getIndex());
            }
        }
    }

    private static OsObjectSchemaInfo createExpectedObjectSchemaInfo() {
        OsObjectSchemaInfo.Builder builder = new OsObjectSchemaInfo.Builder("Conversation", 34, 0);
        builder.addPersistedProperty("clientId", RealmFieldType.STRING, Property.PRIMARY_KEY, Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("conversationId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderId", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("message", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("messageType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderType", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderImageUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("messageStatus", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("imageDesc", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("imageOrientation", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("sentAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("savedAt", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("refId", RealmFieldType.INTEGER, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("sent", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("sendFail", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("fileName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("fileSize", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("filePath", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("senderName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("imageUri", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceIconUrl", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("serviceName", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("problemStat", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("location", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("date", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("acceptedBy", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("imageBitmap", RealmFieldType.BINARY, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("callInitiateTime", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("callDuration", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedProperty("kGraphBack", RealmFieldType.BOOLEAN, !Property.PRIMARY_KEY, !Property.INDEXED, Property.REQUIRED);
        builder.addPersistedProperty("kGraphTitle", RealmFieldType.STRING, !Property.PRIMARY_KEY, !Property.INDEXED, !Property.REQUIRED);
        builder.addPersistedLinkProperty("serviceDoerList", RealmFieldType.LIST, "ServiceDoer");
        builder.addPersistedLinkProperty("kGraphList", RealmFieldType.LIST, "KGraph");
        builder.addPersistedLinkProperty("receiverList", RealmFieldType.LIST, "Receiver");
        return builder.build();
    }

    public static OsObjectSchemaInfo getExpectedObjectSchemaInfo() {
        return expectedObjectSchemaInfo;
    }

    public static ConversationColumnInfo createColumnInfo(OsSchemaInfo schemaInfo) {
        return new ConversationColumnInfo(schemaInfo);
    }

    public static String getSimpleClassName() {
        return "Conversation";
    }

    public static final class ClassNameHelper {
        public static final String INTERNAL_CLASS_NAME = "Conversation";
    }

    @SuppressWarnings("cast")
    public static com.treeleaf.anydone.serviceprovider.realm.model.Conversation createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        final List<String> excludeFields = new ArrayList<String>(3);
        com.treeleaf.anydone.serviceprovider.realm.model.Conversation obj = null;
        if (update) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
            ConversationColumnInfo columnInfo = (ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
            long pkColumnIndex = columnInfo.clientIdIndex;
            long rowIndex = Table.NO_MATCH;
            if (json.isNull("clientId")) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, json.getString("clientId"));
            }
            if (rowIndex != Table.NO_MATCH) {
                final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class), false, Collections.<String> emptyList());
                    obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy();
                } finally {
                    objectContext.clear();
                }
            }
        }
        if (obj == null) {
            if (json.has("serviceDoerList")) {
                excludeFields.add("serviceDoerList");
            }
            if (json.has("kGraphList")) {
                excludeFields.add("kGraphList");
            }
            if (json.has("receiverList")) {
                excludeFields.add("receiverList");
            }
            if (json.has("clientId")) {
                if (json.isNull("clientId")) {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class, null, true, excludeFields);
                } else {
                    obj = (io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy) realm.createObjectInternal(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class, json.getString("clientId"), true, excludeFields);
                }
            } else {
                throw new IllegalArgumentException("JSON object doesn't have the primary key field 'clientId'.");
            }
        }

        final com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) obj;
        if (json.has("conversationId")) {
            if (json.isNull("conversationId")) {
                objProxy.realmSet$conversationId(null);
            } else {
                objProxy.realmSet$conversationId((String) json.getString("conversationId"));
            }
        }
        if (json.has("senderId")) {
            if (json.isNull("senderId")) {
                objProxy.realmSet$senderId(null);
            } else {
                objProxy.realmSet$senderId((String) json.getString("senderId"));
            }
        }
        if (json.has("message")) {
            if (json.isNull("message")) {
                objProxy.realmSet$message(null);
            } else {
                objProxy.realmSet$message((String) json.getString("message"));
            }
        }
        if (json.has("messageType")) {
            if (json.isNull("messageType")) {
                objProxy.realmSet$messageType(null);
            } else {
                objProxy.realmSet$messageType((String) json.getString("messageType"));
            }
        }
        if (json.has("senderType")) {
            if (json.isNull("senderType")) {
                objProxy.realmSet$senderType(null);
            } else {
                objProxy.realmSet$senderType((String) json.getString("senderType"));
            }
        }
        if (json.has("senderImageUrl")) {
            if (json.isNull("senderImageUrl")) {
                objProxy.realmSet$senderImageUrl(null);
            } else {
                objProxy.realmSet$senderImageUrl((String) json.getString("senderImageUrl"));
            }
        }
        if (json.has("messageStatus")) {
            if (json.isNull("messageStatus")) {
                objProxy.realmSet$messageStatus(null);
            } else {
                objProxy.realmSet$messageStatus((String) json.getString("messageStatus"));
            }
        }
        if (json.has("imageDesc")) {
            if (json.isNull("imageDesc")) {
                objProxy.realmSet$imageDesc(null);
            } else {
                objProxy.realmSet$imageDesc((String) json.getString("imageDesc"));
            }
        }
        if (json.has("imageOrientation")) {
            if (json.isNull("imageOrientation")) {
                objProxy.realmSet$imageOrientation(null);
            } else {
                objProxy.realmSet$imageOrientation((String) json.getString("imageOrientation"));
            }
        }
        if (json.has("sentAt")) {
            if (json.isNull("sentAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'sentAt' to null.");
            } else {
                objProxy.realmSet$sentAt((long) json.getLong("sentAt"));
            }
        }
        if (json.has("savedAt")) {
            if (json.isNull("savedAt")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'savedAt' to null.");
            } else {
                objProxy.realmSet$savedAt((long) json.getLong("savedAt"));
            }
        }
        if (json.has("refId")) {
            if (json.isNull("refId")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'refId' to null.");
            } else {
                objProxy.realmSet$refId((long) json.getLong("refId"));
            }
        }
        if (json.has("sent")) {
            if (json.isNull("sent")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'sent' to null.");
            } else {
                objProxy.realmSet$sent((boolean) json.getBoolean("sent"));
            }
        }
        if (json.has("sendFail")) {
            if (json.isNull("sendFail")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'sendFail' to null.");
            } else {
                objProxy.realmSet$sendFail((boolean) json.getBoolean("sendFail"));
            }
        }
        if (json.has("fileName")) {
            if (json.isNull("fileName")) {
                objProxy.realmSet$fileName(null);
            } else {
                objProxy.realmSet$fileName((String) json.getString("fileName"));
            }
        }
        if (json.has("fileSize")) {
            if (json.isNull("fileSize")) {
                objProxy.realmSet$fileSize(null);
            } else {
                objProxy.realmSet$fileSize((String) json.getString("fileSize"));
            }
        }
        if (json.has("filePath")) {
            if (json.isNull("filePath")) {
                objProxy.realmSet$filePath(null);
            } else {
                objProxy.realmSet$filePath((String) json.getString("filePath"));
            }
        }
        if (json.has("senderName")) {
            if (json.isNull("senderName")) {
                objProxy.realmSet$senderName(null);
            } else {
                objProxy.realmSet$senderName((String) json.getString("senderName"));
            }
        }
        if (json.has("imageUri")) {
            if (json.isNull("imageUri")) {
                objProxy.realmSet$imageUri(null);
            } else {
                objProxy.realmSet$imageUri((String) json.getString("imageUri"));
            }
        }
        if (json.has("serviceIconUrl")) {
            if (json.isNull("serviceIconUrl")) {
                objProxy.realmSet$serviceIconUrl(null);
            } else {
                objProxy.realmSet$serviceIconUrl((String) json.getString("serviceIconUrl"));
            }
        }
        if (json.has("serviceName")) {
            if (json.isNull("serviceName")) {
                objProxy.realmSet$serviceName(null);
            } else {
                objProxy.realmSet$serviceName((String) json.getString("serviceName"));
            }
        }
        if (json.has("problemStat")) {
            if (json.isNull("problemStat")) {
                objProxy.realmSet$problemStat(null);
            } else {
                objProxy.realmSet$problemStat((String) json.getString("problemStat"));
            }
        }
        if (json.has("location")) {
            if (json.isNull("location")) {
                objProxy.realmSet$location(null);
            } else {
                objProxy.realmSet$location((String) json.getString("location"));
            }
        }
        if (json.has("date")) {
            if (json.isNull("date")) {
                objProxy.realmSet$date(null);
            } else {
                objProxy.realmSet$date((String) json.getString("date"));
            }
        }
        if (json.has("acceptedBy")) {
            if (json.isNull("acceptedBy")) {
                objProxy.realmSet$acceptedBy(null);
            } else {
                objProxy.realmSet$acceptedBy((String) json.getString("acceptedBy"));
            }
        }
        if (json.has("imageBitmap")) {
            if (json.isNull("imageBitmap")) {
                objProxy.realmSet$imageBitmap(null);
            } else {
                objProxy.realmSet$imageBitmap(JsonUtils.stringToBytes(json.getString("imageBitmap")));
            }
        }
        if (json.has("callInitiateTime")) {
            if (json.isNull("callInitiateTime")) {
                objProxy.realmSet$callInitiateTime(null);
            } else {
                objProxy.realmSet$callInitiateTime((String) json.getString("callInitiateTime"));
            }
        }
        if (json.has("callDuration")) {
            if (json.isNull("callDuration")) {
                objProxy.realmSet$callDuration(null);
            } else {
                objProxy.realmSet$callDuration((String) json.getString("callDuration"));
            }
        }
        if (json.has("kGraphBack")) {
            if (json.isNull("kGraphBack")) {
                throw new IllegalArgumentException("Trying to set non-nullable field 'kGraphBack' to null.");
            } else {
                objProxy.realmSet$kGraphBack((boolean) json.getBoolean("kGraphBack"));
            }
        }
        if (json.has("kGraphTitle")) {
            if (json.isNull("kGraphTitle")) {
                objProxy.realmSet$kGraphTitle(null);
            } else {
                objProxy.realmSet$kGraphTitle((String) json.getString("kGraphTitle"));
            }
        }
        if (json.has("serviceDoerList")) {
            if (json.isNull("serviceDoerList")) {
                objProxy.realmSet$serviceDoerList(null);
            } else {
                objProxy.realmGet$serviceDoerList().clear();
                JSONArray array = json.getJSONArray("serviceDoerList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$serviceDoerList().add(item);
                }
            }
        }
        if (json.has("kGraphList")) {
            if (json.isNull("kGraphList")) {
                objProxy.realmSet$kGraphList(null);
            } else {
                objProxy.realmGet$kGraphList().clear();
                JSONArray array = json.getJSONArray("kGraphList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.KGraph item = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$kGraphList().add(item);
                }
            }
        }
        if (json.has("receiverList")) {
            if (json.isNull("receiverList")) {
                objProxy.realmSet$receiverList(null);
            } else {
                objProxy.realmGet$receiverList().clear();
                JSONArray array = json.getJSONArray("receiverList");
                for (int i = 0; i < array.length(); i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Receiver item = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createOrUpdateUsingJsonObject(realm, array.getJSONObject(i), update);
                    objProxy.realmGet$receiverList().add(item);
                }
            }
        }
        return obj;
    }

    @SuppressWarnings("cast")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static com.treeleaf.anydone.serviceprovider.realm.model.Conversation createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        boolean jsonHasPrimaryKey = false;
        final com.treeleaf.anydone.serviceprovider.realm.model.Conversation obj = new com.treeleaf.anydone.serviceprovider.realm.model.Conversation();
        final com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface objProxy = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) obj;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (false) {
            } else if (name.equals("clientId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$clientId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$clientId(null);
                }
                jsonHasPrimaryKey = true;
            } else if (name.equals("conversationId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$conversationId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$conversationId(null);
                }
            } else if (name.equals("senderId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderId((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderId(null);
                }
            } else if (name.equals("message")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$message((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$message(null);
                }
            } else if (name.equals("messageType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$messageType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$messageType(null);
                }
            } else if (name.equals("senderType")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderType((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderType(null);
                }
            } else if (name.equals("senderImageUrl")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderImageUrl((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderImageUrl(null);
                }
            } else if (name.equals("messageStatus")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$messageStatus((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$messageStatus(null);
                }
            } else if (name.equals("imageDesc")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$imageDesc((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$imageDesc(null);
                }
            } else if (name.equals("imageOrientation")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$imageOrientation((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$imageOrientation(null);
                }
            } else if (name.equals("sentAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$sentAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'sentAt' to null.");
                }
            } else if (name.equals("savedAt")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$savedAt((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'savedAt' to null.");
                }
            } else if (name.equals("refId")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$refId((long) reader.nextLong());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'refId' to null.");
                }
            } else if (name.equals("sent")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$sent((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'sent' to null.");
                }
            } else if (name.equals("sendFail")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$sendFail((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'sendFail' to null.");
                }
            } else if (name.equals("fileName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fileName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fileName(null);
                }
            } else if (name.equals("fileSize")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$fileSize((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$fileSize(null);
                }
            } else if (name.equals("filePath")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$filePath((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$filePath(null);
                }
            } else if (name.equals("senderName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$senderName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$senderName(null);
                }
            } else if (name.equals("imageUri")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$imageUri((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$imageUri(null);
                }
            } else if (name.equals("serviceIconUrl")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceIconUrl((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceIconUrl(null);
                }
            } else if (name.equals("serviceName")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$serviceName((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$serviceName(null);
                }
            } else if (name.equals("problemStat")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$problemStat((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$problemStat(null);
                }
            } else if (name.equals("location")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$location((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$location(null);
                }
            } else if (name.equals("date")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$date((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$date(null);
                }
            } else if (name.equals("acceptedBy")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$acceptedBy((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$acceptedBy(null);
                }
            } else if (name.equals("imageBitmap")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$imageBitmap(JsonUtils.stringToBytes(reader.nextString()));
                } else {
                    reader.skipValue();
                    objProxy.realmSet$imageBitmap(null);
                }
            } else if (name.equals("callInitiateTime")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$callInitiateTime((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$callInitiateTime(null);
                }
            } else if (name.equals("callDuration")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$callDuration((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$callDuration(null);
                }
            } else if (name.equals("kGraphBack")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$kGraphBack((boolean) reader.nextBoolean());
                } else {
                    reader.skipValue();
                    throw new IllegalArgumentException("Trying to set non-nullable field 'kGraphBack' to null.");
                }
            } else if (name.equals("kGraphTitle")) {
                if (reader.peek() != JsonToken.NULL) {
                    objProxy.realmSet$kGraphTitle((String) reader.nextString());
                } else {
                    reader.skipValue();
                    objProxy.realmSet$kGraphTitle(null);
                }
            } else if (name.equals("serviceDoerList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$serviceDoerList(null);
                } else {
                    objProxy.realmSet$serviceDoerList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$serviceDoerList().add(item);
                    }
                    reader.endArray();
                }
            } else if (name.equals("kGraphList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$kGraphList(null);
                } else {
                    objProxy.realmSet$kGraphList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.KGraph item = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$kGraphList().add(item);
                    }
                    reader.endArray();
                }
            } else if (name.equals("receiverList")) {
                if (reader.peek() == JsonToken.NULL) {
                    reader.skipValue();
                    objProxy.realmSet$receiverList(null);
                } else {
                    objProxy.realmSet$receiverList(new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>());
                    reader.beginArray();
                    while (reader.hasNext()) {
                        com.treeleaf.anydone.serviceprovider.realm.model.Receiver item = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createUsingJsonStream(realm, reader);
                        objProxy.realmGet$receiverList().add(item);
                    }
                    reader.endArray();
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        if (!jsonHasPrimaryKey) {
            throw new IllegalArgumentException("JSON object doesn't have the primary key field 'clientId'.");
        }
        return realm.copyToRealm(obj);
    }

    private static com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy newProxyInstance(BaseRealm realm, Row row) {
        // Ignore default values to avoid creating unexpected objects from RealmModel/RealmList fields
        final BaseRealm.RealmObjectContext objectContext = BaseRealm.objectContext.get();
        objectContext.set(realm, row, realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class), false, Collections.<String>emptyList());
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy obj = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy();
        objectContext.clear();
        return obj;
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Conversation copyOrUpdate(Realm realm, ConversationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Conversation object, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
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
            return (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) cachedRealmObject;
        }

        com.treeleaf.anydone.serviceprovider.realm.model.Conversation realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
            long pkColumnIndex = columnInfo.clientIdIndex;
            String value = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$clientId();
            long rowIndex = Table.NO_MATCH;
            if (value == null) {
                rowIndex = table.findFirstNull(pkColumnIndex);
            } else {
                rowIndex = table.findFirstString(pkColumnIndex, value);
            }
            if (rowIndex == Table.NO_MATCH) {
                canUpdate = false;
            } else {
                try {
                    objectContext.set(realm, table.getUncheckedRow(rowIndex), columnInfo, false, Collections.<String> emptyList());
                    realmObject = new io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy();
                    cache.put(object, (RealmObjectProxy) realmObject);
                } finally {
                    objectContext.clear();
                }
            }
        }

        return (canUpdate) ? update(realm, columnInfo, realmObject, object, cache, flags) : copy(realm, columnInfo, object, update, cache, flags);
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Conversation copy(Realm realm, ConversationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Conversation newObject, boolean update, Map<RealmModel,RealmObjectProxy> cache, Set<ImportFlag> flags) {
        RealmObjectProxy cachedRealmObject = cache.get(newObject);
        if (cachedRealmObject != null) {
            return (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) cachedRealmObject;
        }

        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) newObject;

        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);

        // Add all non-"object reference" fields
        builder.addString(columnInfo.clientIdIndex, realmObjectSource.realmGet$clientId());
        builder.addString(columnInfo.conversationIdIndex, realmObjectSource.realmGet$conversationId());
        builder.addString(columnInfo.senderIdIndex, realmObjectSource.realmGet$senderId());
        builder.addString(columnInfo.messageIndex, realmObjectSource.realmGet$message());
        builder.addString(columnInfo.messageTypeIndex, realmObjectSource.realmGet$messageType());
        builder.addString(columnInfo.senderTypeIndex, realmObjectSource.realmGet$senderType());
        builder.addString(columnInfo.senderImageUrlIndex, realmObjectSource.realmGet$senderImageUrl());
        builder.addString(columnInfo.messageStatusIndex, realmObjectSource.realmGet$messageStatus());
        builder.addString(columnInfo.imageDescIndex, realmObjectSource.realmGet$imageDesc());
        builder.addString(columnInfo.imageOrientationIndex, realmObjectSource.realmGet$imageOrientation());
        builder.addInteger(columnInfo.sentAtIndex, realmObjectSource.realmGet$sentAt());
        builder.addInteger(columnInfo.savedAtIndex, realmObjectSource.realmGet$savedAt());
        builder.addInteger(columnInfo.refIdIndex, realmObjectSource.realmGet$refId());
        builder.addBoolean(columnInfo.sentIndex, realmObjectSource.realmGet$sent());
        builder.addBoolean(columnInfo.sendFailIndex, realmObjectSource.realmGet$sendFail());
        builder.addString(columnInfo.fileNameIndex, realmObjectSource.realmGet$fileName());
        builder.addString(columnInfo.fileSizeIndex, realmObjectSource.realmGet$fileSize());
        builder.addString(columnInfo.filePathIndex, realmObjectSource.realmGet$filePath());
        builder.addString(columnInfo.senderNameIndex, realmObjectSource.realmGet$senderName());
        builder.addString(columnInfo.imageUriIndex, realmObjectSource.realmGet$imageUri());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addString(columnInfo.serviceNameIndex, realmObjectSource.realmGet$serviceName());
        builder.addString(columnInfo.problemStatIndex, realmObjectSource.realmGet$problemStat());
        builder.addString(columnInfo.locationIndex, realmObjectSource.realmGet$location());
        builder.addString(columnInfo.dateIndex, realmObjectSource.realmGet$date());
        builder.addString(columnInfo.acceptedByIndex, realmObjectSource.realmGet$acceptedBy());
        builder.addByteArray(columnInfo.imageBitmapIndex, realmObjectSource.realmGet$imageBitmap());
        builder.addString(columnInfo.callInitiateTimeIndex, realmObjectSource.realmGet$callInitiateTime());
        builder.addString(columnInfo.callDurationIndex, realmObjectSource.realmGet$callDuration());
        builder.addBoolean(columnInfo.kGraphBackIndex, realmObjectSource.realmGet$kGraphBack());
        builder.addString(columnInfo.kGraphTitleIndex, realmObjectSource.realmGet$kGraphTitle());

        // Create the underlying object and cache it before setting any object/objectlist references
        // This will allow us to break any circular dependencies by using the object cache.
        Row row = builder.createNewObject();
        io.realm.com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy realmObjectCopy = newProxyInstance(realm, row);
        cache.put(newObject, realmObjectCopy);

        // Finally add all fields that reference other Realm Objects, either directly or through a list
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = realmObjectSource.realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListRealmList = realmObjectCopy.realmGet$serviceDoerList();
            serviceDoerListRealmList.clear();
            for (int i = 0; i < serviceDoerListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer cacheserviceDoerList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cache.get(serviceDoerListItem);
                if (cacheserviceDoerList != null) {
                    serviceDoerListRealmList.add(cacheserviceDoerList);
                } else {
                    serviceDoerListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), serviceDoerListItem, update, cache, flags));
                }
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = realmObjectSource.realmGet$kGraphList();
        if (kGraphListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListRealmList = realmObjectCopy.realmGet$kGraphList();
            kGraphListRealmList.clear();
            for (int i = 0; i < kGraphListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem = kGraphListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph cachekGraphList = (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cache.get(kGraphListItem);
                if (cachekGraphList != null) {
                    kGraphListRealmList.add(cachekGraphList);
                } else {
                    kGraphListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class), kGraphListItem, update, cache, flags));
                }
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = realmObjectSource.realmGet$receiverList();
        if (receiverListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListRealmList = realmObjectCopy.realmGet$receiverList();
            receiverListRealmList.clear();
            for (int i = 0; i < receiverListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem = receiverListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver cachereceiverList = (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cache.get(receiverListItem);
                if (cachereceiverList != null) {
                    receiverListRealmList.add(cachereceiverList);
                } else {
                    receiverListRealmList.add(com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class), receiverListItem, update, cache, flags));
                }
            }
        }

        return realmObjectCopy;
    }

    public static long insert(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Conversation object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long tableNativePtr = table.getNativePtr();
        ConversationColumnInfo columnInfo = (ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long pkColumnIndex = columnInfo.clientIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$clientId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        } else {
            Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$conversationId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$conversationId();
        if (realmGet$conversationId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, realmGet$conversationId, false);
        }
        String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderId();
        if (realmGet$senderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
        }
        String realmGet$message = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$message();
        if (realmGet$message != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageIndex, rowIndex, realmGet$message, false);
        }
        String realmGet$messageType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageType();
        if (realmGet$messageType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, realmGet$messageType, false);
        }
        String realmGet$senderType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderType();
        if (realmGet$senderType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, realmGet$senderType, false);
        }
        String realmGet$senderImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderImageUrl();
        if (realmGet$senderImageUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, realmGet$senderImageUrl, false);
        }
        String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageStatus();
        if (realmGet$messageStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
        }
        String realmGet$imageDesc = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageDesc();
        if (realmGet$imageDesc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageDescIndex, rowIndex, realmGet$imageDesc, false);
        }
        String realmGet$imageOrientation = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageOrientation();
        if (realmGet$imageOrientation != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, realmGet$imageOrientation, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.sentAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sentAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.savedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$savedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.refIdIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$refId(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.sentIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sent(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.sendFailIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sendFail(), false);
        String realmGet$fileName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileName();
        if (realmGet$fileName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fileNameIndex, rowIndex, realmGet$fileName, false);
        }
        String realmGet$fileSize = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileSize();
        if (realmGet$fileSize != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, realmGet$fileSize, false);
        }
        String realmGet$filePath = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$filePath();
        if (realmGet$filePath != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.filePathIndex, rowIndex, realmGet$filePath, false);
        }
        String realmGet$senderName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderName();
        if (realmGet$senderName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderNameIndex, rowIndex, realmGet$senderName, false);
        }
        String realmGet$imageUri = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageUri();
        if (realmGet$imageUri != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageUriIndex, rowIndex, realmGet$imageUri, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        }
        String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceName();
        if (realmGet$serviceName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
        }
        String realmGet$problemStat = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$problemStat();
        if (realmGet$problemStat != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.problemStatIndex, rowIndex, realmGet$problemStat, false);
        }
        String realmGet$location = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$location();
        if (realmGet$location != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationIndex, rowIndex, realmGet$location, false);
        }
        String realmGet$date = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateIndex, rowIndex, realmGet$date, false);
        }
        String realmGet$acceptedBy = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$acceptedBy();
        if (realmGet$acceptedBy != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, realmGet$acceptedBy, false);
        }
        byte[] realmGet$imageBitmap = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageBitmap();
        if (realmGet$imageBitmap != null) {
            Table.nativeSetByteArray(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, realmGet$imageBitmap, false);
        }
        String realmGet$callInitiateTime = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callInitiateTime();
        if (realmGet$callInitiateTime != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, realmGet$callInitiateTime, false);
        }
        String realmGet$callDuration = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callDuration();
        if (realmGet$callDuration != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.callDurationIndex, rowIndex, realmGet$callDuration, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.kGraphBackIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphBack(), false);
        String realmGet$kGraphTitle = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphTitle();
        if (realmGet$kGraphTitle != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, realmGet$kGraphTitle, false);
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                if (cacheItemIndexserviceDoerList == null) {
                    cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, serviceDoerListItem, cache);
                }
                serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphList();
        if (kGraphListList != null) {
            OsList kGraphListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.kGraphListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem : kGraphListList) {
                Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                if (cacheItemIndexkGraphList == null) {
                    cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insert(realm, kGraphListItem, cache);
                }
                kGraphListOsList.addRow(cacheItemIndexkGraphList);
            }
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$receiverList();
        if (receiverListList != null) {
            OsList receiverListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.receiverListIndex);
            for (com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem : receiverListList) {
                Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                if (cacheItemIndexreceiverList == null) {
                    cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insert(realm, receiverListItem, cache);
                }
                receiverListOsList.addRow(cacheItemIndexreceiverList);
            }
        }
        return rowIndex;
    }

    public static void insert(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long tableNativePtr = table.getNativePtr();
        ConversationColumnInfo columnInfo = (ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long pkColumnIndex = columnInfo.clientIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Conversation object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$clientId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            } else {
                Table.throwDuplicatePrimaryKeyException(primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$conversationId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$conversationId();
            if (realmGet$conversationId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, realmGet$conversationId, false);
            }
            String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderId();
            if (realmGet$senderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
            }
            String realmGet$message = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$message();
            if (realmGet$message != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageIndex, rowIndex, realmGet$message, false);
            }
            String realmGet$messageType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageType();
            if (realmGet$messageType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, realmGet$messageType, false);
            }
            String realmGet$senderType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderType();
            if (realmGet$senderType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, realmGet$senderType, false);
            }
            String realmGet$senderImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderImageUrl();
            if (realmGet$senderImageUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, realmGet$senderImageUrl, false);
            }
            String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageStatus();
            if (realmGet$messageStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
            }
            String realmGet$imageDesc = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageDesc();
            if (realmGet$imageDesc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageDescIndex, rowIndex, realmGet$imageDesc, false);
            }
            String realmGet$imageOrientation = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageOrientation();
            if (realmGet$imageOrientation != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, realmGet$imageOrientation, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.sentAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sentAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.savedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$savedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.refIdIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$refId(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.sentIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sent(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.sendFailIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sendFail(), false);
            String realmGet$fileName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileName();
            if (realmGet$fileName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fileNameIndex, rowIndex, realmGet$fileName, false);
            }
            String realmGet$fileSize = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileSize();
            if (realmGet$fileSize != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, realmGet$fileSize, false);
            }
            String realmGet$filePath = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$filePath();
            if (realmGet$filePath != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.filePathIndex, rowIndex, realmGet$filePath, false);
            }
            String realmGet$senderName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderName();
            if (realmGet$senderName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderNameIndex, rowIndex, realmGet$senderName, false);
            }
            String realmGet$imageUri = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageUri();
            if (realmGet$imageUri != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageUriIndex, rowIndex, realmGet$imageUri, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            }
            String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceName();
            if (realmGet$serviceName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
            }
            String realmGet$problemStat = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$problemStat();
            if (realmGet$problemStat != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.problemStatIndex, rowIndex, realmGet$problemStat, false);
            }
            String realmGet$location = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$location();
            if (realmGet$location != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationIndex, rowIndex, realmGet$location, false);
            }
            String realmGet$date = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateIndex, rowIndex, realmGet$date, false);
            }
            String realmGet$acceptedBy = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$acceptedBy();
            if (realmGet$acceptedBy != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, realmGet$acceptedBy, false);
            }
            byte[] realmGet$imageBitmap = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageBitmap();
            if (realmGet$imageBitmap != null) {
                Table.nativeSetByteArray(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, realmGet$imageBitmap, false);
            }
            String realmGet$callInitiateTime = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callInitiateTime();
            if (realmGet$callInitiateTime != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, realmGet$callInitiateTime, false);
            }
            String realmGet$callDuration = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callDuration();
            if (realmGet$callDuration != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.callDurationIndex, rowIndex, realmGet$callDuration, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.kGraphBackIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphBack(), false);
            String realmGet$kGraphTitle = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphTitle();
            if (realmGet$kGraphTitle != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, realmGet$kGraphTitle, false);
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceDoerList();
            if (serviceDoerListList != null) {
                OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insert(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                }
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphList();
            if (kGraphListList != null) {
                OsList kGraphListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.kGraphListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem : kGraphListList) {
                    Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                    if (cacheItemIndexkGraphList == null) {
                        cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insert(realm, kGraphListItem, cache);
                    }
                    kGraphListOsList.addRow(cacheItemIndexkGraphList);
                }
            }

            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$receiverList();
            if (receiverListList != null) {
                OsList receiverListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.receiverListIndex);
                for (com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem : receiverListList) {
                    Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                    if (cacheItemIndexreceiverList == null) {
                        cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insert(realm, receiverListItem, cache);
                    }
                    receiverListOsList.addRow(cacheItemIndexreceiverList);
                }
            }
        }
    }

    public static long insertOrUpdate(Realm realm, com.treeleaf.anydone.serviceprovider.realm.model.Conversation object, Map<RealmModel,Long> cache) {
        if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
            return ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex();
        }
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long tableNativePtr = table.getNativePtr();
        ConversationColumnInfo columnInfo = (ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long pkColumnIndex = columnInfo.clientIdIndex;
        String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$clientId();
        long rowIndex = Table.NO_MATCH;
        if (primaryKeyValue == null) {
            rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
        } else {
            rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
        }
        if (rowIndex == Table.NO_MATCH) {
            rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
        }
        cache.put(object, rowIndex);
        String realmGet$conversationId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$conversationId();
        if (realmGet$conversationId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, realmGet$conversationId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, false);
        }
        String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderId();
        if (realmGet$senderId != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderIdIndex, rowIndex, false);
        }
        String realmGet$message = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$message();
        if (realmGet$message != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageIndex, rowIndex, realmGet$message, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.messageIndex, rowIndex, false);
        }
        String realmGet$messageType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageType();
        if (realmGet$messageType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, realmGet$messageType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, false);
        }
        String realmGet$senderType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderType();
        if (realmGet$senderType != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, realmGet$senderType, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, false);
        }
        String realmGet$senderImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderImageUrl();
        if (realmGet$senderImageUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, realmGet$senderImageUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, false);
        }
        String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageStatus();
        if (realmGet$messageStatus != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, false);
        }
        String realmGet$imageDesc = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageDesc();
        if (realmGet$imageDesc != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageDescIndex, rowIndex, realmGet$imageDesc, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.imageDescIndex, rowIndex, false);
        }
        String realmGet$imageOrientation = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageOrientation();
        if (realmGet$imageOrientation != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, realmGet$imageOrientation, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, false);
        }
        Table.nativeSetLong(tableNativePtr, columnInfo.sentAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sentAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.savedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$savedAt(), false);
        Table.nativeSetLong(tableNativePtr, columnInfo.refIdIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$refId(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.sentIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sent(), false);
        Table.nativeSetBoolean(tableNativePtr, columnInfo.sendFailIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sendFail(), false);
        String realmGet$fileName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileName();
        if (realmGet$fileName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fileNameIndex, rowIndex, realmGet$fileName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fileNameIndex, rowIndex, false);
        }
        String realmGet$fileSize = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileSize();
        if (realmGet$fileSize != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, realmGet$fileSize, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, false);
        }
        String realmGet$filePath = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$filePath();
        if (realmGet$filePath != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.filePathIndex, rowIndex, realmGet$filePath, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.filePathIndex, rowIndex, false);
        }
        String realmGet$senderName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderName();
        if (realmGet$senderName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.senderNameIndex, rowIndex, realmGet$senderName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.senderNameIndex, rowIndex, false);
        }
        String realmGet$imageUri = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageUri();
        if (realmGet$imageUri != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.imageUriIndex, rowIndex, realmGet$imageUri, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.imageUriIndex, rowIndex, false);
        }
        String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceIconUrl();
        if (realmGet$serviceIconUrl != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
        }
        String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceName();
        if (realmGet$serviceName != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, false);
        }
        String realmGet$problemStat = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$problemStat();
        if (realmGet$problemStat != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.problemStatIndex, rowIndex, realmGet$problemStat, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.problemStatIndex, rowIndex, false);
        }
        String realmGet$location = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$location();
        if (realmGet$location != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.locationIndex, rowIndex, realmGet$location, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.locationIndex, rowIndex, false);
        }
        String realmGet$date = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$date();
        if (realmGet$date != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.dateIndex, rowIndex, realmGet$date, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.dateIndex, rowIndex, false);
        }
        String realmGet$acceptedBy = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$acceptedBy();
        if (realmGet$acceptedBy != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, realmGet$acceptedBy, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, false);
        }
        byte[] realmGet$imageBitmap = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageBitmap();
        if (realmGet$imageBitmap != null) {
            Table.nativeSetByteArray(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, realmGet$imageBitmap, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, false);
        }
        String realmGet$callInitiateTime = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callInitiateTime();
        if (realmGet$callInitiateTime != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, realmGet$callInitiateTime, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, false);
        }
        String realmGet$callDuration = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callDuration();
        if (realmGet$callDuration != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.callDurationIndex, rowIndex, realmGet$callDuration, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.callDurationIndex, rowIndex, false);
        }
        Table.nativeSetBoolean(tableNativePtr, columnInfo.kGraphBackIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphBack(), false);
        String realmGet$kGraphTitle = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphTitle();
        if (realmGet$kGraphTitle != null) {
            Table.nativeSetString(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, realmGet$kGraphTitle, false);
        } else {
            Table.nativeSetNull(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, false);
        }

        OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceDoerList();
        if (serviceDoerListList != null && serviceDoerListList.size() == serviceDoerListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = serviceDoerListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                if (cacheItemIndexserviceDoerList == null) {
                    cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                }
                serviceDoerListOsList.setRow(i, cacheItemIndexserviceDoerList);
            }
        } else {
            serviceDoerListOsList.removeAll();
            if (serviceDoerListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                }
            }
        }


        OsList kGraphListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.kGraphListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphList();
        if (kGraphListList != null && kGraphListList.size() == kGraphListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = kGraphListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem = kGraphListList.get(i);
                Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                if (cacheItemIndexkGraphList == null) {
                    cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, kGraphListItem, cache);
                }
                kGraphListOsList.setRow(i, cacheItemIndexkGraphList);
            }
        } else {
            kGraphListOsList.removeAll();
            if (kGraphListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem : kGraphListList) {
                    Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                    if (cacheItemIndexkGraphList == null) {
                        cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, kGraphListItem, cache);
                    }
                    kGraphListOsList.addRow(cacheItemIndexkGraphList);
                }
            }
        }


        OsList receiverListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.receiverListIndex);
        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$receiverList();
        if (receiverListList != null && receiverListList.size() == receiverListOsList.size()) {
            // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
            int objects = receiverListList.size();
            for (int i = 0; i < objects; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem = receiverListList.get(i);
                Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                if (cacheItemIndexreceiverList == null) {
                    cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, receiverListItem, cache);
                }
                receiverListOsList.setRow(i, cacheItemIndexreceiverList);
            }
        } else {
            receiverListOsList.removeAll();
            if (receiverListList != null) {
                for (com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem : receiverListList) {
                    Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                    if (cacheItemIndexreceiverList == null) {
                        cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, receiverListItem, cache);
                    }
                    receiverListOsList.addRow(cacheItemIndexreceiverList);
                }
            }
        }

        return rowIndex;
    }

    public static void insertOrUpdate(Realm realm, Iterator<? extends RealmModel> objects, Map<RealmModel,Long> cache) {
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long tableNativePtr = table.getNativePtr();
        ConversationColumnInfo columnInfo = (ConversationColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        long pkColumnIndex = columnInfo.clientIdIndex;
        com.treeleaf.anydone.serviceprovider.realm.model.Conversation object = null;
        while (objects.hasNext()) {
            object = (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) objects.next();
            if (cache.containsKey(object)) {
                continue;
            }
            if (object instanceof RealmObjectProxy && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm() != null && ((RealmObjectProxy) object).realmGet$proxyState().getRealm$realm().getPath().equals(realm.getPath())) {
                cache.put(object, ((RealmObjectProxy) object).realmGet$proxyState().getRow$realm().getIndex());
                continue;
            }
            String primaryKeyValue = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$clientId();
            long rowIndex = Table.NO_MATCH;
            if (primaryKeyValue == null) {
                rowIndex = Table.nativeFindFirstNull(tableNativePtr, pkColumnIndex);
            } else {
                rowIndex = Table.nativeFindFirstString(tableNativePtr, pkColumnIndex, primaryKeyValue);
            }
            if (rowIndex == Table.NO_MATCH) {
                rowIndex = OsObject.createRowWithPrimaryKey(table, pkColumnIndex, primaryKeyValue);
            }
            cache.put(object, rowIndex);
            String realmGet$conversationId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$conversationId();
            if (realmGet$conversationId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, realmGet$conversationId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.conversationIdIndex, rowIndex, false);
            }
            String realmGet$senderId = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderId();
            if (realmGet$senderId != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderIdIndex, rowIndex, realmGet$senderId, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderIdIndex, rowIndex, false);
            }
            String realmGet$message = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$message();
            if (realmGet$message != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageIndex, rowIndex, realmGet$message, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.messageIndex, rowIndex, false);
            }
            String realmGet$messageType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageType();
            if (realmGet$messageType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, realmGet$messageType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.messageTypeIndex, rowIndex, false);
            }
            String realmGet$senderType = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderType();
            if (realmGet$senderType != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, realmGet$senderType, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderTypeIndex, rowIndex, false);
            }
            String realmGet$senderImageUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderImageUrl();
            if (realmGet$senderImageUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, realmGet$senderImageUrl, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderImageUrlIndex, rowIndex, false);
            }
            String realmGet$messageStatus = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$messageStatus();
            if (realmGet$messageStatus != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, realmGet$messageStatus, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.messageStatusIndex, rowIndex, false);
            }
            String realmGet$imageDesc = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageDesc();
            if (realmGet$imageDesc != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageDescIndex, rowIndex, realmGet$imageDesc, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.imageDescIndex, rowIndex, false);
            }
            String realmGet$imageOrientation = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageOrientation();
            if (realmGet$imageOrientation != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, realmGet$imageOrientation, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.imageOrientationIndex, rowIndex, false);
            }
            Table.nativeSetLong(tableNativePtr, columnInfo.sentAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sentAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.savedAtIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$savedAt(), false);
            Table.nativeSetLong(tableNativePtr, columnInfo.refIdIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$refId(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.sentIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sent(), false);
            Table.nativeSetBoolean(tableNativePtr, columnInfo.sendFailIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$sendFail(), false);
            String realmGet$fileName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileName();
            if (realmGet$fileName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fileNameIndex, rowIndex, realmGet$fileName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fileNameIndex, rowIndex, false);
            }
            String realmGet$fileSize = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$fileSize();
            if (realmGet$fileSize != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, realmGet$fileSize, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.fileSizeIndex, rowIndex, false);
            }
            String realmGet$filePath = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$filePath();
            if (realmGet$filePath != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.filePathIndex, rowIndex, realmGet$filePath, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.filePathIndex, rowIndex, false);
            }
            String realmGet$senderName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$senderName();
            if (realmGet$senderName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.senderNameIndex, rowIndex, realmGet$senderName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.senderNameIndex, rowIndex, false);
            }
            String realmGet$imageUri = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageUri();
            if (realmGet$imageUri != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.imageUriIndex, rowIndex, realmGet$imageUri, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.imageUriIndex, rowIndex, false);
            }
            String realmGet$serviceIconUrl = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceIconUrl();
            if (realmGet$serviceIconUrl != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, realmGet$serviceIconUrl, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceIconUrlIndex, rowIndex, false);
            }
            String realmGet$serviceName = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceName();
            if (realmGet$serviceName != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, realmGet$serviceName, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.serviceNameIndex, rowIndex, false);
            }
            String realmGet$problemStat = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$problemStat();
            if (realmGet$problemStat != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.problemStatIndex, rowIndex, realmGet$problemStat, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.problemStatIndex, rowIndex, false);
            }
            String realmGet$location = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$location();
            if (realmGet$location != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.locationIndex, rowIndex, realmGet$location, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.locationIndex, rowIndex, false);
            }
            String realmGet$date = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$date();
            if (realmGet$date != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.dateIndex, rowIndex, realmGet$date, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.dateIndex, rowIndex, false);
            }
            String realmGet$acceptedBy = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$acceptedBy();
            if (realmGet$acceptedBy != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, realmGet$acceptedBy, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.acceptedByIndex, rowIndex, false);
            }
            byte[] realmGet$imageBitmap = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$imageBitmap();
            if (realmGet$imageBitmap != null) {
                Table.nativeSetByteArray(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, realmGet$imageBitmap, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.imageBitmapIndex, rowIndex, false);
            }
            String realmGet$callInitiateTime = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callInitiateTime();
            if (realmGet$callInitiateTime != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, realmGet$callInitiateTime, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.callInitiateTimeIndex, rowIndex, false);
            }
            String realmGet$callDuration = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$callDuration();
            if (realmGet$callDuration != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.callDurationIndex, rowIndex, realmGet$callDuration, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.callDurationIndex, rowIndex, false);
            }
            Table.nativeSetBoolean(tableNativePtr, columnInfo.kGraphBackIndex, rowIndex, ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphBack(), false);
            String realmGet$kGraphTitle = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphTitle();
            if (realmGet$kGraphTitle != null) {
                Table.nativeSetString(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, realmGet$kGraphTitle, false);
            } else {
                Table.nativeSetNull(tableNativePtr, columnInfo.kGraphTitleIndex, rowIndex, false);
            }

            OsList serviceDoerListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.serviceDoerListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$serviceDoerList();
            if (serviceDoerListList != null && serviceDoerListList.size() == serviceDoerListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = serviceDoerListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                    Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                    if (cacheItemIndexserviceDoerList == null) {
                        cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                    }
                    serviceDoerListOsList.setRow(i, cacheItemIndexserviceDoerList);
                }
            } else {
                serviceDoerListOsList.removeAll();
                if (serviceDoerListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem : serviceDoerListList) {
                        Long cacheItemIndexserviceDoerList = cache.get(serviceDoerListItem);
                        if (cacheItemIndexserviceDoerList == null) {
                            cacheItemIndexserviceDoerList = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.insertOrUpdate(realm, serviceDoerListItem, cache);
                        }
                        serviceDoerListOsList.addRow(cacheItemIndexserviceDoerList);
                    }
                }
            }


            OsList kGraphListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.kGraphListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$kGraphList();
            if (kGraphListList != null && kGraphListList.size() == kGraphListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = kGraphListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem = kGraphListList.get(i);
                    Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                    if (cacheItemIndexkGraphList == null) {
                        cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, kGraphListItem, cache);
                    }
                    kGraphListOsList.setRow(i, cacheItemIndexkGraphList);
                }
            } else {
                kGraphListOsList.removeAll();
                if (kGraphListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem : kGraphListList) {
                        Long cacheItemIndexkGraphList = cache.get(kGraphListItem);
                        if (cacheItemIndexkGraphList == null) {
                            cacheItemIndexkGraphList = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.insertOrUpdate(realm, kGraphListItem, cache);
                        }
                        kGraphListOsList.addRow(cacheItemIndexkGraphList);
                    }
                }
            }


            OsList receiverListOsList = new OsList(table.getUncheckedRow(rowIndex), columnInfo.receiverListIndex);
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = ((com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) object).realmGet$receiverList();
            if (receiverListList != null && receiverListList.size() == receiverListOsList.size()) {
                // For lists of equal lengths, we need to set each element directly as clearing the receiver list can be wrong if the input and target list are the same.
                int objectCount = receiverListList.size();
                for (int i = 0; i < objectCount; i++) {
                    com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem = receiverListList.get(i);
                    Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                    if (cacheItemIndexreceiverList == null) {
                        cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, receiverListItem, cache);
                    }
                    receiverListOsList.setRow(i, cacheItemIndexreceiverList);
                }
            } else {
                receiverListOsList.removeAll();
                if (receiverListList != null) {
                    for (com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem : receiverListList) {
                        Long cacheItemIndexreceiverList = cache.get(receiverListItem);
                        if (cacheItemIndexreceiverList == null) {
                            cacheItemIndexreceiverList = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.insertOrUpdate(realm, receiverListItem, cache);
                        }
                        receiverListOsList.addRow(cacheItemIndexreceiverList);
                    }
                }
            }

        }
    }

    public static com.treeleaf.anydone.serviceprovider.realm.model.Conversation createDetachedCopy(com.treeleaf.anydone.serviceprovider.realm.model.Conversation realmObject, int currentDepth, int maxDepth, Map<RealmModel, CacheData<RealmModel>> cache) {
        if (currentDepth > maxDepth || realmObject == null) {
            return null;
        }
        CacheData<RealmModel> cachedObject = cache.get(realmObject);
        com.treeleaf.anydone.serviceprovider.realm.model.Conversation unmanagedObject;
        if (cachedObject == null) {
            unmanagedObject = new com.treeleaf.anydone.serviceprovider.realm.model.Conversation();
            cache.put(realmObject, new RealmObjectProxy.CacheData<RealmModel>(currentDepth, unmanagedObject));
        } else {
            // Reuse cached object or recreate it because it was encountered at a lower depth.
            if (currentDepth >= cachedObject.minDepth) {
                return (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) cachedObject.object;
            }
            unmanagedObject = (com.treeleaf.anydone.serviceprovider.realm.model.Conversation) cachedObject.object;
            cachedObject.minDepth = currentDepth;
        }
        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface unmanagedCopy = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) unmanagedObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface realmSource = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) realmObject;
        unmanagedCopy.realmSet$clientId(realmSource.realmGet$clientId());
        unmanagedCopy.realmSet$conversationId(realmSource.realmGet$conversationId());
        unmanagedCopy.realmSet$senderId(realmSource.realmGet$senderId());
        unmanagedCopy.realmSet$message(realmSource.realmGet$message());
        unmanagedCopy.realmSet$messageType(realmSource.realmGet$messageType());
        unmanagedCopy.realmSet$senderType(realmSource.realmGet$senderType());
        unmanagedCopy.realmSet$senderImageUrl(realmSource.realmGet$senderImageUrl());
        unmanagedCopy.realmSet$messageStatus(realmSource.realmGet$messageStatus());
        unmanagedCopy.realmSet$imageDesc(realmSource.realmGet$imageDesc());
        unmanagedCopy.realmSet$imageOrientation(realmSource.realmGet$imageOrientation());
        unmanagedCopy.realmSet$sentAt(realmSource.realmGet$sentAt());
        unmanagedCopy.realmSet$savedAt(realmSource.realmGet$savedAt());
        unmanagedCopy.realmSet$refId(realmSource.realmGet$refId());
        unmanagedCopy.realmSet$sent(realmSource.realmGet$sent());
        unmanagedCopy.realmSet$sendFail(realmSource.realmGet$sendFail());
        unmanagedCopy.realmSet$fileName(realmSource.realmGet$fileName());
        unmanagedCopy.realmSet$fileSize(realmSource.realmGet$fileSize());
        unmanagedCopy.realmSet$filePath(realmSource.realmGet$filePath());
        unmanagedCopy.realmSet$senderName(realmSource.realmGet$senderName());
        unmanagedCopy.realmSet$imageUri(realmSource.realmGet$imageUri());
        unmanagedCopy.realmSet$serviceIconUrl(realmSource.realmGet$serviceIconUrl());
        unmanagedCopy.realmSet$serviceName(realmSource.realmGet$serviceName());
        unmanagedCopy.realmSet$problemStat(realmSource.realmGet$problemStat());
        unmanagedCopy.realmSet$location(realmSource.realmGet$location());
        unmanagedCopy.realmSet$date(realmSource.realmGet$date());
        unmanagedCopy.realmSet$acceptedBy(realmSource.realmGet$acceptedBy());
        unmanagedCopy.realmSet$imageBitmap(realmSource.realmGet$imageBitmap());
        unmanagedCopy.realmSet$callInitiateTime(realmSource.realmGet$callInitiateTime());
        unmanagedCopy.realmSet$callDuration(realmSource.realmGet$callDuration());
        unmanagedCopy.realmSet$kGraphBack(realmSource.realmGet$kGraphBack());
        unmanagedCopy.realmSet$kGraphTitle(realmSource.realmGet$kGraphTitle());

        // Deep copy of serviceDoerList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$serviceDoerList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> managedserviceDoerListList = realmSource.realmGet$serviceDoerList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> unmanagedserviceDoerListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
            unmanagedCopy.realmSet$serviceDoerList(unmanagedserviceDoerListList);
            int nextDepth = currentDepth + 1;
            int size = managedserviceDoerListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer item = com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.createDetachedCopy(managedserviceDoerListList.get(i), nextDepth, maxDepth, cache);
                unmanagedserviceDoerListList.add(item);
            }
        }

        // Deep copy of kGraphList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$kGraphList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> managedkGraphListList = realmSource.realmGet$kGraphList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> unmanagedkGraphListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>();
            unmanagedCopy.realmSet$kGraphList(unmanagedkGraphListList);
            int nextDepth = currentDepth + 1;
            int size = managedkGraphListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph item = com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.createDetachedCopy(managedkGraphListList.get(i), nextDepth, maxDepth, cache);
                unmanagedkGraphListList.add(item);
            }
        }

        // Deep copy of receiverList
        if (currentDepth == maxDepth) {
            unmanagedCopy.realmSet$receiverList(null);
        } else {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> managedreceiverListList = realmSource.realmGet$receiverList();
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> unmanagedreceiverListList = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>();
            unmanagedCopy.realmSet$receiverList(unmanagedreceiverListList);
            int nextDepth = currentDepth + 1;
            int size = managedreceiverListList.size();
            for (int i = 0; i < size; i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver item = com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.createDetachedCopy(managedreceiverListList.get(i), nextDepth, maxDepth, cache);
                unmanagedreceiverListList.add(item);
            }
        }

        return unmanagedObject;
    }

    static com.treeleaf.anydone.serviceprovider.realm.model.Conversation update(Realm realm, ConversationColumnInfo columnInfo, com.treeleaf.anydone.serviceprovider.realm.model.Conversation realmObject, com.treeleaf.anydone.serviceprovider.realm.model.Conversation newObject, Map<RealmModel, RealmObjectProxy> cache, Set<ImportFlag> flags) {
        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface realmObjectTarget = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) realmObject;
        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface realmObjectSource = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxyInterface) newObject;
        Table table = realm.getTable(com.treeleaf.anydone.serviceprovider.realm.model.Conversation.class);
        OsObjectBuilder builder = new OsObjectBuilder(table, columnInfo.maxColumnIndexValue, flags);
        builder.addString(columnInfo.clientIdIndex, realmObjectSource.realmGet$clientId());
        builder.addString(columnInfo.conversationIdIndex, realmObjectSource.realmGet$conversationId());
        builder.addString(columnInfo.senderIdIndex, realmObjectSource.realmGet$senderId());
        builder.addString(columnInfo.messageIndex, realmObjectSource.realmGet$message());
        builder.addString(columnInfo.messageTypeIndex, realmObjectSource.realmGet$messageType());
        builder.addString(columnInfo.senderTypeIndex, realmObjectSource.realmGet$senderType());
        builder.addString(columnInfo.senderImageUrlIndex, realmObjectSource.realmGet$senderImageUrl());
        builder.addString(columnInfo.messageStatusIndex, realmObjectSource.realmGet$messageStatus());
        builder.addString(columnInfo.imageDescIndex, realmObjectSource.realmGet$imageDesc());
        builder.addString(columnInfo.imageOrientationIndex, realmObjectSource.realmGet$imageOrientation());
        builder.addInteger(columnInfo.sentAtIndex, realmObjectSource.realmGet$sentAt());
        builder.addInteger(columnInfo.savedAtIndex, realmObjectSource.realmGet$savedAt());
        builder.addInteger(columnInfo.refIdIndex, realmObjectSource.realmGet$refId());
        builder.addBoolean(columnInfo.sentIndex, realmObjectSource.realmGet$sent());
        builder.addBoolean(columnInfo.sendFailIndex, realmObjectSource.realmGet$sendFail());
        builder.addString(columnInfo.fileNameIndex, realmObjectSource.realmGet$fileName());
        builder.addString(columnInfo.fileSizeIndex, realmObjectSource.realmGet$fileSize());
        builder.addString(columnInfo.filePathIndex, realmObjectSource.realmGet$filePath());
        builder.addString(columnInfo.senderNameIndex, realmObjectSource.realmGet$senderName());
        builder.addString(columnInfo.imageUriIndex, realmObjectSource.realmGet$imageUri());
        builder.addString(columnInfo.serviceIconUrlIndex, realmObjectSource.realmGet$serviceIconUrl());
        builder.addString(columnInfo.serviceNameIndex, realmObjectSource.realmGet$serviceName());
        builder.addString(columnInfo.problemStatIndex, realmObjectSource.realmGet$problemStat());
        builder.addString(columnInfo.locationIndex, realmObjectSource.realmGet$location());
        builder.addString(columnInfo.dateIndex, realmObjectSource.realmGet$date());
        builder.addString(columnInfo.acceptedByIndex, realmObjectSource.realmGet$acceptedBy());
        builder.addByteArray(columnInfo.imageBitmapIndex, realmObjectSource.realmGet$imageBitmap());
        builder.addString(columnInfo.callInitiateTimeIndex, realmObjectSource.realmGet$callInitiateTime());
        builder.addString(columnInfo.callDurationIndex, realmObjectSource.realmGet$callDuration());
        builder.addBoolean(columnInfo.kGraphBackIndex, realmObjectSource.realmGet$kGraphBack());
        builder.addString(columnInfo.kGraphTitleIndex, realmObjectSource.realmGet$kGraphTitle());

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListList = realmObjectSource.realmGet$serviceDoerList();
        if (serviceDoerListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer> serviceDoerListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>();
            for (int i = 0; i < serviceDoerListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer serviceDoerListItem = serviceDoerListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer cacheserviceDoerList = (com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer) cache.get(serviceDoerListItem);
                if (cacheserviceDoerList != null) {
                    serviceDoerListManagedCopy.add(cacheserviceDoerList);
                } else {
                    serviceDoerListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ServiceDoerRealmProxy.ServiceDoerColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer.class), serviceDoerListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.serviceDoerListIndex, serviceDoerListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.serviceDoerListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.ServiceDoer>());
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListList = realmObjectSource.realmGet$kGraphList();
        if (kGraphListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph> kGraphListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>();
            for (int i = 0; i < kGraphListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph kGraphListItem = kGraphListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.KGraph cachekGraphList = (com.treeleaf.anydone.serviceprovider.realm.model.KGraph) cache.get(kGraphListItem);
                if (cachekGraphList != null) {
                    kGraphListManagedCopy.add(cachekGraphList);
                } else {
                    kGraphListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_KGraphRealmProxy.KGraphColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.KGraph.class), kGraphListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.kGraphListIndex, kGraphListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.kGraphListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.KGraph>());
        }

        RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListList = realmObjectSource.realmGet$receiverList();
        if (receiverListList != null) {
            RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver> receiverListManagedCopy = new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>();
            for (int i = 0; i < receiverListList.size(); i++) {
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver receiverListItem = receiverListList.get(i);
                com.treeleaf.anydone.serviceprovider.realm.model.Receiver cachereceiverList = (com.treeleaf.anydone.serviceprovider.realm.model.Receiver) cache.get(receiverListItem);
                if (cachereceiverList != null) {
                    receiverListManagedCopy.add(cachereceiverList);
                } else {
                    receiverListManagedCopy.add(com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.copyOrUpdate(realm, (com_treeleaf_anydone_serviceprovider_realm_model_ReceiverRealmProxy.ReceiverColumnInfo) realm.getSchema().getColumnInfo(com.treeleaf.anydone.serviceprovider.realm.model.Receiver.class), receiverListItem, true, cache, flags));
                }
            }
            builder.addObjectList(columnInfo.receiverListIndex, receiverListManagedCopy);
        } else {
            builder.addObjectList(columnInfo.receiverListIndex, new RealmList<com.treeleaf.anydone.serviceprovider.realm.model.Receiver>());
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
        StringBuilder stringBuilder = new StringBuilder("Conversation = proxy[");
        stringBuilder.append("{clientId:");
        stringBuilder.append(realmGet$clientId() != null ? realmGet$clientId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{conversationId:");
        stringBuilder.append(realmGet$conversationId() != null ? realmGet$conversationId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderId:");
        stringBuilder.append(realmGet$senderId() != null ? realmGet$senderId() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{message:");
        stringBuilder.append(realmGet$message() != null ? realmGet$message() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{messageType:");
        stringBuilder.append(realmGet$messageType() != null ? realmGet$messageType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderType:");
        stringBuilder.append(realmGet$senderType() != null ? realmGet$senderType() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderImageUrl:");
        stringBuilder.append(realmGet$senderImageUrl() != null ? realmGet$senderImageUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{messageStatus:");
        stringBuilder.append(realmGet$messageStatus() != null ? realmGet$messageStatus() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{imageDesc:");
        stringBuilder.append(realmGet$imageDesc() != null ? realmGet$imageDesc() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{imageOrientation:");
        stringBuilder.append(realmGet$imageOrientation() != null ? realmGet$imageOrientation() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sentAt:");
        stringBuilder.append(realmGet$sentAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{savedAt:");
        stringBuilder.append(realmGet$savedAt());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{refId:");
        stringBuilder.append(realmGet$refId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sent:");
        stringBuilder.append(realmGet$sent());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{sendFail:");
        stringBuilder.append(realmGet$sendFail());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fileName:");
        stringBuilder.append(realmGet$fileName() != null ? realmGet$fileName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fileSize:");
        stringBuilder.append(realmGet$fileSize() != null ? realmGet$fileSize() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{filePath:");
        stringBuilder.append(realmGet$filePath() != null ? realmGet$filePath() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{senderName:");
        stringBuilder.append(realmGet$senderName() != null ? realmGet$senderName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{imageUri:");
        stringBuilder.append(realmGet$imageUri() != null ? realmGet$imageUri() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceIconUrl:");
        stringBuilder.append(realmGet$serviceIconUrl() != null ? realmGet$serviceIconUrl() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceName:");
        stringBuilder.append(realmGet$serviceName() != null ? realmGet$serviceName() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{problemStat:");
        stringBuilder.append(realmGet$problemStat() != null ? realmGet$problemStat() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{location:");
        stringBuilder.append(realmGet$location() != null ? realmGet$location() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{date:");
        stringBuilder.append(realmGet$date() != null ? realmGet$date() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{acceptedBy:");
        stringBuilder.append(realmGet$acceptedBy() != null ? realmGet$acceptedBy() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{imageBitmap:");
        stringBuilder.append(realmGet$imageBitmap() != null ? realmGet$imageBitmap() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{callInitiateTime:");
        stringBuilder.append(realmGet$callInitiateTime() != null ? realmGet$callInitiateTime() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{callDuration:");
        stringBuilder.append(realmGet$callDuration() != null ? realmGet$callDuration() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{kGraphBack:");
        stringBuilder.append(realmGet$kGraphBack());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{kGraphTitle:");
        stringBuilder.append(realmGet$kGraphTitle() != null ? realmGet$kGraphTitle() : "null");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{serviceDoerList:");
        stringBuilder.append("RealmList<ServiceDoer>[").append(realmGet$serviceDoerList().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{kGraphList:");
        stringBuilder.append("RealmList<KGraph>[").append(realmGet$kGraphList().size()).append("]");
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{receiverList:");
        stringBuilder.append("RealmList<Receiver>[").append(realmGet$receiverList().size()).append("]");
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
        com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy aConversation = (com_treeleaf_anydone_serviceprovider_realm_model_ConversationRealmProxy)o;

        String path = proxyState.getRealm$realm().getPath();
        String otherPath = aConversation.proxyState.getRealm$realm().getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;

        String tableName = proxyState.getRow$realm().getTable().getName();
        String otherTableName = aConversation.proxyState.getRow$realm().getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (proxyState.getRow$realm().getIndex() != aConversation.proxyState.getRow$realm().getIndex()) return false;

        return true;
    }
}
