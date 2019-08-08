package com.example.selene;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import java.util.Calendar;

import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.data.CalendarDay;

public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);





        final ScrollCalendar scrollCalendar = findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {

            }
        });

        scrollCalendar.getAdapter().setDateWatcher(new DateWatcher() {
            @Override
            public int getStateForDate(int year, int month, int day) {

                //this circles every single day!
                Calendar today = Calendar.getInstance();

                if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH) && day == today.get(Calendar.DAY_OF_MONTH)) {

                    return CalendarDay.TODAY;
                }else {

                    return CalendarDay.DEFAULT;
                }
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
}
