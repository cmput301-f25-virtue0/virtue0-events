package com.example.lotteryeventapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;



public class EventTest {
    public Event mockEvent(){
        return new Event("shopping","6:00pm, January 27, 2025", "123","Southgate","11:59pm, December 1,2024","Stuff will happen",false,false,20,20);

    }

    @Test
    void testEditEvent() {
        Event event = mockEvent();
        event.editEvent("2:00am, December 25, 2025", "Denny's","12pm, February 14,2024","Stuff wont happen",true,true,10,15);
        assertEquals("2:00am, December 25, 2025",event.getDate_time());
        assertEquals("Denny's",event.getLocation());
        assertEquals("12pm, February 14,2024",event.getRegistration_deadline());
        assertEquals("Stuff wont happen",event.getDetails());
        assertTrue(event.willTrack_geolocation());
        assertTrue(event.willAutomaticallyRedraw());
        assertEquals(10,event.getWaitlist_limit());
        assertEquals(15,event.getAttendee_limit());


    }

}
