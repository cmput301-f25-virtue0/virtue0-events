package com.example.lotteryeventapp;

import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * This class contains a collection of events created by an organizer
 */
public class Organizer {
    private String uid;
    private ArrayList<String> events;

    /**
     * An Organizer is created with no Events
     */
    public Organizer(String uid){
        this.uid = uid;
        this.events = new ArrayList<>();
    }

    /**
     * constructs organizer without a uid or events
     */
    public Organizer(){
        this.events = new ArrayList<>();
    }

    /**
     * get uid of Organizer
     * @return uid of Organizer
     */
    public String getUid() {
        return uid;
    }

    /**
     * add an Event to the Organizer
     * @param event the Event to be added to the Organizer
     */
    public void addEvent(String event){
        this.events.add(event);
    }

    /**
     * get the Organizers Events
     * @return the Organizers Events
     */
    public ArrayList<String> getEvents() {
        return events;
    }

}

