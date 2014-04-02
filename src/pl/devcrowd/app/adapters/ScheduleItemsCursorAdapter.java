package pl.devcrowd.app.adapters;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevcrowdTables;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ScheduleItemsCursorAdapter extends CursorAdapter implements
		OnClickListener {

	public interface AdapterInterface {
		public void buttonPressed(String presentationTitle, String hourStart,
				String presentationID, boolean isChecked, View toggleView);
	}

	private LayoutInflater mInflater;
	private AdapterInterface mAdapterInterface;
	private List<Boolean> tooglesStates;
	
	public ScheduleItemsCursorAdapter(Context context, Cursor cursor,
			int flags, AdapterInterface mAdapterInterface) {
		super(context, cursor, flags);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mAdapterInterface = mAdapterInterface;

		populateNewTogglesList(cursor);
	}

	public void updateTogglesList(Cursor cursor) {
		populateNewTogglesList(cursor);
	}

	private void populateNewTogglesList(Cursor cursor) {
		tooglesStates = new ArrayList<Boolean>();
		while (cursor.moveToNext()) {

			String checked = cursor.getString(cursor
					.getColumnIndex(DevcrowdTables.PRESENTATION_FAVOURITE));
			boolean isChecked = (checked
					.equals(DevcrowdTables.PRESENTATION_FAVOURITE_FLAG)) ? true
					: false;
			tooglesStates.add(isChecked);
		}
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ScheduleItemHolder holder = (ScheduleItemHolder) view.getTag();

		String presentationTitle = getStringValue(cursor,
				DevcrowdTables.PRESENTATION_TITLE);
		String hourStart = getStringValue(cursor,
				DevcrowdTables.PRESENTATION_START);
		String presentationID = getStringValue(cursor,
				DevcrowdTables.PRESENTATION_ID);

		ItemData itemData = new ItemData();
		itemData.id = presentationID;
		itemData.topic = presentationTitle;
		itemData.hourStart = hourStart;
		itemData.hour = cursor.getString(cursor
				.getColumnIndex(DevcrowdTables.PRESENTATION_HOUR_JOIN));
		itemData.position = cursor.getPosition();
		itemData.speaker = cursor.getString(cursor
				.getColumnIndex(DevcrowdTables.JOIN_SPEAKERS_NAMES));

		holder.textItemHour.setText(itemData.hour);
		holder.textItemTopic.setText(presentationTitle);
		holder.textItemSpeaker.setText(itemData.speaker);
		
		if (holder.textItemSpeaker.getText().toString().equals("")) {
			holder.textItemSpeaker.setVisibility(View.GONE);
		} else {
			holder.textItemSpeaker.setVisibility(View.VISIBLE);
		}

		if (tooglesStates.size() > itemData.position) {
			holder.starButton.setChecked(tooglesStates.get(itemData.position));
		}
		holder.starButton.setTag(itemData);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		View view = mInflater.inflate(R.layout.schedule_item, parent, false);

		ScheduleItemHolder holder = new ScheduleItemHolder();
		holder.textItemHour = (TextView) view.findViewById(R.id.textItemHour);
		holder.textItemTopic = (TextView) view.findViewById(R.id.textItemTopic);
		holder.textItemSpeaker = (TextView) view
				.findViewById(R.id.textItemSpeaker);
		
		holder.starButton = (ToggleButton) view.findViewById(R.id.toggleFavo);
		holder.starButton.setOnClickListener(this);

		view.setTag(holder);

		return view;
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ToggleButton) {
			boolean checked = ((ToggleButton) view).isChecked();
			ItemData itemData = (ItemData) view.getTag();
			View toggleView = view.findViewById(R.id.toggleFavo);
			mAdapterInterface.buttonPressed(itemData.topic, itemData.hourStart,
					itemData.id, checked, toggleView);
			tooglesStates.set(itemData.position, checked);

		}

	}

	private String getStringValue(Cursor cursor, String columnName) {
		String value = "";
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex >= 0) {
			value = cursor.getString(columnIndex);
			if (value == null) {
				value = "";
			}
		}
		return value;
	}

	private static class ScheduleItemHolder {
		public TextView textItemHour;
		public TextView textItemTopic;
		public TextView textItemSpeaker;
		public ToggleButton starButton;
	}

	private static class ItemData {
		public String id;
		public String hourStart;
		public String hour;
		public String topic;
		public String speaker;
		int position;
	}

}
