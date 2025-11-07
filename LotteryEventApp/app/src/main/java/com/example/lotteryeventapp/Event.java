package com.example.lotteryeventapp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * This class contains all of the information and functionality of an Event
 */
public class Event {
    private String title;
    private String uid;
    private String date_time;
    private String location;
    private String registration_deadline;
    private String details;
    private boolean track_geolocation;
    private boolean will_automatically_redraw;
    private int waitlist_limit;
    private int attendee_limit;
    private ArrayList<String> waitlist;
    private ArrayList<String> attendee_list;
    private ArrayList<String> cancelled_list;
    private ArrayList<String> invited_list;
    private boolean drawn;

    /**
     * Creates an Event
     * @param date_time date and time of the Event
     * @param location location of the Event
     * @param registration_deadline registration deadline of an event
     * @param details details and information about an Event such as: topic, difficulty, age range etc.
     * @param track_geolocation whether the Event would like to track the waitlist Entrants location
     * @param will_automatically_redraw whether the Event will immediately redraw an Entrant from the waitlist to be added to the invited list
     * @param waitlist_limit maximum amount of Entrants in a waitlist
     * @param attendee_limit maximum amount of Entrant that will attend the event
     */
    public Event(String title, String uid, String date_time, String location, String registration_deadline, String details,
                  boolean track_geolocation,boolean will_automatically_redraw, int waitlist_limit, int attendee_limit){
        this.title = title;
        this.uid = uid;
        this.date_time = date_time;
        this.location = location;
        this.registration_deadline = registration_deadline;
        this.details = details;
        this.track_geolocation = track_geolocation;
        this.will_automatically_redraw = will_automatically_redraw;
        this.waitlist_limit = waitlist_limit;
        this.attendee_limit = attendee_limit;
        this.waitlist = new ArrayList<>();
        this.attendee_list = new ArrayList<>();
        this.cancelled_list = new ArrayList<>();
        this.invited_list = new ArrayList<>();
        this.drawn = false;

    }

