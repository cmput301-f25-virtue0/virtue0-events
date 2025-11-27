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

/**
 * Adapter to display a list of Entrant profiles for the admin.
 */
public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ProfileViewHolder> {

    // Click listener interface
    public interface OnProfileClickListener {
        void onProfileClick(Entrant entrant, int position);
        void onDeleteClick(Entrant entrant, int position);
    }

    private final List<Entrant> items = new ArrayList<>();
    private final OnProfileClickListener clickListener;

    /**
     * Constructor for the adapter.
     * @param initialData The initial list of Entrants to display.
     * @param listener A listener to handle item clicks and delete clicks.
     */
    public ProfileListAdapter(@NonNull List<Entrant> initialData, @NonNull OnProfileClickListener listener) {
        items.addAll(initialData);
        this.clickListener = listener;
    }

    /**
     * Updates the list with new data.
     * @param newItems The new list of Entrants.
     */
    public void setItems(@NonNull List<Entrant> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged(); // In a real app, use DiffUtil for better performance
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_profile_item, parent, false);
        return new ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder h, int position) {
        Entrant entrant = items.get(position);
        Entrant.Profile profile = entrant.getProfile();

        if (profile != null) {
            // Set the entrant's name (and ID for debugging)

            String displayText = profile.getName() + " (ID: " + entrant.getUid().substring(0, 6) + "...)";
            h.tvName.setText(displayText);
        } else {
            // Fallback text if the profile is somehow null
            h.tvName.setText("Invalid Profile (ID: " + entrant.getUid().substring(0, 6) + "...)");
        }

        // Set click listeners
        h.itemView.setOnClickListener(v -> {
            clickListener.onProfileClick(entrant, h.getBindingAdapterPosition());
        });

        h.ivDelete.setOnClickListener(v -> {
            clickListener.onDeleteClick(entrant, h.getBindingAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * ViewHolder for the profile item.
     */
    static class ProfileViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView ivDelete;

        ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find views from admin_profile_item.xml
            tvName = itemView.findViewById(R.id.tvName);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}