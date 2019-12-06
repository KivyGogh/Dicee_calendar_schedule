package com.smilexie.weekview.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.smile.calendar.manager.CalendarManager;
import com.smile.calendar.module.EventModel;
import com.smile.calendar.util.RealmHelper;
import com.smile.calendar.view.CollapseCalendarView;
import com.smilexie.weekview.sample.adapter.ScheduleAdapter;
import com.smilexie.weekview.sample.apiclient.Event;
import com.smilexie.weekview.sample.apiclient.MyJsonService;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * 日程列表形式
 * Created by SmileXie on 2017/5/8.
 */

public class CalendarScheduleActivity extends AppCompatActivity {

    private LocalDate selectedDate;//当前选择的日期
    private CalendarManager mManager;
    private CollapseCalendarView calendarView;
    private RecyclerView recyclerView;
    private ScheduleAdapter scheduleAdapter;
    private HashMap<Integer, List<Event>> eventMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        CollapseCalendarView.withMonthSchedule = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);
        calendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(this);
        recyclerView.setAdapter(scheduleAdapter);
        eventMap = new HashMap<>();
        initCalendarListener();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        loadData();
    }


    private void loadData() {
        List<EventModel> eventModels = RealmHelper.getRealmHelperInstance().queryAllEvent();

        List<Event> events = new ArrayList<>();
        for (int i = 0; i< eventModels.size(); i++) {
            Event event =toEvent(eventModels.get(i));
            event.setColor(getColorByIndex(i));
            events.add(event);
        }
        for (int i = 0; i < events.size(); i ++) {
            int key = events.get(i).getDayOfMonth();
            if (!eventMap.containsKey(key)) {
                eventMap.put(key, new ArrayList<Event>());
            }
            (eventMap.get(key)).add(events.get(i));
        }
        if (eventMap.containsKey(selectedDate.getDayOfMonth())) {
            scheduleAdapter.setMSchedule(eventMap.get(selectedDate.getDayOfMonth()));
        }
    }

    private Event toEvent(EventModel eventModel) {
        Event event = new Event();
        event.setDayOfMonth(eventModel.getDay());
        event.setEndTime(eventModel.getEndTime());
        event.setName(eventModel.getName());
        event.setStartTime(eventModel.getStartTime());
        return event;
    }

    private int getColorByIndex(int index) {
        switch (index % 4) {
            case 0:
                return getResources().getColor(R.color.event_color_01);
            case 1:
                return getResources().getColor(R.color.event_color_02);
            case 2:
                return getResources().getColor(R.color.event_color_03);
            case 3:
                return getResources().getColor(R.color.event_color_04);
        }
        return 0;
    }

    private void initCalendarListener() {
        selectedDate = LocalDate.now();
        mManager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(60));
        //月份切换监听器
        mManager.setMonthChangeListener(new CalendarManager.OnMonthChangeListener() {

            @Override
            public void monthChange(String month, LocalDate mSelected) {
                setTitle(month);
            }

            @Override
            public void weekChange(String week, LocalDate mSelected) {//周切换

            }
        });
        calendarView.init(mManager);
        /**
         * 日期选中监听器
         */
        calendarView.setDateSelectListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                selectedDate = date;
                if (eventMap.containsKey(selectedDate.getDayOfMonth())) {
                    scheduleAdapter.setMSchedule(eventMap.get(selectedDate.getDayOfMonth()));
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
