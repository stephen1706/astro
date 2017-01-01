package com.stephen.astro.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.stephen.astro.R;
import com.stephen.astro.adapter.ChannelListAdapter;
import com.stephen.astro.modelhandlers.MainModelHandler;
import com.stephen.astro.util.ViewUtils;
import com.stephen.astro.viewmodels.ChannelViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;

import io.reactivex.Observable;

import static com.stephen.astro.Constants.SORT_NAME;
import static com.stephen.astro.Constants.SORT_NUMBER;

public class MainActivity extends RxAppCompatActivity {

    private RecyclerView mRecyclerView;
    private MainModelHandler mModelHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mModelHandler = new MainModelHandler(this);

        setUpView();
        setUpRequestAPI(SORT_NUMBER);
    }

    private void setUpRequestAPI(int sort) {
        ProgressDialog progressDialog = ViewUtils.showProgressDialog(this, getString(R.string.loading));
        Observable<ArrayList<ChannelViewModel>> observable;

        if (sort == SORT_NAME) {
            observable = mModelHandler.getChannelListSortedByName();
        } else {
            observable = mModelHandler.getChannelListByNumber();
        }
        observable.compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe((channelViewModels) -> {
                    setUpAdapter(channelViewModels);
                    progressDialog.dismiss();
                }, throwable -> {
                    progressDialog.dismiss();
                    throwable.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.fail_get_channel_list, Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpAdapter(ArrayList<ChannelViewModel> channelViewModels) {
        ChannelListAdapter adapter = new ChannelListAdapter(channelViewModels, new ChannelListAdapter.OnFavouriteListener() {
            @Override
            public void onLike(int id) {
                Log.d("test","like : " + id);
                mModelHandler.addFavourite(id);
            }

            @Override
            public void onDislike(int id) {
                Log.d("test","dislike : " + id);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_name:
                setUpRequestAPI(SORT_NAME);
                break;
            case R.id.sort_number:
                setUpRequestAPI(SORT_NUMBER);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
