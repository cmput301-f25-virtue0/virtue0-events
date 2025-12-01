package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * intermediary between database and Event
 */
public class EventDataHolder {
    private String title;
    private String uid;
    private String dateTime;
    private String location;

    private String registrationStart;
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
    private String image;

    private String organizer;

    private ArrayList<String> tags = new ArrayList<>();

    /**
     * create an EventDataHolder based on given event
     * @param event event that EventDataHolder is based on
     */
    public EventDataHolder(Event event) {
        this.uid = event.getUid();
        this.title = event.getTitle();
        this.dateTime = event.getDate_time();
        this.location = event.getLocation();
        this.registrationStart = event.getRegistration_start();
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
        this.organizer = event.getOrganizer();
        this.image = event.getImage();

        if (event.getTags() != null) {
            for (Event.EventTag tag : event.getTags()) {
                this.tags.add(tag.name());
            }
        }
        // If empty, add ALL
        if (this.tags.isEmpty()) {
            this.tags.add(Event.EventTag.ALL.name());
        }

    }

    /**
     * get registration start time
     * @return registration start time
     */
    public String getRegistrationStart() {
        return registrationStart;
    }

    /**
     * set registration start time
     * @param registrationStart registration start time
     */
    public void setRegistrationStart(String registrationStart) {
        this.registrationStart = registrationStart;
    }
    /**
     * get title of EventDataHolder
     * @return title of EventDataHolder
     */
    public String getTitle() {
        return title;
    }
    /**
     * set title of EventDataHolder
     * @param title title of EventDataHolder
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * get uid of EventDataHolder
     * @return uid of EventDataHolder
     */
    public String getUid() {
        return uid;
    }
    /**
     * set uid of EventDataHolder
     * @param uid uid of EventDataHolder
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    /**
     * get date and time of EventDataHolder
     * @return date and time of EventDataHolder
     */
    public String getDateTime() {
        return dateTime;
    }
    /**
     * set date and time of EventDataHolder
     * @param dateTime date and time of EventDataHolder
     */
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    /**
     * get location of EventDataHolder
     * @return location of EventDataHolder
     */
    public String getLocation() {
        return location;
    }
    /**
     * set location of EventDataHolder
     * @param location location of EventDataHolder
     */
    public void setLocation(String location) {
        this.location = location;
    }
    /**
     * get registration deadline of EventDataHolder
     * @return registration deadline of EventDataHolder
     */
    public String getRegistrationDeadline() {
        return registrationDeadline;
    }
    /**
     * set registration deadline of EventDataHolder
     * @param registrationDeadline registration deadline of EventDataHolder
     */
    public void setRegistrationDeadline(String registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }
    /**
     * get details of EventDataHolder
     * @return details of EventDataHolder
     */
    public String getDetails() {
        return details;
    }
    /**
     * set details of EventDataHolder
     * @param details details of EventDataHolder
     */
    public void setDetails(String details) {
        this.details = details;
    }
    /**
     * get whether EventDataHolder tracks location of entrants
     * @return whether EventDataHolder tracks location of entrants
     */
    public boolean isTrackGeolocation() {
        return trackGeolocation;
    }
    /**
     * set whether EventDataHolder tracks location of entrants
     * @param trackGeolocation whether EventDataHolder tracks location of entrants
     */
    public void setTrackGeolocation(boolean trackGeolocation) {
        this.trackGeolocation = trackGeolocation;
    }
    /**
     * get whether EventDataHolder automatically redraws when entrant declines invitation
     * @return whether EventDataHolder automatically redraws when entrant declines invitation
     */
    public boolean isWillAutomaticallyRedraw() {
        return willAutomaticallyRedraw;
    }
    /**
     * set whether EventDataHolder automatically redraws when entrant declines invitation
     * @param willAutomaticallyRedraw whether EventDataHolder automatically redraws when entrant declines invitation
     */
    public void setWillAutomaticallyRedraw(boolean willAutomaticallyRedraw) {
        this.willAutomaticallyRedraw = willAutomaticallyRedraw;
    }
    /**
     * get waitlist limit of EventDataHolder
     * @return waitlist limit of EventDataHolder
     */
    public int getWaitlistLimit() {
        return waitlistLimit;
    }
    /**
     * set waitlist limit of EventDataHolder
     * @param waitlistLimit waitlist limit of EventDataHolder
     */
    public void setWaitlistLimit(int waitlistLimit) {
        this.waitlistLimit = waitlistLimit;
    }
    /**
     * get attendee limit of EventDataHolder
     * @return attendee limit of EventDataHolder
     */
    public int getAttendeeLimit() {
        return attendeeLimit;
    }
    /**
     * set attendee limit of EventDataHolder
     * @param attendeeLimit  attendee limit of EventDataHolder
     */
    public void setAttendeeLimit(int attendeeLimit) {
        this.attendeeLimit = attendeeLimit;
    }
    /**
     * get waitlist of EventDataHolder
     * @return waitlist of EventDataHolder
     */
    public ArrayList<String> getWaitlist() {
        return waitlist;
    }
    /**
     * set waitlist of EventDataHolder
     * @param waitlist waitlist of EventDataHolder
     */
    public void setWaitlist(ArrayList<String> waitlist) {
        this.waitlist = waitlist;
    }
    /**
     * get attendee list of EventDataHolder
     * @return attendee list of EventDataHolder
     */
    public ArrayList<String> getAttendeeList() {
        return attendeeList;
    }
    /**
     * set attendee list of EventDataHolder
     * @param attendeeList attendee list of EventDataHolder
     */
    public void setAttendeeList(ArrayList<String> attendeeList) {
        this.attendeeList = attendeeList;
    }
    /**
     * get cancelled list of EventDataHolder
     * @return cancelled list of EventDataHolder
     */
    public ArrayList<String> getCancelledList() {
        return cancelledList;
    }
    /**
     * set cancelled list of EventDataHolder
     * @param cancelledList cancelled list of EventDataHolder
     */
    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }
    /**
     * get invited list of EventDataHolder
     * @return invited list of EventDataHolder
     */
    public ArrayList<String> getInvitedList() {
        return invitedList;
    }
    /**
     * set invited list of EventDataHolder
     * @param invitedList  invited list of EventDataHolder
     */
    public void setInvitedList(ArrayList<String> invitedList) {
        this.invitedList = invitedList;
    }
    /**
     * get whether EventDataHolder has been drawn
     * @return whether EventDataHolder has been drawn
     */
    public boolean isDrawn() {
        return drawn;
    }
    /**
     * set whether EventDataHolder has been drawn
     * @param drawn whether EventDataHolder has been drawn
     */
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }
    /**
     * get organizer of EventDataHolder
     * @return organizer of EventDataHolder
     */
    public String getOrganizer() {
        return organizer;
    }
    /**
     * set organizer of EventDataHolder
     * @param organizer  organizer of EventDataHolder
     */
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }
    /**
     * get poster image of EventDataHolder
     * @return poster image of EventDataHolder
     */
    public String getImage() {
        return image;
    }
    /**
     * set poster image of EventDataHolder
     * @param image  poster image of EventDataHolder
     */
    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    /**
     * create EventDataHolder based on data map from database
     * @param data info from database
     * @param eventId uid of event being pulled from database
     */
    public EventDataHolder(Map<String, Object> data, String eventId) {
        this.uid = eventId;
        this.title = (String) data.get("title");
        this.dateTime = (String) data.get("dateTime");
        this.location = (String) data.get("location");
        this.registrationStart = (String) data.get("registrationStart");
        this.registrationDeadline = (String) data.get("registrationDeadline");
        this.details = (String) data.get("details");
        this.trackGeolocation = (Boolean) data.get("trackGeolocation");
        this.willAutomaticallyRedraw = (Boolean) data.get("willAutomaticallyRedraw");
        this.waitlistLimit = ((Long) data.get("waitlistLimit")).intValue();
        this.attendeeLimit = ((Long) data.get("attendeeLimit")).intValue();
        this.organizer = (String) data.get("organizer");
        this.image = (String) data.get("image");

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

        List<Object> loadedTags = (List<Object>) data.get("tags");
        if (loadedTags != null) {
            for (Object o : loadedTags) {
                this.tags.add((String) o);
            }
        }

        this.drawn = (Boolean) data.get("drawn");
    }

    /**
     * create Event based on EventDataHolder
     * @return Event based on EventDataHolder
     */
    public Event createEventInstance() {

        ArrayList<Event.EventTag> eventTags = new ArrayList<>();

        //Convert string to enum
        if (this.tags != null) {
            for (String s : this.tags) {
                try {
                    eventTags.add(Event.EventTag.valueOf(s));
                } catch (IllegalArgumentException e) {
                    // Default to ALL
                }
            }
        }

        Event event = new Event(this.title, this.uid, this.dateTime, this.location, this.registrationStart, this.registrationDeadline, this.details,
                this.trackGeolocation, this.willAutomaticallyRedraw, this.waitlistLimit, this.attendeeLimit, this.organizer, eventTags);

        event.getWaitlist().addAll(this.waitlist);
        event.getAttendee_list().addAll(this.attendeeList);
        event.getCancelled_list().addAll(this.cancelledList);
        event.getInvited_list().addAll(this.invitedList);
        event.setOrganizer(organizer);
        event.setImage(this.image);
        event.setDrawn(this.drawn);



        return event;
    }
}
