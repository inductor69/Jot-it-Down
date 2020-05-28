package com.freejob.alert2020.jotted.util;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JournalDate {

    private final static String DATE_PATTERN = "MMMM d, yyyy";
    private final static String DATA_STRING_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date currentDate() {
        return Calendar.getInstance().getTime();
    }

    public static String welcomeString(Date date) {
        String ret;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 4 && hour < 12) {    // morning: 4:00 am - 11:59 am
            ret = "Good morning! ";
        } else if (hour >= 12 && hour < 18) {    // afternoon: 12 pm - 5:59 pm
            ret = "Good afternoon! ";
        } else {    // evening: 6 pm - 3:59 am
            ret = "Good evening! ";
        }
        return ret;
    }

    public static String formatDateToString(Date date) {
        return new SimpleDateFormat(DATE_PATTERN, Locale.CANADA).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static String formatDateToDataString(Date date) {
        return new SimpleDateFormat(DATA_STRING_PATTERN).format(date);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date dataStringToDate(String ds) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_STRING_PATTERN);
        try {
            return sdf.parse(ds);
        } catch (ParseException e) {
            Log.e("error", "Error parsing date string: ", e);
        }
        return currentDate();
    }

}
