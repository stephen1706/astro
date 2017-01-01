package com.stephen.astro;

import android.content.Context;

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
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.TreeMap;
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

    public static LinkedHashMap<Integer, ArrayList<ScheduleDataModel>> sortByChannelNumber(Map<Integer, ArrayList<ScheduleDataModel>> map) {
        List<Map.Entry<Integer, ArrayList<ScheduleDataModel>>> list = new LinkedList<Map.Entry<Integer, ArrayList<ScheduleDataModel>>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, ArrayList<ScheduleDataModel>>>() {
            @Override
            public int compare(Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry1, Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry2) {
                return Integer.parseInt(entry1.getValue().get(0).getChannelStbNumber())
                        - Integer.parseInt(entry2.getValue().get(0).getChannelStbNumber());
            }
        });

        LinkedHashMap<Integer, ArrayList<ScheduleDataModel>> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static LinkedHashMap<Integer, ArrayList<ScheduleDataModel>> sortByChannelName(Map<Integer, ArrayList<ScheduleDataModel>> map) {
        List<Map.Entry<Integer, ArrayList<ScheduleDataModel>>> list = new LinkedList<Map.Entry<Integer, ArrayList<ScheduleDataModel>>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<Integer, ArrayList<ScheduleDataModel>>>() {
            @Override
            public int compare(Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry1, Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry2) {
                return entry1.getValue().get(0).getChannelTitle().compareTo(entry2.getValue().get(0).getChannelTitle());
            }
        });

        LinkedHashMap<Integer, ArrayList<ScheduleDataModel>> result = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static TreeMap<Integer, ArrayList<ScheduleDataModel>> get2dScheduleData(ScheduleListDataModel scheduleListDataModel) {
        TreeMap<Integer, ArrayList<ScheduleDataModel>> data = new TreeMap<>();

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
        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : data.entrySet()) {
            ArrayList<ScheduleDataModel> list = entry.getValue();
            Collections.sort(list, new ScheduleComparatorByNumber());
        }

        return data;//already sorted by channelId from treeMap
    }

    public static ArrayList<ScheduleListViewModel> getScheduleList(Context context, LinkedHashMap<Integer, ArrayList<ScheduleDataModel>> data, String startTime) {
        SimpleDateFormat timeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat startTimeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat outSdf = new SimpleDateFormat("HH:mm");

        timeSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        TimeZone malaysiaTimezone = new SimpleTimeZone((int) TimeUnit.HOURS.toMillis(8), "GMT+8");
        startTimeSdf.setTimeZone(malaysiaTimezone);

        ArrayList<ScheduleListViewModel> list = new ArrayList<ScheduleListViewModel>();
        ScheduleListViewModel currentChannel = null;

        for (Map.Entry<Integer, ArrayList<ScheduleDataModel>> entry : data.entrySet()) {
            ScheduleDataModel lastSchedule = null;

            ArrayList<ScheduleDataModel> scheduleDataModels = entry.getValue();
            currentChannel = new ScheduleListViewModel();
            currentChannel.setChannelId(entry.getKey());
            currentChannel.setChannelName(scheduleDataModels.get(0).getChannelTitle());
            currentChannel.setChannelNumber(Integer.parseInt(scheduleDataModels.get(0).getChannelStbNumber()));

            ArrayList<ScheduleViewModel> scheduleViewModels = new ArrayList<>();
            //add empty padding
            ScheduleViewModel emptyPaddingStart = new ScheduleViewModel();
            try {
                Date startDate = startTimeSdf.parse(startTime);
                Date startFirstEventDate = timeSdf.parse(scheduleDataModels.get(0).getDisplayDateTimeUtc());

                long difference = startFirstEventDate.getTime() - startDate.getTime();
                int duration = (int) TimeUnit.MILLISECONDS.toMinutes(difference);//add leftover from previous day
                emptyPaddingStart.setDuration(duration);
                emptyPaddingStart.setWidth(getWidth(duration, context));
                scheduleViewModels.add(emptyPaddingStart);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for(ScheduleDataModel dataModel : scheduleDataModels){
                if(lastSchedule != null) {
                    try {//add middle gap if any
                        Date lastScheduleStartDate = timeSdf.parse(lastSchedule.getDisplayDateTimeUtc());
                        long lastScheduleFinishTime = lastScheduleStartDate.getTime()
                                + getDurationInMillis(lastSchedule.getDisplayDuration());

                        long currentScheduleStartDate = timeSdf.parse(dataModel.getDisplayDateTimeUtc()).getTime();

                        if (currentScheduleStartDate > lastScheduleFinishTime) {
                            ScheduleViewModel emptyPaddingMiddle = new ScheduleViewModel();
                            int duration = (int) TimeUnit.MILLISECONDS.toMinutes(currentScheduleStartDate - lastScheduleFinishTime);
                            emptyPaddingMiddle.setDuration(duration);
                            emptyPaddingMiddle.setWidth(getWidth(duration, context));
                            scheduleViewModels.add(emptyPaddingMiddle);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                ScheduleViewModel viewModel = new ScheduleViewModel();
                viewModel.setRawTimeUTC(dataModel.getDisplayDateTimeUtc());
                viewModel.setEventId(dataModel.getEventID());
                viewModel.setProgramTitle(dataModel.getProgrammeTitle());

                int duration = getDurationInMinutes(dataModel.getDisplayDuration());
                viewModel.setDuration(duration);
                viewModel.setWidth(getWidth(duration, context));
                try {
                    Date date = timeSdf.parse(dataModel.getDisplayDateTimeUtc());
                    viewModel.setStartTime(outSdf.format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                scheduleViewModels.add(viewModel);
                lastSchedule = dataModel;
            }
            currentChannel.setScheduleList(scheduleViewModels);

            list.add(currentChannel);
        }

        return list;
    }

    public static ArrayList<ScheduleListViewModel> getScheduleList(Context context, ScheduleListDataModel scheduleListDataModel, String startTime) {
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
                if (lastSchedule != null) {
                    try {
                        Date lastScheduleStartDate = timeSdf.parse(lastSchedule.getDisplayDateTimeUtc());
                        long lastScheduleFinishTime = lastScheduleStartDate.getTime()
                                + getDurationInMillis(lastSchedule.getDisplayDuration());

                        long currentScheduleStartDate = timeSdf.parse(dataModel.getDisplayDateTimeUtc()).getTime();

                        if (currentScheduleStartDate > lastScheduleFinishTime) {
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
        return (int) ((double) duration * ViewUtils.dpToPx(300, context) / 60);
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
