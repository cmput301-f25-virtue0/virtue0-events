package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizerDataHolder {
    private String uid;
    private ArrayList<String> events = new ArrayList<String>();;

    public OrganizerDataHolder(Organizer organizer) {
        this.uid = organizer.getUid();
        this.events.addAll(organizer.getEvents());
    }

    public OrganizerDataHolder(Map<String, Object> data, String deviceId) {
        this.uid = deviceId;

        List<Object> events = (List<Object>) data.get("events");
        for (Object o: events) {
            this.events.add((String) o);
        }
    }

    public Organizer createOrganizerInstance() {
        Organizer organizer = new Organizer(this.uid);
        organizer.getEvents().addAll(this.events);

        return organizer;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public ArrayList<String> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }
}
