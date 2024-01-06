package com.rgzn.heritage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class HeritageItemAdapter extends RecyclerView.Adapter<HeritageItemAdapter.HeritageItemViewHolder> {
    private final List<HeritageItem> heritageItemList;
    private OnItemClickListener listener;

    HeritageItemAdapter(List<HeritageItem> heritageItemList, OnItemClickListener listener) {
        this.heritageItemList = heritageItemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeritageItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_heritage, parent, false);
        return new HeritageItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeritageItemViewHolder holder, int position) {
        HeritageItem item = heritageItemList.get(position);
        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        holder.image.setImageResource(item.getImageResId());

        // 设置点击监听器
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return heritageItemList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(HeritageItem item);
    }

    static class HeritageItemViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        ImageView image;

        HeritageItemViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.heritageItemTitle);
            description = itemView.findViewById(R.id.heritageItemDescription);
            image = itemView.findViewById(R.id.heritageItemImage);
        }
    }
}

