package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class F_Lottery extends Fragment {
    private int role;
    private DataModel model;
    private Event event;

    public static F_Lottery newInstance(int role) {
        F_Lottery fragment = new F_Lottery();
        Bundle args = new Bundle();
        args.putInt("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    private ProfileListAdapter.OnProfileClickListener profileListener;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.lottery, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();
        toolbar.setNavigationOnClickListener(v -> {
            // Go back to the Applicants screen
            ((MainActivity) requireActivity()).showFragment(new F_Applicants());
        });

        //Set up buttons
        view.findViewById(R.id.btnDraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    event.doLottery();
                } catch(InterruptedException e) {
                    Log.e("F_Lottery", "doLottery threw error");
                }
            }
        });

        // --- Set up RecyclerView ---
        RecyclerView rv = view.findViewById(R.id.rvEntrants);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Create Dummy Data
        /*List<Entrant> data = Arrays.asList(
                new Entrant("device4",  new Entrant.Profile("Charlie", "charlie@example.com", "780-555-0101")),
                new Entrant("device5",  new Entrant.Profile("Eva",     "eva@example.org",     "780-555-0102")),
                new Entrant("device6",  new Entrant.Profile("Frank",   "frank@example.net",    "780-555-0103")),
                new Entrant("device7",  new Entrant.Profile("Grace",   "grace@ualberta.ca",    "780-555-0104")),
                new Entrant("device8",  new Entrant.Profile("Henry",   "henry@example.com",    "780-555-0105"))
        );*/

//        List<Entrant> data;
//        try {
//            data = event.getUsableWaitList();
//        } catch(InterruptedException e) {
//            Log.e("F_Lottery", "getUsableWaitList threw error");
//            data = new ArrayList<>();
//        }
//        ProfileListAdapter.OnProfileClickListener fragment = this.profileListener;
        model.getUsableEntrants(event,new DataModel.GetCallback() {

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }
            @Override
            public void onSuccess(Object obj) {
                Log.d("Firebase", "retrieved");
                ArrayList<Entrant> data = (ArrayList<Entrant>) obj;


                TextView myText = view.findViewById(R.id.tvEventName);
                myText.setText(event.getTitle());
                myText = view.findViewById(R.id.tvWaitlistSize);
                String waitText = data.size() + " " + getString(R.string.in_waitlist);
                myText.setText(waitText);

                // Set adapter
                profileListener = new ProfileListAdapter.OnProfileClickListener() {
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


//                entrants.add(entrant);
//                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
//                latch.countDown();
            }
        });
        // Set the text

    }

}
