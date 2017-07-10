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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.database.DatabaseHandler;
import com.example.myfirstapp.helpers.Convert;
import com.example.myfirstapp.objects.Room;
import com.example.myfirstapp.objects.Service;

/**
 * Screen that Handles the Room/Service Booking as well as the availability check.
 * @author Lutsch
 *
 */
public class SelectActivity extends FragmentActivity {
    // -------------------------------------------------------------------------
    //   ___ _      _   ___ _____   ___   ___
    //  / __| |    /_\ / __/ __\ \ / /_\ | _ \___
    // | (__| |__ / _ \\__ \__ \\ V / _ \|   (_-<
    //  \___|____/_/ \_\___/___/ \_/_/ \_\_|_|__/
    //
	/** Variable to Access Textfield from DialogFragment. */
	private static EditText DATE_COMING;

	/** Variable to Access Textfield from DialogFragment. */
	private static EditText DATE_LEAVING;
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Database Object to connect to the SQLite Database. */
	private DatabaseHandler datasource;

	/** Spinner with RoomTypes for Selection. */
	private Spinner roomSpinner;

	/** Spinner with Services for Selection. */
	private Spinner serviceSpinner;

	/** A List for Rooms selected for booking. */
	private ArrayList<Room> roomList;

	/** A List for Services selected for booking. */
	private ArrayList<Service> serviceList;

	/** ArrayAdapter gets the Values to add onto the list in the view. */
	private ArrayAdapter<?> objectAdapter;

	/** Intent to get Data back to. */
	private Intent source;

	/** Int to decide which view to show to. */
	private int view;
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		source = getIntent();
		view = source.getIntExtra("view", 0);
		roomList = source.getParcelableArrayListExtra("rooms");
		serviceList = source.getParcelableArrayListExtra("services");

		datasource = new DatabaseHandler(this);
		datasource.open();

		switch(view) {
	    	case 0:
	    		setContentView(R.layout.room_select);
	    		break;
	    	case 1:
	    		setContentView(R.layout.service_select);
	    		addServicesOnSpinner();
	    		break;
			default:
				setContentView(R.layout.room_select);
		}

		initializeList();

