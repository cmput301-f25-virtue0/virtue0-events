package com.example.lotteryeventapp;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class AcceptanceTest {
    public Event mockEvent(){
        return new Event("6:00pm, January 27, 2025", "Southgate","11:59pm, December 1,2024","Stuff will happen",false,false,20,20);
    }
    public Entrant mockEntrant(){
        return new Entrant();
    }

    public Acceptance mockAcceptance(){
        Acceptance acceptance =  new Acceptance(mockEvent(),mockEntrant());
        mockEntrant().addNotification(acceptance);
        return acceptance;
    }


    @Test
    void testSignUp() {
        Acceptance acceptance = mockAcceptance();
        Entrant entrant = acceptance.getEntrant();
        acceptance.signUp();
        assertTrue(acceptance.getEvent().getAttendee_list().contains(entrant));
        assertFalse(entrant.getNotifications().contains(acceptance));
    }
    @Test
    void testDecline() {
        Acceptance acceptance = mockAcceptance();
        Entrant entrant = acceptance.getEntrant();
        acceptance.decline();
        assertTrue(acceptance.getEvent().getCancelled_list().contains(entrant));
        assertFalse(entrant.getNotifications().contains(acceptance));


    }


}
