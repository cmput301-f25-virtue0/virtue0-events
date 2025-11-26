package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class F_Applicants extends Fragment {

    private int role;
    private DataModel model;
    private Event event;

    public static F_Applicants newInstance(int myRole){
        F_Applicants fragment = new F_Applicants();
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
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.applicants, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();
        if (event == null) {
            Toast.makeText(requireContext(), "Error: No event selected to run the lottery.", Toast.LENGTH_LONG).show();
            // Go back to the organizer's home page (role 1)
            ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            return;
        }
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            });

        // Detect button presses
        view.findViewById(R.id.btnDoLottery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_Lottery.newInstance(1));
            }});

        view.findViewById(R.id.btnChosen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_Chosen.newInstance(1));
            }});

        view.findViewById(R.id.btnEnrolled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_Enrolled.newInstance(1));
            }});

        view.findViewById(R.id.btnCancelled).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_Cancelled.newInstance(1));
            }});

        view.findViewById(R.id.btnMap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_Map());
            }});
    }
}
