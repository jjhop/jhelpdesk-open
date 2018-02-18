package com.buzzlers.jhelpdesk.utils;

import java.util.Date;

public class DateUtil {

    private static long MILLIS_IN_WEEK = 60480000;

    public static boolean isOlderThankWeek(Date date) {
        long millisFromDate = System.currentTimeMillis() - date.getTime();
        return millisFromDate > MILLIS_IN_WEEK;
    }
}
