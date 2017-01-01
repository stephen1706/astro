package com.stephen.astro.modelhandlers;

import android.content.Context;

import com.stephen.astro.AstroDataBridge;
import com.stephen.astro.api.ApiService;
import com.stephen.astro.datamodels.ChannelListDataModel;
import com.stephen.astro.provider.ApiProvider;
import com.stephen.astro.comparator.ChannelComparatorByName;
import com.stephen.astro.comparator.ChannelComparatorByNumber;
import com.stephen.astro.util.Conditions;
import com.stephen.astro.viewmodels.ChannelViewModel;

import java.util.ArrayList;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by stephenadipradhana on 12/14/16.
 */


public class BaseModelHandler {
    protected final Context mContext;
    protected final ApiService mApiService;
    protected ChannelListDataModel mList;

    public BaseModelHandler(Context context) {
        this.mContext = context;
        mApiService = ApiProvider.getApiService();
    }

    public Observable<ArrayList<ChannelViewModel>> getChannelList(){
        return Conditions.notNullElse(mList, mApiService.getChannelList())
                .subscribeOn(Schedulers.io())
                .doOnNext(channelListDataModel -> mList = channelListDataModel)
                .observeOn(AndroidSchedulers.mainThread())
                .map(AstroDataBridge::getChannelListViewModel)
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
}