    public Event(String title, String date_time, String location, String registration_deadline, String details,
                 boolean track_geolocation,boolean will_automatically_redraw, int waitlist_limit, int attendee_limit){
        this.title = title;
        this.date_time = date_time;
        this.location = location;
        this.registration_deadline = registration_deadline;
        this.details = details;
        this.track_geolocation = track_geolocation;
        this.will_automatically_redraw = will_automatically_redraw;
        this.waitlist_limit = waitlist_limit;
        this.attendee_limit = attendee_limit;
        this.waitlist = new ArrayList<>();
        this.attendee_list = new ArrayList<>();
        this.cancelled_list = new ArrayList<>();
        this.invited_list = new ArrayList<>();
        this.drawn = false;
        DataModel model = new DataModel();
        model.setEvent(this, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Firebase", "written");
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get date and time of Event
     * @return date and time of event
     */
    public String getDate_time() {
        return date_time;
    }

    /**
     * set date and time of Event
     * @param date_time date and time of event
     */
    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    /**
     * get location of Event
     * @return location of Event
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets location of Event
     * @param location location of Event
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * get registration deadline of Event
     * @return registration deadline of Event
     */
    public String getRegistration_deadline() {
        return registration_deadline;
    }

    /**
     * set registration deadline of Event
     * @param registration_deadline registration deadline of Event
     */
    public void setRegistration_deadline(String registration_deadline) {
        this.registration_deadline = registration_deadline;
    }

    /**
     * get details of Event
     * @return the details about the Event
     */
    public String getDetails() {
        return details;
    }

    /**
     * sets details about the Event
     * @param details details and information about the event
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * tells whether the Event is tracking the location of the Entrants
     * @return whether location tracking is on
     */
    public boolean willTrack_geolocation() {
        return track_geolocation;
    }

    /**
     * sets the tracking geolocation
     * @param track_geolocation whether or not to track geolocation
     */
    public void setTrack_geolocation(boolean track_geolocation) {
        this.track_geolocation = track_geolocation;
    }


    public boolean willAutomaticallyRedraw() {
        return will_automatically_redraw;
    }

    public void setWillAutomaticallyRedraw(boolean will_automatically_redraw) {
        this.will_automatically_redraw = will_automatically_redraw;
    }
    /**
     * Returns the current number of Entrants in the waitlist.
     * @return number of Entrants in waitlist
     */
    public int getWaitlistAmount() {
        return waitlist.size();
    }
    /**
     * gets the waitlist limit
     * @return the waitlist limit
     */
    public int getWaitlist_limit() {
        return waitlist_limit;
    }

    /**
     * sets the waitlist limit
     * @param waitlist_limit the limit on how many Entrants can be on the waitlist
     */
    public void setWaitlist_limit(int waitlist_limit) {
        this.waitlist_limit = waitlist_limit;
    }

    /**
     * gets the max amount of people who can attend the event
     * @return amount of people who can attend the event
     */
    public int getAttendee_limit() {
        return attendee_limit;
    }

    /**
     * sets the maximum amount of people attending the event
     * @param attendee_limit maximum amount of people attending the event
     */
    public void setAttendee_limit(int attendee_limit) {
        this.attendee_limit = attendee_limit;
    }

    /**
     * gets the waitlist for the Event
     * @return waitlist for the Event
     */
    public ArrayList<String> getWaitlist() {
        return waitlist;
    }

    public ArrayList<Entrant> getUsableWaitList() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Entrant> entrants = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(getWaitlistAmount());
        for (String entrant_id: getWaitlist()) {
            model.getEntrant(entrant_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    entrants.add(entrant);
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
        return entrants;
    }

    /**
     * gets the list of Entrants attending the Event
     * @return a list of Entrants attending the Event
     */
    public ArrayList<String> getAttendee_list() {
        return attendee_list;
    }

    public ArrayList<Entrant> getUsableAttendeeList() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Entrant> entrants = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(getAttendee_list().size());
        for (String entrant_id: getAttendee_list()) {
            model.getEntrant(entrant_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    entrants.add(entrant);
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
        return entrants;
    }
    /**
     * gets the list of Entrants who cancelled
     * @return list of Entrants who cancelled
     */
    public ArrayList<String> getCancelled_list() {
        return cancelled_list;
    }
    public ArrayList<Entrant> getUsableCancelledList() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Entrant> entrants = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(getAttendee_list().size());
        for (String entrant_id: getAttendee_list()) {
            model.getEntrant(entrant_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    entrants.add(entrant);
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
        return entrants;
    }
    /**
     * gets the invited list containing Entrants who have been drawn
     * but haven't confirmed attendance and invites are pending
     * @return list of invited Entrants invited to the Event
     */
    public ArrayList<String> getInvited_list() { return invited_list; }
    public ArrayList<Entrant> getUsableInvitedList() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Entrant> entrants = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(getInvited_list().size());
        for (String entrant_id: getInvited_list()) {
            model.getEntrant(entrant_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    entrants.add(entrant);
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
        return entrants;
    }
    public void invitedListAdd(String entrant){
        invited_list.add(entrant);
    }
    public void invitedListRemove(String entrant){
        invited_list.remove(entrant);
    }
    /**
     * add an Entrant to the waitlist of the Event
     * @param entrant entrant to be added to the waitlist
     */
    public void waitlistAdd(String entrant){
        waitlist.add(entrant);
        DataModel model = new DataModel();
        model.setEvent(this, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Firebase", "written");
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
    }
    public void waitlistRemove(String entrant){
        waitlist.remove(entrant);
    }
    /**
     * adds an Entrant to the attendee list
     * @param entrant Entrant to be added to the attendee_list
     */
    public void attendeeListAdd(String entrant){
        attendee_list.add(entrant);
    }
    public void attendeeListRemove(String entrant){
        attendee_list.remove(entrant);
    }
    /**
     * add an Entrant to the cancelled_list and redraws automaticallu after selected Entrant cancels
     * @param entrant Entrant to be added to the cancelled_list
     */
    public void cancelledListAdd(String entrant) {
        cancelled_list.add(entrant);
    }
    public void cancelledListRemove(String entrant){
        cancelled_list.remove(entrant);
    }


    /**
     * Conducts a lottery draw: randomly selects a number of entrants from
     * the waitlist (up to available attendee spots), removes them from the waitlist,
     * and moves them to the invited list.
     */
    public void doLottery() throws InterruptedException {
        if (waitlist.isEmpty()) {
            System.out.println("No entrants on the waitlist to draw from.");
            return;
        }

        Random rand = new Random();
        int numToDraw = Math.min(attendee_limit - invited_list.size(), waitlist.size());
        CountDownLatch latch = new CountDownLatch(numToDraw);
        for (int i = 0; i < numToDraw; i++) {
            int randomIndex = rand.nextInt(waitlist.size());
            String drawnEntrant = waitlist.remove(randomIndex);
            invited_list.add(drawnEntrant);
            DataModel model = new DataModel();
            Event event = this;
            model.getEntrant(drawnEntrant, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    Invitation invitation = new Invitation(event.uid, entrant.getUid(), "");
                    model.setNotification(invitation, new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("Firebase", "written");
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e("Firebase", "fail");
                        }
                    });
                    entrant.addNotification(invitation.getUid());
                    model.setEntrant(entrant,new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("Firebase", "written");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Firebase", "fail");
                        }
                    });
                    latch.countDown();
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {


                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                    latch.countDown();
                }
            });

        }
        latch.await();
        CountDownLatch rejection_latch = new CountDownLatch(this.getWaitlistAmount());
        for (int i = 0; i < this.getWaitlistAmount(); i++) {

            String rejectedEntrant = this.getWaitlist().get(i);

            DataModel model = new DataModel();
            Event event = this;
            model.getEntrant(rejectedEntrant, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    Entrant entrant = (Entrant) obj;
                    Rejection rejection = new Rejection(event.uid, entrant.getUid(), "");
                    model.setNotification(rejection, new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("Firebase", "written");
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e("Firebase", "fail");
                        }
                    });
                    entrant.addNotification(rejection.getUid());
                    model.setEntrant(entrant,new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String msg) {
                            Log.d("Firebase", "written");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Firebase", "fail");
                        }
                    });
                    rejection_latch.countDown();
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                    rejection_latch.countDown();
                }
            });
        }
        rejection_latch.await();
        drawn = true;
    }
    /**
     * tells whether the Event has already drawn names yet
     * @return whether the Event has already drawn names
     */
    public boolean isDrawn() {
        return drawn;
    }

    /**
     * sets whether the Event has drawn names
     * @param drawn whether the Event has drawn names
     */
    public void setDrawn(boolean drawn) {
        this.drawn = drawn;
    }

    /**
     * edits the information about an existing Event
     * @param date_time date and time of the Event
     * @param location location of the Event
     * @param registration_deadline registration deadline of an event
     * @param details details and information about an Event such as: topic, difficulty, age range etc.
     * @param track_geolocation whether the Event would like to track the waitlist Entrants location
     * @param will_automatically_redraw whether the Event will immediately redraw an Entrant from the waitlist to be added to the invited list
     * @param waitlist_limit maximum amount of Entrants in a waitlist
     * @param attendee_limit maximum amount of Entrant that will attend the event
     */
    public void editEvent(String date_time, String location, String registration_deadline, String details,
                          boolean track_geolocation, boolean will_automatically_redraw, int waitlist_limit, int attendee_limit){
        setDate_time(date_time);
        setLocation(location);
        setRegistration_deadline(registration_deadline);
        setDetails(details);
        setTrack_geolocation(track_geolocation);
        setWillAutomaticallyRedraw(will_automatically_redraw);
        setWaitlist_limit(waitlist_limit);
        setAttendee_limit(attendee_limit);
        DataModel model = new DataModel();
        model.setEvent(this, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                Log.d("Firebase", "written");
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
    }
}
