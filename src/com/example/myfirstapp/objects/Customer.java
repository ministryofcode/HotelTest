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
 * Customer Object with all relevant dates to define a customer.
 * Implements the Interface Parcelable so it's possible to pass Object
 * between different Activities of the GUI.
 * @author Lutsch
 *
 */
public class Customer implements Parcelable {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	private long id;
	private String title;
	private String firstname;
	private String lastname;
	private String street;
	private int number;
	private String plz;
	private String city;
	private String bdate;
	// -------------------------------------------------------------------------
	//   ___ _____ ___  ___
	//  / __|_   _/ _ \| _ \___
	// | (__  | || (_) |   (_-<
	//  \___| |_| \___/|_|_|__/
	//
	public Customer() {}
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
	public String getTitle() {
		return title;
	}
	public void setTitle(final String title) {
		this.title = title;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(final String street) {
		this.street = street;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(final int number) {
		this.number = number;
	}
	public String getPlz() {
		return plz;
	}
	public void setPlz(final String plz) {
		this.plz = plz;
	}
	public String getCity() {
		return city;
	}
	public void setCity(final String city) {
		this.city = city;
	}
	public String getBdate() {
		return bdate;
	}
	public void setBdate(final String bdate) {
		this.bdate = bdate;
	}
	@Override
	public String toString() {
		return firstname + " " + lastname;
	}
	/**
	 * Returns the Customer with ALL available Information.
	 * @return String of all infos for one customer.
	 */
	public String toFullString() {
		return "id: " + id + "\n"
				+ title + "\n"
				+ "Name: " + firstname + " " + lastname + "\n"
				+ "Str: " + street + " " + number + "\n"
				+ "Ort: " + plz + " " + city + "\n"
				+ "Geb.Dat.: " + bdate;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(title);
		dest.writeString(firstname);
		dest.writeString(lastname);
		dest.writeString(street);
		dest.writeInt(number);
		dest.writeString(plz);
		dest.writeString(city);
		dest.writeString(bdate);

	}
	public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
		@Override
		public Customer createFromParcel(Parcel in)
		{
			return new Customer(in);
		}
		@Override
		public Customer[] newArray(int size)
		{
			return new Customer[size];
		}
	};
	private Customer(Parcel in) {
		id = in.readLong();
		title = in.readString();
		firstname = in.readString();
		lastname = in.readString();
		street = in.readString();
		number = in.readInt();
		plz = in.readString();
		city = in.readString();
		bdate = in.readString();
	}
}