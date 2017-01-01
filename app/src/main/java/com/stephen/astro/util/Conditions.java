package com.stephen.astro.util;

import io.reactivex.Observable;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class Conditions {
    public static <T> Observable<T> notNullElse(T object, Observable<T> fallback) {
        if (object != null) {
            return Observable.just(object);
        }
        return fallback;
    }
}
