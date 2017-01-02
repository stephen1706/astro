package com.stephen.astro.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.stephen.astro.api.ApiRoutes;
import com.stephen.astro.api.AstroApiService;
import com.stephen.astro.api.FavouriteApiService;
import com.stephen.astro.api.NullOnEmptyConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by stephenadipradhana on 12/14/16.
 */

public class ApiProvider {
    private static AstroApiService astroApiService;
    private static FavouriteApiService favouriteApiService;

    public static void setUp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Retrofit astroRetrofit = new Retrofit.Builder()
                .baseUrl(ApiRoutes.ASTRO_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        Retrofit favouriteRetrofit = new Retrofit.Builder()
                .baseUrl(ApiRoutes.FAVOURITE_BASE_URL)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        astroApiService = astroRetrofit.create(AstroApiService.class);
        favouriteApiService = favouriteRetrofit.create(FavouriteApiService.class);
    }

    public static FavouriteApiService getFavouriteApiService() {
        return favouriteApiService;
    }

    public static AstroApiService getAstroApiService() {
        return astroApiService;
    }
}
