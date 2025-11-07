package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Notification;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;


import java.util.Arrays;
import java.util.List;

// 1. Implement the adapter's click listener
public class F_Notification extends Fragment {

    private int role;
    private DataModel model;


    //role = 0 for entrant, role = 1 for organizer
    public F_Notification(int myRole, DataModel myModel) {
        this.role = myRole;
        model = myModel;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the updated layout
        return inflater.inflate(R.layout.notifications_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        // 2. Set up the Toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (role == 0) {
                ((MainActivity) requireActivity()).showFragment(new F_HomePage(0, model));
            }
            else {
                ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(0, model));
            }
        });

        // 3. Find RecyclerView and set LayoutManager
        RecyclerView rv = view.findViewById(R.id.rvNotification);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

    }
}
