package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.AllImagesPagination;
import com.example.lotteryeventapp.FirestorePagination;
import com.example.lotteryeventapp.ImageDataHolder;
import com.example.lotteryeventapp.ImageListAdapter;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class F_AdminImages extends Fragment implements ImageListAdapter.OnImageClickListener {
    private int role;
    private DataModel model;

    public static F_AdminImages newInstance(int myRole){
        F_AdminImages fragment = new F_AdminImages();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

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
        return inflater.inflate(R.layout.admin_image_list, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        model= ((MainActivity) requireActivity()).getDataModel();
        RecyclerView rv = view.findViewById(R.id.rvImages);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));

        // mock data
//        List<String> data = Arrays.asList(
//                "Event Poster 1.jpg",
//                "Profile Picture 2.png",
//                "Event Poster 3.jpg"
//        );
//        F_AdminImages listener = this;
        ArrayList<ImageDataHolder> data = new ArrayList<>();
        ImageListAdapter adapter = new ImageListAdapter(data, this);

        AllImagesPagination pagination = new AllImagesPagination(12);
        pagination.getNextPage(new FirestorePagination.PaginationCallback() {
            @Override
            public <T> void onGetPage(boolean hasResults, ArrayList<T> obs) {
                ArrayList<ImageDataHolder> imageData = (ArrayList<ImageDataHolder>) obs;
                adapter.setItems(imageData);
//                ImageListAdapter adapter = new ImageListAdapter(imageData, listener);

                adapter.notifyDataSetChanged();


            }
            @Override
            public void onError(Exception e) {
                throw new RuntimeException(e);
//                Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();


            }
        });
        rv.setAdapter(adapter);
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmImage);
        toolbar.setNavigationOnClickListener(v -> {
            ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
        });
//        ImageListAdapter adapter = new ImageListAdapter(data, this);
//        rv.setAdapter(adapter);
//
//        MaterialToolbar toolbar = view.findViewById(R.id.toolbarAdmImage);
//        toolbar.setNavigationOnClickListener(v -> {
//            ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
//        });
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
