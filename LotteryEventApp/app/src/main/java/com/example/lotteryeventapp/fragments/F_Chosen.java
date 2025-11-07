package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class F_Chosen extends Fragment {
    private DataModel model;
    private Event event;

    public F_Chosen(DataModel myModel) {
        model = myModel;
        event = model.getCurrentEvent();
    }

    private ProfileListAdapter.OnProfileClickListener profileListener;
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout
        return inflater.inflate(R.layout.chosen, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_Applicants(model));
        });

        // Set up RecyclerView
        RecyclerView rv = view.findViewById(R.id.rvChosen);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create Dummy Data
        /*List<Entrant> data = Arrays.asList(
                new Entrant("device22", new Entrant.Profile("Victor",  "victor@example.net",   "780-555-0119")),
                new Entrant("device23", new Entrant.Profile("Willow",  "willow@ualberta.ca",   "780-555-0120")),
                new Entrant("device24", new Entrant.Profile("Xavier",  "xavier@example.com",   "780-555-0121")),
                new Entrant("device25", new Entrant.Profile("Yara",    "yara@example.org",     "780-555-0122")),
                new Entrant("device26", new Entrant.Profile("Zane",    "zane@example.net",     "780-555-0123")),
                new Entrant("device27", new Entrant.Profile("Jasmin",  "jasmin@ualberta.ca",   "780-555-0124"))
        );*/
        List<Entrant> data;
        try {
            data = event.getUsableInvitedList();
        } catch(InterruptedException e) {
            Log.e("F_Chosen", "getUsableInvitedList threw error");
            data = new ArrayList<>();
        }

        this.profileListener = new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Profile clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Delete clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                // TODO: Handle delete
            }
        };

        rv.setAdapter(new ProfileListAdapter(data, profileListener));


    }
}