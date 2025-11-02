package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.Random;

/**
 * This class contains all of the information and functionality of an Event
 */
public class Event {

    private String date_time;
    private String location;
    private String registration_deadline;
    private String details;
    private boolean track_geolocation;
    private boolean will_automatically_redraw;
    private int waitlist_limit;
    private int attendee_limit;
    private ArrayList<Entrant> waitlist;
    private ArrayList<Entrant> attendee_list;
    private ArrayList<Entrant> cancelled_list;
    private ArrayList<Entrant> invited_list;
    private boolean drawn;

    /**
     * Creates an Event
     * @param date_time date and time of the Event
     * @param location location of the Event
     * @param registration_deadline registration deadline of an event
     * @param details details and information about an Event such as: topic, difficulty, age range etc.
     * @param track_geolocation whether the Event would like to track the waitlist Entrants location
     * @param will_automatically_redraw whether the Event will immediately redraw an Entrant from the waitlist to be added to the invited list
     * @param waitlist_limit maximum amount of Entrants in a waitlist
     * @param attendee_limit maximum amount of Entrant that will attend the event
     */
    public Event(String date_time, String location, String registration_deadline, String details,
                  boolean track_geolocation,boolean will_automatically_redraw, int waitlist_limit, int attendee_limit){
        this.date_time = date_time;
        this.location = location;
        this.registration_deadline = registration_deadline;
        this.details = details;
        this.track_geolocation = track_geolocation;
        this.will_automatically_redraw = will_automatically_redraw;
        this.waitlist_limit = waitlist_limit;
        this.attendee_limit = attendee_limit;
        this.waitlist = new ArrayList<>();
        this.attendee_list = new ArrayList<>();
        this.cancelled_list = new ArrayList<>();
        this.invited_list = new ArrayList<>();
        this.drawn = false;
    }

    /**
     * get date and time of Event
     * @return date and time of event
     */
    public String getDate_time() {
        return date_time;
    }

    /**
     * set date and time of Event
     * @param date_time date and time of event
     */
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    /**
     * get location of Event
     * @return location of Event
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets location of Event
     * @param location location of Event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get registration deadline of Event
     * @return registration deadline of Event
     */
    public String getRegistration_deadline() {
        return registration_deadline;
    }

    /**
     * set registration deadline of Event
     * @param registration_deadline registration deadline of Event
     */
    public void setRegistration_deadline(String registration_deadline) {
        this.registration_deadline = registration_deadline;
    }

    /**
     * get details of Event
     * @return the details about the Event
     */
    public String getDetails() {
        return details;
    }

    /**
     * sets details about the Event
     * @param details details and information about the event
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * tells whether the Event is tracking the location of the Entrants
     * @return whether location tracking is on
     */
    public boolean willTrack_geolocation() {
        return track_geolocation;
    }

    /**
     * sets the tracking geolocation
     * @param track_geolocation whether or not to track geolocation
     */
    public void setTrack_geolocation(boolean track_geolocation) {
        this.track_geolocation = track_geolocation;
    }


    public boolean willAutomaticallyRedraw() {
        return will_automatically_redraw;
    }

    public void setWillAutomaticallyRedraw(boolean will_automatically_redraw) {
        this.will_automatically_redraw = will_automatically_redraw;
    }
    /**
     * Returns the current number of Entrants in the waitlist.
     * @return number of Entrants in waitlist
     */
    public int getWaitlistAmount() {
        return waitlist.size();
    }
    /**
     * gets the waitlist limit
     * @return the waitlist limit
     */
    public int getWaitlist_limit() {
        return waitlist_limit;
    }

    /**
     * sets the waitlist limit
     * @param waitlist_limit the limit on how many Entrants can be on the waitlist
     */
    public void setWaitlist_limit(int waitlist_limit) {
        this.waitlist_limit = waitlist_limit;
    }

    /**
     * gets the max amount of people who can attend the event
     * @return amount of people who can attend the event
     */
    public int getAttendee_limit() {
        return attendee_limit;
    }

    /**
     * sets the maximum amount of people attending the event
     * @param attendee_limit maximum amount of people attending the event
     */
    public void setAttendee_limit(int attendee_limit) {
        this.attendee_limit = attendee_limit;
    }

