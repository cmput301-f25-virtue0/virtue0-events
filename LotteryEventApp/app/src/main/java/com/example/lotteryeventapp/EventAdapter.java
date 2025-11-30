// EventAdapter.java
package com.example.lotteryeventapp;

import android.graphics.Color;
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
    private String currentOrganizerId;

    private String currentEntrantId;


    public interface OnEventClickListener {
        void onEventClick(@NonNull Event event, int position);
        void onDeleteClick(Event delEvent, int delPosition);
    }

    private final List<Event> items = new ArrayList<>();
    private final OnEventClickListener clickListener;

    public EventAdapter() { this.clickListener = null; }


    public EventAdapter(@NonNull List<Event> initial, int myRole, String myId) {
        items.addAll(initial);
        this.clickListener = null;
        this.role = myRole;

        if (role == 1) {
            this.currentOrganizerId = myId;
        } else if (role == 0) {
            this.currentEntrantId = myId;
        }
    }


    public EventAdapter(@NonNull List<Event> initial, int role, @NonNull OnEventClickListener listener, String myId) {
        items.addAll(initial);
        this.clickListener = listener;
        this.role = role;

        if (role == 1) {
            this.currentOrganizerId = myId;
        } else if (role == 0) {
            this.currentEntrantId = myId;
        }
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
        if(e.getImage()==null){
            h.ivPoster.setImageResource(R.drawable.lottery);
        }else if(e.getImage().isEmpty()){
            h.ivPoster.setImageResource(R.drawable.lottery);
        }else{
            DataModel model = new DataModel();
            model.getImage(e.getImage(), new DataModel.GetCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            ImageDataHolder image = (ImageDataHolder) obj;
                            h.ivPoster.setImageBitmap(image.convertToBitmap());

                        }

                        @Override
                        public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        }


        // Reset Visibility
        h.tvOwnerTag.setVisibility(View.GONE);
        h.tvJoinedTag.setVisibility(View.GONE);

        if (role == 1 && currentOrganizerId != null && e.getOrganizer() != null && currentOrganizerId.equals(e.getOrganizer())) {
            h.tvOwnerTag.setVisibility(View.VISIBLE);
        }

        //entrant lane
        if (role == 0 && currentEntrantId != null) {
            boolean isWaitlisted = e.getWaitlist() != null && e.getWaitlist().contains(currentEntrantId);
            boolean isAttending = e.getAttendee_list() != null && e.getAttendee_list().contains(currentEntrantId);
            boolean isInvited = e.getInvited_list() != null && e.getInvited_list().contains(currentEntrantId);
            boolean isCancelled = e.getCancelled_list() != null && e.getCancelled_list().contains(currentEntrantId);


            if (isAttending) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("ATTENDING");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
            }
            else if (isInvited) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("INVITED");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
            }
            else if (isWaitlisted) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("WAITLISTED");
                h.tvJoinedTag.setBackgroundColor(Color.GRAY); // Default Gray
            }
            else if (isCancelled) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("CANCELLED");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#F44336")); // Red
            }
        }

        //admin lane
        if (role == 2) {
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnDelete.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onDeleteClick(e, h.getBindingAdapterPosition());
                }
            });
                }
         else {
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

        TextView tvOwnerTag;

        TextView tvJoinedTag;


        EventViewHolder(@NonNull View itemView,
                        @NonNull OnEventClickListener listener,
                        @NonNull List<Event> dataRef) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            tvTitle  = itemView.findViewById(R.id.tvTitle);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDate   = itemView.findViewById(R.id.tvDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            tvOwnerTag = itemView.findViewById(R.id.tvOwnerTag);
            tvJoinedTag = itemView.findViewById(R.id.tvTag);



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


