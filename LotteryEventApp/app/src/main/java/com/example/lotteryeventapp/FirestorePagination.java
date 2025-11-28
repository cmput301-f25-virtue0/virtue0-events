package com.example.lotteryeventapp;

import android.util.Log;

import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public abstract class FirestorePagination {
    protected Query baseQuery;
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
        Query query;
        if (this.pageNumber == 0) {
            query = this.baseQuery.limit(this.pageSize);

            AggregateQuery queryCount = this.baseQuery.count();
            CountDownLatch latch = new CountDownLatch(1);
            queryCount
                    .get(AggregateSource.SERVER)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            AggregateQuerySnapshot snapshot = task.getResult();
                            int count = (int) snapshot.getCount();
                            int pageCount = count / this.pageSize;
                            if (count % this.pageSize == 0) {
                                this.lastPageNumber = pageCount;
                            }else {
                                this.lastPageNumber = pageCount + 1;
                            }

                            Log.d("FirestorePagination", "Pagination Success: Calculated last page number");
                            latch.countDown();
                        }else {
                            Log.e("FirestorePagination", "Pagination Failed: Could not calculate last page number");
                            latch.countDown();
                        }
                    });
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }else if (this.lastPageNumber == this.pageNumber) {
            Log.d("FirestorePagination", "Pagination Success: Already on last page");
            cb.onGetPage(false, null);
            return;
        }else {
            query = this.forwardQuery;
        }

        query
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
            return;
        } else if (this.pageNumber == 1) {
            Log.d("FirestorePagination", "Pagination Success: Already on first page");
            cb.onGetPage(false, null);
            return;
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
