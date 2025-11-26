package com.example.lotteryeventapp;

import android.util.Log;

/**
 * This class is a notification that the Entrant has been accepted into the Event
 */
public class Invitation extends Notification{
    /**
     * A type of notification sent to an Entrant indicating that they can choose to attend the Event
     * @param event the Event the Entrant could sign up for
     * @param entrant the Entrant that could sign up for the Event
     */
    public Invitation(String uid, String event, String entrant, String msg){
        super(uid, event, entrant, msg);
    }

    public Invitation(String event, String entrant, String msg){
        super(event, entrant, msg);
    }

    /**
     * Entrant chooses to join the attending list
     */
    public void signUp(){
        String event = this.event;
        DataModel model = new DataModel();
        String entrant = this.entrant;
        Invitation invitation = this;
        model.getEvent(event, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }
            @Override
            public void onSuccess(Object obj) {
                Log.d("Firebase", "retrieved");

                Event event = (Event) obj;
                event.invitedListRemove(entrant);
                event.attendeeListAdd(entrant);
                model.setEvent(event, new DataModel.SetCallback() {
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
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
        model.getEntrant(entrant, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }
            @Override
            public void onSuccess(Object obj) {
                Log.d("Firebase", "retrieved");
                Entrant entrant = (Entrant) obj;
                entrant.removeNotification(invitation.getUid());
                model.setEntrant(entrant, new DataModel.SetCallback() {
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
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
    }

    /**
     * Entrant chooses to not attend the Event
     */
    public void decline(){
        String event = this.event;
        DataModel model = new DataModel();
        String entrant = this.entrant;
        Invitation invitation = this;
        model.getEntrant(entrant, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }
            @Override
            public void onSuccess(Object obj) {
                Log.d("Firebase", "retrieved");
                Entrant entrant = (Entrant) obj;
                entrant.removeNotification(invitation.getUid());

            }
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });
        model.getEvent(event, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {

            }
            @Override
            public void onSuccess(Object obj) {
                Log.d("Firebase", "retrieved");

                Event event = (Event) obj;
                event.invitedListRemove(entrant);
                event.cancelledListAdd(entrant);
                try {
                    event.doLottery();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                model.setEvent(event, new DataModel.SetCallback() {
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
            @Override
            public void onError(Exception e) {
                Log.e("Firebase", "fail");
            }
        });

    }
}
