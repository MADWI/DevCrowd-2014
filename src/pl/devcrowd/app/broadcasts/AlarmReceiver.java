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

	private static final String TICKER_TEXT = "Nie spóŸnij siê na prezentacjê.";
	private String MESSAGE_TEXT = "Godzina: ";

	private int lessonID;

	@Override
	public void onReceive(Context context, Intent intent) {
		this.lessonID = intent.getIntExtra("lessonID", -1);

		DebugLog.d("ID of AlarmAction: " + this.lessonID);

		MESSAGE_TEXT += getPresentationStartTime(Integer.toString(this.lessonID), context)
				+ ", sala: " + getPresentationRoom(Integer.toString(this.lessonID), context);

		Notifications.setNotification(
				getPresentationTitle(
						Integer.toString(this.lessonID),
						context), MESSAGE_TEXT, TICKER_TEXT, this.lessonID,
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

	private String getPresentationStartTime(String id, Context context) {
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
						.getColumnIndex(DevcrowdTables.PRESENTATION_START));
			}
		}
		return null;
	}

	private String getPresentationRoom(String id, Context context) {
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
						.getColumnIndex(DevcrowdTables.PRESENTATION_ROOM));
			}
		}
		return null;
	}

	private Cursor getCursor(Uri uri, Context context) {
		return context.getContentResolver().query(uri, null, null, null,
				DevcrowdTables.PRESENTATION_ID + " DESC");
	}
}
