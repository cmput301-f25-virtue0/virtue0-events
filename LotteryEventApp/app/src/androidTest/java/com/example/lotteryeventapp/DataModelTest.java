package com.example.lotteryeventapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class DataModelTest {
    final String permanentTestEvent = "j74qByX628ykkeajI5wh";
    final String permanentTestEntrant = "TEST_ENTRANT_DO_NOT_REMOVE";
    final String permanentTestOrganizer = "TEST_ORGANIZER_DO_NOT_REMOVE";
    final String permanentTestInvitation = "TEST_INVITATION_DO_NOT_REMOVE";
    final String permanentTestRejection = "TEST_REJECTION_DO_NOT_REMOVE";



    public Organizer permanentMockOrganizer(){
        return new Organizer("TEST_ORGANIZER_DO_NOT_REMOVE");
    }

    public Organizer mockOrganizer(){
        return new Organizer("TEST_ORGANIZER_TEMPORARY");
    }

    @Test
    public void getNonexistentOrganizerTest() throws InterruptedException {
        DataModel model = new DataModel();
        CountDownLatch latch = new CountDownLatch(1);

        model.getOrganizer("organizer", new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                assertEquals(false, true);
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Organizer does not exist")) {
                    assertEquals(true, true);
                }else {
                    assertEquals(false, true);
                }

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void newOrganizerAndDeleteOrganizerTest() throws InterruptedException {
        setEntrantTest();
        deleteEntrantTest();
    }

    public void deleteOrganizerTest () throws InterruptedException {
        DataModel model = new DataModel();
        Organizer organizer = mockOrganizer();
        CountDownLatch latch = new CountDownLatch(1);

        model.deleteOrganizer(organizer, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();
    }

    public void setOrganizerTest() throws InterruptedException {
        DataModel model = new DataModel();
        Organizer organizer = mockOrganizer();
        CountDownLatch latch = new CountDownLatch(1);

        model.setOrganizer(organizer, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    public void getOrganizerTest() throws InterruptedException {
        DataModel model = new DataModel();
        CountDownLatch latch = new CountDownLatch(1);
        model.getOrganizer(this.permanentTestOrganizer, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Organizer organizer = (Organizer) obj;
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();
    }



    public Entrant permanentMockEntrant() {
        return new Entrant("TEST_ENTRANT_DO_NOT_REMOVE", createMockProfile("Personnel #4865",
                "[REDACTED]@[REDACTED]", "[REDACTED]"));
    }

    public Entrant mockEntrant() {
        return new Entrant("TEST_ENTRANT_TEMPORARY", createMockProfile("Personnel #8746",
                "[REDACTED]@[REDACTED]", "[REDACTED]"));
    }

    private Entrant.Profile createMockProfile(String name, String email, String phone) {
        return new Entrant.Profile(name, email, phone);
    }

    @Test
    public void getNonexistentEntrantTest() throws InterruptedException {
        DataModel model = new DataModel();
        CountDownLatch latch = new CountDownLatch(1);

        model.getEntrant("entrant", new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                assertEquals(false, true);
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Entrant does not exist")) {
                    assertEquals(true, true);
                }else {
                    assertEquals(false, true);
                }

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void newEntrantAndDeleteEntrantTest() throws InterruptedException {
        setEntrantTest();
        deleteEntrantTest();
    }

    public void deleteEntrantTest () throws InterruptedException {
        DataModel model = new DataModel();
        Entrant entrant = mockEntrant();
        CountDownLatch latch = new CountDownLatch(1);

        model.deleteEntrant(entrant, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();
    }

    public void setEntrantTest() throws InterruptedException {
        DataModel model = new DataModel();
        Entrant entrant = mockEntrant();
        CountDownLatch latch = new CountDownLatch(1);

        model.setEntrant(entrant, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();
    }

    @Test
    public void getEntrantTest() throws InterruptedException {
        DataModel model = new DataModel();
        Entrant mockEntrant = permanentMockEntrant();
        CountDownLatch latch = new CountDownLatch(1);
        model.getEntrant(this.permanentTestEntrant, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Entrant entrant = (Entrant) obj;
                assertEquals(entrant.getProfile().getName(), mockEntrant.getProfile().getName());
                assertEquals(entrant.getProfile().getEmail(), mockEntrant.getProfile().getEmail());
                assertEquals(entrant.getProfile().getPhone(), mockEntrant.getProfile().getPhone());
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });

        latch.await();
    }

    public Event permanentMockEvent() {
        return new Event("Testing", "12:00pm, January 01, 2000","Outpost 19","11:59pm, December 1,2024","Testing event object, DO NOT REMOVE",false,false,20,20);
    }

    public Event mockEvent() {
        return new Event("Testing 2", "12:00pm, January 10, 2000","Outpost 16","11:59pm, December 1,2024","Temporary testing event object",false,false,20,20);
    }

    @Test
    public void getNonexistentEventTest() throws InterruptedException {
        DataModel model = new DataModel();
        Event event = mockEvent();
        CountDownLatch latch = new CountDownLatch(1);

        model.getEvent("event", new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                assertEquals(false, true);
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                if (e.getMessage().equals("Event does not exist")) {
                    assertEquals(true, true);
                }else {
                    assertEquals(false, true);
                }

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void newEventAndDeleteEventTest() throws InterruptedException {
        String newEventId = setEventTest();
        deleteEventTest(newEventId);
    }

    public void deleteEventTest (String eventId) throws InterruptedException {
        DataModel model = new DataModel();
        Event event = mockEvent();
        event.setUid(eventId);
        CountDownLatch latch = new CountDownLatch(1);

        model.deleteEvent(event, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });

        latch.await();
    }

    public String setEventTest() throws InterruptedException {
        DataModel model = new DataModel();
        Event event = mockEvent();
        CountDownLatch latch = new CountDownLatch(1);

        model.setEvent(event, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                assertEquals(true, true);
                latch.countDown();
            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });
        latch.await();

        assertNotNull(event.getUid());
        assertNotEquals("", event.getUid());
        return event.getUid();
    }

    @Test
    public void getEventTest() throws InterruptedException {
        DataModel model = new DataModel();
        Event mockEvent = permanentMockEvent();
        CountDownLatch latch = new CountDownLatch(1);
        model.getEvent(this.permanentTestEvent, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Event event = (Event) obj;
                assertEquals(event.getTitle(), mockEvent.getTitle());
                assertEquals(event.getDate_time(), mockEvent.getDate_time());
                assertEquals(event.getLocation(), mockEvent.getLocation());
                latch.countDown();
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }

            @Override
            public void onError(Exception e) {
                assertEquals(false, true);
                latch.countDown();
            }
        });

        latch.await();
    }
}
