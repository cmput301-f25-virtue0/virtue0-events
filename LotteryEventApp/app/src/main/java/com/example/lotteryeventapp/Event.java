package com.example.lotteryeventapp;

import java.util.ArrayList;

public class Event {

    private String date_time;
    private String location;
    private String registration_deadline;
    private String details;
    private boolean track_geolocation;
    private boolean redraw;
    private int waitlist_limit;
    private int attendee_limit;
    private ArrayList<Entrant> waitlist;
    private ArrayList<Entrant> attendee_list;
    private ArrayList<Entrant> cancelled_list;
    private boolean drawn;
    public Event(String date_time, String location, String registration_deadline, String details,
                  boolean track_geolocation,boolean redraw, int waitlist_limit, int attendee_limit){
        this.date_time = date_time;
        this.location = location;
        this.registration_deadline = registration_deadline;
        this.details = details;
        this.track_geolocation = track_geolocation;
        this.redraw = redraw;
        this.waitlist_limit = waitlist_limit;
        this.attendee_limit = attendee_limit;
        this.waitlist = new ArrayList<>();
        this.attendee_list = new ArrayList<>();
        this.cancelled_list = new ArrayList<>();
        this.drawn = false;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistration_deadline() {
        return registration_deadline;
    }

    public void setRegistration_deadline(String registration_deadline) {
        this.registration_deadline = registration_deadline;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isTrack_geolocation() {
        return track_geolocation;
    }

    public void setTrack_geolocation(boolean track_geolocation) {
        this.track_geolocation = track_geolocation;
    }

    public boolean isRedraw() {
        return redraw;
    }

    public void setRedraw(boolean redraw) {
        this.redraw = redraw;
    }

    public int getWaitlist_limit() {
        return waitlist_limit;
    }

    public void setWaitlist_limit(int waitlist_limit) {
        this.waitlist_limit = waitlist_limit;
    }

    public int getAttendee_limit() {
        return attendee_limit;
    }

    public void setAttendee_limit(int attendee_limit) {
        this.attendee_limit = attendee_limit;
    }

    public ArrayList<Entrant> getWaitlist() {
        return waitlist;
    }

    public ArrayList<Entrant> getAttendee_list() {
        return attendee_list;
    }

    public ArrayList<Entrant> getCancelled_list() {
        return cancelled_list;
    }
    public void waitlistAdd(Entrant entrant){
        waitlist.add(entrant);
    }
    public void attendeeListAdd(Entrant entrant){
        attendee_list.add(entrant);
    }
    public void cancelledListAdd(Entrant entrant){
        cancelled_list.add(entrant);
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    public void editEvent(String date_time, String location, String registration_deadline, String details,
                          boolean track_geolocation, boolean redraw, int waitlist_limit, int attendee_limit){
        setDate_time(date_time);
        setLocation(location);
        setRegistration_deadline(registration_deadline);
        setDetails(details);
        setTrack_geolocation(track_geolocation);
        setRedraw(redraw);
        setWaitlist_limit(waitlist_limit);
        setAttendee_limit(attendee_limit);
    }
}
