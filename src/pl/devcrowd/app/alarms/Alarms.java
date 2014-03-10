package pl.devcrowd.app.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class Alarms {
	
	private Context ctx;
	private AlarmManager alarmManager;
	
	public Alarms(Context context){
		this.ctx = context;
	}
	
	public void setAlarm(int lessonID, long time){
		alarmManager = (AlarmManager) ctx.getSystemService(ctx.ALARM_SERVICE);
		Intent intent = new Intent("AlarmReceiver");
		intent.putExtra("lessonID", lessonID);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, lessonID, intent, PendingIntent.FLAG_ONE_SHOT);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 0, pendingIntent);
	}

}
