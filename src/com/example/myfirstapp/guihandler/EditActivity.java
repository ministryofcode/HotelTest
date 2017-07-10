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
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.database.DatabaseHandler;

/**
 * Screen that shows all entries from a customer and allows to change or delete them.
 * @author Lutsch
 *
 */
public class EditActivity extends Activity {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Database-Object to connect to SQLite database. */
	private DatabaseHandler datasource;

	/** Text fields with information about the customer. */
	private EditText cId, title, ln, fn, str, no, plz, city, bdate;
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.edit_customer);

	    final Intent toGet = getIntent();

	    datasource = new DatabaseHandler(this);
		datasource.open();

	    // Initialize all Fields in Form
		cId = (EditText)findViewById(R.id.customerId);
		title = (EditText)findViewById(R.id.title);
		ln = (EditText)findViewById(R.id.name);
		fn = (EditText)findViewById(R.id.vorname);
		str = (EditText)findViewById(R.id.street);
		no = (EditText)findViewById(R.id.number);
		plz = (EditText)findViewById(R.id.plz);
		city = (EditText)findViewById(R.id.city);
		bdate = (EditText)findViewById(R.id.bdate);

		// Convert LONG and INT to String
		final String id = String.valueOf(toGet.getLongExtra("id", 0));
		final String noAsString = String.valueOf(toGet.getIntExtra("number", 0));

		// Insert Customer Values Into Form
		cId.setText(id);
		title.setText(toGet.getStringExtra("title"));
		ln.setText(toGet.getStringExtra("lastname"));
		fn.setText(toGet.getStringExtra("firstname"));
		str.setText(toGet.getStringExtra("street"));
		no.setText(noAsString);
		plz.setText(toGet.getStringExtra("plz"));
		city.setText(toGet.getStringExtra("city"));
		bdate.setText(toGet.getStringExtra("bdate"));
	}
	/**
	 * Reads Values from the GUI and Updates the Database.
	 * @param view
	 */
	public void updateCustomer(final View view) {
		final ContentValues values = DatabaseHandler.generateCustomerValues(title.getText().toString(),
																	  fn.getText().toString(),
																	  ln.getText().toString(),
																	  str.getText().toString(),
																	  Integer.parseInt(no.getText().toString()),
																	  plz.getText().toString(),
																	  city.getText().toString(),
																	  bdate.getText().toString());

		final boolean success = datasource.updateCustomer(values, Long.parseLong(cId.getText().toString()));

		if(success)
			Toast.makeText(this, getString(R.string.succesfulUpdate), Toast.LENGTH_SHORT).show();

		changeBack(view);
	}
	/**
	 * Reads the Customer ID from GUI checks if there is a booking related to this customer.
	 * IF NOT: Customer will be deleted from Database
	 * IF YES: A Warning will be Displayed
	 * @param view the view where the action took place
	 */
	public void deleteCustomer(final View view) {
		final long deleteId = Long.parseLong(cId.getText().toString());
		final String result;

		if(datasource.deleteCustomer(deleteId)) {
			result = "Der Kunde mit der Nummer " + deleteId + " wurde entfernt";
		} else {
			result = "Der Kunde konnte nicht entfernt werden da noch eine Buchung offen ist.";
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();

		changeBack(view);
	}
	/**
	 * Changes Back to the previous screen. ShowAllActivity.
	 * @param view the view where the button was clicked.
	 */
	public void changeBack(final View view) {
		final Intent lastScreen = new Intent(getApplicationContext(), ShowAllActivity.class);
		lastScreen.putExtra("show", "cst");
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
}
