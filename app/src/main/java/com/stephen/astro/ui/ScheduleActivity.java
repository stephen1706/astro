package com.stephen.astro.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.stephen.astro.PaginationFinishException;
import com.stephen.astro.R;
import com.stephen.astro.adapter.ChannelScheduleAdapter;
import com.stephen.astro.adapter.ScheduleAdapter;
import com.stephen.astro.modelhandlers.ScheduleModelHandler;
import com.stephen.astro.util.ViewUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.stephen.astro.Constants.PAGINATION_LENGTH;
import static com.stephen.astro.Constants.SORT_FAVOURITE;
import static com.stephen.astro.Constants.SORT_NAME;
import static com.stephen.astro.Constants.SORT_NUMBER;

/**
 * Created by stephenadipradhana on 12/30/16.
 */

public class ScheduleActivity extends RxAppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private static final String DATEPICKER_TAG = "datepicker";

    private ScheduleModelHandler mModelHandler;
    private ListView channelListView;
    private ListView contentListView;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mCurrentSort;
    private int mIndex;
    private ScheduleAdapter scheduleAdapter;
    private ChannelScheduleAdapter channelAdapter;
    private boolean isLoading;
    private ProgressDialog progressDialog;
    private boolean isFinished;
    private View loadingFrame;
    private SimpleDateFormat mSimpleDateFormat;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mModelHandler = new ScheduleModelHandler(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpAttribute();
        setUpView();
        setUpRequestAPI(SORT_NUMBER);
    }

    private void setUpAttribute() {
        mIndex = 0;
        isLoading = false;
        mCurrentSort = SORT_NUMBER;

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);

        mSimpleDateFormat = new SimpleDateFormat("d MMM");
    }

    private void setUpRequestAPI(int sort) {
        isLoading = true;
        mCurrentSort = sort;
        if (mIndex == 0) {
            loadingFrame.setVisibility(View.GONE);
            progressDialog = ViewUtils.showProgressDialog(this, getString(R.string.loading));
        } else {
            loadingFrame.setVisibility(View.VISIBLE);
        }

        mModelHandler.getScheduleList(sort, (Calendar) calendar.clone(), mIndex)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(scheduleListViewModels -> {
                    getSupportActionBar().setTitle(getString(R.string.tv_guide_date,
                            mSimpleDateFormat.format(calendar.getTime())));

                    if (scheduleListViewModels.size() == 0) {
                        Toast.makeText(this, R.string.no_schedule, Toast.LENGTH_SHORT).show();
                    }

                    if (mIndex == 0) {
                        scheduleAdapter = new ScheduleAdapter(this, scheduleListViewModels);
                        contentListView.setAdapter(scheduleAdapter);

                        channelAdapter = new ChannelScheduleAdapter(this, R.layout.row_channel, scheduleListViewModels);
                        channelListView.setAdapter(channelAdapter);
                        setUpListener();
                    } else {
                        scheduleAdapter.addAll(scheduleListViewModels);
                        channelAdapter.addAll(scheduleListViewModels);
                    }

                    isLoading = false;
                    mIndex += PAGINATION_LENGTH;
                    progressDialog.dismiss();
                    loadingFrame.setVisibility(View.GONE);
                }, throwable -> {
                    throwable.printStackTrace();
                    progressDialog.dismiss();
                    loadingFrame.setVisibility(View.GONE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                        }
                    }, 1000);//avoid re-requesting to fast when end of pagination

                    if (throwable instanceof PaginationFinishException) {
                        isFinished = true;
                    } else {
                        Toast.makeText(ScheduleActivity.this, R.string.fail_get_schedule, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setUpListener() {
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

                final int lastItem = firstVisibleItem + visibleItemCount;

                if (!isFinished && !isLoading && lastItem == totalItemCount) {
                    //reach bottom
                    Log.d("test", "reach bottom");
                    setUpRequestAPI(mCurrentSort);
                }
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

        });
    }

    private void setUpView() {
        contentListView = (ListView) findViewById(R.id.list_view_content);
        channelListView = (ListView) findViewById(R.id.list_view_channel);
        loadingFrame = findViewById(R.id.frame_loading);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort_name:
                mIndex = 0;
                setUpRequestAPI(SORT_NAME);
                break;
            case R.id.sort_number:
                mIndex = 0;
                setUpRequestAPI(SORT_NUMBER);
                break;
            case R.id.sort_favourite:
                mIndex = 0;
                setUpRequestAPI(SORT_FAVOURITE);
                break;
            case R.id.change_date:
                Fragment picker = getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
                if (picker == null || !picker.isAdded()) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(2017, 2018);
                    datePickerDialog.setCloseOnSingleTapDay(true);
                    datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        mIndex = 0;
        setUpRequestAPI(mCurrentSort);
    }

}
