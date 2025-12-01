package com.example.lotteryeventapp;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private int role;
    private String currentOrganizerId;
    private String currentEntrantId;

    // Ensure this string EXACTLY matches how you save dates in F_CreateEditEvent
    private static final String EVENT_DATE_FORMAT = "EEE, MMM d, yyyy 'at' h:mm a";

    public interface OnEventClickListener {
        void onEventClick(@NonNull Event event, int position);
        void onDeleteClick(Event delEvent, int delPosition);
    }

    private final List<Event> items = new ArrayList<>();
    private final List<Event> allItems = new ArrayList<>();
    private final OnEventClickListener clickListener;

    public EventAdapter() { this.clickListener = null; }

    public EventAdapter(@NonNull List<Event> initial, int myRole, String myId) {
        items.addAll(initial);
        this.allItems.addAll(initial);
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
        this.allItems.addAll(initial);
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
        allItems.clear();
        allItems.addAll(newItems);
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

        if (e.getImage() == null || e.getImage().isEmpty()) {
            h.ivPoster.setImageResource(R.drawable.lottery);
        } else {
            DataModel model = new DataModel();
            model.getImage(e.getImage(), new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    ImageDataHolder image = (ImageDataHolder) obj;
                    h.ivPoster.setImageBitmap(image.convertToBitmap());
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

                @Override
                public void onError(Exception e) { }
            });
        }

        // Reset Visibility
        h.tvOwnerTag.setVisibility(View.GONE);
        h.tvJoinedTag.setVisibility(View.GONE);

        if (role == 1 && currentOrganizerId != null && e.getOrganizer() != null && currentOrganizerId.equals(e.getOrganizer())) {
            h.tvOwnerTag.setVisibility(View.VISIBLE);
        }

        // Entrant lane
        if (role == 0 && currentEntrantId != null) {
            boolean isWaitlisted = e.getWaitlist() != null && e.getWaitlist().contains(currentEntrantId);
            boolean isAttending = e.getAttendee_list() != null && e.getAttendee_list().contains(currentEntrantId);
            boolean isInvited = e.getInvited_list() != null && e.getInvited_list().contains(currentEntrantId);
            boolean isCancelled = e.getCancelled_list() != null && e.getCancelled_list().contains(currentEntrantId);

            if (isAttending) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("ATTENDING");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#4CAF50")); // Green
            } else if (isInvited) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("INVITED");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#FF9800")); // Orange
            } else if (isWaitlisted) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("WAITLISTED");
                h.tvJoinedTag.setBackgroundColor(Color.GRAY); // Default Gray
            } else if (isCancelled) {
                h.tvJoinedTag.setVisibility(View.VISIBLE);
                h.tvJoinedTag.setText("CANCELLED");
                h.tvJoinedTag.setBackgroundColor(Color.parseColor("#F44336")); // Red
            }
        }

        // Admin lane
        if (role == 2) {
            h.btnDelete.setVisibility(View.VISIBLE);
            h.btnDelete.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onDeleteClick(e, h.getBindingAdapterPosition());
                }
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

    public void applyFilter(Set<Event.EventTag> tags, Date startDate, Date endDate) {
        items.clear();

        // If no filters, show all
        if ((tags == null || tags.isEmpty()) && startDate == null && endDate == null) {
            items.addAll(allItems);
            notifyDataSetChanged();
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(EVENT_DATE_FORMAT, Locale.US);
        sdf.setLenient(true); // Allow flexibility in parsing

        for (Event event : allItems) {
            boolean matchesTags = true;
            boolean matchesDate = true;

            // Check tags
            if (tags != null && !tags.isEmpty()) {
                matchesTags = false;
                if (event.getTags() != null) {
                    for (Event.EventTag t : event.getTags()) {
                        if (tags.contains(t)) {
                            matchesTags = true;
                            break;
                        }
                    }
                }
            }

            // Check date range
            if (startDate != null && endDate != null) {
                if (event.getDate_time() == null) {
                    matchesDate = false;
                } else {
                    try {
                        // Trim removes leading/trailing spaces which often cause parse errors
                        String dateStr = event.getDate_time().trim();
                        Date eventDate = sdf.parse(dateStr);

                        if (eventDate != null) {
                            // Check if eventDate is outside the range
                            if (eventDate.before(startDate) || eventDate.after(endDate)) {
                                matchesDate = false;
                            }
                        }
                    } catch (ParseException e) {
                        // Log the error to debug exact format mismatches
                        Log.e("FilterError", "Failed to parse date: '" + event.getDate_time() + "' Expected format: " + EVENT_DATE_FORMAT);
                        matchesDate = false;
                    }
                }
            }

            if (matchesTags && matchesDate) {
                items.add(event);
            }
        }
        notifyDataSetChanged();
    }

    private static String n(String s) { return s == null ? "" : s; }
}