package com.example.lotteryeventapp;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying a list of Notification objects.
 * Handles both Invitation and Rejection types through the abstract Notification class.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> items = new ArrayList<>();
    private final OnNotificationClickListener clickListener;

    /**
     * Interface for handling clicks on a notification item.
     */
    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification, int position);
    }

    /**
     * ArrayListAdapter for notifications
     * @param initial initial data for list of notifications
     * @param listener click listener of notification
     */
    public NotificationAdapter(@NonNull List<Notification> initial, @NonNull OnNotificationClickListener listener) {
        items.addAll(initial);
        this.clickListener = listener;
    }

    /**
     * Updates the data in the adapter.
     */
    public void setItems(@NonNull List<Notification> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(v, clickListener, items);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = items.get(position);

        holder.tvMessage.setText(notification.getMessage());

        // Set the Title and Color based on Type
        if (notification instanceof Invitation) {
            holder.tvTitle.setText("Invitation");
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_green_dark));
        } else if (notification instanceof Rejection) {
            holder.tvTitle.setText("Rejection");
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.holo_red_dark));
        } else {
            holder.tvTitle.setText("Notification");
            holder.tvTitle.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.black));
        }

        // Set Timestamp (Placeholder for now, as Notification class doesn't have a timestamp field)
        holder.tvTimestamp.setText("Just now");

        // Style unread notifications (Bold text vs Normal)
        if (!notification.isRead()) {
            holder.tvTitle.setTypeface(null, Typeface.BOLD);
            holder.tvMessage.setTypeface(null, Typeface.BOLD);
        } else {
            holder.tvTitle.setTypeface(null, Typeface.NORMAL);
            holder.tvMessage.setTypeface(null, Typeface.NORMAL);
        }
    }

    /**
     * get number of notifications
     * @return number of notifications
     */
    @Override
    public int getItemCount() {
        return items.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvMessage;
        TextView tvTimestamp;

        NotificationViewHolder(@NonNull View itemView, OnNotificationClickListener listener, List<Notification> dataRef) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvNotifTitle);
            tvMessage = itemView.findViewById(R.id.tvNotifMessage);
            tvTimestamp = itemView.findViewById(R.id.tvNotifTimestamp);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        listener.onNotificationClick(dataRef.get(pos), pos);
                    }
                }
            });
        }
    }
}