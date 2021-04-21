package com.treeleaf.januswebrtc;

import java.math.BigInteger;

public class Const {
    public static final String CLIENT = "client";
    public static final String SERVER = "server";
    //    public static final String BASE_URL = "https://mediaserver.anydone.net/janus/";
    public static final String BASE_URL = "https://mediaserver-a.anydone.com/janus/";//TODO: for prod
    //    public static String API_SECRET = "anydone@321123!@#";
    public static String API_SECRET = "A$#@2hsggsJHS0123GSA";//TODO: for prod
    public static BigInteger ROOM_NUMBER;

    public static final String JANUS_URL = "janus_base_url";
    public static final String JANUS_API_KEY = "janus_api_key";
    public static final String JANUS_API_SECRET = "janus_api_secret";
    public static final String CALLEE_NAME = "callee_name";
    public static final String CALLEE_PROFILE_URL = "callee_profile_url";
    public static final String CALLER_PROFILE_URL = "caller_profile_url";
    public static final String CALLER_ACCOUNT_ID = "callee_account_id";

    public static final String LOCAL_JOINEE_NAME = "local_joinee_name";
    public static final String LOCAL_JOINEE_PROFILE_URL = "local_joinee_profile_url";
    public static final String LOCAL_JOINEE_ACCOUNT_ID = "local_joinee_account_id";

    public static final String JANUS_CREDENTIALS_SET = "janus_credentials_set";
    public static final String JANUS_ROOM_NUMBER = "janus_room_number";
    public static final String JANUS_PARTICIPANT_ID = "janus_participant_id";
    public static final String KEY_RUNNING_ON = "KEY_RUNNING_ON";
    public static final String KEY_MULTIPLE_CALL = "KEY_MULTIPLE_CALL";
    public static final String KEY_DIRECT_CALL_ACCEPT = "KEY_DIRECT_CALL_ACCEPT";
    public static final String KEY_LAUNCHED_FROM_NOTIFICATION = "KEY_LAUNCHED_FROM_NOTIFICATION";

    public static final String NOTIFICATION_BRODCAST_CALL = "notification_broadcast_call";
    public static final String NOTIFICATION_RTC_MESSAGE_ID = "notification_rtc_message_id";
    public static final String NOTIFICATION_BASE_URL = "notification_base_url";
    public static final String NOTIFICATION_API_KEY = "notification_api_key";
    public static final String NOTIFICATION_API_SECRET = "notification_api_secret";
    public static final String NOTIFICATION_ROOM_ID = "notification_room_id";
    public static final String NOTIFICATION_PARTICIPANT_ID = "notification_participant_id";
    public static final String NOTIFICATION_CALLER_NAME = "notification_caller_name";
    public static final String NOTIFICATION_CALLER_ACCOUNT_ID = "notification_caller_account_id";
    public static final String NOTIFICATION_CALLER_PROFILE_URL = "notification_caller_profile_url";
    public static final String NOTIFICATION_CALLER_ACCOUNT_TYPE = "notification_caller_account_type";
    public static final String NOTIFICATION_DIRECT_CALL_ACCEPT = "notification_direct_call_accept";
    public static final String NOTIFICATION_TOKEN = "notificationToken";
    public static final String NOTIFICATION_NUMBER_OF_PARTICIPANTS = "number_of_participants";
    public static final String NOTIFICATION_REFERENCE_ID = "refId";
    public static final String NOTIFICATION_CALLER_CONTEXT = "notification_caller_context";
    public static final String NOTIFICATION_LOCAL_ACCOUNT_ID = "notification_local_account_id";


    public static final String NOTIFICATION_HOST_ACCOUNT_ID = "notification_host_account_id";
    public static final String NOTIFICATION_CALL_DURATION = "notification_call_duration";
    public static final String NOTIFICATION_STARTED_AT = "notification_started_at";
    public static final String NOTIFICATION_HOST_CLIENT_ID = "notification_host_client_id";
    public static final String NOTIFICATION_HOST_REF_ID = "notification_host_ref_id";


    public static final String JOINEE_REMOTE = "joinee_remote";
    public static final String JOINEE_LOCAL = "joinee_local";
    public static final String MQTT_CONNECTED = "MQTT_CONNECTED";
    public static final String MQTT_DISCONNECTED = "MQTT_DISCONNECTED";

    public static final String CONSUMER_TYPE = "CONSUMER_TYPE";
    public static final String SERVICE_PROVIDER_TYPE = "SERVICE_PROVIDER_TYPE";

    public static final String LOCAL_LOG = "LOCAL_LOG";
    public static final String RMEOTE_LOG = "REMOTE_LOG";

    public static final Integer PICTURE_COUNT_MAX = 4;
    public static final String PICTURE_EXCEED_MSG = "Image draw is limited to 5!!!";

//    public static boolean isCallTakingPlace = false;

    public static void setRoomNumber(BigInteger roomNumber) {
        ROOM_NUMBER = roomNumber;
    }

    public static BigInteger getRoomNumber() {
        return ROOM_NUMBER;
    }

    public static class CallStatus {
        public static boolean isCallingScreenOn = false;
        public static boolean isCallTakingPlace = false;
    }

}
