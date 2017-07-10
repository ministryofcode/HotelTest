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
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.database.DatabaseHandler;
import com.example.myfirstapp.helpers.Convert;

/**
 * Screen that shows a Creation Dialog for Customers, Rooms or Services.
 * @author Lutsch
 *
 */
public class CreateActivity extends Activity {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Database Object to connect to the SQLite Database. */
	private DatabaseHandler datasource;

	/** The title spinner to Select the title of the customer. */
	private Spinner titleSpinner;

	/** Variable that declares from which Screen this screen was called. */
	private String cf;
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    final Intent i = getIntent();
	    cf = i.getStringExtra("cameFrom");

	    switch(i.getIntExtra("view", 0)) {
	    	case 0:
	    		setContentView(R.layout.create_customer);
	    		break;
	    	case 1:
	    		setContentView(R.layout.create_room);
	    		break;
	    	case 2:
	    		setContentView(R.layout.create_service);
	    		break;
    		default:
    			setContentView(R.layout.create_customer);
	    }

	    datasource = new DatabaseHandler(this);
		datasource.open();

	}
	/**
	 * Determines which Button was clicked and loads the corresponding Method
	 * for putting either Customer, Room or Service into the Database.
	 * @param view the view where the button was clicked.
	 */
	public void input(final View view) {
		switch(view.getId()) {
			case R.id.btnInputCustomer:
				inputCustomer(view);
				break;
			case R.id.btnInputRoom:
				inputRoom(view);
				break;
			case R.id.btnInputService:
				inputService(view);
				break;
			default:
		}
		//changeBack(view);
	}
	/**
	 * Reads the Values for a customer from the GUI and saves them into the Database.
	 * Clears the Entry-Fields after successful Insertion.
	 * @param view the view where the action took place
	 */
	public void inputCustomer(final View view) {
		final String[] allFields = new String[8];
		titleSpinner = (Spinner)findViewById(R.id.titleSpinner);
		allFields[0] = titleSpinner.getSelectedItem().toString();

		final EditText fnText = (EditText)findViewById(R.id.vorname);
		allFields[1] = fnText.getText().toString();

		final EditText lnText = (EditText)findViewById(R.id.name);
		allFields[2] = lnText.getText().toString();

		final EditText strText = (EditText)findViewById(R.id.street);
		allFields[3] = strText.getText().toString();

		final EditText nrText = (EditText)findViewById(R.id.number);
		allFields[4] = nrText.getText().toString();

		final EditText plzText = (EditText)findViewById(R.id.plz);
		allFields[5] = plzText.getText().toString();

		final EditText cityText = (EditText)findViewById(R.id.city);
		allFields[6] = cityText.getText().toString();

		final EditText bdateText = (EditText)findViewById(R.id.bdate);
		allFields[7] = bdateText.getText().toString();

		String valuesOk = Convert.checkEnteredValues(allFields);

		if(!valuesOk.equals("erfolg")) {
			Toast.makeText(this, valuesOk, Toast.LENGTH_SHORT).show();
			return;
		}

		final ContentValues valuesToInsert = DatabaseHandler.generateCustomerValues(allFields[0], allFields[1], allFields[2],
								  											  allFields[3], Integer.parseInt(allFields[4]),
								  											  allFields[5], allFields[6], allFields[7]);

		final boolean success = datasource.insertCustomer(valuesToInsert);

		if(success) {
			Toast.makeText(this, getString(R.string.succesfulInsert), Toast.LENGTH_SHORT).show();
			fnText.setText(""); lnText.setText(""); strText.setText(""); nrText.setText("");
			plzText.setText(""); cityText.setText(""); bdateText.setText(""); fnText.requestFocus();
		}
	}
	/**
	 * Reads the Values for a Room from the GUi and inserts them into the Database.
	 * Clears the Entry-Fields after successful Insertion.
	 * @param view the view where the action took place
	 */
	public void inputRoom(final View view) {
		final String[] allFields = new String[3];
		titleSpinner = (Spinner)findViewById(R.id.roomTypeSpinner);
		allFields[0] = titleSpinner.getSelectedItem().toString();

		final EditText roomNo = (EditText)findViewById(R.id.roomNumber);
		allFields[1] = roomNo.getText().toString();

		final EditText roomPr = (EditText)findViewById(R.id.roomPrice);
		allFields[2] = roomPr.getText().toString();

		for(String field: allFields) {
			if(field.trim().equals("")) {
				Toast.makeText(this, "Bitte alle Felder ausfŸllen", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		final boolean success = datasource.insertRoom(allFields[0], Integer.parseInt(allFields[2]), Integer.parseInt(allFields[1]));

		if(success) {
			Toast.makeText(this, getString(R.string.succesfulInsert), Toast.LENGTH_SHORT).show();
			roomNo.setText(""); roomPr.setText("");
		}
	}
	/**
	 * Reads the Values for a Service from the GUI and inserts them into the Database.
	 * Clears the Entry-Fields after successful Insertion.
	 * @param view the view where the action took place.
	 */
	public void inputService(final View view) {
		final String[] allFields = new String[2];

		final EditText serviceType = (EditText)findViewById(R.id.serviceType);
		allFields[0] = serviceType.getText().toString();

		final EditText servicePrice = (EditText)findViewById(R.id.servicePrice);
		allFields[1] = servicePrice.getText().toString();

		for(String field: allFields) {
			if(field.trim().equals("")) {
				Toast.makeText(this, "Bitte alle Felder ausfŸllen", Toast.LENGTH_SHORT).show();
				return;
			}
		}

		final boolean success = datasource.insertService(allFields[0], Integer.parseInt(allFields[1]));

		if(success) {
			Toast.makeText(this, getString(R.string.succesfulInsert), Toast.LENGTH_SHORT).show();
			serviceType.setText(""); servicePrice.setText("");
		}
	}
	/**
	 * Switches back to the previous view, after determining which one it was.
	 * In this case two are possible: Booking or Main.
	 * @param view the view where the button was clicked
	 */
	public void changeBack(final View view) {
		final Intent next = new Intent();

		if(cf.equals("booking"))
			next.setClass(getApplicationContext(), BookingActivity.class);
		else
			next.setClass(getApplicationContext(), MainActivity.class);

		startActivity(next);
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
}
