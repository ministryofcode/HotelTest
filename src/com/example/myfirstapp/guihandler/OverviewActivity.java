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

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
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
 * Last Screen in the Booking Cycle showing the overview and performing the booking
 * on confirmation of the user.
 * @author Lutsch
 *
 */
public class OverviewActivity extends Activity {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Database-Object to connect to SQLite Database. */
	private DatabaseHandler datasource;

	/** Intent to get Data from the previous Screen. */
	private Intent i;

	/** Customer corresponding to Booking. */
	private Customer cst;

	/** List with Rooms corresponding to Booking. */
	private List<Room> rooms;

	/** List with Services corresponding to Booking. */
	private List<Service> services;


    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.overview_screen);

	    i = getIntent();
		cst = i.getParcelableExtra("cst");
		rooms = i.getParcelableArrayListExtra("rooms");
		services = i.getParcelableArrayListExtra("services");

	    datasource = new DatabaseHandler(this);
		datasource.open();

		// Fill Values into Components
		final EditText cstEt = (EditText)findViewById(R.id.customer);
		cstEt.setText(cst.toString());

		addDataOnSpinner();

		final EditText priceEt = (EditText)findViewById(R.id.price);
		final int total = getTotal(rooms, services);
		priceEt.setText("Gesamt: " + total + " Euro");
	}
	/**
	 * Put the Values in the Database.
	 * @param view the view where the button was clicked.
	 */
	public void finalize(final View view) {

		final ContentValues bookingValues = DatabaseHandler.generateBookingValues(cst.getId());

		final boolean success = datasource.insertBooking(bookingValues, rooms, services);

		if(success)
			Toast.makeText(this, getString(R.string.succesfulBooking), Toast.LENGTH_SHORT).show();

		changeBack();
	}
	/**
	 * Abort booking and switch back to HomeScreen. Perform a rollback.
	 * @param view the view where the button was clicked.
	 */
	public void abort(final View view) {
		final int deleted = datasource.cleanRoomHelper();
		final String rbmsg = "Rollback erfolgreich: " + deleted + " reservierte(s) Zimmer freigegeben!";
		Toast.makeText(this, rbmsg, Toast.LENGTH_SHORT).show();
		final Intent abort = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(abort);
	}
	public void changeBack() {
		final Intent main = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(main);
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
    // -------------------------------------------------------------------------
    //  _  _ ___ _    ___ ___ ___
    // | || | __| |  | _ \ __| _ \___
    // | __ | _|| |__|  _/ _||   (_-<
    // |_||_|___|____|_| |___|_|_|__/
    //
	/**
	 * Fills the Spinners with the Data on the Lists.
	 */
	public void addDataOnSpinner() {
		final Spinner roomSpinner = (Spinner)findViewById(R.id.roomSpinner);
		final Spinner serviceSpinner = (Spinner)findViewById(R.id.serviceSpinner);

		final ArrayAdapter<Room> roomAdapter = new ArrayAdapter<Room>(this, android.R.layout.simple_spinner_item, rooms);
		roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		final ArrayAdapter<Service> serviceAdapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_item, services);
		serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		roomSpinner.setAdapter(roomAdapter);
		serviceSpinner.setAdapter(serviceAdapter);
	}
	/**
	 * Calculates the total price of the Booking.
	 * @param rooms 	List with Rooms corresponding to Booking
	 * @param services	List with Services corresponding to Booking
	 * @return the Total as Integer
	 */
	private int getTotal(final List<Room> rooms, final List<Service> services) {

		int priceForRooms = 0;
		int priceForServices = 0;

		for(Room rtemp: rooms) {
			priceForRooms += rtemp.getPrice() * rtemp.getDays();
		}

		if(!services.isEmpty()) {
			for(Service stemp: services) {
				priceForServices += stemp.getPrice() * stemp.getDays();
			}
		}

		return priceForRooms + priceForServices;
	}
}
