package com.example.lotteryeventapp;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AllEventsPagination extends FirestorePagination {

    public AllEventsPagination(int pageSize) {
        super();
        this.pageSize = pageSize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.forwardQuery = db.collection("events")
                .orderBy("title", Query.Direction.DESCENDING)
                .startAfter(this.currentPageLastSnapshot)
                .limit(this.pageSize);
        this.backQuery = db.collection("events")
                .orderBy("title", Query.Direction.DESCENDING)
                .endBefore(this.currentPageFirstSnapshot)
                .limit(this.pageSize);
    }

    @Override
    protected void createPage(QuerySnapshot snapshot, PaginationCallback cb) {
        ArrayList<Event> events = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            EventDataHolder data = new EventDataHolder(document.getData(), document.getId());
            events.add(data.createEventInstance());
        }
        events.sort(Comparator.comparing(Event::getTitle));

        cb.onGetPage(true, events);
    }
}
