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

    public static F_Notification newInstance(int myRole){
        F_Notification fragment = new F_Notification();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

    //role = 0 for entrant, role = 1 for organizer
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CRITICAL: Retrieve the 'role' argument here
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
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

        model = ((MainActivity) requireActivity()).getDataModel();
        // 2. Set up the Toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (role == 0) {
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));
            }
            else {
                ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
            }
        });

        // 3. Find RecyclerView and set LayoutManager
        RecyclerView rv = view.findViewById(R.id.rvNotification);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

    }
}
