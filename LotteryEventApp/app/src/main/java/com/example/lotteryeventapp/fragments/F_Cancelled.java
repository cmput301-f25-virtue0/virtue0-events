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
import android.widget.ImageButton;
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
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class F_Cancelled extends Fragment {

    private int role;
    private DataModel model;
    private Event event;
    private RecyclerView rv;
    private TextView tvCount;
    private ProfileListAdapter adapter;
    private Button btnMessageCancelled;
    private final List<Entrant> cancelledEntrants = new ArrayList<>();

    public static F_Cancelled newInstance(int myRole) {
        F_Cancelled fragment = new F_Cancelled();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.cancelled, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();
        btnMessageCancelled = view.findViewById(R.id.btnMessageCancelled);

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> {
                ((MainActivity) requireActivity()).showFragment(F_Applicants.newInstance(role));
            });
        }

        // Initialize Views
        rv = view.findViewById(R.id.rvCancelled);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView tvTitle = view.findViewById(R.id.tvEventName);
        tvCount = view.findViewById(R.id.tvListSize);

        if (event != null) {
            if (tvTitle != null) tvTitle.setText(event.getTitle());
//            fetchCancelledEntrants();
            loadCancelledList();
        } else {
            Log.e("F_Cancelled", "Event is null");
        }
        // Initialize adapter with empty list first
        adapter = new ProfileListAdapter(cancelledEntrants, new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Profile: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), "Action not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);


        btnMessageCancelled.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            new AlertDialog.Builder(requireContext())
                    .setView(input)
                    .setTitle("Message Cancelled List")
                    .setPositiveButton("Send", (dialog, which) -> {
                        String message = input.getText().toString();
                        model.getUsableCancelledEntrants(event,new DataModel.GetCallback() {
                            @Override
                            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                            }
                            @Override
                            public void onSuccess(Object obj) {
                                Log.d("Firebase", "retrieved");
                                ArrayList<Entrant> cancelledEntrants = (ArrayList<Entrant>) obj;
                                for(int i=0;i<cancelledEntrants.size();i++){
                                    Entrant entrant = cancelledEntrants.get(i);
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

                                Toast.makeText(requireContext(), "Message sent to cancelled list", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e("Firebase", "fail");
                            }
                        });

                    }).setNegativeButton("Cancel", null).show();

        });
    }

    private void loadCancelledList() {
        ArrayList<String> cancelledIds = event.getCancelled_list();

        // Update count immediately if possible
        if (tvCount != null) {
            tvCount.setText(String.valueOf(cancelledIds.size()) + " Cancelled");
        }

        // Handle empty list case
        if (cancelledIds == null || cancelledIds.isEmpty()) {
            setupAdapter(new ArrayList<>());
            return;
        }

        // Fetch Entrant objects asynchronously
        model.getEntrantsByIds(cancelledIds, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Entrant> result = (List<Entrant>) obj;

                // Update UI on main thread
                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> setupAdapter(result));
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

            @Override
            public void onError(Exception e) {
                Log.e("F_Cancelled", "Error fetching cancelled list", e);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error loading list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Sets up the RecyclerView adapter with the fetched list of entrants.
     */
    private void setupAdapter(List<Entrant> entrants) {
        // Update count text again after fetch

        TextView myText = getView().findViewById(R.id.tvEventName);
        myText.setText(event.getTitle());


        if (tvCount != null) {
            tvCount.setText(String.valueOf(entrants.size())+" cancelled");
        }

        if (entrants == null) {
            entrants = new ArrayList<>();
            Log.e("F_Cancelled", "Entrants list is null");
        }

        ProfileListAdapter.OnProfileClickListener listener = new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), "Action not implemented for Cancelled list", Toast.LENGTH_SHORT).show();
            }
        };

        adapter = new ProfileListAdapter(entrants, listener);
        rv.setAdapter(adapter);
    }
}