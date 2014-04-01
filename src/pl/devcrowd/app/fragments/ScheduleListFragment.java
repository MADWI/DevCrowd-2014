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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class ScheduleListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, AdapterInterface, OnRefreshListener {

	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private static final int ALARM_DELAY_MS = -600;
	private static final String PRESENTATION_DATE = "04/12/2014 ";
	private ScheduleItemsCursorAdapter adapter;
	private String roomNumber = "126";
	private ListView list;

	private AlarmManager am;
	private SwipeRefreshLayout swipeLayout;

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
		getLoaderManager().initLoader(LOADER_ID, null, this);

	}

	@Override
	public void onStart() {
		super.onStart();
		getLoaderManager().restartLoader(LOADER_ID, null, this);
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
				swipeLayout.setRefreshing(false);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		swipeLayout = (SwipeRefreshLayout) inflater.inflate(
				R.layout.schedule_list_view, container, false);

		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.swipeRefreshColor1,
				R.color.swipeRefreshColor2, R.color.swipeRefreshColor3,
				R.color.swipeRefreshColor4);

		list = (ListView) swipeLayout.findViewById(android.R.id.list);
		list.setSelector(android.R.color.transparent);

		return swipeLayout;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		asyncLoadPresentationsAndSpeakers();
	}

	private void asyncLoadPresentationsAndSpeakers() {

		if (swipeLayout != null) {
			swipeLayout.setRefreshing(true);
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

	@Override
	public void onRefresh() {
		asyncLoadPresentationsAndSpeakers();
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
				new String[] { roomNumber }, "CAST("
						+ DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_START
						+ " as datetime) ASC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		DebugLog.d("onLoadFinished rows:" + data.getCount());

		if (adapter == null) {
			adapter = new ScheduleItemsCursorAdapter(getActivity(), data,
					NO_FLAGS, this);

			setListAdapter(adapter);
		}
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

			cal = CalendarUtils.getDateDifferBySeconds(ALARM_DELAY_MS,
					tmp.toString());

			Alarms.setAlarm(Integer.parseInt(presentationID),
					cal.getTimeInMillis(), getActivity(), am);

			Toast.makeText(
					getActivity(),
					getString(R.string.added_presentation_alarm_info) + " \""
							+ presentationTitle + "\"", Toast.LENGTH_SHORT)
					.show();

		} else {
			Alarms.cancelAlarm(Integer.parseInt(presentationID), getActivity(),
					am);
			Toast.makeText(
					getActivity(),
					getString(R.string.removed_presentation_alarm_info) + " \""
							+ presentationTitle + "\"", Toast.LENGTH_SHORT)
					.show();

		}
	}
}
