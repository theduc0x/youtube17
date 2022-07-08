package com.example.youtubeapp.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.youtubeapp.R;
import com.example.youtubeapp.model.itemrecycleview.CategoryItem;
import com.example.youtubeapp.my_interface.IItemOnClickCategoryListener;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CateViewHolder> {
    IItemOnClickCategoryListener onClickCategoryListener;
    ArrayList<CategoryItem> listCate;

    public CategoryAdapter(IItemOnClickCategoryListener onClickCategoryListener) {
        this.onClickCategoryListener = onClickCategoryListener;
    }

    public void setData(ArrayList<CategoryItem> listCate) {
        this.listCate = listCate;
    }
    @NonNull
    @Override
    public CateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new CateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CateViewHolder holder, int position) {
        CategoryItem cate = listCate.get(position);
        if (cate == null) {
            return;
        }
        holder.setData(cate);
        String idCate = cate.getIdCategory();
        holder.btCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCategoryListener.onClickCategory(idCate);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (listCate != null) {
            return listCate.size();
        }
        return 0;
    }

    class CateViewHolder extends RecyclerView.ViewHolder {
        private AppCompatButton btCate;
        public CateViewHolder(@NonNull View itemView) {
            super(itemView);
            btCate = itemView.findViewById(R.id.bt_category);
        }
        public void setData(CategoryItem cate) {
            btCate.setText(cate.getTitleCate());
        }
    }
}
