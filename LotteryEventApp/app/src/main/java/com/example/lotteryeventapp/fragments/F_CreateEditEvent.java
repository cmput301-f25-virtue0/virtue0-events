package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class F_CreateEditEvent extends Fragment {
    private int type;
    private DataModel model;
    private Event event;

    public static F_CreateEditEvent newInstance(int myType) {
        F_CreateEditEvent fragment = new F_CreateEditEvent();
        Bundle args = new Bundle();
        args.putInt("type", myType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        if (getArguments() != null) {
            this.type = getArguments().getInt("type");
        }
        return inflater.inflate(R.layout.edit_and_create_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarCreateEvent);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();



        //If editing, fill out information with existing event info
        if (this.type == 1 && this.event != null) {
            toolbar.setTitle("Edit Event"); // Update toolbar title

            TextInputEditText myText;

            myText = view.findViewById(R.id.etName);
            myText.setText(Objects.requireNonNullElse(event.getTitle(), ""));

            myText = view.findViewById(R.id.etDesc);
            myText.setText(Objects.requireNonNullElse(event.getDetails(), ""));

            myText = view.findViewById(R.id.etLocation);
            myText.setText(Objects.requireNonNullElse(event.getLocation(), ""));

            myText = view.findViewById(R.id.etWhen);
            myText.setText(Objects.requireNonNullElse(event.getDate_time(), ""));

            myText = view.findViewById(R.id.etRegCloses);
            myText.setText(Objects.requireNonNullElse(event.getRegistration_deadline(), ""));

            myText = view.findViewById(R.id.etCapacity);
            myText.setText(String.valueOf(event.getAttendee_limit()));

            myText = view.findViewById(R.id.etWaitlistLimit);
            myText.setText(String.valueOf(event.getWaitlist_limit()));

            MaterialSwitch mySwitch = view.findViewById(R.id.switchGeo);
            mySwitch.setChecked(event.willTrack_geolocation()); // Use getter
        } else {
            // This is "create" mode
            toolbar.setTitle("Create Event");
        }

        toolbar.setNavigationOnClickListener(v -> {
            if (type == 1) {
                ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));
            }
            else {
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            }
        });

        view.findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Get info from fields
                    TextInputEditText etTitle = view.findViewById(R.id.etName);
                    TextInputEditText etDetails = view.findViewById(R.id.etDesc);
                    TextInputEditText etLocation = view.findViewById(R.id.etLocation);
                    TextInputEditText etWhen = view.findViewById(R.id.etWhen);
                    TextInputEditText etRegCloses = view.findViewById(R.id.etRegCloses);
                    TextInputEditText etCapacity = view.findViewById(R.id.etCapacity);
                    TextInputEditText etWaitlist = view.findViewById(R.id.etWaitlistLimit);
                    MaterialSwitch swGeo = view.findViewById(R.id.switchGeo);

                    // Validate fields before parsing
                    String title = etTitle.getText().toString();
                    String details = etDetails.getText().toString();
                    String location = etLocation.getText().toString();
                    String dateTime = etWhen.getText().toString();
                    String regDeadline = etRegCloses.getText().toString();
                    String capacityStr = etCapacity.getText().toString();
                    String waitlistStr = etWaitlist.getText().toString();

                    if (title.isEmpty() || details.isEmpty() || location.isEmpty() || dateTime.isEmpty() ||
                            regDeadline.isEmpty() || capacityStr.isEmpty() || waitlistStr.isEmpty()) {
                        throw new Exception("Empty fields");
                    }

                    int attendee_limit = Integer.parseInt(capacityStr);
                    int waitlist_limit = Integer.parseInt(waitlistStr);
                    boolean track_geo = swGeo.isChecked();

                    // Differentiate between Create and Edit
                    if (type == 0) {
                        // create new event (will be automatically added to the database)
                        Event makeEvent = new Event(title, dateTime, location, regDeadline,
                                details, track_geo, true, waitlist_limit, attendee_limit);
                        model.setEvent(makeEvent, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                Log.d("Firebase", "written");
                                Organizer organizer = model.getCurrentOrganizer();
                                organizer.addEvent(makeEvent.getUid());
                                model.setOrganizer(organizer, new DataModel.SetCallback() {
                                    @Override
                                    public void onSuccess(String msg) {
                                        Log.d("Firebase", "written");

                                    }
                                    @Override
                                    public void onError(Exception e) {
                                        Log.e("Firebase", "fail");
                                    }
                                });

                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("Firebase", "fail");
                            }
                        });
                        Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                        model.setCurrentEvent(makeEvent);
                        //View newly created event
                        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));

                    } else {
                        // update existing event
                        event.setTitle(title);
                        event.setDetails(details);
                        event.setLocation(location);
                        event.setDate_time(dateTime);
                        event.setRegistration_deadline(regDeadline);
                        event.setAttendee_limit(attendee_limit);
                        event.setWaitlist_limit(waitlist_limit);
                        event.setTrack_geolocation(track_geo); // Use setter

                        //Update the existing event
//                        event.editEvent(dateTime, location, regDeadline,
//                                details, track_geo, true, waitlist_limit, attendee_limit);
                        Toast.makeText(getContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                        model.setEvent(event, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                Log.d("Firebase", "written");

                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("Firebase", "fail");
                            }
                        });
                    }

                    if (type == 1) {
                        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));
                    }
                    else {
                        ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
                    }

                } catch(Exception e) {
                    throw new RuntimeException(e);
//                    Toast.makeText(getContext(), "Please fill all missing fields!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

}
