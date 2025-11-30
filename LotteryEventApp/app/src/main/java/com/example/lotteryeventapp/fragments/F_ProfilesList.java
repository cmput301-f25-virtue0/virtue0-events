package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.AllEntrantsPagination;
import com.example.lotteryeventapp.AllEventsPagination;
import com.example.lotteryeventapp.AllOrganizersPagination;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.FirestorePagination;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class F_ProfilesList extends Fragment {

    private DataModel model;
    private RecyclerView rvProfiles;
    private TabLayout tabProfiles;
    private ProfileListAdapter adapter;

    public F_ProfilesList() {
        // empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.admin_profile_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) requireActivity();
        model = activity.getDataModel();

        Toolbar toolbar = view.findViewById(R.id.toolbarAdmProfile);
        rvProfiles = view.findViewById(R.id.rvProfiles);
        tabProfiles = view.findViewById(R.id.tabProfiles);

        toolbar.setNavigationOnClickListener(v ->
                ((MainActivity) requireActivity())
                        .showFragment(F_AdminHomePage.newInstance(2))
        );

        // RecyclerView
        rvProfiles.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ProfileListAdapter(new ArrayList<>());
        rvProfiles.setAdapter(adapter);

        tabProfiles.addTab(tabProfiles.newTab().setText("Entrants"));
        tabProfiles.addTab(tabProfiles.newTab().setText("Organizers"));

        loadEntrants();

        tabProfiles.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("Entrants".contentEquals(tab.getText())) {
                    loadEntrants();
                } else {
                    loadOrganizers();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadEntrants() {
        AllEntrantsPagination pagination = new AllEntrantsPagination(10);
        pagination.getNextPage(new FirestorePagination.PaginationCallback() {
            @Override
            public <T> void onGetPage(boolean hasResults, ArrayList<T> obs) {
                ArrayList<Entrant> entrantData = (ArrayList<Entrant>) obs;
                List<ProfileRow> rows = new ArrayList<>();
                for (Entrant e : entrantData) {
                    String name = e.getProfile() != null ? e.getProfile().getName() : "";
                    String email = e.getProfile() != null ? e.getProfile().getEmail() : "";
                    rows.add(new ProfileRow(
                            e.getUid(),
                            name,
                            email,
                            "Entrant"
                    ));
                }

                adapter.updateData(rows);

            }

            @Override
            public void onError(Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void loadOrganizers() {

        AllOrganizersPagination pagination = new AllOrganizersPagination(10);
        pagination.getNextPage(new FirestorePagination.PaginationCallback() {
            @Override
            public <T> void onGetPage(boolean hasResults, ArrayList<T> obs) {
                ArrayList<Organizer> organizerData = (ArrayList<Organizer>) obs;
                List<ProfileRow> rows = new ArrayList<>();
                for (Organizer o : organizerData) {
                    // If Organizer has profile later, you can show more details
                    rows.add(new ProfileRow(
                            o.getUid(),
                            "",    // name unknown for now
                            "",
                            "Organizer"
                    ));
                }
                adapter.updateData(rows);



            }

            @Override
            public void onError(Exception e) {
                throw new RuntimeException(e);
            }

        });
    }

    static class ProfileRow {
        String uid;
        String name;
        String email;
        String role;

        ProfileRow(String uid, String name, String email, String role) {
            this.uid = uid;
            this.name = name;
            this.email = email;
            this.role = role;
        }
    }

    static class ProfileListAdapter extends RecyclerView.Adapter<ProfileViewHolder> {

        private final List<ProfileRow> items;

        ProfileListAdapter(List<ProfileRow> items) {
            this.items = items;
        }

        void updateData(List<ProfileRow> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new ProfileViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileViewHolder holder,
                                     int position) {
            ProfileRow row = items.get(position);

            String line1;
            if (!row.name.isEmpty()) {
                line1 = row.name;
            } else {
                line1 = row.uid;
            }

            String line2;
            if (!row.email.isEmpty()) {
                line2 = "Email: " + row.email;
            } else {
                line2 = "UID: " + row.uid;
            }

            holder.bind(line1, line2);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    static class ProfileViewHolder extends RecyclerView.ViewHolder {

        private final android.widget.TextView text1;
        private final android.widget.TextView text2;

        ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            text1 = itemView.findViewById(android.R.id.text1);
            text2 = itemView.findViewById(android.R.id.text2);
        }

        void bind(String line1, String line2) {
            text1.setText(line1);
            text2.setText(line2);
        }
    }
}
