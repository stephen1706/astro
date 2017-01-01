package com.stephen.astro.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.stephen.astro.R;
import com.stephen.astro.viewmodels.ScheduleListViewModel;

import java.util.List;

/**
 * Created by stephenadipradhana on 12/31/16.
 */

public class ChannelScheduleAdapter extends ArrayAdapter<ScheduleListViewModel> {
    private final LayoutInflater mLayoutInflater;

    public ChannelScheduleAdapter(Context context, int resource, List<ScheduleListViewModel> objects) {
        super(context, resource, objects);
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = mLayoutInflater.inflate(R.layout.row_channel, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.channelName.setText(getItem(position).getChannelName());
        viewHolder.channelNumber.setText(String.valueOf(getItem(position).getChannelNumber()));
        return convertView;
    }

    class ViewHolder {
        TextView channelName;
        TextView channelNumber;

        ViewHolder(View convertView) {
            channelName = (TextView) convertView.findViewById(R.id.text_view_channel_name);
            channelNumber = (TextView) convertView.findViewById(R.id.text_view_channel_number);
        }
    }
}
