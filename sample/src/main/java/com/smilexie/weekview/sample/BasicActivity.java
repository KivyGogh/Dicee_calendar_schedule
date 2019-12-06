package com.smilexie.weekview.sample;

import com.alamkanak.weekview.WeekViewEvent;
import com.smile.calendar.module.EventModel;
import com.smile.calendar.util.RealmHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A basic example of how to use week view library.
 * Created by Raquib-ul-Alam Kanak on 1/3/2014.
 * Website: http://alamkanak.github.io
 */
public class BasicActivity extends BaseActivity {

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        List<EventModel> eventModels = RealmHelper.getRealmHelperInstance().queryAllEvent();
        for (int i =0; i < eventModels.size(); i++) {
            EventModel eventModel = eventModels.get(i);
            if (eventModel.getYear() != newYear || eventModel.getMonth() != newMonth) {
                continue;
            }
            WeekViewEvent weekViewEvent = toWeekView(eventModels.get(i));
            weekViewEvent.setColor(getColorByIndex(i));
            events.add(weekViewEvent);
        }
        return events;
    }

    private WeekViewEvent toWeekView(EventModel eventModel) {

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.DAY_OF_MONTH, eventModel.getDay());
        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(eventModel.getStartTime().split(":")[0]));
        startTime.set(Calendar.MINUTE, Integer.valueOf(eventModel.getStartTime().split(":")[1]));
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);
        startTime.set(Calendar.MONTH, eventModel.getMonth() - 1);
        startTime.set(Calendar.YEAR, eventModel.getYear());
        Calendar endTime = (Calendar) startTime.clone();
        endTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(eventModel.getEndTime().split(":")[0]));
        endTime.set(Calendar.MINUTE, Integer.valueOf(eventModel.getEndTime().split(":")[1]));
        WeekViewEvent event = new WeekViewEvent(Long.valueOf(eventModel.getId()), eventModel.getStartTime(), null,
                startTime, endTime, false);
        event.setName(eventModel.getName());
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
}
