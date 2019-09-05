package com.example.selene;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import com.example.selene.utils.ToastMatcher;
import org.junit.Rule;
import org.junit.Test;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;

/**
 * UITests tests user interaction using Espresso tests
 */

public class UITests {

    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule = new ActivityTestRule<>(MainActivity.class);


    final int RECYCLERVIEW_POSITION = 1;

    @Test
    public void addNewInputFAB_isClickable() {

        onView(withId(R.id.add_new_input_fab))
                .perform(click());
    }


    @Test
    public void calendarButton_isClickable() {

        onView(withId(R.id.action_calendar))
                .perform(click());
    }

    @Test
    public void recyclerViewItem_isClickable() {

        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
    }

    @Test
    public void bleedingCheckbox_isClickable() {

        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.cb_bleeding))
                .perform(click());
    }

    @Test
    public void dateButton_isClickable() {

        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.btn_selectDate))
                .perform(click());
    }

    @Test
    public void emotionSpinnerSelection_isCorrect() {
        final String EMOTION = "Happy";
        final int POSITION = 0;
        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.emotion_spinner))
                .perform(click());
        onData(anything()).atPosition(POSITION).perform(click());
        onView(withId(R.id.emotion_spinner)).check(matches(withSpinnerText(containsString(EMOTION))));
    }

    @Test
    public void physicalSpinnerSelection_isCorrect() {
        final String PHYSICAL_FEELING = "Headache";
        final int POSITION = 0;
        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.physical_spinner))
                .perform(click());
        onData(anything()).atPosition(POSITION).perform(click());
        onView(withId(R.id.physical_spinner)).check(matches(withSpinnerText(containsString(PHYSICAL_FEELING))));
    }

    @Test
    public void enterNoteText_isCorrect(){

        final String NOTE_STRING = "This is a note";
        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.enterNote))
                .perform(typeText(NOTE_STRING));
        onView(withId(R.id.btn_finishNote))
                .perform(click());

        onView(withId(R.id.enterNote))
                .check(matches(withText(NOTE_STRING)));
    }

    @Test
    public void enterNoteText_canBeEditedMultipleTimes(){
        final String FIRST_NOTE = "This is ";
        final String SECOND_NOTE = "a ";
        final String THIRD_NOTE = " note";
        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.enterNote))
                .perform(typeText(FIRST_NOTE));
        onView(withId(R.id.btn_finishNote))
                .perform(click());
        onView(withId(R.id.enterNote))
                .perform(typeText(SECOND_NOTE));
        onView(withId(R.id.btn_finishNote))
                .perform(click());
        onView(withId(R.id.btn_finishNote))
                .perform(click());
        onView(withId(R.id.enterNote))
                .perform(typeText(THIRD_NOTE));

        onView(withId(R.id.enterNote))
                .check(matches(withText(FIRST_NOTE + SECOND_NOTE + THIRD_NOTE)));
    }

    @Test
    public void clickOnSaveWithoutEnteringDate_pickDateToastIsDisplayed(){
        onView(withId(R.id.add_new_input_fab))
                .perform(click());
        onView(withId(R.id.btn_save))
                .perform(click());
        onView(withText(R.string.pick_date)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

    }

    @Test
    public void clickOnSaveButtonAfterEdit_savedToastIsDisplayed(){
        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        onView(withId(R.id.btn_edit))
                .perform(click());
        onView(withId(R.id.btn_save))
                .perform(click());
        onView(withText(R.string.saved)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void saveButtonInViewMode_notDisplayed(){


        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        onView(withId(R.id.btn_save))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void selectDateButtonInViewMode_notDisplayed(){
        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        onView(withId(R.id.btn_selectDate))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void finishNoteButtonInViewMode_notDisplayed(){
        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        onView(withId(R.id.btn_finishNote))
                .check(matches(not(isDisplayed())));
    }

    @Test
    public void editDailyInputButtonInViewMode_isDisplayedAndClickable() {
        onView(withId(R.id.recyclerView_dailyItems))
                .perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_POSITION, click()));
        onView(withId(R.id.btn_edit))
                .check(matches(isDisplayed()));
        onView(withId(R.id.btn_edit))
                .perform(click());
    }


}
