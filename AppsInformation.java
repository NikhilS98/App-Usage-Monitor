package com.android.ang.seprocessmonitor;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AppsInformation {

    //UsageStatsManager is the class that contains the usage information
    private UsageStatsManager usageStatsManager;
    private SimpleDateFormat sdf;
    //PackageManager contains useful information of installed apps like icon, name etc
    private PackageManager pkgManager;
    //Don't know much about Context but have to pass in some cases if the class is not an activity like this class
    private Context context;

    public AppsInformation(Context c) {
        context = c;
        usageStatsManager = (UsageStatsManager) c.getSystemService(Context.USAGE_STATS_SERVICE);
        sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        pkgManager = c.getPackageManager();
    }

    public void setDailyUsage (DeviceApps deviceApps, int day, int month, int year) {
        //gets info of current date and time
        Calendar calendar = Calendar.getInstance();
        //set to the day, month, year set by the user using DatePicker
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        long end = calendar.getTimeInMillis();

        //go to the starting time of the selected date
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);
        long start = calendar.getTimeInMillis();

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, start, end);
        setListsForUsage(deviceApps, stats);
    }

    public void setWeeklyUsage (DeviceApps deviceApps, int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        long end = calendar.getTimeInMillis();

        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        long start = calendar.getTimeInMillis();

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_WEEKLY, start, end);
        setListsForUsage(deviceApps, stats);
    }

    public void setMonthlyUsage (DeviceApps deviceApps, int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        long end = calendar.getTimeInMillis();

        calendar.add(Calendar.MONTH, -1);
        long start = calendar.getTimeInMillis();

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, start, end);
        setListsForUsage(deviceApps, stats);
    }

    //Inactive apps was not working properly with weekly setWeeklyUsage so made this
    public void setLastTenDaysUsage (DeviceApps deviceApps, int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        long end = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_MONTH, -10);
        long start = calendar.getTimeInMillis();

        List<UsageStats> stats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_MONTHLY, start, end);
        setListsForUsage(deviceApps, stats);
    }

    //sets the lists in DeviceApps object passed to it
    private void setListsForUsage(DeviceApps deviceApps, List<UsageStats> stats) {
        if(stats == null) {
            Toast.makeText(context, "Invalid Date", Toast.LENGTH_LONG);
        }
        else {
            ArrayList<String> appNameList = new ArrayList<>();
            ArrayList<Drawable> appIconList = new ArrayList<>();
            ArrayList<String> usageTimeList = new ArrayList<>();
            ArrayList<String> lastUsedList = new ArrayList<>();
            ArrayList<Long> usageTimeMilliList = new ArrayList<>();
            ArrayList<Long> lastUsedMilliList = new ArrayList<>();

            for (int i = 0; i < stats.size(); i++) {
                String pkgName = stats.get(i).getPackageName();
                try {
                    String appName = (String) pkgManager.getApplicationLabel(
                            pkgManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA));
                    if(appName.startsWith("com."))
                        continue;
                    int index = appNameList.indexOf(appName);
                    if(index > -1) {
                        long usageMilli = stats.get(index).getTotalTimeInForeground();
                        usageTimeMilliList.set(index, usageMilli + stats.get(i).getTotalTimeInForeground());
                        lastUsedMilliList.set(index, stats.get(i).getLastTimeUsed());
                        usageTimeList.set(index, millisToHMS(usageTimeMilliList.get(index)));
                        lastUsedList.set(index, sdf.format(lastUsedMilliList.get(index)));
                    }
                    else {
                        appNameList.add(appName);
                        appIconList.add(pkgManager.getApplicationIcon(pkgName));
                        usageTimeMilliList.add(stats.get(i).getTotalTimeInForeground());
                        usageTimeList.add(millisToHMS(stats.get(i).getTotalTimeInForeground()));
                        lastUsedList.add(sdf.format(stats.get(i).getLastTimeUsed()));
                        lastUsedMilliList.add(stats.get(i).getLastTimeUsed());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
            deviceApps.setAppNameList(appNameList);
            deviceApps.setUsageTimeList(usageTimeList);
            deviceApps.setLastUsedList(lastUsedList);
            deviceApps.setAppIconList(appIconList);
            deviceApps.setUsageTimeMilliList(usageTimeMilliList);
            deviceApps.setLastUsedMilliList(lastUsedMilliList);
        }
    }

    private String millisToHMS(long millis) {
        int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis / (1000 * 60)) % 60);
        int seconds = (int) (millis / 1000) % 60;
        return hours + "hr " + minutes + "m " + seconds + "s";
    }
}
