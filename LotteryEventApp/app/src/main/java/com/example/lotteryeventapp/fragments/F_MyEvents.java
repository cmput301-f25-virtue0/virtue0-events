package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.provider.Settings;
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
import java.util.HashSet;
import java.util.Set;
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

    private View rootView;

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
        this.rootView = view;

        Log.i("CURRENT ROLE MY_EVENTS", "Current MY_EVENTS user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();


        if (role == 1) {
            // ORGANIZER ROLE
            if (model.getCurrentOrganizer() != null) {
                this.organizer = model.getCurrentOrganizer();
                setupUI(organizer.getUid());
            } else {
                Log.w("F_MyEvents", "Organizer null on rotation. Refetching...");
                String deviceId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                model.getOrganizer(deviceId, new DataModel.GetCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (isAdded() && getActivity() != null) {
                            organizer = (Organizer) obj;
                            model.setCurrentOrganizer(organizer);
                            setupUI(organizer.getUid());
                        }
                    }
                    @Override
                    public <T extends Enum<T>> void onSuccess(Object obj, T type) {}
                    @Override
                    public void onError(Exception e) {
                        Log.e("F_MyEvents", "Failed to reload organizer", e);
                    }
                });
            }
        } else {
            // ENTRANT ROLE
            if (model.getCurrentEntrant() != null) {
                this.entrant = model.getCurrentEntrant();
                setupUI(entrant.getUid());
            } else {
                Log.w("F_MyEvents", "Entrant null on rotation. Refetching...");
                String deviceId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);


                model.getEntrant(deviceId, new DataModel.GetCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                        if (isAdded() && getActivity() != null) {
                            entrant = (Entrant) obj;
                            model.setCurrentEntrant(entrant);
                            setupUI(entrant.getUid());
                        }
                    }
                    @Override
                    public <T extends Enum<T>> void onSuccess(Object obj, T type) {}
                    @Override
                    public void onError(Exception e) {
                        Log.e("F_MyEvents", "Failed to reload entrant", e);
                    }
                });
            }
        }
    }

    /**
     * Helper method to initialize the RecyclerView and Adapter.
     * Only called once we have a valid User ID.
     */
    private void setupUI(String userId) {
        if (rootView == null) return;

        // Initialize views
        rv = rootView.findViewById(R.id.rvEvents);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        MaterialToolbar toolbar = rootView.findViewById(R.id.toolbar);

        // Setup RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set adapter
        adapter = new EventAdapter(new ArrayList<>(), role, this, userId);
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

        if (adapter != null) {
            fetchEvents(false);
        }
    }

    private void fetchEvents(boolean forceRefresh) {
        Log.d("F_MyEvents", "Fetching events for role: " + role);


        if (swipeRefreshLayout == null) return;

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
        }

        if (organizer == null) {
            Log.e("F_MyEvents", "Current Organizer is null");
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        ArrayList<String> createdEventIds = organizer.getEvents(); // Get list of created event IDs

        // If no created events, clear list and stop refreshing
        if (createdEventIds == null || createdEventIds.isEmpty()) {
            if (adapter != null) adapter.setItems(new ArrayList<>());
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
        Entrant current = model.getCurrentEntrant();
        if (current == null) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }
        String entrantId = current.getUid();

        // reload from Firestore to ensure lists are up to date
        model.getEntrant(entrantId, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                if (obj instanceof Entrant) {
                    // Update local reference
                    entrant = (Entrant) obj;
                    model.setCurrentEntrant(entrant);

                    // combine the list from the 'fresh' object
                    Set<String> allEventIds = new HashSet<>();

                    if (entrant.getWaitlistedEvents() != null) allEventIds.addAll(entrant.getWaitlistedEvents());
                    if (entrant.getInvitedEvents() != null) allEventIds.addAll(entrant.getInvitedEvents());
                    if (entrant.getAttendedEvents() != null) allEventIds.addAll(entrant.getAttendedEvents());

                    ArrayList<String> combinedIds = new ArrayList<>(allEventIds);

                    if (combinedIds.isEmpty()) {
                        if (isAdded() && adapter != null) {
                            adapter.setItems(new ArrayList<>());
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        return;
                    }

                    // fetch the actual event objects
                    fetchEventsFromIds(combinedIds);
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

            @Override
            public void onError(Exception e) {
                Log.e("F_MyEvents", "Failed to refresh entrant profile", e);
                if (isAdded()) swipeRefreshLayout.setRefreshing(false);
            }
        });
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
                                if (adapter != null) {
                                    adapter.setItems(fetchedEvents);
                                    adapter.notifyDataSetChanged();
                                }
                                if (swipeRefreshLayout != null) {
                                    swipeRefreshLayout.setRefreshing(false);
                                }
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