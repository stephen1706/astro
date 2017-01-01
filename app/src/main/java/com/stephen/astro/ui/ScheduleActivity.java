package com.stephen.astro.ui;

import android.os.Bundle;

import com.greasemonk.timetable.TimeTable;
import com.stephen.astro.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleActivity extends RxAppCompatActivity {

    private TimeTable timeTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        timeTable = (TimeTable) findViewById(R.id.time_table);
        timeTable.setItems(generateSamplePlanData());
    }

    private static List<ScheduleItem> generateSamplePlanData() {
        List<ScheduleItem> planItems = new ArrayList<>();
        for (int i = 0; i < 20; i++)
            planItems.add(ScheduleItem.generateSample());

        return planItems;
    }
}
