package com.example.lotteryeventapp;

import java.util.ArrayList;

public class Organizer {
    private ArrayList<Event> events;
    public Organizer(){
        this.events = new ArrayList<>();
    }
    public void addEvent(Event event){
        this.events.add(event);
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
