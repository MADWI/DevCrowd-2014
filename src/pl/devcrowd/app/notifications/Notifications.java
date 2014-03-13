package pl.devcrowd.app.notifications;

import pl.devcrowd.app.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

public final class Notifications {

	private Notifications(){
		
	}

	public static void setNotification(final String title, final String message, final String tickerText, final int lessonID, Context mContext) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(message)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setTicker(tickerText)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

		NotificationManager mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(lessonID, mBuilder.build());
	}

}
