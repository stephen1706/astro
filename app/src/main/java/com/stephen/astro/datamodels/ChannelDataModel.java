package com.stephen.astro.datamodels;

/**
 * Created by stephenadipradhana on 12/28/16.
 */
public class ChannelDataModel {
    /**
     * channelId : 1
     * channelTitle : HBO
     * channelStbNumber : 411
     */

    private int channelId;
    private String channelTitle;
    private int channelStbNumber;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public int getChannelStbNumber() {
        return channelStbNumber;
    }

    public void setChannelStbNumber(int channelStbNumber) {
        this.channelStbNumber = channelStbNumber;
    }
}
