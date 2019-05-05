package com.android.ang.seprocessmonitor;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class DeviceApps {

    private AppsInformation appsInformation;

    private ArrayList<String> appNameList;
    private ArrayList<Drawable> appIconList;
    private ArrayList<String> usageTimeList;
    private ArrayList<String> lastUsedList;
    private ArrayList<Long> usageTimeMilliList;
    private ArrayList<Long> lastUsedMilliList;

    public DeviceApps(Context c) {
        appsInformation = new AppsInformation(c);
    }

    public void dailyUsage() {
        appsInformation.setDailyUsage(this, DateSelected.getDay(), DateSelected.getMonth(), DateSelected.getYear());
    }

    public void weeklyUsage() {
        appsInformation.setWeeklyUsage(this, DateSelected.getDay(), DateSelected.getMonth(), DateSelected.getYear());
    }

    public void monthlyUsage() {
        appsInformation.setMonthlyUsage(this, DateSelected.getDay(), DateSelected.getMonth(), DateSelected.getYear());
    }

    public void lastTenDaysUsage() {
        appsInformation.setLastTenDaysUsage(this, DateSelected.getDay(), DateSelected.getMonth(), DateSelected.getYear());
    }

    public void setAppNameList(ArrayList<String> appNameList) {
        this.appNameList = appNameList;
    }

    public void setUsageTimeList(ArrayList<String> usageTimeList) {
        this.usageTimeList = usageTimeList;
    }

    public void setLastUsedList(ArrayList<String> lastUsedList) {
        this.lastUsedList = lastUsedList;
    }

    public void setUsageTimeMilliList(ArrayList<Long> usageTimeMilliList) {
        this.usageTimeMilliList = usageTimeMilliList;
    }

    public void setLastUsedMilliList(ArrayList<Long> lastUsedMilliList) {
        this.lastUsedMilliList = lastUsedMilliList;
    }

    public void setAppIconList(ArrayList<Drawable> appIconList) {
        this.appIconList = appIconList;
    }

    public ArrayList<String> getAppNameList () {
        return appNameList;
    }

    public ArrayList<String> getUsageTimeList(){
        return usageTimeList;
    }

    public ArrayList<String> getLastUsedList() {
        return lastUsedList;
    }

    public ArrayList<Drawable> getAppIconList() {
        return appIconList;
    }

    public ArrayList<Long> getUsageTimeMilliList() {
        return usageTimeMilliList;
    }

    public ArrayList<Long> getLastUsedMilliList() {
        return lastUsedMilliList;
    }

    public void sortOnUsageDescending() {
        for(int j = 0; j < appNameList.size() - 1; j++) {
            for(int k = 0; k < appNameList.size() - j - 1; k++) {
                if(usageTimeMilliList.get(k) < usageTimeMilliList.get(k + 1)) {
                    long tempUsageMilli = usageTimeMilliList.get(k);
                    String tempAppName = appNameList.get(k);
                    String tempUsage = usageTimeList.get(k);
                    String tempLastUsed = lastUsedList.get(k);
                    Drawable tempIcon = appIconList.get(k);

                    usageTimeMilliList.set(k, usageTimeMilliList.get(k + 1));
                    appNameList.set(k, appNameList.get(k + 1));
                    usageTimeList.set(k, usageTimeList.get(k + 1));
                    lastUsedList.set(k, lastUsedList.get(k + 1));
                    appIconList.set(k, appIconList.get(k + 1));

                    usageTimeMilliList.set(k + 1, tempUsageMilli);
                    appNameList.set(k + 1, tempAppName);
                    usageTimeList.set(k + 1, tempUsage);
                    lastUsedList.set(k + 1, tempLastUsed);
                    appIconList.set(k + 1, tempIcon);
                }
            }
        }
    }

    public void sortOnLastUsedDescending() {
        for(int j = 0; j < appNameList.size() - 1; j++) {
            for(int k = 0; k < appNameList.size() - j - 1; k++) {
                if(lastUsedMilliList.get(k) < lastUsedMilliList.get(k + 1)) {
                    long tempLastUsedMilli = lastUsedMilliList.get(k);
                    String tempAppName = appNameList.get(k);
                    String tempUsage = usageTimeList.get(k);
                    String tempLastUsed = lastUsedList.get(k);
                    Drawable tempIcon = appIconList.get(k);

                    lastUsedMilliList.set(k, lastUsedMilliList.get(k + 1));
                    appNameList.set(k, appNameList.get(k + 1));
                    usageTimeList.set(k, usageTimeList.get(k + 1));
                    lastUsedList.set(k, lastUsedList.get(k + 1));
                    appIconList.set(k, appIconList.get(k + 1));

                    lastUsedMilliList.set(k + 1, tempLastUsedMilli);
                    appNameList.set(k + 1, tempAppName);
                    usageTimeList.set(k + 1, tempUsage);
                    lastUsedList.set(k + 1, tempLastUsed);
                    appIconList.set(k + 1, tempIcon);
                }
            }
        }
    }
}
