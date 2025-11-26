package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class F_Chosen extends Fragment {

    private int role;
    private DataModel model;
    private Event event;
    private RecyclerView rv;
    private ProfileListAdapter adapter;
    private TextView tvCount;

    public static F_Chosen newInstance(int role) {
        F_Chosen fragment = new F_Chosen();
        Bundle args = new Bundle();
        args.putInt("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Reuse the lottery list layout (ensure this XML exists and has rvProfiles)
        return inflater.inflate(R.layout.chosen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        rv = view.findViewById(R.id.rvChosen);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView tvTitle = view.findViewById(R.id.tvEventName);
        tvCount = view.findViewById(R.id.tvListSize);


        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(F_Applicants.newInstance(1));
        });

        if (event != null) {
            if (tvTitle != null) tvTitle.setText(event.getTitle());
            fetchChosenEntrants();
        }
    }

    private void fetchChosenEntrants() {
        ArrayList<String> invitedIds = event.getInvited_list();

        // update count
        if (tvCount != null) tvCount.setText(invitedIds.size() + " Chosen");

        model.getEntrantsByIds(invitedIds, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Entrant> loadedEntrants = (List<Entrant>) obj;

                // update list
                if (isAdded() && getActivity() != null) {
                    requireActivity().runOnUiThread(() -> setupAdapter(loadedEntrants));
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
            }

            @Override
            public void onError(Exception e) {
                Log.e("F_Chosen", "Error fetching list");
            }
        });
    }

    private void setupAdapter(List<Entrant> entrants) {
        ProfileListAdapter.OnProfileClickListener listener = new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), "Cancelling invite for " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                // Logic to remove entrant from chosen list goes here
            }
        };

        adapter = new ProfileListAdapter(entrants, listener);
        rv.setAdapter(adapter);
    }
}
