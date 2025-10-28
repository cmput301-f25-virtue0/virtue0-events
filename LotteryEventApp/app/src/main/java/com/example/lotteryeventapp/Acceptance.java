package com.example.lotteryeventapp;

public class Acceptance extends Notification{
    private Event event;
    private Entrant entrant;
    public Acceptance(Event event, Entrant entrant){
        this.event = event;
        this.entrant = entrant;
    }

    public Event getEvent() {
        return event;
    }

    public Entrant getEntrant() {
        return entrant;
    }

    public void signUp(){
        this.event.attendeeListAdd(this.entrant);
        this.entrant.removeNotification(this);
    }
    public void decline(){
        this.event.cancelledListAdd(this.entrant);
        this.entrant.removeNotification(this);
    }
}
