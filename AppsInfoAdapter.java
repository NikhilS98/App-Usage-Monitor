package com.android.ang.seprocessmonitor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

//To add values in the custom designed ListView

public class AppsInfoAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<String> appNameList, usageTimeList, lastUsedList;
    private ArrayList<Drawable> appIconList;

    public AppsInfoAdapter(Context c, ArrayList<String> n, ArrayList<String> u, ArrayList<String> l, ArrayList<Drawable> i) {
        layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        appNameList = n;
        usageTimeList = u;
        lastUsedList = l;
        appIconList = i;
    }

    //Required to override these but we don't need all
    @Override
    public int getCount() {
        return appNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return appNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.apps_info_layout, null);

        ImageView appIconImageView = (ImageView) v.findViewById(R.id.appIconImageView);
        TextView appNameTextView = (TextView) v.findViewById(R.id.appNameTextView);
        TextView usageTimeTextView = (TextView) v.findViewById(R.id.usageTimeTextView);
        TextView lastUsedTextView = (TextView) v.findViewById(R.id.lastUsedTextView);

        String appName = appNameList.get(position);
        String usageTime = usageTimeList.get(position);
        String lastUsed = lastUsedList.get(position);
        Drawable appIcon = appIconList.get(position);

        appNameTextView.setText(appName);
        usageTimeTextView.setText(usageTime);
        lastUsedTextView.setText("Last Used: " + lastUsed);
        appIconImageView.setImageDrawable(appIcon);

        return v;
    }
}
