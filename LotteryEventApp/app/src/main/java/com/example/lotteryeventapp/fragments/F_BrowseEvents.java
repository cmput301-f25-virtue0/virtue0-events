package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class F_BrowseEvents extends Fragment implements EventAdapter.OnEventClickListener {

    private static final String ARG_ROLE = "role";

    private int role;
    private DataModel model;
    private RecyclerView rv;
    private EventAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static F_BrowseEvents newInstance(int role) {
        F_BrowseEvents fragment = new F_BrowseEvents();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getInt(ARG_ROLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        // Inflates the layout containing the SwipeRefreshLayout and RecyclerView
        return i.inflate(R.layout.fragment_events_list, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        super.onViewCreated(v, b);

        Log.i("CURRENT ROLE BROWSE", "Current BROWSE user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();

        // Initialize views
        rv = v.findViewById(R.id.rvEvents);
        swipeRefreshLayout = v.findViewById(R.id.swipeRefreshLayout); // Initialize the SwipeRefreshLayout
        MaterialToolbar toolbar = v.findViewById(R.id.toolbar);

        // Setup RecyclerView and Adapter
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter with an empty list initially
        adapter = new EventAdapter(new ArrayList<>(), role, this);
        rv.setAdapter(adapter);

        // Configure Toolbar visibility and navigation
        if (role != 2) {
            toolbar.setVisibility(View.GONE);
        } else {
            toolbar.setNavigationOnClickListener(v1 -> {
                ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
            });
        }

        // Configure SwipeRefreshLayout listener
        // When the user swipes down, call fetchEvents()
        swipeRefreshLayout.setOnRefreshListener(() -> fetchEvents(true));

        // Initial data fetch
        // Set refreshing to true to show the indicator immediately on load
        swipeRefreshLayout.setRefreshing(true);
        fetchEvents(false);
    }


    /**
     * Fetches all events from the DataModel and updates the RecyclerView Adapter.
     * Manages the SwipeRefreshLayout loading indicator.
     */
    private void fetchEvents(boolean forceRefresh) {
        Log.d("F_BrowseEvents", "Fetching events...");

        // The loading indicator is visible while fetching
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }

        model.getAllEvents(new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
            }

            @Override
            public void onSuccess(Object obj) {
                // Data retrieved successfully
                ArrayList<Event> eventData = (ArrayList<Event>) obj;

                Log.d("Browse Events", "Retrieved " + eventData.size() + " events.");


                // Update the adapter with new data and notify the RecyclerView
                if (adapter != null) {
                    adapter.setItems(eventData);
                }

                // Stop the refreshing indicator
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "Failed to retrieve events: " + e.getMessage());
                Toast.makeText(requireContext(), "Error loading events", Toast.LENGTH_SHORT).show();

                // Stop the refreshing indicator even if there was an error
                swipeRefreshLayout.setRefreshing(false);
            }
        }, forceRefresh);
    }

    @Override
    public void onEventClick(@NonNull Event event, int position) {
        // Navigate to event details when card is clicked
        model.setCurrentEvent(event);
        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(role));
    }

    @Override
    public void onDeleteClick(Event delEvent, int delPosition) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete '" + delEvent.getTitle() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(true);
                    }

                    model.deleteEvent(delEvent, new DataModel.DeleteCallback() {
                        @Override
                        public void onSuccess() {
                            // CRITICAL FIX: Check context before showing Toast
                            if (isAdded() && getContext() != null) {
                                Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();
                                fetchEvents(true);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("DeleteEvent", "Error deleting event", e);
                            // CRITICAL FIX: Check context before showing Toast
                            if (isAdded() && getContext() != null) {
                                Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}