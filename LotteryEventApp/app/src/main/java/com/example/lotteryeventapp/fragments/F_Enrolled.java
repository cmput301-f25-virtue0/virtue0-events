// F_Enrolled.java
package com.example.lotteryeventapp.fragments;

import android.net.Uri; 
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import java.io.OutputStream;
import java.util.ArrayList;

public class F_Enrolled extends Fragment {

    private int role;
    private DataModel model;
    private Event event;
    private ArrayList<Entrant> currentEnrolledList = new ArrayList<>();

    private ActivityResultLauncher<String> createCsvLauncher;

    public static F_Enrolled newInstance(int role) {
        F_Enrolled fragment = new F_Enrolled();
        Bundle args = new Bundle();
        args.putInt("role", role);
        fragment.setArguments(args);
        return fragment;
    }

    private ProfileListAdapter.OnProfileClickListener profileListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }

        // Initialize the file creation launcher
        createCsvLauncher = registerForActivityResult(new ActivityResultContracts.CreateDocument("text/csv"), uri -> {
            if (uri != null) {
                writeCsvToUri(uri);
            }
        });
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.enrolled, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        //csv button logic
        view.findViewById(R.id.btnDownloadCsv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentEnrolledList == null || currentEnrolledList.isEmpty()) {
                    Toast.makeText(getContext(), "No entrants to export", Toast.LENGTH_SHORT).show();
                } else {
                    String filename = "Enrolled_Entrants.csv";
                    if (event != null && event.getTitle() != null) {
                        filename = event.getTitle().replaceAll("\\s+", "_") + "_Enrolled.csv";
                    }
                    createCsvLauncher.launch(filename);
                }
            }
        });

        Event cachedEvent = model.getCurrentEvent();
        String eventId = cachedEvent != null ? cachedEvent.getUid() : null;

        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_Applicants());
        });

        RecyclerView rv = view.findViewById(R.id.rvEnrolled);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        if (eventId != null) {
            // refresh event from firebase
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Event freshEvent = (Event) obj;
                    event = freshEvent; // Update local reference
                    model.setCurrentEvent(freshEvent); // Update cache

                    model.getEntrantsByIds(freshEvent.getAttendee_list(), new DataModel.GetCallback() {
                        @Override
                        public void onSuccess(Object obj) {
                            Log.d("Firebase", "retrieved enrolled list");
                            ArrayList<Entrant> data = (ArrayList<Entrant>) obj;

                            // NEW: Update the class-level list for CSV export
                            currentEnrolledList = data;

                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() -> {
                                    TextView myText = view.findViewById(R.id.tvEventName);
                                    myText.setText(freshEvent.getTitle());

                                    myText = view.findViewById(R.id.tvListSize);
                                    String countText = data.size() + " enrolled";
                                    myText.setText(countText);

                                    // Set adapter logic
                                    profileListener = new ProfileListAdapter.OnProfileClickListener() {
                                        @Override
                                        public void onProfileClick(Entrant entrant, int position) {
                                            Toast.makeText(requireContext(), "Clicked: " + entrant.getProfile().getName(), Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onDeleteClick(Entrant entrant, int position) {
                                            // Handle delete
                                        }
                                    };
                                    rv.setAdapter(new ProfileListAdapter(data, profileListener));
                                });
                            }
                        }

                        @Override
                        public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

                        @Override
                        public void onError(Exception e) {
                            Log.e("F_Enrolled", "Failed to fetch entrants");
                        }
                    });
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

                @Override
                public void onError(Exception e) {
                    Log.e("F_Enrolled", "Failed to refresh event");
                }
            });
        }
    }

    // Helper method to write the CSV file
    private void writeCsvToUri(Uri uri) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("Name,Email,Phone,Status\n");

            for (Entrant e : currentEnrolledList) {
                String name = e.getProfile().getName().replace(",", " ");
                String email = e.getProfile().getEmail().replace(",", " ");
                String phone = e.getProfile().getPhone() != null ? e.getProfile().getPhone().replace(",", " ") : "";

                sb.append(name).append(",")
                        .append(email).append(",")
                        .append(phone).append(",")
                        .append("Enrolled\n");
            }

            try (OutputStream os = requireContext().getContentResolver().openOutputStream(uri)) {
                if (os != null) {
                    os.write(sb.toString().getBytes());
                    Toast.makeText(getContext(), "Export Successful!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("CSV", "Error writing CSV", e);
            Toast.makeText(getContext(), "Failed to export CSV", Toast.LENGTH_SHORT).show();
        }
    }
}
