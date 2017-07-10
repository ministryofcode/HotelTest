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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.database.DatabaseHandler;
import com.example.myfirstapp.objects.Booking;
import com.example.myfirstapp.objects.Customer;

/**
 * Activity which shows all Customers or Bookings in the Database.
 * This handler fills the respective Elements into the View.
 * @author Lutsch
 *
 */
public class ShowAllActivity extends FragmentActivity {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	/** Datasource connects to a database for Content Handling.	 */
	private DatabaseHandler datasource;

	/** ArrayAdapter gets the Values to add onto the list in the view. */
	private ArrayAdapter<?> adapter;

	/** The List filled with the Data from the Database is used to fill the View. */
	private List<Booking> bookingValues;

	/** Variable to define which data shall be shown. */
	private String show;
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_all_customer);

		datasource = new DatabaseHandler(this);
		datasource.open();

		final Intent before = getIntent();
		show = before.getStringExtra("show");

		final ListView lv = (ListView)findViewById(android.R.id.list);

		if("cst".equals(show)) {
			final List<Customer> values = datasource.getAllCustomers();
			adapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_list_item_1, values);
		} else {
			bookingValues = datasource.getAllBookings();

			adapter = new ArrayAdapter<Booking>(this, android.R.layout.simple_list_item_1, bookingValues);
			registerForContextMenu(lv);
		}

		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
				if("cst".equals(show)) {
					final Customer sel = (Customer)parent.getItemAtPosition(position);

					// Put Values of clicked Customer to Intent
					final Intent i = new Intent(getApplicationContext(), EditActivity.class);
					i.putExtra("id", sel.getId()); // LONG
					i.putExtra("title", sel.getTitle()); // STRING
					i.putExtra("firstname", sel.getFirstname()); // STRING
					i.putExtra("lastname", sel.getLastname()); // STRING
					i.putExtra("street", sel.getStreet()); // STRING
					i.putExtra("number", sel.getNumber()); // INT
					i.putExtra("plz", sel.getPlz()); // STRING
					i.putExtra("city", sel.getCity()); // STRING
					i.putExtra("bdate", sel.getBdate()); // STRING

					startActivity(i);
				} else {
					Toast.makeText(getApplicationContext(), R.string.pressLong, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	@Override
    public void onCreateContextMenu(final ContextMenu menu, final View v, final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Aktion:");
        menu.add(0, v.getId(), 0, R.string.fullDelete);
    }
	@Override
    public boolean onContextItemSelected(final MenuItem item) {
		boolean retVal;

        if(item.getTitle().equals(getString(R.string.fullDelete))) {
        	final AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        	final int position = (int)info.id;

        	final long toDelete = ((Booking)adapter.getItem(position)).get_id();

        	if(datasource.deleteBooking(toDelete)) {
        		bookingValues.remove(position);
        		adapter.notifyDataSetChanged();
        	}

        	retVal = true;
    	} else {
    		retVal = false;
		}
        return retVal;
    }
	/**
	 * Handles the Action when the back Button is clicked.
	 * Returns to the view it came from, here only Main is possible
	 * @param view The view where the button was clicked
	 */
	public void changeBack(final View view) {
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
}
