package com.example.lotteryeventapp;

import java.util.Map;

public abstract class NotificationDataHolder {
    protected String uid;
    protected String event;
    protected String entrant;
    protected String message;
    protected boolean sent;
    protected boolean read;
    protected NotificationType notificationType;

    public enum NotificationType {
        INVITATION,
        REJECTION;

        public static final NotificationType INV = INVITATION;
        public static final NotificationType REJ = REJECTION;
    }

    public NotificationDataHolder(Notification notif, NotificationType notifType) {
        this.uid = notif.getUid();
        this.event = notif.getEvent();
        this.entrant = notif.getEntrant();
        this.message = notif.getMessage();
        this.sent = notif.isSent();
        this.read = notif.isRead();
        this.notificationType = notifType;
    }

    public NotificationDataHolder(Map<String, Object> data, String uid) {
        this.uid = (String) data.get("uid");
        this.event = (String) data.get("event");
        this.entrant = (String) data.get("entrant");
        this.message = (String) data.get("message");
        this.sent = (Boolean) data.get("is_sent");
        this.read = (Boolean) data.get("is_read");
        this.notificationType = (NotificationType) data.get("notification_type");
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEntrant() {
        return entrant;
    }

    public void setEntrant(String entrant) {
        this.entrant = entrant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
