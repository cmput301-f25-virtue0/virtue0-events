package com.example.lotteryeventapp;

/**
 * An abstract base class representing a notification related to an event.
 * Specific notification types (like Invitation, Reminder, or Cancellation)
 * should extend this class.
 */
public abstract class Notification {
    private String uid;
    protected String event;
    protected String entrant;
    private String message;
    private boolean sent;
    private boolean read;

    /**
     * constructs Notification
     * @param uid uid of notification
     * @param event event notification is from
     * @param entrant entrant notification is sent to
     * @param msg message of the notification
     */
    public Notification(String uid, String event, String entrant, String msg) {
        this.uid = uid;
        this.event = event;
        this.entrant = entrant;
        this.message = msg;
    }
    /**
     * constructs Notification without uid
     * @param event event notification is from
     * @param entrant entrant notification is sent to
     * @param msg message of the notification
     */
    public Notification(String event, String entrant, String msg) {
        this.uid = "";
        this.event = event;
        this.entrant = entrant;
        this.message = msg;
    }
    /**
     * constructs Notification without uid or message
     * @param event event notification is from
     * @param entrant entrant notification is sent to
     */
    @Deprecated
    public Notification(String event, String entrant) {
        this.uid = "";
        this.event = event;
        this.entrant = entrant;
        this.message = "";
    }

    /**
     * get uid of notification
     * @return uid of notification
     */
    public String getUid() {
        return uid;
    }
    /**
     * set uid of notification
     * @param uid uid of notification
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * Gets the event this notification refers to
     * @return event notification refers to
     */
    public String getEvent() {
        return event;
    }

    /**
     * Gets the entrant who received the notification
     * @return entrant who received the notification
     */
    public String getEntrant() {
        return entrant;
    }

    /** Marks this notification as read. */
    public void markAsRead() {
        this.read = true;
    }

    /** Checks if this notification has been read
     * @return whether notification has been read
     */
    public boolean isRead() {
        return read;
    }

    /** Checks if this notification has been sent.
     * @return whether notification is sent
     */
    public boolean isSent() {
        return sent;
    }

    /** Marks this notification as sent. */
    protected void markAsSent() {
        this.sent = true;
    }

    /**
     * get message of notification
      * @return message of notification
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * set message of notification
     * @param msg message of notification
     */
    public void setMessage(String msg) {
        this.message = msg;
    }
}
