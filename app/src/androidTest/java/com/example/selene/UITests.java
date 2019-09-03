package com.example.selene;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

public class UITests {

    @Test
    public void testEditNote(){

        onView(withId(R.id.enterNote))
                .perform(typeTextIntoFocusedView("test string"),
                        closeSoftKeyboard());
        onView(withId(R.id.btn_finishNote)).perform(click());
    }
}
