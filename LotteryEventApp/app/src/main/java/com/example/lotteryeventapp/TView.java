package com.example.lotteryeventapp;

public interface TView<M extends TModel> {
    public void update(M model);
}