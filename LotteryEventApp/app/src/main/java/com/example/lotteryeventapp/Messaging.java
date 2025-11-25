package com.example.lotteryeventapp;

public class Messaging extends Notification{
    public Messaging(String uid, String event, String entrant, String msg){
        super(uid, event, entrant, msg);
    }

    public Messaging(String event, String entrant, String msg){
        super(event, entrant, msg);
    }
}
