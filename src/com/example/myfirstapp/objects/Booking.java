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
package com.example.myfirstapp.objects;

import java.util.List;

/**
 * Booking Object which stores all the relevant Data for a booking.
 * @author Lutsch
 *
 */
public class Booking {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	private long _id;
	private String timestamp;
	private String firstname;
	private String lastname;
	private List<Room> rooms;
	private List<Service> services;
    // -------------------------------------------------------------------------
    //   ___ _____ ___  ___
    //  / __|_   _/ _ \| _ \___
    // | (__  | || (_) |   (_-<
    //  \___| |_| \___/|_|_|__/
    //
	/**
	 * Constructor for Booking-Object.
	 * @param _id The specific ID of the booking. (underscore is required for certain Android Functions)
	 * @param ts timestamp of the booking creation
	 * @param fn firstname of the related customer
	 * @param ln lastname of the related customer
	 * @param rl List with booked Rooms
	 * @param sl List with booked Services
	 */
	public Booking(final long _id, final String ts,
			       final String fn, final String ln,
			       final List<Room> rl, final List<Service> sl) {
		this._id = _id;
		this.timestamp = ts;
		this.firstname = fn;
		this.lastname = ln;
		this.rooms = rl;
		this.services = sl;
	}
    // -------------------------------------------------------------------------
    //  __  __ ___ _____ _  _  ___  ___
    // |  \/  | __|_   _| || |/ _ \|   \ ___
    // | |\/| | _|  | | | __ | (_) | |) (_-<
    // |_|  |_|___| |_| |_||_|\___/|___//__/
    //
	public long get_id() {
		return _id;
	}
	public void set_id(long _id) {
		this._id = _id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public List<Room> getRooms() {
		return rooms;
	}
	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}
	public List<Service> getServices() {
		return services;
	}
	public void setServices(List<Service> services) {
		this.services = services;
	}
	@Override
	public String toString() {
		final String retVal = "Buchung " + _id + " vom: " + timestamp
						+ "\n" + firstname + " " + lastname
						+ " hat folgende Zimmer gebucht:";

		String roomVal = "";
		String serviceVal = "";

		for( Room temp: rooms ) {
			roomVal += "\n" + temp.getDays() + " Tage " +  temp.getType() + " Nr: " + temp.getRoomNumber() + " für " + temp.getDays()*temp.getPrice() + "€.";
		}

		if(!services.isEmpty()) {
			for( Service temp: services) {
				serviceVal += "\n" + temp.getType() + " " + temp.getPrice();
			}
		}

		return retVal + roomVal + serviceVal;
	}


}