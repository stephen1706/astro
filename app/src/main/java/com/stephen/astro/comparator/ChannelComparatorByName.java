package com.stephen.astro.comparator;

import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.Comparator;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class ChannelComparatorByName implements Comparator<ChannelViewModel>{

    @Override
    public int compare(ChannelViewModel o, ChannelViewModel o2) {
        return o.getChannelName().charAt(0) - o2.getChannelName().charAt(0);
    }
}
