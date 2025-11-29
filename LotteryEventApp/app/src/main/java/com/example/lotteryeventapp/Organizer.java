package com.example.lotteryeventapp;

import android.util.Log;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * This class contains a collection of events created by an organizer
 */
public class Organizer {
    private String uid;
    private ArrayList<String> events;

    /**
     * An Organizer is created with no Events
     */
    public Organizer(String uid){
        this.uid = uid;
        this.events = new ArrayList<>();
    }
    public Organizer(){
        this.events = new ArrayList<>();
    }

    public String getUid() {
        return uid;
    }

    /**
     * add an Event to the Organizer
     * @param event the Event to be added to the Organizer
     */
    public void addEvent(String event){
        this.events.add(event);
    }

    /**
     * get the Organizers Events
     * @return the Organizers Events
     */
    public ArrayList<String> getEvents() {
        return events;
    }
    public ArrayList<Event> getUsableEvents() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Event> events = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(this.events.size());
        for (String event_id: getEvents()) {
            model.getEvent(event_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                    latch.countDown();

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Event event = (Event) obj;
                    events.add(event);
                    latch.countDown();
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                    latch.countDown();
                }
            });


        }
        latch.await();
        return events;
    }
}

