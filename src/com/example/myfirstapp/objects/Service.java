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
 * Object for a Service with all relevant Data.
 * Implements the interface Parcelable so it's possible to pass object
 * between different Activities of the GUI.
 * @author Lutsch
 *
 */
public class Service implements Parcelable {
    // -------------------------------------------------------------------------
    //   ___  ___    _ ___ ___ _______   ___   ___
    //  / _ \| _ )_ | | __/ __|_   _\ \ / /_\ | _ \___
    // | (_) | _ \ || | _| (__  | |  \ V / _ \|   (_-<
    //  \___/|___/\__/|___\___| |_|   \_/_/ \_\_|_|__/
    //
	private long id;
	private String type;
	private int price;
	private String from;
	private String to;
	private int days;
    // -------------------------------------------------------------------------
    //   ___ _____ ___  ___
    //  / __|_   _/ _ \| _ \___
    // | (__  | || (_) |   (_-<
    //  \___| |_| \___/|_|_|__/
    //
	public Service() {}
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
	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(final int price) {
		this.price = price;
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
		return type + " " + price;
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
        dest.writeString(from);
        dest.writeString(to);
        dest.writeInt(days);
    }
    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>()
    {
        @Override
		public Service createFromParcel(Parcel in)
        {
            return new Service(in);
        }
        @Override
		public Service[] newArray(int size)
        {
            return new Service[size];
        }
    };
    private Service(Parcel in) {
    	id = in.readLong();
    	type = in.readString();
    	price = in.readInt();
    	from = in.readString();
    	to = in.readString();
    	days = in.readInt();
    }
}
