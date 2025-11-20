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

public class MainActivity extends AppCompatActivity {

    private DataModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        String deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Entrant.Profile profile = new Entrant.Profile("Daniel", "dk8@ualberta.ca", "123-456-7890");
        Entrant entrant = new Entrant(deviceID, profile);
        model = new DataModel();
        model.setCurrentEntrant(entrant);

        //todo: set current organizer to model using model.setCurrentOrganizer(organizer);

        //Send user to choose role page if not previous state is detected
        if (savedInstanceState == null) {
            showFragment(new F_SelectRole());
        }
    }

    public void showFragment(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, newFragment)
                .commit();
    }

    public DataModel getDataModel() {
        return model;
    }


}
