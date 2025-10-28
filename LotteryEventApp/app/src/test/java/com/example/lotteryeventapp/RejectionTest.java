package com.example.lotteryeventapp;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RejectionTest {
    public Event mockEvent(){
        return new Event("6:00pm, January 27, 2025", "Southgate","11:59pm, December 1,2024","Stuff will happen",false,false,20,20);
    }
    public Entrant mockEntrant(){
        return new Entrant();
    }

    public Rejection mockRejection(){
        Rejection rejection =  new Rejection(mockEvent(),mockEntrant());
        mockEntrant().addNotification(rejection);
        return rejection;
    }


    @Test
    void testRejoinWaitlist() {
        Rejection rejection = mockRejection();
        Entrant entrant = rejection.getEntrant();
        rejection.rejoinWaitlist();
        assertTrue(rejection.getEvent().getWaitlist().contains(entrant));

    }
    @Test
    void testDone() {
        Rejection rejection = mockRejection();
        Entrant entrant = rejection.getEntrant();
        rejection.done();
        assertFalse(entrant.getNotifications().contains(rejection));


    }
}
