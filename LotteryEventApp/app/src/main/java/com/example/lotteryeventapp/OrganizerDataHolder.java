package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizerDataHolder {
    private String uid;
    private ArrayList<String> events;

    public OrganizerDataHolder(Organizer organizer) {
        this.uid = organizer.getUid();

        ArrayList<Event> events = organizer.getEvents();
        for (Event event: events) {
            if (!event.getUid().isEmpty()) {
                this.events.add(event.getUid());
            }else {
                throw new RuntimeException("Organizer contains event that has no uid");
            }
        }
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

        for (String eventId: this.events) {
            // TODO: Create event firestore methods

            //organizer.addEvent();
        }

        return organizer;
    }
}
