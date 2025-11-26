package com.example.lotteryeventapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lotteryeventapp.fragments.F_BrowseEvents;
import com.example.lotteryeventapp.fragments.F_MyEvents;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int role;
    private DataModel model;

    public ViewPagerAdapter(@NonNull Fragment parent, int myRole, DataModel myModel) {
        super(parent);
        this.role = myRole;
        model = myModel;
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        if (position == 0) return F_BrowseEvents.newInstance(role);
        return F_MyEvents.newInstance(role);
    }

    @Override
    public int getItemCount() { return 2; }
}

