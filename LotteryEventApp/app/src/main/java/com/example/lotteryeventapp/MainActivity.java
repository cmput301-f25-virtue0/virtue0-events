package com.example.lotteryeventapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.fragments.F_SelectRole;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DataModel model;
    private Entrant entrant;
    private Organizer organizer;
    private int activeHomePageTab = 0;
    private FusedLocationProviderClient fusedLocationClient;
    private ArrayList<Double> entrantLocation = new ArrayList<>(Arrays.asList(0.0, 0.0));
    private static final int PERMISSION_REQUEST_CODE = 123;


    private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        model = new DataModel();
        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };

        // much of this code is taken from GeeksforGeeks: https://www.geeksforgeeks.org/android/android-how-to-request-permissions-in-android-application/
        List<String> permissionsToRequest = new ArrayList<>();

        // Filter out the permissions that are not yet granted
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        // If there are permissions that need to
        // be requested, ask the user for them
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]), // Convert list to array
                    PERMISSION_REQUEST_CODE // Pass the request code
            );
        } else {
            // All permissions are already granted
            Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show();
        }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
//            public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                      int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            entrantLocation.set(0, location.getLongitude());
                            entrantLocation.set(1, location.getLatitude());
                            loadEntrant(deviceID);
                            loadOrganizer(deviceID);
                        } else {
                            entrantLocation.set(0, null);
                            entrantLocation.set(1, null);
                            loadEntrant(deviceID);
                            loadOrganizer(deviceID);
                        }
                    }
                });


        //Send user to choose role page if not previous state is detected
        if (savedInstanceState == null) {
            showFragment(new F_SelectRole());
        }
    }

    private void requestPermissions() {

        // List of permissions the app may need
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        List<String> permissionsToRequest = new ArrayList<>();

        // Filter out the permissions that are not yet granted
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        // If there are permissions that need to
        // be requested, ask the user for them
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]), // Convert list to array
                    PERMISSION_REQUEST_CODE // Pass the request code
            );
        } else {
            // All permissions are already granted
            Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            List<String> deniedPermissions = new ArrayList<>();

            // Check which permissions were denied
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }

            if (deniedPermissions.isEmpty()) {

                // All permissions granted
                Toast.makeText(this, "All permissions granted", Toast.LENGTH_SHORT).show();
            } else {

                // Some permissions were denied, show them in a Toast
                Toast.makeText(this, "Permissions denied: " + deniedPermissions, Toast.LENGTH_LONG).show();
            }
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
                entrant.setLocation(entrantLocation);
                model.setEntrant(entrant, new DataModel.SetCallback() {
                    @Override
                    public void onSuccess(String id) {
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
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
        entrant = new Entrant(deviceID, profile, entrantLocation);

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


    public Entrant getEntrant() {
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
                if (obj == null) {
                    Log.d("OrganizerLoad", "Organizer not found (obj is null), creating new.");
                    createNewOrganizer(deviceId);
                    return;
                }
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

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }
}

