package com.stephen.astro.util;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by stephenadipradhana on 2/16/16.
 */
public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
        this.retryCount = 0;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> attempts) throws Exception {
        return attempts
                .flatMap(throwable -> {
                    if (++retryCount < maxRetries) {
                        // When this Observable calls onNext, the original
                        // Observable will be retried (i.e. re-subscribed).
                        return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                    }

                    // Max retries hit. Just pass the error along.
                    return Observable.error(throwable);
                });
    }
}

