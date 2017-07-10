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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myfirstapp.helpers.Convert;
import com.example.myfirstapp.objects.Booking;
import com.example.myfirstapp.objects.Customer;
import com.example.myfirstapp.objects.Room;
import com.example.myfirstapp.objects.Service;

/**
 * Class which handles all the Database functions.
 * @author Lutsch
 *
 */
public class DatabaseHandler {
	//	    ____  _     _           _
	//	   / __ \| |   (_)         | |
	//	  | |  | | |__  _  ___  ___| |___   ____ _ _ __ ___
	//	  | |  | | '_ \| |/ _ \/ __| __\ \ / / _` | '__/ __|
	//	  | |__| | |_) | |  __/ (__| |_ \ V / (_| | |  \__ \
	//	   \____/|_.__/| |\___|\___|\__| \_/ \__,_|_|  |___/
	//	              _/ |
	//	             |__/
	/** The Database-Object itself which is used to connect to the SQLite Database. */
	private SQLiteDatabase database;

	/** The Helper which is used to create the Database and Tables. */
	private MySQLiteHelper dbHelper;
	//	    _____ _______ ____  _____
	//	   / ____|__   __/ __ \|  __ \
	//	  | |       | | | |  | | |__) |___
	//	  | |       | | | |  | |  _  // __|
	//	  | |____   | | | |__| | | \ \\__ \
	//	   \_____|  |_|  \____/|_|  \_\___/
	/**
	 * Constructor for a new Database Object.
	 * @param context The Application context.
	 */
	public DatabaseHandler(final Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	//	   __  __      _   _               _
	//	  |  \/  |    | | | |             | |
	//	  | \  / | ___| |_| |__   ___   __| |___
	//	  | |\/| |/ _ \ __| '_ \ / _ \ / _` / __|
	//	  | |  | |  __/ |_| | | | (_) | (_| \__ \
	//	  |_|  |_|\___|\__|_| |_|\___/ \__,_|___/
	/**
	 * Opens a Connection to a SQLiteDatabase.
	 */
	public void open() {
		database = dbHelper.getWritableDatabase();
	}
	/**
	 * Closes the Connection.
	 */
	public void close() {
		dbHelper.close();
	}
	//	   _____                     _
	//	  |_   _|                   | |
	//	    | |  _ __  ___  ___ _ __| |_ ___
	//	    | | | '_ \/ __|/ _ \ '__| __/ __|
	//	   _| |_| | | \__ \  __/ |  | |_\__ \
	//	  |_____|_| |_|___/\___|_|   \__|___/
	/**
	 * Inserts values into the Customer Table. Values are key value pairs.
	 * Keys are the Column Names, Values the corresponding values.
	 * @param values
	 * @return true for successful insert, false for fail.
	 */
	public boolean insertCustomer(final ContentValues values) {
		final boolean success;

		final long insertId = database.insert(CustomerTable.TABLE_CUSTOMER, null, values);

		if(insertId != -1)
			success = true;
		else
			success = false;

		return success;
	}
	/**
	 * Inserts a Room into the Database, Values roomType, price and a specific roomNumber are required.
	 * @param type the type of the room to insert
	 * @param price the price of the room to insert
	 * @param number the number of the room to insert
	 * @return true for successful insert, false for fail.
	 */
	public boolean insertRoom(final String type, final int price, final int number) {
		final ContentValues values = new ContentValues();
		final boolean success;

		values.put(RoomTable.COLUMN_TYPE, type);
		values.put(RoomTable.COLUMN_PRICE, price);
		values.put(RoomTable.COLUMN_NR, number);

		final long insertId = database.insert(RoomTable.TABLE_ROOM, null, values);
		if(insertId != -1)
			success = true;
		else
			success = false;
		return success;
	}
	/**
	 * Inserts a Room into the RoomHelper-Table to reserve it.
	 * @param room the Room to insert as Object
	 * @return true for successful insert, false for fail.
	 */
	public boolean insertInRoomHelper(final Room room) {
		final ContentValues values = new ContentValues();
		final boolean success;

		values.put(RoomHelper.COLUMN_BOOKING_ID, 0);
		values.put(RoomHelper.COLUMN_ROOM_ID, room.getId());
		values.put(RoomHelper.COLUMN_FROM, room.getFrom());
		values.put(RoomHelper.COLUMN_TO, room.getTo());
		values.put(RoomHelper.COLUMN_DAYS, room.getDays());

		final long insertId = database.insert(RoomHelper.TABLE_ROOM_HELPER, null, values);
		if(insertId != -1)
			success = true;
		else
			success = false;
		return success;
	}
	/**
	 * Inserts a Service into the database, Values of serviceType and price are required.
	 * @param type the type of the service
	 * @param price the price of the service
	 * @return true for successful insert, false for fail.
	 */
	public boolean insertService(final String type, final int price) {
		final ContentValues values = new ContentValues();
		final boolean success;

		values.put(ServiceTable.COLUMN_TYPE, type);
		values.put(ServiceTable.COLUMN_PRICE, price);

		final long insertId = database.insert(ServiceTable.TABLE_SERVICE, null, values);
		if(insertId != -1)
			success = true;
		else
			success = false;
		return success;
	}
	/**
	 * Inserts a new Booking into the Database. Values as key value pair are required.
	 * @param bookingVals Corresponding Booking Values like Customer ID.
	 * @param rooms a list with the rooms belonging to the specific booking
	 * @param services a list with services belonging to the specific booking
	 * @return true for successful insert, false for fail.
	 */
	public boolean insertBooking(final ContentValues bookingVals, final List<Room> rooms, final List<Service> services) {
		boolean success = false;
		boolean failure = false;
		final long insertId;
		int roomInsert;
		long serviceInsert = 0;
		final ContentValues roomVals = new ContentValues();
		final ContentValues serviceVals = new ContentValues();

		insertId = database.insert(BookingTable.TABLE_BOOKING, null, bookingVals);

		if(insertId != -1) {
			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
			 * Rooms are already in RoomHelper with bId = 0, change that to the new Booking-ID.  *
			 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
			roomVals.put(RoomHelper.COLUMN_BOOKING_ID, insertId);
			roomInsert = database.update(RoomHelper.TABLE_ROOM_HELPER, roomVals, RoomHelper.COLUMN_BOOKING_ID + "=0", null);

			if(rooms.size() != roomInsert)
				failure = true;

			if(!services.isEmpty()) {
				for(Service temp: services) {
					serviceVals.clear();
					serviceVals.put(ServiceHelper.COLUMN_BOOKING_ID, insertId);
					serviceVals.put(ServiceHelper.COLUMN_SERVICE_ID, temp.getId());
					serviceVals.put(ServiceHelper.COLUMN_FROM, temp.getFrom());
					serviceVals.put(ServiceHelper.COLUMN_TO, temp.getTo());
					serviceVals.put(ServiceHelper.COLUMN_DAYS, temp.getDays());

					serviceInsert = database.insert(ServiceHelper.TABLE_SERVICE_HELPER, null, serviceVals);

					if(serviceInsert == -1) {
						failure = true;
						break;
					}
				}
			}

			if(failure) {
				// Delete all entered Data with given Booking Number.
				database.delete(BookingTable.TABLE_BOOKING, BookingTable.COLUMN_ID + "=" + insertId, null);
				deleteFromRoomHelper(insertId);
				deleteFromRoomHelper(0);
				database.delete(ServiceHelper.TABLE_SERVICE_HELPER, ServiceHelper.COLUMN_BOOKING_ID + "=" + insertId, null);
			} else {
				success = true;
			}
		}

		return success;
	}
	//	   _    _           _       _
	//	  | |  | |         | |     | |
	//	  | |  | |_ __   __| | __ _| |_ ___  ___
	//	  | |  | | '_ \ / _` |/ _` | __/ _ \/ __|
	//	  | |__| | |_) | (_| | (_| | ||  __/\__ \
	//	   \____/| .__/ \__,_|\__,_|\__\___||___/
	//	         | |
	//	         |_|
	/**
	 * Updates the Customer table. The values as a key value pair and the id of the entry
	 * to be updated are required.
	 * @param values the values to insert in the database
	 * @param id the id of the customer to update
	 * @return true for successful insert, false for fail.
	 */
	public boolean updateCustomer(final ContentValues values, final long id) {
		final boolean success;
		final String whereClause = "_id=" + id;

		final long updateId = database.update(CustomerTable.TABLE_CUSTOMER, values, whereClause, null);

		if(updateId != -1)
			success = true;
		else
			success = false;

		return success;
	}
	//	   _____       _      _
	//	  |  __ \     | |    | |
	//	  | |  | | ___| | ___| |_ ___  ___
	//	  | |  | |/ _ \ |/ _ \ __/ _ \/ __|
	//	  | |__| |  __/ |  __/ ||  __/\__ \
	//	  |_____/ \___|_|\___|\__\___||___/
	/**
	 * Deletes a Booking from the Booking Table. Requires the id of the booking to delete.
	 * Deletes also the corresponding entries from the Room- and ServiceHelper Tables.
	 * @param id the id of the Booking to delete
	 * @return True after deletion.
	 */
	public boolean deleteBooking(final long id) {
		database.delete(BookingTable.TABLE_BOOKING, BookingTable.COLUMN_ID + "=" + id, null);
		database.delete(RoomHelper.TABLE_ROOM_HELPER, RoomHelper.COLUMN_BOOKING_ID + "=" + id, null);
		database.delete(ServiceHelper.TABLE_SERVICE_HELPER, ServiceHelper.COLUMN_BOOKING_ID + "=" + id, null);
		return true;
	}
	/**
	 * Deletes a customer if there is no booking associated with him.
	 * @param id The id of the customer to delete is required.
	 * @return true for successful insert, false for fail.
	 */
	public boolean deleteCustomer(final long id) {
		boolean retVal = false;

		final String whereClause = BookingTable.COLUMN_CUSTOMER + " = ?";
		final String[] whereArgs = { String.valueOf(id) };
		final Cursor cursor = database.query(BookingTable.TABLE_BOOKING, BookingTable.allBookingColumns, whereClause, whereArgs, null, null, null);

		if(cursor.getCount() == 0) {
			database.delete(CustomerTable.TABLE_CUSTOMER, CustomerTable.COLUMN_ID + "=" + id, null);
			retVal = true;
		}

		return retVal;
	}
	/**
	 * Deletes Rooms from the RoomHelper with bId = 0. This is a rollback function if the booking is not committed.
	 * @return number of rows affected
	 */
	public int cleanRoomHelper() {
		return database.delete(RoomHelper.TABLE_ROOM_HELPER, RoomHelper.COLUMN_BOOKING_ID + "= 0", null);
	}
	/**
	 * Deletes a Room from the RoomHelper. Requires the rId to delete. Deletes only reserved rooms where bId = 0.
	 * @param id the id of the room to delete
	 * @return number of rows affected
	 */
	public int deleteFromRoomHelper(final long rId) {
		final String whereClause = RoomHelper.COLUMN_ROOM_ID + " = ? AND " + RoomHelper.COLUMN_BOOKING_ID + " = 0";
		final String[] whereArgs = { String.valueOf(rId) };
		return database.delete(RoomHelper.TABLE_ROOM_HELPER, whereClause, whereArgs);
	}
	//	   _____      _
	//	  |  __ \    | |
	//	  | |__) |___| |_ _   _ _ __ _ __  ___
	//	  |  _  // _ \ __| | | | '__| '_ \/ __|
	//	  | | \ \  __/ |_| |_| | |  | | | \__ \
	//	  |_|  \_\___|\__|\__,_|_|  |_| |_|___/
	/**
	 * Reads all Bookings from the Database and returns them as an ArrayList.
	 * @return All Bookings as an ArrayList with Booking Objects.
	 * SELECT * FROM `Buchung` JOIN Zimmerhelfer ON Buchung.idBuchung = Zimmerhelfer.BuchungsId JOIN Hotelzimmer ON Hotelzimmer.idHotelzimmer =  Zimmerhelfer.ZimmerID;
	 * SELECT * FROM `Buchung` JOIN Dienstleistungshelfer ON Buchung.idBuchung = Dienstleistungshelfer.BuchungsId JOIN Dienstleistung ON Dienstleistung.idDienstleistung = Dienstleistungshelfer.DienstleistungsId;
	 */
	public List<Booking> getAllBookings() {
		final List<Booking> bookings = new ArrayList<Booking>();

		final String getBookingWithRoom = "SELECT "
											+ BookingTable.TABLE_BOOKING 	+ "." + BookingTable.COLUMN_ID + ", "			//  0
											+ BookingTable.TABLE_BOOKING 	+ "." + BookingTable.COLUMN_TIMESTAMP + ", "	//  1
											+ CustomerTable.TABLE_CUSTOMER 	+ "." + CustomerTable.COLUMN_FIRSTNAME + ", "	//  2
											+ CustomerTable.TABLE_CUSTOMER 	+ "." + CustomerTable.COLUMN_LASTNAME + ", "	//  3
											+ RoomTable.TABLE_ROOM 			+ "." + RoomTable.COLUMN_ID + ", "				//  4
											+ RoomTable.TABLE_ROOM 			+ "." + RoomTable.COLUMN_TYPE + ", "			//  5
											+ RoomTable.TABLE_ROOM 			+ "." + RoomTable.COLUMN_PRICE + ", "			//  6
											+ RoomTable.TABLE_ROOM 			+ "." + RoomTable.COLUMN_NR + ", "				//  7
											+ RoomHelper.TABLE_ROOM_HELPER 	+ "." + RoomHelper.COLUMN_FROM + ", "			//  8
											+ RoomHelper.TABLE_ROOM_HELPER 	+ "." + RoomHelper.COLUMN_TO + ", "				//  9
											+ RoomHelper.TABLE_ROOM_HELPER 	+ "." + RoomHelper.COLUMN_DAYS					// 10
											+ " FROM " 	+ BookingTable.TABLE_BOOKING + ", "
											+ 			  CustomerTable.TABLE_CUSTOMER
											+ " JOIN " 	+ RoomHelper.TABLE_ROOM_HELPER
											+ " ON "   	+ BookingTable.TABLE_BOOKING   + "." + BookingTable.COLUMN_ID
											+ " = "    	+ RoomHelper.TABLE_ROOM_HELPER + "." + RoomHelper.COLUMN_BOOKING_ID
											+ " JOIN " 	+ RoomTable.TABLE_ROOM
											+ " ON "   	+ RoomTable.TABLE_ROOM         + "." + RoomTable.COLUMN_ID
											+ " = "    	+ RoomHelper.TABLE_ROOM_HELPER + "." + RoomHelper.COLUMN_ROOM_ID
											+ " WHERE " + BookingTable.TABLE_BOOKING   + "." + BookingTable.COLUMN_CUSTOMER
											+ " = "    	+ CustomerTable.TABLE_CUSTOMER + "." + CustomerTable.COLUMN_ID;

//		final String getBookingWithService = "SELECT "
//											+ BookingTable.TABLE_BOOKING 	+ "." + BookingTable.COLUMN_ID + ", "
//											+ BookingTable.TABLE_BOOKING 	+ "." + BookingTable.COLUMN_TIMESTAMP + ", "
//											+ CustomerTable.TABLE_CUSTOMER 	+ "." + CustomerTable.COLUMN_FIRSTNAME + ", "
//											+ CustomerTable.TABLE_CUSTOMER 	+ "." + CustomerTable.COLUMN_LASTNAME + ", "
//											+ ServiceTable.TABLE_SERVICE 			+ "." + ServiceTable.COLUMN_TYPE + ", "
//											+ ServiceTable.TABLE_SERVICE 			+ "." + ServiceTable.COLUMN_PRICE + ", "
//											+ ServiceHelper.TABLE_SERVICE_HELPER 	+ "." + ServiceHelper.COLUMN_FROM + ", "
//											+ ServiceHelper.TABLE_SERVICE_HELPER 	+ "." + ServiceHelper.COLUMN_TO + ", "
//											+ ServiceHelper.TABLE_SERVICE_HELPER 	+ "." + ServiceHelper.COLUMN_DAYS
//											+ " FROM " 	+ BookingTable.TABLE_BOOKING + ", "
//											+ 			 CustomerTable.TABLE_CUSTOMER
//											+ " JOIN " 	+ ServiceHelper.TABLE_SERVICE_HELPER
//											+ " ON "   	+ BookingTable.TABLE_BOOKING   + "." + BookingTable.COLUMN_ID
//											+ " = "    	+ ServiceHelper.TABLE_SERVICE_HELPER + "." + ServiceHelper.COLUMN_BOOKING_ID
//											+ " JOIN " 	+ ServiceTable.TABLE_SERVICE
//											+ " ON "   	+ ServiceTable.TABLE_SERVICE   + "." + ServiceTable.COLUMN_ID
//											+ " = "    	+ ServiceHelper.TABLE_SERVICE_HELPER + "." + ServiceHelper.COLUMN_SERVICE_ID
//											+ " WHERE " + BookingTable.TABLE_BOOKING   + "." + BookingTable.COLUMN_CUSTOMER
//											+ " = "    	+ CustomerTable.TABLE_CUSTOMER + "." + CustomerTable.COLUMN_ID;

		final Cursor roomCursor = database.rawQuery(getBookingWithRoom, null);
		// final Cursor serviceCursor = database.rawQuery(getBookingWithRoom, null);

		long bId = 0;
		int loc = -1;
		String ts = "";
		String tsconv = "";
		String fn = "";
		String ln = "";
		final List<Room> readRooms = new ArrayList<Room>();
		final List<Service> readServices = new ArrayList<Service>();

		roomCursor.moveToFirst();
		// serviceCursor.moveToFirst();
		while(!roomCursor.isAfterLast()) {
			if(bId != roomCursor.getLong(0)) {
				loc++;
				readRooms.clear();
				bId = roomCursor.getLong(0);
				ts = roomCursor.getString(1);
				tsconv = Convert.toGuiDate(ts.substring(0, 10)) + " um " + ts.substring(11);
				fn = roomCursor.getString(2);
				ln = roomCursor.getString(3);
				readRooms.add( new Room( roomCursor.getLong(4), roomCursor.getString(5),
										 roomCursor.getInt(6), roomCursor.getInt(7),
										 roomCursor.getString(8), roomCursor.getString(9),
										 roomCursor.getInt(10) ) );
				bookings.add( new Booking(bId, tsconv, fn, ln, new ArrayList<Room>(readRooms), readServices) );

			} else {

				readRooms.add( new Room( roomCursor.getLong(4), roomCursor.getString(5),
										 roomCursor.getInt(6), roomCursor.getInt(7),
										 roomCursor.getString(8), roomCursor.getString(9),
										 roomCursor.getInt(10) ) );
				bookings.set(loc, new Booking(bId, tsconv, fn, ln, new ArrayList<Room>(readRooms), readServices) );

			}

			roomCursor.moveToNext();
		}

		return bookings;
	}
	/**
	 * Reads all customers from the database and returns them as an ArrayList.
	 * @return An ArrayList with Customer Objects.
	 */
	public List<Customer> getAllCustomers() {
		final List<Customer> customers = new ArrayList<Customer>();

	    final Cursor cursor = database.query(CustomerTable.TABLE_CUSTOMER, CustomerTable.ALL_CUSTOMER_COLUMNS,
	    		null, null, null, null, null);

	    cursor.moveToLast();
	    while (!cursor.isBeforeFirst())
	    {
		      final Customer customer = cursorToCustomer(cursor);
		      customers.add(customer);
		      cursor.moveToPrevious();
	    }

	    cursor.close();
	    return customers;
	}
	/**
	 * Reads all Services from Database and returns them as an ArrayList.
	 * @return An ArrayList with Service Objects.
	 */
	public List<Service> getAllServices() {
		final List<Service> services = new ArrayList<Service>();

		final Cursor cursor = database.query(ServiceTable.TABLE_SERVICE, ServiceTable.allServiceColumns, null, null, null, null, null);

		cursor.moveToFirst();
		cursor.moveToNext();
	    while (!cursor.isAfterLast())
	    {
	    	final Service service = cursorToService(cursor);
		    services.add(service);
		    cursor.moveToNext();
	    }

	    cursor.close();
		return services;
	}
    //	                     _       _           _
	//	    /\            (_) |     | |   (_) (_) |
	//	   /  \__   ____ _ _| | __ _| |__  _| |_| |_ _   _
	//	  / /\ \ \ / / _` | | |/ _` | '_ \| | | | __| | | |
	//	 / ____ \ V / (_| | | | (_| | |_) | | | | |_| |_| |
	//	/_/    \_\_/ \__,_|_|_|\__,_|_.__/|_|_|_|\__|\__, |
	//                              				  __/ |
	//                              				 |___/
	/**
	 * Checks for free rooms within an specified time and a specified type.
	 * Required fields are the Type of the room, a start date (from) and an end date (to)
	 * @param roomType the type of the room
	 * @param from the first date the room has to be available from
	 * @param to the last date the room has to be available to
	 * @return The free room as Object or null if there is no free room within the specified time.
	 */
	public Room checkFreeRoom(final String roomType, final String from, final String to) {
		final Room retVal;

		final Room unusedRoom = checkUnusedRoom(roomType);

		if(unusedRoom != null) {
			retVal = unusedRoom;
			return retVal;
		}

		// SELECT * FROM roomHelper JOIN room ON roomHelper.rId = room._id WHERE rId NOT IN
		// (SELECT rId FROM roomHelper WHERE '2014-06-10' > roomHelper.roomFrom AND '2014-06-09' < roomHelper.roomTo)
		// AND room.type = 'Doppelzimmer';
		final String freeRoomQuery = "SELECT * FROM " + RoomHelper.TABLE_ROOM_HELPER
								   + " JOIN " + RoomTable.TABLE_ROOM
								   + " ON " + RoomHelper.TABLE_ROOM_HELPER + "." + RoomHelper.COLUMN_ROOM_ID
								   + "=" + RoomTable.TABLE_ROOM + "." + RoomTable.COLUMN_ID
								   + " WHERE " + RoomHelper.COLUMN_ROOM_ID
								   + " NOT IN (SELECT " + RoomHelper.COLUMN_ROOM_ID + " FROM " + RoomHelper.TABLE_ROOM_HELPER
								   + " WHERE '" + to + "' > " + RoomHelper.TABLE_ROOM_HELPER + "." + RoomHelper.COLUMN_FROM
								   + " AND '" + from + "' < " + RoomHelper.TABLE_ROOM_HELPER + "." + RoomHelper.COLUMN_TO
								   + ") AND " + RoomTable.TABLE_ROOM + "." + RoomTable.COLUMN_TYPE + "= '" + roomType + "';";

		final Cursor cursor = database.rawQuery(freeRoomQuery, null);

		if(cursor.getCount() == 0) {
			retVal = null;
		} else {
			cursor.moveToFirst();
			retVal = new Room(cursor.getLong(5), cursor.getString(6), cursor.getInt(7), cursor.getInt(8));
		}

		cursor.close();
		return retVal;
	}
	/**
	 * Helper Method checking if there are rooms of given type not yet used in a booking.
	 * @param roomType type of the room
	 * @return The Room as Object not yet referenced in the BookingTable.
	 */
	private Room checkUnusedRoom(final String roomType) {
		final Room retVal;

		final String getUnusedRoom = "SELECT * FROM " + RoomTable.TABLE_ROOM
									 + " WHERE " + RoomTable.COLUMN_ID
									 + " NOT IN (SELECT " + RoomHelper.COLUMN_ROOM_ID
									 + " FROM " + RoomHelper.TABLE_ROOM_HELPER
									 + ") AND " + RoomTable.COLUMN_TYPE
									 + " = '" + roomType + "'";

		final Cursor cursor = database.rawQuery(getUnusedRoom, null);

		if(cursor.getCount() == 0) {
			retVal = null;
		} else {
			cursor.moveToFirst();
			retVal = new Room(cursor.getLong(0), cursor.getString(1), cursor.getInt(2), cursor.getInt(3));
		}

		cursor.close();
		return retVal;
	}
	//	   _    _      _
	//	  | |  | |    | |
	//	  | |__| | ___| |_ __   ___ _ __ ___
	//	  |  __  |/ _ \ | '_ \ / _ \ '__/ __|
	//	  | |  | |  __/ | |_) |  __/ |  \__ \
	//	  |_|  |_|\___|_| .__/ \___|_|  |___/
	//	                | |
	//	                |_|
	/**
	 * Generates a ValueSet with all values required for a customer.
	 * @param title the title of the customer
	 * @param firstname customers firstname
	 * @param lastname customers lastname
	 * @param street street the customer live in
	 * @param nr number corresponding to the street
	 * @param plz zip code of the city customer lives at
	 * @param city city customer lives at
	 * @param bdate birthdate of the customer
	 * @return ContentValues with all the values required to enter a customer into the db.
	 */
	public static ContentValues generateCustomerValues(final String title, final String firstname, final String lastname,
													   final String street, final int nr,
													   final String plz, final String city,
													   final String bdate) {
		final ContentValues values = new ContentValues();

		values.put(CustomerTable.COLUMN_TITLE, title);
		values.put(CustomerTable.COLUMN_FIRSTNAME, firstname);
		values.put(CustomerTable.COLUMN_LASTNAME, lastname);
		values.put(CustomerTable.COLUMN_STREET, street);
		values.put(CustomerTable.COLUMN_NUMBER, nr);
		values.put(CustomerTable.COLUMN_PLZ, plz);
		values.put(CustomerTable.COLUMN_CITY, city);
		values.put(CustomerTable.COLUMN_BDATE, bdate);

		return values;
	}
	/**
	 * Generates a ValueSet with all values required for a Booking.
	 * @param cId customer ID
	 * @return ContentValues with all the values required to enter a booking into the db.
	 */
	public static ContentValues generateBookingValues(final long cId) {
		final ContentValues values = new ContentValues();

		values.put(BookingTable.COLUMN_CUSTOMER, cId);

		return values;
	}
	/**
	 * Reads all Elements from a cursor into a Customer Object.
	 * @param cursor the cursor with the elements from the database
	 * @return The Customer as Object.
	 */
	private Customer cursorToCustomer(final Cursor cursor) {
		final Customer customer = new Customer();
	    customer.setId(cursor.getLong(0));
	    customer.setTitle(cursor.getString(1));
	    customer.setFirstname(cursor.getString(2));
	    customer.setLastname(cursor.getString(3));
	    customer.setStreet(cursor.getString(4));
	    customer.setNumber(cursor.getInt(5));
	    customer.setPlz(cursor.getString(6));
	    customer.setCity(cursor.getString(7));
	    customer.setBdate(cursor.getString(8));
	    return customer;
	}
	/**
	 * Reads all Elements from a cursor into a Service Object.
	 * @param cursor the cursor with the elements from the database
	 * @return The Service as Object.
	 */
	private Service cursorToService(final Cursor cursor) {
		final Service service = new Service();
		service.setId(cursor.getLong(0));
		service.setType(cursor.getString(1));
		service.setPrice(cursor.getInt(2));
		return service;
	}
}// closing class
