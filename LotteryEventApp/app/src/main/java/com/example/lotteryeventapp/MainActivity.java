package com.example.lotteryeventapp;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.content.Context;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.fragments.F_SelectRole;
import com.example.lotteryeventapp.DataModel;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.Firebase;

public class MainActivity extends AppCompatActivity {

    private DataModel model;
    private Entrant entrant;
    public static Organizer organizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        model = new DataModel();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("DeviceID", "Android ID = " + deviceID);

        loadEntrant(deviceID);

//        Entrant.Profile profile = new Entrant.Profile();
//        this.entrant = new Entrant(deviceID, profile);
//        model.setEntrant(this.entrant, new DataModel.SetCallback() {
//            @Override
//            public void onSuccess(String msg) {
//                Log.d("Firebase", "written");
//            }
//            @Override
//            public void onError(Exception e) {
//                Log.e("Firebase", "fail");
//            }
//        });
//        model.setCurrentEntrant(entrant);

        try {
            model.getOrganizer(deviceID, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    organizer = (Organizer) obj;

                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                }
            });
        }catch (Exception RuntimeError){

            this.organizer = new Organizer(deviceID);
            model.setOrganizer(this.organizer, new DataModel.SetCallback() {
                @Override
                public void onSuccess(String msg) {
                    Log.d("Firebase", "written");
                }
                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                }
            });
            model.setCurrentOrganizer(this.organizer);
        }


//
//        this.organizer = new Organizer(deviceID);
//        model.setOrganizer(this.organizer, new DataModel.SetCallback() {
//            @Override
//            public void onSuccess(String msg) {
//                Log.d("Firebase", "written");
//            }
//            @Override
//            public void onError(Exception e) {
//                Log.e("Firebase", "fail");
//            }
//        });
//        model.setCurrentOrganizer(this.organizer);


        //todo: set current organizer to model using model.setCurrentOrganizer(organizer);

        //Send user to choose role page if not previous state is detected
        if (savedInstanceState == null) {
            showFragment(new F_SelectRole());
        }
    }

    private void loadEntrant(String deviceID) {
        Log.d("EntrantLoad", "Loading entrant for deviceID = " + deviceID);

        model.getEntrant(deviceID, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
            }
            @Override
            public void onSuccess(Object obj) {
                if (obj == null) {
                    Log.d("EntrantLoad", "No entrant found for this device, create new for " + deviceID);
                    createNewEntrant(deviceID);
                    return;
                }
                Log.d("EntrantLoad", "entrant retrieved");
                entrant = (Entrant) obj;
                model.setCurrentEntrant(entrant);
            }

            @Override
            public void onError(Exception e) {
                Log.e("EntrantLoad", "failed, creating new.", e);
                createNewEntrant(deviceID);
            }
        });
    }
    public void createNewEntrant(String deviceID) {
        Log.d("EntrantLoad", "Creating new entrant for deviceID = " + deviceID);

        Entrant.Profile profile = new Entrant.Profile();
        entrant = new Entrant(deviceID, profile);

        Log.d("EntrantLoad", "About to call setEntrant for uid = " + entrant.getUid());

        model.setEntrant(entrant, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String id) {
                Log.d("EntrantLoad", "setEntrant.onSuccess, New entrant with id: " + id);
            }

            @Override
            public void onError(Exception e) {
                Log.d("EntrantLoad", "setEntrant.onError, Failed to write new entrant", e);

            }
        });
        model.setCurrentEntrant(entrant);
    }

    public void showFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, newFragment)
                .commit();
    }

    public DataModel getDataModel() {
        return model;
    }


    public Entrant getEntrant(){
        return this.entrant;
    }

    public Organizer getOrganizer() {
        return this.organizer;
    }
}