package com.stephen.astro.comparator;

import com.stephen.astro.datamodels.ScheduleDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * Created by stephenadipradhana on 1/1/17.
 */

public class ScheduleComparatorByNumber implements Comparator<ScheduleDataModel> {
    @Override
    public int compare(ScheduleDataModel t0, ScheduleDataModel t1) {
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return (int) (timeSdf.parse(t0.getDisplayDateTimeUtc()).getTime()
                    - timeSdf.parse(t1.getDisplayDateTimeUtc()).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
