package com.stephen.astro;

import java.util.concurrent.TimeUnit;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public interface Constants {
    long SERVER_TIMEZONE = TimeUnit.HOURS.toMillis(8);//server time is +8 GMT, request must be in +8 not in UTC
    String REALM_DATABASE_NAME = "weather.realm";

    public static final int SORT_NUMBER = 0;
    public static final int SORT_NAME = 1;

}
