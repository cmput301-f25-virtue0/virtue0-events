package com.example.lotteryeventapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventDataHolder {
    private String title;
    private String uid;
    private String dateTime;
    private String location;
    private String registrationDeadline;
    private String details;
    private boolean trackGeolocation;
    private boolean willAutomaticallyRedraw;
    private int waitlistLimit;
    private int attendeeLimit;
    private ArrayList<String> waitlist = new ArrayList<String>();
    private ArrayList<String> attendeeList = new ArrayList<String>();
    private ArrayList<String> cancelledList = new ArrayList<String>();
    private ArrayList<String> invitedList = new ArrayList<String>();
    private boolean drawn;

    public EventDataHolder(Event event) {
        this.title = event.getTitle();
        this.dateTime = event.getDate_time();
        this.location = event.getLocation();
        this.registrationDeadline = event.getRegistration_deadline();
        this.details = event.getDetails();
        this.trackGeolocation = event.willTrack_geolocation();
        this.willAutomaticallyRedraw = event.willAutomaticallyRedraw();
        this.waitlistLimit = event.getWaitlist_limit();
        this.attendeeLimit = event.getAttendee_limit();
        this.waitlist.addAll(event.getWaitlist());
        this.attendeeList.addAll(event.getAttendee_list());
        this.cancelledList.addAll(event.getCancelled_list());
        this.invitedList.addAll(event.getInvited_list());
        this.drawn = event.isDrawn();
    }

    public EventDataHolder(Map<String, Object> data, String eventId) {
        this.title = (String) data.get("title");
        this.dateTime = (String) data.get("date_time");
        this.location = (String) data.get("location");
        this.registrationDeadline = (String) data.get("registration_deadline");
        this.details = (String) data.get("details");
        this.trackGeolocation = (Boolean) data.get("track_geolocation");
        this.willAutomaticallyRedraw = (Boolean) data.get("redraw");
        this.waitlistLimit = (int) data.get("waitlist_limit");
        this.attendeeLimit = (int) data.get("attendee_limit");

        List<Object> waitlist = (List<Object>) data.get("waitlist");
        for (Object o: waitlist) {
            this.waitlist.add((String) o);
        }

        List<Object> attendeeList = (List<Object>) data.get("attendee_list");
        for (Object o: attendeeList) {
            this.attendeeList.add((String) o);
        }

        List<Object> cancelledList = (List<Object>) data.get("cancelled_list");
        for (Object o: cancelledList) {
            this.cancelledList.add((String) o);
        }

        List<Object> invitedList = (List<Object>) data.get("invited_list");
        for (Object o: invitedList) {
            this.invitedList.add((String) o);
        }

        this.drawn = (Boolean) data.get("drawn");
    }

    public Event createEventInstance() {
        Event event = new Event(this.title, this.uid, this.dateTime, this.location, this.registrationDeadline, this.details,
                this.trackGeolocation, this.willAutomaticallyRedraw, this.waitlistLimit, this.attendeeLimit);

        event.getWaitlist().addAll(this.waitlist);
        event.getAttendee_list().addAll(this.attendeeList);
        event.getCancelled_list().addAll(this.cancelledList);
        event.getInvited_list().addAll(this.invitedList);

        event.setDrawn(this.drawn);

        return event;
    }
}
