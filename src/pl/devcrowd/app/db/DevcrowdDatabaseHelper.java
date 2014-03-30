package pl.devcrowd.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DevcrowdDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "devcrowd.db";
	private static final int DATABASE_VERSION = 4;

	public DevcrowdDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DevcrowdTables.DATABASE_PRESENTATIONS_CREATE);
		database.execSQL(DevcrowdTables.DATABASE_SPEAKERS_CREATE);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + DevcrowdTables.TABLE_PRESENTATIONS);
		database.execSQL("DROP TABLE IF EXISTS " + DevcrowdTables.TABLE_SPEAKERS);
		onCreate(database);
	}
}
