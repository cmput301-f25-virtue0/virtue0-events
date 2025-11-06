package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

public class F_AdminImages extends Fragment {
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.select_role, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmImage);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_AdminHomepage());
        });
    }
}
