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

public class CustomerTable {
	// -------------------------------------------------------------------------
	//	 _   _                      _   _  ____      _
	//	| \ | | __ _ _ __ ___   ___| \ | |/ ___|___ | |_   _ _ __ ___  _ __  ___
	//	|  \| |/ _` | '_ ` _ \ / _ \  \| | |   / _ \| | | | | '_ ` _ \| '_ \/ __|
	//	| |\  | (_| | | | | | |  __/ |\  | |__| (_) | | |_| | | | | | | | | \__ \
	//	|_| \_|\__,_|_| |_| |_|\___|_| \_|\____\___/|_|\__,_|_| |_| |_|_| |_|___/
	//
	public static final String TABLE_CUSTOMER = "customer";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_FIRSTNAME = "firstname";
    public static final String COLUMN_LASTNAME = "lastname";
    public static final String COLUMN_STREET ="street";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_PLZ = "plz";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_BDATE = "bdate";

    public static final String[] ALL_CUSTOMER_COLUMNS = { COLUMN_ID, COLUMN_TITLE, COLUMN_FIRSTNAME,
    						   							COLUMN_LASTNAME, COLUMN_STREET, COLUMN_NUMBER,
    						   							COLUMN_PLZ, COLUMN_CITY, COLUMN_BDATE };

    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE "
						    		+ TABLE_CUSTOMER + "("
						    		+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
						    		+ COLUMN_TITLE + " TEXT NULL, "
						    		+ COLUMN_FIRSTNAME + " TEXT NOT NULL, "
						    		+ COLUMN_LASTNAME + " TEXT NOT NULL, "
						    		+ COLUMN_STREET + " TEXT NULL, "
						    		+ COLUMN_NUMBER + " INTEGER NULL, "
						    		+ COLUMN_PLZ + " TEXT NULL, "
						    		+ COLUMN_CITY + " TEXT NULL, "
						    		+ COLUMN_BDATE + " DATE NULL);";
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
    /**
     * Creates Table in Database with creation string.
     * @param database the databse to connect to
     */
    public static void onCreate(final SQLiteDatabase database) {
    	database.execSQL(CREATE_CUSTOMER_TABLE);
	}
    /**
     * Upgrades Database. First Table will be deleted, then newly created.
     * @param db database to upgrade
     * @param oldVersion old version of the database
     * @param newVersion new version of the database
     */
	public static void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
		Log.w(MySQLiteHelper.class.getName(), "Upgrading database from version " + oldVersion + " to "+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
		onCreate(db);
	}
}
