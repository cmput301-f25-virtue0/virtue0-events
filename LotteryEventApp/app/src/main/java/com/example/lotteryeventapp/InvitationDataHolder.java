package com.example.lotteryeventapp;

import java.util.Map;

public class InvitationDataHolder extends NotificationDataHolder{
    public InvitationDataHolder(Invitation inv) {
        super(inv, NotificationType.INVITATION);
    }

    public InvitationDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

    public Invitation createInvitationInstance() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
