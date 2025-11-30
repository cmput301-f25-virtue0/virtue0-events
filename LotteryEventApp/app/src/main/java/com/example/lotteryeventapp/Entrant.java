package com.example.lotteryeventapp;


import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * This class represents the user(entrants) that participate in the events.  Each entrant has
 * a profile containing name and contact information, and a unique id from their device.
 * Responsibilities include managing their own information and their notification preferences
 */

public class Entrant {
    private Entrant.Profile profile;
    private final String uid;
    private ArrayList<String> notifications = new ArrayList<>();
    private boolean notificationOptOut = false;

    private ArrayList<String> waitlistedEvents = new ArrayList<>();

    private ArrayList<String> invitedEvents = new ArrayList<>();

    private ArrayList<String> attendedEvents = new ArrayList<>();
    private ArrayList<Double> location;




    /**
     * Constructs a new entrant
     * @param uid a unique identifier from their device
     * @param profile a profile object containing contact information
     * @throws NullPointerException if id or profile is null
     */
    public Entrant(String uid, Entrant.Profile profile, ArrayList<Double> location) {
        this.uid = Objects.requireNonNull(uid, "uid");
        this.profile = Objects.requireNonNull(profile, "profile");
        this.location = location;
    }

    /**
     * get list of those attending the event
     * @return list of those attending event
     */
    public ArrayList<String> getAttendedEvents() {
        return attendedEvents;
    }
    /**
     * set list of those attending the event
     * @param attendedEvents list of those attending the event
     */
    public void setAttendedEvents(ArrayList<String> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }
    /**
     * get list of those invited to the event
     * @return list of those invited to the event
     */
    public ArrayList<String> getInvitedEvents() {
        return invitedEvents;
    }

    /**
     * get list of those invited to the event
     * @param invitedEvents list of those invited to event
     */
    public void setInvitedEvents(ArrayList<String> invitedEvents) {
        this.invitedEvents = invitedEvents;
    }

    /**
     * @return entrant's String type ID
     */
    public String getUid() {
        return uid;
    }

    /**
     * @return profile for this entrant
     */
    public Entrant.Profile getProfile() {
        return profile;
    }

    /**
     * @return array of notification objects
     */
    public ArrayList<String> getNotifications() {
        return notifications;
    }


    /**
     * deletes notification
     * @param notification to remove notification
     */
    public void removeNotification(String notification) {
        this.notifications.remove(notification);
        DataModel model = new DataModel();
        model.setEntrant(this, new DataModel.SetCallback() {
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

    /**
     * entrants receiving notifications(invitation)
     * @param notification
     */
    public void addNotification(String notification) {
        this.notifications.add(notification);
    }

    /**
     * this method checks if the user opted out of notifications
     * @return boolean value.  True is the entrant opted out of notification, false if not
     */
    public boolean isNotificationOptOut() {
        return notificationOptOut;
    }

    /**
     * set notification opt-out preferences manually
     * @param out boolean that determines to opt out or not
     */
    public void setNotificationOptOut(boolean out) {
        this.notificationOptOut = out;
    }

    /**
     * gets location of entrant
     * @return location of entrant
     */
    public ArrayList<Double> getLocation() {
        return location;
    }
    /**
     * sets location of entrant
     * @param location location of entrant
     */
    public void setLocation(ArrayList<Double> location) {
        this.location = location;
    }

    /**
     * Represents the entrant's profile containing name, email and phone number
     */
    public static class Profile {
        private String name;
        private String email;
        private String phone;//Keep the phone number as string type?

        /**
         *
         * @param name entrant's name
         * @param email entrant's email
         * @param phone entrant's phone number
         */
        public Profile(String name, String email, String phone) {
            setName(name);
            setEmail(email);
            setPhone(phone);
        }
        public Profile() {
            setName("");
            setEmail("default@example.com");
            setPhone("");
        }

        /**
         * gets the name from profile
         * @return name from profile
         */
        public String getName() {
            return name;
        }

        /**
         * gets email from profile
         * @return email from profile
         */
        public String getEmail() {
            return email;
        }

        /**
         * gets phone from profile
         * @return phone from profile
         */
        public String getPhone() {
            return phone;
        }

        /**
         * sets entrant's name
         * @param name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * sets entrant's email
         * @param email
         */
        public void setEmail(String email) {
            if (email == null || !email.contains("@"))
                throw new IllegalArgumentException("Valid email is required");
            this.email = email;
        }

        /**
         * sets entrant's phone number
         * @param phone
         */
        public void setPhone(String phone) {
            this.phone = phone;

        }
    }

    /**
     * Updates entrant's profile information
     * @param name
     * @param email
     * @param phone
     */

    /**
     * deletes entrant's profile
     */
    public void deleteProfile() {
        DataModel model = new DataModel();
        model.deleteEntrant(this, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }


    /**
     * Compare two user if needed(maybe for duplicate)
     * @param o user to compare to
     * @return true if both have the same id, false if not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrant)) return false;
        return uid.equals(((Entrant) o).uid);
    }

    /**
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    /**
     * add event to waitlisted events
     * @param eventId event to be added to waitlisted events
     */
    public void addWaitlistedEvent(String eventId) {
        if (!waitlistedEvents.contains(eventId)) {
            waitlistedEvents.add(eventId);
        }
    }

    /**
     * remove event from waitlisted event
     * @param eventId event to be removed
     */
    public void removeWaitlistedEvent(String eventId) {
        waitlistedEvents.remove(eventId);
    }

    /**
     * get waitlisted events
     * @return waitlisted events
     */
    public ArrayList<String> getWaitlistedEvents() {
        return waitlistedEvents;
    }
    /**
     * add event to invited events
     * @param eventId event to be added to invited events
     */
    public void addInvitedEvent(String eventId) {
        if (!invitedEvents.contains(eventId)) {
            invitedEvents.add(eventId);
        }
    }
    /**
     * remove event from invited events
     * @param eventId event to be removed
     */
    public void removeInvitedEvent(String eventId) {
        invitedEvents.remove(eventId);
    }
    /**
     * get waitlisted events
     * @return waitlisted events
     */
    public void addAttendedEvent(String eventId) {
        if (!attendedEvents.contains(eventId)) {
            attendedEvents.add(eventId);
        }
    }

    /**
     * remove an event from the attending list
     * @param eventId event to be removed
     */
    public void removeAttendedEvent(String eventId) {
        attendedEvents.remove(eventId);
    }

    /**
     * update info in profile
     * @param name name to be updated
     * @param email email to be updated
     * @param phone phone to be updated
     */
    public void updateProfile(String name, String email, String phone) {
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhone(phone);

        DataModel model = new DataModel();
        model.updateEntrantProfile(this, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String id) {
                Log.d("Firebase", "profile updated");
            }
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "profile update failed", e);
            }
        });
    }
}