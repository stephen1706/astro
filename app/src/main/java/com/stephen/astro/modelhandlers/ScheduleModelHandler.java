package com.stephen.astro.modelhandlers;

import android.content.Context;

import com.stephen.astro.AstroDataBridge;
import com.stephen.astro.viewmodels.ChannelViewModel;
import com.stephen.astro.viewmodels.ScheduleListViewModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleModelHandler extends BaseModelHandler {
    public ScheduleModelHandler(Context context) {
        super(context);
    }

    public Observable<ArrayList<ScheduleListViewModel>> getScheduleList() {
        return getChannelListByNumber()
                .observeOn(Schedulers.newThread())
                .map(channelViewModels -> {
                    String channelIds = "";

                    for(int i = 0 ;i<10;i++){
                        ChannelViewModel channelViewModel = channelViewModels.get(i);
                        channelIds += channelViewModel.getChannelId() + ",";
                    }

                    channelIds = channelIds.substring(0, channelIds.length() - 1);
                    return channelIds;
                })
                .flatMap(channelIds ->
                        mApiService.getScheduleList(channelIds, "2016-12-31 01:00", "2017-01-01 00:59"))//request time is in malaysia time +8 GMT
                .map(channelListDataModel -> AstroDataBridge.getScheduleList(mContext, channelListDataModel, "2016-12-31 01:00"))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
