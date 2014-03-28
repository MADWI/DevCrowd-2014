package pl.devcrowd.app.broadcasts;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.notifications.Notifications;
import pl.devcrowd.app.utils.DebugLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
	
	private static final String TICKER_TEXT = "Nie spóŸnij siê na prezentacjê: ";
	private static final String MESSAGE_TEXT = "Za 10 minut rozpoczyna siê prezentacja.";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		DebugLog.d("ID of AlarmAction: " + intent.getIntExtra("lessonID", -1));
		Notifications.setNotification(
				getPresentationTitle(
						Integer.toString(intent.getIntExtra("lessonID", -1)),
						context), 
						MESSAGE_TEXT, 
						TICKER_TEXT, 
						intent.getIntExtra("lessonID", -1),
						context);
	}

	private String getPresentationTitle(String id, Context context) {
		Cursor cursor = getCursor(
				DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, context);
		if (cursor == null) {
			return null;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {

				return cursor.getString(cursor
						.getColumnIndex(DevcrowdTables.PRESENTATION_TITLE));
			}
		}
		return null;
	}

	private Cursor getCursor(Uri uri, Context context) {
		return context.getContentResolver().query(uri, null, null, null,
				DevcrowdTables.PRESENTATION_ID + " DESC");
	}
}
