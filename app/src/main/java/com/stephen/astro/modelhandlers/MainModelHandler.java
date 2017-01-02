package com.stephen.astro.modelhandlers;

import android.content.Context;

import com.stephen.astro.realm.Favourite;
import com.stephen.astro.util.Prefs;
import com.stephen.astro.util.RetryWithDelay;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class MainModelHandler extends BaseModelHandler {
    public MainModelHandler(Context context) {
        super(context);
    }

    public RealmResults<Favourite> getFavouriteListFromDb(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Favourite.class).findAll();
    }

    public void addFavourite(int id){
        mFavouriteApiService.addFavourite(getAccessKey(), id)
                .retryWhen(new RetryWithDelay(3, 1000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    Realm realm = Realm.getDefaultInstance();

                    realm.beginTransaction();
                    Favourite favourite = realm.createObject(Favourite.class, id);
                    realm.commitTransaction();
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void removeFavourite(int id){
        mFavouriteApiService.deleteFavourite(getAccessKey(), id)
                .retryWhen(new RetryWithDelay(3, 1000))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    Realm realm = Realm.getDefaultInstance();
                    Favourite favourite = realm.where(Favourite.class).equalTo("channelId", id).findFirst();
                    realm.beginTransaction();
                    favourite.deleteFromRealm();
                    realm.commitTransaction();
                    realm.close();
                }, Throwable::printStackTrace);
    }

    public void clearCache() {
        Prefs.clear().apply();
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Favourite.class).findAll().deleteAllFromRealm();
            }
        });
        realm.close();
    }
}
