package com.github.sasd97.upitter.events;

/**
 * Created by alexander on 12.07.16.
 */
public interface OnQueueListener<T> {

    void onQueueCompete(T list);
    void onQueueError();
}
