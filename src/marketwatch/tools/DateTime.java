package marketwatch.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
	
	/** Gets date and time as milliseconds since year 1970, and returns date and time as seconds since year 1970.
	 * Millisecond reminder will be rounded down.
	 * @param millisecond Date and time as milliseconds since year 1970
	 * @return Date and time as seconds since year 1970 */
	public long millisecondToSecond(long millisecond) {
		long remainder = millisecond % 1000;
		long second = (millisecond - remainder) / 1000;
		return second;
	}
	
	/** Gets date and time as seconds since year 1970, and returns date and time as milliseconds since year 1970.
	 * @param second Date and time as seconds since year 1970
	 * @return Date and time as milliseconds since year 1970 */
	public long secondToMillisecond(long second) {
		long millisecond = second * 1000;
		return millisecond;
	}
	
	/** Gets date and time as date object, and returns date and time as milliseconds since year 1970.
	 * @param date Date and time as date object
	 * @return Date and time as milliseconds since year 1970 */
	public long dateToMillisecond(Date date) {
		long millisecond = date.getTime();
		return millisecond;
	}
	
	/** Gets date and time as milliseconds since year 1970, and returns date and time as date object.
	 * @param second Date and time as milliseconds since year 1970
	 * @return Date and time as date object */
	public Date millisecondToDate(long millisecond) {
		Date date = new Date(millisecond);
		return date;
	}
	
	/** Gets date and time as date object, and returns date and time as seconds since year 1970.
	 * @param date Date and time as date object
	 * @return Date and time as seconds since year 1970 */
	public long dateToSecond(Date date) {
		long millisecond = dateToMillisecond(date);
		long second = millisecondToSecond(millisecond);
		return second;
	}
	
	/** Gets date and time as seconds since year 1970, and returns date and time as date object.
	 * @param second Date and time as seconds since year 1970
	 * @return Date and time as date object */
	public Date secondToDate(long second) {
		long millisecond = secondToMillisecond(second);
		Date date = new Date(millisecond);
		return date;
	}
	
	/** Gets date and time as date object, and returns date and/or time as string in a specific format.
	 * @param date Date and time as date object
	 * @param dateFormat Date and time format
	 * @return Date and time as string */
	public String dateToString(Date date, SimpleDateFormat dateFormat) {
		try {
			String text = dateFormat.format(date);
			return text;
		}
		catch (Exception ex) {
			return null;
		}
	}

	/** Gets date and time as date object, and returns date and time as string in the order.
	 * Format: "dd/MM/yyyy HH:mm:ss"
	 * @param date Date and time as date object
	 * @return Date and time as string in the order */
	public String dateToString_dateTime(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return dateToString(date, dateFormat);
	}
	
	/** Gets date and time as date object, and returns time and date as string in the order.
	 * Format: "HH:mm:ss dd/MM/yyyy"
	 * @param date Date and time as date object
	 * @return Time and date as string in the order */
	public String dateToString_timeDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return dateToString(date, dateFormat);
	}
	
	/** Gets date and time as date object, and returns date as string.
	 * Format: "dd/MM/yyyy"
	 * @param date Date and time as date object
	 * @return Date as string */
	public String dateToString_date(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		return dateToString(date, dateFormat);
	}

	/** Gets date and time as date object, and returns time as string.
	 * Format: "HH:mm:ss"
	 * @param date Date and time as date object
	 * @return Time as string */
	public String dateToString_time(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		return dateToString(date, dateFormat);
	}
	
	/** Gets date and time as string, and returns date and time as seconds since year 1970 in a specific format.
	 * @param str Date and time as string
	 * @param dateFormat Date and time format
	 * @return Date and time as seconds since year 1970 */
	public long stringToLong(String str, SimpleDateFormat dateFormat) {
		try {
			Date date = dateFormat.parse(str);
			return dateToSecond(date);
		}
		catch (Exception ex) {
			return -1;
		}
	}
	
	/** Gets date and time as string in the order, and returns date and time as seconds since year 1970.
	 * @param str Date and time as string in the order
	 * @return Date and time as seconds since year 1970 */
	public long stringToLong_dateTime(String str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return stringToLong(str, dateFormat);
	}
	
	/** Gets time and date as string in the order, and returns date and time as seconds since year 1970.
	 * @param str Time and date as string in the order
	 * @return Date and time as seconds since year 1970 */
	public long stringToLong_timeDate(String str) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
		return stringToLong(str, dateFormat);
	}
	
	/** Gets date as string in the order, and returns date and midnight time as seconds since year 1970.
	 * @param str Date as string
	 * @return Date and time as seconds since year 1970 */
	public long stringToLong_date(String str) {
		str = str + " 00:00:00";
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return stringToLong(str, dateFormat);
	}
	
	/** Gets time as string in the order, and returns today date and time as seconds since year 1970.
	 * @param str Time as string in the order
	 * @return Date and time as seconds since year 1970 */
	public long stringToLong_time(String str) {
		str = dateToString_date(new Date()) + " " + str;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return stringToLong(str, dateFormat);
	}
}