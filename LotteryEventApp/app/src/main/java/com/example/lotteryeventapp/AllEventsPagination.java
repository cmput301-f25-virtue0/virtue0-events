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
        Query query = db.collection("events")
                .orderBy("title", Query.Direction.DESCENDING);

        this.baseQuery = query;
        this.forwardQuery = query.limit(pageSize);
        this.backQuery = query.limit(pageSize);
    }

    public AllEventsPagination(int pageSize, Query customQuery) {
        super(pageSize, customQuery);
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
