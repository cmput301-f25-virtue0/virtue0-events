package com.example.lotteryeventapp;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

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
