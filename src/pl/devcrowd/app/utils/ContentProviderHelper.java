package pl.devcrowd.app.utils;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.models.Presentation;
import pl.devcrowd.app.models.Speaker;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public final class ContentProviderHelper {
	private static final String TAG = ContentProviderHelper.class
			.getSimpleName();
	private static final String DEFAULT_SORT = " DESC";	
	
	private ContentProviderHelper() {
	};

	public static void notifyChange(final ContentResolver resolver,
			final Uri uri) {
		resolver.notifyChange(uri, null);
	}

	public static Uri addPresenatiton(final ContentResolver resolver,
			final Presentation presentation) {
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.PRESENTATION_TITLE, presentation.getTitle());
		values.put(DevcrowdTables.PRESENTATION_DESCRIPTION,
				presentation.getDescription());
		values.put(DevcrowdTables.PRESENTATION_ROOM, presentation.getRoom());
		values.put(DevcrowdTables.PRESENTATION_START, presentation.getHourEnd());
		values.put(DevcrowdTables.PRESENTATION_END, presentation.getHourEnd());
//		values.put(DevcrowdTables.PRESENTATION_SPEAKER,
//				presentation.getSpeaker());
		values.put(DevcrowdTables.PRESENTATION_TOPIC_GRADE, presentation.getGradeTopic());
		values.put(DevcrowdTables.PRESENTATION_SPEAKER_GRADE, presentation.getGradeSpeaker());
		values.put(DevcrowdTables.PRESENTATION_FAVOURITE, presentation.getFavourite());

		return resolver.insert(
				DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, values);
	}

	public static List<Presentation> getPresentations(
			final ContentResolver resolver) {

		List<Presentation> resultList = new ArrayList<Presentation>();

		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRESENATIONS;
		String[] projection = { 
//				DevcrowdTables.PRESENTATION_SPEAKER,
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_END,
				DevcrowdTables.PRESENTATION_DESCRIPTION,
				DevcrowdTables.PRESENTATION_TOPIC_GRADE,
				DevcrowdTables.PRESENTATION_SPEAKER_GRADE,
				DevcrowdTables.PRESENTATION_FAVOURITE};
		Cursor cursor = resolver.query(uri, projection, null, null,
				DevcrowdTables.PRESENTATION_ID + DEFAULT_SORT);

		if (cursor != null) {
			while (cursor.moveToNext()) {
				resultList.add(getPresentation(cursor));
			}
			cursor.close();
		}
		return resultList;
	}

	public static Presentation getPresentation(final ContentResolver resolver,
			final String presentationTitle) {
		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRESENATIONS;
		String[] projection = { 
//				DevcrowdTables.PRESENTATION_SPEAKER,
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_END,
				DevcrowdTables.PRESENTATION_DESCRIPTION,
				DevcrowdTables.PRESENTATION_TOPIC_GRADE,
				DevcrowdTables.PRESENTATION_SPEAKER_GRADE,
				DevcrowdTables.PRESENTATION_FAVOURITE};
		Cursor cursor = resolver.query(uri, projection,
				DevcrowdTables.PRESENTATION_TITLE + "=?",
				new String[] { presentationTitle },
				DevcrowdTables.PRESENTATION_ID + DEFAULT_SORT);

		return getPresentation(cursor);

	}
	
	public static void updatePresentation(final ContentResolver resolver, 
			final String titleOld, final Presentation presentation) {
		AsyncQueryHandler asyncHandler = new AsyncQueryHandler(resolver) {};
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.PRESENTATION_TITLE, presentation.getTitle());
		values.put(DevcrowdTables.PRESENTATION_DESCRIPTION,
				presentation.getDescription());
		values.put(DevcrowdTables.PRESENTATION_ROOM, presentation.getRoom());
		values.put(DevcrowdTables.PRESENTATION_START, presentation.getHourEnd());
		values.put(DevcrowdTables.PRESENTATION_END, presentation.getHourEnd());
