package com.example.lotteryeventapp;

import java.util.Map;
/**
 * intermediary between database and Notification
 */
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
        REJECTION,
        MESSAGING;

        public static final NotificationType INV = INVITATION;
        public static final NotificationType REJ = REJECTION;
        public static final NotificationType MES = MESSAGING;

    }

    /**
     * construct NotificationDataHolder from notification
     * @param notif notification NotificationDataHolder is based on
     * @param notifType type of Notification
     */
    public NotificationDataHolder(Notification notif, NotificationType notifType) {
        this.uid = notif.getUid();
        this.event = notif.getEvent();
        this.entrant = notif.getEntrant();
        this.message = notif.getMessage();
        this.sent = notif.isSent();
        this.read = notif.isRead();
        this.notificationType = notifType;
    }

    /**
     * construct NotificationDataHolder from data from database
     * @param data data from database
     * @param uid uid of NotificationDataHolder
     */
    public NotificationDataHolder(Map<String, Object> data, String uid) {
        this.uid = uid;
        this.event = (String) data.get("event");
        this.entrant = (String) data.get("entrant");
        this.message = (String) data.get("message");
        this.sent = (Boolean) data.get("sent");
        this.read = (Boolean) data.get("read");
        this.notificationType = NotificationType.valueOf((String)data.get("notificationType"));
    }

    /**
     * get uid of NotificationDataHolder
     * @return uid of NotificationDataHolder
     */
    public String getUid() {
        return uid;
    }
    /**
     * set uid of NotificationDataHolder
     * @param uid uid of NotificationDataHolder
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
    /**
     * get event of NotificationDataHolder
     * @return event of NotificationDataHolder
     */
    public String getEvent() {
        return event;
    }
    /**
     * set event of NotificationDataHolder
     * @param event event of NotificationDataHolder
     */
    public void setEvent(String event) {
        this.event = event;
    }
    /**
     * get entrant of NotificationDataHolder
     * @return entrant of NotificationDataHolder
     */
    public String getEntrant() {
        return entrant;
    }
    /**
     * set entrant of NotificationDataHolder
     * @param entrant entrant of NotificationDataHolder
     */
    public void setEntrant(String entrant) {
        this.entrant = entrant;
    }
    /**
     * get message of NotificationDataHolder
     * @return message of NotificationDataHolder
     */
    public String getMessage() {
        return message;
    }
    /**
     * set message of NotificationDataHolder
     * @param message message of NotificationDataHolder
     */
    public void setMessage(String message) {
        this.message = message;
    }
    /**
     * is NotificationDataHolder sent
     * @return if NotificationDataHolder is sent
     */
    public boolean isSent() {
        return sent;
    }
    /**
     * sets if NotificationDataHolder is sent
     * @param sent whether NotificationDataHolder is sent
     */
    public void setSent(boolean sent) {
        this.sent = sent;
    }
    /**
     * is NotificationDataHolder read
     * @return if NotificationDataHolder is read
     */
    public boolean isRead() {
        return read;
    }
    /**
     * sets if NotificationDataHolder is read
     * @param read whether NotificationDataHolder is read
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * get NotificationType
     * @return notificationType
     */
    public NotificationType getNotificationType() {
        return notificationType;
    }
    /**
     * set NotificationType
     * @param notificationType notificationType
     */
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
}
