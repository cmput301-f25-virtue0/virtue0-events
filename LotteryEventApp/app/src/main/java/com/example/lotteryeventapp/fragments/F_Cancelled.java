package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class F_Cancelled extends Fragment {

    private int role;
    private DataModel model;
    private Event event;
    private RecyclerView rv;
    private TextView tvCount;
    private ProfileListAdapter adapter;
    private final List<Entrant> cancelledEntrants = new ArrayList<>();

    public static F_Cancelled newInstance(int myRole) {
        F_Cancelled fragment = new F_Cancelled();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
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
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.cancelled, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(v -> {
                ((MainActivity) requireActivity()).showFragment(F_Applicants.newInstance(role));
            });
        }

        rv = view.findViewById(R.id.rvCancelled);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list first
        adapter = new ProfileListAdapter(cancelledEntrants, new ProfileListAdapter.OnProfileClickListener() {
            @Override
            public void onProfileClick(Entrant entrant, int position) {
                Toast.makeText(requireContext(), "Profile: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeleteClick(Entrant entrant, int position) {
                Toast.makeText(getContext(), "Action not implemented", Toast.LENGTH_SHORT).show();
            }
        });
        rv.setAdapter(adapter);

        if (event != null) {
            loadCancelledList();
        } else {
            Log.e("F_Cancelled", "Event is null");
        }
    }

    private void loadCancelledList() {
        ArrayList<String> cancelledIds = event.getCancelled_list();

        if (tvCount != null) {
            tvCount.setText(String.valueOf(cancelledIds.size()));
        }

        if (cancelledIds == null || cancelledIds.isEmpty()) {
            cancelledEntrants.clear();
            adapter.notifyDataSetChanged();
            return;
        }

        // Fetch Entrant objects asynchronously
        model.getEntrantsByIds(cancelledIds, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                List<Entrant> result = (List<Entrant>) obj;

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        cancelledEntrants.clear();
                        cancelledEntrants.addAll(result);
                        adapter.notifyDataSetChanged();

                        if (tvCount != null) {
                            tvCount.setText(String.valueOf(cancelledEntrants.size()));
                        }
                    });
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

            @Override
            public void onError(Exception e) {
                Log.e("F_Cancelled", "Failed to load cancelled entrants", e);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Error loading list", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
