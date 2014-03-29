package pl.devcrowd.app.fragments;

import java.util.Calendar;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.alarms.Alarms;
import pl.devcrowd.app.broadcasts.AlarmReceiver;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.services.ApiService;
import pl.devcrowd.app.utils.CalendarUtils;
import pl.devcrowd.app.utils.DebugLog;
import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ScheduleListFragment extends ListFragment implements
		LoaderCallbacks<Cursor> {

	private ToggleButton tglFavo;

	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private static final String PRESENTATION_DATE = "04/12/2014 ";
	private SimpleCursorAdapter adapter;
	private String roomNumber = "126";

	private AlarmManager am;
	private int lessonID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		am = (AlarmManager) getActivity().getSystemService(
				Context.ALARM_SERVICE);
		if (getArguments() != null) {
			roomNumber = getArguments().getString(
					ScheduleHostFragment.ROOM_NUMBER);
		}
		setHasOptionsMenu(true);
		asyncLoadPresentationsAndSpeakers();
		fillData();
	}

	private void asyncLoadPresentationsAndSpeakers() {
		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiService.ACTION_GET_PRESENTATIONS);
		getActivity().startService(intent);
		intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiService.ACTION_GET_SPEAKERS);
		getActivity().startService(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(null);
		getListView().setSelector(android.R.color.transparent);
	}

	@Override
	public void onListItemClick(final ListView l, View v, final int position,
			long id) {

		tglFavo = (ToggleButton) v.findViewById(R.id.toggleFavo);
		tglFavo.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.toggleFavo:
					if (tglFavo.isChecked()) {
						Cursor cursor = ((SimpleCursorAdapter) l.getAdapter())
								.getCursor();
						cursor.moveToPosition(position);
						lessonID = Integer.parseInt(cursor.getString(cursor
								.getColumnIndex(DevcrowdTables.PRESENTATION_ID)));
						Calendar cal = CalendarUtils.getCurrentTime();
						
						StringBuilder tmp = new StringBuilder(PRESENTATION_DATE
								+ getPresentationStartTime(
										Integer.toString(lessonID),
										getActivity()) + ":00");
						
						DebugLog.d(tmp.toString());

						cal = CalendarUtils.getDateDifferBySeconds(-600,
								tmp.toString());

						Alarms.setAlarm(lessonID, cal.getTimeInMillis(),
								getActivity(), am);
						break;
					} else {
						Cursor cursor = ((SimpleCursorAdapter) l.getAdapter())
								.getCursor();
						cursor.moveToPosition(position);
						lessonID = Integer.parseInt(cursor.getString(cursor
								.getColumnIndex(DevcrowdTables.PRESENTATION_ID)));
						Alarms.cancelAlarm(lessonID, getActivity(), am);
						break;
					}
				}
			}
		});

		if (isAdded()) {
			Cursor cursor = ((SimpleCursorAdapter) l.getAdapter()).getCursor();
			cursor.moveToPosition(position);
			Intent iDetails = new Intent(getActivity(),
					ScheduleDetailsActivity.class);
			iDetails.putExtra(DevcrowdTables.PRESENTATION_ID, cursor
					.getString(cursor
							.getColumnIndex(DevcrowdTables.PRESENTATION_ID)));
			startActivity(iDetails);
			getActivity().overridePendingTransition(R.anim.slide_left_enter,
					R.anim.slide_left_exit);
		}
	}

	private void fillData() {
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] {
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_HOUR_JOIN,
				DevcrowdTables.SPEAKER_COLUMN_NAME,
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.textItemTopic, R.id.textItemHour,
				R.id.textItemSpeaker };

		getLoaderManager().initLoader(LOADER_ID, null, this);

		adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.schedule_item, null, from, to, NO_FLAGS);

		setListAdapter(adapter);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID,
				DevcrowdTables.PRESENTATION_HOUR_JOIN,
				DevcrowdTables.SPEAKER_COLUMN_NAME };
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
				DevcrowdContentProvider.CONTENT_URI_JOIN, projection,
				DevcrowdTables.PRESENTATION_ROOM + " =? ",
				new String[] { roomNumber }, DevcrowdTables.TABLE_PRESENTATIONS
						+ "." + DevcrowdTables.PRESENTATION_ID + " DESC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		DebugLog.d("onLoadFinished rows:" + data.getCount());
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		DebugLog.d("onLoaderReset");
		// data is not available anymore, delete reference
		adapter.swapCursor(null);
	}

	private String getPresentationStartTime(String id, Context context) {
		Cursor cursor = getCursor(
				DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, context);
		if (cursor == null) {
			return null;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {

				return cursor.getString(cursor
						.getColumnIndex(DevcrowdTables.PRESENTATION_START));
			}
		}
		return null;
	}

	private Cursor getCursor(Uri uri, Context context) {
		return context.getContentResolver().query(uri, null, null, null,
				DevcrowdTables.PRESENTATION_ID + " DESC");
	}
}
