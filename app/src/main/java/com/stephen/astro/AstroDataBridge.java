package com.stephen.astro;

import android.content.Context;

import com.stephen.astro.comparator.ScheduleComparatorByName;
import com.stephen.astro.comparator.ScheduleComparatorByNumber;
import com.stephen.astro.datamodels.ChannelDataModel;
import com.stephen.astro.datamodels.ChannelListDataModel;
import com.stephen.astro.datamodels.ScheduleDataModel;
import com.stephen.astro.datamodels.ScheduleListDataModel;
import com.stephen.astro.realm.Favourite;
import com.stephen.astro.util.ViewUtils;
import com.stephen.astro.viewmodels.ChannelViewModel;
import com.stephen.astro.viewmodels.ScheduleListViewModel;
import com.stephen.astro.viewmodels.ScheduleViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class AstroDataBridge {
    public static ArrayList<ChannelViewModel> getChannelListViewModel(ChannelListDataModel channelListDataModel) {
        Realm realm = Realm.getDefaultInstance();

        ArrayList<ChannelViewModel> list = new ArrayList<>();
        for (ChannelDataModel dataModel : channelListDataModel.getChannels()) {
            ChannelViewModel viewModel = new ChannelViewModel();
            viewModel.setChannelId(dataModel.getChannelId());
            viewModel.setChannelName(dataModel.getChannelTitle());
            viewModel.setChannelNumber(dataModel.getChannelStbNumber());
            if (realm.where(Favourite.class).equalTo("channelId", dataModel.getChannelStbNumber()).findFirst() != null) {
                viewModel.setFavourite(true);
            }
            list.add(viewModel);
        }

        return list;
    }

    public static HashMap<Integer, ArrayList<ScheduleDataModel>>  sortScheduleByName(HashMap<Integer, ArrayList<ScheduleDataModel>> scheduleListDataModel){
        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : scheduleListDataModel.entrySet()) {
            ArrayList<ScheduleDataModel> list = entry.getValue();

            Collections.sort(list, new ScheduleComparatorByName());
        }
        return scheduleListDataModel;
    }

    public static HashMap<Integer, ArrayList<ScheduleDataModel>>  sortScheduleByNumber(HashMap<Integer, ArrayList<ScheduleDataModel>> scheduleListDataModel){
        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : scheduleListDataModel.entrySet()) {
            ArrayList<ScheduleDataModel> list = entry.getValue();

            Collections.sort(list, new ScheduleComparatorByNumber());
        }
        return scheduleListDataModel;
    }

    public static HashMap<Integer, ArrayList<ScheduleDataModel>> get2dScheduleData(ScheduleListDataModel scheduleListDataModel){
        HashMap<Integer, ArrayList<ScheduleDataModel>> data = new HashMap<>();

        for (ScheduleDataModel dataModel : scheduleListDataModel.getGetevent()) {
            if (data.containsKey(dataModel.getChannelId())) {
                ArrayList<ScheduleDataModel> list = data.get(dataModel.getChannelId());
                list.add(dataModel);
            } else {
                ArrayList<ScheduleDataModel> list = new ArrayList<ScheduleDataModel>();
                list.add(dataModel);
                data.put(dataModel.getChannelId(), list);
            }
        }

        return data;
    }

    public static ArrayList<ScheduleListViewModel> getScheduleList(Context context, ScheduleListDataModel scheduleListDataModel, String startTime) {
        //todo stephen need to make data 2dimenstion with hashmap<ChannelId, Arraylist<ScheduleDataModel>>, then sort the arraylist with time
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat startTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat outSdf = new SimpleDateFormat("HH:mm");

        timeSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        TimeZone malaysiaTimezone = new SimpleTimeZone((int) TimeUnit.HOURS.toMillis(8), "GMT+8");
        startTimeSdf.setTimeZone(malaysiaTimezone);

        ArrayList<ScheduleListViewModel> list = new ArrayList<ScheduleListViewModel>();
        ScheduleListViewModel currentChannel = null;
        ScheduleDataModel lastSchedule = null;

        for (ScheduleDataModel dataModel : scheduleListDataModel.getGetevent()) {
            if (currentChannel == null || currentChannel.getChannelId() != dataModel.getChannelId()) {
                //change channel since data from api is not 2 dimension array
                lastSchedule = null;
                currentChannel = new ScheduleListViewModel();
                currentChannel.setChannelId(dataModel.getChannelId());
                currentChannel.setChannelName(dataModel.getChannelTitle());
                currentChannel.setChannelNumber(Integer.parseInt(dataModel.getChannelStbNumber()));
                currentChannel.setScheduleList(new ArrayList<>());
                list.add(currentChannel);

                ScheduleViewModel emptyPaddingStart = new ScheduleViewModel();

                try {
                    Date startDate = startTimeSdf.parse(startTime);
                    Date startFirstEventDate = timeSdf.parse(dataModel.getDisplayDateTimeUtc());

                    long difference = startFirstEventDate.getTime() - startDate.getTime();
                    int duration = (int) TimeUnit.MILLISECONDS.toMinutes(difference);//add leftover from previous day
                    emptyPaddingStart.setDuration(duration);
                    emptyPaddingStart.setWidth(getWidth(duration, context));
                    currentChannel.getScheduleList().add(emptyPaddingStart);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                //we need to add empty if there is a gap in the middle then we need to insert blank gap
                if(lastSchedule != null) {
                    try {
                        Date lastScheduleStartDate = timeSdf.parse(lastSchedule.getDisplayDateTimeUtc());
                        long lastScheduleFinishTime = lastScheduleStartDate.getTime()
                                + getDurationInMillis(lastSchedule.getDisplayDuration());

                        long currentScheduleStartDate = timeSdf.parse(dataModel.getDisplayDateTimeUtc()).getTime();

                        if(currentScheduleStartDate > lastScheduleFinishTime){
                            ScheduleViewModel emptyPaddingStart = new ScheduleViewModel();
                            int duration = (int) TimeUnit.MILLISECONDS.toMinutes(currentScheduleStartDate - lastScheduleFinishTime);
                            emptyPaddingStart.setDuration(duration);
                            emptyPaddingStart.setWidth(getWidth(duration, context));
                            currentChannel.getScheduleList().add(emptyPaddingStart);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            ScheduleViewModel scheduleViewModel = new ScheduleViewModel();
            scheduleViewModel.setEventId(dataModel.getEventID());
            scheduleViewModel.setProgramTitle(dataModel.getProgrammeTitle());

            int duration = getDurationInMinutes(dataModel.getDisplayDuration());
            scheduleViewModel.setDuration(duration);
            scheduleViewModel.setWidth(getWidth(duration, context));
            try {
                Date date = timeSdf.parse(dataModel.getDisplayDateTimeUtc());
                scheduleViewModel.setStartTime(outSdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            scheduleViewModel.setRawTimeUTC(dataModel.getDisplayDateTimeUtc());
            currentChannel.getScheduleList().add(scheduleViewModel);
            lastSchedule = dataModel;
        }

        return list;
    }

    private static int getWidth(int duration, Context context) {
        return (int) (((double) duration / 60) * ViewUtils.dpToPx(300, context));
    }

    private static int getDurationInMinutes(String input) {
        String[] tokens = input.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);
        int duration = 3600 * hours + 60 * minutes + seconds;
        return (duration / 60);
    }

    private static long getDurationInMillis(String input) {
        String[] tokens = input.split(":");
        int hours = Integer.parseInt(tokens[0]);
        int minutes = Integer.parseInt(tokens[1]);
        int seconds = Integer.parseInt(tokens[2]);
        int duration = 3600 * hours + 60 * minutes + seconds;
        return duration * 1000;
    }
}
