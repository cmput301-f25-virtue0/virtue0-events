package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class F_MyEvents extends Fragment implements EventAdapter.OnEventClickListener {

    private static final String ARG_ROLE = "role";

    private int role;
    private DataModel model;
    private Entrant entrant;

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
        // Inflates the layout containing the SwipeRefreshLayout and RecyclerView
        return inflater.inflate(R.layout.fragment_events_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("CURRENT ROLE MY_EVENTS", "Current MY_EVENTS user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();
        entrant = model.getCurrentEntrant();

        // Initialize views
        rv = view.findViewById(R.id.rvEvents);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);

        // Setup RecyclerView and Adapter
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize adapter with an empty list initially
        adapter = new EventAdapter(new ArrayList<>(), role, this);
        rv.setAdapter(adapter);

        // Configure Toolbar visibility (Hide for Entrants as they likely have a main toolbar)
        if (toolbar != null) {
            toolbar.setVisibility(View.GONE);
        }

        // Configure SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(() -> fetchEvents(true));

        // Initial data fetch
        swipeRefreshLayout.setRefreshing(true);
        fetchEvents(false);
    }

    /**
     * Fetches waitlisted events for the current Entrant.
     * Uses AtomicInteger to track when all individual event fetches are complete.
     */
    private void fetchEvents(boolean forceRefresh) {
        Log.d("F_MyEvents", "Fetching waitlisted events...");

        // Ensure entrant is loaded
        if (entrant == null) {
            entrant = model.getCurrentEntrant();
            if (entrant == null) {
                Log.e("F_MyEvents", "Current Entrant is null");
                swipeRefreshLayout.setRefreshing(false);
                return;
            }
        }

        ArrayList<String> waitlistedIds = entrant.getWaitlistedEvents();

        // If no waitlisted events, clear list and stop refreshing
        if (waitlistedIds == null || waitlistedIds.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        ArrayList<Event> fetchedEvents = new ArrayList<>();
        // Counter to track when all async calls are finished
        AtomicInteger activeFetches = new AtomicInteger(waitlistedIds.size());

        for (String eventId : waitlistedIds) {
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (obj instanceof Event) {
                        // Synchronized block to safely add to list from multiple callbacks
                        synchronized (fetchedEvents) {
                            fetchedEvents.add((Event) obj);
                        }
                    }
                    checkCompletion();
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                    // Not used for getEvent
                }

                @Override
                public void onError(Exception e) {
                    Log.e("F_MyEvents", "Error fetching event ID: " + eventId);
                    checkCompletion();
                }

                // Helper to check if this was the last request
                private void checkCompletion() {
                    if (activeFetches.decrementAndGet() == 0) {
                        // Run UI updates on main thread
                        if (isAdded() && getActivity() != null) {
                            requireActivity().runOnUiThread(() -> {
                                Log.d("F_MyEvents", "Retrieved " + fetchedEvents.size() + " waitlisted events.");
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
        // Entrants cannot delete events from the database.
        // If this was "Leave Waitlist", logic would go here.
        // For safety, we do nothing or show a message.
        Toast.makeText(getContext(), "Entrants cannot delete events.", Toast.LENGTH_SHORT).show();
    }
}