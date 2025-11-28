package com.example.lotteryeventapp.fragments;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<Entrant.Profile> myEntrants = getEntrants();

        LatLng pos = new LatLng(53.5267, -115.5256);
        gmap.addMarker(new MarkerOptions().position(pos).title("testmarker"));
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10));

        /*float minx = 99999;
        float maxx = -99999;
        float miny = 99999;
        float maxy = -99999;

        for (int i = 0; i < myEntrants.size(); i++) {
            Entrant.Profile thisProfile = myEntrants.get(i);
            //todo: get location from profile
            float x = 0;
            float y = 0;
            String name = "placeholder";

            LatLng pos = new LatLng(x, y);
            if (x < minx) {minx = x;}
            if (x > maxx) {maxx = x;}
            if (y < miny) {miny = y;}
            if (y > maxy) {maxy = y;}

            gmap.addMarker(new MarkerOptions().position(pos).title(name));
        }
        //todo: get zoom based on max and min x & y
        float posx = minx + (maxx - minx) / 2;
        float posy = miny + (maxy - miny) / 2;
        LatLng position = new LatLng(posx, posy);
        int zoom = 1;
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));*/
    }

    public ArrayList<Entrant.Profile> getEntrants() {
        //todo: add a way of fetching all entrants from the waiting list, chosen, and enrolled.
        return new ArrayList<Entrant.Profile>();
    }
}