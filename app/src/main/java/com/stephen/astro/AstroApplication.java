package com.stephen.astro;

import android.app.Application;

import com.stephen.astro.provider.ApiProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class AstroApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Constants.REALM_DATABASE_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        initProvider();
    }

    private void initProvider() {
        ApiProvider.setUp();
    }
}
