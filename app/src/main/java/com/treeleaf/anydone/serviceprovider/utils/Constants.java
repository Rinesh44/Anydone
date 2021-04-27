package com.treeleaf.anydone.serviceprovider.utils;

public class
Constants {
    public static final String TOKEN = "token";
    public static final String LOGGED_IN = "logged_in";
    public static final String IS_PHONE = "is_phone";
    public static final String COUNTRY_CODE = "country_code";
    public static final String REGISTERED = "registered";
    public static final String USER_VERIFIED = "user_verified";
    public static final String EMAIL_PHONE = "email_phone";
    public static final String SELECTED_LANGUAGE = "selected_language";
    public static final String VISA = "^4[0-9]{6,}$";
    public static final String MASTERCARD = "^5[1-5][0-9]{5,}$";
    public static final String AMERICAN_EXPRESS = "^3[47][0-9]{5,}$";
    public static final String DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
    public static final String DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
    public static final String JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
    public static final String MAP_BOX_TOKEN = "sk.eyJ1IjoicmluZXNoNDQiLCJhIjoiY2s5OW5oZml0MDA0cjNmcGI5c2ZkZmR0MyJ9.UxQJINlJAffP5HFwhyKvVQ";
    public static final String AUTHORIZATION_FAILED = "authorization failed.";
    public static final String WILL_MESSAGE_TOPIC = "will_msg_topic";
    public static final String MQTT_URI = "ssl://mqtt.anydone.net:8883";
    public static final String MQTT_URI_PROD = "ssl://emqx.anydone.com:8883";
    public static final String MQTT_SSL_KEY_PASSWORD = "mqtt.ssl.key.password";
    public static final String MQTT_USER_PROD = "mqtt_android";
    public static final String MQTT_USER = "admin";
    public static final String MQTT_PASSWORD = "aW123k@1234";
    public static final String MQTT_PASSWORD_PROD = "mqtt_androidkopassword@321!@#";
    public static final int MAX_INFLIGHT = 1000000;
    public static final String TLS_VERSION = "TLSv1.2";
    public static final String SERVICE_PROVIDER_NAME = "service_provider_name";
    public static final String SERVICE_PROVIDER_IMG = "service_provider_img";
    public static final String SERVICE_PROVIDER = "SERVICE_PROVIDER";
    public static final String CURRENT_SERVICE_ORDER_ID = "current_service_order_id";
    public static final String CURRENCY = "currency";
    public static final String CURRENCY_NAME = "currency_name";
    public static final String CURRENCY_FLAG = "currency_flag";
    public static final String ZONE = "zone";
    public static final String TIMEZONE = "timezone";
    public static final String TIMEZONE_ID = "timezone_id";
    public static final String TIMEZONE_GMT = "timezone_gmt";
    public static final String ANYDONE_BOT = "Anydone Bot";
    public static final String KGRAPH_TITLE = "kgraph_title";
    public static final String SELECTED_FILTER_STATUS = "selected_filter_status";
    public static final String SELECTED_TICKET_FILTER_STATUS = "selected_ticket_filter_status";
    public static final String FETCH_SUBSCRIBEABLE_LIST = "fetch_subscribeable_list";
    public static final String FETCH_SUBSCRIBED_LIST = "fetch_subscribed_list";
    public static final String FETCH_PENDING_LIST = "fetch_pending_list";
    public static final String FETCH_IN_PROGRESS_LIST = "fetch_in_progress_list";
    public static final String FETCH_CONTRIBUTED_LIST = "fetch_contributed_list";
    public static final String FETCH_CLOSED_LIST = "fetch_closed_list";
    public static final String TICKET_STARTED = "ticket_started";
    public static final String REFETCH_TICKET = "refetch_ticket";
    public static final String SELECTED_SERVICE = "selected_service";
    public static final String SERVICE_CHANGED_THREAD = "service_changed_thread";
    public static final String SERVICE_CHANGED_TICKET = "service_changed_tickets";
    public static final String SERVICE_CHANGED_DASHBOARD = "service_changed_dashboard";
    public static final String SERVICE_CHANGED_INBOX = "service_changed_inbox";
    public static final String PENDING = "PENDING";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String SUBSCRIBED = "SUBSCRIBED";
    public static final String CLOSED_RESOLVED = "CLOSED_RESOLVED";
    public static final String CONTRIBUTED = "CONTRIBUTED";
    public static final String ASSIGNABLE = "ASSIGNABLE";
    public static final String SUBSCRIBEABLE = "SUBSCRIBEABLE";
    public static final String ALL = "ALL";
    public static final String OPEN = "OPEN";
    public static final String OWNED = "OWNED";
    public static final String LINKED = "LINKED";
    public static final String CUSTOMER = "CUSTOMER";
    public static final String BOT_REPLY = "BOT_REPLY";
    public static final String RTC_CONTEXT_SERVICE_REQUEST = "RTC_CONTEXT_SERVICE_REQUEST";
    public static final String RTC_CONTEXT_TICKET = "RTC_CONTEXT_TICKET";
    public static final String RTC_CONTEXT_INBOX = "RTC_CONTEXT_INBOX";
    public static final String TICKET_STAT_STATUS = "TICKET_STAT_STATUS";
    public static final String TICKET_STAT_SOURCE = "TICKET_STAT_SOURCE";
    public static final String TICKET_STAT_PRIORITY = "TICKET_STAT_PRIORITY";
    public static final String TICKET_STAT_DATE = "TICKET_STAT_DATE";
    public static final String TICKET_STAT_RESOLVED_TIME = "TICKET_STAT_RESOLVED_TIME";
    public static final String REFETCH_TICKET_STAT = "REFETCH_TICKET_STAT";
    public static final String XA_XIS_TYPE = "X_AXIS_TYPE";
    public static final String TICKET_ASSIGNED = "TICKET_ASSIGNED";
    public static final String TICKET_CONTRIBUTED = "TICKET_CONTRIBUTED";
    public static final String TICKET_SUBSCRIBED = "TICKET_SUBSCRIBED";
    public static final String TICKET_SERVICE_ID = "TICKET_SERVICE_ID";
    public static final String TICKET_PENDING = "TICKET_PENDING";
    public static final String TICKET_IN_PROGRESS = "TICKET_IN_PROGRESS";
    public static final String TICKET_RESOLVED = "TICKET_RESOLVED";
    public static final String TICKET_OPEN = "TICKET_OPEN";
    public static final String TICKET_OWNED = "TICKET_OWNED";
    public static final String DASHBOARD_SERVICE_ID = "DASHBOARD_SERVICE_ID";
    public static final String CONVERSATION_SERVICE_ID = "CONVERSATION_SERVICE_ID";
    public static final String MANUAL_DATE = "MANUAL_DATE";
    public static final String BASE_URL = "BASE_URL";
    public static final String PROD_BASE_URL = "https://api.anydone.com/";
    public static final String DEV_BASE_URL = "https://api.anydone.net/";
    public static final String MQTT_CONNECTED = "MQTT_CONNECTED";
    public static final String SERVER_ERROR = "Error connecting to server";
    public static final String MQTT_LOG = "MQTT_LOG";
    public static final String SUGGESTION_ACCEPTED = "SUGGESTION_ACCEPTED";
    public static final String SUGGESTION_REJECTED = "SUGGESTION_REJECTED";

    public static final String NOTIFICATION_LOCAL_ACCOUNT_ID = "notification_local_account_id";
    public static final String NOTIFICATION_CLIENT_ID = "NOTIFICATION_CLIENT_ID";

    public interface ACTION {
        String START_FOREGROUND_ACTION = "com.treeleaf.anydone.constants.action.startforeground";
        String STOP_FOREGROUND_ACTION = "com.treeleaf.anydone.constants.action.stopforeground";
    }

    public interface CHANNEL {
        String NC_ANYDONE_CALL = "nc_anydone_call";
    }

}
