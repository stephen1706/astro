package com.stephen.astro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stephen.astro.R;
import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.HashSet;
import java.util.List;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class ChannelListAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private final OnFavouriteListener mListener;
    private List<ChannelViewModel> list;
    private HashSet<Integer> favourited;

    public ChannelListAdapter(List<ChannelViewModel> list, OnFavouriteListener listener) {
        this.list = list;
        mListener = listener;
        favourited = new HashSet<>();
        initCache(list);
    }

    private void initCache(List<ChannelViewModel> list) {
        for(ChannelViewModel viewModel : list){
            if(viewModel.isFavourite()){
                favourited.add(viewModel.getChannelId());
            }
        }
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_channel_list, parent, false);

        return new ChannelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        ChannelViewModel item = list.get(position);
        holder.channelName.setText(item.getChannelName());
        holder.channelNumber.setText(String.valueOf(item.getChannelNumber()));
        if(favourited.contains(item.getChannelId())){
            holder.favouriteButton.setImageResource(android.R.drawable.star_big_on);
            holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onDislike(item.getChannelId());
                    favourited.remove(item.getChannelId());
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.favouriteButton.setImageResource(android.R.drawable.star_big_off);
            holder.favouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onLike(item.getChannelId());
                    favourited.add(item.getChannelId());
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnFavouriteListener {
        public void onLike(int id);
        public void onDislike(int id);
    }
}