package pl.devcrowd.app.db;

import java.util.Arrays;
import java.util.HashSet;

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
	private static final int PRELEGENCI = 30;
	private static final int PRELEGENT_ID = 40;

	private static final String AUTHORITY = "pl.devcrowd.app.db";

	private static final String PRESENTATIONS_PATH = "presentations";
	private static final String PRELEGENCI_PATH = "prelegenci";
	
	public static final Uri CONTENT_URI_PRESENATIONS = Uri.parse("content://" + AUTHORITY
			+ "/" + PRESENTATIONS_PATH);
	public static final Uri CONTENT_URI_PRELEGENCI = Uri.parse("content://" + AUTHORITY
			+ "/" + PRELEGENCI_PATH);

	public static final String CONTENT_TYPE_PRESENTATIONS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/presentations";
	public static final String CONTENT_ITEM_TYPE_PRESENTATIONS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/presentation";
	public static final String CONTENT_TYPE_PRELEGENTS = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/prelegenci";
	public static final String CONTENT_ITEM_TYPE_PRELEGENTS = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/prelegent";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, PRESENTATIONS_PATH, PRESENTATIONS);
		sURIMatcher.addURI(AUTHORITY, PRESENTATIONS_PATH + "/#", PRESENTATION_ID);
		sURIMatcher.addURI(AUTHORITY, PRELEGENCI_PATH, PRELEGENCI);
		sURIMatcher.addURI(AUTHORITY, PRELEGENCI_PATH + "/#", PRELEGENT_ID);
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

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

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
		case PRELEGENCI:
			queryBuilder.setTables(DevcrowdTables.TABLE_PRELEGENCI);
			break;
		case PRELEGENT_ID:
			queryBuilder.setTables(DevcrowdTables.TABLE_PRELEGENCI);
			// Adding the ID to the original query
			queryBuilder.appendWhere(DevcrowdTables.PRELEGENT_COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

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
		case PRELEGENCI:
			id = sqlDB.insert(DevcrowdTables.TABLE_PRELEGENCI, null, values);
			uriPath = PRELEGENCI_PATH;
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
		case PRESENTATION_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(DevcrowdTables.TABLE_PRESENTATIONS, values,
						DevcrowdTables.PRESENTATION_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(DevcrowdTables.TABLE_PRESENTATIONS, values,
						DevcrowdTables.PRESENTATION_ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}
	
	private void checkColumns(String[] projection) {
		String[] available = { DevcrowdTables.PRESENTATION_ID,
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_END,
				DevcrowdTables.PRESENTATION_PRELEGENT,
				DevcrowdTables.PRESENTATION_DESCRIPTION,
				DevcrowdTables.PRELEGENT_COLUMN_ID,
				DevcrowdTables.PRELEGENT_COLUMN_NAME,
				DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION,
				DevcrowdTables.PRELEGENT_COLUMN_FOTO };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}
}
