package pl.devcrowd.app.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public final class DevcrowdTables {

	public static final String TABLE_PRESENTATIONS = "presentationsTable";
	public static final String PRESENTATION_ID = "_id";
	public static final String PRESENTATION_TITLE = "title";
	public static final String PRESENTATION_ROOM = "room";
	public static final String PRESENTATION_START = "hourStart";
	public static final String PRESENTATION_END = "hourEnd";
	public static final String PRESENTATION_SPEAKER = "speakerName";
	public static final String PRESENTATION_DESCRIPTION = "description";
	public static final String PRESENTATION_TOPIC_GRADE = "presentationTopicGrade";
	public static final String PRESENTATION_OVERALL_GRADE = "presentationOverallGrade";
	public static final String PRESENTATION_FAVOURITE = "presentationFavourite";

	public static final String TABLE_SPEAKERS = "speakersTable";
	public static final String SPEAKER_COLUMN_ID = "_id";
	public static final String SPEAKER_COLUMN_NAME = "speakerName";
	public static final String SPEAKER_COLUMN_DESCRIPTION = "speakerDescription";
	public static final String SPEAKER_COLUMN_FOTO = "speakerFoto";

	private static final String TYPE_ID = " integer primary key autoincrement ";
	private static final String TYPE_TEXT_NOT_NULL_COLUMN = " text not null ";

	private static final String DATABASE_PRESENTATIONS_CREATE = "create table "
			+ TABLE_PRESENTATIONS + "(" + PRESENTATION_ID + TYPE_ID + ","
			+ PRESENTATION_TITLE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_ROOM + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_START + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_END + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_SPEAKER + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_DESCRIPTION + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_TOPIC_GRADE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_OVERALL_GRADE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_FAVOURITE + TYPE_TEXT_NOT_NULL_COLUMN + ");";

	private static final String DATABASE_PRELEGENCI_CREATE = "create table "
			+ TABLE_SPEAKERS + "(" + SPEAKER_COLUMN_ID + TYPE_ID + ","
			+ SPEAKER_COLUMN_NAME + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ SPEAKER_COLUMN_DESCRIPTION + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ SPEAKER_COLUMN_FOTO + TYPE_TEXT_NOT_NULL_COLUMN + ");";

	private DevcrowdTables() {
	};

	public static void onCreate(final SQLiteDatabase database) {
		Log.w(DevcrowdTables.class.getName(), "Create DB: "
				+ DATABASE_PRESENTATIONS_CREATE);
		database.execSQL(DATABASE_PRESENTATIONS_CREATE);
		database.execSQL(DATABASE_PRELEGENCI_CREATE);
	}

	public static void onUpgrade(final SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DevcrowdTables.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENTATIONS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SPEAKERS);
		onCreate(database);
	}
}
