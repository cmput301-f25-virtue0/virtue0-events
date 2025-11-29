package com.example.lotteryeventapp;

import android.util.Log;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class EntrantDataHolder {
    private String name;
    private String email;
    private String phone;
    private String deviceId;
    private ArrayList<String> notifications = new ArrayList<>();
    private boolean notificationOptOut = false;

    private ArrayList<String> waitlistedEvents = new ArrayList<>();
    private ArrayList<String> invitedEvents = new ArrayList<>();
    private ArrayList<String> attendedEvents = new ArrayList<>();
    private ArrayList<Double> location = new ArrayList<>(2);


    public EntrantDataHolder(Entrant entrant) {
        if (entrant.getProfile() != null) {
            this.name = entrant.getProfile().getName();
            this.email = entrant.getProfile().getEmail();
            this.phone = entrant.getProfile().getPhone();
        }
        this.deviceId = entrant.getUid();
        this.notifications.addAll(entrant.getNotifications());
        this.notificationOptOut = entrant.isNotificationOptOut();
        this.waitlistedEvents.addAll(entrant.getWaitlistedEvents());
        this.invitedEvents.addAll(entrant.getInvitedEvents());
        this.attendedEvents.addAll(entrant.getAttendedEvents());
        this.location = entrant.getLocation();
    }


    public EntrantDataHolder(Map<String, Object> data, String deviceId) {
        this.deviceId = deviceId;


        try {

            Object profileObj = data.get("profile");
            if (profileObj instanceof Map) {
                Map<String, Object> profileMap = (Map<String, Object>) profileObj;
                this.name = safeString(profileMap.get("name"));
                this.email = safeString(profileMap.get("email"));
                this.phone = safeString(profileMap.get("phone"));
            } else {

                this.name = safeString(data.get("name"));
                this.email = safeString(data.get("email"));
                this.phone = safeString(data.get("phone"));
            }


            loadList(data, "waitlistedEvents", this.waitlistedEvents);
            loadList(data, "attendedEvents", this.attendedEvents);
            loadList(data, "invitedEvents", this.invitedEvents);
            loadList(data, "notifications", this.notifications);


            this.notificationOptOut = Boolean.TRUE.equals(data.get("notificationOptOut"));
            this.location = (ArrayList<Double>) data.get("location");
        } catch (Exception e) {
            Log.e("EntrantDataHolder", "Error parsing data for " + deviceId, e);
            this.name = "Error Parsing";
        }
    }

    private String safeString(Object o) {
        return o != null ? String.valueOf(o) : "";
    }

    private void loadList(Map<String, Object> data, String key, ArrayList<String> target) {
        Object listObj = data.get(key);
        if (listObj instanceof List) {
            List<?> list = (List<?>) listObj;
            for (Object o : list) {
                if (o != null) target.add(String.valueOf(o));
            }
        }
    }

    public Entrant createEntrantInstance() {
        String finalName = (name == null) ? "Unknown" : name;
        String finalEmail = (email == null) ? "" : email;
        String finalPhone = (phone == null) ? "" : phone;

        Entrant.Profile profile = new Entrant.Profile(finalName, finalEmail, finalPhone);
        Entrant entrant = new Entrant(this.deviceId, profile,this.location);

        entrant.getNotifications().addAll(this.notifications);
        entrant.setNotificationOptOut(this.notificationOptOut);
        entrant.getWaitlistedEvents().addAll(this.waitlistedEvents);
        entrant.getInvitedEvents().addAll(this.invitedEvents);
        entrant.getAttendedEvents().addAll(this.attendedEvents);


        return entrant;
    }


    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDeviceId() { return deviceId; }
    public ArrayList<String> getNotifications() { return notifications; }
    public boolean isNotificationOptOut() { return notificationOptOut; }
    public ArrayList<String> getWaitlistedEvents() { return waitlistedEvents; }
    public ArrayList<String> getInvitedEvents() { return invitedEvents; }
    public ArrayList<String> getAttendedEvents() { return attendedEvents; }

    public ArrayList<Double> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Double> location) {
        this.location = location;
    }

}