		SelectActivity.DATE_COMING = (EditText)findViewById(R.id.dateFrom);
		SelectActivity.DATE_LEAVING = (EditText)findViewById(R.id.dateTo);
	}
	/**
	 * Reads Data from GUI, checks availability for the desired time.
	 * Gives a warning when no room is available
	 * or passes the data to the next view.
	 * @param view the view where the button was clicked
	 */
	public void add(final View view) {
		// Get Entered Dates
		final String firstDate = SelectActivity.DATE_COMING.getText().toString();
		final String secondDate = SelectActivity.DATE_LEAVING.getText().toString();

		// Check if Dates are valid depending on Room or Service Booking
		if(!datesAreValid(firstDate, secondDate, this.view)) {
			final String dateProblem = "Bitte beide Daten angeben!\n" +
								 "Mindestens eine †bernachtung!\n" +
								 "Erstes darf nicht vor zweitem liegen!";
			Toast.makeText(this, dateProblem, Toast.LENGTH_LONG).show();
			return;
		}

		// Convert Dates to DB-Format
		final String dbDateFrom = Convert.toDbDate(firstDate);
		final String dbDateTo = Convert.toDbDate(secondDate);

		// Initialize Vars for Object-Creation
		final String roomType;
		final Service wantedService;

		/** * * * * * * * * * * * * * * * * * * * * * *
		 * When Room View, get Room and Add to List   *
		 * * * * * * * * * * * * * * * * * * * * * * **/
		if(this.view == 0) {
			// Get RoomType from Spinner
			roomSpinner = (Spinner)findViewById(R.id.roomSpinner);
			roomType = roomSpinner.getSelectedItem().toString();

			// Query DB for free Room
			final Room availableRoom = getAvailableRoom(roomType, dbDateFrom, dbDateTo);

			// If no Free Room is available in the Given Time of the given Type, Toast an Error
			if(availableRoom == null) {
				final String noFreeRoom = "Leider ist in diesem Zeitraum kein " + roomType + " mehr frei.";
				Toast.makeText(this, noFreeRoom, Toast.LENGTH_SHORT).show();
				return;
			}

			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
			 * Extended Availability Check here. Insert Available Room into RoomHelper with bId = 0. *
			 * If everything is fine change bId from 0 to ID of newly inserted Booking.              *
			 * If something fails just delete all entries with bId = 0. 							 *
			 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
			if(datasource.insertInRoomHelper(availableRoom)) {
				roomList.add( new Room(availableRoom.getId(), availableRoom.getType(), availableRoom.getPrice(), availableRoom.getRoomNumber(),
									   dbDateFrom, dbDateTo, Convert.inDaysToStay(dbDateFrom, dbDateTo) ) );
			}

		/** * * * * * * * * * * * * * * * * * * * * * * * * *
		 * When Service View, get Service and Add to List   *
		 * * * * * * * * * * * * * * * * * * * * * * * * * **/
		} else {
			// Read wanted Service from Spinner
			wantedService = (Service)serviceSpinner.getSelectedItem();

			// If no Service is on Spinner Toast warning and return method
			if(wantedService == null) {
				Toast.makeText(this, "Bitte erst Service anlegen!", Toast.LENGTH_SHORT).show();
				return;
			}

			// If Service Is Available Add Dates To Object And Then Object To List
			wantedService.setFrom(dbDateFrom);
			wantedService.setTo(dbDateTo);
			wantedService.setDays(Convert.inDaysToStay(dbDateFrom, dbDateTo));
			serviceList.add(wantedService);
		}

		// Notify Adapter that List has Changed
		updateList();
	}
	/**
	 * Switches back to Main View.
	 * @param view the view where the button was clicked
	 */
	public void changeBack(final View view) {
		final Intent data = new Intent();
		data.putParcelableArrayListExtra("rooms", roomList);
		data.putParcelableArrayListExtra("services", serviceList);
		setResult(Activity.RESULT_OK, data);
		finish();
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
	 * Reads all Services from the Database and adds them on the Spinner
	 * for Display in the GUI.
	 */
	public void addServicesOnSpinner() {
		serviceSpinner = (Spinner)findViewById(R.id.serviceSpinner);
		List<Service> list = datasource.getAllServices();

		final ArrayAdapter<Service> dataAdapter = new ArrayAdapter<Service>(this, android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		serviceSpinner.setAdapter(dataAdapter);
	}
	/**
	 *  Initializes the List with the selected Objects.
	 */
	public void initializeList() {
		ListView added = (ListView)findViewById(R.id.addedElements);
		if(view == 0) {
			objectAdapter = new ArrayAdapter<Room>(this, android.R.layout.simple_list_item_1, roomList);
			added.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
					datasource.deleteFromRoomHelper(roomList.get(position).getId());
					roomList.remove(position);
					updateList();
				}
			});
		} else {
			objectAdapter = new ArrayAdapter<Service>(this, android.R.layout.simple_list_item_1, serviceList);
			added.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
					serviceList.remove(position);
					updateList();
				}
			});
		}
		added.setAdapter(objectAdapter);
	}
	/**
	 * Updates the List when new Objects are chosen.
	 */
	public void updateList() {
		objectAdapter.notifyDataSetChanged();
	}
	/**
	 * Handles the onClickEvent for the Date fields.
	 * A DatePicker is shown where a Date can be chosen.
	 * @param view the view where the button was clicked.
	 */
	public void chooseDate(final View view) {
		final DialogFragment dateFragment = new DatePickerFragment();

		switch(view.getId()) {
			case R.id.dateFrom:
				dateFragment.show(getSupportFragmentManager(), "frPicker");
				break;
			case R.id.dateTo:
				dateFragment.show(getSupportFragmentManager(), "toPicker");
				break;
			default:
		}
	}
	/**
	 * Checks two dates for correct Behaving. Meaning both dates are not empty.
	 * Both Dates are not the same. (Minimum one overnight required.)
	 * The second date is not before the first one.
	 * @param firstDate the starting date
	 * @param secondDate the ending date
	 * @param type 0 for Room, 1 for Service
	 * @return True if everything is ok, otherwise false.
	 */
	private boolean datesAreValid(final String firstDate, final String secondDate, final int type) {
		final boolean retVal;

		// Check dates.
		if(firstDate.equals("") || secondDate.equals("")) {
			retVal = false;
		} else if(firstDate.equals(secondDate) && type == 0) {
			retVal = false;
		} else {
			// Calendar for comparing two dates
			final Calendar first = Calendar.getInstance();
			final Calendar second = Calendar.getInstance();

			// Convert GUI-Date to Ints (Year, Month, Day)
			final int[] dateMin = Convert.dateToInts(firstDate);
			final int[] dateMax = Convert.dateToInts(secondDate);

			// Set Start and end Date to calendar
			first.set(dateMin[2], dateMin[1], dateMin[0]);
			second.set(dateMax[2], dateMax[1], dateMax[0]);

			if(second.before(first)) {
				retVal = false;
			} else {
				retVal = true;
			}
		}

		return retVal;
	}
	/**
	 * This method is checking if there are rooms available in the desired time
	 * and of the desired type. Therefore three parameters are needed.
	 * @param roomType the type of the desired room
	 * @param from start date DB-Format
	 * @param to end date DB-Format
	 * @return The Available Room as an Object.
	 */
	public Room getAvailableRoom(final String roomType, final String from, final String to) {
		final Room retVal = datasource.checkFreeRoom(roomType, from, to);
		if(retVal != null) {
			retVal.setFrom(from);
			retVal.setTo(to);
			retVal.setDays(Convert.inDaysToStay(from, to));
		}
		return retVal;
	}
	// -------------------------------------------------------------------------
	//	 _   _           _           _  ____ _
	//	| \ | | ___  ___| |_ ___  __| |/ ___| | __ _ ___ ___
	//	|  \| |/ _ \/ __| __/ _ \/ _` | |   | |/ _` / __/ __|
	//	| |\  |  __/\__ \ ||  __/ (_| | |___| | (_| \__ \__ \
	//	|_| \_|\___||___/\__\___|\__,_|\____|_|\__,_|___/___/
	//
	/**
	 * Nested class for a DatePicker which is shown so the user can select a date.
	 * Android requires inner classes to be static therefore nested.
	 * @author Lutsch
	 *
	 */
	public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	    // -------------------------------------------------------------------------
	    //   ___  ___    _ ___ ___ _______   ___   ___
	    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
	    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
	    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
	    //
		/** String which defines which picker is chosen. Start or enddate picker. */
		private String chosenPicker;
	    // -------------------------------------------------------------------------
	    //  __  __ ___ _____ _  _  ___  ___
	    // |  \/  | __|_   _| || |/ _ \|   \ ___
	    // | |\/| | _|  | | | __ | (_) | |) (_-<
	    // |_|  |_|___| |_| |_||_|\___/|___//__/
	    //
		@Override
		public Dialog onCreateDialog(final Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			String setDate;
			chosenPicker = this.toString();
			chosenPicker = chosenPicker.substring(chosenPicker.length()-9, chosenPicker.length()-1);

			if(chosenPicker.equals("frPicker")) {
				setDate = SelectActivity.DATE_COMING.getText().toString();
			} else {
				setDate = SelectActivity.DATE_LEAVING.getText().toString();
				if(setDate.equals(""))
					setDate = SelectActivity.DATE_COMING.getText().toString();
			}

			final int year, month, day;
			if(setDate.equals("")) {
				final Calendar c = Calendar.getInstance();
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH);
				day = c.get(Calendar.DAY_OF_MONTH);
			} else {
				final int[] date = Convert.dateToInts(setDate);
				day = date[0];
				month = date[1];
				year = date[2];
			}

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		/**
		 * Defines what happens when the date was changed in the Picker.
		 * @param view the view where the picker was started from
		 * @param year the year on the picker
		 * @param month the month on the picker
		 * @param day the day on the picker
		 */
		@Override
		public void onDateSet(final DatePicker view, final int year, final  int month, final int day) {
			// Do something with the date chosen by the user
			final String date = String.format(Locale.GERMANY, "%02d.%02d.%d", day, month+1, year);
			if(chosenPicker.equals("frPicker")) {
				SelectActivity.DATE_COMING.setText(date);
			} else {
				SelectActivity.DATE_LEAVING.setText(date);
			}
		} // onDateSet Ende
	} // DatePickerFragment Ende
}