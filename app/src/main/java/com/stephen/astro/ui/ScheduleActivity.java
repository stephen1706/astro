package com.stephen.astro.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.stephen.astro.R;
import com.stephen.astro.adapter.ChannelScheduleAdapter;
import com.stephen.astro.adapter.ScheduleAdapter;
import com.stephen.astro.modelhandlers.ScheduleModelHandler;
import com.stephen.astro.util.ViewUtils;
import com.stephen.astro.viewmodels.ScheduleListViewModel;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

import io.reactivex.Observable;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        mModelHandler = new ScheduleModelHandler(this);

        setUpAttribute();
        setUpView();
        setUpRequestAPI(SORT_NUMBER);
    }

    private void setUpAttribute() {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
    }

    private void setUpRequestAPI(int sort) {
        mCurrentSort = sort;

        ProgressDialog progressDialog = ViewUtils.showProgressDialog(this, getString(R.string.loading));
        Observable<ArrayList<ScheduleListViewModel>> observable;

        mModelHandler.getScheduleList(sort, (Calendar) calendar.clone())
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(scheduleListViewModels -> {
                    if(scheduleListViewModels.size() == 0){
                        Toast.makeText(this, R.string.no_schedule, Toast.LENGTH_SHORT).show();
                    }
                    ScheduleAdapter adapter = new ScheduleAdapter(this, scheduleListViewModels);
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
                    Toast.makeText(ScheduleActivity.this, R.string.fail_get_schedule, Toast.LENGTH_SHORT).show();
                });
    }

    private void setUpView() {
        contentListView = (ListView) findViewById(R.id.list_view_content);
        channelListView = (ListView) findViewById(R.id.list_view_channel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule, menu);
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
            case R.id.change_date:
                Fragment picker = getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
                if(picker == null || !picker.isAdded()) {
                    datePickerDialog.setVibrate(false);
                    datePickerDialog.setYearRange(2016, 2017);
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
        setUpRequestAPI(mCurrentSort);
    }
}
