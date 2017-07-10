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


import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookingTable {
	// -------------------------------------------------------------------------
	//	 _   _                      _   _  ____      _
	//	| \ | | __ _ _ __ ___   ___| \ | |/ ___|___ | |_   _ _ __ ___  _ __  ___
	//	|  \| |/ _` | '_ ` _ \ / _ \  \| | |   / _ \| | | | | '_ ` _ \| '_ \/ __|
	//	| |\  | (_| | | | | | |  __/ |\  | |__| (_) | | |_| | | | | | | | | \__ \
	//	|_| \_|\__,_|_| |_| |_|\___|_| \_|\____\___/|_|\__,_|_| |_| |_|_| |_|___/
	//
	public static final String TABLE_BOOKING = "booking";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CUSTOMER = "cId";
//    public static final String COLUMN_ROOM = "rId";
//    public static final String COLUMN_SERVICE = "sId";
//    public static final String COLUMN_FROM ="fromDate";
//    public static final String COLUMN_TO = "toDate";
//    public static final String COLUMN_DAYS = "days";
//    public static final String COLUMN_SERVICEDATE = "servicedate";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public static final String[] allBookingColumns = { COLUMN_ID, COLUMN_CUSTOMER, COLUMN_TIMESTAMP };

    public static final String CREATE_BOOKING_TABLE = "CREATE TABLE "
    		+ TABLE_BOOKING + "("
    		+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
    		+ COLUMN_CUSTOMER + " INTEGER NOT NULL, "
    		+ COLUMN_TIMESTAMP + " DATETIME NOT NULL DEFAULT (DATETIME()), "
    		+ "FOREIGN KEY(" + COLUMN_CUSTOMER + ") REFERENCES " + CustomerTable.TABLE_CUSTOMER + "(" + CustomerTable.COLUMN_ID + ")"
    		+ ");";
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
    /**
     * Creates Table in Database with creation string.
     * @param database the database to connect to
     */
     public static void onCreate(final SQLiteDatabase database) {
	    database.execSQL(CREATE_BOOKING_TABLE);
	  }
     /**
      * Upgrades Database. First Table will be deleted, then newly created.
      * @param db database to upgrade
      * @param oldVersion old version of the database
      * @param newVersion new version of the database
      */
	  public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
	    Log.w(MySQLiteHelper.class.getName(),
	        "Upgrading database from version " + oldVersion + " to "
	            + newVersion + ", which will destroy all old data");
	    db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKING);
	    onCreate(db);
	  }
}
