package com.example.lotteryeventapp;

import com.google.android.gms.common.data.DataHolder;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class AllOrganizersPagination extends FirestorePagination {
    public AllOrganizersPagination(int pageSize) {
        super();
        this.pageSize = pageSize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("organizers")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING);

        this.baseQuery = query;
        this.forwardQuery = query.limit(pageSize);
        this.backQuery = query.limit(pageSize);
    }

    @Override
    protected void createPage(QuerySnapshot snapshot, PaginationCallback cb) {
        ArrayList<Organizer> organizers = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            OrganizerDataHolder data = new OrganizerDataHolder(document.getData(), document.getId());
            organizers.add(data.createOrganizerInstance());
        }
        organizers.sort(Comparator.comparing(Organizer::getUid));

        cb.onGetPage(true, organizers);
    }
}
