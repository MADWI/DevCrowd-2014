package pl.devcrowd.app.db;

public final class DevcrowdTables {

	public static final String TABLE_PRESENTATIONS = "presentationsTable";
	public static final String PRESENTATION_ID = "_id";
	public static final String PRESENTATION_TITLE = "title";
	public static final String PRESENTATION_ROOM = "room";
	public static final String PRESENTATION_START = "hourStart";
	public static final String PRESENTATION_END = "hourEnd";
	public static final String PRESENTATION_DESCRIPTION = "description";
	public static final String PRESENTATION_TOPIC_GRADE = "presentationTopicGrade";
	public static final String PRESENTATION_SPEAKER_GRADE = "presentationSpeakerGrade";
	public static final String PRESENTATION_FAVOURITE = "presentationFavourite";
	public static final String PRESENTATION_HOUR_JOIN = "presentationHourJoin";

	public static final String TABLE_SPEAKERS = "speakersTable";
	public static final String SPEAKER_COLUMN_ID = "_id";
	public static final String SPEAKER_COLUMN_PRESENTATION_NAME = "speakerPresentationName";
	public static final String SPEAKER_COLUMN_NAME = "speakerName";
	public static final String SPEAKER_COLUMN_DESCRIPTION = "speakerDescription";
	public static final String SPEAKER_COLUMN_FOTO = "speakerFoto";

	private static final String TYPE_ID = " integer primary key autoincrement ";
	private static final String TYPE_TEXT_NOT_NULL_COLUMN = " text not null ";

	public static final String DATABASE_PRESENTATIONS_CREATE = "create table "
			+ TABLE_PRESENTATIONS + "(" + PRESENTATION_ID + TYPE_ID + ","
			+ PRESENTATION_TITLE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_ROOM + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_START + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_END + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_DESCRIPTION + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_TOPIC_GRADE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_SPEAKER_GRADE + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_HOUR_JOIN + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ PRESENTATION_FAVOURITE + TYPE_TEXT_NOT_NULL_COLUMN + ");";

	public static final String DATABASE_SPEAKERS_CREATE = "create table "
			+ TABLE_SPEAKERS + "(" + SPEAKER_COLUMN_ID + TYPE_ID + ","
			+ SPEAKER_COLUMN_PRESENTATION_NAME + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ SPEAKER_COLUMN_NAME + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ SPEAKER_COLUMN_DESCRIPTION + TYPE_TEXT_NOT_NULL_COLUMN + ","
			+ SPEAKER_COLUMN_FOTO + TYPE_TEXT_NOT_NULL_COLUMN + ");";

	private DevcrowdTables() {
	};
}
