package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;


public class F_AdminHomePage extends Fragment {
    private int role;

    //role = 0 for entrant, role = 1 for organizer, role = 2 for admin

    public F_AdminHomePage(int myRole) {
        this.role = myRole;
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.admin_homepage, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Detect button presses
        view.findViewById(R.id.btnProfiles).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_ProfilesList(2));
        }

    });
        view.findViewById(R.id.btnImages).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_ImagesList(2));
                }
        });
        view.findViewById(R.id.btnEvents).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_BrowseEvents(2));
                }
        });
        view.findViewById(R.id.btnNotifications).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_Notification(2));
                }
        });
    }





}
