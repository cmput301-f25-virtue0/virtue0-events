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
    }

    public EntrantDataHolder(Map<String, Object> data, String deviceId) {
        this.name = (String) data.get("name");
        this.email = (String) data.get("email");
        this.phone = (String) data.get("phone");
        this.deviceId = deviceId;

        List<Object> notifications = (List<Object>) data.get("notifications");
        for (Object o: notifications) {
            this.notifications.add((String) o);
        }

        this.notificationOptOut = (Boolean) data.get("notificationOptOut");
    }

    public Entrant createEntrantInstance() {
        Entrant.Profile profile = new Entrant.Profile(this.name, this.email, this.phone);
        Entrant entrant = new Entrant(this.deviceId, profile);
        entrant.getNotifications().addAll(this.notifications);
        entrant.setNotificationOptOut(this.notificationOptOut);

        return entrant;
    }
}
