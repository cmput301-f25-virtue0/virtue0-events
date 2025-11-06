package com.example.lotteryeventapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lotteryeventapp.fragments.F_BrowseEvents;
import com.example.lotteryeventapp.fragments.F_MyEvents;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int role;

    public ViewPagerAdapter(@NonNull Fragment parent, int myRole) {
        super(parent);
        this.role = myRole;
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new F_BrowseEvents(role);
        return new F_MyEvents();
    }

    @Override
    public int getItemCount() { return 2; }
}

