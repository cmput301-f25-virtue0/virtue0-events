package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.R;
import com.example.lotteryeventapp.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class F_HomePage extends Fragment {
    private static final String ARG_ROLE = "role";
    private int role;
    private DataModel model;

    public static F_HomePage newInstance(int role) {
        F_HomePage fragment = new F_HomePage();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    //role = 0 for entrant, role = 1 for organizer
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.homepage, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.i("CURRENT ROLE", "Current user role is: " + role);
        ViewPager2 pager = view.findViewById(R.id.view_pager);
        TabLayout tabs = view.findViewById(R.id.tab_layout);
        if (getArguments() != null) {
            role = getArguments().getInt(ARG_ROLE);
        }
        Log.i("CURRENT ROLE", "Current user role is: " + role);
        model = ((MainActivity) requireActivity()).getDataModel();
        pager.setAdapter(new ViewPagerAdapter(this, role, model));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabs.getTabAt(position).select();
            }
        });

        //Back button
        view.findViewById(R.id.backButtonHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_SelectRole());
            }
        });

        // Set up page based on role
        if (role == 0) {
            view.findViewById(R.id.Notification).setVisibility(View.VISIBLE);
            view.findViewById(R.id.Profile).setVisibility(View.VISIBLE);
            view.findViewById(R.id.filterButton).setVisibility(View.VISIBLE);
            view.findViewById(R.id.QRCodeButton).setVisibility(View.VISIBLE);
            view.findViewById(R.id.newEventButton).setVisibility(View.INVISIBLE);

            // Detect button presses
            view.findViewById(R.id.Notification).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(F_Notification.newInstance(0));
                }
            });
            view.findViewById(R.id.Profile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_Profile(model));
                }
            });
            view.findViewById(R.id.filterButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ;
                }
            });
            view.findViewById(R.id.QRCodeButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { ; }
            });
        }

        else if (role == 1) {
            view.findViewById(R.id.Notification).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.Profile).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.filterButton).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.QRCodeButton).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.newEventButton).setVisibility(View.VISIBLE);

            // Detect button presses
            view.findViewById(R.id.newEventButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_CreateEditEvent(0, model));
                }
            });

            Log.i("CURRENT ROLE", "Current user role is: " + role);
        }
    }
}
