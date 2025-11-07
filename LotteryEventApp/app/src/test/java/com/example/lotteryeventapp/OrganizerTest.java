package com.example.lotteryeventapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class OrganizerTest {
    public Event mockEvent(){
        return new Event("Shopping","123","6:00pm, January 27, 2025", "Southgate","11:59pm, December 1,2024","Stuff will happen",false,false,20,20);

    }
    public Organizer mockOrganizer(){
        return new Organizer();
    }

    @Test
    void testAddEvent() {
        Organizer organizer= mockOrganizer();
        Event event = mockEvent();
        assertEquals(0,organizer.getEvents().size());
        organizer.addEvent(event.getUid());
        assertEquals(1,organizer.getEvents().size());
        assertEquals(event.getUid(),organizer.getEvents().get(0));
    }
}
