package com.android.ang.seprocessmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;

public class Ads extends AppCompatActivity {

    // 2000000 milliseconds is a little more than half an hour
    // set to 10 for testing
    // long type since built-in java classes return time in ms in long type
    private long benchmark = 10;

    private String[] lifestyle = {"spotify", "tripadvisor", "uber",
            "careem", "nike+ training club", "workout trainer", "sure universal remote"
            ,"youtube","keepsafe"};

    private String[] games = {"angry birds", "subway surfer", "clash of clans",
            "pokémon go","fruit ninja","candy crush saga","temple run"};

    private String[] socialmedia = {"facebook", "snapchat", "pinterest", "instagram",
            "whatsapp", "tumblr", "twitter", "google+","reddit","9gag"};

    private String[] productivity = {"microsoft word", "microsoft excel",
            "microsoft powerpoint", "google docs", "microsoft onenote", "microsoft outlook",
            "gmail", "yahoo!", "chrome", "google maps","google assistant"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);

        //TextView to display the string of ads
        TextView adsTextView = (TextView) findViewById(R.id.adsTextView);
        adsTextView.setMovementMethod(new ScrollingMovementMethod());

        DeviceApps deviceApps = new DeviceApps(this);
        //sets the lists in deviceApps by weekly usage.
        deviceApps.weeklyUsage();

        String suggestedApps = suggestApps(deviceApps.getAppNameList(), deviceApps.getUsageTimeMilliList());
        if(!suggestedApps.equals(""))
            adsTextView.setText(suggestedApps);

    }

    public String suggestApps(ArrayList<String> appNameList, ArrayList<Long> usageTimeMilliList) {
        String suggestedApps = "";
        ArrayList<String> appNameListLower = new ArrayList<>();

        //If one of the lists has been appended to the string we don't want to check for it again
        boolean lifestyleFound = false, gamesFound = false, socialmediaFound = false, productivityFound = false;

        //because all the names are lowercase
        for(int i = 0; i < appNameList.size(); i++){
            appNameListLower.add(appNameList.get(i).toLowerCase());
        }

        for(int i = 0; i < appNameListLower.size(); i++) {
            //break the loop if all lists have already been appended
            if(lifestyleFound && gamesFound && socialmediaFound && productivityFound)
                break;
            String appname = appNameListLower.get(i);
            long usageTime = usageTimeMilliList.get(i);

            if(usageTime >= benchmark) {
                if (!lifestyleFound) {
                    for (String s : lifestyle) {
                        if (s.equals(appname)) {
                            for (int j = 0; j < lifestyle.length; j++) {
                                //check if any of the apps in suggestion list is not installed already
                                if (!appNameListLower.contains(lifestyle[j])) {
                                    suggestedApps += lifestyle[j] + "\n";
                                }
                            }
                            lifestyleFound = true;
                            break;
                        }
                    }
                }
                if (!gamesFound) {
                    for (String s : games) {
                        if (s.equals(appname)) {
                            for (int j = 0; j < games.length; j++) {
                                if (!appNameListLower.contains(games[j])) {
                                    suggestedApps += games[j] + "\n";
                                }
                            }
                            gamesFound = true;
                            break;
                        }
                    }
                }
                if (!socialmediaFound) {
                    for (String s : socialmedia) {
                        if (s.equals(appname)) {
                            for (int j = 0; j < socialmedia.length; j++) {
                                if (!appNameListLower.contains(socialmedia[j])) {
                                    suggestedApps += socialmedia[j] + "\n";
                                }
                            }
                            socialmediaFound = true;
                            break;
                        }
                    }
                }
                if (!productivityFound) {
                    for (String s : productivity) {
                        if (s.equals(appname)) {
                            for (int j = 0; j < productivity.length; j++) {
                                if (!appNameListLower.contains(productivity[j])) {
                                    suggestedApps += productivity[j] + "\n";
                                }
                            }
                            productivityFound = true;
                            break;
                        }
                    }
                }
            }

        }

        return suggestedApps;
    }

    //return to previous Activity when back btn is pressed
    @Override
    public void onBackPressed() {
        finish();
    }
}
