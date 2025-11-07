package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.Event;
import com.google.android.material.textfield.TextInputEditText;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;

public class F_EventInfo extends Fragment {
    private int role;
    private Event event;

    //role = 0 for entrant, role = 1 for organizer
    public F_EventInfo(int myRole, Event myEvent) {
        this.role = myRole;
        event = myEvent;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.event_info, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Set up info based on event
        if (event != null) {
            //Title
            TextView myText = view.findViewById(R.id.eventName);
            myText.setText(event.getTitle());
            //DateTime
            myText = view.findViewById(R.id.eventDateTime);
            myText.setText(event.getDate_time());
            //Location
            myText = view.findViewById(R.id.eventLocation);
            myText.setText(event.getLocation());
            //Tags
            //todo
            //Description
            myText = view.findViewById(R.id.eventDescription);
            myText.setText(event.getDetails());
            //Wait list size
            if (role == 0) {
                myText = view.findViewById(R.id.waitingListSize);
                String fraction = event.getWaitlistAmount() + "/" + event.getWaitlist_limit();
                myText.setText(fraction);
            }
        }

        // Set up page based on role
        if (role == 0) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.GONE);

            // Detect button presses
            view.findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Joined waiting list", Toast.LENGTH_SHORT).show();
                }});

            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_HomePage(0));
                }});
        }
        else if (role == 1) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.GONE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.GONE);

            view.findViewById(R.id.editEventBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_CreateEditEvent(1, event));
                }});

            view.findViewById(R.id.applicantsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_Applicants());
                }});
            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_HomePage(1));
                }});
        } else if (role == 2) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.GONE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.VISIBLE);

            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_BrowseEvents(2));
                }});

        }



    }
}
