package com.example.lotteryeventapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lotteryeventapp.DataModel;
import com.example.lotteryeventapp.Entrant;
import com.example.lotteryeventapp.Invitation;
import com.example.lotteryeventapp.MainActivity;
import com.example.lotteryeventapp.Messaging;
import com.example.lotteryeventapp.Notification;
import com.example.lotteryeventapp.NotificationAdapter;
import com.example.lotteryeventapp.R;
import com.example.lotteryeventapp.Rejection;
import com.google.android.material.appbar.MaterialToolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class F_Notification extends Fragment implements NotificationAdapter.OnNotificationClickListener {

    private static final String ARG_ROLE = "role";
    private int role;
    private DataModel model;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    public static F_Notification newInstance(int myRole){
        F_Notification fragment = new F_Notification();
        Bundle args = new Bundle();
        args.putInt(ARG_ROLE, myRole);
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
        return inflater.inflate(R.layout.notifications_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof MainActivity) {
            model = ((MainActivity) getActivity()).getDataModel();
        } else {
            Log.e("F_Notification", "Activity is not MainActivity");
            return;
        }
        NotificationAdapter.OnNotificationClickListener listener = this;
        recyclerView = view.findViewById(R.id.rvNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Entrant entrant = model.getCurrentEntrant();
        notificationList = new ArrayList<>();




        if (getArguments().getInt(ARG_ROLE)==0) {
            model.getEntrant(entrant.getUid(),new DataModel.GetCallback() {
                @Override
                public void onSuccess(Object obj) {
                    Entrant updatedEntrant = (Entrant) obj;
                    model.setCurrentEntrant(updatedEntrant);
                    model.getUsableNotifications(model.getCurrentEntrant(), new DataModel.GetCallback() {
                        @Override
                        public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                        }

                        @Override
                        public void onSuccess(Object obj) {
                            Log.d("Firebase", "retrieved");
                            ArrayList<Notification> notificationList = (ArrayList<Notification>) obj;
                            adapter = new NotificationAdapter(notificationList, listener);

                            recyclerView.setAdapter(adapter);

                            MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
                            toolbar.setNavigationOnClickListener(v -> {
                                ((MainActivity) requireActivity()).showFragment(F_HomePage.newInstance(0));

                            });
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e("Firebase", "fail");
                        }
                    });
                }

                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }

                @Override
                public void onError(Exception e) {
                    Log.e("Entrant", "Error fetching Entrant", e);
                }
            });

        }else if(getArguments().getInt(ARG_ROLE)==2){
            model.getAllNotifications(new DataModel.GetCallback() {
                @Override
                public <T extends Enum<T>> void onSuccess(Object obj, T type) {

                }

                @Override
                public void onSuccess(Object obj) {
                    Log.d("Firebase", "retrieved");
                    ArrayList<Notification> notificationList = (ArrayList<Notification>) obj;
                    adapter = new NotificationAdapter(notificationList, listener);

                    recyclerView.setAdapter(adapter);

                    MaterialToolbar toolbar = view.findViewById(R.id.toolbar);
                    toolbar.setNavigationOnClickListener(v -> {
                        ((MainActivity) requireActivity()).showFragment(F_AdminHomePage.newInstance(2));
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.e("Firebase", "fail");
                }
            });
        }


//        fetchNotifications();
    }

    private void fetchNotifications() {
        if (model == null) return;

        Entrant currentEntrant = model.getCurrentEntrant();

        if (currentEntrant != null) {
            ArrayList<String> notificationIds = currentEntrant.getNotifications();

            notificationList.clear();
            adapter.notifyDataSetChanged();

            if (notificationIds == null || notificationIds.isEmpty()) {
                return;
            }

            for (String notifId : notificationIds) {
                model.getNotification(notifId, new DataModel.GetCallback() {
                    @Override
                    public void onSuccess(Object obj) {
                    }

                    @Override
                    public <T extends Enum<T>> void onSuccess(Object obj, T type) {
                        if (obj instanceof Notification) {
                            Notification notif = (Notification) obj;
                            notificationList.add(notif);
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Notification", "Error fetching notification with ID: " + notifId, e);
                    }
                });
            }
        } else {
            Log.e("F_Notification", "Current Entrant is null.");
        }
    }

    @Override
    public void onNotificationClick(Notification notification, int position) {
        if (getArguments().getInt(ARG_ROLE) == 0) {
            if (!notification.isRead()) {
                notification.markAsRead();
                model.setNotification(notification, new DataModel.SetCallback() {
                    @Override
                    public void onSuccess(String id) {
                        adapter.notifyItemChanged(position);
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e("Notification", "Error marking notification as read", e);
                    }
                });
            }
            if (notification.getClass() == Invitation.class) {
                model.setCurrentNotification(notification);
                ((MainActivity) requireActivity()).showFragment(F_Acceptance.newInstance(2));

            } else if (notification.getClass() == Rejection.class) {

            } else if (notification.getClass() == Messaging.class) {
                Toast.makeText(getContext(), notification.getMessage(), Toast.LENGTH_SHORT).show();

            }
//        ((MainActivity) requireActivity()).showFragment(F_Applicants.newInstance(2));
        }
    }
}