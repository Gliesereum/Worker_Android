package com.gliesereum.coupler_worker.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.gliesereum.coupler_worker.R;
import com.gliesereum.coupler_worker.network.json.carwash.AllCarWashResponse;
import com.gliesereum.coupler_worker.network.json.carwash.RecordsItem;
import com.gliesereum.coupler_worker.network.json.carwash.WorkTimesItem;
import com.gliesereum.coupler_worker.util.FastSave;
import com.gliesereum.coupler_worker.util.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.ehsun.coloredtimebar.TimelineView;

public class OrderNewActivity extends AppCompatActivity {

    private AllCarWashResponse carWash;
    private Map<String, WorkTimesItem> workTimeMap = new HashMap<>();
    private LinearLayout boxLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_new);
        carWash = FastSave.getInstance().getObject("carWash", AllCarWashResponse.class);
//        boxLinearLayout = (LinearLayout) findViewById(R.id.boxLinearLayout);
        workTimeMap.clear();
        for (int i = 0; i < carWash.getWorkTimes().size(); i++) {
            workTimeMap.put(carWash.getWorkTimes().get(i).getDayOfWeek(), carWash.getWorkTimes().get(i));
        }
        setBoxTime(carWash);


    }

    private void setBoxTime(AllCarWashResponse carWash) {
        for (int i = 0; i < carWash.getSpaces().size(); i++) {
            View layout2 = LayoutInflater.from(this).inflate(R.layout.layout_boxline, boxLinearLayout, false);
            TimelineView timelineBox = layout2.findViewById(R.id.timelineView);
            Calendar calendar = Calendar.getInstance();
            int intDay = calendar.get(Calendar.DAY_OF_WEEK);
            timelineBox.setTimeRange(Util.getStringTime(workTimeMap.get(getCurrentDayOfWeek(intDay)).getFrom()) + "-" + Util.getStringTime(workTimeMap.get(getCurrentDayOfWeek(intDay)).getTo()));
            timelineBox.setTimeTextInterval(10);
            timelineBox.setFractionTextSize(50);
            timelineBox.setId(i);
            timelineBox.setBarWidth(100);
            timelineBox.setFractionLineLength(30);
            timelineBox.setBarColorAvailable(Color.parseColor("#282828"));
            timelineBox.setBarColorNotAvailable(Color.parseColor("#F5A623"));
            timelineBox.setAvailableTimeRange(getTimeInBox(carWash.getRecords(), carWash.getSpaces().get(i).getId()));
            boxLinearLayout.addView(layout2);
        }
    }

    private List<String> getTimeInBox(List<RecordsItem> records, String id) {
        List<String> timeRange = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            if (records.get(i).getWorkingSpaceId().equals(id)) {
                timeRange.add(Util.getStringTime(records.get(i).getBegin()) + "-" + Util.getStringTime(records.get(i).getFinish()));
            }
        }
        return timeRange;
    }

    private String getCurrentDayOfWeek(int intDay) {
        String dayOfWeek = "";
        switch (intDay) {
            case 1:
                dayOfWeek = "SUNDAY";
                break;
            case 2:
                dayOfWeek = "MONDAY";
                break;
            case 3:
                dayOfWeek = "TUESDAY";
                break;
            case 4:
                dayOfWeek = "WEDNESDAY";
                break;
            case 5:
                dayOfWeek = "THURSDAY";
                break;
            case 6:
                dayOfWeek = "FRIDAY";
                break;
            case 7:
                dayOfWeek = "SATURDAY";
                break;
        }

        return dayOfWeek;
    }
}
