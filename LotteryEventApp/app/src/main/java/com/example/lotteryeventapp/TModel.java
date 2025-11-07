package com.example.lotteryeventapp;

import java.util.ArrayList;

public abstract class TModel<V extends TView> {
    private ArrayList<V> views = new ArrayList<V>();

    public TModel() {

    }

    public void addView(V view) {
        if (view != null && !views.contains(view)) {
            views.add(view);
        }
    }

    public void removeView(V view) {
        if (view != null) {
            views.remove(view);
        }
    }

    public void notifyAllViews() {
        for (V view: views) {
            view.update(this);
        }
    }
}