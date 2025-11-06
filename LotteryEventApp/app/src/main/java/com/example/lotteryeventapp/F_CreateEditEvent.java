package com.example.lotteryeventapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class F_CreateEditEvent extends Fragment {
    private int type;
    private Event event;

    //type = 0 for create, type = 1 for edit
    public F_CreateEditEvent(int myType) {
        this.type = myType;
        event = null;
    }

    public F_CreateEditEvent(int myType, Event myEvent) {
        this.type = myType;
        event = myEvent;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.edit_and_create_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        //If editing, fill out information with existing event info
        if ((this.type == 1) && (event != null)){
            //Title
            TextInputEditText myText = view.findViewById(R.id.etName);
            myText.setText(event.getTitle());
            //Description
            myText = view.findViewById(R.id.etDesc);
            myText.setText(event.getDetails());
            //Location
            myText = view.findViewById(R.id.etLocation);
            myText.setText(event.getLocation());
            //Date
            myText = view.findViewById(R.id.etWhen);
            myText.setText(event.getDate_time());
            //Registration Start
            //myText = view.findViewById(R.id.etRegOpens);
            //myText.setText(event.getRegistration_start());
            //Registration End
            myText = view.findViewById(R.id.etRegCloses);
            myText.setText(event.getRegistration_deadline());
            //Capacity
            myText = view.findViewById(R.id.etCapacity);
            myText.setText(event.getAttendee_limit());
            //Sample Size
            //myText = view.findViewById(R.id.etSample);
            //myText.setText(event.getSample_size());
            //Waitlist Limit
            myText = view.findViewById(R.id.etWaitlistLimit);
            myText.setText(event.getWaitlist_limit());
            //Geolocation
            MaterialSwitch mySwitch = view.findViewById(R.id.switchGeo);
            if (event.willTrack_geolocation()) { mySwitch.setChecked(true); }
            else { mySwitch.setChecked(false); }
        }

        // Detect button presses
        /*Toolbar myToolBar = view.findViewById(R.id.toolbarCreateEvent);
        myToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_HomePage(1));
            }
        });*/

        view.findViewById(R.id.btnPublish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();

                //Make the new event
                //Get the values from fields
                TextInputEditText myText = view.findViewById(R.id.etName);
                String title = Objects.requireNonNull(myText.getText()).toString();
                myText = view.findViewById(R.id.etDesc);
                String details = Objects.requireNonNull(myText.getText()).toString();
                myText = view.findViewById(R.id.etLocation);
                String location = Objects.requireNonNull(myText.getText()).toString();
                myText = view.findViewById(R.id.etWhen);
                String date_time = Objects.requireNonNull(myText.getText()).toString();
                myText = view.findViewById(R.id.etRegCloses);
                String registration_deadline = Objects.requireNonNull(myText.getText()).toString();
                myText = view.findViewById(R.id.etCapacity);
                int attendee_limit = Integer.parseInt(Objects.requireNonNull(myText.getText()).toString());
                myText = view.findViewById(R.id.etWaitlistLimit);
                int waitlist_limit = Integer.parseInt(Objects.requireNonNull(myText.getText()).toString());
                MaterialSwitch mySwitch = view.findViewById(R.id.switchGeo);

                //Create the new event object
                boolean track_geo = mySwitch.isChecked();
                Event makeEvent = new Event(title, date_time, location, registration_deadline,
                        details, track_geo, true, waitlist_limit, attendee_limit);
                //Add it to the database
                //todo

                ((MainActivity) requireActivity()).showFragment(new F_HomePage(1));
            }
        });
    }
}
