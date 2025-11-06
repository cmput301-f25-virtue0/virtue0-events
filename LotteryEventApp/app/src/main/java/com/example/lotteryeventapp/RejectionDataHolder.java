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
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
