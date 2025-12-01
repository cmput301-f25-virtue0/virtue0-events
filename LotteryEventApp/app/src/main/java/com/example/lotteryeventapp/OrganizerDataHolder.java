package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * intermediary between database and Organizer
 */
public class OrganizerDataHolder {
    private String uid;
    private ArrayList<String> events = new ArrayList<String>();;

    /**
     * construct OrganizerDataHolder based on Organizer
     * @param organizer organizer OrganizerDataHolder is made from
     */
    public OrganizerDataHolder(Organizer organizer) {
        this.uid = organizer.getUid();
        this.events.addAll(organizer.getEvents());
    }

    /**
     * construct OrganizerDataHolder from data from database
     * @param data data from database
     * @param deviceId deviceId associated with Organizer
     */
    public OrganizerDataHolder(Map<String, Object> data, String deviceId) {
        this.uid = deviceId;

        List<Object> events = (List<Object>) data.get("events");
        for (Object o: events) {
            this.events.add((String) o);
        }
    }

    /**
     * create Organizer from OrganizerDataHolder
     * @return Organizer made from OrganizerDataHolder
     */
    public Organizer createOrganizerInstance() {
        Organizer organizer = new Organizer(this.uid);
        organizer.getEvents().addAll(this.events);

        return organizer;
    }

    /**
     * get Uid of OrganizerDataHolder
     * @return uid of OrganizerDataHolder
     */
    public String getUid() {
        return uid;
    }
    /**
     * set Uid of OrganizerDataHolder
     * @param uid uid of OrganizerDataHolder
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    /**
     * get events of OrganizerDataHolder
     * @return events of OrganizerDataHolder
     */
    public ArrayList<String> getEvents() {
        return events;
    }
    /**
     * set events of OrganizerDataHolder
     * @param events events of OrganizerDataHolder
     */
    public void setEvents(ArrayList<String> events) {
        this.events = events;
    }
}
