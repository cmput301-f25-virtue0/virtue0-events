package com.example.lotteryeventapp;

import java.util.Map;
/**
 * intermediary between database and Rejection
 */
public class RejectionDataHolder extends NotificationDataHolder {
    /**
     * construct RejectionDataHolder from Rejection
     * @param rej Rejection RejectionDataHolder is based on
     */
    public RejectionDataHolder(Rejection rej) {
        super(rej, NotificationType.REJECTION);
    }

    /**
     * construct RejectionDataHolder from data from the database
     * @param data data from the database
     * @param uid uid of RejectionDataHolder
     */
    public RejectionDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

    /**
     * create Rejection from RejectionDataHolder
     * @return Rejection made from RejectionDataHolder
     */
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
