package com.example.lotteryeventapp.fragments;

import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;

public class F_Acceptance extends Fragment {
    private Notification notif;

    public F_Acceptance(Notification myNotif) {
        this.notif = myNotif;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.acceptance, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //mark notification as read
        // todo

        // Detect button presses
        view.findViewById(R.id.backArrowAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_Notification(0));
            }
        });

        view.findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.signed_up), Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).showFragment(new F_Notification(0));
            }
        });

        view.findViewById(R.id.btnDecline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.declined), Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).showFragment(new F_Notification(0));
            }
        });
    }
}
