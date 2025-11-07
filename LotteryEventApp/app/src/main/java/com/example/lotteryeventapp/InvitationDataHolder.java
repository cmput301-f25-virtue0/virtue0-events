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
        Invitation inv = new Invitation(this.uid, this.event, this.entrant, this.message);

        if (this.sent) {
            inv.markAsSent();
        }
        if (this.read) {
            inv.markAsRead();
        }

        return inv;
    }
}
