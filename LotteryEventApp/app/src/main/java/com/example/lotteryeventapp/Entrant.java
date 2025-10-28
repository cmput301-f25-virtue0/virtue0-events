package com.example.lotteryeventapp;

import java.util.ArrayList;

public class Entrant {
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
}
