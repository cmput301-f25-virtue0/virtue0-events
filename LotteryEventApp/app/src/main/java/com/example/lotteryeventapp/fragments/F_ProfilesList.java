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

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;


public class F_ProfilesList extends Fragment {

    private int role;

    private ProfileListAdapter.OnProfileClickListener profileListener;

    //role = 0 for entrant, role = 1 for organizer, role = 2 for admin

    public F_ProfilesList(int myRole) {
        this.role = myRole;
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.admin_profile_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView rv = view.findViewById(R.id.rvProfiles);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Entrant> data = Arrays.asList(
                new Entrant("device1", new Entrant.Profile("Daniel", "dk8@ualberta.ca", "780-123-4567")),

                new Entrant("device2", new Entrant.Profile("Alice", "a@b.com", "555-1234")),

                new Entrant("device3", new Entrant.Profile("Bob", "b@c.com", "555-5678"))
        );

        this.profileListener = new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Profile clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Delete clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                // TODO: Handle delete
            }
        };

        rv.setAdapter(new ProfileListAdapter(data, profileListener));


        // Detect button presses
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmProfile);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(2));
        });
    }

}
