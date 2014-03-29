package pl.devcrowd.app.adapters;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevcrowdTables;
import android.content.Context;
import android.database.Cursor;
import android.sax.StartElementListener;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ScheduleItemsCursorAdapter extends CursorAdapter {
	
	public interface AdapterInterface {
        public void buttonPressed(String presentationTitle,
        		String hourStart, boolean isChecked);
    }
	
	private LayoutInflater mInflater;
	private AdapterInterface mAdapterInterface;

	public ScheduleItemsCursorAdapter(Context context, Cursor c, int flags, AdapterInterface mAdapterInterface) {
		super(context, c, flags);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mAdapterInterface = mAdapterInterface;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView textItemHour = (TextView) view.findViewById(R.id.textItemHour);
		TextView textItemTopic = (TextView) view.findViewById(R.id.textItemTopic);
		TextView textItemSpeaker = (TextView) view.findViewById(R.id.textItemSpeaker);
		
		ToggleButton starButton = (ToggleButton) view.findViewById(R.id.toggleFavo);
		
		final String presentationTitle = cursor.getString(cursor.getColumnIndex(DevcrowdTables.PRESENTATION_TITLE));
		final String hourStart = cursor.getString(cursor.getColumnIndex(DevcrowdTables.PRESENTATION_START));
		
		textItemHour.setText(cursor.getString(cursor.getColumnIndex(DevcrowdTables.PRESENTATION_HOUR_JOIN)));
		textItemTopic.setText(presentationTitle);
		textItemSpeaker.setText(cursor.getString(cursor.getColumnIndex(DevcrowdTables.JOIN_SPEAKERS_NAMES)));
		
		final String checked = cursor.getString(cursor.getColumnIndex(DevcrowdTables.PRESENTATION_FAVOURITE));
		final boolean isChecked = (checked.equals(DevcrowdTables.PRESENTATION_FAVOURITE_FLAG)) ? true : false;
		starButton.setChecked(isChecked);
		
		starButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mAdapterInterface.buttonPressed(presentationTitle, hourStart, isChecked);
			}
		});
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mInflater.inflate(R.layout.schedule_item, parent, false);
	}
}
