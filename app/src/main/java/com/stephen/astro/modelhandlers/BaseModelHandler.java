package com.stephen.astro.modelhandlers;

import android.content.Context;

import com.stephen.astro.AstroDataBridge;
import com.stephen.astro.SharedPreferenceConstants;
import com.stephen.astro.api.AstroApiService;
import com.stephen.astro.api.FavouriteApiService;
import com.stephen.astro.comparator.ChannelComparatorByName;
import com.stephen.astro.comparator.ChannelComparatorByNumber;
import com.stephen.astro.datamodels.ChannelListDataModel;
import com.stephen.astro.provider.ApiProvider;
import com.stephen.astro.realm.Favourite;
import com.stephen.astro.util.Conditions;
import com.stephen.astro.util.Prefs;
import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by stephenadipradhana on 12/14/16.
 */

public class BaseModelHandler {
    protected final Context mContext;
    protected final AstroApiService mAstroApiService;
    protected final FavouriteApiService mFavouriteApiService;
    protected ChannelListDataModel mList;
    protected ArrayList<Integer> mFavouriteList;

    public BaseModelHandler(Context context) {
        this.mContext = context;
        mAstroApiService = ApiProvider.getAstroApiService();
        mFavouriteApiService = ApiProvider.getFavouriteApiService();
    }

    public Observable<ArrayList<Integer>> getFavourite() {
        return mFavouriteApiService.getFavourite(getAccessKey())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(strings -> {//store in local cache
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            realm.where(Favourite.class).findAll().deleteAllFromRealm();
                            for (int id : strings) {
                                realm.createObject(Favourite.class, id);
                            }
                        }
                    });
                })
                .onErrorResumeNext(throwable -> {//if any error, use from local database
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Favourite> result = realm.where(Favourite.class)
                            .findAll();
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    for(Favourite favourite : result){
                        list.add(favourite.getChannelId());
                    }
                    return Observable.just(list);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<ChannelViewModel>> getChannelList(){
        return getFavourite()
                .observeOn(Schedulers.io())
                .flatMap(strings -> {
                    mFavouriteList = strings;
                    return Conditions.notNullElse(mList, mAstroApiService.getChannelList());
                })
                .doOnNext(channelListDataModel -> mList = channelListDataModel)
                .observeOn(AndroidSchedulers.mainThread())
                .map((channelListDataModel1) -> AstroDataBridge.getChannelListViewModel(channelListDataModel1, mFavouriteList))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<ChannelViewModel>> getChannelListByNumber(){
        return getChannelList()
                .subscribeOn(Schedulers.newThread())
                .map((viewModels) -> {
                    Collections.sort(viewModels, new ChannelComparatorByNumber());
                    return viewModels;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ArrayList<ChannelViewModel>> getChannelListSortedByName(){
        return getChannelList()
                .subscribeOn(Schedulers.newThread())
                .map((viewModels) -> {
                    Collections.sort(viewModels, new ChannelComparatorByName());
                    return viewModels;
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    public ArrayList<Integer> getFavouriteList() {
        return mFavouriteList;
    }

    protected String getAccessKey() {
        return Prefs.getString(SharedPreferenceConstants.ACCESS_KEY, null);
    }
}

