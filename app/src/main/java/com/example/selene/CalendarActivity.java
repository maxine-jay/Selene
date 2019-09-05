package com.example.selene;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.selene.models.DailyInput;
import com.example.selene.database.DailyInputRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import pl.rafman.scrollcalendar.ScrollCalendar;
import pl.rafman.scrollcalendar.contract.DateWatcher;
import pl.rafman.scrollcalendar.contract.MonthScrollListener;
import pl.rafman.scrollcalendar.contract.OnDateClickListener;
import pl.rafman.scrollcalendar.data.CalendarDay;
/**
 * ScrollCalendar was created by rafal.manka
 *
 * https://github.com/RafalManka/ScrollCalendar
 */
/*
  CalendarActivity displays the ScrollCalendar and contains methods for interacting
  with the calendar
 */

public class CalendarActivity extends AppCompatActivity {

    DailyInputRepository mDailyInputRepository = new DailyInputRepository(this);
    List<DailyInput> mDailyInputs = new ArrayList<>();

    private static final String CALENDAR = "Calendar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(CALENDAR);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        observeBleedingDates();

        final ScrollCalendar scrollCalendar = findViewById(R.id.scrollCalendar);
        scrollCalendar.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onCalendarDayClicked(int year, int month, int day) {

            }
        });

        //allows appearance of dates to be manipulated
        scrollCalendar.getAdapter().setDateWatcher(new DateWatcher() {
            @Override
            public int getStateForDate(int year, int month, int day) {

                //a variable with today's date
                Calendar today = Calendar.getInstance();

                //a variable for every day of the scrolling calendar
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                calendar.set(Calendar.HOUR_OF_DAY, 12);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                Date calendarDate = calendar.getTime();


                if (year == today.get(Calendar.YEAR) && month == today.get(Calendar.MONTH) && day == today.get(Calendar.DAY_OF_MONTH)) {
                    //shows a circle outline around today's date
                    return CalendarDay.TODAY;
                }
                else {
                    //shows a green circle around dates which have been marked as bleeding
                    for (DailyInput dailyInput : mDailyInputs) {
                        if (dailyInput.getDate().equals(calendarDate)) {
                            return CalendarDay.SELECTED;
                        }
                    }
                }

                //show regular date
                return CalendarDay.DEFAULT;
            }
        });

        scrollCalendar.setMonthScrollListener(new MonthScrollListener() {
            @Override
            public boolean shouldAddNextMonth(int lastDisplayedYear, int lastDisplayedMonth) {
                // returns false so as not to show future months
                return false;
            }

            @Override
            public boolean shouldAddPreviousMonth(int firstDisplayedYear, int firstDisplayedMonth) {
                // returns true to show previous months
                return true;
            }
        });


    }

    //overrides animated transition for home button
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return false;
    }

    //retrieves all dates marked as bleeding from the database
    //populates mDailyInputs with ONLY bleeding dates
    public void observeBleedingDates() {
        mDailyInputRepository.retrieveBleedingInputTask().observe(this, new Observer<List<DailyInput>>() {
            @Override
            public void onChanged(List<DailyInput> dailyInputs) {
                mDailyInputs = dailyInputs;

            }
        });

    }
}
