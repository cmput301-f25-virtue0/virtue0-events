package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.ImageListAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;

public class F_AdminImages extends Fragment implements ImageListAdapter.OnImageClickListener {
    private int role;
    private DataModel model;

    public F_AdminImages(int myRole, DataModel myModel) {
        this.role = myRole;
        model = myModel;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.admin_image_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        RecyclerView rv = view.findViewById(R.id.rvImages);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // mock data
        List<String> data = Arrays.asList(
                "Event Poster 1.jpg",
                "Profile Picture 2.png",
                "Event Poster 3.jpg"
        );


        ImageListAdapter adapter = new ImageListAdapter(data, this);
        rv.setAdapter(adapter);

        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmImage);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(new F_AdminHomePage(2, model));
        });
    }

    @Override
    public void onImageClick(String imageLabel, int position) {
        Toast.makeText(requireContext(), "Clicked on: " + imageLabel, Toast.LENGTH_SHORT).show();
        // TODO: Maybe open a full-screen image view
    }

    @Override
    public void onDeleteClick(String imageLabel, int position) {
        Toast.makeText(requireContext(), "Delete: " + imageLabel, Toast.LENGTH_SHORT).show();
        // TODO: Add code to delete the image from firestore and maybe refresh adapter
    }
}
