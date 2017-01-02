package com.stephen.astro.modelhandlers;

import android.content.Context;
import android.util.Log;

import com.stephen.astro.AstroDataBridge;
import com.stephen.astro.Constants;
import com.stephen.astro.PaginationFinishException;
import com.stephen.astro.viewmodels.ChannelViewModel;
import com.stephen.astro.viewmodels.ScheduleListViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleModelHandler extends BaseModelHandler {

    private final SimpleDateFormat requestFormat;

    public ScheduleModelHandler(Context context) {
        super(context);
        requestFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    }

    public Observable<ArrayList<ScheduleListViewModel>> getScheduleList(int sort, Calendar calendar, int index) {
        long offsetFromGMT = calendar.getTimeZone().getRawOffset();
        long differenceToServer = Constants.SERVER_TIMEZONE - offsetFromGMT;

        calendar.setTimeInMillis(calendar.getTimeInMillis() + differenceToServer);
        String startTime = requestFormat.format(calendar.getTime());

        calendar.add(Calendar.HOUR, 23);
        calendar.add(Calendar.MINUTE, 59);
        String endTime = requestFormat.format(calendar.getTime());

        Observable<ArrayList<ChannelViewModel>> observable;
        if(sort == Constants.SORT_NAME){
            observable = getChannelListSortedByName();
        } else {
            observable = getChannelListByNumber();
        }
        return observable
                .observeOn(Schedulers.newThread())
                .map(channelViewModels -> {
                    if (index >= channelViewModels.size()) {
                        Log.d("test","PaginationFinishException");
                        throw new PaginationFinishException();
                    }

                    String channelIds = "";

                    for (int i = index; i < index + Constants.PAGINATION_LENGTH && i < channelViewModels.size(); i++) {
                        ChannelViewModel channelViewModel = channelViewModels.get(i);
                        channelIds += channelViewModel.getChannelId() + ",";
                    }

                    channelIds = channelIds.substring(0, channelIds.length() - 1);
                    return channelIds;
                })
                .flatMap(channelIds ->
                        mAstroApiService.getScheduleList(channelIds, startTime, endTime))//request time is in malaysia time +8 GMT
                .map(AstroDataBridge::get2dScheduleData)
                .map((map) -> {
                    if (sort == Constants.SORT_NAME) {
                        return AstroDataBridge.sortByChannelName(map);
                    } else {
                        return AstroDataBridge.sortByChannelNumber(map);
                    }
                })
                .map(channelListDataModel -> AstroDataBridge.getScheduleList(mContext, channelListDataModel, startTime))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
