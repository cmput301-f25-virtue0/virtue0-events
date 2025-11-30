package com.example.lotteryeventapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;



public class EventTest {
    public Event mockEvent(){
        return new Event("shopping","6:00pm, January 27, 2025", "Southgate","November 12","11:59pm, December 1,2024","Stuff will happen",false,false,20,20,"","");

    }


}
