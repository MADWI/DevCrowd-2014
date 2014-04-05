package pl.devcrowd.app.adapters;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.utils.DebugLog;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FavouritesItemsCursorAdapter extends CursorAdapter {

	private LayoutInflater mInflater;

	public FavouritesItemsCursorAdapter(Context context, Cursor cursor,
			int flags) {
		super(context, cursor, flags);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		FavouritesItemHolder holder = (FavouritesItemHolder) view.getTag();

		holder.textFavoItemTopic.setText(getStringValue(cursor,
				DevcrowdTables.PRESENTATION_TITLE));
		holder.textFavoItemHour.setText(getStringValue(cursor,
				DevcrowdTables.PRESENTATION_HOUR_JOIN));
		holder.textFavoItemSpeaker.setText(getStringValue(cursor,
				DevcrowdTables.JOIN_SPEAKERS_NAMES));
		holder.textFavoRoom.setText(getStringValue(cursor,
				DevcrowdTables.PRESENTATION_ROOM));

		CharSequence speaker = holder.textFavoItemSpeaker.getText();

		if (speaker != null && speaker.toString().equals("")) {
			holder.textFavoItemSpeaker.setVisibility(View.GONE);
		} else {
			holder.textFavoItemSpeaker.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View view = mInflater.inflate(R.layout.favo_item, parent, false);

		FavouritesItemHolder holder = new FavouritesItemHolder();
		holder.textFavoItemTopic = (TextView) view
				.findViewById(R.id.textFavoItemTopic);
		holder.textFavoItemHour = (TextView) view
				.findViewById(R.id.textFavoItemHour);
		holder.textFavoItemSpeaker = (TextView) view
				.findViewById(R.id.textFavoItemSpeaker);
		holder.textFavoRoom = (TextView) view.findViewById(R.id.textFavoRoom);
		view.setTag(holder);
		return view;
	}

	private String getStringValue(Cursor cursor, String columnName) {
		String value = "";
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex >= 0) {
			try {
				value = cursor.getString(columnIndex);
			} catch (IndexOutOfBoundsException e) {
				DebugLog.e(e.toString());
				value = null;
			}
			if (value == null) {
				value = "";
			}
		}
		return value;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		Cursor cursor = getCursor();
		cursor.moveToPosition(position);

		return getStringValue(cursor, DevcrowdTables.SPEAKER_COLUMN_NAME)
				.equals("") ? false : true;
	}

	private static class FavouritesItemHolder {
		public TextView textFavoItemTopic;
		public TextView textFavoItemHour;
		public TextView textFavoItemSpeaker;
		public TextView textFavoRoom;
	}
}
