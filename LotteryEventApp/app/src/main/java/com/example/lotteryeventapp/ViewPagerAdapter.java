package com.example.lotteryeventapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.lotteryeventapp.fragments.F_BrowseEvents;
import com.example.lotteryeventapp.fragments.F_MyEvents;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment parent) {
        super(parent);
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new F_BrowseEvents();
        return new F_MyEvents();
    }

    @Override
    public int getItemCount() { return 2; }
}

