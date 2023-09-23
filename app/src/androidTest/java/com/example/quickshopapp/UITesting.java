package com.example.quickshopapp;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UITesting {

    @Rule
    public ActivityScenarioRule<SplashScreenActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(SplashScreenActivity.class);

    @Test
    public void uITesting() {
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.signInTextView), withText("Already a customer? Sign-In"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        0),
                                3),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.loginEmail),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                3),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("swapnaponna@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.loginPassword),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Ponna123"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.loginPassword), withText("Ponna123"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                4),
                        isDisplayed()));
        appCompatEditText3.perform(pressImeActionButton());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.signInButton), withText("Sign-In"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.FrameLayout")),
                                        1),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        ViewInteraction cardView = onView(
                allOf(withId(R.id.shoes1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.HorizontalScrollView")),
                                        0),
                                0)));
        cardView.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.add_to_cart_button), withText("Add to Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.viewAllProducts), withText("VIEW ALL"),
                        childAtPosition(
                                allOf(withId(R.id.home_layout),
                                        childAtPosition(
                                                withClassName(is("androidx.core.widget.NestedScrollView")),
                                                0)),
                                6),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.searchList),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                1)));
        recyclerView.perform(actionOnItemAtPosition(3, click()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.add_to_cart_button), withText("Add to Cart"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.core.widget.NestedScrollView")),
                                        0),
                                7),
                        isDisplayed()));
        appCompatButton3.perform(click());

        ViewInteraction materialRadioButton = onView(
                allOf(withId(R.id.bottom_cart), withText("My Cart"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup1),
                                        childAtPosition(
                                                withId(R.id.bottomNavBar),
                                                0)),
                                3),
                        isDisplayed()));
        materialRadioButton.perform(click());

        ViewInteraction materialRadioButton2 = onView(
                allOf(withId(R.id.bottom_profile), withText("Profile"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup1),
                                        childAtPosition(
                                                withId(R.id.bottomNavBar),
                                                0)),
                                5),
                        isDisplayed()));
        materialRadioButton2.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.profileUsername), withText("swapna"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4)));
        appCompatEditText4.perform(scrollTo(), replaceText("user"));

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.profileUsername), withText("user"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText5.perform(closeSoftKeyboard());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.done),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction materialTextView3 = onView(
                allOf(withId(R.id.profileHistory), withText("Your Orders"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0)));
        materialTextView3.perform(scrollTo(), click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withId(R.id.backProfile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cart_toolbar),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction materialRadioButton3 = onView(
                allOf(withId(R.id.bottom_cart), withText("My Cart"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup1),
                                        childAtPosition(
                                                withId(R.id.bottomNavBar),
                                                0)),
                                3),
                        isDisplayed()));
        materialRadioButton3.perform(click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.next_button), withText("BUY NOW"),
                        childAtPosition(
                                allOf(withId(R.id.llBottom),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                3)),
                                0),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.shipName),
                        childAtPosition(
                                allOf(withId(R.id.ll),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                1),
                        isDisplayed()));
        appCompatEditText6.perform(replaceText("pnr"), closeSoftKeyboard());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.shipPhone),
                        childAtPosition(
                                allOf(withId(R.id.ll),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("2233445566"), closeSoftKeyboard());

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.shipAddress),
                        childAtPosition(
                                allOf(withId(R.id.ll),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                1)),
                                3),
                        isDisplayed()));
        appCompatEditText8.perform(replaceText("xxxx"), closeSoftKeyboard());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.confirmOrder), withText("PLACE ORDER"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        2),
                                2),
                        isDisplayed()));
        appCompatButton5.perform(click());

        ViewInteraction cardView2 = onView(
                allOf(withId(R.id.shoes3),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.HorizontalScrollView")),
                                        0),
                                2)));
        cardView2.perform(scrollTo(), click());

        ViewInteraction appCompatImageView3 = onView(
                allOf(withId(R.id.product_back),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatImageView3.perform(click());

        ViewInteraction materialRadioButton4 = onView(
                allOf(withId(R.id.bottom_profile), withText("Profile"),
                        childAtPosition(
                                allOf(withId(R.id.radioGroup1),
                                        childAtPosition(
                                                withId(R.id.bottomNavBar),
                                                0)),
                                4),
                        isDisplayed()));
        materialRadioButton4.perform(click());

        ViewInteraction materialTextView4 = onView(
                allOf(withId(R.id.profileHistory), withText("Your Orders"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                0)));
        materialTextView4.perform(scrollTo(), click());

        ViewInteraction appCompatImageView4 = onView(
                allOf(withId(R.id.backProfile),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.cart_toolbar),
                                        0),
                                0),
                        isDisplayed()));
        appCompatImageView4.perform(click());

        ViewInteraction materialTextView5 = onView(
                allOf(withId(R.id.profileLogout), withText("Logout"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                2)));
        materialTextView5.perform(scrollTo(), click());

        ViewInteraction materialTextView6 = onView(
                allOf(withId(R.id.yesButton), withText("YES"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        2),
                                1),
                        isDisplayed()));
        materialTextView6.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
