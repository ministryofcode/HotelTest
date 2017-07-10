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


public class ServiceHelper {
	// -------------------------------------------------------------------------
	//	 _   _                      _   _  ____      _
	//	| \ | | __ _ _ __ ___   ___| \ | |/ ___|___ | |_   _ _ __ ___  _ __  ___
	//	|  \| |/ _` | '_ ` _ \ / _ \  \| | |   / _ \| | | | | '_ ` _ \| '_ \/ __|
	//	| |\  | (_| | | | | | |  __/ |\  | |__| (_) | | |_| | | | | | | | | \__ \
	//	|_| \_|\__,_|_| |_| |_|\___|_| \_|\____\___/|_|\__,_|_| |_| |_|_| |_|___/
	//
	public static final String TABLE_SERVICE_HELPER = "serviceHelper";
	public static final String COLUMN_BOOKING_ID = "bId";
	public static final String COLUMN_SERVICE_ID = "sId";
	public static final String COLUMN_FROM = "serviceFrom";
	public static final String COLUMN_TO = "serviceTo";
	public static final String COLUMN_DAYS = "serviceDays";

	public static final String[] allRoomHelperColumns = { COLUMN_BOOKING_ID, COLUMN_SERVICE_ID, COLUMN_FROM, COLUMN_TO, COLUMN_DAYS };

	public static final String CREATE_SERVICE_HELPER_TABLE = "CREATE TABLE " + TABLE_SERVICE_HELPER + "("
			+ COLUMN_BOOKING_ID + " INTEGER NOT NULL, "
			+ COLUMN_SERVICE_ID + " INTEGER NOT NULL, "
			+ COLUMN_FROM + " DATE NOT NULL, "
			+ COLUMN_TO + " DATE NOT NULL, "
			+ COLUMN_DAYS + " INTEGER NOT NULL, "
			+ "FOREIGN KEY(" + COLUMN_BOOKING_ID + ") REFERENCES " + BookingTable.TABLE_BOOKING + "(" + BookingTable.COLUMN_ID + "), "
    		+ "FOREIGN KEY(" + COLUMN_SERVICE_ID + ") REFERENCES " + ServiceTable.TABLE_SERVICE + "(" + ServiceTable.COLUMN_ID + ")"
    		+ ");";
	// -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
    /**
     * Creates Table in Database with creation string.
     * @param database the database to connect
     */
	 public static void onCreate(final SQLiteDatabase database) {
		 database.execSQL(CREATE_SERVICE_HELPER_TABLE);
	 }
    /**
     * Upgrades Database. First Table will be deleted the newly created.
     * @param db the database to upgrade
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
	public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
			"Upgrading database from version " + oldVersion + " to "
			+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVICE_HELPER);
		onCreate(db);
	}
}
