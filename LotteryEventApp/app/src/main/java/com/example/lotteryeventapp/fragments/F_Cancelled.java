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

public class F_Cancelled extends Fragment {

    private int role;
    private DataModel model;
    private Event event;
    private ProfileListAdapter.OnProfileClickListener profileListener;

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
        // Inflate the layout
        return inflater.inflate(R.layout.cancelled, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(F_Applicants.newInstance(2));
        });

        RecyclerView rv = view.findViewById(R.id.rvCancelled);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        /*List<Entrant> data = Arrays.asList(
                new Entrant("device14", new Entrant.Profile("Noah",    "noah@example.net",     "780-555-0111")),
                new Entrant("device15", new Entrant.Profile("Olivia",  "olivia@ualberta.ca",   "780-555-0112")),
                new Entrant("device16", new Entrant.Profile("Parker",  "parker@example.com",   "780-555-0113")),
                new Entrant("device17", new Entrant.Profile("Quinn",   "quinn@example.org",    "780-555-0114")),
                new Entrant("device18", new Entrant.Profile("Riley",   "riley@example.net",    "780-555-0115")),
                new Entrant("device19", new Entrant.Profile("Sofia",   "sofia@ualberta.ca",    "780-555-0116")),
                new Entrant("device20", new Entrant.Profile("Tyler",   "tyler@example.com",    "780-555-0117")),
                new Entrant("device21", new Entrant.Profile("Uma",     "uma@example.org",      "780-555-0118"))
        );*/
        List<Entrant> data;
        try {
            data = event.getUsableCancelledList();
        } catch(InterruptedException e) {
            Log.e("F_Lottery", "getUsableCancelledList threw error");
            data = new ArrayList<>();
        }

        // Set adapter
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