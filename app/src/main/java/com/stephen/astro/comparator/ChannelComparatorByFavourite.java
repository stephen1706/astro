package com.stephen.astro.comparator;

import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.Comparator;

/**
 * Created by stephenadipradhana on 1/3/17.
 */

public class ChannelComparatorByFavourite implements Comparator<ChannelViewModel> {
    @Override
    public int compare(ChannelViewModel o, ChannelViewModel o2) {
        if ((o.isFavourite() && o2.isFavourite())
                || (!o.isFavourite() && !o2.isFavourite())) {
            return o.getChannelNumber() - o2.getChannelNumber();
        } else if(o.isFavourite()){
            return -1;
        } else if(o2.isFavourite()){
            return 1;
        } else {
            return 0;
        }
    }
}
