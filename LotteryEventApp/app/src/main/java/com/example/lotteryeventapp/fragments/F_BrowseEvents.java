// F_BrowseEvents.java
package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.EventAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;

public class F_BrowseEvents extends Fragment {
    private int role;
    public F_BrowseEvents(int myRole) {
        this.role = myRole;
    }


    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_events_list, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {

        if (role != 2) {
            v.findViewById(R.id.toolbar).setVisibility(View.GONE);

        }

        MaterialToolbar toolbar = v.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(role));
            }
        });

        RecyclerView rv = v.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));



        // Demo "global" events
        List<Event> data = Arrays.asList(
                new Event("Swim Lessons (Beginners)", "Mon Jan 6 · 6:00–7:30 PM", "Downtown Rec Centre",
                        "2024-12-15", "Learn to swim safely.", false, true, 100, 20),
                new Event("Intro to Piano", "Wed Jan 8 · 5:30–6:30 PM", "Music Room B",
                        "2024-12-15", "Basics of piano playing.", false, false, 100, 25),
                new Event("Canoe Safety Workshop", "Sat Jan 11 · 10:00–12:00 PM", "Lakefront Dock",
                        "2024-12-20", "Paddling & safety essentials.", false, true, 60, 12)
        );

        EventAdapter adapter = new EventAdapter(data, role, (event, pos) ->
        { ((MainActivity) requireActivity()).showFragment(new F_EventInfo(role, event)); }
        );
        rv.setAdapter(adapter);
    }
}


