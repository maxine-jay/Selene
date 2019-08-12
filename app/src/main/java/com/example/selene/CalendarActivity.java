package com.example.selene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.os.Bundle;

import com.example.selene.Models.DailyInput;
import com.example.selene.Room.DailyInputRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.data.CalendarDay;

public class CalendarActivity extends AppCompatActivity {

    private ArrayList mBleedingDates = new ArrayList();
    DailyInputRepository mDailyInputRepository = new DailyInputRepository(this);
    List<DailyInput> mDailyInputs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        observeBleedingDates();


        final ScrollCalendar scrollCalendar = findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {

            }
        });

        scrollCalendar.getAdapter().setDateWatcher(new DateWatcher() {
            @Override
            public int getStateForDate(int year, int month, int day) {


                Calendar today = Calendar.getInstance();

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date calendarDate = calendar.getTime();

                if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH) && day == today.get(Calendar.DAY_OF_MONTH)) {

                    return CalendarDay.TODAY;
                }


                for (DailyInput dailyInput : mDailyInputs) {
                    if (dailyInput.getDate().equals(calendarDate)) {
                        return CalendarDay.SELECTED;
                    }
                }


                return CalendarDay.DEFAULT;
            }
        });

        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                // return false if you don't want to show later months
                return true;
            }

            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                // return false if you don't want to show previous months
                return true;
            }
        });


    }

    public void observeBleedingDates() {
        mDailyInputRepository.retrieveBleedingInputTask().observe(this, new Observer<List<DailyInput>>() {
            @Override
            public void onChanged(List<DailyInput> dailyInputs) {
                mDailyInputs = dailyInputs;
            }
        });

    }
}
