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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.database.DatabaseHandler;
import com.example.myfirstapp.objects.Customer;
import com.example.myfirstapp.objects.Room;
import com.example.myfirstapp.objects.Service;

/**
 * Screen that Handles the Booking input.
 * @author Lutsch
 *
 */
public class BookingActivity extends FragmentActivity {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Database Object to connect to the SQLite Database. */
	private DatabaseHandler datasource;

	/** Two spinners to fill with customers and roomTypes for Selection. */
	private Spinner cstSpinner;

	/** A List with all the Customers to fill the Spinner with. */
	private List<Customer> list;

	/** A List with all the Customers to fill the Spinner with. */
	private ArrayList<Room> roomList;

	/** A List with all the Customers to fill the Spinner with. */
	private ArrayList<Service> serviceList;
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booking_screen);

		roomList = new ArrayList<Room>();
		serviceList = new ArrayList<Service>();

		datasource = new DatabaseHandler(this);
		datasource.open();

		addCustomersOnSpinner();
	}
	/**
	 * Called when a button is clicked. Defined in XML File.
	 * Defines which button was clicked and loads the specific view
	 * @param view the corresponding view where the element was clicked.
	 */
	public void onClick(final View view) {
		final Intent nextScreen = new Intent(this, SelectActivity.class);
		int selectView;

		switch(view.getId()) {
			case R.id.createRoomButton:
				selectView = 0;
				break;
			case R.id.createServiceButton:
				//nextScreen
				selectView = 1;
				break;
			default:
				selectView = 0;
		}
		nextScreen.putExtra("view", selectView);
		nextScreen.putExtra("cameFrom", "booking");
		nextScreen.putParcelableArrayListExtra("rooms", roomList);
		nextScreen.putParcelableArrayListExtra("services", serviceList);

		startActivityForResult(nextScreen, selectView);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // If the request went well (OK) and the request was SELECT_ROOMS
	    if (resultCode == Activity.RESULT_OK) {
	        // Do Something with the Rooms
	    	roomList = data.getParcelableArrayListExtra("rooms");
	    	serviceList = data.getParcelableArrayListExtra("services");
			final EditText bro = (EditText)findViewById(R.id.bookedRooms);
			String broData = String.valueOf(data.getParcelableArrayListExtra("rooms").size());
			bro.setText(broData);
			final EditText bse = (EditText)findViewById(R.id.bookedServices);
			String bseData = String.valueOf(data.getParcelableArrayListExtra("services").size());
			bse.setText(bseData);
	    }
	}
	/**
	 * Reads Data from GUI, checks availability for the desired time.
	 * Gives a warning when no room is available
	 * or passes the data to the next view.
	 * @param view the view where the button was clicked
	 */
	public void next(final View view) {
		final Customer toSend = (Customer)cstSpinner.getSelectedItem();
		if(toSend == null) {
			Toast.makeText(this, "Bitte erst Kunden anlegen!", Toast.LENGTH_SHORT).show();
			return;
		}

		if(roomList.isEmpty()) {
			Toast.makeText(this, "Bitte mindestens ein Zimmer buchen!", Toast.LENGTH_SHORT).show();
			return;
		}

		final Intent next = new Intent(getApplicationContext(), OverviewActivity.class);
		next.putExtra("cst", toSend);
		next.putParcelableArrayListExtra("rooms", roomList);
		next.putParcelableArrayListExtra("services", serviceList);
		startActivity(next);
	}
	/**
	 * OnClickEvent for the Create Customer Button. Switches to the Create View.
	 * @param view the view where the dialog was opened from
	 */
	public void createDialog(final View view) {
		final Intent next = new Intent(getApplicationContext(), CreateActivity.class);
		next.putExtra("view", 0);
		next.putExtra("cameFrom", "booking");
		startActivity(next);
	}
	/**
	 * Switches back to Main View. If there are reserved ROoms a rollback is done.
	 * @param view the view where the button was clicked
	 */
	public void changeBack(final View view) {
		final int deleted = datasource.cleanRoomHelper();
		if(deleted > 0) {
			final String rbmsg = "Rollback erfolgreich: " + deleted + " reservierte(s) Zimmer freigegeben!";
			Toast.makeText(this, rbmsg, Toast.LENGTH_LONG).show();
		}
		final Intent lastScreen = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(lastScreen);
	}
	@Override
	protected void onResume() {
		datasource.open();
		super.onResume();
	}
	@Override
	protected void onPause() {
		datasource.close();
		super.onPause();
	}
	//	   _    _      _
	//	  | |  | |    | |
	//	  | |__| | ___| |_ __   ___ _ __ ___
	//	  |  __  |/ _ \ | '_ \ / _ \ '__/ __|
	//	  | |  | |  __/ | |_) |  __/ |  \__ \
	//	  |_|  |_|\___|_| .__/ \___|_|  |___/
	//	                | |
	//	                |_|
	/**
	 * Reads all Customers from the Database and adds them on the Spinner
	 * for Display in the GUI.
	 */
	public void addCustomersOnSpinner() {
		cstSpinner = (Spinner)findViewById(R.id.customerSpinner);
		list = datasource.getAllCustomers();

		final ArrayAdapter<Customer> dataAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cstSpinner.setAdapter(dataAdapter);
	}
}