    /**
     * gets the waitlist for the Event
     * @return waitlist for the Event
     */
    public ArrayList<Entrant> getWaitlist() {
        return waitlist;
    }

    /**
     * gets the list of Entrants attending the Event
     * @return a list of Entrants attending the Event
     */
    public ArrayList<Entrant> getAttendee_list() {
        return attendee_list;
    }

    /**
     * gets the list of Entrants who cancelled
     * @return list of Entrants who cancelled
     */
    public ArrayList<Entrant> getCancelled_list() {
        return cancelled_list;
    }
    /**
     * gets the invited list containing Entrants who have been drawn
     * but haven't confirmed attendance and invites are pending
     * @return list of invited Entrants invited to the Event
     */
    public ArrayList<Entrant> getInvited_list() { return invited_list; }

    /**
     * add an Entrant to the waitlist of the Event
     * @param entrant entrant to be added to the waitlist
     */
    public void waitlistAdd(Entrant entrant){
        waitlist.add(entrant);
    }

    /**
     * adds an Entrant to the attendee list
     * @param entrant Entrant to be added to the attendee_list
     */
    public void attendeeListAdd(Entrant entrant){
        attendee_list.add(entrant);
    }

    /**
     * add an Entrant to the cancelled_list and redraws automaticallu after selected Entrant cancels
     * @param entrant Entrant to be added to the cancelled_list
     */
    public void cancelledListAdd(Entrant entrant) {
        cancelled_list.add(entrant);
        attendee_list.remove(entrant);
    }

    /**
     * adds an Entrant to the invited list
     * @param cancelledEntrant Entrant to be added to the invited list
     */
    public void handleInvitationCancelled(Entrant cancelledEntrant) {
        invited_list.remove(cancelledEntrant);
        cancelledListAdd(cancelledEntrant);

        if (will_automatically_redraw && !waitlist.isEmpty()) {
            Entrant replacement = waitlist.remove(0);
            invited_list.add(replacement);
        }
    }
    /**
     * Conducts a lottery draw: randomly selects a number of entrants from
     * the waitlist (up to available attendee spots), removes them from the waitlist,
     * and moves them to the invited list.
     */
    public void doLottery() {
        if (waitlist.isEmpty()) {
            System.out.println("No entrants on the waitlist to draw from.");
            return;
        }

        Random rand = new Random();
        int numToDraw = Math.min(attendee_limit - invited_list.size(), waitlist.size());

        for (int i = 0; i < numToDraw; i++) {
            int randomIndex = rand.nextInt(waitlist.size());
            Entrant drawnEntrant = waitlist.remove(randomIndex);
            invited_list.add(drawnEntrant);
        }

        drawn = true;
    }
    /**
     * tells whether the Event has already drawn names yet
     * @return whether the Event has already drawn names
     */
    public boolean isDrawn() {
        return drawn;
    }

    /**
     * sets whether the Event has drawn names
     * @param drawn whether the Event has drawn names
     */
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    /**
     * edits the information about an existing Event
     * @param date_time date and time of the Event
     * @param location location of the Event
     * @param registration_deadline registration deadline of an event
     * @param details details and information about an Event such as: topic, difficulty, age range etc.
     * @param track_geolocation whether the Event would like to track the waitlist Entrants location
     * @param will_automatically_redraw whether the Event will immediately redraw an Entrant from the waitlist to be added to the invited list
     * @param waitlist_limit maximum amount of Entrants in a waitlist
     * @param attendee_limit maximum amount of Entrant that will attend the event
     */
    public void editEvent(String date_time, String location, String registration_deadline, String details,
                          boolean track_geolocation, boolean will_automatically_redraw, int waitlist_limit, int attendee_limit){
        setDate_time(date_time);
        setLocation(location);
        setRegistration_deadline(registration_deadline);
        setDetails(details);
        setTrack_geolocation(track_geolocation);
        setWillAutomaticallyRedraw(will_automatically_redraw);
        setWaitlist_limit(waitlist_limit);
        setAttendee_limit(attendee_limit);
    }
}
