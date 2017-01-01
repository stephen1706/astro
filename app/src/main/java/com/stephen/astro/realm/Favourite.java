package com.stephen.astro.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by stephenadipradhana on 12/28/16.
 */

public class Favourite extends RealmObject {
    @PrimaryKey
    private int channelId;

    public int getChannelId() {
        return channelId;
    }
}

