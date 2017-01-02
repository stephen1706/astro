package com.stephen.astro.api;

import com.stephen.astro.datamodels.ChannelListDataModel;
import com.stephen.astro.datamodels.ScheduleListDataModel;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by stephenadipradhana on 12/14/16.
 */

public interface AstroApiService {
    @GET(ApiRoutes.GET_CHANNEL_ROUTES)
    Observable<ChannelListDataModel> getChannelList();

    @GET(ApiRoutes.GET_SCHEDULE_ROUTES)
    Observable<ScheduleListDataModel> getScheduleList(@Query("channelId") String channelId,
                                                      @Query("periodStart") String periodStart,
                                                      @Query("periodEnd") String periodEnd);
}
