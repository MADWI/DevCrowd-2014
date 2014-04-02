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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.PreferenceManager;
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

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.targets.ViewTarget;

public class ScheduleListFragment extends ListFragment implements
		LoaderCallbacks<Cursor>, AdapterInterface, OnRefreshListener {

	private static final String SHOWN_FAVOURITE = "SHOWN_FAVOURITE";
	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private static final int ALARM_DELAY_MS = -600;
	private static final String PRESENTATION_DATE = "04/12/2014 ";
	private ScheduleItemsCursorAdapter adapter;
	private String roomNumber = "126";
	private ListView list;

	private AlarmManager am;
	private SwipeRefreshLayout swipeLayout;

	private SharedPreferences pref;
	private SharedPreferences.Editor editor;

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

		pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		editor = pref.edit();
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
		View view = inflater.inflate(R.layout.schedule_list_view, container,
				false);

		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(R.color.swipeRefreshColor1,
				R.color.swipeRefreshColor2, R.color.swipeRefreshColor3,
				R.color.swipeRefreshColor4);

		list = (ListView) view.findViewById(android.R.id.list);
		list.setSelector(android.R.color.transparent);

		return view;
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

			if (getStringValue(cursor, DevcrowdTables.SPEAKER_COLUMN_NAME)
					.equals("")) {
				return;
			}

			Intent iDetails = new Intent(getActivity(),
					ScheduleDetailsActivity.class);
			iDetails.putExtra(DevcrowdTables.PRESENTATION_ID,
					getStringValue(cursor, DevcrowdTables.PRESENTATION_ID));
			startActivity(iDetails);
			getActivity().overridePendingTransition(R.anim.slide_left_enter,
					R.anim.slide_left_exit);
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
				DevcrowdTables.PRESENTATION_FAVOURITE,
				DevcrowdTables.SPEAKER_COLUMN_NAME };
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
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		DebugLog.d("onLoadFinished rows:" + cursor.getCount());

		if (adapter == null) {
			adapter = new ScheduleItemsCursorAdapter(getActivity(), cursor,
					NO_FLAGS, this);

			setListAdapter(adapter);
		} else {
			adapter.updateTogglesList(cursor);
		}

		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		DebugLog.d("onLoaderReset");
		adapter.swapCursor(null);
	}

	@Override
	public void buttonPressed(String presentationTitle, String hourStart,
			String presentationID, boolean isChecked, View toggleView) {
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

			if (!pref.getBoolean(SHOWN_FAVOURITE, false)) {
				ViewTarget target = new ViewTarget(toggleView);
				ShowcaseView.insertShowcaseView(target, getActivity(),
						R.string.favoTitle, R.string.favoDetails);
				editor.putBoolean(SHOWN_FAVOURITE, true);
				editor.commit();
			}

		} else {
			Alarms.cancelAlarm(Integer.parseInt(presentationID), getActivity(),
					am);
		}
	}
}
