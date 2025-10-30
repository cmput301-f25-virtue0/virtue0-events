package com.example.lotteryeventapp;

import java.util.ArrayList;

/**
 * This class contains a collection of events created by an organizer
 */
public class Organizer {
    private ArrayList<Event> events;

    /**
     * An Organizer is created with no Events
     */
    public Organizer(){
        this.events = new ArrayList<>();
    }

    /**
     * add an Event to the Organizer
     * @param event the Event to be added to the Organizer
     */
    public void addEvent(Event event){
        this.events.add(event);
    }

    /**
     * get the Organizers Events
     * @return the Organizers Events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }
}
