package com.example.lotteryeventapp;

import java.util.Map;

public class RejectionDataHolder extends NotificationDataHolder {
    public RejectionDataHolder(Rejection rej) {
        super(rej, NotificationType.REJECTION);
    }

    public RejectionDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

    public Rejection createRejectionInstance() {
        Rejection rej = new Rejection(this.uid, this.event, this.entrant, this.message);

        if (this.sent) {
            rej.markAsSent();
        }
        if (this.read) {
            rej.markAsRead();
        }

        return rej;
    }

}
