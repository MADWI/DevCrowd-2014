package pl.devcrowd.app.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DevcrowdTables {

	// Database table
	public static final String TABLE_PRESENTATIONS = "presentationsTable";
	public static final String PRESENTATION_ID = "_id";
	public static final String PRESENTATION_TITLE = "title";
	public static final String PRESENTATION_ROOM = "room";
	public static final String PRESENTATION_START = "hourStart";
	public static final String PRESENTATION_END = "hourEnd";
	public static final String PRESENTATION_PRELEGENT = "prelegentName";
	public static final String PRESENTATION_DESCRIPTION = "description";
	
	public static final String TABLE_PRELEGENCI = "prelegenciTable";
	public static final String PRELEGENT_COLUMN_ID = "_id";
	public static final String PRELEGENT_COLUMN_NAME = "prelegentName";
	public static final String PRELEGENT_COLUMN_DESCRIPTION = "prelegentDescription";
	public static final String PRELEGENT_COLUMN_FOTO = "prelegentFoto";

	// Database creation SQL statement
	private static final String DATABASE_PRESENTATIONS_CREATE = "create table " 
			+ TABLE_PRESENTATIONS
			+ "(" + PRESENTATION_ID + " integer primary key autoincrement, " 
			+ PRESENTATION_TITLE + " text not null, " 
			+ PRESENTATION_ROOM + " text not null, " 
			+ PRESENTATION_START + " text not null, " 
			+ PRESENTATION_END + " text not null, " 
			+ PRESENTATION_PRELEGENT + " text not null, "
			+ PRESENTATION_DESCRIPTION + " text not null "
			+ ");";
	
	// Database creation SQL statement
	private static final String DATABASE_PRELEGENCI_CREATE = "create table " 
			+ TABLE_PRELEGENCI
			+ "(" + PRELEGENT_COLUMN_ID + " integer primary key autoincrement, " 
			+ PRELEGENT_COLUMN_NAME + " text not null, " 
			+ PRELEGENT_COLUMN_DESCRIPTION + " text not null, "
			+ PRELEGENT_COLUMN_FOTO + " text not null "
			+ ");";
		
	public static void onCreate(SQLiteDatabase database) {
		Log.w(DevcrowdTables.class.getName(),"Create DB: " + DATABASE_PRESENTATIONS_CREATE);
		database.execSQL(DATABASE_PRESENTATIONS_CREATE);
		database.execSQL(DATABASE_PRELEGENCI_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(DevcrowdTables.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRESENTATIONS);
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRELEGENCI);
		onCreate(database);
	}
}
