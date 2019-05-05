package com.android.ang.seprocessmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ExceededUsageApps extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exceeded_usage_apps);


        DoubleObject[] appsRecommendedUsage = new DoubleObject[7];
        //appsRecommendedUsage[0] = new DoubleObject("whatsapp", (long) 29);
        appsRecommendedUsage[0] = new DoubleObject("chrome", (long) 53);
        appsRecommendedUsage[1] = new DoubleObject("skype", (long) 16);
        appsRecommendedUsage[2] = new DoubleObject("messenger", (long) 10);
        appsRecommendedUsage[3] = new DoubleObject("facebook", (long) 41);
        appsRecommendedUsage[4] = new DoubleObject("instagram", (long) 53);
        appsRecommendedUsage[5] = new DoubleObject("snapchat", (long) 50);
        appsRecommendedUsage[6] = new DoubleObject("youtube", (long) 60);

        TextView exceededAppsTextView = (TextView) findViewById(R.id.exceededAppsTextView);
        exceededAppsTextView.setMovementMethod(new ScrollingMovementMethod());

        for(int i = 0; i < appsRecommendedUsage.length; i++) {
            exceededAppsTextView.append("App: " + appsRecommendedUsage[i].getObject1() + "\n"
                                        + "Recommended Daily Use: " + appsRecommendedUsage[i].getObject2() + " minutes" + "\n\n");
        }
    }
}
