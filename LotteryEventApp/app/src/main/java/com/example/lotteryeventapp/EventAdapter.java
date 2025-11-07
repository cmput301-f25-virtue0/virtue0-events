// EventAdapter.java
package com.example.lotteryeventapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private int role;

    public interface OnEventClickListener {
        void onEventClick(@NonNull Event event, int position);
        void onDeleteClick(Event delEvent, int delPosition);
    }

    private final List<Event> items = new ArrayList<>();
    private final OnEventClickListener clickListener;

    public EventAdapter() { this.clickListener = null; }

    public EventAdapter(@NonNull List<Event> initial, int myRole) {
        items.addAll(initial);
        this.clickListener = null;
        this.role = myRole;
    }

    public EventAdapter(@NonNull List<Event> initial, int role, @NonNull OnEventClickListener listener) {
        items.addAll(initial);
        this.clickListener = listener;
        this.role = role;
    }

    public void setItems(@NonNull List<Event> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        return new EventViewHolder(v, clickListener, items);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder h, int position) {
        Event e = items.get(position);
        h.tvTitle.setText(n(e.getTitle()));
        h.tvLocation.setText(n(e.getLocation()).toUpperCase());
        h.tvDate.setText(n(e.getDate_time()));
        h.ivPoster.setImageResource(R.drawable.lottery); // placeholder
        if (role == 2) { // 2 = Admin
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnDelete.setOnClickListener(v -> {
                clickListener.onDeleteClick(e, h.getBindingAdapterPosition());
                DataModel dm = new DataModel();
                dm.deleteEvent(e);

            });
        } else {
            h.btnDelete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() { return items.size(); }

    /* ---------- ViewHolder ---------- */
    static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPoster;
        TextView tvTitle, tvLocation, tvDate;

        Button btnDelete;




        EventViewHolder(@NonNull View itemView,
                        @NonNull OnEventClickListener listener,
                        @NonNull List<Event> dataRef) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle  = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate   = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);



            itemView.setOnClickListener(v -> {
                if (listener == null) return;
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    listener.onEventClick(dataRef.get(pos), pos);
                }
            });
        }
    }

    private static String n(String s) { return s == null ? "" : s; }
}


