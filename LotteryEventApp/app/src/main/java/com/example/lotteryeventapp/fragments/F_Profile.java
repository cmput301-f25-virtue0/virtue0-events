package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;

public class F_Profile extends Fragment {
    private DataModel model;

    //role = 0 for entrant, role = 1 for organizer
    public F_Profile(DataModel myModel) {
        model = myModel;
    }

    private ImageButton btnEditName, btnEditEmail, btnEditPhone;
    private TextView tvName, tvEmail, tvPhone;
    private Switch notificationSwitch;

    private Entrant entrant; //temporary entrant for now

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Detect button presses
        view.findViewById(R.id.backArrowProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(new F_HomePage(0, model));
            }});

        Entrant.Profile p = new Entrant.Profile("John", "John@gmail.com", "780-123-4567");
        entrant = new Entrant("1234", p);

        btnEditName = view.findViewById(R.id.btnEditName);
        btnEditEmail = view.findViewById(R.id.btnEditEmail);
        btnEditPhone = view.findViewById(R.id.btnEditPhone);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);

        btnEditName.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(entrant.getProfile().getName());
            new AlertDialog.Builder(requireContext())
                    .setView(input)
                    .setTitle("Edit Name")
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newName = input.getText().toString();
                        entrant.updateProfile(newName, entrant.getProfile().getEmail(), entrant.getProfile().getPhone());
                        tvName.setText(newName);
                        Toast.makeText(requireContext(), "Name updated", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Cancel", null).show();
        });

        btnEditEmail.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(entrant.getProfile().getEmail());
            new AlertDialog.Builder(requireContext())
                    .setView(input)
                    .setTitle("Edit Email")
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newEmail = input.getText().toString();
                        entrant.updateProfile(entrant.getProfile().getName(), newEmail, entrant.getProfile().getPhone());
                        tvEmail.setText(newEmail);
                        Toast.makeText(requireContext(), "Email updated", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Cancel", null).show();

        });

        btnEditPhone.setOnClickListener(v -> {
            EditText input = new EditText(requireContext());
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText(entrant.getProfile().getPhone());
            new AlertDialog.Builder(requireContext())
                    .setView(input)
                    .setTitle("Edit Phone Number")
                    .setPositiveButton("Save", (dialog, which) -> {
                        String newPhone = input.getText().toString();
                        entrant.updateProfile(entrant.getProfile().getName(), newPhone, entrant.getProfile().getPhone());
                        tvEmail.setText(newPhone);
                        Toast.makeText(requireContext(), "Phone updated", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Cancel", null).show();

        });
//For the toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entrant.setNotificationOptOut(!entrant.isNotificationOptOut());
//            EntrantRepository.getInstance().save(entrant); //example for now, when database completed
        });


    }
}
