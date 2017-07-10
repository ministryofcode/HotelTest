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
package com.example.myfirstapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * A Helper Object to help create and connect to a SQLite Database.
 * @author Lutsch
 *
 */
public class MySQLiteHelper extends SQLiteOpenHelper {
    // -------------------------------------------------------------------------
    //   ___ _      _   ___ _____   ___   ___
    //  / __| |    /_\ / __/ __\ \ / /_\ | _ \___
    // | (__| |__ / _ \\__ \__ \\ V / _ \|   (_-<
    //  \___|____/_/ \_\___/___/ \_/_/ \_\_|_|__/
    //
	/** The Filename of the Database. */
	private static final String DATABASE_NAME = "hotel.db";

	/** The Version of the Database. */
	private static final int DATABASE_VERSION = 2;
    // -------------------------------------------------------------------------
    //   ___ _____ ___  ___
    //  / __|_   _/ _ \| _ \___
    // | (__  | || (_) |   (_-<
    //  \___| |_| \___/|_|_|__/
    //
	/**
	 * Constructor for the Helper-Object.
	 * @param context the ApplicationContext.
	 */
	public MySQLiteHelper(final Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	@Override
	public void onCreate(final SQLiteDatabase database) {
	    CustomerTable.onCreate(database);
	    RoomHelper.onCreate(database);
	    RoomTable.onCreate(database);
	    ServiceHelper.onCreate(database);
	    ServiceTable.onCreate(database);
	    BookingTable.onCreate(database);
	}
	@Override
	public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		CustomerTable.onUpgrade(db, oldVersion, newVersion);
		RoomHelper.onUpgrade(db, oldVersion, newVersion);
		RoomTable.onUpgrade(db, oldVersion, newVersion);
		ServiceHelper.onUpgrade(db, oldVersion, newVersion);
		ServiceTable.onUpgrade(db, oldVersion, newVersion);
		BookingTable.onUpgrade(db, oldVersion, newVersion);
	}
}
