package com.stephen.astro.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.stephen.astro.R;
import com.stephen.astro.adapter.ChannelScheduleAdapter;
import com.stephen.astro.adapter.ScheduleAdapter;
import com.stephen.astro.modelhandlers.ScheduleModelHandler;
import com.stephen.astro.util.ViewUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class TestActivity extends RxAppCompatActivity {
    private ScheduleModelHandler mModelHandler;
    private ListView channelListView;
    private ListView contentListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mModelHandler = new ScheduleModelHandler(this);

        contentListView = (ListView) findViewById(R.id.list_view_content);
        channelListView = (ListView) findViewById(R.id.list_view_channel);

        ProgressDialog progressDialog = ViewUtils.showProgressDialog(this, getString(R.string.loading));
        mModelHandler.getScheduleList()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(scheduleListViewModels -> {
                    ScheduleAdapter adapter = new ScheduleAdapter(TestActivity.this, scheduleListViewModels);
                    contentListView.setAdapter(adapter);

                    ChannelScheduleAdapter adapter1 = new ChannelScheduleAdapter(this, R.layout.row_channel, scheduleListViewModels);
                    channelListView.setAdapter(adapter1);
                    progressDialog.dismiss();

                    channelListView.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            return (event.getAction() == MotionEvent.ACTION_MOVE);
                        }
                    });

                    contentListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                            if (view.getChildAt(0) != null) {
                                channelListView.setSelectionFromTop(firstVisibleItem, view.getChildAt(0).getTop());
                            }
                        }

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {
                        }
                    });
                }, throwable -> {
                    throwable.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(TestActivity.this, R.string.fail_get_schedule, Toast.LENGTH_SHORT).show();
                });
    }
}
