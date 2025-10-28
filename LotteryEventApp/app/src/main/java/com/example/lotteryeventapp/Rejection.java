package com.example.lotteryeventapp;

public class Rejection extends Notification{
    private Event event;
    private Entrant entrant;
    public Rejection(Event event, Entrant entrant){
        this.event = event;
        this.entrant = entrant;
    }

    public Event getEvent() {
        return event;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void rejoinWaitlist(){
        this.event.waitlistAdd(this.entrant);
        this.entrant.removeNotification(this);
    }
    public void done(){
        this.entrant.removeNotification(this);
    }
}
