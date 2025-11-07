package com.example.lotteryeventapp;

/**
 * This class is a notification that the Entrant has been accepted into the Event
 */
public class Invitation extends Notification{
    /**
     * A type of notification sent to an Entrant indicating that they can choose to attend the Event
     * @param event the Event the Entrant could sign up for
     * @param entrant the Entrant that could sign up for the Event
     */
    public Invitation(String uid, Event event, Entrant entrant, String msg){
        super(uid, event, entrant, msg);
    }

    public Invitation(Event event, Entrant entrant, String msg){
        super(event, entrant, msg);
    }

    /**
     * Entrant chooses to join the attending list
     */
    public void signUp(){
        this.event.attendeeListAdd(this.entrant.getUid());
        this.entrant.removeNotification(this.getUid());
    }

    /**
     * Entrant chooses to not attend the Event
     */
    public void decline(){
        this.event.cancelledListAdd(this.entrant.getUid());
        this.event.handleInvitationCancelled(this.entrant.getUid());
        this.entrant.removeNotification(this.getUid());
    }
}
