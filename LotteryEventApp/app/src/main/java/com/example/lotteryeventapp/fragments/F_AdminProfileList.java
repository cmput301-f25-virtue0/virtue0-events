package com.example.lotteryeventapp.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lotteryeventapp.R;


public class F_AdminProfileList extends Fragment{
    private int role;

    public F_AdminProfileList(int myRole) {
        this.role = myRole;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ){
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.admin_profile_list, container, false);

    }
}
