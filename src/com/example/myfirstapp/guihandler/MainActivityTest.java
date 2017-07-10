/**
 * @Datum 15.07.2014
 * @author Christian Tobias Lutsch
 *
 * _    _       _       _
 *| |  | |     | |     | |
 *| |__| | ___ | |_ ___| |
 *|  __  |/ _ \| __/ _ \ |
 *| |  | | (_) | ||  __/ |
 *|_|  |_|\___/ \__\___|_|
 *
 *
 * Entwickelt mit ADT v22.6.2-1085508, OSX 10.9, Eclipse
 */
package com.example.myfirstapp.guihandler;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

/**
 * Class to test the MainActivity. All the Changes to other Activities will be checked
 * and made sure the switch opend the right activity.
 * @author Lutsch
 *
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private Solo solo;

	public MainActivityTest() {
		super(MainActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testPreferenceIsSaved() throws Exception {
	    // check that we have the right activity
	    solo.assertCurrentActivity("wrong activity", MainActivity.class);

	    // Click a button which will start a new Activity
	    // Here we use the ID of the string to find the right button
	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.booking));

	    // Validate that the Activity is the correct one
	    solo.assertCurrentActivity("wrong activity", BookingActivity.class);
	    solo.goBack();

	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.customer));
	    solo.assertCurrentActivity("wrong activity", CreateActivity.class);
	    solo.goBack();


	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.room));
	    solo.assertCurrentActivity("wrong activity", CreateActivity.class);
	    solo.goBack();

	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.service));
	    solo.assertCurrentActivity("wrong activity", CreateActivity.class);
	    solo.goBack();

	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.showAllCustomers));
	    solo.assertCurrentActivity("wrong activity", ShowAllActivity.class);
	    solo.goBack();

	    solo.clickOnButton(solo.getString(com.example.myfirstapp.R.string.showAllBookings));
	    solo.assertCurrentActivity("wrong activity", ShowAllActivity.class);
	    solo.goBack();

	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
