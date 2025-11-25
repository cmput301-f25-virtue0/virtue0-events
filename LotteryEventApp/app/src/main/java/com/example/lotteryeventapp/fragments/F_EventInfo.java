package com.example.lotteryeventapp.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Organizer;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class F_EventInfo extends Fragment {
    private int role;
    private Event event;
    private Entrant entrant;
    private DataModel model;

    private static final String KEY_EVENT_ID = "current_event_id";
    private String recoveredEventId;

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the event ID so we can fetch it again after rotation
        if (event != null) {
            outState.putString(KEY_EVENT_ID, event.getUid());
        } else if (recoveredEventId != null) {
            // Keep the ID if we haven't finished fetching the object yet
            outState.putString(KEY_EVENT_ID, recoveredEventId);
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

        if (model.getCurrentEvent() != null) {
            // Model has data
            recoveredEventId = model.getCurrentEvent().getUid();
        } else if (savedInstanceState != null) {
            // Crash/Rotation case: Retrieve ID from saved state
            recoveredEventId = savedInstanceState.getString(KEY_EVENT_ID);
        }

        // Ensure data exists before touching UI
        if (recoveredEventId != null) {
            ensureDataAndSetupUI(view, recoveredEventId);
        } else {
            // If we have absolutely no way to know which event to show
            Toast.makeText(getContext(), "Error: Event information lost.", Toast.LENGTH_SHORT).show();
        }

    }

    //Fetch requests order: Fetch Event -> Fetch User -> Setup UI
    private void ensureDataAndSetupUI(View view, String eventId) {
        // Ensure event is Loaded
        if (model.getCurrentEvent() == null) {
            model.getEvent(eventId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (!isAdded()) return;
                    Event fetchedEvent = (Event) obj;
                    model.setCurrentEvent(fetchedEvent);
                    event = fetchedEvent;

                    ensureUserAndSetupUI(view);
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {}

                @Override
                public void onError(Exception e) {
                    Log.e("EventInfo", "Failed to re-fetch event", e);
                }
            });
        } else {
            event = model.getCurrentEvent();
            ensureUserAndSetupUI(view);
        }
    }

    private void ensureUserAndSetupUI(View view) {
        String deviceId = Settings.Secure.getString(requireContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        // User is loaded based on role
        if (role == 0 && model.getCurrentEntrant() == null) {
            model.getEntrant(deviceId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (!isAdded()) return;
                    model.setCurrentEntrant((Entrant) obj);
                    setupUI(view); // Setup UI
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {}
                @Override
                public void onError(Exception e) { Log.e("EventInfo", "Failed to fetch Entrant", e); }
            });

        } else if (role == 1 && model.getCurrentOrganizer() == null) {
            model.getOrganizer(deviceId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    if (!isAdded()) return;
                    model.setCurrentOrganizer((Organizer) obj);
                    setupUI(view); //Setup UI
                }
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {}
                @Override
                public void onError(Exception e) { Log.e("EventInfo", "Failed to fetch Organizer", e); }
            });
        } else {
            // Data is already there
            setupUI(view);
        }
    }

    private void setupUI(View view) {
        if (event == null) return;

        // Title, Date, Location, Desc
        TextView myText = view.findViewById(R.id.eventName);
        myText.setText(event.getTitle());

        myText = view.findViewById(R.id.eventDateTime);
        myText.setText(event.getDate_time());

        myText = view.findViewById(R.id.eventLocation);
        myText.setText(event.getLocation());

        myText = view.findViewById(R.id.eventDescription);
        myText.setText(event.getDetails());

        // Wait list size
        myText = view.findViewById(R.id.waitingListSize);
        String fraction = event.getWaitlistAmount() + "/" + event.getWaitlist_limit();
        myText.setText(fraction);


        // Role specific ui
        if (role == 0) {
            // ENTRANT LOGIC
            Entrant currentEntrant = model.getCurrentEntrant();

            // Safety check in case Entrant fetch failed
            if (currentEntrant != null) {
                boolean isWaitlisted = event.getWaitlist() != null && event.getWaitlist().contains(currentEntrant.getUid());
                boolean isAttending = event.getAttendee_list() != null && event.getAttendee_list().contains(currentEntrant.getUid());
                boolean isInvited = event.getInvited_list() != null && event.getInvited_list().contains(currentEntrant.getUid());

                if (!isAttending || !isInvited) {
                    view.findViewById(R.id.layoutEntrant).setVisibility(View.VISIBLE);
                }
                view.findViewById(R.id.layoutOrganizer).setVisibility(View.GONE);
                view.findViewById(R.id.layoutAdmin).setVisibility(View.GONE);

                if (isWaitlisted || isAttending || isInvited) {
                    view.findViewById(R.id.joinButton).setVisibility(View.GONE);
                }

                if (isInvited) {
                    view.findViewById(R.id.layoutInvited).setVisibility(View.VISIBLE);
                }

                if (isWaitlisted) {
                    view.findViewById(R.id.leave_button).setVisibility(View.VISIBLE);
                }

                // Click Listeners
                view.findViewById(R.id.joinButton).setOnClickListener(v -> joinWaitlist());
                view.findViewById(R.id.backButton).setOnClickListener(v ->
                        ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0))
                );
            }

        } else if (role == 1) {
            // ORGANIZER LOGIC
            Organizer currentOrg = model.getCurrentOrganizer();

            if (currentOrg != null) {
                view.findViewById(R.id.joinButton).setVisibility(View.GONE);

                // Check if this organizer owns the event
                if (currentOrg.getUid().equals(event.getOrganizer())) {
                    view.findViewById(R.id.layoutOrganizer).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.layoutAdmin).setVisibility(View.VISIBLE);
                }

                view.findViewById(R.id.editEventBtn).setOnClickListener(v ->
                        ((MainActivity) requireActivity()).showFragment(F_CreateEditEvent.newInstance(1))
                );
                view.findViewById(R.id.applicantsBtn).setOnClickListener(v ->
                        ((MainActivity) requireActivity()).showFragment(new F_Applicants())
                );
                view.findViewById(R.id.backButton).setOnClickListener(v ->
                        ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1))
                );
            }

            view.findViewById(R.id.btnDeleteEvent).setOnClickListener(v-> {
                if (getContext() == null) return;

               Event delEvent = event;

                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete '" + delEvent.getTitle() + "'? This will remove it from all users.")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            performCascadeDelete(delEvent);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

        } else if (role == 2) {
            // ADMIN LOGIC
            view.findViewById(R.id.joinButton).setVisibility(View.GONE);
            view.findViewById(R.id.layoutAdmin).setVisibility(View.VISIBLE);
            view.findViewById(R.id.backButton).setOnClickListener(v ->
                    ((MainActivity) requireActivity()).showFragment(F_BrowseEvents.newInstance(2))
            );
        }

        // QR Code Button
        view.findViewById(R.id.showQRCodeBtn).setOnClickListener(v -> {
            String content = event.getUid();
            Bitmap qrBitmap = generateQRCodeBitmap(content);
            showDialogWithQR(qrBitmap);
        });

        view.findViewById(R.id.btnDeleteEvent).setOnClickListener(v-> {
            if (getContext() == null) return;

            Event delEvent = event;

            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete '" + delEvent.getTitle() + "'? This will remove it from all users.")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        performCascadeDelete(delEvent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
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

        // Create an ImageView to display the QR code
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(qrBitmap);
        imageView.setAdjustViewBounds(true);

        imageView.setPadding(50, 50, 50, 50);

        // Create the Dialog
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

    private void performCascadeDelete(Event event) {
        Log.d("CascadeDelete", "Starting deletion for: " + event.getTitle());

        //  Remove from Organizer
        removeEventFromOrganizer(event, () -> {
            // Remove from all Entrants
            removeEventFromEntrants(event, () -> {
                //Finally delete the Event itself
                deleteEventDocument(event);
            });
        });
    }

    private void removeEventFromOrganizer(Event event, Runnable onComplete) {
        String orgId = event.getOrganizer();
        if (orgId == null || orgId.isEmpty()) {
            onComplete.run();
            return;
        }

        model.getOrganizer(orgId, new DataModel.GetCallback() {
            @Override
            public void onSuccess(Object obj) {
                Organizer org = (Organizer) obj;
                if (org != null && org.getEvents().contains(event.getUid())) {
                    org.getEvents().remove(event.getUid());
                    model.setOrganizer(org, new DataModel.SetCallback() {
                        @Override
                        public void onSuccess(String id) {
                            Log.d("CascadeDelete", "Removed event from Organizer.");
                            onComplete.run();
                        }
                        @Override
                        public void onError(Exception e) {
                            Log.e("CascadeDelete", "Failed to update Organizer. Continuing anyway.", e);
                            onComplete.run(); // Continue even if this fails to ensure event is deleted
                        }
                    });
                } else {
                    onComplete.run();
                }
            }

            @Override
            public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

            @Override
            public void onError(Exception e) {
                Log.e("CascadeDelete", "Failed to fetch Organizer. Continuing.", e);
                onComplete.run();
            }
        });
    }

    private void removeEventFromEntrants(Event event, Runnable onComplete) {
        // Collect all unique Entrant IDs involved in this event
        Set<String> affectedUserIds = new HashSet<>();
        if (event.getWaitlist() != null) affectedUserIds.addAll(event.getWaitlist());
        if (event.getAttendee_list() != null) affectedUserIds.addAll(event.getAttendee_list());
        if (event.getInvited_list() != null) affectedUserIds.addAll(event.getInvited_list());
        if (event.getCancelled_list() != null) affectedUserIds.addAll(event.getCancelled_list());

        if (affectedUserIds.isEmpty()) {
            onComplete.run();
            return;
        }

        Log.d("CascadeDelete", "Cleaning up " + affectedUserIds.size() + " entrants.");

        // Use a counter to track when all async operations are done
        AtomicInteger counter = new AtomicInteger(affectedUserIds.size());

        for (String userId : affectedUserIds) {
            model.getEntrant(userId, new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant entrant = (Entrant) obj;
                    if (entrant != null) {
                        // Remove from all potential lists in the Entrant object
                        entrant.removeWaitlistedEvent(event.getUid());
                        entrant.removeInvitedEvent(event.getUid());
                        entrant.removeAttendedEvent(event.getUid());

                        // Save updated entrant
                        model.setEntrant(entrant, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String id) {
                                checkCompletion();
                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("CascadeDelete", "Failed to update Entrant " + userId, e);
                                checkCompletion();
                            }
                        });
                    } else {
                        checkCompletion();
                    }
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) { }

                @Override
                public void onError(Exception e) {
                    Log.e("CascadeDelete", "Failed to fetch Entrant " + userId, e);
                    checkCompletion();
                }

                private void checkCompletion() {
                    // Decrement counter. If 0, we are done with everyone.
                    if (counter.decrementAndGet() == 0) {
                        Log.d("CascadeDelete", "All entrants updated.");

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(onComplete);
                        }
                    }
                }
            });
        }
    }

    private void deleteEventDocument(Event event) {
        model.deleteEvent(event, new DataModel.DeleteCallback() {
            @Override
            public void onSuccess() {
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Event and references deleted", Toast.LENGTH_SHORT).show();

                    if (role == 1) { //organizer role
                        ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(role));
                    } else { //admin role
                        ((MainActivity) requireActivity()).showFragment(F_BrowseEvents.newInstance(role));
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("DeleteEvent", "Error deleting event doc", e);
                if (isAdded() && getContext() != null) {
                    Toast.makeText(getContext(), "Failed to delete event", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
