package com.stephen.astro.ui;

import android.support.annotation.Nullable;

import com.greasemonk.timetable.IGridItem;
import com.greasemonk.timetable.TimeRange;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ScheduleItem implements IGridItem {
    private String employeeName, projectName;
    private TimeRange timeRange;

    public ScheduleItem() {
    }

    public ScheduleItem(String employeeName, String projectName, Date planStart, Date planEnd) {
        this.employeeName = employeeName;
        this.projectName = projectName;
        this.timeRange = new TimeRange(planStart, planEnd);
    }

    public static ScheduleItem generateSample() {
        final String[] firstNameSamples = {"Kristeen", "Carran", "Lillie", "Marje", "Edith", "Steve", "Henry", "Kyle", "Terrence"};
        final String[] lastNameSamples = {"Woodham", "Boatwright", "Lovel", "Dennel", "Wilkerson", "Irvin", "Aston", "Presley"};
        final String[] projectNames = {"Roof Renovation", "Mall Construction", "Demolition old Hallway"};

        // Generate a date range between now and 30 days
        Random rand = new Random();
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
//        int r1 = -rand.nextInt(12);
//        int r2 = rand.nextInt(12);
//        start.add(Calendar.DATE, r1);
        end.add(Calendar.DATE, 7);

        return new ScheduleItem(firstNameSamples[rand.nextInt(firstNameSamples.length)] + " " +
                lastNameSamples[rand.nextInt(lastNameSamples.length)],
                projectNames[rand.nextInt(projectNames.length)],
                start.getTime(),
                end.getTime());
    }

    @Nullable
    @Override
    public TimeRange getTimeRange() {
        return timeRange;
    }

    @Override
    public String getName() {
        return projectName;
    }

    @Override
    public String getPersonName() {
        return employeeName;
    }
}