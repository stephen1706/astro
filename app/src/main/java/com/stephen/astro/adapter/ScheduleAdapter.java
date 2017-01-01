package com.stephen.astro.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stephen.astro.R;
import com.stephen.astro.viewmodels.ScheduleListViewModel;

import java.util.ArrayList;

/**
 * Created by stephenadipradhana on 12/31/16.
 */
public class ScheduleAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<ScheduleListViewModel> mList;
    private final LayoutInflater mLayoutInflater;

    public ScheduleAdapter(Context context, ArrayList<ScheduleListViewModel> scheduleListViewModels) {
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        mList = scheduleListViewModels;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public ScheduleListViewModel getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null || convertView.getTag() == null) {
            convertView = View.inflate(mContext, R.layout.row_empty, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.parent.removeAllViews();

        ScheduleListViewModel item = getItem(position);
        for (int i = 0; i < item.getScheduleList().size(); i++) {
            View view = mLayoutInflater.inflate(R.layout.item_schedule, viewHolder.parent, false);
            TextView programName = (TextView) view.findViewById(R.id.text_view_program_name);
            TextView startTime = (TextView) view.findViewById(R.id.text_view_start_time);

            if (TextUtils.isEmpty(item.getScheduleList().get(i).getProgramTitle())) {
                programName.setText("");
                startTime.setText("");
                view.setBackgroundColor(mContext.getResources().getColor(android.R.color.black));
            } else {
                programName.setText(item.getScheduleList().get(i).getProgramTitle());
                startTime.setText(item.getScheduleList().get(i).getStartTime());
                view.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            }

            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = item.getScheduleList().get(i).getWidth();
            layoutParams.height = (int) mContext.getResources().getDimension(R.dimen.schedule_height);
            view.setLayoutParams(layoutParams);
            viewHolder.parent.addView(view);
        }

        return convertView;
    }

    class ViewHolder {
        LinearLayout parent;

        ViewHolder(View convertView) {
            parent = (LinearLayout) convertView.findViewById(R.id.parent);
        }
    }
}
