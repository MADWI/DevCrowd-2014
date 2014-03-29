package pl.devcrowd.app.db;

import java.util.Arrays;
import java.util.HashSet;

import pl.devcrowd.app.utils.DebugLog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DevcrowdContentProvider extends ContentProvider {

	// database
	private DevcrowdDatabaseHelper database;

	// Used for the UriMacher
	private static final int PRESENTATIONS = 10;
	private static final int PRESENTATION_ID = 20;
	private static final int SPEAKERS = 30;
	private static final int SPEAKER_ID = 40;
	private static final int JOIN = 50;

	private static final String AUTHORITY = "pl.devcrowd.app.db";

	private static final String PRESENTATIONS_PATH = "presentations";
	private static final String SPEAKERS_PATH = "speakers";
	private static final String JOIN_PATH = "join";

	public static final Uri CONTENT_URI_PRESENATIONS = Uri.parse("content://"
			+ AUTHORITY + "/" + PRESENTATIONS_PATH);
	public static final Uri CONTENT_URI_SPEAKERS = Uri.parse("content://"
			+ AUTHORITY + "/" + SPEAKERS_PATH);
	public static final Uri CONTENT_URI_JOIN = Uri.parse("content://"
			+ AUTHORITY + "/" + JOIN_PATH);

	public static final String CONTENT_TYPE_PRESENTATIONS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/presentations";
	public static final String CONTENT_ITEM_TYPE_PRESENTATIONS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/presentation";
	public static final String CONTENT_TYPE_SPEKAERS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/speakers";
	public static final String CONTENT_ITEM_TYPE_SPEKAERS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/speaker";

	public static final String EMAIL_RATE_VALUES_KEY = "rateEmail";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, PRESENTATIONS_PATH, PRESENTATIONS);
		sURIMatcher.addURI(AUTHORITY, PRESENTATIONS_PATH + "/#",
				PRESENTATION_ID);
		sURIMatcher.addURI(AUTHORITY, SPEAKERS_PATH, SPEAKERS);
		sURIMatcher.addURI(AUTHORITY, SPEAKERS_PATH + "/#", SPEAKER_ID);
		sURIMatcher.addURI(AUTHORITY, JOIN_PATH, JOIN);
	}

	@Override
	public boolean onCreate() {
		database = new DevcrowdDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		String groupBy = null;

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case PRESENTATIONS:
			queryBuilder.setTables(DevcrowdTables.TABLE_PRESENTATIONS);
			break;
		case PRESENTATION_ID:
			queryBuilder.setTables(DevcrowdTables.TABLE_PRESENTATIONS);
			// Adding the ID to the original query
			queryBuilder.appendWhere(DevcrowdTables.PRESENTATION_ID + "="
					+ uri.getLastPathSegment());
			break;
		case SPEAKERS:
			queryBuilder.setTables(DevcrowdTables.TABLE_SPEAKERS);
			break;
		case SPEAKER_ID:
			queryBuilder.setTables(DevcrowdTables.TABLE_SPEAKERS);
			// Adding the ID to the original query
			queryBuilder.appendWhere(DevcrowdTables.SPEAKER_COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		case JOIN:
			queryBuilder.setTables(DevcrowdTables.TABLE_PRESENTATIONS
					+ " LEFT OUTER JOIN " + DevcrowdTables.TABLE_SPEAKERS
					+ " ON (" + DevcrowdTables.PRESENTATION_TITLE + " = "
					+ DevcrowdTables.SPEAKER_COLUMN_PRESENTATION_TITLE + ")");
			groupBy = DevcrowdTables.TABLE_PRESENTATIONS + "."
					+ DevcrowdTables.PRESENTATION_TITLE;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, groupBy, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		DebugLog.d("query rows:" + cursor.getCount());
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		String uriPath = "";
		long id = 0;
		switch (uriType) {
		case PRESENTATIONS:
			id = sqlDB.insert(DevcrowdTables.TABLE_PRESENTATIONS, null, values);
			uriPath = PRESENTATIONS_PATH;
			break;
		case SPEAKERS:
			id = sqlDB.insert(DevcrowdTables.TABLE_SPEAKERS, null, values);
			uriPath = SPEAKERS_PATH;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(uriPath + "/" + id);
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case PRESENTATIONS:
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DevcrowdTables.TABLE_PRESENTATIONS,
						values, selection, selectionArgs);
				DebugLog.d(rowsUpdated + "");
			}
			break;
		case SPEAKERS:
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DevcrowdTables.TABLE_SPEAKERS,
						values, selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
}
