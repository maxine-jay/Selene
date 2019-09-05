package com.example.selene;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;

public class IntentsTests {

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<>(MainActivity.class);

    final int RECYCLERVIEW_POSITION = 1;

    /**
     * Tests that add new FAB navigates to DailyInputActivity
     */
    @Test
    public void addNewFabToDailyInputActivity_intentLaunched() {

        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        Intents.intended(hasComponent(DailyInputActivity.class.getName()));

    }

    @Test
    public void recyclerViewItemToDailyInputActivity_intentLaunched() {
        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        Intents.intended(hasComponent(DailyInputActivity.class.getName()));
    }

    /**
     * Tests if clicking on recycler view intent does have extra - needs this to be able to decide which layout to show on DailyInputActivity
     */
    @Test
    public void recyclerViewItemClick_intentHasExtra() {
        //define key
        String key = "selected_input";

        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        Intents.intended(hasComponent(DailyInputActivity.class.getName()));
        Intents.intended(hasExtraWithKey(key));
    }

    /**
     * Tests that clicking on FAB does NOT have intent; necessary so DailyInputActivity displays views for new input
     */
    @Test
    public void addNewFabClick_intentNotHaveExtra() {
        String key = "selected_input";

        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        Intents.intended(hasComponent(DailyInputActivity.class.getName()));
        Intents.intended(not(hasExtraWithKey(key)));
    }

    /**
     * Tests that calendar button navigates to CalendarActivity
     */
    @Test
    public void calendarButtonToCalendarActivity_intentLaunched() {

        onView(withId(R.id.action_calendar))
                .perform(click());
        Intents.intended(hasComponent(CalendarActivity.class.getName()));
    }


}
