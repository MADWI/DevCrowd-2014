package pl.devcrowd.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {

	public static Calendar getTimeAfterInSecs(int secs, String dateTime) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
				Locale.GERMANY);
		try {
			Date date = df.parse(dateTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.SECOND, secs);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Calendar getTimeAfterInSecs(int secs) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, secs);
		return cal;
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
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss",
				Locale.GERMANY);
		df.setLenient(false);
		String s = df.format(cal.getTime());
		return s;
	}
}
