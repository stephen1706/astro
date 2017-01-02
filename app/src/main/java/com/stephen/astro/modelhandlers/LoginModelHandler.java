package com.stephen.astro.modelhandlers;

import android.content.Context;
import android.text.TextUtils;

import com.stephen.astro.SharedPreferenceConstants;
import com.stephen.astro.datamodels.LoginDataModel;
import com.stephen.astro.util.Prefs;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by stephenadipradhana on 1/3/17.
 */

public class LoginModelHandler extends BaseModelHandler {
    public LoginModelHandler(Context context) {
        super(context);
    }

    public Observable<LoginDataModel> login(String email){
        return mFavouriteApiService.login(email)
                .doOnNext(key -> Prefs.putString(SharedPreferenceConstants.ACCESS_KEY, key.getAccessKey()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(Prefs.getString(SharedPreferenceConstants.ACCESS_KEY, null));
    }
}
