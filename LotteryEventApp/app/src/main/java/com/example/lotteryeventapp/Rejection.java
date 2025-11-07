package com.example.lotteryeventapp;

/**
 * This class is a Notification that the Entrant was rejected from the Event
 */
public class Rejection extends Notification{
    /**
     * A rejection from the Event indicating the Entrant was not chosen from the waitlist
     * @param event the Event the Entrant was rejected from
     * @param entrant the Entrant the Event rejected
     */
    public Rejection(String uid, String event, String entrant, String msg){
        super(uid, event, entrant, msg);
    }

    public Rejection(String event, String entrant, String msg){
        super(event, entrant, msg);
    }
}
