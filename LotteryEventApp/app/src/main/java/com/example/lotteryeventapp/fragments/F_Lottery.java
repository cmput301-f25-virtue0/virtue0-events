package com.example.lotteryeventapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Messaging;
import com.example.lotteryeventapp.Notification;
import com.example.lotteryeventapp.NotificationAdapter;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment that shows the current event's lottery waitlist.
 * Uses ProfileListAdapter to display Entrant profiles.
 */
public class F_Lottery extends Fragment {

    private static final String ARG_ROLE = "role";
    private static final String TAG = "F_Lottery";

    private int role;
    private DataModel model;
    private Event event;

    private RecyclerView rv;
    private TextView tvEventName;
    private TextView tvWaitlistSize;
    private Button btnMessageWaitlist;
    private ProfileListAdapter adapter;

    public static F_Lottery newInstance(int role) {
        F_Lottery fragment = new F_Lottery();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt(ARG_ROLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.lottery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        model = ((MainActivity) requireActivity()).getDataModel();
        Event cachedEvent = model.getCurrentEvent();
        String eventId = cachedEvent != null ? cachedEvent.getUid() : null;
        btnMessageWaitlist = view.findViewById(R.id.btnMessageWaitlist);


        tvEventName = view.findViewById(R.id.tvEventName);
        tvWaitlistSize = view.findViewById(R.id.tvWaitlistSize);
        rv = view.findViewById(R.id.rvEntrants);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);


        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(new F_Applicants()));
        }

        // Draw button: run the lottery on the current event
        View btnDraw = view.findViewById(R.id.btnDraw);
        if (btnDraw != null) {
            btnDraw.setOnClickListener(v -> {
                if (event != null) {
                    try {
                        event.doLottery();
                        Toast.makeText(getContext(), "Lottery Processed", Toast.LENGTH_SHORT).show();
                        // Reload event from Firestore and update lists
                        refreshEventAndLoadList(event.getUid());
                    } catch (Exception e) {
                        Log.e(TAG, "Error executing lottery", e);
                    }
                }
            });
        }

        btnMessageWaitlist.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(requireContext())
                    .setView(input)
                    .setTitle("Message Waitlist")
                    .setPositiveButton("Send", (dialog, which) -> {
                        String message = input.getText().toString();
                        model.getUsableWaitlistEntrants(event,new DataModel.GetCallback() {
                            @Override
                            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                            }
                            @Override
                            public void onSuccess(Object obj) {
                                Log.d("Firebase", "retrieved");
                                ArrayList<Entrant> waitlistEntrants = (ArrayList<Entrant>) obj;
                                for(int i=0;i<waitlistEntrants.size();i++){
                                    Entrant entrant = waitlistEntrants.get(i);
                                    if(!entrant.isNotificationOptOut()) {
                                        Messaging messaging = new Messaging(event.getUid(), entrant.getUid(), message);
                                        model.setNotification(messaging, new DataModel.SetCallback() {
                                            @Override
                                            public void onSuccess(String msg) {
                                                Log.d("Firebase", "written");
                                                entrant.addNotification(messaging.getUid());
                                                model.setEntrant(entrant, new DataModel.SetCallback() {
                                                    @Override
                                                    public void onSuccess(String msg) {
                                                        Log.d("Firebase", "written");
                                                    }

                                                    @Override
                                                    public void onError(Exception e) {
                                                        Log.e("Firebase", "fail");
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Log.e("Firebase", "fail");
                                            }
                                        });
                                    }

                                }

                                Toast.makeText(requireContext(), "Message sent to waitlist", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Firebase", "fail");
                            }
                        });

                    }).setNegativeButton("Cancel", null).show();

        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProfileListAdapter(
                new ArrayList<>(),
                new ProfileListAdapter.OnProfileClickListener() {
                    @Override
                    public void onProfileClick(Entrant entrant, int position) {
                        if (getContext() != null && entrant.getProfile() != null) {
                            Toast.makeText(
                                    requireContext(),
                                    "Profile: " + entrant.getProfile().getName(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onDeleteClick(Entrant entrant, int position) {

                    }
                }
        );
        rv.setAdapter(adapter);

        // If we already have a cached event, show its title immediately
        if (cachedEvent != null) {
            tvEventName.setText(cachedEvent.getTitle());
        }

        // Then refresh from Firestore so we have the latest waitlist
        if (eventId != null) {
            refreshEventAndLoadList(eventId);
        } else {
            Log.e(TAG, "Current event ID is null");
        }
    }

    /**
     * Reloads the Event object from Firestore and then loads its waitlist.
     */
    private void refreshEventAndLoadList(@NonNull String eventId) {
        model.getEvent(eventId, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Event freshEvent = (Event) obj;
                event = freshEvent;
                model.setCurrentEvent(freshEvent);

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(
                            () -> tvEventName.setText(event.getTitle())
                    );
                }
                loadWaitlist();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                // Not used in this fragment
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed to refresh event", e);
            }
        });
    }

    /**
     * Uses the current Event to fetch Entrant profiles for the waitlist.
     */
    private void loadWaitlist() {
        if (event == null) return;

        ArrayList<String> waitlistIds = event.getWaitlist();

        // Update header text immediately
        if (isAdded() && getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (tvWaitlistSize != null) {
                    int count = (waitlistIds != null) ? waitlistIds.size() : 0;
                    tvWaitlistSize.setText(
                            "(" + event.getTitle() + " • " + count + " in waitlist)"
                    );
                }
            });
        }

        if (waitlistIds == null || waitlistIds.isEmpty()) {
            // Clear the adapter if there is nobody on the waitlist
            if (adapter != null) {
                adapter.setItems(new ArrayList<>());
            }
            return;
        }

        model.getEntrantsByIds(waitlistIds, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                @SuppressWarnings("unchecked")
                List<Entrant> result = (List<Entrant>) obj;

                if (isAdded() && getActivity() != null && adapter != null) {
                    getActivity().runOnUiThread(() -> {

                        adapter.setItems(result);
                        Log.d(TAG, "Displayed " + result.size() + " entrants.");
                    });
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                // Not used here
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Failed to load waitlist", e);
            }
        });
    }
}
