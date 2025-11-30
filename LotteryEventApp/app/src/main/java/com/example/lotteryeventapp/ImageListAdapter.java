package com.example.lotteryeventapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    public interface OnImageClickListener {
        void onImageClick(String imageLabel, int position);
        void onDeleteClick(ImageDataHolder imageLabel, int position);
    }

    private final List<ImageDataHolder> items = new ArrayList<>();
    private final OnImageClickListener clickListener;

    public ImageListAdapter(@NonNull List<ImageDataHolder> initialData, @NonNull OnImageClickListener listener) {
        items.addAll(initialData);
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_image_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder h, int position) {
        ImageDataHolder image = items.get(position);

        h.tvLabel.setText(image.getUid());
        h.ivThumb.setImageBitmap(image.convertToBitmap());

        h.itemView.setOnClickListener(v -> {
            clickListener.onImageClick(image.getUid(), h.getBindingAdapterPosition());
        });

        h.ivDelete.setOnClickListener(v -> {
            clickListener.onDeleteClick(image, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setItems(@NonNull List<ImageDataHolder> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }
    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView tvLabel;
        ImageView ivDelete;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb = itemView.findViewById(R.id.ivThumb);
            tvLabel = itemView.findViewById(R.id.tvLabel);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}