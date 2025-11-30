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

public class OrganizerListAdapter extends RecyclerView.Adapter<OrganizerListAdapter.OrganizerViewHolder> {
    public interface OnOrganizerClickListener {
        void onProfileClick(Organizer organizer, int position);
        void onDeleteClick(Organizer organizer, int position);
    }

    private final List<Organizer> items = new ArrayList<>();
    private final OnOrganizerClickListener clickListener;

    /**
     * Constructor for the adapter.
     * @param initialData The initial list of Entrants to display.
     * @param listener A listener to handle item clicks and delete clicks.
     */
    public OrganizerListAdapter(@NonNull List<Organizer> initialData, @NonNull OnOrganizerClickListener listener) {
        items.addAll(initialData);
        this.clickListener = listener;
    }

    /**
     * Updates the list with new data.
     * @param newItems The new list of Entrants.
     */
    public void setItems(@NonNull List<Organizer> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged(); // In a real app, use DiffUtil for better performance
    }

    @NonNull
    @Override
    public OrganizerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_profile_item, parent, false);
        return new OrganizerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrganizerViewHolder h, int position) {
        Organizer organizer = items.get(position);
        h.tvName.setText(organizer.getUid());


        // Set click listeners
        h.itemView.setOnClickListener(v -> {
            clickListener.onProfileClick(organizer, h.getBindingAdapterPosition());
        });

        h.ivDelete.setOnClickListener(v -> {
            clickListener.onDeleteClick(organizer, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder for the profile item.
     */
    static class OrganizerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivDelete;

        OrganizerViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views from admin_profile_item.xml
            tvName = itemView.findViewById(R.id.tvName);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
