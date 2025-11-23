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
    private Organizer organizer;
    private int activeHomePageTab = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        model = new DataModel();
        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        loadEntrant(deviceID);
        loadOrganizer(deviceID);

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

//        try {
//            model.getOrganizer(deviceID, new DataModel.GetCallback() {
//                @Override
//                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
//
//                }
//                @Override
//                public void onSuccess(Object obj) {
//                    Log.d("Firebase", "retrieved");
//                    organizer = (Organizer) obj;
//
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Log.e("Firebase", "fail");
//                }
//            });
//        }catch (Exception RuntimeError){
//
//            this.organizer = new Organizer(deviceID);
//            model.setOrganizer(this.organizer, new DataModel.SetCallback() {
//                @Override
//                public void onSuccess(String msg) {
//                    Log.d("Firebase", "written");
//                }
//                @Override
//                public void onError(Exception e) {
//                    Log.e("Firebase", "fail");
//                }
//            });
//            model.setCurrentOrganizer(this.organizer);
//        }



        //Send user to choose role page if not previous state is detected
        if (savedInstanceState == null) {
            showFragment(new F_SelectRole());
        }
    }
    private void loadEntrant(String deviceID) {

        model.getEntrant(deviceID, new DataModel.GetCallback() {
            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
            }
            @Override
            public void onSuccess(Object obj) {
                //if entrant does not exists (null), create new entrant
                if (obj == null) {
                    Log.d("EntrantLoad", "Entrant not found (obj is null), creating new.");
                    createNewEntrant(deviceID);
                    return;
                }

                Log.d("Firebase", "retrieved");
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

    private void loadOrganizer(String deviceId) {
        Log.d("OrganizerLoad", "Loading organizer for deviceID = " + deviceId);

        model.getOrganizer(deviceId, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                // FOUND: Organizer exists in database
                Log.d("OrganizerLoad", "Organizer retrieved");
                organizer = (Organizer) obj;
                model.setCurrentOrganizer(organizer);
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                // Not used
            }

            @Override
            public void onError(Exception e) {
                Log.e("OrganizerLoad", "Failed to load organizer, creating new one. Error: " + e.getMessage());
                createNewOrganizer(deviceId);
            }
        });
    }

    private void createNewOrganizer(String deviceId) {
        Log.d("OrganizerLoad", "Creating new organizer for deviceID = " + deviceId);

        // Create the new object
        organizer = new Organizer(deviceId);

        // Save it to Firebase
        model.setOrganizer(organizer, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String id) {
                Log.d("OrganizerLoad", "New organizer created and saved.");
            }

            @Override
            public void onError(Exception e) {
                Log.e("OrganizerLoad", "Failed to save new organizer", e);
            }
        });

        // Set it as current in the model
        model.setCurrentOrganizer(organizer);
    }

    public int getActiveHomePageTab() {
        return activeHomePageTab;
    }

    public void setActiveHomePageTab(int tabIndex) {
        this.activeHomePageTab = tabIndex;
    }
}
