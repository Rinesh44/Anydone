package com.treeleaf.anydone.serviceprovider.utils;


import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
/*
    public static void main(String[] args) {

        DateUtils obj = new DateUtils();
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat("dd/M/yyyy hh:mm:ss");

        try {

            Date date1 = simpleDateFormat.parse("10/10/2013 11:30:10");
            Date date2 = simpleDateFormat.parse("13/10/2013 20:35:55");

            obj.printDifference(date1, date2);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }*/

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public String printDifference(long startDate, long endDate) {

        //milliseconds
        long different = endDate - startDate;

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days %d hours %d minutes %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        StringBuilder elapsedTime = new StringBuilder();
        if (elapsedDays != 0) {
            elapsedTime.append(elapsedDays);
            if (elapsedDays > 1)
                elapsedTime.append(" days ");
            else
                elapsedTime.append(" day ");
        }
        if (elapsedHours != 0) {
            elapsedTime.append(elapsedHours);
            if (elapsedHours > 1)
                elapsedTime.append(" hrs ");
            else
                elapsedTime.append(" hr ");
        }
        if (elapsedMinutes != 0) {
            elapsedTime.append(elapsedMinutes);
            if (elapsedMinutes > 1)
                elapsedTime.append(" mins");
            else
                elapsedTime.append(" min");
        }
//        elapsedTime.append(elapsedSeconds);
        return elapsedTime.toString();

    }

    public static String getElapsedTime(long time) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = time / daysInMilli;
        time = time % daysInMilli;

        long elapsedHours = time / hoursInMilli;
        time = time % hoursInMilli;

        long elapsedMinutes = time / minutesInMilli;
        time = time % minutesInMilli;

        long elapsedSeconds = time / secondsInMilli;

        System.out.printf(
                "%d days %d hours %d minutes %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        StringBuilder elapsedTime = new StringBuilder();
        if (elapsedDays != 0) {
            elapsedTime.append(elapsedDays);
            if (elapsedDays > 1)
                elapsedTime.append(" days ");
            else
                elapsedTime.append(" day ");

            return elapsedTime.toString();
        }

        if (elapsedHours != 0) {
            elapsedTime.append(elapsedHours);
            if (elapsedHours > 1)
                elapsedTime.append(" hrs ");
            else
                elapsedTime.append(" hr ");

            return elapsedTime.toString();
        }
        if (elapsedMinutes != 0) {
            elapsedTime.append(elapsedMinutes);
            if (elapsedMinutes > 1)
                elapsedTime.append(" mins ");
            else
                elapsedTime.append(" min ");
            return elapsedTime.toString();
        }

        if (elapsedTime.toString().isEmpty()) {
            if (elapsedSeconds != 0) {
                elapsedTime.append(elapsedSeconds);
                if (elapsedSeconds > 1)
                    elapsedTime.append(" secs");
                else
                    elapsedTime.append(" sec");
                return elapsedTime.toString();
            }
        }

        return elapsedTime.toString();
    }

    public static long getStartOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDay() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTimeInMillis();
    }

    public static long getStartOfDayYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDayYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTimeInMillis();
    }


    public static long getStartOfDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 0, 0, 0);
        return calendar.getTimeInMillis();
    }

    public static long getEndOfDay(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        calendar.set(year, month, day, 23, 59, 59);
        return calendar.getTimeInMillis();
    }

    public static Date getDateLocal(long time) {
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();//get your local time zone.
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        sdf.setTimeZone(tz);//set time zone.
        String localTime = sdf.format(new Date(time * 1000));
        Date date = new Date();
        try {
            date = sdf.parse(localTime);//get local date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
