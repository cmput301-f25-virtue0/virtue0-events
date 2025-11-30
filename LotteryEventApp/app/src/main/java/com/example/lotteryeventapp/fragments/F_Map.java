package com.example.lotteryeventapp.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class F_Map extends Fragment implements OnMapReadyCallback {

    private int role;
    private DataModel model;
    private Event event;

    private GoogleMap gmap;
    private TextView tvWaitlistSize;
    private TextView tvCount;

    public static F_Map newInstance(int myRole){
        F_Map fragment = new F_Map();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CRITICAL: Retrieve the 'role' argument here
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout
        return inflater.inflate(R.layout.map, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        // Toolbar setup
        MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_Applicants());
        });

        // Set up the map view
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        if (!event.willTrack_geolocation()) return;

        new Thread(() -> {

            ArrayList<Entrant> myEntrants = getEntrants(); // still catches InterruptedException inside getEntrants()
            if (myEntrants.isEmpty()) return;

            double minx = Double.MAX_VALUE;
            double maxx = -Double.MAX_VALUE;
            double miny = Double.MAX_VALUE;
            double maxy = -Double.MAX_VALUE;

            // Prepare markers
            ArrayList<MarkerOptions> markerOptionsList = new ArrayList<>();

            for (Entrant thisEntrant : myEntrants) {
                ArrayList<Double> thisPos = thisEntrant.getLocation();
                double y = thisPos.get(0);
                double x = thisPos.get(1);

                Log.d("coords", y + ", " + x);

                String name = thisEntrant.getProfile().getName();

                // Geocoding in background thread
                if (x >= -90 && x <= 90 && y >= -180 && y <= 180) {
                    try {
                        Geocoder geo = new Geocoder(requireContext(), Locale.getDefault());
                        List<Address> results = geo.getFromLocation(x, y, 1);
                        if (results != null && !results.isEmpty()) {
                            Address addr = results.get(0);
                            if (addr.getLocality() != null) {
                                name += ", " + addr.getLocality();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                LatLng myLatLng = new LatLng(x, y);
                markerOptionsList.add(new MarkerOptions().position(myLatLng).title(name));

                // Update bounds
                minx = Math.min(minx, x);
                maxx = Math.max(maxx, x);
                miny = Math.min(miny, y);
                maxy = Math.max(maxy, y);
            }

            double finalMinx = minx, finalMaxx = maxx, finalMiny = miny, finalMaxy = maxy;

            // Post UI updates to main thread
            requireActivity().runOnUiThread(() -> {
                // Add markers to the map
                for (MarkerOptions options : markerOptionsList) {
                    gmap.addMarker(options);
                }

                // Animate camera to fit bounds
                if (!markerOptionsList.isEmpty()) {
                    LatLng southwest = new LatLng(finalMinx, finalMiny);
                    LatLng northeast = new LatLng(finalMaxx, finalMaxy);
                    LatLngBounds bounds = new LatLngBounds(southwest, northeast);
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
                }
            });
        }).start();
    }

    public ArrayList<Entrant> getEntrants() {
        ArrayList<Entrant> total = new ArrayList<Entrant>();
        try {
            ArrayList<Entrant> waitlist = event.getUsableWaitList();
            ArrayList<Entrant> invited = event.getUsableInvitedList();
            ArrayList<Entrant> attendees = event.getUsableAttendeeList();
            total.addAll(waitlist);
            total.addAll(invited);
            total.addAll(attendees);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return total;
    }
}