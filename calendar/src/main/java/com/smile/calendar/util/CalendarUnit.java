package com.smile.calendar.util;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Blaz Solar on 27/02/14.
 */
public abstract class CalendarUnit {

    @IntDef({ TYPE_WEEK, TYPE_MONTH })
    @Retention(RetentionPolicy.SOURCE)
    public @interface CalendarType { }

    public static final int TYPE_WEEK = 1;
    public static final int TYPE_MONTH = 2;

    private final LocalDate mToday;
    private LocalDate mFrom;
    private LocalDate mTo;
    private boolean mSelected;

    protected CalendarUnit(LocalDate from, LocalDate to, LocalDate today) {
        mToday = today;
        mFrom = from;
        mTo = to;
    }

    public LocalDate getToday() {
        return mToday;
    }

    public LocalDate getFrom() {
        return mFrom;
    }

    public LocalDate getTo() {
        return mTo;
    }

    protected void setFrom(  LocalDate from) {
        mFrom = from;
    }

    protected void setTo(  LocalDate to) {
        mTo = to;
    }

    protected void setSelected(boolean selected) {
        mSelected = selected;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public abstract boolean hasNext();

    public abstract boolean hasPrev();

    public abstract boolean next();

    public abstract boolean prev();

    @CalendarType public abstract int getType();

    public boolean isIn(  LocalDate date) {
        return !mFrom.isAfter(date) && !mTo.isBefore(date);
    }

    public boolean isInView(  LocalDate date) {
        return !mFrom.withDayOfWeek(DateTimeConstants.MONDAY).isAfter(date)
                && !mTo.withDayOfWeek(DateTimeConstants.SUNDAY).isBefore(date);
    }

    public abstract void deselect(  LocalDate date);

    public abstract boolean select(  LocalDate date);

    public abstract void build();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CalendarUnit)) return false;

        CalendarUnit that = (CalendarUnit) o;

        if (mSelected != that.mSelected) return false;
        if (!mFrom.equals(that.mFrom)) return false;
        if (!mTo.equals(that.mTo)) return false;
        return mToday.equals(that.mToday);

    }

    @Override
    public int hashCode() {
        int result = mToday.hashCode();
        result = 31 * result + mFrom.hashCode();
        result = 31 * result + mTo.hashCode();
        result = 31 * result + (mSelected ? 1 : 0);
        return result;
    }
}
