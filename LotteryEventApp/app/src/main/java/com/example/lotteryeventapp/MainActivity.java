package com.example.lotteryeventapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.      ), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });*/

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
}
