package com.example.lotteryeventapp;

/**
 * An abstract base class representing a notification related to an event.
 * Specific notification types (like Invitation, Reminder, or Cancellation)
 * should extend this class.
 */
public abstract class Notification {
    private String uid;
    protected Event event;
    protected Entrant entrant;
    private String message;
    private boolean sent;
    private boolean read;

    public Notification(String uid, Event event, Entrant entrant, String msg) {
        this.uid = uid;
        this.event = event;
        this.entrant = entrant;
        this.message = msg;
    }

    public Notification(Event event, Entrant entrant, String msg) {
        this.uid = "";
        this.event = event;
        this.entrant = entrant;
        this.message = msg;
    }

    @Deprecated
    public Notification(Event event, Entrant entrant) {
        this.uid = "";
        this.event = event;
        this.entrant = entrant;
        this.message = "";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /** Gets the event this notification refers to. */
    public Event getEvent() {
        return event;
    }

    /** Gets the entrant who received the notification. */
    public Entrant getEntrant() {
        return entrant;
    }

    /** Marks this notification as read. */
    public void markAsRead() {
        this.read = true;
    }

    /** Checks if this notification has been read. */
    public boolean isRead() {
        return read;
    }

    /** Checks if this notification has been sent. */
    public boolean isSent() {
        return sent;
    }

    /** Marks this notification as sent. */
    protected void markAsSent() {
        this.sent = true;
    }

    /**
     * Sends the notification to the entrant.
     * (Assumes Entrant has addNotification(Notification) implemented.)
     */
    public void send() {
        if (entrant != null) {
            entrant.addNotification(this);
            markAsSent();
        }
    }

    /**
     * Defines the message content of this notification.
     * Each subclass must override this to provide a message.
     */
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
