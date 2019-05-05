package com.android.ang.seprocessmonitor;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class InactiveApps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inactive_apps);

        ListView inactiveAppsListView = (ListView) findViewById(R.id.inactiveAppsListView);

        DeviceApps deviceApps = new DeviceApps(this);
        //Inactive apps was not working properly with weekly setWeeklyUsage so made this
        deviceApps.lastTenDaysUsage();

        ArrayList<String> inactiveAppNameList = new ArrayList<>();
        ArrayList<Drawable> inactiveAppIconList = new ArrayList<>();
        ArrayList<String> inactiveUsageTimeList = new ArrayList<>();
        ArrayList<String> inactiveLastUsedList = new ArrayList<>();

        ArrayList<String> appNameList = deviceApps.getAppNameList();
        ArrayList<Drawable> appIconList = deviceApps.getAppIconList();
        ArrayList<String> usageTimeList = deviceApps.getUsageTimeList();
        ArrayList<Long> lastUsedMilliList = deviceApps.getLastUsedMilliList();
        ArrayList<String> lastUsedList = deviceApps.getLastUsedList();

        Calendar calendar = Calendar.getInstance();
        long now = calendar.getTimeInMillis();

        for(int i = 0; i < appNameList.size(); i++) {
            if((now - lastUsedMilliList.get(i))/1000 > 604800){
                inactiveAppNameList.add(appNameList.get(i));
                inactiveAppIconList.add(appIconList.get(i));
                inactiveUsageTimeList.add(usageTimeList.get(i));
                inactiveLastUsedList.add(lastUsedList.get(i));
            }
        }

        AppsInfoAdapter appsInfoAdapter = new AppsInfoAdapter(this, inactiveAppNameList, inactiveUsageTimeList,
                inactiveLastUsedList, inactiveAppIconList);
        inactiveAppsListView.setAdapter(appsInfoAdapter);
    }

    //return to previous Activity when back btn is pressed
    @Override
    public void onBackPressed() {
        finish();
    }
}
