package com.stephen.astro.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.stephen.astro.R;
import com.stephen.astro.adapter.ChannelListAdapter;
import com.stephen.astro.modelhandlers.MainModelHandler;
import com.stephen.astro.viewmodels.ChannelViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import static com.stephen.astro.Constants.SORT_FAVOURITE;
import static com.stephen.astro.Constants.SORT_NAME;
import static com.stephen.astro.Constants.SORT_NUMBER;

public class MainActivity extends RxAppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainModelHandler mModelHandler;
    private ArrayList<String> mFavouriteList;
    private SwipeRefreshLayout mSwipeLayout;
    private int mCurrentSort;
    private Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mModelHandler = new MainModelHandler(this);
        getSupportActionBar().setTitle(R.string.channel_list);

        setUpView();
        setUpListener();
        setUpRequestAPI(SORT_NUMBER);
    }

    private void setUpListener() {
        mSwipeLayout.setOnRefreshListener(() -> setUpRequestAPI(mCurrentSort));
    }


    private void setUpRequestAPI(int sort) {
        if(mDisposable != null && !mDisposable.isDisposed()){//stop current running request
            mDisposable.dispose();
        }

        mCurrentSort = sort;
        new Handler().post(() -> mSwipeLayout.setRefreshing(true));

        Observable<ArrayList<ChannelViewModel>> observable;

        if (sort == SORT_NAME) {
            observable = mModelHandler.getChannelListSortedByName();
        } else if(sort == SORT_NUMBER){
            observable = mModelHandler.getChannelListByNumber();
        } else {
            observable = mModelHandler.getChannelListSortedByFavourite();
        }

        mDisposable = observable.compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe((channelViewModels) -> {
                    setUpAdapter(channelViewModels);
                    mSwipeLayout.setRefreshing(false);
                }, throwable -> {
                    mSwipeLayout.setRefreshing(false);
                    throwable.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.fail_get_channel_list, Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpAdapter(ArrayList<ChannelViewModel> channelViewModels) {
        ChannelListAdapter adapter = new ChannelListAdapter(channelViewModels, new ChannelListAdapter.OnFavouriteListener() {
            @Override
            public void onLike(int id) {
                mModelHandler.addFavourite(id);
            }

            @Override
            public void onDislike(int id) {
                mModelHandler.removeFavourite(id);
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
    }

    private void setUpView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tv_guide:
                startActivity(new Intent(this, ScheduleActivity.class));
                break;
            case R.id.sort_name:
                setUpRequestAPI(SORT_NAME);
                break;
            case R.id.sort_number:
                setUpRequestAPI(SORT_NUMBER);
                break;
            case R.id.sort_favourite:
                setUpRequestAPI(SORT_FAVOURITE);
                break;
            case R.id.logout:
                mModelHandler.clearCache();
                LoginManager.getInstance().logOut();
                TaskStackBuilder.create(this)
                        .addNextIntent(new Intent(this, LoginActivity.class))
                        .startActivities();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
