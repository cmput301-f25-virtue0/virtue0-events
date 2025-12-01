package com.example.lotteryeventapp.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Event;
import com.example.lotteryeventapp.ImageDataHolder;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Organizer;
import com.example.lotteryeventapp.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.util.Objects;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class F_CreateEditEvent extends Fragment {
    private int type;
    private DataModel model;
    private Event event;
    private ImageView posterUpload;
    private ImageDataHolder imageHolder;


    private final Calendar eventDateCalendar = Calendar.getInstance();

    private final Calendar regStartCalendar = Calendar.getInstance();

    private final Calendar regDeadlineCalendar = Calendar.getInstance();

    private final String DATE_FORMAT = "EEE, MMM d, yyyy 'at' h:mm a";

    private ArrayList<Event.EventTag> selectedTags = new ArrayList<>();

    private TextView tvSelectedTags;
    private Button btnSelectTags;
    private boolean[] selectedBooleanArray;

    public static F_CreateEditEvent newInstance(int myType) {
        F_CreateEditEvent fragment = new F_CreateEditEvent();
        Bundle args = new Bundle();
        args.putInt("type", myType);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment & get the count text view
        if (getArguments() != null) {
            this.type = getArguments().getInt("type");
        }
        return inflater.inflate(R.layout.edit_and_create_event, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        MaterialToolbar toolbar = view.findViewById(R.id.toolbarCreateEvent);
        model = ((MainActivity) requireActivity()).getDataModel();
        event = model.getCurrentEvent();

        TextInputEditText etRegOpens = view.findViewById(R.id.etRegOpens);
        TextInputEditText etWhen = view.findViewById(R.id.etWhen);
        TextInputEditText etRegCloses = view.findViewById(R.id.etRegCloses);
        this.posterUpload = view.findViewById(R.id.ivPoster);


        setupDateTimePicker(etWhen, eventDateCalendar);
        setupDateTimePicker(etRegOpens, regStartCalendar);
        setupDateTimePicker(etRegCloses, regDeadlineCalendar);

        tvSelectedTags = view.findViewById(R.id.tv_selected_tags);
        btnSelectTags = view.findViewById(R.id.btn_select_tags);

        Event.EventTag[] allTags = Event.EventTag.values();
        ArrayList<String> tagNameList = new ArrayList<>();
        ArrayList<Event.EventTag> tagValueList = new ArrayList<>();

        for (Event.EventTag tag : allTags) {
            if (tag != Event.EventTag.ALL) {
                tagNameList.add(tag.name());
                tagValueList.add(tag);
            }
        }
        String[] tagNames = tagNameList.toArray(new String[0]);
        selectedBooleanArray = new boolean[tagNames.length];

        btnSelectTags.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Select Tags")
                    .setMultiChoiceItems(tagNames, selectedBooleanArray, (dialog, which, isChecked) -> {
                        selectedBooleanArray[which] = isChecked;
                        Event.EventTag tag = tagValueList.get(which);
                        if (isChecked) {
                            if (!selectedTags.contains(tag)) selectedTags.add(tag);
                            selectedTags.remove(Event.EventTag.ALL); // Remove Default if specific tag chosen
                        } else {
                            selectedTags.remove(tag);
                        }
                    })
                    .setPositiveButton("OK", (dialog, which) -> updateSelectedTagsText())
                    .setNegativeButton("Cancel", null)
                    .show();
        });


        //If editing, fill out information with existing event info
        if (this.type == 1 && this.event != null) {
            toolbar.setTitle("Edit Event"); // Update toolbar title

            TextInputEditText myText;

            myText = view.findViewById(R.id.etName);
            myText.setText(Objects.requireNonNullElse(event.getTitle(), ""));

            myText = view.findViewById(R.id.etDesc);
            myText.setText(Objects.requireNonNullElse(event.getDetails(), ""));

            myText = view.findViewById(R.id.etLocation);
            myText.setText(Objects.requireNonNullElse(event.getLocation(), ""));

            if (event.getDate_time() != null) {
                etWhen.setText(event.getDate_time());
                parseDateStringIntoCalendar(event.getDate_time(), eventDateCalendar);
            }

            if (event.getRegistration_start() != null) {
                etRegOpens.setText(event.getRegistration_start());
                parseDateStringIntoCalendar(event.getRegistration_start(), regStartCalendar);
            }

            if (event.getRegistration_deadline() != null) {
                etRegCloses.setText(event.getRegistration_deadline());
                parseDateStringIntoCalendar(event.getRegistration_deadline(), regDeadlineCalendar);
            }

            myText = view.findViewById(R.id.etCapacity);
            myText.setText(String.valueOf(event.getAttendee_limit()));

            myText = view.findViewById(R.id.etWaitlistLimit);
            myText.setText(String.valueOf(event.getWaitlist_limit()));

            MaterialSwitch mySwitch = view.findViewById(R.id.switchGeo);
            mySwitch.setChecked(event.willTrack_geolocation()); // Use getter
            Button finalize = view.findViewById(R.id.btnFinalize);
            finalize.setText("Update Event");

            selectedTags = new ArrayList<>(event.getTags());
            // Sync the booleans for the dialog
            for (int i = 0; i < tagValueList.size(); i++) {
                if (selectedTags.contains(tagValueList.get(i))) {
                    selectedBooleanArray[i] = true;
                }
            }
            updateSelectedTagsText();

        } else {
            // This is "create" mode
            toolbar.setTitle("Create Event");
        }

        toolbar.setNavigationOnClickListener(v -> {
            if (type == 1) {
                ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));
            }
            else {
                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
            }
        });

        view.findViewById(R.id.btnFinalize).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Get info from fields
                    TextInputEditText etTitle = view.findViewById(R.id.etName);
                    TextInputEditText etDetails = view.findViewById(R.id.etDesc);
                    TextInputEditText etLocation = view.findViewById(R.id.etLocation);
                    TextInputEditText etWhen = view.findViewById(R.id.etWhen);
                    TextInputEditText etRegCloses = view.findViewById(R.id.etRegCloses);
                    TextInputEditText etCapacity = view.findViewById(R.id.etCapacity);
                    TextInputEditText etWaitlist = view.findViewById(R.id.etWaitlistLimit);
                    MaterialSwitch swGeo = view.findViewById(R.id.switchGeo);

                    // Validate fields before parsing
                    String title = etTitle.getText().toString();
                    String details = etDetails.getText().toString();
                    String location = etLocation.getText().toString();
                    String dateTime = etWhen.getText().toString();
                    String regStart = etRegOpens.getText().toString();
                    String regDeadline = etRegCloses.getText().toString();
                    String capacityStr = etCapacity.getText().toString();
                    String waitlistStr = etWaitlist.getText().toString();

                    if (regStartCalendar.after(regDeadlineCalendar)) {
                        Toast.makeText(getContext(), "Registration must start before it closes!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (title.isEmpty() || details.isEmpty() || location.isEmpty() || dateTime.isEmpty() ||
                            regDeadline.isEmpty() || capacityStr.isEmpty() || waitlistStr.isEmpty()) {
                        throw new Exception("Empty fields");
                    }

                    int attendee_limit = Integer.parseInt(capacityStr);
                    int waitlist_limit = Integer.parseInt(waitlistStr);
                    boolean track_geo = swGeo.isChecked();
                    String organizer = model.getCurrentOrganizer().getUid();

                    if (type == 0) {

                        // create new event
                        Event makeEvent = new Event(title, dateTime, location,regStart, regDeadline,
                                details, track_geo, true, waitlist_limit, attendee_limit, organizer, selectedTags);
                        if(posterUpload.getDrawable()==null){
                            model.setEvent(makeEvent, new DataModel.SetCallback() {
                                @Override
                                public void onSuccess(String msg) {
                                    Log.d("Firebase", "written");
                                    Organizer organizer = model.getCurrentOrganizer();
                                    organizer.addEvent(makeEvent.getUid());
                                    model.setOrganizer(organizer, new DataModel.SetCallback() {
                                        @Override
                                        public void onSuccess(String msg) {
                                            Log.d("Firebase", "written");

                                        }
                                        @Override
                                        public void onError(Exception e) {
                                            Log.e("Firebase", "fail");
                                        }
                                    });

                                }
                                @Override
                                public void onError(Exception e) {
                                    Log.e("Firebase", "fail");
//                                    throw new RuntimeException();
                                }
                            });
                        }else {
                            ImageDataHolder image = new ImageDataHolder(posterUpload);

                            model.setImage(image, new DataModel.SetCallback() {
                                @Override
                                public void onSuccess(String msg) {
                                    Log.d("Firebase", "written");
                                    makeEvent.setImage(msg);
                                    model.setEvent(makeEvent, new DataModel.SetCallback() {
                                        @Override
                                        public void onSuccess(String msg) {
                                            Log.d("Firebase", "written");
                                            Organizer organizer = model.getCurrentOrganizer();
                                            organizer.addEvent(makeEvent.getUid());
                                            model.setOrganizer(organizer, new DataModel.SetCallback() {
                                                @Override
                                                public void onSuccess(String msg) {
                                                    Log.d("Firebase", "written");

                                                }

                                                @Override
                                                public void onError(Exception e) {
                                                    Log.e("Firebase", "fail");
                                                }
                                            });

                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.e("Firebase", "fail");
                                        }
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Firebase", "fail");
                                }
                            });
                        }
                        Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                        model.setCurrentEvent(makeEvent);
                        //View newly created event
                        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));
                    } else {
                        // update existing event
                        event.setTitle(title);
                        event.setDetails(details);
                        event.setLocation(location);
                        event.setDate_time(dateTime);
                        event.setRegistration_start(regStart);
                        event.setRegistration_deadline(regDeadline);
                        event.setAttendee_limit(attendee_limit);
                        event.setWaitlist_limit(waitlist_limit);
                        event.setTrack_geolocation(track_geo); // Use setter
                        event.setOrganizer(organizer);
                        event.setTags(selectedTags);

                        if(posterUpload.getDrawable()!=null) {
                            ImageDataHolder image = new ImageDataHolder(posterUpload);

                            model.setImage(image, new DataModel.SetCallback() {
                                @Override
                                public void onSuccess(String msg) {
                                    Log.d("Firebase", "written");
                                    event.setImage(msg);
                                    model.setEvent(event, new DataModel.SetCallback() {
                                        @Override
                                        public void onSuccess(String msg) {
                                            Log.d("Firebase", "written");
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.e("Firebase", "fail");
                                        }
                                    });
                                }

                                @Override
                                public void onError(Exception e) {
                                    Log.e("Firebase", "fail");
                                }
                            });
                        }


                            Organizer currentOrganizer = model.getCurrentOrganizer();
                        if (!currentOrganizer.getEvents().contains(event.getUid())) {

                            // Add it locally
                            currentOrganizer.addEvent(event.getUid());

                            // Save the updated organizer to Firebase
                            model.setOrganizer(currentOrganizer, new DataModel.SetCallback() {
                                @Override
                                public void onSuccess(String msg) {
                                    Log.d("Firebase", "Linked missing event to Organizer");
                                }
                                @Override
                                public void onError(Exception e) {
                                    Log.e("Firebase", "Failed to update Organizer link");
                                }
                            });
                        }

                        //Update the existing event
                        Toast.makeText(getContext(), "Event Updated", Toast.LENGTH_SHORT).show();
                        model.setEvent(event, new DataModel.SetCallback() {
                            @Override
                            public void onSuccess(String msg) {
                                Log.d("Firebase", "written");

                            }
                            @Override
                            public void onError(Exception e) {
                                Log.e("Firebase", "fail");
                            }
                        });
                    }

                    if (type == 1) {
                        ((MainActivity) requireActivity()).showFragment(F_EventInfo.newInstance(1));
                    }
                    else {
                        ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(1));
                    }

                } catch(Exception e) {
                    throw new RuntimeException(e);
//                    Toast.makeText(getContext(), "Please fill all missing fields!", Toast.LENGTH_SHORT).show();
                }


            }
        });




        ActivityResultLauncher<Intent> uploadImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Intent data = result.getData();
                            Uri uploadedImage = data.getData();
                            posterUpload.setImageURI(uploadedImage);
                        }
                    }
                });


        view.findViewById(R.id.emptyPosterState).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                uploadImageResultLauncher.launch(intent);
            }
        });
    }


    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed



    private void setupDateTimePicker(TextInputEditText editText, Calendar calendar) {
        editText.setOnClickListener(v -> {
            // Show Date Picker
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Show Time Picker immediately after Date Picker
                        TimePickerDialog timePickerDialog = new TimePickerDialog(
                                requireContext(),
                                (timeView, hourOfDay, minute) -> {
                                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                    calendar.set(Calendar.MINUTE, minute);

                                    // Update the EditText
                                    updateLabel(editText, calendar);
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false // Use 12 hour format (AM/PM)
                        );
                        timePickerDialog.show();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });
    }

    /**
     * Updates the EditText with the formatted date string
     */
    private void updateLabel(TextInputEditText editText, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        editText.setText(sdf.format(calendar.getTime()));
    }

    /**
     * Tries to parse existing strings back into the calendar to prevent overwriting
     * with "Today" when editing.
     */
    private void parseDateStringIntoCalendar(String dateString, Calendar calendar) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
            calendar.setTime(Objects.requireNonNull(sdf.parse(dateString)));
        } catch (Exception e) {
            // If parsing fails (e.g. old data format), just leave calendar as 'now'
            Log.w("DateParse", "Could not parse date: " + dateString);
        }
    }

    private void updateSelectedTagsText() {
        if (selectedTags.isEmpty() || (selectedTags.size() == 1 && selectedTags.contains(Event.EventTag.ALL))) {
            tvSelectedTags.setText("Tags: Default (ALL)");
        } else {
            StringBuilder sb = new StringBuilder("Tags: ");
            for (Event.EventTag tag : selectedTags) {
                if (tag != Event.EventTag.ALL) {
                    sb.append(tag.name()).append(", ");
                }
            }
            if (sb.length() > 6) sb.setLength(sb.length() - 2);
            tvSelectedTags.setText(sb.toString());
        }
    }
}

