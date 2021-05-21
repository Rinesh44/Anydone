package com.anydone.desk.notification;

import com.anydone.desk.utils.GlobalUtils;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;

public class MessagingServiceTest extends TestCase {
    private static final String TAG = "MessagingServiceTest";

    public void testOnMessageReceived() throws JSONException {
        String data = "{type=TICKET_STATUS_UPDATED_TYPE, timestamp=1608120439000, state=TICKET_CLOSED, notificationId=6c7d774a26a942fdba9fda3f9b50a9e6, ticketId=67, isInApp=false}";
        GlobalUtils.showLog(TAG, "print: " + data.charAt(94));
        JSONObject jsonObject = new JSONObject(data);

    }
}