//		values.put(DevcrowdTables.PRESENTATION_SPEAKER,
//				presentation.getSpeaker());
		values.put(DevcrowdTables.PRESENTATION_TOPIC_GRADE, presentation.getGradeTopic());
		values.put(DevcrowdTables.PRESENTATION_SPEAKER_GRADE, presentation.getGradeSpeaker());
		values.put(DevcrowdTables.PRESENTATION_FAVOURITE, presentation.getFavourite());
		
		asyncHandler.startUpdate(-1, null, DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, 
				values, DevcrowdTables.PRESENTATION_TITLE + "=?", new String[]{titleOld});
	}
	
	
	public static void updateRating(final ContentResolver resolver, 
			final String presentationTitle, float topiGrade, float speakerGrade) {
		AsyncQueryHandler asyncHandler = new AsyncQueryHandler(resolver) {};
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.PRESENTATION_TOPIC_GRADE, topiGrade);
		values.put(DevcrowdTables.PRESENTATION_SPEAKER_GRADE, speakerGrade);
		asyncHandler.startUpdate(-1, null, DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, 
				values, DevcrowdTables.PRESENTATION_TITLE + "=?", new String[]{presentationTitle});
	}

	public static Uri addSpeaker(final ContentResolver resolver,
			final Speaker speaker) {
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.SPEAKER_COLUMN_NAME, speaker.getName());
		values.put(DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION,
				speaker.getDescription());
		values.put(DevcrowdTables.SPEAKER_COLUMN_FOTO, speaker.getPhotoUrl());

		return resolver.insert(DevcrowdContentProvider.CONTENT_URI_SPEAKERS,
				values);
	}

	private static Presentation getPresentation(final Cursor cursor) {
		Presentation presentation = new Presentation();
		if (cursor != null) {
			cursor.moveToFirst();
			presentation.setTitle(getColumnValue(cursor,
					DevcrowdTables.PRESENTATION_TITLE));
			presentation.setRoom(getColumnValue(cursor,
					DevcrowdTables.PRESENTATION_ROOM));
			presentation.setDescription(getColumnValue(cursor,
					DevcrowdTables.PRESENTATION_DESCRIPTION));
			presentation.setHourStart(getColumnValue(cursor,
					DevcrowdTables.PRESENTATION_START));
			presentation.setHourEnd(getColumnValue(cursor,
					DevcrowdTables.PRESENTATION_END));
//			presentation.setSpeaker(getColumnValue(cursor,
//					DevcrowdTables.PRESENTATION_SPEAKER));
			cursor.close();

		}
		return presentation;
	}

	public static Speaker getSpeaker(final ContentResolver resolver,
			final String name) {
		Uri uri = DevcrowdContentProvider.CONTENT_URI_SPEAKERS;
		String[] projection = { DevcrowdTables.SPEAKER_COLUMN_NAME,
				DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION,
				DevcrowdTables.SPEAKER_COLUMN_FOTO };
		Cursor cursor = resolver.query(uri, projection,
				DevcrowdTables.SPEAKER_COLUMN_NAME + "=?",
				new String[] { name }, DevcrowdTables.SPEAKER_COLUMN_ID
						+ DEFAULT_SORT);
		return getSpeaker(cursor);
	}

	private static Speaker getSpeaker(final Cursor cursor) {
		final Speaker speaker = new Speaker();
		if (cursor != null) {
			cursor.moveToFirst();
			speaker.setName(getColumnValue(cursor,
					DevcrowdTables.SPEAKER_COLUMN_NAME));
			speaker.setDescription(getColumnValue(cursor,
					DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION));
			speaker.setPhotoUrl(getColumnValue(cursor,
					DevcrowdTables.SPEAKER_COLUMN_FOTO));
			cursor.close();
		}
		return speaker;
	}

	public List<Speaker> getSpeakerS(final ContentResolver resolver) {

		List<Speaker> resultList = new ArrayList<Speaker>();

		Uri uri = DevcrowdContentProvider.CONTENT_URI_SPEAKERS;
		String[] projection = { DevcrowdTables.SPEAKER_COLUMN_NAME,
				DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION,
				DevcrowdTables.SPEAKER_COLUMN_FOTO };
		Cursor cursor = resolver.query(uri, projection, null, null,
				DevcrowdTables.SPEAKER_COLUMN_ID + DEFAULT_SORT);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				resultList.add(getSpeaker(cursor));
			}
			cursor.close();
		}
		return resultList;
	}
	
	public static void updateSpeaker(final ContentResolver resolver, 
			final String nameOld, final Speaker speaker) {
		AsyncQueryHandler asyncHandler = new AsyncQueryHandler(resolver) {};
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.SPEAKER_COLUMN_NAME, speaker.getName());
		values.put(DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION,
				speaker.getDescription());
		values.put(DevcrowdTables.SPEAKER_COLUMN_FOTO, speaker.getPhotoUrl());
		
		asyncHandler.startUpdate(-1, null, DevcrowdContentProvider.CONTENT_URI_SPEAKERS, 
				values, DevcrowdTables.SPEAKER_COLUMN_NAME + "=?", new String[]{nameOld});
	}

	private static String getColumnValue(final Cursor cursor,
			final String columnName) {
		try {
			int index = cursor.getColumnIndexOrThrow(columnName);
			return cursor.getString(index);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, "IllegalArgumentException unknown kolumn");
			return "";
		}

	}

}
