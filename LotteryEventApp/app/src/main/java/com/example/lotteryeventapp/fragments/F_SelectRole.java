package com.example.lotteryeventapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;
import com.google.api.Distribution;

public class F_SelectRole extends Fragment {
    private DataModel model;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.select_role, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        model = ((MainActivity) requireActivity()).getDataModel();
        // Detect button presses
        view.findViewById(R.id.btnEntrant).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getCurrentOrganizer() == null || model.getCurrentEntrant() == null) {
                    Toast.makeText(getContext(), "Loading profile... please wait.", Toast.LENGTH_SHORT).show();
                    return;
                }


                ((MainActivity) requireActivity()).setActiveHomePageTab(0);
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));
            }
        });

        view.findViewById(R.id.btnOrganizer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (model.getCurrentOrganizer() == null ||  model.getCurrentEntrant() == null) {
                    Toast.makeText(getContext(), "Loading profile... please wait.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((MainActivity) requireActivity()).setActiveHomePageTab(0);
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            }
        });

        view.findViewById(R.id.btnAdmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = new EditText(requireContext());
                username.setHint("Username");
                EditText password = new EditText(requireContext());
                password.setHint("Password");
                password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

                LinearLayout layout = new LinearLayout(requireContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(username);
                layout.addView(password);

                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Admin Login")
                        .setView(layout)
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Log in", null)
                        .create();

                dialog.show();


                Button loginButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                loginButton.setOnClickListener(v -> {
                    String uname = username.getText().toString();
                    String pword = password.getText().toString();
                    if (uname.equals("Admin1") && pword.equals("123456")) {
                        dialog.dismiss();
                        ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
                    } else {
                        Toast.makeText(requireContext(),
                                "Invalid Username or Password",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}
