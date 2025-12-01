package com.example.lotteryeventapp;

import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
/**
 * retrieves a select amount of Images from the Database
 */
public class AllImagesPagination extends FirestorePagination {
    /**
     * constructs an AllImagesPagination with given pageSize
     * @param pageSize number of images
     */
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
    /**
     * creates a new page of Images
     * @param snapshot snapshot from collection
     * @param cb callback for retrieving pagination from the database
     */
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
