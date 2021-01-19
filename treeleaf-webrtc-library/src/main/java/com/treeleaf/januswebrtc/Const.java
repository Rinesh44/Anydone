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
    public static final String JANUS_CREDENTIALS_SET = "janus_credentials_set";
    public static final String JANUS_ROOM_NUMBER = "janus_room_number";
    public static final String JANUS_PARTICIPANT_ID = "janus_participant_id";
    public static final String KEY_RUNNING_ON = "KEY_RUNNING_ON";

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

    public static void setRoomNumber(BigInteger roomNumber) {
        ROOM_NUMBER = roomNumber;
    }

    public static BigInteger getRoomNumber() {
        return ROOM_NUMBER;
    }
}
