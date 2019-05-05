package com.android.ang.seprocessmonitor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class VisualAppsInformation extends AppCompatActivity {

    private ArrayList<String> appNameList;
    private long[] appUsageList;
    private BarChart appUsageBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual_apps_information);

        Bundle b = getIntent().getExtras();
        appNameList = b.getStringArrayList("appNameList");
        appUsageList = b.getLongArray("usageTimeList");

        appUsageBarChart = (BarChart) findViewById(R.id.appUsageBarChart);

        Description desc = new Description();
        desc.setText("");

        appUsageBarChart.setDescription(desc);
        appUsageBarChart.setDrawBarShadow(false);
        appUsageBarChart.setDrawValueAboveBar(true);
        appUsageBarChart.setMaxVisibleValueCount(appNameList.size());
        appUsageBarChart.setPinchZoom(true);
        appUsageBarChart.setDrawGridBackground(false);
        appUsageBarChart.zoom(6f, 6f,0, 0);

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < appUsageList.length; i++) {
            barEntries.add(new BarEntry(i, appUsageList[i]/60000));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "Usage in Minutes");
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet.setValueTextSize(12f);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(1f);

        appUsageBarChart.setData(barData);
        appUsageBarChart.animateXY(1500, 1500);

        XAxis xAxis = appUsageBarChart.getXAxis();
        xAxis.setLabelRotationAngle(90f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setGranularity(barData.getBarWidth());
        xAxis.setValueFormatter(new MyXAxisValueFormatter(appNameList));

        YAxis leftYAxis = appUsageBarChart.getAxisLeft();
        leftYAxis.setSpaceTop(600f);
        leftYAxis.setSpaceBottom(0f);
        leftYAxis.setTextSize(12f);

        YAxis rightYAxis = appUsageBarChart.getAxisRight();
        rightYAxis.setEnabled(false);

    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private ArrayList<String> values;

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return values.get((int) value);
        }
    }

    //return to previous Activity when back btn is pressed
    @Override
    public void onBackPressed() {
        finish();
    }
}
