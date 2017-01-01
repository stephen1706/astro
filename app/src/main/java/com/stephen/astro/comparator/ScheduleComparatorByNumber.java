package com.stephen.astro.comparator;

import com.stephen.astro.datamodels.ScheduleDataModel;

import java.util.Comparator;

/**
 * Created by stephenadipradhana on 1/1/17.
 */

public class ScheduleComparatorByNumber implements Comparator<ScheduleDataModel> {
    @Override
    public int compare(ScheduleDataModel t0, ScheduleDataModel t1) {
        return Integer.parseInt(t0.getChannelStbNumber()) - Integer.parseInt(t1.getChannelStbNumber());
    }
}
