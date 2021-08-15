package com.example.taskmaster;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.action.ViewActions.*;


@RunWith(AndroidJUnit4.class)
public class Espresso {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule=new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void UIElementsTest(){
onView(withId(R.id.add_task)).check(matches(withText("ADD TASK")));
    }

    @Test
    public void tabOnTaskTest(){
        onView((withId(R.id.taskOne))).perform(click());
        onView(withId(R.id.taskTitleDetail)).check(matches(withText("task one")));
    }
    @Test
    public void editingUserNameTest(){
        onView(withText("Setting Page")).perform(click());
        onView(withId(R.id.settingsUserName)).perform(clearText()).perform(typeText("Moe"));

        onView(withId(R.id.setting_button)).perform(click());
        onView(withId(R.id.shared_user_name)).check(matches(withText("Moe")) );
    }
}
