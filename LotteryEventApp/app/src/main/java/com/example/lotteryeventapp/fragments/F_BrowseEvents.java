// F_BrowseEvents.java
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

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Notification;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class F_BrowseEvents extends Fragment {
    private int role;
    private DataModel model;
    public F_BrowseEvents() { }


    public F_BrowseEvents(int myRole, DataModel myModel) {
        this.role = myRole;
        model = myModel;
    }

    public F_BrowseEvents(int myRole) {
        this.role = myRole;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_events_list, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {

        if (role != 2) {
            v.findViewById(R.id.toolbar).setVisibility(View.GONE);

        }


        RecyclerView rv = v.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        MaterialToolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v1 -> {
            ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(2, model));
        });

        DataModel dataModel = new DataModel();

// Start the asynchronous data fetch
        dataModel.getAllEvents(new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) { /* not used here */ }

            @Override
            public void onSuccess(Object obj) {
                // this code runs only after data has been retrieved
                ArrayList<Event> eventData = (ArrayList<Event>) obj;

                Log.d("Firebase", "retrieved " + eventData.size() + " events.");

                // 1. Create the adapter with the retrieved data
                EventAdapter adapter = new EventAdapter(eventData, role, (event, pos) ->
                {
                    // No need for latch anymore,
                    // this adapter and data are only created after fetch.
                    dataModel.setCurrentEvent(event);
                    ((MainActivity) requireActivity()).showFragment(new F_EventInfo(role, dataModel));
                });

                // 2. Set the adapter to the RecyclerView
                rv.setAdapter(adapter);
            }

            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail to retrieve events: " + e.getMessage());
                Toast.makeText(requireContext(), "Error loading events", Toast.LENGTH_SHORT).show();
            }
        });


    }
}

