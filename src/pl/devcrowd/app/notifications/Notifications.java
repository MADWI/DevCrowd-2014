package pl.devcrowd.app.notifications;

import pl.devcrowd.app.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

public class Notifications {

	private Context mContext;

	public Notifications(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void setNotification(String title, String message) {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				mContext);
		mBuilder.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(message)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setSound(
						RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

		NotificationManager mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1, mBuilder.build());
	}

}
