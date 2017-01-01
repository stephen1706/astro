package com.stephen.astro.comparator;

import com.stephen.astro.datamodels.ScheduleDataModel;

import java.util.Comparator;

/**
 * Created by stephenadipradhana on 1/1/17.
 */

public class ScheduleComparatorByName implements Comparator<ScheduleDataModel> {
    @Override
    public int compare(ScheduleDataModel t0, ScheduleDataModel t1) {
        return t0.getChannelTitle().charAt(0) - t1.getChannelTitle().charAt(0);
    }
}
