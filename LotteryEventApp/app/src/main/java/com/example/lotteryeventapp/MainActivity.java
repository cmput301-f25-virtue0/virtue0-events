package com.example.lotteryeventapp;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.fragments.F_SelectRole;

public class MainActivity extends AppCompatActivity {
    private Entrant entrant;
    public static Organizer organizer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        DataModel model = new DataModel();

        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            model.getEntrant(deviceID, new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }
                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    entrant = (Entrant) obj;

                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                }
            });
        }catch (Exception RuntimeError){
            Entrant.Profile profile = new Entrant.Profile();

            this.entrant = new Entrant(deviceID, profile);
            model.setEntrant(this.entrant, new DataModel.SetCallback() {
                @Override
                public void onSuccess(String msg) {
                    Log.d("Firebase", "written");
                }
                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                }
            });
            model.setCurrentEntrant(entrant);
        }
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
            showFragment(new F_SelectRole(model));
        }
    }

    public void showFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, newFragment)
                .commit();
    }
    public Entrant getEntrant(){
        return this.entrant;
    }

    public Organizer getOrganizer() {
        return this.organizer;
    }
}
