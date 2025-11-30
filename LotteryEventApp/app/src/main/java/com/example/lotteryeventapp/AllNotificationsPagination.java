package com.example.lotteryeventapp;

import android.util.Log;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class AllNotificationsPagination extends FirestorePagination {
    public AllNotificationsPagination(int pageSize) {
        super();
        this.pageSize = pageSize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("notifications")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING);

        this.baseQuery = query;
        this.forwardQuery = query.limit(pageSize);
        this.backQuery = query.limit(pageSize);
    }

    @Override
    protected void createPage(QuerySnapshot snapshot, PaginationCallback cb) {
        ArrayList<Notification> notifs = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            NotificationDataHolder.NotificationType notifType = NotificationDataHolder.NotificationType.valueOf(document.getString("notificationType"));

            if (notifType == NotificationDataHolder.NotificationType.INVITATION) {
                InvitationDataHolder data = new InvitationDataHolder(document.getData(), document.getId());
                notifs.add(data.createInvitationInstance());
            }else if (notifType == NotificationDataHolder.NotificationType.REJECTION) {
                RejectionDataHolder data = new RejectionDataHolder(document.getData(), document.getId());
                notifs.add(data.createRejectionInstance());
            }else if (notifType == NotificationDataHolder.NotificationType.MESSAGING) {
                MessagingDataHolder data = new MessagingDataHolder(document.getData(), document.getId());
                notifs.add(data.createMessagingInstance());
            }else {
                Log.e("FirestorePagination", "Pagination Failed: Unknown notification type " + document.getId());
                cb.onError(new RuntimeException("Unknown notification type"));
                return;
            }
        }
        notifs.sort(Comparator.comparing(Notification::getUid));

        cb.onGetPage(true, notifs);
    }
}
