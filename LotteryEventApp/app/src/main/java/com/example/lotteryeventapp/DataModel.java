package com.example.lotteryeventapp;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Used for interactions with the database.
 */
public class DataModel extends TModel<TView>{
    private final FirebaseFirestore db;
    private final CollectionReference entrants;
    private final CollectionReference organizers;
    private final CollectionReference admins;
    private final CollectionReference events;
    private final CollectionReference notifications;

    private ArrayList<View> views = new ArrayList<View>();

    public DataModel() {
        this.db = FirebaseFirestore.getInstance();
        this.entrants = db.collection("entrants");
        this.organizers = db.collection("organizers");
        this.admins = db.collection("admins");
        this.events = db.collection("events");
        this.notifications = db.collection("notifications");
        // add more as we go

        // Ignore below code chunk, saved for if snapshotListeners are needed at any point
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

        <T extends Enum<T>> void onSuccess(Object obj, T type);

        /**
         * Called on failed fetch from database
         * @param e exception thrown
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
         * @param e exception thrown
         */
        void onError(Exception e);
    }

    public interface DeleteCallback {
        /**
         * Called on successful deletion from database
         */
        void onSuccess();

        /**
         * Called on failed deletion from database
         * @param e exception thrown
         */
        void onError(Exception e);
    }

