package com.stephen.astro.api;

import com.google.gson.JsonElement;
import com.stephen.astro.datamodels.LoginDataModel;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by stephenadipradhana on 1/3/17.
 */

public interface FavouriteApiService {
    @FormUrlEncoded
    @POST(ApiRoutes.LOGIN_ROUTES)
    Observable<LoginDataModel> login(@Field("email") String email);

    @GET(ApiRoutes.GET_FAVOURITES)
    Observable<ArrayList<Integer>> getFavourite(@Query("access_key") String accessKey);

    @FormUrlEncoded
    @POST(ApiRoutes.ADD_FAVOURITE)
    Observable<JsonElement> addFavourite(@Field("access_key") String accessKey, @Field("channel_id") int id);

    @FormUrlEncoded
    @POST(ApiRoutes.DELETE_FAVOURITE)
    Observable<JsonElement> deleteFavourite(@Field("access_key") String accessKey, @Field("channel_id") int id);
}
