package com.stephen.astro.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stephen.astro.R;

/**
 * Created by stephenadipradhana on 12/28/16.
 */
public class ChannelViewHolder extends RecyclerView.ViewHolder {
    TextView channelName;
    TextView channelNumber;
    ImageButton favouriteButton;

    public ChannelViewHolder(View view) {
        super(view);
        channelName = (TextView) view.findViewById(R.id.text_view_channel_name);
        channelNumber = (TextView) view.findViewById(R.id.text_view_channel_number);
        favouriteButton = (ImageButton) view.findViewById(R.id.button_favourite);
    }
}
