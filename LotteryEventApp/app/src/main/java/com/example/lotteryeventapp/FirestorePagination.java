package com.example.lotteryeventapp;

import android.util.Log;

import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class FirestorePagination {
    protected Query forwardQuery;
    protected Query backQuery;
    protected int pageSize;
    protected int pageNumber = 0;
    protected int lastPageNumber = -1;
    protected DocumentSnapshot currentPageFirstSnapshot;
    protected DocumentSnapshot currentPageLastSnapshot;

    protected FirestorePagination() {

    }

    public interface PaginationCallback {
        <T> void onGetPage(boolean hasResults, ArrayList<T> obs);
        void onError(Exception e);
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void getNextPage(PaginationCallback cb) {
        if (this.lastPageNumber == this.pageNumber) {
            Log.d("FirestorePagination", "Pagination Success: Already on last page");
            cb.onGetPage(false, null);
        }

        this.forwardQuery
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();
                        if (snapshot.isEmpty()) {
                            // Query returned no results
                            Log.d("FirestorePagination", "Pagination Success: No more pages");
                            if (this.lastPageNumber == -1) {
                                this.lastPageNumber = ++this.pageNumber;
                            }
                            cb.onGetPage(false, null);
                        }else {
                            // Query returned results
                            List<DocumentSnapshot> docList = snapshot.getDocuments();
                            this.currentPageFirstSnapshot = docList.get(0);
                            this.currentPageLastSnapshot = docList.get(snapshot.size() - 1);
                            this.pageNumber++;

                            Log.d("FirestorePagination", "Pagination Success: Fetched page " + this.pageNumber);
                            this.createPage(snapshot, cb);
                        }
                    } else {
                        Log.e("FirestorePagination", "Pagination Failed: ", task.getException());
                        cb.onError(task.getException());
                    }
                });
    }

    public void getPreviousPage(PaginationCallback cb) {
        if (this.pageNumber == 0) {
            Log.d("FirestorePagination", "Pagination Success: No pages have been fetched");
            cb.onGetPage(false, null);
        } else if (this.pageNumber == 1) {
            Log.d("FirestorePagination", "Pagination Success: Already on first page");
            cb.onGetPage(false, null);
        }

        this.backQuery
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot snapshot = task.getResult();

                        List<DocumentSnapshot> docList = snapshot.getDocuments();
                        this.currentPageFirstSnapshot = docList.get(0);
                        this.currentPageLastSnapshot = docList.get(snapshot.size() - 1);
                        this.pageNumber--;

                        Log.d("FirestorePagination", "Pagination Success: Fetched page " + this.pageNumber);
                        this.createPage(snapshot, cb);
                    } else {
                        Log.d("FirestorePagination", "Pagination Failed: ", task.getException());
                        cb.onError(task.getException());
                    }
                });
    }

    abstract void createPage(QuerySnapshot snapshot, PaginationCallback cb);
}
