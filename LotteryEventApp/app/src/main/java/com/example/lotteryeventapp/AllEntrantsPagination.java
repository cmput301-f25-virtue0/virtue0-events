package com.example.lotteryeventapp;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class AllEntrantsPagination extends FirestorePagination {
    public AllEntrantsPagination(int pageSize) {
        super();
        this.pageSize = pageSize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("entrants")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING);

        this.baseQuery = query;
        this.forwardQuery = query.limit(pageSize);
        this.backQuery = query.limit(pageSize);
    }

    @Override
    protected void createPage(QuerySnapshot snapshot, PaginationCallback cb) {
        ArrayList<Entrant> entrants = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            EntrantDataHolder data = new EntrantDataHolder(document.getData(), document.getId());
            entrants.add(data.createEntrantInstance());
        }
        entrants.sort(Comparator.comparing(Entrant::getUid));

        cb.onGetPage(true, entrants);
    }
}
