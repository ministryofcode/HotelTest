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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myfirstapp.R;

/**
 * MainActivity (=Screen) of the App. Here u can navigate to the other Screens.
 * It's possible to Create: Customers, Rooms and Services.
 * or to show all Customers or Bookings in the Database.
 * @author Lutsch
 *
 */
public class MainActivity extends Activity {
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	/**
	 * Called when a button is clicked. Defined in XML File.
	 * Defines which button was clicked and loads the specific view
	 * @param view the corresponding view where the element was clicked.
	 */
	public void onClick(final View view) {
		final Intent nextScreen = new Intent(getApplicationContext(), CreateActivity.class);
		nextScreen.putExtra("cameFrom", "main");
		int createView = 0;

		switch(view.getId()) {
			case R.id.btnBooking:
				nextScreen.setClass(getApplicationContext(), BookingActivity.class);
				break;
			case R.id.btnCustomer:
				//nextScreen
				createView = 0;
				break;
			case R.id.btnRoom:
				//nextScreen = new Intent(getApplicationContext(), CreateActivity.class);
				createView = 1;
				break;
			case R.id.btnService:
				//nextScreen = new Intent(getApplicationContext(), CreateActivity.class);
				createView = 2;
				break;
			case R.id.btnShowCustomers:
				nextScreen.setClass(getApplicationContext(), ShowAllActivity.class);
				nextScreen.putExtra("show", "cst");
				break;
			case R.id.btnShowBookings:
				nextScreen.setClass(getApplicationContext(), ShowAllActivity.class);
				nextScreen.putExtra("show", "book");
				break;
			default:
		}
		nextScreen.putExtra("view", createView);
		startActivity(nextScreen);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}