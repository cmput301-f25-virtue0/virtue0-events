package com.example.lotteryeventapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class F_EventInfo extends Fragment {
    private int role;

    //role = 0 for entrant, role = 1 for organizer
    public F_EventInfo(int myRole) {
        this.role = myRole;
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

        // Set up page based on role
        if (role == 0) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);

            // Detect button presses
            view.findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Joined waiting list", Toast.LENGTH_SHORT).show();
                }});
        }
        else if (role == 1) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.GONE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.VISIBLE);

            view.findViewById(R.id.editEventBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_CreateEditEvent(1));
                }});

            view.findViewById(R.id.applicantsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_Applicants());
                }});
        }

        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_HomePage(0));
            }});
    }
}
