package com.example.lotteryeventapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntrantDataHolder {
    private String name;
    private String email;
    private String phone;
    private String deviceId;
    private ArrayList<String> notifications = new ArrayList<String>();
    private boolean notificationOptOut;

    private ArrayList<String> waitlistedEvents = new ArrayList<>();

    private ArrayList<String> invitedEvents = new ArrayList<>();

    private ArrayList<String> attendedEvents = new ArrayList<>();

    public void setWaitlistedEvents(ArrayList<String> waitlistedEvents) {
        this.waitlistedEvents = waitlistedEvents;
    }

    public ArrayList<String> getInvitedEvents() {
        return invitedEvents;
    }

    public void setInvitedEvents(ArrayList<String> invitedEvents) {
        this.invitedEvents = invitedEvents;
    }

    public ArrayList<String> getAttendedEvents() {
        return attendedEvents;
    }

    public void setAttendedEvents(ArrayList<String> attendedEvents) {
        this.attendedEvents = attendedEvents;
    }

    public ArrayList<String> getWaitlistedEvents() {
        return waitlistedEvents;
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

    public String getDeviceId() {
        return deviceId;
    }

    public ArrayList<String> getNotifications() {
        return notifications;
    }

    public boolean isNotificationOptOut() {
        return notificationOptOut;
    }

    public EntrantDataHolder(Entrant entrant) {
        this.name = entrant.getProfile().getName();
        this.email = entrant.getProfile().getEmail();
        this.phone = entrant.getProfile().getPhone();
        this.deviceId = entrant.getUid();
        this.notifications.addAll(entrant.getNotifications());
        this.notificationOptOut = entrant.isNotificationOptOut();
        this.waitlistedEvents.addAll(entrant.getWaitlistedEvents());
        this.invitedEvents.addAll(entrant.getInvitedEvents());
        this.attendedEvents.addAll(entrant.getAttendedEvents());
    }

    public EntrantDataHolder(Map<String, Object> data, String deviceId) {
        this.name = (String) data.get("name");
        this.email = (String) data.get("email");
        this.phone = (String) data.get("phone");
        this.deviceId = deviceId;

        loadList(data, "waitlistedEvents", this.waitlistedEvents);
        loadList(data, "attendedEvents", this.attendedEvents);
        loadList(data, "invitedEvents", this.invitedEvents);
        loadList(data, "notifications", this.notifications);

        this.notificationOptOut = (Boolean) data.get("notificationOptOut");
    }

    //Helper Method
    private void loadList(Map<String, Object> data, String key, ArrayList<String> target) {
        List<Object> list = (List<Object>) data.get(key);
        if (list != null) {
            for (Object o : list) {
                if (o != null) target.add((String) o);
            }
        }
    }

    public Entrant createEntrantInstance() {
        Entrant.Profile profile = new Entrant.Profile(this.name, this.email, this.phone);
        Entrant entrant = new Entrant(this.deviceId, profile);
        entrant.getNotifications().addAll(this.notifications);
        entrant.setNotificationOptOut(this.notificationOptOut);
        entrant.getWaitlistedEvents().addAll(this.waitlistedEvents);
        entrant.getInvitedEvents().addAll(this.invitedEvents);
        entrant.getAttendedEvents().addAll(this.attendedEvents);

        return entrant;
    }
}
