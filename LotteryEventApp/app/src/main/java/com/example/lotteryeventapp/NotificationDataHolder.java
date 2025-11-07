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
}
