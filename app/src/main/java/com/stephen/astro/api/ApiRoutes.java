package com.stephen.astro.api;

/**
 * Created by stephenadipradhana on 12/14/16.
 */

public interface ApiRoutes {
    String ASTRO_BASE_URL = "http://ams-api.astro.com.my/";
    String FAVOURITE_BASE_URL = "http://episepalous-sons.000webhostapp.com/";

    String GET_CHANNEL_ROUTES = "ams/v3/getChannelList";
    String GET_SCHEDULE_ROUTES = "ams/v3/getEvents";

    String LOGIN_ROUTES = "login.php";
    String GET_FAVOURITES = "get_favourite.php";
    String ADD_FAVOURITE = "add_favourite.php";
    String DELETE_FAVOURITE = "delete_favourite.php";
}
