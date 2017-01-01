package com.stephen.astro.viewmodels;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class ChannelViewModel {
    protected int channelId;
    protected String channelName;
    protected boolean isFavourite;
    protected int channelNumber;

    public int getChannelNumber() {
        return channelNumber;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
}
