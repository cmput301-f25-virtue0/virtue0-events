package com.example.lotteryeventapp.fragments;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class F_ImagesList extends Fragment {
    private int role;

    public F_ImagesList(int myRole) {
        this.role = myRole;
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.admin_image_list, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(2));
            }
        });
    }
}
