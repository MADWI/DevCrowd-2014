package pl.devcrowd.app.fragments;

import java.util.Calendar;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.adapters.ScheduleItemsCursorAdapter;
import pl.devcrowd.app.adapters.ScheduleItemsCursorAdapter.AdapterInterface;
import pl.devcrowd.app.alarms.Alarms;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.services.ApiService;
import pl.devcrowd.app.utils.CalendarUtils;
import pl.devcrowd.app.utils.ContentProviderHelper;
import pl.devcrowd.app.utils.DebugLog;
import pl.devcrowd.app.utils.ProgressUtils;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, AdapterInterface {

	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private static final int ALARM_DELAY_MS = -600;
	private static final String PRESENTATION_DATE = "04/12/2014 ";	
	private ScheduleItemsCursorAdapter adapter;
	private String roomNumber = "126";

	private AlarmManager am;

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
		fillData();
		asyncLoadPresentationsAndSpeakers();
	}

	private void asyncLoadPresentationsAndSpeakers() {

		if (isAdded()) {
			ProgressUtils.show(getActivity());
		}

		Intent intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiService.ACTION_GET_PRESENTATIONS);
		intent.putExtra(ApiService.RECEIVER_KEY, new ServiceResultReceiver(
				new Handler()));
		getActivity().startService(intent);
		intent = new Intent(getActivity(), ApiService.class);
		intent.setAction(ApiService.ACTION_GET_SPEAKERS);
		intent.putExtra(ApiService.RECEIVER_KEY, new ServiceResultReceiver(
				new Handler()));
		getActivity().startService(intent);
	}

	private class ServiceResultReceiver extends ResultReceiver {

		public ServiceResultReceiver(Handler handler) {
			super(handler);
		}

		@Override
		public void onReceiveResult(int resultCode, Bundle resultData) {
			super.onReceiveResult(resultCode, resultData);
			if (isAdded()) {
				ProgressUtils.hide(getActivity());

				if (resultCode == ApiService.LOADING_SUCCESS) {
					getLoaderManager().restartLoader(LOADER_ID, null,
							ScheduleListFragment.this);
				} else if (resultCode == ApiService.LOADING_FAILED) {
					Toast.makeText(getActivity(),
							getString(R.string.error_loading_presentations),
							Toast.LENGTH_LONG).show();
				}
			}
		}
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
		if (isAdded()) {
			Cursor cursor = ((ScheduleItemsCursorAdapter) l.getAdapter())
					.getCursor();
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
				DevcrowdTables.JOIN_SPEAKERS_NAMES,
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.textItemTopic, R.id.textItemHour,
				R.id.textItemSpeaker };

		getLoaderManager().initLoader(LOADER_ID, null, this);

		adapter = new ScheduleItemsCursorAdapter(getActivity(), null, NO_FLAGS,
				this);

		setListAdapter(adapter);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID,
				DevcrowdTables.PRESENTATION_HOUR_JOIN,
				"GROUP_CONCAT(" + DevcrowdTables.SPEAKER_COLUMN_NAME
						+ ",', ') AS " + DevcrowdTables.JOIN_SPEAKERS_NAMES,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_FAVOURITE };
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
		adapter.swapCursor(null);
	}

	@Override
	public void buttonPressed(String presentationTitle, String hourStart,
			String presentationID, boolean isChecked) {
		ContentProviderHelper.updateFavourite(getActivity()
				.getContentResolver(), presentationTitle, isChecked);

		if (isChecked) {
			Calendar cal = CalendarUtils.getCurrentTime();

			StringBuilder tmp = new StringBuilder(PRESENTATION_DATE + hourStart
					+ ":00");

			cal = CalendarUtils.getDateDifferBySeconds(ALARM_DELAY_MS, tmp.toString());

			Alarms.setAlarm(Integer.parseInt(presentationID),
					cal.getTimeInMillis(), getActivity(), am);

			Toast.makeText(getActivity(),
					"Date: " + tmp.toString() + "\nState: " + isChecked,
					Toast.LENGTH_LONG).show();
		} else {
			Alarms.cancelAlarm(Integer.parseInt(presentationID), getActivity(),
					am);
		}
	}
}
