package com.example.lotteryeventapp;

import java.util.Map;

public class MessagingDataHolder extends NotificationDataHolder {
    public MessagingDataHolder(Messaging mes) {
        super(mes, NotificationDataHolder.NotificationType.MESSAGING);
    }

    public MessagingDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

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
