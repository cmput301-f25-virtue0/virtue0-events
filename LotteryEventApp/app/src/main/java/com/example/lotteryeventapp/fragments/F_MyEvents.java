// F_MyEvents.java
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
import com.example.lotteryeventapp.R;

import java.util.Arrays;
import java.util.List;

public class F_MyEvents extends Fragment {
    private int role;
    public F_MyEvents(int myRole) {
        this.role = myRole;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_events_list, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        RecyclerView rv = v.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Demo "my events" (different set)
        List<Event> data = Arrays.asList(
                new Event("My Registered: Swim Lessons", "Mon Jan 6 · 6:00–7:30 PM", "Downtown Rec Centre",
                        "2024-12-15", "You’re on the waitlist.", false, true, 100, 20),
                new Event("My Registered: Canoe Safety", "Sat Jan 11 · 10:00–12:00 PM", "Lakefront Dock",
                        "2024-12-20", "Invitation pending.", false, true, 60, 12)
        );

        EventAdapter adapter = new EventAdapter(data, (event, pos) ->
                /*Toast.makeText(requireContext(),
                        "My Events tapped: " + event.getTitle(),
                        Toast.LENGTH_SHORT).show()*/
        { ((MainActivity) requireActivity()).showFragment(new F_EventInfo(role, event)); }
        );
        rv.setAdapter(adapter);
    }
}


