package com.example.lotteryeventapp;

public class Messaging extends Notification{
    /**
     * constructs messaging
     * @param uid uid of messaging
     * @param event event the messaging is about
     * @param entrant entrant the messaging is going to
     * @param msg message for messaging
     */
    public Messaging(String uid, String event, String entrant, String msg){
        super(uid, event, entrant, msg);
    }
    /**
     * constructs messaging
     * @param event event the messaging is about
     * @param entrant entrant the messaging is going to
     * @param msg message for messaging
     */
    public Messaging(String event, String entrant, String msg){
        super(event, entrant, msg);
    }
}
