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
    private ArrayList<String> waitlist;
    private ArrayList<String> attendeeList;
    private ArrayList<String> cancelledList;
    private ArrayList<String> invitedList;
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

        ArrayList<Entrant> waitList = event.getWaitlist();
        for (Entrant entrant : waitList) {
            this.waitlist.add(entrant.getUid());
        }

        ArrayList<Entrant> attendeeList = event.getAttendee_list();
        for (Entrant entrant : attendeeList) {
            this.attendeeList.add(entrant.getUid());
        }

        ArrayList<Entrant> cancelledList = event.getCancelled_list();
        for (Entrant entrant : cancelledList) {
            this.cancelledList.add(entrant.getUid());
        }

        ArrayList<Entrant> invitedList = event.getInvited_list();
        for (Entrant entrant : invitedList) {
            this.invitedList.add(entrant.getUid());
        }

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

        for (String entrantId: this.waitlist) {
            // TODO: Implement

            //event.addWaitlist();
        }

        for (String entrantId: this.attendeeList) {
            // TODO: Implement

            //event.addAttendeeList();
        }

        for (String entrantId: this.cancelledList) {
            // TODO: Implement

            //event.addCancelledList();
        }

        for (String entrantId: this.invitedList) {
            // TODO: Implement

            //event.addInvitedList();
        }

        event.setDrawn(this.drawn);

        return event;
    }
}
