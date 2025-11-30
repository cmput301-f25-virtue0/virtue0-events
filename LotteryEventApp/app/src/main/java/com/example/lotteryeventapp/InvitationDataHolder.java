package com.example.lotteryeventapp;

import java.util.Map;

public class InvitationDataHolder extends NotificationDataHolder{
    /**
     * constructs InvitationDataHolder based on Invitation object
     * @param inv Invitation that InvitationDataHolder is based on
     */
    public InvitationDataHolder(Invitation inv) {
        super(inv, NotificationType.INVITATION);
    }

    /**
     * constructs InvitationDataHolder based on data from database
     * @param data data from database
     * @param uid uid of Invitation from database
     */
    public InvitationDataHolder(Map<String, Object> data, String uid) {
        super(data, uid);
    }

    /**
     * creates an Invitation from InvitationDataHolder
     * @return Invitation made from InvitationDataHolder
     */
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
