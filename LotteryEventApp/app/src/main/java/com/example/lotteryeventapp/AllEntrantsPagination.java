package com.example.lotteryeventapp;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * retrieves a select amount of Entrants from the Database
 */
public class AllEntrantsPagination extends FirestorePagination {
    /**
     * constructs an AllEntrantsPagination with given pageSize
     * @param pageSize number of entrants
     */
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

    /**
     * creates a new page of Entrants
     * @param snapshot snapshot from collection
     * @param cb callback for retrieving pagination from the database
     */
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
