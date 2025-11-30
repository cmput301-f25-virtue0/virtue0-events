package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.lotteryeventapp.CaptureAct;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.R;
import com.example.lotteryeventapp.ViewPagerAdapter;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class F_HomePage extends Fragment {

    private static final String ARG_ROLE = "role";

    private int role;
    private int currentTab = 0;
    private DataModel model;

    private Set<Event.EventTag> activeFilters = new HashSet<>();


    public static F_HomePage newInstance(int role) {
        F_HomePage fragment = new F_HomePage();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, role);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.role = getArguments().getInt(ARG_ROLE);
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

        int savedTab = ((MainActivity) requireActivity()).getActiveHomePageTab();

        pager.setCurrentItem(savedTab, false); // false disables ViewPager scroll animation

        TabLayout.Tab initialTab = tabs.getTabAt(savedTab);
        if (initialTab != null) {
            initialTab.select(); // Selects the tab before the view is drawn
        }

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                ((MainActivity) requireActivity()).setActiveHomePageTab(tab.getPosition());
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
                ((MainActivity) requireActivity()).setActiveHomePageTab(position);
            }
        });


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

            view.findViewById(R.id.QRCodeButton).setOnClickListener(v->
            {
                scanCode();
            });

            view.findViewById(R.id.filterButton).setOnClickListener(v -> {
                showFilterDialog();
            });


        } else if (role == 1) {
            view.findViewById(R.id.Notification).setVisibility(View.GONE);
            view.findViewById(R.id.Profile).setVisibility(View.GONE);
            view.findViewById(R.id.filterButton).setVisibility(View.GONE);
            view.findViewById(R.id.QRCodeButton).setVisibility(View.GONE);
            view.findViewById(R.id.newEventButton).setVisibility(View.VISIBLE);

            view.findViewById(R.id.newEventButton).setOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(F_CreateEditEvent.newInstance(0))
            );
        }
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(false);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String scannedEventId = result.getContents();
            Log.d("QRScan", "Scanned ID: " + scannedEventId);
            model = ((MainActivity) requireActivity()).getDataModel();
            model.getEvent(scannedEventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    // Event found. Cast it and set it as current
                    Event scannedEvent = (Event) obj;
                    model.setCurrentEvent(scannedEvent);

                    if (isAdded() && getActivity() != null) {
                        Toast.makeText(getContext(), "Found Event: " + scannedEvent.getTitle(), Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(0));
                    }
            }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                    // Not used here
                }

                @Override
                public void onError(Exception e) {
                    Log.e("QRScan", "Event not found", e);
                    if (getContext() != null) {
                        Toast.makeText(getContext(), "Error: Event not found. Invalid QR Code.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    });

    private void showFilterDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.dialog_filter_events, null);

        //Build the Dialog
        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .setCancelable(true)
                .create();

        //Setup UI
        ChipGroup chipGroup = dialogView.findViewById(R.id.filterChipGroup);
        Button btnApply = dialogView.findViewById(R.id.btnApplyFilter);
        Button btnClear = dialogView.findViewById(R.id.btnClearFilter);

        //Populate Chips
        for (Event.EventTag tag : Event.EventTag.values()) {
            if (tag == Event.EventTag.ALL) continue;

            Chip chip = new Chip(requireContext());
            chip.setText(tag.name());
            chip.setCheckable(true);
            chip.setClickable(true);

            chip.setChipBackgroundColorResource(com.google.android.material.R.color.mtrl_choice_chip_background_color);
            chip.setTextColor(getResources().getColorStateList(R.color.black, null)); //

            // Pre-select if it was already active
            if (activeFilters.contains(tag)) {
                chip.setChecked(true);
            }

            chipGroup.addView(chip);
        }

        // Handle apply
        btnApply.setOnClickListener(v -> {
            activeFilters.clear();
            // Loop through chips to see what is checked
            for (int i = 0; i < chipGroup.getChildCount(); i++) {
                Chip chip = (Chip) chipGroup.getChildAt(i);
                if (chip.isChecked()) {
                    // Convert text back to Enum
                    activeFilters.add(Event.EventTag.valueOf(chip.getText().toString()));
                }
            }

            applyFiltersToFragment();
            dialog.dismiss();
        });

        // Handle Clear
        btnClear.setOnClickListener(v -> {
            activeFilters.clear();
            applyFiltersToFragment();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void applyFiltersToFragment() {
        Toast.makeText(getContext(), "Filters Applied: " + activeFilters.size(), Toast.LENGTH_SHORT).show();
    }

}
