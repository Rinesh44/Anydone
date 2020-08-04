package com.treeleaf.anydone.serviceprovider.utils;


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

}
