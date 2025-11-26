package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Invitation;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;

public class F_Acceptance extends Fragment {

    private int role;
    private Invitation notif;
    private DataModel model;

    public static F_Acceptance newInstance(int myRole){
        F_Acceptance fragment = new F_Acceptance();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
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
        model = ((MainActivity) requireActivity()).getDataModel();

        this.notif = (Invitation) this.model.getCurrentNotification();


        // Detect button presses
        view.findViewById(R.id.backArrowAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_Notification.newInstance(0));
            }
        });

        view.findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.signed_up), Toast.LENGTH_SHORT).show();
                notif.signUp();
                ((MainActivity) requireActivity()).showFragment(F_Notification.newInstance(0));
            }
        });

        view.findViewById(R.id.btnDecline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), getString(R.string.declined), Toast.LENGTH_SHORT).show();
                notif.decline();
                ((MainActivity) requireActivity()).showFragment(F_Notification.newInstance(0));
            }
        });
    }
}
