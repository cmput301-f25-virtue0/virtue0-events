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
    public F_Profile(DataModel myModel) {
        model = myModel;
    }

    private ImageButton btnEditName, btnEditEmail, btnEditPhone;
    private TextView tvName, tvEmail, tvPhone, tvUID;
    private Switch notificationSwitch;

    private Entrant entrant;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.profile, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.backArrowProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));
            }});

        MainActivity activity = (MainActivity) requireActivity();
        model = activity.getDataModel();
        entrant = activity.getEntrant();

        tvName = view.findViewById(R.id.UserName);
        tvEmail = view.findViewById(R.id.Email);
        tvPhone = view.findViewById(R.id.UserPhone);
        tvUID = view.findViewById(R.id.userID);

        btnEditName = view.findViewById(R.id.btnEditName);
        btnEditEmail = view.findViewById(R.id.btnEditEmail);
        btnEditPhone = view.findViewById(R.id.btnEditPhone);
        notificationSwitch = view.findViewById(R.id.notificationSwitch);
        ImageButton btnLotteryInfo = view.findViewById(R.id.lotteryExplanationBtn);

        if (entrant != null) {
            tvName.setText(entrant.getProfile().getName());
            tvEmail.setText(entrant.getProfile().getEmail());
            tvPhone.setText(entrant.getProfile().getPhone());
            tvUID.setText(entrant.getUid());
        }

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
                        entrant.updateProfile(entrant.getProfile().getName(), entrant.getProfile().getEmail(), newPhone);
                        tvPhone.setText(newPhone);
                        Toast.makeText(requireContext(), "Phone updated", Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Cancel", null).show();

        });
//For the toggle
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            entrant.setNotificationOptOut(!entrant.isNotificationOptOut());
//            EntrantRepository.getInstance().save(entrant); //example for now, when database completed
        });

        btnLotteryInfo.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.lottery_explanation_title)
                    .setMessage(R.string.lottery_explanation_body)
                    .setPositiveButton(R.string.close, (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });

    }
}
