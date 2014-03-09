package pl.devcrowd.app.models;

import java.util.ArrayList;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Presentation {
	
	private String hourStart;
	private String hourEnd;
	private String room;
	private String description;
	private String title;
	private String prelegent;
	
	public Presentation() {
		super();
		this.hourStart = null;
		this.hourEnd = null;
		this.room = null;
		this.description = null;
		this.title = null;
		this.prelegent = null;
	}
	
	public Presentation(String hourStart, String hourEnd, String room,
			String description, String title, String prelegent) {
		super();
		this.hourStart = hourStart;
		this.hourEnd = hourEnd;
		this.room = room;
		this.description = description;
		this.title = title;
		this.prelegent = prelegent;
	}
	
	Presentation(ContentResolver resolver, String title) {
		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRESENATIONS;
		String[] projection = { DevcrowdTables.PRESENTATION_PRELEGENT,
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_END,
				DevcrowdTables.PRESENTATION_DESCRIPTION };
		Cursor cursor = resolver.query(uri, projection,
				DevcrowdTables.PRESENTATION_TITLE + "=?",
				new String[] { title }, DevcrowdTables.PRESENTATION_ID + " DESC");
		if (cursor != null) {
			cursor.moveToFirst();

			this.title = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_TITLE));
			this.room = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_ROOM));
			this.description = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_DESCRIPTION));
			this.hourStart = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_START));
			this.hourEnd = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_END));
			this.prelegent = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_PRELEGENT));

			cursor.close();
		} else {
			this.title = null;
			this.description = null;
			this.room = null;
			this.hourStart = null;
			this.hourEnd = null;
			this.prelegent = null;
		}
	}
	
	public Uri addToDB(ContentResolver resolver){
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.PRESENTATION_TITLE, this.title);
		values.put(DevcrowdTables.PRESENTATION_DESCRIPTION, this.description);
		values.put(DevcrowdTables.PRESENTATION_ROOM, this.room);
		values.put(DevcrowdTables.PRESENTATION_START, this.hourStart);
		values.put(DevcrowdTables.PRESENTATION_END, this.hourEnd);
		values.put(DevcrowdTables.PRESENTATION_PRELEGENT, this.prelegent);

		return resolver.insert(DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, values);
	}
	
	public ArrayList<Presentation> getPrelegents(ContentResolver resolver) {

		ArrayList<Presentation> resultList = new ArrayList<Presentation>();

		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRESENATIONS;
		String[] projection = { DevcrowdTables.PRESENTATION_PRELEGENT,
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_END,
				DevcrowdTables.PRESENTATION_DESCRIPTION };
		Cursor cursor = resolver.query(uri, projection, null, null, DevcrowdTables.PRESENTATION_ID + " DESC");
		if (cursor != null) {
			cursor.moveToFirst();
			Presentation tmpPresentation;
			String title, description, room, hourStart, hourEnd, prelegent;

			do{
				try {
					title = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_TITLE));
					description = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_DESCRIPTION));
					room = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_ROOM));
					hourStart = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_START));
					hourEnd = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_END));
					prelegent = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRESENTATION_PRELEGENT));
					tmpPresentation = new Presentation(hourStart, hourEnd, room, description, title, prelegent);
					resultList.add(tmpPresentation);
				} catch (Exception e) {
					Log.e("Presentation", "Error " + e.toString());
				}
			} while(cursor.moveToNext());
			cursor.close();
		}		
		return resultList;
	}

	public String getHourStart() {
		return hourStart;
	}


	public void setHourStart(String hourStart) {
		this.hourStart = hourStart;
	}


	public String getHourEnd() {
		return hourEnd;
	}


	public void setHourEnd(String hourEnd) {
		this.hourEnd = hourEnd;
	}


	public String getRoom() {
		return room;
	}


	public void setRoom(String room) {
		this.room = room;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getPrelegent() {
		return prelegent;
	}


	public void setSpeaker(String prelegent) {
		this.prelegent = prelegent;
	}
	
}
