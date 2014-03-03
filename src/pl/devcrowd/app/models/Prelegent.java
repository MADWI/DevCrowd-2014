package pl.devcrowd.app.models;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class Prelegent {
	
	private String name;
	private String photoPath;
	private String description;
	
	Prelegent(String name, String photoPath, String description){
		this.setName(name);
		this.setPhotoPath(photoPath);
		this.setDescription(description);
	}
	
	Prelegent(ContentResolver resolver, String name) {
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
