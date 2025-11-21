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
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class F_MyEvents extends Fragment implements EventAdapter.OnEventClickListener {

    private static final String ARG_ROLE = "role";

    private int role;
    private DataModel model;
    private Entrant entrant;
    private Organizer organizer;

    private RecyclerView rv;
    private EventAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static F_MyEvents newInstance(int role) {
        F_MyEvents fragment = new F_MyEvents();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getInt(ARG_ROLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_events_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("CURRENT ROLE MY_EVENTS", "Current MY_EVENTS user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();

        // Initialize user objects based on role
        if (role == 1) { // Organizer
            organizer = model.getCurrentOrganizer();
        } else { // Entrant
            entrant = model.getCurrentEntrant();
        }

        // Initialize views
        rv = view.findViewById(R.id.rvEvents);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        // Setup RecyclerView and Adapter
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new EventAdapter(new ArrayList<>(), role, this);
        rv.setAdapter(adapter);

        // Configure Toolbar visibility
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }

        // Configure SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(() -> fetchEvents(true));

        // Initial data fetch
        swipeRefreshLayout.setRefreshing(true);
        fetchEvents(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh data when returning to the fragment
        fetchEvents(false);
    }

    private void fetchEvents(boolean forceRefresh) {
        Log.d("F_MyEvents", "Fetching events for role: " + role);

        if (role == 1) {
            fetchOrganizerEvents();
        } else {
            fetchEntrantEvents();
        }
    }

    /**
     * Fetches events created by the current Organizer.
     */
    private void fetchOrganizerEvents() {
        // Ensure organizer is loaded
        if (organizer == null) {
            organizer = model.getCurrentOrganizer();
            if (organizer == null) {
                Log.e("F_MyEvents", "Current Organizer is null");
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
        }

        ArrayList<String> createdEventIds = organizer.getEvents(); // Get list of created event IDs

        // If no created events, clear list and stop refreshing
        if (createdEventIds == null || createdEventIds.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        fetchEventsFromIds(createdEventIds);
    }

    /**
     * Fetches waitlisted events for the current Entrant.
     */
    private void fetchEntrantEvents() {
        // Ensure entrant is loaded
        if (entrant == null) {
            entrant = model.getCurrentEntrant();
            if (entrant == null) {
                Log.e("F_MyEvents", "Current Entrant is null");
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
        }

        ArrayList<String> waitlistedIds = entrant.getWaitlistedEvents(); // Get list of waitlisted IDs

        // If no waitlisted events, clear list and stop refreshing
        if (waitlistedIds == null || waitlistedIds.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        fetchEventsFromIds(waitlistedIds);
    }

    /**
     * Shared helper method to fetch Event objects from a list of IDs.
     */
    private void fetchEventsFromIds(ArrayList<String> eventIds) {
        ArrayList<Event> fetchedEvents = new ArrayList<>();
        AtomicInteger activeFetches = new AtomicInteger(eventIds.size());

        for (String eventId : eventIds) {
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (obj instanceof Event) {
                        synchronized (fetchedEvents) {
                            fetchedEvents.add((Event) obj);
                        }
                    }
                    checkCompletion();
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

                @Override
                public void onError(Exception e) {
                    Log.e("F_MyEvents", "Error fetching event ID: " + eventId);
                    checkCompletion();
                }

                private void checkCompletion() {
                    if (activeFetches.decrementAndGet() == 0) {
                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                Log.d("F_MyEvents", "Retrieved " + fetchedEvents.size() + " events.");
                                adapter.setItems(fetchedEvents);
                                swipeRefreshLayout.setRefreshing(false);
                            });
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onEventClick(@NonNull Event event, int position) {
        // Navigate to event details
        model.setCurrentEvent(event);
        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(role));
    }

    @Override
    public void onDeleteClick(Event delEvent, int delPosition) {
        if (role == 1) {
            // Organizer Deletion Logic
            new AlertDialog.Builder(requireContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete '" + delEvent.getTitle() + "'?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        swipeRefreshLayout.setRefreshing(true);
                        model.deleteEvent(delEvent, new DataModel.DeleteCallback() {
                            @Override
                            public void onSuccess() {
                                if (isAdded() && getContext() != null) {
                                    Toast.makeText(getContext(), "Event deleted", Toast.LENGTH_SHORT).show();

                                    // Update local organizer list and refresh
                                    if (organizer != null) {
                                        fetchEvents(true);
                                    }
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                if (isAdded() && getContext() != null) {
                                    Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            Toast.makeText(getContext(), "Entrants cannot delete events.", Toast.LENGTH_SHORT).show();
        }
    }
}
