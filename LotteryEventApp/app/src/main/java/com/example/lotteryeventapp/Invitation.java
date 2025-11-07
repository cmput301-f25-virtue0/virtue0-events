package com.example.lotteryeventapp;

/**
 * This class is a notification that the Entrant has been accepted into the Event
 */
public class Invitation extends Notification{
    private String uid;


    /**
     * A type of notification sent to an Entrant indicating that they can choose to attend the Event
     * @param event the Event the Entrant could sign up for
     * @param entrant the Entrant that could sign up for the Event
     */
    public Invitation(Event event, Entrant entrant,String uid){
        this.uid = uid;
        this.event = event;
        this.entrant = entrant;
    }
    public Invitation(Event event, Entrant entrant){
        this.event = event;
        this.entrant = entrant;
    }

    /**
     * gets the Event this Invitation is about
     * @return the Event the Invitation is about
     */
    public Event getEvent() {
        return event;
    }

    /**
     * gets the Entrant receiving the Invitation
     * @return the Entrant receiving the Invitation
     */
    public Entrant getEntrant() {
        return entrant;
    }

    @Override
    public String getMessage() {
        return "";
    }

    /**
     * Entrant chooses to join the attending list
     */
    public void signUp(){
        this.event.attendeeListAdd(this.entrant);
        this.entrant.removeNotification(this);
    }

    /**
     * Entrant chooses to not attend the Event
     */
    public void decline(){
        this.event.cancelledListAdd(this.entrant);
        this.event.handleInvitationCancelled(this.entrant);
        this.entrant.removeNotification(this);
    }
}
