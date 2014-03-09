package pl.devcrowd.app.models;

import java.util.ArrayList;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Speaker {
	
	private String name;
	private String photoPath;
	private String description;
	
	public Speaker(){
		this.name = null;
		this.photoPath = null;
		this.description = null;
	}
	
	public Speaker(String name, String photoPath, String description){
		this.setName(name);
		this.setPhotoPath(photoPath);
		this.setDescription(description);
	}
	
	//DB methods
	
	Speaker(ContentResolver resolver, String name) {
		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRELEGENCI;
		String[] projection = { DevcrowdTables.PRELEGENT_COLUMN_NAME,
				DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION,
				DevcrowdTables.PRELEGENT_COLUMN_FOTO };
		Cursor cursor = resolver.query(uri, projection,
				DevcrowdTables.PRELEGENT_COLUMN_NAME + "=?",
				new String[] { name }, DevcrowdTables.PRELEGENT_COLUMN_ID + " DESC");
		if (cursor != null) {
			cursor.moveToFirst();

			this.name = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_NAME));
			this.description = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION));
			this.photoPath = cursor
					.getString(cursor
							.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_FOTO));

			cursor.close();
		} else {
			this.name = null;
			this.description = null;
			this.photoPath = null;
		}
	}
	
	public Uri addToDB(ContentResolver resolver){
		ContentValues values = new ContentValues();
		values.put(DevcrowdTables.PRELEGENT_COLUMN_NAME, this.name);
		values.put(DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION, this.description);
		values.put(DevcrowdTables.PRELEGENT_COLUMN_FOTO, this.photoPath);

		return resolver.insert(DevcrowdContentProvider.CONTENT_URI_PRELEGENCI, values);
	}
	
	public ArrayList<Speaker> getPrelegents(ContentResolver resolver) {

		ArrayList<Speaker> resultList = new ArrayList<Speaker>();

		Uri uri = DevcrowdContentProvider.CONTENT_URI_PRELEGENCI;
		String[] projection = { DevcrowdTables.PRELEGENT_COLUMN_NAME,
				DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION,
				DevcrowdTables.PRELEGENT_COLUMN_FOTO };
		Cursor cursor = resolver.query(uri, projection, null, null, DevcrowdTables.PRELEGENT_COLUMN_ID + " DESC");
		if (cursor != null) {
			cursor.moveToFirst();
			Speaker tmpSpeaker;
			String name, description, photoPath;

			do{
				try {
					name = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_NAME));
					description = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_DESCRIPTION));
					photoPath = cursor
							.getString(cursor
									.getColumnIndexOrThrow(DevcrowdTables.PRELEGENT_COLUMN_FOTO));
					tmpSpeaker = new Speaker(name, description, photoPath);
					resultList.add(tmpSpeaker);
				} catch (Exception e) {
					Log.e("Speaker", "Error " + e.toString());
				}
			} while(cursor.moveToNext());
			cursor.close();
		}		
		return resultList;
	}
	
	//END - DB methods

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the photoPath
	 */
	public String getPhotoPath() {
		return photoPath;
	}

	/**
	 * @param photoPath the photoPath to set
	 */
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
