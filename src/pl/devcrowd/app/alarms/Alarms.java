package pl.devcrowd.app.alarms;

import pl.devcrowd.app.utils.DebugLog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public final class Alarms {

	private AlarmManager alarmManager;
	private static final String lessonIDTAG = "lessonID";

	private Alarms() {
		
	}

	public void setAlarm(final int lessonID, final long time, Context ctx) {
		try {
			alarmManager = (AlarmManager) ctx
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent("AlarmReceiver");
			intent.putExtra(lessonIDTAG, lessonID);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,
					lessonID, intent, PendingIntent.FLAG_ONE_SHOT);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 0,
					pendingIntent);
			DebugLog.d("Alarm for ID: " + lessonID + "is set on now!");
		} catch (Exception e) {
			DebugLog.e(e.getMessage());
		}
	}

	public void cancelAlarm(final int lessonID, Context ctx) {
		try {
			Intent intent = new Intent("AlarmReceiver");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,
					lessonID, intent, 0);
			alarmManager = (AlarmManager) ctx
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			DebugLog.d("Alarm for ID: " + lessonID + "was canceled!");
		} catch (Exception e) {
			DebugLog.e(e.getMessage());
		}
	}

}
