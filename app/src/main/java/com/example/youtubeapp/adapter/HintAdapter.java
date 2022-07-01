package com.example.youtubeapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.my_interface.IItemOnClickHintListener;

import java.util.ArrayList;

public class HintAdapter extends RecyclerView.Adapter<HintAdapter.HintViewHolder> {
    ArrayList<String> listHint;
    IItemOnClickHintListener onClickHintListener;

    public HintAdapter(IItemOnClickHintListener onClickHintListener) {
        this.onClickHintListener = onClickHintListener;
    }

    public void setData(ArrayList<String> listHint) {
        this.listHint = listHint;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public HintViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_hint, parent, false);
        return new HintViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HintViewHolder holder, int position) {
        String hint = listHint.get(position);
        if (hint == null) {
            return;
        }

        holder.tvHintSearch.setText(hint);
        holder.ibGetHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickHintListener.onClickListener(hint);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listHint != null) {
            return listHint.size();
        }
        return 0;
    }

    class HintViewHolder extends RecyclerView.ViewHolder {
        TextView tvHintSearch;
        ImageView ibGetHint;
        public HintViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHintSearch = itemView.findViewById(R.id.tv_hint_search);
            ibGetHint = itemView.findViewById(R.id.ib_get_hint);
        }
    }
}
