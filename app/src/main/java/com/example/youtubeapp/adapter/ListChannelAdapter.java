package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.infochannel.Channel;
import com.example.youtubeapp.model.itemrecycleview.ChannelItem;
import com.example.youtubeapp.utiliti.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListChannelAdapter extends RecyclerView.Adapter<ListChannelAdapter.ListChannelViewHolder> {
    ArrayList<ChannelItem> listItems;
    @NonNull
    @Override
    public ListChannelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_channels_list, parent, false);
        return new ListChannelViewHolder(view);
    }
    public void setData(ArrayList<ChannelItem> listItems) {
        this.listItems = listItems;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ListChannelViewHolder holder, int position) {
        ChannelItem item = listItems.get(position);
        if (item == null) {
            return;
        }
        Picasso.get().load(item.getUrlLogoChannel()).into(holder.civLogoChannel);
        holder.tvTitleChannel.setText(item.getTitleChannel());
        holder.tvVideoCount.setText(Util.convertViewCount(Double.parseDouble(item.getVideoCount())));
        holder.tvSubCount.setText(Util.convertViewCount(Double.parseDouble(item.getSubCount())));
        String idChannel = item.getIdChannel();

        holder.llOpenChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (listItems != null) {
            return listItems.size();
        }
        return 0;
    }

    class ListChannelViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView civLogoChannel;
        private TextView tvTitleChannel, tvSubCount, tvVideoCount;
        private AppCompatButton btSub;
        private LinearLayout llOpenChannel;
        public ListChannelViewHolder(@NonNull View itemView) {
            super(itemView);
            civLogoChannel = itemView.findViewById(R.id.civ_logo_channel_list);
            tvTitleChannel = itemView.findViewById(R.id.tv_title_channel_list);
            tvSubCount = itemView.findViewById(R.id.tv_sub_count_channel_list);
            tvVideoCount = itemView.findViewById(R.id.tv_video_count_channel_list);
            btSub = itemView.findViewById(R.id.bt_subsribe_channel_list);
            llOpenChannel = itemView.findViewById(R.id.ll_open_channel_list);
        }
    }
}
