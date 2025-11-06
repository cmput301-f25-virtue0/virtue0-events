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

    public F_AdminHomePage(int myRole) {
        this.role = myRole;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_homepage, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if (role == 2) {
            view.findViewById(R.id.btnProfiles).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_AdminProfileList(2));
                }
            });
        }
    }
}
