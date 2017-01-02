package com.stephen.astro.datamodels;

import com.google.gson.annotations.SerializedName;

/**
 * Created by stephenadipradhana on 1/3/17.
 */

public class LoginDataModel {
    @SerializedName("access_key")
    private String accessKey;

    public String getAccessKey() {
        return accessKey;
    }
}