    public void setEntrant(Entrant entrant, SetCallback cb) {
        EntrantDataHolder data = new EntrantDataHolder(entrant);
        DocumentReference entrantRef = this.entrants.document(entrant.getUid());
        entrantRef.set(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Firestore set succeeded: Entrant " + entrant.getUid());
                    cb.onSuccess(entrant.getUid());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore set failed: Could not set entrant " + entrant.getUid());
                    cb.onError(e);
                });
    }

    public void getEntrant(String deviceId, GetCallback cb) {
        DocumentReference entrantRef = this.entrants.document(deviceId);
        entrantRef.get()
                .addOnSuccessListener(entrantSnap -> {
                    if (entrantSnap.exists()) {
                        EntrantDataHolder entrantData = new EntrantDataHolder(entrantSnap.getData(), deviceId);

                        Log.d("Firestore", "Firestore fetch succeeded: Entrant " + deviceId);
                        cb.onSuccess(entrantData.createEntrantInstance());
                    }else {
                        Log.e("Firestore", "Firestore fetch failed: Entrant " + deviceId + " does not exist");
                        cb.onError(new RuntimeException("Entrant does not exist"));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore fetch failed: Entrant " + deviceId + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    public void deleteEntrant(Entrant entrant, DeleteCallback cb) {
        DocumentReference entrantRef = this.entrants.document(entrant.getUid());
        entrantRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Firestore delete succeeded: Entrant " + entrant.getUid());
                    cb.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore delete failed: Entrant " + entrant.getUid() + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    // Can throw an exception outside of callback
    public void setOrganizer(Organizer organizer, SetCallback cb) {
        OrganizerDataHolder data = new OrganizerDataHolder(organizer);
        DocumentReference organizerRef = this.organizers.document(organizer.getUid());
        organizerRef.set(data)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Firestore set succeeded: Organizer " + organizer.getUid());
                    cb.onSuccess(organizer.getUid());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore set failed: Could not set organizer " + organizer.getUid());
                    cb.onError(e);
                });
    }

    public void getOrganizer(String deviceId, GetCallback cb) {
        DocumentReference organizerRef = this.organizers.document(deviceId);
        organizerRef.get()
                .addOnSuccessListener(organizerSnap -> {
                    if (organizerSnap.exists()) {
                        OrganizerDataHolder organizerData = new OrganizerDataHolder(organizerSnap.getData(), deviceId);

                        Log.d("Firestore", "Firestore fetch succeeded: Organizer " + deviceId);
                        cb.onSuccess(organizerData.createOrganizerInstance());
                    }else {
                        Log.e("Firestore", "Firestore fetch failed: Organizer " + deviceId + " does not exist");
                        cb.onError(new RuntimeException("Organizer does not exist"));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore fetch failed: Organizer " + deviceId + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    public void deleteOrganizer(Organizer organizer, DeleteCallback cb) {
        DocumentReference organizerRef = this.organizers.document(organizer.getUid());
        organizerRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Firestore delete succeeded: Organizer " + organizer.getUid());
                    cb.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore delete failed: Organizer " + organizer.getUid() + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    public void setAdmin() {
        throw new UnsupportedOperationException("Admin not implemented yet");
    }

    public void getAdmin() {
        throw new UnsupportedOperationException("Admin not implemented yet");
    }

    public void deleteAdmin() {
        throw new UnsupportedOperationException("Admin not implemented yet");
    }

    /**
     * Adds or updates the given instance of Event to database. The event's unique id or an exception is passed to the callback.
     * @param event Event instance to be added to database
     * @param cb instance of SetCallback
     */
    public void setEvent(Event event, SetCallback cb) {
        EventDataHolder data = new EventDataHolder(event);

        if (!event.getUid().isEmpty()) {
            // Update existing event
            DocumentReference eventRef = this.events.document(event.getUid());
            eventRef.set(data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Firestore set succeeded: Updated event " + event.getUid());
                        cb.onSuccess(event.getUid());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Firestore set failed: Could not update event " + event.getUid());
                        cb.onError(e);
                    });
        }else {
            // Add new event
            this.events.add(data)
                    .addOnSuccessListener(documentReference -> {
                        event.setUid(documentReference.getId());

                        Log.d("Firestore", "Firestore set succeeded: Added event " + event.getUid());
                        cb.onSuccess(event.getUid());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Firestore set failed: Could not add event");
                        cb.onError(e);
                    });
        }
    }

    /**
     * Attempts to retrieve an event from the database. The retrieved event or an exception is passed to the callback.
     * @param eventId event's unique id
     * @param cb instance of GetCallback
     */
    public void getEvent(String eventId, GetCallback cb) {
        DocumentReference eventRef = this.events.document(eventId);
        eventRef.get()
                .addOnSuccessListener(eventSnap -> {
                    if (eventSnap.exists()) {
                        EventDataHolder eventData = new EventDataHolder(eventSnap.getData(), eventId);

                        Log.d("Firestore", "Firestore fetch succeeded: Event " + eventId);
                        cb.onSuccess(eventData.createEventInstance());
                    }else {
                        Log.e("Firestore", "Firestore fetch failed: Event " + eventId + " does not exist");
                        cb.onError(new RuntimeException("Event does not exist"));
                    }
                }).addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore fetch failed: Event " + eventId + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    public void deleteEvent(Event event, DeleteCallback cb) {
        DocumentReference eventRef = this.events.document(event.getUid());
        eventRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Firestore delete succeeded: Event " + event.getUid());
                    cb.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore delete failed: Event " + event.getUid() + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }
    public void getAllEvents(GetCallback cb){
        ArrayList<Event> events = new ArrayList<Event>();
        db.collection("cities")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                EventDataHolder data = new EventDataHolder(document.getData(),document.getId());
                                events.add(data.createEventInstance());
                            }
                            cb.onSuccess(events);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                            cb.onError(task.getException());
                        }
                    }
                });
    }

    public void setNotification(Notification notif, SetCallback cb) {
        NotificationDataHolder data;
        if (notif instanceof Invitation) {
            data = new InvitationDataHolder((Invitation) notif);
        }else if (notif instanceof Rejection) {
            data = new RejectionDataHolder((Rejection) notif);
        }else {
            throw new RuntimeException("Unknown notification type");
        }

        if (!notif.getUid().isEmpty()) {
            // Update existing notification
            DocumentReference notifRef = this.notifications.document(notif.getUid());
            notifRef.set(data)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "Firestore set succeeded: Organizer " + notif.getUid());
                        cb.onSuccess(notif.getUid());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Firestore set failed: Could not set organizer " + notif.getUid());
                        cb.onError(e);
                    });
        }else {
            // Add new notification
            this.notifications.add(data)
                    .addOnSuccessListener(documentReference -> {
                        notif.setUid(documentReference.getId());

                        Log.d("Firestore", "Firestore set succeeded: Added notification " + notif.getUid());
                        cb.onSuccess(notif.getUid());
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firestore", "Firestore set failed: Could not add notification");
                        cb.onError(e);
                    });
        }
    }

    public void getNotification(String notifId, GetCallback cb) {
        DocumentReference notifRef = this.notifications.document(notifId);
        notifRef.get()
                .addOnSuccessListener(notifSnap -> {
                    if (notifSnap.exists()) {
                        NotificationDataHolder.NotificationType notifType = NotificationDataHolder.NotificationType.valueOf(notifSnap.getString("notification_type"));
                        if (notifType == NotificationDataHolder.NotificationType.INVITATION) {
                            InvitationDataHolder invData = new InvitationDataHolder(notifSnap.getData(), notifId);

                            Log.d("Firestore", "Firestore fetch succeeded: Notification " + notifId);
                            cb.onSuccess(invData.createInvitationInstance(), NotificationDataHolder.NotificationType.INVITATION);
                        }else if (notifType == NotificationDataHolder.NotificationType.REJECTION) {
                            RejectionDataHolder rejData = new RejectionDataHolder(notifSnap.getData(), notifId);

                            Log.d("Firestore", "Firestore fetch succeeded: Rejection " + notifId);
                            cb.onSuccess(rejData.createRejectionInstance(), NotificationDataHolder.NotificationType.REJECTION);
                        }else {
                            Log.e("Firestore", "Firestore fetch failed: Unknown notification type " + notifId);
                            cb.onError(new RuntimeException("Unknown notification type"));
                        }
                    }else {
                        Log.e("Firestore", "Firestore fetch failed: Notification " + notifId + " does not exist");
                        cb.onError(new RuntimeException("Notification does not exist"));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore fetch failed: Notification " + notifId + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }

    public void deleteNotification(Notification notif, DeleteCallback cb) {
        DocumentReference notifRef = this.notifications.document(notif.getUid());
        notifRef.delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Firestore delete succeeded: Notification " + notif.getUid());
                    cb.onSuccess();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Firestore delete failed: Notification " + notif.getUid() + "\n" + e.getMessage());
                    cb.onError(e);
                });
    }
}
