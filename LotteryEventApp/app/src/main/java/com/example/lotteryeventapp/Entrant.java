package com.example.lotteryeventapp;

import java.util.ArrayList;

public class Entrant {
    private Profile profile;
    private ArrayList<Notification> notifications;
    public Entrant(){
        this.notifications = new ArrayList<>();
    }

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void removeNotification(Notification notification){
        this.notifications.remove(notification);
    }
    public void addNotification(Notification notification){
        this.notifications.add(notification);
    }

    // ============================= Profile ===============================
    public static class Profile{  //Would organizer and administrator also have profiles??
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
}
