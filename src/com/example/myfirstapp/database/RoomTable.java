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

public class RoomTable {
	// -------------------------------------------------------------------------
	//	 _   _                      _   _  ____      _
	//	| \ | | __ _ _ __ ___   ___| \ | |/ ___|___ | |_   _ _ __ ___  _ __  ___
	//	|  \| |/ _` | '_ ` _ \ / _ \  \| | |   / _ \| | | | | '_ ` _ \| '_ \/ __|
	//	| |\  | (_| | | | | | |  __/ |\  | |__| (_) | | |_| | | | | | | | | \__ \
	//	|_| \_|\__,_|_| |_| |_|\___|_| \_|\____\___/|_|\__,_|_| |_| |_|_| |_|___/
	//
	public static final String TABLE_ROOM = "room";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_TYPE = "type";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_NR = "roomnumber";

	public static final String[] allRoomColumns = { COLUMN_ID, COLUMN_TYPE, COLUMN_PRICE, COLUMN_NR };

	public static final String CREATE_ROOM_TABLE = "CREATE TABLE " + TABLE_ROOM + "("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_TYPE + " TEXT NOT NULL, "
			+ COLUMN_PRICE + " INTEGER NOT NULL, "
			+ COLUMN_NR + " INTEGER NOT NULL "
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
		 database.execSQL(CREATE_ROOM_TABLE);
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);
		onCreate(db);
	}
}
