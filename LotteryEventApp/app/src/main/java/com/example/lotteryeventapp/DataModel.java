package com.example.lotteryeventapp;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Used for interactions with the database.
 */
public class DataModel {
    private final FirebaseFirestore db;
    private final CollectionReference events;

    public DataModel() {
        this.db = FirebaseFirestore.getInstance();
        this.events = db.collection("events");
        // add more as we go

//        this.events.addSnapshotListener((value, error) -> {
//            if (error != null) {
//                Log.e("Firestore", error.toString());
//            }
//            if (value != null && !value.isEmpty()) {
//                // Collection changed
//                Log.e("Firestore", "'event' collection has been changed");
//            }
//        });

    }

    /**
     * A callback for getting data from the database
     */
    public interface GetCallback {
        /**
         * Called on successful fetch from database
         * @param obj instance of the data retrieved from the database
         */
        void onSuccess(Object obj);

        /**
         * Called on failed fetch from database
         * @param e error message
         */
        void onError(Exception e);
    }

    /**
     * A callback for sending data from the database
     */
    public interface SetCallback {
        /**
         * Called on successful data transfer to database
         * @param id unique id of newly added document
         */
        void onSuccess(String id);

        /**
         * Called on failed data transfer to database
         * @param e error message
         */
        void onError(Exception e);
    }

    /**
     * Attempts to retrieve an event from the database. The retrieved event or an error is passed to the callback.
     * @param eventId event's unique id
     * @param cb instance of GetCallback
     */
    public void getEvent(String eventId, GetCallback cb) {
        DocumentReference eventRef = this.events.document(eventId);
        eventRef.get()
                .addOnSuccessListener(eventSnap -> {
                    if (eventSnap.exists()) {
                        String dateTime = eventSnap.getString("date_time");
                        String location = eventSnap.getString("location");
                        String registrationDeadline = eventSnap.getString("registration_deadline");
                        String details = eventSnap.getString("details");
                        Boolean geolocation = eventSnap.getBoolean("track_geolocation");
                        Boolean redraw = eventSnap.getBoolean("redraw");
                        int waitlistLimit;
                        if (eventSnap.getLong("waitlist_limit") != null) {
                            waitlistLimit = eventSnap.getLong("waitlist_limit").intValue();
                        }else {
                            cb.onError(new RuntimeException("Event field 'waitlist_limit' is null"));
                            return;
                        }
                        int attendeeLimit;
                        if (eventSnap.getLong("attendee_limit") != null) {
                            attendeeLimit = eventSnap.getLong("attendee_limit").intValue();
                        }else {
                            cb.onError(new RuntimeException("Event field 'attendee_limit' is null"));
                            return;
                        }

                        cb.onSuccess(new Event(dateTime, location, registrationDeadline, details,
                                Boolean.TRUE.equals(geolocation), false, waitlistLimit, attendeeLimit));
                    }else {
                        cb.onError(new RuntimeException("Event does not exist"));
                    }
                }).addOnFailureListener(e -> {
                    Log.w("Firestore", "Firestore fetch failed: " + e.getMessage());
                    cb.onError(new RuntimeException("Could not fetch from server"));
                });
    }

    /**
     * Adds the given instance of Event to database. The event's unique id or an error is passed to the callback.
     * @param event Event instance to be added to database
     * @param cb instance of SetCallback
     */
    public void addEvent(Event event, SetCallback cb) {
        this.events.add(event)
                .addOnSuccessListener(documentReference -> {
                    cb.onSuccess(documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    cb.onError(new RuntimeException("Could not create event"));
                });
    }
}
