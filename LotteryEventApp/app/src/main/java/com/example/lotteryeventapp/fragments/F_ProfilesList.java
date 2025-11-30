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
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.ImageListAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.OrganizerListAdapter;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.ProfileListAdapter;
import com.example.lotteryeventapp.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class F_ProfilesList extends Fragment implements ProfileListAdapter.OnProfileClickListener, OrganizerListAdapter.OnOrganizerClickListener {

    private DataModel model;
    private RecyclerView rvProfiles;
    private TabLayout tabProfiles;
    private ProfileListAdapter entrantAdapter;
    private OrganizerListAdapter organizerAdapter;


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
        entrantAdapter = new ProfileListAdapter(new ArrayList<>(),this);
        organizerAdapter = new OrganizerListAdapter(new ArrayList<>(),this);


        tabProfiles.addTab(tabProfiles.newTab().setText("Entrants"));
        tabProfiles.addTab(tabProfiles.newTab().setText("Organizers"));

        loadEntrants();

        tabProfiles.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("Entrants".contentEquals(tab.getText())) {
                    rvProfiles.setAdapter(entrantAdapter);
                    loadEntrants();
                } else {
                    rvProfiles.setAdapter(organizerAdapter);
                    loadOrganizers();
                }
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadEntrants() {
        model.getAllEntrants(new DataModel.ListCallback<Entrant>() {
            @Override
            public void onSuccess(List<Entrant> list) {
                entrantAdapter.setItems(list);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(requireContext(),
                        "Failed to load entrants",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOrganizers() {
        model.getAllOrganizers(new DataModel.ListCallback<Organizer>() {
            @Override
            public void onSuccess(List<Organizer> list) {

                organizerAdapter.setItems(list);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(requireContext(),
                        "Failed to load organizers",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onProfileClick(Organizer organizer, int position) {

    }

    @Override
    public void onDeleteClick(Organizer organizer, int position) {
        ArrayList<String> events = organizer.getEvents();
        for(String event: events) {
            deleteEvent(event);
        }
        model.deleteOrganizer(organizer, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    @Override
    public void onProfileClick(Entrant entrant, int position) {

    }

    @Override
    public void onDeleteClick(Entrant entrant, int position) {
        deleteEntrantFromWaitlist(entrant);
        deleteEntrantfromInvitedList(entrant);
        deleteEntrantFromAttendeeList(entrant);
        model.deleteEntrant(entrant, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void deleteEntrantFromWaitlist(Entrant entrant){
        ArrayList<String> waitlistedEvents = entrant.getWaitlistedEvents();
        for(String eventId: waitlistedEvents){
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Event event = (Event) obj;
                    if(event != null) {
                        event.waitlistRemove(entrant.getUid());
                        model.setEvent(event, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
    private void deleteEntrantfromInvitedList(Entrant entrant){
        ArrayList<String> invitedlistedEvents = entrant.getInvitedEvents();
        for(String eventId: invitedlistedEvents){
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Event event = (Event) obj;
                    if(event != null) {
                        event.invitedListRemove(entrant.getUid());
                        model.setEvent(event, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
    private void deleteEntrantFromAttendeeList(Entrant entrant){
        ArrayList<String> attendeelistedEvents = entrant.getAttendedEvents();
        for(String eventId: attendeelistedEvents){
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Event event = (Event) obj;
                    if(event != null) {
                        event.attendeeListRemove(entrant.getUid());
                        model.setEvent(event, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
    private void deleteEvent(String event){
        model.getEvent(event, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Event deletingEvent = (Event) obj;
                if(deletingEvent != null) {
                    deleteEventFromLists(deletingEvent);
                    model.deleteEvent(deletingEvent, new DataModel.DeleteCallback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                        }
                    });
                }
            }
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    private void deleteEventFromLists(Event obj){
        deleteEventFromWaitlist(obj);
        deleteEventFromInvitedList(obj);
        deleteEventFromAttendingList(obj);
    }
    private void deleteEventFromWaitlist(Event event){
        ArrayList<String> waitlist = event.getWaitlist();
        for(String entrantId: waitlist){
            model.getEntrant(entrantId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant entrant = (Entrant) obj;
                    if(entrant != null) {
                        entrant.removeWaitlistedEvent(event.getUid());
                        model.setEntrant(entrant, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }

    }
    private void deleteEventFromInvitedList(Event event){
        ArrayList<String> invitedList = event.getInvited_list();
        for(String entrantId: invitedList){
            model.getEntrant(entrantId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant entrant = (Entrant) obj;
                    if(entrant != null) {
                        entrant.removeInvitedEvent(event.getUid());
                        model.setEntrant(entrant, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
    private void deleteEventFromAttendingList(Event event){
        ArrayList<String> attendingList = event.getAttendee_list();
        for(String entrantId: attendingList){
            model.getEntrant(entrantId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant entrant = (Entrant) obj;
                    if(entrant != null) {
                        entrant.removeAttendedEvent(event.getUid());
                        model.setEntrant(entrant, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                            }

                            @Override
                            public void onError(Exception e) {
                            }
                        });
                    }
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                }
                @Override
                public void onError(Exception e) {
                }
            });
        }
    }
}
