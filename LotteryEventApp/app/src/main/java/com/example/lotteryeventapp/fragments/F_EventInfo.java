package com.example.lotteryeventapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.Entrant;
import com.google.android.material.textfield.TextInputEditText;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.R;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class F_EventInfo extends Fragment {
    private int role;
    private Event event;
    private Entrant entrant;
    private DataModel model;

    //role = 0 for entrant, role = 1 for organizer

    public static F_EventInfo newInstance(int myRole){
        F_EventInfo fragment = new F_EventInfo();
        Bundle args = new Bundle();
        args.putInt("role", myRole);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // CRITICAL: Retrieve the 'role' argument here
        if (getArguments() != null) {
            this.role = getArguments().getInt("role");
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        return inflater.inflate(R.layout.event_info, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {

        Log.i("CURRENT EVENT INFO ROLE", "Current EVENT INFO user role is: " + role);

        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();
        // Set up info based on event
        if (event != null) {
            //Title
            TextView myText = view.findViewById(R.id.eventName);
            myText.setText(event.getTitle());
            //DateTime
            myText = view.findViewById(R.id.eventDateTime);
            myText.setText(event.getDate_time());
            //Location
            myText = view.findViewById(R.id.eventLocation);
            myText.setText(event.getLocation());
            //Tags
            //todo
            //Description
            myText = view.findViewById(R.id.eventDescription);
            myText.setText(event.getDetails());
            //Wait list size
            if (role == 0) {
                myText = view.findViewById(R.id.waitingListSize);
                String fraction = event.getWaitlistAmount() + "/" + event.getWaitlist_limit();
                myText.setText(fraction);
            }
        }

        // Set up page based on role
        if (role == 0) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.GONE);
            Entrant currentEntrant = model.getCurrentEntrant();
            if (currentEntrant.getWaitlistedEvents().contains(event.getUid())) {
                view.findViewById(R.id.joinButton).setVisibility(View.GONE);
            }
            // Detect button presses
            view.findViewById(R.id.joinButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joinWaitlist();
                }
            });

            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));
                }
            });
        }
        else if (role == 1) {
            view.findViewById(R.id.joinButton).setVisibility(View.GONE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.VISIBLE);

            view.findViewById(R.id.editEventBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(F_CreateEditEvent.newInstance(1));
                }});

            view.findViewById(R.id.applicantsBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(new F_Applicants());
                }});

            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
                }});
        } else if (role == 2) {
            view.findViewById(R.id.layoutEntrant).setVisibility(View.GONE);
            view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.VISIBLE);

            view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainActivity) requireActivity()).showFragment(F_BrowseEvents.newInstance(2));
                }});

        }
        view.findViewById(R.id.showQRCodeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = event.getUid();
                Bitmap qrBitmap = generateQRCodeBitmap(content);
                showDialogWithQR(qrBitmap);
            }
        });
    }
    /**
     * Handles the logic for an entrant joining the event's waitlist.
     */
    private void joinWaitlist() {
        if (event == null) {
            Toast.makeText(getContext(), "Error: Event not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        Entrant currentEntrant = model.getCurrentEntrant();
        if (currentEntrant == null) {
            Toast.makeText(getContext(), "Error: User profile not found.", Toast.LENGTH_SHORT).show();
            return;
        }

        String entrantId = currentEntrant.getUid();

        // Check if already on the waitlist or enrolled
        if (event.getWaitlist().contains(entrantId)) {
            Toast.makeText(getContext(), "You are already on the waitlist.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if waitlist is full (if limit > 0)
        if (event.getWaitlist_limit() > 0 && event.getWaitlistAmount() >= event.getWaitlist_limit()) {
            Toast.makeText(getContext(), "Waitlist is full.", Toast.LENGTH_SHORT).show();
            return;
        }

        //  Update Local Objects
        event.waitlistAdd(entrantId);
        currentEntrant.addWaitlistedEvent(event.getUid()); // Add to entrant's list

        //  Update Event in Firestore
        model.setEvent(event, new DataModel.SetCallback() {
            @Override
            public void onSuccess(String msg) {
                // Event update successful, now update the Entrant
                model.setEntrant(currentEntrant, new DataModel.SetCallback() {
                    @Override
                    public void onSuccess(String id) {
                        // Both updates successful
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Joined waiting list!", Toast.LENGTH_SHORT).show();
                            // Update the displayed fraction
                            TextView waitlistText = getView().findViewById(R.id.waitingListSize);
                            String fraction = event.getWaitlistAmount() + "/" + event.getWaitlist_limit();
                            waitlistText.setText(fraction);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        // Entrant update failed
                        Log.e("JoinWaitlist", "Failed to update entrant", e);
                        if (getContext() != null) {
                            Toast.makeText(getContext(), "Error updating profile. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                        // Revert local changes for consistency
                        event.waitlistRemove(entrantId);
                        currentEntrant.removeWaitlistedEvent(event.getUid());
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                // Event update failed
                event.waitlistRemove(entrantId);
                currentEntrant.removeWaitlistedEvent(event.getUid());
                Log.e("JoinWaitlist", "Failed to update event", e);
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Failed to join waitlist. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDialogWithQR(Bitmap qrBitmap) {
        if (getContext() == null || qrBitmap == null) return;

        // 1. Create an ImageView programmatically to hold the QR code
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(qrBitmap);
        imageView.setAdjustViewBounds(true);

        // Optional: Add some padding so the QR code isn't touching the edges
        imageView.setPadding(50, 50, 50, 50);

        // 2. Create the Dialog
        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Event QR Code")
                .setMessage("Here is the QR Code for the event.")
                .setView(imageView)
                .setPositiveButton("Close", (dialog, which) -> {
                    dialog.dismiss();
                    ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(role));
                })
                .setCancelable(false)
                .show();
    }

    private Bitmap generateQRCodeBitmap(String content) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
                }
            }
            return bmp;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
