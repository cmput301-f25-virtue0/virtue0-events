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
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class F_MyEvents extends Fragment {
    private int role;
    private Organizer org;
    private DataModel model;
    public F_MyEvents(int myRole, DataModel myModel) {
        this.role = myRole;
        model = myModel;
        org = model. getCurrentOrganizer();
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle b) {
        return i.inflate(R.layout.fragment_events_list, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle b) {
        RecyclerView rv = v.findViewById(R.id.rvEvents);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        if (role != 2) {
            v.findViewById(R.id.toolbar).setVisibility(View.GONE);
        }


        // Demo "my events" (different set)
        /*List<Event> data = Arrays.asList(
                new Event("My Registered: Swim Lessons", "Mon Jan 6 · 6:00–7:30 PM", "Downtown Rec Centre",
                        "2024-12-15", "You’re on the waitlist.", false, true, 100, 20),
                new Event("My Registered: Canoe Safety", "Sat Jan 11 · 10:00–12:00 PM", "Lakefront Dock",
                        "2024-12-20", "Invitation pending.", false, true, 60, 12)
        );*/
        List<Event> data = new ArrayList<>(); //org.getUsableEvents(); todo: get organizer and replace 'new' statement with commented statement

        EventAdapter adapter = new EventAdapter(data, role, (event, pos) ->
        {
            model.setCurrentEvent(event);
            ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(role)); }
        );
        rv.setAdapter(adapter);
    }
}


