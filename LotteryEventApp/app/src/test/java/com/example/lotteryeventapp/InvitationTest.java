package com.example.lotteryeventapp;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class InvitationTest {
    public Event mockEvent(){
        return new Event("6:00pm, January 27, 2025", "Southgate","11:59pm, December 1,2024","Stuff will happen",false,false,20,20);
    }
    public Entrant mockEntrant(){
        return new Entrant();
    }

    public Invitation mockInvitation(){
        Invitation invitation =  new Invitation(mockEvent(),mockEntrant());
        mockEntrant().addNotification(invitation);
        return invitation;
    }


    @Test
    void testSignUp() {
        Invitation invitation = mockInvitation();
        Entrant entrant = invitation.getEntrant();
        invitation.signUp();
        assertTrue(invitation.getEvent().getAttendee_list().contains(entrant));
        assertFalse(entrant.getNotifications().contains(invitation));
    }
    @Test
    void testDecline() {
        Invitation invitation = mockInvitation();
        Entrant entrant = invitation.getEntrant();
        invitation.decline();
        assertTrue(invitation.getEvent().getCancelled_list().contains(entrant));
        assertFalse(entrant.getNotifications().contains(invitation));


    }


}
