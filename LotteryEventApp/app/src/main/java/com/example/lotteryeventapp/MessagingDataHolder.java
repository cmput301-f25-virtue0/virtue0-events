package com.example.lotteryeventapp;

import java.util.Map;
/**
 * intermediary between database and Messaging
 */
public class MessagingDataHolder extends NotificationDataHolder {
    /**
     * constructs based on messaging
     * @param mes messaging the MessageDataHolder is based on
     */
    public MessagingDataHolder(Messaging mes) {
        super(mes, NotificationDataHolder.NotificationType.MESSAGING);
    }

    /**
     * construct MessagingDataHolder from data from database
     * @param data data from database
     * @param uid uid of MessagingDataHolder
     */
    public MessagingDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

    /**
     * create a Messaging from MessagingDataHolder
     * @return Messaging made from MessagingDataHolder
     */
    public Messaging createMessagingInstance() {
        Messaging mes = new Messaging(this.uid, this.event, this.entrant, this.message);

        if (this.sent) {
            mes.markAsSent();
        }
        if (this.read) {
            mes.markAsRead();
        }

        return mes;
    }
}
