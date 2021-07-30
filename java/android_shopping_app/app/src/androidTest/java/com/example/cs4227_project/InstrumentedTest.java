package com.example.cs4227_project;

import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.tabs.TabLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {

    private final static int LONG_WAIT = 1000;
    private final static int MED_WAIT = 500;
    private final static int SHORT_WAIT = 250;
    private String orderTime = "Date purchased: ";

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void verifyViewProductsUseCase() throws InterruptedException {
        Thread.sleep(LONG_WAIT);
        //go to view clothes
        checkViewProductsType(R.id.clothesButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view shoes
        checkViewProductsType(R.id.shoeButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view accessories
        checkViewProductsType(R.id.accButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.genderTab)).check(matches(isDisplayed())).perform(selectTabAtPosition(1));
        Thread.sleep(MED_WAIT);
        Thread.sleep(LONG_WAIT);
        //go to view clothes
        checkViewProductsType(R.id.clothesButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view shoes
        checkViewProductsType(R.id.shoeButton);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);
        //go to view accessories
        checkViewProductsType(R.id.accButton);
    }

    //Tests to see if order cards and order dialogs display
    @Test
    public void adapterPattern() throws InterruptedException {
        //Get text of log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //Check if logged in
        if(loginText.equals("Log Out")) {

            //Go to orders fragment
            onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
            Thread.sleep(LONG_WAIT);

            //Count items in recycler view
            RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.simpleRecyclerView);
            int originalItemCount = recyclerView.getChildCount();

            //If there are orders
            if(originalItemCount > 0) {

                //Click the order
                onView(withId(R.id.simpleRecyclerView)).perform(actionOnItemAtPosition(0,click()));
                Thread.sleep(SHORT_WAIT);

                //Check if order dialog is displayed
                onView(withId(R.id.layout)).check(matches(isDisplayed()));
                onView(withId(R.id.heading)).check(matches(withText("Order Summary")));
            //If there are no orders
            } else {
                //builderPattern to create an order and recall adapterPattern()
                builderPattern();
                adapterPattern();
            }

        //If logged out
        } else {
            //Log in and recall adapterPattern()
            logInUser();
            adapterPattern();
        }
    }

    //Test to log in regular user
    @Test
    public void logInUser() throws InterruptedException {
        //Get text of log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //If logged out
        if(loginText.equals("Log In")) {

            //Go to log in fragment and input credentials
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).perform(typeText("testuser@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.fieldPassword)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
            onView(withId(R.id.signIn)).check(matches(isDisplayed())).perform(click());
        //If logged in
        } else {
            //Press log out and call logInUser() again
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInUser();
        }
    }

    //Test to log in admin user
    @Test
    public void logInAdmin() throws InterruptedException {
        //Get text from log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //If logged out
        if(loginText.equals("Log In")) {

            //Go to log in fragment and input credentials
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldEmail)).perform(typeText("testadmin@gmail.com"), closeSoftKeyboard());
            onView(withId(R.id.fieldPassword)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.fieldPassword)).perform(typeText("password"), closeSoftKeyboard());
            onView(withId(R.id.signIn)).check(matches(isDisplayed())).perform(click());
        //If logged in
        } else {
            //Press log out and call logInAdmin() again
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInAdmin();
        }
    }

    //Test to place an order and make sure its displaying in the order's fragment
    @Test
    public void builderPattern() throws InterruptedException {
        try {
            //Get text from log in button
            Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
            String loginText = (String) b.getText();

            //If the user is logged in
            if(loginText.equals("Log Out")) {
                //Purchase item and press 'ok' on order confirmation dialog
                buyItem();
                onView(withText("Ok")).perform(click());

                //Click on most recent order
                onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
                Thread.sleep(LONG_WAIT);
                onView(withId(R.id.simpleRecyclerView)).perform(actionOnItemAtPosition(0, click()));
                Thread.sleep(LONG_WAIT);
                onView(withId(R.id.layout)).check(matches(isDisplayed()));
                onView(withId(R.id.heading)).check(matches(withText("Order Summary")));

                //Check that the information displayed matches the order time and order total variables in buyItem()
                onView(withId(R.id.orderTime)).check(matches(withSubstring((orderTime))));
                onView(isRoot()).perform(pressBack());

            //If logged out
            } else {
                //Log in and call builderPattern()
                logInUser();
                builderPattern();
            }

            //Catch if not on homepage
        } catch(NoMatchingViewException e) {
            //Click back and recall builderPattern()
            onView(isRoot()).perform(pressBack());
            Thread.sleep(SHORT_WAIT);
            builderPattern();
        }
    }

    @Test
    public void buyItem() throws InterruptedException {
        //Checks and clicks on 'Clothes'
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Clicks on first product in recycler view
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(actionOnItemAtPosition(0,click()));
        Thread.sleep(LONG_WAIT);
        TextView cost = activityRule.getActivity().findViewById(R.id.productPrice);

        //Input quantity
        onView(withId(R.id.quantity)).perform(typeText("1"), closeSoftKeyboard());

        //Add item to cart
        onView(withId(R.id.addToCart)).check(matches(isDisplayed())).perform(click());
        onView(isRoot()).perform(pressBack());

        //Go to cart fragment and click 'checkout'
        onView(withId(R.id.cartBtn)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.checkout)).check(matches(isDisplayed())).perform(click());

        //Input values into form fields
        onView(withId(R.id.townInput)).check(matches(isDisplayed())).perform(click(), typeText("Annagh"), closeSoftKeyboard());
        onView(withId(R.id.cityInput)).check(matches(isDisplayed())).perform(click(), typeText("Tralee"), closeSoftKeyboard());
        onView(withId(R.id.countyInput)).check(matches(isDisplayed())).perform(click(), typeText("Co. Kerry"), closeSoftKeyboard());
        onView(withId(R.id.cardNameInput)).check(matches(isDisplayed())).perform(click(), typeText("Shelley Howarth"), closeSoftKeyboard());
        onView(withId(R.id.cardNumInput)).check(matches(isDisplayed())).perform(click(), typeText("1234567812345678"), closeSoftKeyboard());
        onView(withId(R.id.expiryDateInput)).check(matches(isDisplayed())).perform(click(), typeText("12/2021"), closeSoftKeyboard());
        onView(withId(R.id.cvvInput)).perform(click());
        onView(withId(R.id.cvvInput)).check(matches(isDisplayed())).perform(click(), typeText("123"), closeSoftKeyboard());

        //click order button
        Date timeNow = new Date();
        onView(withId(R.id.submitButton)).check(matches(isDisplayed())).perform(click());
        SimpleDateFormat sfd = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());
        orderTime += sfd.format(timeNow);
    }

    //Test to undo an order
    @Test
    public void mementoPattern() throws InterruptedException {
        //Buy an item and click 'undo order' in confirm order dialog
        logInUser();
        buyItem();
        onView(withText("Undo Order")).perform(click());

        //Go to most recent order in orders fragment
        onView(withId(R.id.ordersBtn)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.simpleRecyclerView);
        int originalItemCount = recyclerView.getChildCount();

        //If orders exist
        if(originalItemCount > 0) {
            onView(withId(R.id.simpleRecyclerView)).perform(actionOnItemAtPosition(0,click()));
            Thread.sleep(LONG_WAIT);
            onView(withId(R.id.layout)).check(matches(isDisplayed()));
            onView(withId(R.id.heading)).check(matches(withText("Order Summary")));

            //Check that the time and total are different to the time and total in buyItem()
            onView(withId(R.id.orderTime)).check(matches(not(withSubstring((orderTime)))));
        }
    }

    //Test that log in fragment displays when logged out user reaches checkout
    @Test
    public void interceptorPattern() throws InterruptedException {
        //Get text from log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //If logged out
        if(loginText.equals("Log In")) {

            //Checks and clicks on 'Clothes'
            Thread.sleep(LONG_WAIT);
            onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
            Thread.sleep(LONG_WAIT);

            //Clicks on first product in recycler view
            onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(actionOnItemAtPosition(0, click()));
            Thread.sleep(LONG_WAIT);
            TextView cost = activityRule.getActivity().findViewById(R.id.productPrice);

            //Input quantity and add item cart
            onView(withId(R.id.quantity)).perform(typeText("1"), closeSoftKeyboard());
            onView(withId(R.id.addToCart)).check(matches(isDisplayed())).perform(click());
            onView(isRoot()).perform(pressBack());

            //Go to cart fragment and click checkout
            onView(withId(R.id.cartBtn)).check(matches(isDisplayed())).perform(click());
            onView(withId(R.id.checkout)).check(matches(isDisplayed())).perform(click());

            //Check if log in screen appears
            onView(withId(R.id.fragment_log_in)).check(matches(isDisplayed()));

        //If logged in
        } else {
            //Log out and call interceptorPattern()
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            interceptorPattern();
        }
    }

    //Test to add stock to the store and check that it displays
    @Test
    public void commandPattern() throws InterruptedException {
        //Get text from log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //If logged out, log in admin
        if(loginText.equals("Log In")) {
            logInAdmin();
        //If logged in, log out and log in admin
        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInAdmin();
        }

        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.stockBtn)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Fill in form
        onView(withId(R.id.productName)).check(matches(isDisplayed())).perform(click(), typeText("Crew Neck"), closeSoftKeyboard());
        onView(withId(R.id.price)).check(matches(isDisplayed())).perform(click(), typeText("17.50"), closeSoftKeyboard());
        onView(withId(R.id.female)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.clothes)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.quantity1)).check(matches(isDisplayed())).perform(click(), typeText("10"), closeSoftKeyboard());

        //Submit
        onView(withId(R.id.finish)).perform(scrollTo()).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Go back to clothes
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(isRoot()).perform(pressBack());
        Thread.sleep(LONG_WAIT);

        //Check if new product is displayed
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.recycler_view)).check(matches(hasItem(hasDescendant(withText("Crew Neck")))));
    }

    //Method to check for children in recycler view
    public static Matcher<View> hasItem(final Matcher<View> matcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {

            @Override public void describeTo(Description description) {
                description.appendText("has item: ");
                matcher.describeTo(description);
            }

            @Override protected boolean matchesSafely(RecyclerView view) {
                RecyclerView.Adapter adapter = view.getAdapter();
                for (int position = 0; position < adapter.getItemCount(); position++) {
                    int type = adapter.getItemViewType(position);
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, type);
                    adapter.onBindViewHolder(holder, position);
                    if (matcher.matches(holder.itemView)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    //Test filters
    @Test
    public void facadePattern() throws InterruptedException {
        //Click into clothes
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Get spinner items
        Spinner sizeSpinner = activityRule.getActivity().findViewById(R.id.sizeSpinner);
        int items = sizeSpinner.getCount();

        //Loop through all the spinner options
        for(int i = 1; i < items; i++) {
            //Clicks spinner and selects size
            onView(withId(R.id.sizeSpinner)).check(matches(isDisplayed())).perform(click());
            Thread.sleep(MED_WAIT);
            onData(anything()).atPosition(i).perform(click());

            //Clicks filter
            onView(withId(R.id.filter)).check(matches(isDisplayed())).perform(click());

            //Get items in recycler view
            RecyclerView recyclerView = activityRule.getActivity().findViewById(R.id.recycler_view);
            Thread.sleep(LONG_WAIT);
            int originalItemCount = recyclerView.getChildCount();
            String size = sizeSpinner.getSelectedItem().toString();

            //If product appears, check the size
            if(originalItemCount > 0) {
                onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(actionOnItemAtPosition(0, click()));
                onView(withId(R.id.spinner)).check(matches(isDisplayed())).perform(click());
                onData(allOf(is(instanceOf(String.class)),
                        is(size))).inRoot(isPlatformPopup()).perform(click());
                onView(isRoot()).perform(pressBack());
            }
        }
    }

    @Test
    public void testUpdateStock() throws InterruptedException {
        //Get text from log in button
        Button b = activityRule.getActivity().findViewById(R.id.logInBtn);
        String loginText = (String) b.getText();

        //If logged out, log in admin
        if(loginText.equals("Log In")) {
            logInAdmin();
            //If logged in, log out and log in admin
        } else {
            onView(withId(R.id.logInBtn)).check(matches(isDisplayed())).perform(click());
            logInAdmin();
        }

        //Checks and clicks on 'Clothes'
        Thread.sleep(LONG_WAIT);
        onView(withId(R.id.clothesButton)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        //Clicks on first product in recycler view
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed())).perform(actionOnItemAtPosition(0,click()));
        Thread.sleep(LONG_WAIT);

        onView(withId(R.id.aS)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(LONG_WAIT);

        onView(withText("UpdateStock")).check(matches(isDisplayed()));
        onView(withId(R.id.size1)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(MED_WAIT);
        onData(anything()).atPosition(0).perform(click());
        onView(withId(R.id.quantity1)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.quantity1)).check(matches(isDisplayed())).perform(click(), typeText("40"), closeSoftKeyboard());

        onView(withId(R.id.size2)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(MED_WAIT);
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.quantity2)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.quantity2)).check(matches(isDisplayed())).perform(click(), typeText("55"), closeSoftKeyboard());

        onView(withId(R.id.size3)).check(matches(isDisplayed())).perform(click());
        Thread.sleep(MED_WAIT);
        onData(anything()).atPosition(2).perform(click());
        onView(withId(R.id.quantity3)).check(matches(isDisplayed())).perform(click());
        onView(withId(R.id.quantity3)).check(matches(isDisplayed())).perform(click(), typeText("80"), closeSoftKeyboard());

        onView(withId(R.id.confirm)).check(matches(isDisplayed())).perform(click());
    }

    public void checkViewProductsType(int buttonId) throws InterruptedException {
        //go to selected view
        onView(withId(buttonId)).check(matches(isDisplayed()));
        onView(withId(buttonId)).perform(click());
        Thread.sleep(LONG_WAIT);
        //check if recycler view is displayed and populated with data
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
    }

    @NonNull
    private static ViewAction selectTabAtPosition(final int position) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return allOf(isDisplayed(), isAssignableFrom(TabLayout.class));
            }

            @Override
            public String getDescription() {
                return "with tab at index" + String.valueOf(position);
            }

            @Override
            public void perform(UiController uiController, View view) {
                if (view instanceof TabLayout) {
                    TabLayout tabLayout = (TabLayout) view;
                    TabLayout.Tab tab = tabLayout.getTabAt(position);

                    if (tab != null) {
                        tab.select();
                    }
                }
            }
        };
    }
}