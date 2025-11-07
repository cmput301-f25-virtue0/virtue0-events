package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizerDataHolder {
    private String uid;
    private ArrayList<String> events;

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
}
