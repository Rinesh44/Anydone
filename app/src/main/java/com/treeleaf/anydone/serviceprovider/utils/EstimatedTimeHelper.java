package com.treeleaf.anydone.serviceprovider.utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EstimatedTimeHelper {
    private static final String WEEKS = "\\d+weeks";
    private static final String WEEK = "\\d+week";
    private static final String WW = "\\d+w";
    private static final String DAYS = "\\d+days";
    private static final String DAY = "\\d+day";
    private static final String DD = "\\d+d";
    private static final String HOURS = "\\d+hours";
    private static final String HOUR = "\\d+hour";
    private static final String HH = "\\d+h";
    private static final String MINUTES = "\\d+minutes";
    private static final String MINUTE = "\\d+minute";
    private static final String MIN = "\\d+min";
    private static final String MM = "\\d+m";
    private static final String SECONDS = "\\d+seconds";
    private static final String SECOND = "\\d+second";
    private static final String SEC = "\\d+sec";
    private static final String SS = "\\d+s";
    private static final Pattern PATTERN = Pattern.compile("\\d+\\s*\\w+");
    private static final String ALPHABETIC_CHARS = "[A-Za-z]*";

    private EstimatedTimeHelper() {
    }

    public static long getEstimatedTime(long startedTime, String estimatedTimeDesc) {
        estimatedTimeDesc = estimatedTimeDesc.toLowerCase();
        long estimatedTime = getEstimatedTime(estimatedTimeDesc, startedTime);
        if (estimatedTime == startedTime) {
            return 0;
        }
        return estimatedTime;
    }

    public static boolean validateEstimatedTime(String estimatedTimeDesc) {
        if (estimatedTimeDesc.isEmpty()) {
            return true;
        }

        estimatedTimeDesc = estimatedTimeDesc.trim().toLowerCase();
        long estimatedTime = getEstimatedTime(estimatedTimeDesc, 0);
        return estimatedTime != 0;
    }

    private static long getEstimatedTime(String estimatedTimeDesc, long startTime) {
        long estimatedTime = startTime;
        Matcher matcher = PATTERN.matcher(estimatedTimeDesc);
        while (matcher.find()) {
            String group = Objects.requireNonNull(matcher.group(0))
                    .replaceAll("\\s*", "")
                    .toLowerCase();
            if (group.matches(WEEKS) || group.matches(WEEK) || group.matches(WW)) {
                String s = group.replaceAll(ALPHABETIC_CHARS, "");
                long weeks = Long.parseLong(s);
                estimatedTime = addWeeks(estimatedTime, weeks);
            } else if (group.matches(DAYS) || group.matches(DAY) || group.matches(DD)) {
                String s = group.replaceAll(ALPHABETIC_CHARS, "");
                long days = Long.parseLong(s);
                estimatedTime = addDays(estimatedTime, days);
            } else if (group.matches(HOURS) || group.matches(HOUR) || group.matches(HH)) {
                String s = group.replaceAll(ALPHABETIC_CHARS, "");
                long hours = Long.parseLong(s);
                estimatedTime = addHours(estimatedTime, hours);
            } else if (group.matches(MINUTES) || group.matches(MINUTE) || group.matches(MIN) || group.matches(MM)) {
                String s = group.replaceAll(ALPHABETIC_CHARS, "");
                long minutes = Long.parseLong(s);
                estimatedTime = addMinutes(estimatedTime, minutes);
            } else if (group.matches(SECONDS) || group.matches(SECOND) || group.matches(SEC) || group.matches(SS)) {
                String s = group.replaceAll(ALPHABETIC_CHARS, "");
                long seconds = Long.parseLong(s);
                estimatedTime = addSeconds(estimatedTime, seconds);
            } else {
                return estimatedTime;
            }
        }
        return estimatedTime;
    }


    public static long addSeconds(long timestamp, long seconds) {
        return timestamp + seconds * 1000L;
    }

    public static long addMinutes(long timestamp, long minutes) {
        return timestamp + minutes * 60L * 1000L;
    }

    public static long addHours(long timestamp, long hours) {
        return timestamp + hours * 60L * 60L * 1000L;
    }

    public static long addDays(long timestamp, long days) {
        return timestamp + days * 24L * 60L * 60L * 1000L;
    }

    public static long addWeeks(long timestamp, long weeks) {
        return timestamp + weeks * 7L * 24L * 60L * 60L * 1000L;
    }
}
