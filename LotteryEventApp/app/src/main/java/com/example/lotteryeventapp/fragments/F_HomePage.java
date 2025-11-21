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

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.example.lotteryeventapp.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class F_HomePage extends Fragment {
    private static final String ARG_ROLE = "role";
    private static final String ARG_START_TAB = "start_tab";

    private int role;
    private int currentTab = 0;
    private DataModel model;

    public static F_HomePage newInstance(int role) {
        return newInstance(role, 0);
    }

    public static F_HomePage newInstance(int role, int startTab) {
        F_HomePage fragment = new F_HomePage();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        args.putInt(ARG_START_TAB, startTab);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt(ARG_ROLE);
            this.currentTab = getArguments().getInt(ARG_START_TAB, 0);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homepage, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Log.i("CURRENT ROLE", "Current user role is: " + role);

        ViewPager2 pager = view.findViewById(R.id.view_pager);
        TabLayout tabs = view.findViewById(R.id.tab_layout);

        model = ((MainActivity) requireActivity()).getDataModel();

        pager.setAdapter(new ViewPagerAdapter(this, role, model));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                TabLayout.Tab tab = tabs.getTabAt(position);
                if (tab != null && !tab.isSelected()) {
                    tab.select();
                }
            }
        });

        if (currentTab > 0) {
            view.post(() -> {
                pager.setCurrentItem(currentTab, false);
                TabLayout.Tab initialTab = tabs.getTabAt(currentTab);
                if (initialTab != null) initialTab.select();
            });
        }


        view.findViewById(R.id.backButtonHome).setOnClickListener(v ->
                ((MainActivity) requireActivity()).showFragment(new F_SelectRole())
        );

        if (role == 0) {
            view.findViewById(R.id.Notification).setVisibility(View.VISIBLE);
            view.findViewById(R.id.Profile).setVisibility(View.VISIBLE);
            view.findViewById(R.id.filterButton).setVisibility(View.VISIBLE);
            view.findViewById(R.id.QRCodeButton).setVisibility(View.VISIBLE);
            view.findViewById(R.id.newEventButton).setVisibility(View.INVISIBLE);

            view.findViewById(R.id.Notification).setOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(F_Notification.newInstance(0))
            );
            view.findViewById(R.id.Profile).setOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(new F_Profile(model))
            );
        } else if (role == 1) {
            view.findViewById(R.id.Notification).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.Profile).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.filterButton).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.QRCodeButton).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.newEventButton).setVisibility(View.VISIBLE);

            view.findViewById(R.id.newEventButton).setOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(new F_CreateEditEvent(0, model))
            );
        }
    }
}