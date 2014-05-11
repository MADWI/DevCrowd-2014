package pl.devcrowd.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
	
	public static boolean compareTwoDates(String alarmDate, String currentDate) {

		try {
			Date date1 = dateFormat.parse(alarmDate);
			Date date2 = dateFormat.parse(currentDate);
			
			if (date1 != null && date2 != null) {
	            boolean retVal = date2.before(date1);
	            return retVal;
	        }
	        return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		}
    }

	private static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"MM/dd/yyyy HH:mm:ss", new Locale("pl", "PL"));

	public static Calendar getDateDifferBySeconds(int secs, String dateTime) {

		try {
			Date date = dateFormat.parse(dateTime);
			Calendar cal = Calendar.getInstance(new Locale("pl", "PL"));
			cal.setTime(date);
			cal.add(Calendar.SECOND, secs);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Calendar getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		return cal;
	}

	public static Calendar getTodayAt(int hours) {
		Calendar today = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();
		cal.clear();

		int year = today.get(Calendar.YEAR);
		int month = today.get(Calendar.MONTH);

		int day = today.get(Calendar.DATE);
		cal.set(year, month, day, hours, 0, 0);
		return cal;
	}

	public static String getDateTimeString(Calendar cal) {
		dateFormat.setLenient(false);
		String s = dateFormat.format(cal.getTime());
		return s;
	}
}
