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
//    private boolean redraw;

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
//        this.redraw = event.isDrawn();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isTrackGeolocation() {
        return trackGeolocation;
    }

    public void setTrackGeolocation(boolean trackGeolocation) {
        this.trackGeolocation = trackGeolocation;
    }

    public boolean isWillAutomaticallyRedraw() {
        return willAutomaticallyRedraw;
    }

    public void setWillAutomaticallyRedraw(boolean willAutomaticallyRedraw) {
        this.willAutomaticallyRedraw = willAutomaticallyRedraw;
    }

    public int getWaitlistLimit() {
        return waitlistLimit;
    }

    public void setWaitlistLimit(int waitlistLimit) {
        this.waitlistLimit = waitlistLimit;
    }

    public int getAttendeeLimit() {
        return attendeeLimit;
    }

    public void setAttendeeLimit(int attendeeLimit) {
        this.attendeeLimit = attendeeLimit;
    }

    public ArrayList<String> getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(ArrayList<String> waitlist) {
        this.waitlist = waitlist;
    }

    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }

    public void setAttendeeList(ArrayList<String> attendeeList) {
        this.attendeeList = attendeeList;
    }

    public ArrayList<String> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public ArrayList<String> getInvitedList() {
        return invitedList;
    }

    public void setInvitedList(ArrayList<String> invitedList) {
        this.invitedList = invitedList;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public EventDataHolder(Map<String, Object> data, String eventId) {
        this.title = (String) data.get("title");
        this.dateTime = (String) data.get("dateTime");
        this.location = (String) data.get("location");
        this.registrationDeadline = (String) data.get("registrationDeadline");
        this.details = (String) data.get("details");
        this.trackGeolocation = (Boolean) data.get("trackGeolocation");
        this.willAutomaticallyRedraw = (Boolean) data.get("willAutomaticallyRedraw");
        this.waitlistLimit = ((Long) data.get("waitlistLimit")).intValue();
        this.attendeeLimit = ((Long) data.get("attendeeLimit")).intValue();

        List<Object> waitlist = (List<Object>) data.get("waitlist");
        for (Object o: waitlist) {
            this.waitlist.add((String) o);
        }

        List<Object> attendeeList = (List<Object>) data.get("attendeeList");
        for (Object o: attendeeList) {
            this.attendeeList.add((String) o);
        }

        List<Object> cancelledList = (List<Object>) data.get("cancelledList");
        for (Object o: cancelledList) {
            this.cancelledList.add((String) o);
        }

        List<Object> invitedList = (List<Object>) data.get("invitedList");
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
