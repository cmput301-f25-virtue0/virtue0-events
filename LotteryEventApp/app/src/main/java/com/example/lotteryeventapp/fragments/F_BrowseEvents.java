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

import com.example.lotteryeventapp.AllEventsPagination;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.FirestorePagination;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class F_BrowseEvents extends Fragment implements EventAdapter.OnEventClickListener {

    private static final String ARG_ROLE = "role";

    private int role;
    private DataModel model;
    private RecyclerView rv;
    private EventAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private View rootView;

    private String currentOrganizer;

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

        this.rootView = v;

        Log.i("CURRENT ROLE BROWSE", "Current BROWSE user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();
        Organizer currentOrganizer = model.getCurrentOrganizer();
        if (currentOrganizer != null) {
            setupRecyclerView(currentOrganizer.getUid());
        } else {
            // Organizer not loaded yet.
            Log.w("F_BrowseEvents", "Organizer is null. Fetching data again...");
            String deviceId = ((MainActivity) requireActivity()).getDeviceID();
            model.getOrganizer(deviceId, new DataModel.GetCallback() {

                @Override
                public void onSuccess(Object obj) {
                    // Check if Fragment is still attached
                    if (isAdded() && getActivity() != null) {

                        Organizer fetchedOrganizer = (Organizer) obj;

                        model.setCurrentOrganizer(fetchedOrganizer);

                        // Setup page again
                        setupRecyclerView(fetchedOrganizer.getUid());
                    }
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("F_BrowseEvents", "Failed to reload organizer data", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error loading user data.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }


    /**
     * Fetches all events from the DataModel and updates the RecyclerView Adapter.
     * Manages the SwipeRefreshLayout loading indicator.
     */
    private void setupRecyclerView(String organizerUid) {
        // Initialize views
        rv = rootView.findViewById(R.id.rvEvents);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        MaterialToolbar toolbar = rootView.findViewById(R.id.toolbar);

        // Setup RecyclerView
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));


        adapter = new EventAdapter(new ArrayList<>(), role, this, organizerUid);
        rv.setAdapter(adapter);

        // Configure Toolbar visibility and navigation
        if (role == 2) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar.setNavigationOnClickListener(v1 -> {
                ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
            });
        }

        // Configure SwipeRefreshLayout listener
        swipeRefreshLayout.setOnRefreshListener(() -> fetchEvents(true));

        // Initial data fetch
        swipeRefreshLayout.setRefreshing(true);
        fetchEvents(false);
    }

    /**
     * Fetches all events from the DataModel and updates the RecyclerView Adapter.
     */
    private void fetchEvents(boolean forceRefresh) {
        Log.d("F_BrowseEvents", "Fetching events...");

        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(true);
        }


        AllEventsPagination pagination = new AllEventsPagination(12);
        pagination.getNextPage(new FirestorePagination.PaginationCallback() {
            @Override
            public <T> void onGetPage(boolean hasResults, ArrayList<T> obs) {
                ArrayList<Event> eventData = (ArrayList<Event>) obs;
                if (adapter != null) {
                    adapter.setItems(eventData);
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(Exception e) {

            }
        });
//        model.getAllEvents(new DataModel.GetCallback() {
//            @Override
//            public <T extends Enum<T>> void onSuccess(Object obj, T type) { }
//
//            @Override
//            public void onSuccess(Object obj) {
//                ArrayList<Event> eventData = (ArrayList<Event>) obj;
//                Log.d("Browse Events", "Retrieved " + eventData.size() + " events.");
//
//                if (adapter != null) {
//                    adapter.setItems(eventData);
//                    adapter.notifyDataSetChanged();
//                }
//                swipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Log.e("Firebase", "Failed to retrieve events: " + e.getMessage());
//                if (getContext() != null) {
//                    Toast.makeText(getContext(), "Error loading events", Toast.LENGTH_SHORT).show();
//                }
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        }, forceRefresh);
    }

    @Override
    public void onEventClick(@NonNull Event event, int position) {
        model.setCurrentEvent(event);
        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(role));
    }

    @Override
    public void onDeleteClick(Event delEvent, int delPosition) {
        if (getContext() == null) return;

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Event")
                .setMessage("Are you sure you want to delete '" + delEvent.getTitle() + "'? This will remove it from all users.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    performCascadeDelete(delEvent);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Coordinate the deletion: Organizer -> Entrants -> Event Document
     */
    private void performCascadeDelete(Event event) {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        Log.d("CascadeDelete", "Starting deletion for: " + event.getTitle());

        //  Remove from Organizer
        removeEventFromOrganizer(event, () -> {
            // Remove from all Entrants
            removeEventFromEntrants(event, () -> {
                //Finally delete the Event itself
                deleteEventDocument(event);
            });
        });
    }

    private void removeEventFromOrganizer(Event event, Runnable onComplete) {
        String orgId = event.getOrganizer();
        if (orgId == null || orgId.isEmpty()) {
            onComplete.run();
            return;
        }

        model.getOrganizer(orgId, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Organizer org = (Organizer) obj;
                if (org != null && org.getEvents().contains(event.getUid())) {
                    org.getEvents().remove(event.getUid());
                    model.setOrganizer(org, new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String id) {
                            Log.d("CascadeDelete", "Removed event from Organizer.");
                            onComplete.run();
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e("CascadeDelete", "Failed to update Organizer. Continuing anyway.", e);
                            onComplete.run(); // Continue even if this fails to ensure event is deleted
                        }
                    });
                } else {
                    onComplete.run();
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

            @Override
            public void onError(Exception e) {
                Log.e("CascadeDelete", "Failed to fetch Organizer. Continuing.", e);
                onComplete.run();
            }
        });
    }

    private void removeEventFromEntrants(Event event, Runnable onComplete) {
        // Collect all unique Entrant IDs involved in this event
        Set<String> affectedUserIds = new HashSet<>();
        if (event.getWaitlist() != null) affectedUserIds.addAll(event.getWaitlist());
        if (event.getAttendee_list() != null) affectedUserIds.addAll(event.getAttendee_list());
        if (event.getInvited_list() != null) affectedUserIds.addAll(event.getInvited_list());
        if (event.getCancelled_list() != null) affectedUserIds.addAll(event.getCancelled_list());

        if (affectedUserIds.isEmpty()) {
            onComplete.run();
            return;
        }

        Log.d("CascadeDelete", "Cleaning up " + affectedUserIds.size() + " entrants.");

        // Use a counter to track when all async operations are done
        AtomicInteger counter = new AtomicInteger(affectedUserIds.size());

        for (String userId : affectedUserIds) {
            model.getEntrant(userId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant entrant = (Entrant) obj;
                    if (entrant != null) {
                        // Remove from all potential lists in the Entrant object
                        entrant.removeWaitlistedEvent(event.getUid());
                        entrant.removeInvitedEvent(event.getUid());
                        entrant.removeAttendedEvent(event.getUid());

                        // Save updated entrant
                        model.setEntrant(entrant, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                                checkCompletion();
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("CascadeDelete", "Failed to update Entrant " + userId, e);
                                checkCompletion();
                            }
                        });
                    } else {
                        checkCompletion();
                    }
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

                @Override
                public void onError(Exception e) {
                    Log.e("CascadeDelete", "Failed to fetch Entrant " + userId, e);
                    checkCompletion();
                }

                private void checkCompletion() {
                    // Decrement counter. If 0, we are done with everyone.
                    if (counter.decrementAndGet() == 0) {
                        Log.d("CascadeDelete", "All entrants updated.");

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(onComplete);
                        }
                    }
                }
            });
        }
    }

    private void deleteEventDocument(Event event) {
        model.deleteEvent(event, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Event and references deleted", Toast.LENGTH_SHORT).show();
                    fetchEvents(true); // Refresh list
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("DeleteEvent", "Error deleting event doc", e);
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }
}
