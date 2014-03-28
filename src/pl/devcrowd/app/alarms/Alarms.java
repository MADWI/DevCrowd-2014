package pl.devcrowd.app.alarms;

import pl.devcrowd.app.utils.DebugLog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public final class Alarms {

	private static final String lessonIDTAG = "lessonID";

	private Alarms() {
		
	}

	public static void setAlarm(final int lessonID, final long time, Context ctx, AlarmManager alarmManager) {
			alarmManager = (AlarmManager) ctx
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent("AlarmReceiver");
			intent.putExtra(lessonIDTAG, lessonID);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,
					lessonID, intent, PendingIntent.FLAG_ONE_SHOT);
			alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
			DebugLog.d("Alarm for ID: " + lessonID + " is set on now!");
	}

	public static void cancelAlarm(final int lessonID, Context ctx, AlarmManager alarmManager) {
			Intent intent = new Intent("AlarmReceiver");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,
					lessonID, intent, PendingIntent.FLAG_ONE_SHOT);
			alarmManager = (AlarmManager) ctx
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.cancel(pendingIntent);
			DebugLog.d("Alarm for ID: " + lessonID + " was canceled!");
	}

}
