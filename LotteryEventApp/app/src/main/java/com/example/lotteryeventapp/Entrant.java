package com.example.lotteryeventapp;

import android.provider.Settings;

import java.util.ArrayList;
import java.util.Objects;

/**
 * This class represents the user(entrants) that participate in the events.  Each entrant has
 * a profile containing name and contact information, and a unique id from their device.
 * Responsibilities include managing their own information and their notification preferences
 */

public class Entrant {
    private Profile profile;
    private final String id;
    private ArrayList<Notification> notifications = new ArrayList<>();
    private boolean notificationOptOut = false;

    /**
     * Constructs a new entrant
     * @param id a unique identifier from their device
     * @param profile a profile object containing contact information
     * @throws NullPointerException if id or profile is null
     */
    public Entrant(String id, Profile profile) {

        this.id = Objects.requireNonNull(id, "id");
        this.profile = Objects.requireNonNull(profile, "profile");
    }


    /**
     * @return entrant's String type ID
     */
    public String getId() {
        return id;
    }

    /**
     * @return profile for this entrant
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * @return array of notification objects
     */
    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * @param notification to remove notification
     */
    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
    }

    /**
     * entrants receiving notifications(invitation)
     * @param notification
     */
    public void addNotification(Notification notification) {
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

        public String name() {
            return name;
        }

        public String email() {
            return email;
        }

        public String phone() {
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
    public void updateProfile(String name, String email, String phone) {
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhone(phone);
    }

    /**
     * deletes entrant's profile
     */
    public void deleteProfile() {
        // TODO: Implement profile deletion once database is setup
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
        return id.equals(((Entrant) o).id);
    }

    /**
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
