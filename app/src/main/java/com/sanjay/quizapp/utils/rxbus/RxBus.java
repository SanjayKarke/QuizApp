package com.sanjay.quizapp.utils.rxbus;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class RxBus {
    private static RxBus instance;
    private final PublishSubject<Object> bus = PublishSubject.create();

    public static synchronized RxBus getInstance() {
        if (instance == null) {
            instance = new RxBus();
        }
        return instance;
    }

    public void send(Object event) {
        bus.onNext(event);
    }

    public Observable<Object> toObservable() {
        return bus;
    }

}
