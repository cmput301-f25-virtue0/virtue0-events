package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;

public class F_CreateEditEvent extends Fragment {
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.edit_and_create_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Detect button presses
        Toolbar myToolBar = view.findViewById(R.id.toolbarCreateEvent);
        myToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_HomePage(1));
            }
        });
    }
}
