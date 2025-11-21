package com.example.lotteryeventapp;

import android.provider.Settings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * This class represents the user(entrants) that participate in the events.  Each entrant has
 * a profile containing name and contact information, and a unique id from their device.
 * Responsibilities include managing their own information and their notification preferences
 */

public class Entrant {
    private Profile profile;
    private final String uid;
    private ArrayList<String> notifications = new ArrayList<>();
    private boolean notificationOptOut = false;

    private ArrayList<String> waitlistedEvents = new ArrayList<>();


    /**
     * Constructs a new entrant
     * @param uid a unique identifier from their device
     * @param profile a profile object containing contact information
     * @throws NullPointerException if id or profile is null
     */
    public Entrant(String uid, Profile profile) {
        this.uid = Objects.requireNonNull(uid, "uid");
        this.profile = Objects.requireNonNull(profile, "profile");
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
    public Profile getProfile() {
        return profile;
    }

    /**
     * @return array of notification objects
     */
    public ArrayList<String> getNotifications() {
        return notifications;
    }
    public ArrayList<Notification> getUsableNotifications() throws InterruptedException {
        DataModel model = new DataModel();
        ArrayList<Notification> notifications = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(getNotifications().size());
        for (String notification_id: getNotifications()) {
            model.getNotification(notification_id, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                    Notification notification = (Notification) obj;
                    notifications.add(notification);
                    latch.countDown();
                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");

                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                    latch.countDown();
                }
            });


        }
        latch.await();
        return notifications;
    }

    /**
     * @param notification to remove notification
     */
    public void removeNotification(String notification) {
        this.notifications.remove(notification);
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
            setEmail("default@email");
            setPhone("");
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

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
    public void updateProfile(String name, String email, String phone) {
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhone(phone);
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

    public void addWaitlistedEvent(String eventId) {
        if (!waitlistedEvents.contains(eventId)) {
            waitlistedEvents.add(eventId);
        }
    }

    public void removeWaitlistedEvent(String eventId) {
        waitlistedEvents.remove(eventId);
    }

    public ArrayList<String> getWaitlistedEvents() {
        return waitlistedEvents;
    }

}
