package pl.devcrowd.app.notifications;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.db.DevcrowdTables;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

public final class Notifications {

	private Notifications() {

	}

	public static void setNotification(final String title,
			final String message, final String tickerText, final int lessonID,
			Context mContext) {
		Intent iDetails = new Intent(mContext, ScheduleDetailsActivity.class);
		iDetails.putExtra(DevcrowdTables.PRESENTATION_ID, Integer.toString(lessonID));
		PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
				iDetails, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.ic_launcher).setContentTitle(title)
				.setContentText(message)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setTicker(tickerText)
				.setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
				.setContentIntent(contentIntent);

		NotificationManager mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(lessonID, mBuilder.build());
	}

}
