package com.stephen.astro.viewmodels;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleViewModel {
    private int duration;//in minutes
    private int width;//to calculate item width
    private String programTitle;
    private String startTime;
    private String eventId;
    //todo stephen remove only for testing
    private String rawTimeUTC;

    public String getRawTimeUTC() {
        return rawTimeUTC;
    }

    public void setRawTimeUTC(String rawTimeUTC) {
        this.rawTimeUTC = rawTimeUTC;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getProgramTitle() {
        return programTitle;
    }

    public void setProgramTitle(String programTitle) {
        this.programTitle = programTitle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
