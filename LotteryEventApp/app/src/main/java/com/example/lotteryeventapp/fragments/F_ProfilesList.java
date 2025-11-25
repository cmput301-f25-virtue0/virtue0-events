package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class F_ProfilesList extends Fragment {

    private int role;
    private DataModel model;
    private ProfileListAdapter adapter;
    private RecyclerView rv;

    public static F_ProfilesList newInstance(int myRole) {
        F_ProfilesList fragment = new F_ProfilesList();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_profile_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        model = ((MainActivity) requireActivity()).getDataModel();
        rv = view.findViewById(R.id.rvProfiles);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));


        adapter = new ProfileListAdapter(new ArrayList<>(), new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                if (entrant.getProfile() != null) {
                    Toast.makeText(requireContext(), entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                deleteEntrant(entrant);
            }
        });
        rv.setAdapter(adapter);


        loadProfiles();

        // Setup Toolbar
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmProfile);
        toolbar.setNavigationOnClickListener(v -> {
            // Navigate back to Admin Home
            if (isAdded() && getActivity() != null) {
                ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
            }
        });
    }

    private void loadProfiles() {
        model.getAllEntrants(new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Entrant> entrants = (List<Entrant>) obj;
                if (isAdded() && getActivity() != null) {
                    adapter.setItems(entrants);
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

            @Override
            public void onError(Exception e) {
                Log.e("F_ProfilesList", "Error loading profiles", e);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to load profiles", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteEntrant(Entrant entrant) {
        model.deleteEntrant(entrant, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Profile deleted", Toast.LENGTH_SHORT).show();
                    loadProfiles(); // Refresh list to show removal
                }
            }

            @Override
            public void onError(Exception e) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}