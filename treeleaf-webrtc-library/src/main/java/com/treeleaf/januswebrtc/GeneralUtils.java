package com.treeleaf.januswebrtc;

import java.util.Random;

public class GeneralUtils {

    public static String generateTransactionString(Integer length) {
        final String str = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        final Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(str.charAt(rnd.nextInt(str.length())));
        }
        return sb.toString();
    }

    public static String appendEndPoint(String baseUrl) {
        StringBuilder builder = new StringBuilder();
        builder.append(baseUrl);
        builder.append("/");
        return builder.toString();
    }

}
