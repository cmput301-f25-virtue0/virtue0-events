package com.example.lotteryeventapp;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;

public class AllImagesPagination extends FirestorePagination {
    public AllImagesPagination(int pageSize) {
        super();
        this.pageSize = pageSize;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Query query = db.collection("images")
                .orderBy(FieldPath.documentId(), Query.Direction.DESCENDING);

        this.baseQuery = query;
        this.forwardQuery = query.limit(pageSize);
        this.backQuery = query.limit(pageSize);
    }

    @Override
    protected void createPage(QuerySnapshot snapshot, PaginationCallback cb) {
        ArrayList<ImageDataHolder> images = new ArrayList<>();
        for (QueryDocumentSnapshot document : snapshot) {
            ImageDataHolder data = new ImageDataHolder(document.getData(), document.getId());
            images.add(data);
        }
        images.sort(Comparator.comparing(ImageDataHolder::getUid));

        cb.onGetPage(true, images);
    }
}
