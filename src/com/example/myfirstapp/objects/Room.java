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

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Object for a Room with all relevant Data.
 * Implements the interface Parcelable so it's possible to pass object
 * between different Activities of the GUI.
 * @author Lutsch
 *
 */
public class Room implements Parcelable {
	// -------------------------------------------------------------------------
	//   ___  ___    _ ___ ___ _______   ___   ___
	//  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
	// | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
	//  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
	//
	private long id;
	private String type;
	private int price;
	private int roomNumber;
	private String from;
	private String to;
	private int days;
	// -------------------------------------------------------------------------
	//   ___ _____ ___  ___
	//  / __|_   _/ _ \| _ \___
	// | (__  | || (_) |   (_-<
	//  \___| |_| \___/|_|_|__/
	//
	/**
	 * Constructor for the Room-Object.
	 * @param type type of the room
	 * @param price price of the room
	 * @param from Date of Arrival
	 * @param to Date of Departure
	 * @param days Days to Stay
	 */
	public Room(final String type, final int price, final String from, final String to, final int days) {
		this(0, type, price, 0, from, to, days);
	}
	/**
	 * Constructor for the Room-Object.
	 * @param id ID of the Room
	 * @param type type of the room
	 * @param price price of the room
	 * @param roomNumber number of the romm (real world not in database)
	 */
	public Room(final long id, final String type, final int price, final int number) {
		this(id, type, price, number, "", "", 0);
	}
	/**
	 * Constructor for the Room-Object.
	 * @param id ID of the Room
	 * @param type type of the room
	 * @param price price of the room
	 * @param roomNumber number of the Room (real world not in database)
	 * @param from Date of arrival
	 * @param to Date of departure
	 * @param days Number of Days to stay
	 */
	public Room(final long id, final String type, final int price, final int number, final String from, final String to, final int days) {
		this.id = id;
		this.roomNumber = number;
		this.price = price;
		this.type = type;
		this.from = from;
		this.to = to;
		this.days = days;
	}
	// -------------------------------------------------------------------------
	//  __  __ ___ _____ _  _  ___  ___
	// |  \/  | __|_   _| || |/ _ \|   \ ___
	// | |\/| | _|  | | | __ | (_) | |) (_-<
	// |_|  |_|___| |_| |_||_|\___/|___//__/
	//
	public long getId() {
		return id;
	}
	public void setId(final long id) {
		this.id = id;
	}
	public int getRoomNumber() {
		return roomNumber;
	}
	public void setRoomNumber(final int roomNumber) {
		this.roomNumber = roomNumber;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(final int price) {
		this.price = price;
	}
	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public int getDays() {
		return days;
	}
	public void setDays(int days) {
		this.days = days;
	}
	@Override
	public String toString() {
		return type + ", Nr: " + roomNumber + " Preis: " + price;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(type);
		dest.writeInt(price);
		dest.writeInt(roomNumber);
		dest.writeString(from);
		dest.writeString(to);
		dest.writeInt(days);
	}
	public static final Parcelable.Creator<Room> CREATOR = new Parcelable.Creator<Room>() {
		@Override
		public Room createFromParcel(Parcel in)
		{
			return new Room(in);
		}
		@Override
		public Room[] newArray(int size)
		{
			return new Room[size];
		}
	};
	private Room(Parcel in) {
		id = in.readLong();
		type = in.readString();
		price = in.readInt();
		roomNumber = in.readInt();
		from = in.readString();
		to = in.readString();
		days = in.readInt();
	}
}
