package com.stephen.astro.comparator;

import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.Comparator;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class ChannelComparatorByNumber implements Comparator<ChannelViewModel> {

    @Override
    public int compare(ChannelViewModel o, ChannelViewModel o2) {
        return o.getChannelNumber() - o2.getChannelNumber();
    }
}
