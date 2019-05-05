package com.android.ang.seprocessmonitor;

public class DateSelected {
    //Convenient to keep static since the same values are needed in different class
    private static int day, month, year;
    private static boolean today = true;

    public static void setDay (int d) {
        day = d;
    }

    public static void setMonth (int m) {
        month = m;
    }

    public static void setYear (int y) {
        year = y;
    }

    public static void setToday (boolean t) {
        today = t;
    }

    public static int getDay () {
        return day;
    }

    public static int getMonth () {
        return month;
    }

    public static int getYear () {
        return year;
    }

    public static String getDate () {
        if(today)
            return "Today";
        return day + "/" + (month + 1) + "/" + year;
    }
}
