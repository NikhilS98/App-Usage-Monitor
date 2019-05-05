package com.android.ang.seprocessmonitor;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener, AdapterView.OnItemSelectedListener {

    private Intent notifierServiceIntent;
    private NotifierService notifierService;
    private TextView dateTextView;
    private ListView appsInfoListView;
    private AppsInfoAdapter appsInfoAdapter;
    private DatePickerFragment datePickerFragment;
    private DeviceApps deviceApps;
    private String durationSelected;
    private boolean sortOnUsage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notifierService = new NotifierService();
        notifierServiceIntent = new Intent(this, NotifierService.class);
        if (!isMyServiceRunning(notifierService.getClass())) {
            startService(notifierServiceIntent);
        }

        //shows the date selected
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        //the list for apps info
        appsInfoListView = (ListView) findViewById(R.id.appsInfoListView);

        datePickerFragment = new DatePickerFragment();

        deviceApps = new DeviceApps(this);

        durationSelected = "Daily";
        sortOnUsage = true;

        //Daily, Weekly and Monthly dropdown
        Spinner durationSpinner = (Spinner) findViewById(R.id.durationSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.durations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        durationSpinner.setAdapter(adapter);

        durationSpinner.setOnItemSelectedListener(this);

        //if usage stats permission are granted then calculate the usage otherwise direct to settings page
        if(checkUsageStatsPermission()){
            setListView(durationSelected);
        }
        else{
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }

        Button visualAppsInfoBtn = (Button) findViewById(R.id.visualAppsInfoBtn);
        visualAppsInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVisualAppsInformation();
            }
        });

        Button inactiveAppsBtn = (Button) findViewById(R.id.inactiveAppsBtn);
        inactiveAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInactiveApps();
            }
        });

        Button suggestedAppsBtn = (Button) findViewById(R.id.suggestedAppsBtn);
        suggestedAppsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSuggestedApps();
            }
        });

        ToggleButton sortToggleBtn = (ToggleButton) findViewById(R.id.sortToggleBtn);
        sortToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    sortOnUsage = false;
                }
                else {
                    sortOnUsage = true;
                }
                setListView(durationSelected);
            }
        });

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

    }

    //Called when app is returned to foreground from background. Have to recalculate the usage
    //stats as another app could have been used meanwhile
    @Override
    protected void onResume() {
        super.onResume();
        setListView(durationSelected);
    }

    @Override
    protected void onDestroy() {
        stopService(notifierServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();
    }

    //Called when DatePicker dialog is closed. Read the comment on onDismiss in DatePicker class
    @Override
    public void onDismiss(DialogInterface dialog) {
        dateTextView.setText(DateSelected.getDate());
        setListView(durationSelected);
    }

    //Called when an item in the Spinner is selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(!text.equals(durationSelected)){
            durationSelected = text;
            setListView(durationSelected);
        }
    }

    //Also from Spinner class
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //returns if usage stats permission is granted or not
    private boolean checkUsageStatsPermission() {
        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    private void callSelectedUsage(String text) {
        if(text.equals("Daily")) {
            deviceApps.dailyUsage();
        }
        else if(text.equals("Weekly")) {
            deviceApps.weeklyUsage();
        }
        else if(text.equals("Monthly")) {
            deviceApps.monthlyUsage();
        }
    }

    //Starts the given Activity. Here the Ads Activity
    private void openSuggestedApps() {
        Intent intent = new Intent(this, Ads.class);
        startActivity(intent);
    }

    private void openInactiveApps() {
        Intent intent = new Intent(this, InactiveApps.class);
        startActivity(intent);
    }

    private void openVisualAppsInformation() {
        Intent intent = new Intent(this, VisualAppsInformation.class);

        //To pass the Lists to the given Activity
        intent.putExtra("appNameList", deviceApps.getAppNameList());

        ArrayList<Long> usageTimeMilliList = deviceApps.getUsageTimeMilliList();
        //Had to do this otherwise App kept crashing whenever bar chart was opened
        long[] usageTimeList = new long[usageTimeMilliList.size()];
        for(int i = 0; i < usageTimeList.length; i++) {
            usageTimeList[i] = usageTimeMilliList.get(i);
        }
        intent.putExtra("usageTimeList", usageTimeList);

        startActivity(intent);
    }

    private void setListView(String text) {
        callSelectedUsage(text);
        if(sortOnUsage)
            deviceApps.sortOnUsageDescending();
        else
            deviceApps.sortOnLastUsedDescending();

        appsInfoAdapter = new AppsInfoAdapter(this, deviceApps.getAppNameList(), deviceApps.getUsageTimeList(),
                deviceApps.getLastUsedList(), deviceApps.getAppIconList());
        appsInfoListView.setAdapter(appsInfoAdapter);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

}
