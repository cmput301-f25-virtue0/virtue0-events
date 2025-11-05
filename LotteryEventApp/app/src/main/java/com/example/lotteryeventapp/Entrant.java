package com.example.lotteryeventapp;

import android.provider.Settings;

import java.util.ArrayList;
import java.util.Objects;

public class Entrant {
    private Profile profile;
    private final String id; //String for now, implement id by mobile device later

    private ArrayList<Notification> notifications = new ArrayList<>();

    public Entrant(String id, Profile profile) {

        this.id = Objects.requireNonNull(id, "id");
        this.profile = Objects.requireNonNull(profile, "profile");
    }

    public String getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }


    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void removeNotification(Notification notification) {
        this.notifications.remove(notification);
    }

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
    }

    // ============================= Profile ===============================
    public static class Profile {
        private String name;
        private String email;
        private String phone;//Keep the phone number as string type?

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

        public void setName(String name) {
            this.name = name;
        }

        public void setEmail(String email) {
            if (email == null || !email.contains("@"))
                throw new IllegalArgumentException("Valid email is required");
            this.email = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;

        }
    }

    //==========================Updating Profile========================
    public void updateProfile(String name, String email, String phone) {
        profile.setName(name);
        profile.setEmail(email);
        profile.setPhone(phone);
    }

    public void deleteProfile() {
    }


    //===========================Method for comparing entrants if ever needed
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entrant)) return false;
        return id.equals(((Entrant) o).id);
    }


}
