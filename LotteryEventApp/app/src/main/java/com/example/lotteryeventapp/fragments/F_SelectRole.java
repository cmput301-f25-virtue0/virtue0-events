package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;

public class F_SelectRole extends Fragment {
    private DataModel model;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.select_role, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        model = ((MainActivity) requireActivity()).getDataModel();
        // Detect button presses
        view.findViewById(R.id.btnEntrant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getCurrentOrganizer() == null || model.getCurrentEntrant() == null) {
                    Toast.makeText(getContext(), "Loading profile... please wait.", Toast.LENGTH_SHORT).show();
                    return;
                }


                ((MainActivity) requireActivity()).setActiveHomePageTab(0);
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));
            }
        });

        view.findViewById(R.id.btnOrganizer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getCurrentOrganizer() == null ||  model.getCurrentEntrant() == null) {
                    Toast.makeText(getContext(), "Loading profile... please wait.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((MainActivity) requireActivity()).setActiveHomePageTab(0);
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            }
        });

        view.findViewById(R.id.btnAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
            }
        });
    }
}
