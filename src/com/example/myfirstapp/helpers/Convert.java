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
package com.example.myfirstapp.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Helper Class for Converting Dates, and other helpers.
 * @author Lutsch
 *
 */
public class Convert {
    // -------------------------------------------------------------------------
    //   ___ _____ ___  ___
    //  / __|_   _/ _ \| _ \___
    // | (__  | || (_) |   (_-<
    //  \___| |_| \___/|_|_|__/
    //
	/**
	 * Empty constructor prevents instances of the class.
	 */
	public Convert() {}
    // -------------------------------------------------------------------------
    //  _  _ ___ _    ___ ___ ___
    // | || | __| |  | _ \ __| _ \___
    // | __ | _|| |__|  _/ _||   (_-<
    // |_||_|___|____|_| |___|_|_|__/
    //
	/**
	 * Converts a Date from DB-Format to GUI-Format.
	 * Database Format: YYYY-MM-DD
	 * GUI-Format: DD.MM.YYYY
	 * @param dbFormat Date-String in DB-FOrmat i.e. YYYY-MM-DD
	 * @return The String in GUI-Format
	 */
	public static String toGuiDate(final String dbFormat) {
		final String retVal;
		retVal = dbFormat.substring(8) + "." + dbFormat.substring(5, 7) + "." + dbFormat.substring(0, 4);
		return retVal;
	}
	/**
	 * Converts a Date from GUI-Format to DB-Format.
	 * Database Format: YYYY-MM-DD
	 * GUI-Format: DD.MM.YYYY
	 * @param guiFormat Data as String in GUI-Format i.e. DD.MM.YYYY
	 * @return String in DB-Format
	 */
	public static String toDbDate(final String guiFormat) {
		final String retVal;
		final int[] dateAsInt = dateToInts(guiFormat);
		retVal = String.format(Locale.GERMANY, "%d-%02d-%02d", dateAsInt[2], dateAsInt[1]+1, dateAsInt[0]);
		return retVal;
	}
	/**
	 * Takes a Date in GUI-Format, reads the values for day, month and year
	 * and returns them as an Integer Array. Months are Zero Based, JANUARY = 0
	 * Pos[0] = Day
	 * Pos[1] = Month
	 * Pos[2] = Year
	 * @param guiFormat Data as String in GUI-Format i.e. DD.MM.YYYY
	 * @return Int Array with the values
	 */
	public static int[] dateToInts(final String guiFormat) {
		final int[] date = new int[3];

		// GUI Format is DD.MM.YYYY
		//				 0123456789
		date[0] = Integer.parseInt(guiFormat.substring(0, 2));
		date[1] = Integer.parseInt(guiFormat.substring(3, 5))-1;
		date[2] = Integer.parseInt(guiFormat.substring(guiFormat.length()-4));

		return date;
	}
	/**
	 * Calculates the number of days who lie between to dates.
	 * @param fromDate Date the Guest is coming DB-Format
	 * @param toDate Date the guest is leaving DB-Format
	 * @return The number of days as an int.
	 */
	public static int inDaysToStay(final String fromDate, final String toDate) {
	    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);

	    final Calendar start = Calendar.getInstance();
	    final Calendar end = Calendar.getInstance();

	    try
	    {
	    	start.setTime(sdf.parse(fromDate));
		    end.setTime(sdf.parse(toDate));
	    }
	    catch(ParseException e)
	    {
	    	System.out.println(e.getMessage());
	    }

	    final long difference = end.getTimeInMillis() - start.getTimeInMillis();
	    final long diff = difference / (24 * 60 * 60 * 1000);
	    int days = (int)diff;

	    if(days == 0)
	    	days = 1;

	    return days;
	}
	/**
	 * Checks the entered Values of a Customer Dataset.	 *
	 * @param fields String-Array with following values:
	 * 			0	title
	 * 			1	firstname
	 * 			2	lastname
	 * 			3	street
	 * 			4	number
	 * 			5 	plz
	 * 			6	city
	 * 			7	bdate
	 * @return The Return Message as String or failure Code else.
	 */
	public static String checkEnteredValues(String[] fields) {
		final String[] errorCode = { "erfolg",												// 0
								"Vorname, Nachname, Stra§e und Ort mŸssen Text enthalten",	// 1
								"PLZ muss 5 Stellen haben",									// 2
								"Geburtsdatum im Format TT.MM.JJJJ angeben!",				// 3
								"Bitte alle Felder ausfŸllen!"								// 4
							  };

		// Check if all Values are filled in.
		for(String field: fields) {
			if(field.trim().equals("")) {
				return errorCode[4];
			}
		}

		// Check if Firstname, Lastname, Street or City contain a digit
		if(containsDigit(fields[1]) || containsDigit(fields[2]) || containsDigit(fields[3]) || containsDigit(fields[6])) {
			return errorCode[1];
		}

		// Check if plz has length of 5
		if(fields[5].length() != 5) {
			return errorCode[2];
		}

		// Check if Date is in Format DD.MM.YYYY
		if(fields[7].length() != 10 || fields[7].charAt(2) != '.' || fields[7].charAt(5) != '.') {
			return errorCode[3];
		}

		// If everything is fine return Success
		return errorCode[0];
	}
	/**
	 * Help method to Determine whether a String contains a digit or not.
	 * @param string The string to check.
	 * @return true is there is a Digit in or False if teher is not.
	 */
	private static final boolean containsDigit(String string){
	    boolean containsDigit = false;

        for(char c : string.toCharArray()){
            if(containsDigit = Character.isDigit(c)){
                break;
            }
        }

	    return containsDigit;
	}
}
