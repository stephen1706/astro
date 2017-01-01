package com.stephen.astro.viewmodels;

import java.util.ArrayList;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleListViewModel extends ChannelViewModel {
    private ArrayList<ScheduleViewModel> scheduleList;

    public ArrayList<ScheduleViewModel> getScheduleList() {
        return scheduleList;
    }

    public void setScheduleList(ArrayList<ScheduleViewModel> scheduleList) {
        this.scheduleList = scheduleList;
    }
}
