// F_Enrolled.java
package com.example.lotteryeventapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;


public class F_Enrolled extends Fragment {

    private int role;
    private DataModel model;
    private Event event;

    public static F_Enrolled newInstance(int role) {
        F_Enrolled fragment = new F_Enrolled();
        Bundle args = new Bundle();
        args.putInt("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    private ProfileListAdapter.OnProfileClickListener profileListener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.enrolled, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();
        toolbar.setNavigationOnClickListener(v -> {
            // Go back to the Applicants screen
            ((MainActivity) requireActivity()).showFragment(new F_Applicants());
        });

        view.findViewById(R.id.btnExportCsv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {}
        });

        Event cachedEvent = model.getCurrentEvent();
        String eventId = cachedEvent != null ? cachedEvent.getUid() : null;

        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_Applicants());
        });

        RecyclerView rv = view.findViewById(R.id.rvEnrolled);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if (eventId != null) {
            // refresh event from firebase
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Event freshEvent = (Event) obj;
                    model.setCurrentEvent(freshEvent); // Update cache

                    model.getEntrantsByIds(freshEvent.getAttendee_list(), new DataModel.GetCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            Log.d("Firebase", "retrieved enrolled list");
                            ArrayList<Entrant> data = (ArrayList<Entrant>) obj;

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    TextView myText = view.findViewById(R.id.tvEventName);
                                    myText.setText(freshEvent.getTitle());

                                    myText = view.findViewById(R.id.tvListSize);
                                    String countText = data.size() + " enrolled";
                                    myText.setText(countText);

                                    // Set adapter logic
                                    profileListener = new ProfileListAdapter.OnProfileClickListener() {
                                        @Override
                                        public void onProfileClick(Entrant entrant, int position) {
                                            Toast.makeText(requireContext(), "Clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onDeleteClick(Entrant entrant, int position) {
                                            // Handle delete
                                        }
                                    };
                                    rv.setAdapter(new ProfileListAdapter(data, profileListener));
                                });
                            }
                        }

                        @Override
                        public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

                        @Override
                        public void onError(Exception e) {
                            Log.e("F_Enrolled", "Failed to fetch entrants");
                        }
                    });
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

                @Override
                public void onError(Exception e) {
                    Log.e("F_Enrolled", "Failed to refresh event");
                }
            });
        }
    }

}
