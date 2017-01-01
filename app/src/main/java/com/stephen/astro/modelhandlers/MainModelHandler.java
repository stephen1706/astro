package com.stephen.astro.modelhandlers;

import android.content.Context;

import com.stephen.astro.realm.Favourite;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class MainModelHandler extends BaseModelHandler {
    public MainModelHandler(Context context) {
        super(context);
    }

    public RealmResults<Favourite> getFavouriteList(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Favourite.class).findAll();
    }

    public void addFavourite(int id){
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        Favourite favourite = realm.createObject(Favourite.class, id);
        realm.commitTransaction();
        realm.close();
    }

    public void removeFavourite(int id){
        Realm realm = Realm.getDefaultInstance();
                Favourite favourite = realm.where(Favourite.class).equalTo("channelId", id).findFirst();
        realm.beginTransaction();
        favourite.deleteFromRealm();
        realm.commitTransaction();
        realm.close();
    }
}
