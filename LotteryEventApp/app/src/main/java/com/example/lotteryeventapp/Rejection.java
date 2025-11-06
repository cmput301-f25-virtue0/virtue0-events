package com.example.lotteryeventapp;

/**
 * This class is a Notification that the Entrant was rejected from the Event
 */
public class Rejection extends Notification{
    private String uid;
    private Event event;
    private Entrant entrant;

    /**
     * A rejection from the Event indicating the Entrant was not chosen from the waitlist
     * @param event the Event the Entrant was rejected from
     * @param entrant the Entrant the Event rejected
     */
    public Rejection(Event event, Entrant entrant,String uid){
        this.uid = uid;
        this.event = event;
        this.entrant = entrant;
    }

    public Rejection(Event event, Entrant entrant){
        this.event = event;
        this.entrant = entrant;
    }

    /**
     * gets the Event that the Entrant is being rejected from
     * @return the Event the Entrant is being rejected from
     */
    public Event getEvent() {
        return event;
    }

    /**
     * gets the Entrant the Event is rejecting
     * @return the Entrant the Event is rejecting
     */
    public Entrant getEntrant() {
        return entrant;
    }

    @Override
    public String getMessage() {
        return "";
    }


}
