package com.urent.server.toy;

import java.util.Calendar;

/**
 * Created by Hey on 2015/11/23.
 */

/****
 *
 */
public class HelloEE {


    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int first=calendar.getFirstDayOfWeek();
        System.out.println(weekday);
        System.out.println(first);
    }
}
