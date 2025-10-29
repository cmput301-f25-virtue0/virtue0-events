package com.example.lotteryeventapp;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public interface Callback {
        void onSuccess(Object obj);
        void onError(Exception e);
    }

    public void getEvent(String eventId, Callback cb) {
        DocumentReference eventRef = this.events.document(eventId);
        eventRef.get()
                .addOnSuccessListener(eventSnap -> {
                    if (eventSnap.exists()) {
                        String dateTime = eventSnap.getString("date_time");
                        String location = eventSnap.getString("location");
                        String registrationDeadline = eventSnap.getString("registration_deadline");
                        String details = eventSnap.getString("details");
                        Boolean geolocation = eventSnap.getBoolean("track_geolocation");
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
}
