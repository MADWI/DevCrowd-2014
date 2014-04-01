package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.utils.DebugLog;
import pl.devcrowd.app.utils.ProgressUtils;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FavouritesListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private SimpleCursorAdapter adapter;
	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (isAdded()) {
			ProgressUtils.show(getActivity());
		}
		fillData();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favo_list_view, container, false);
		list = (ListView) view.findViewById(android.R.id.list);
		list.setSelector(android.R.color.transparent);
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (isAdded()) {
			if (isAdded()) {
				Cursor cursor = ((SimpleCursorAdapter) l.getAdapter())
						.getCursor();
				cursor.moveToPosition(position);
				Intent iDetails = new Intent(getActivity(),
						ScheduleDetailsActivity.class);
				iDetails.putExtra(
						DevcrowdTables.PRESENTATION_ID,
						cursor.getString(cursor
								.getColumnIndex(DevcrowdTables.PRESENTATION_ID)));
				startActivity(iDetails);
				getActivity().overridePendingTransition(
						R.anim.slide_left_enter, R.anim.slide_left_exit);
			}
		}
	}

	private void fillData() {
		String[] from = new String[] {
				DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_HOUR_JOIN,
				DevcrowdTables.JOIN_SPEAKERS_NAMES,
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID };
		int[] to = new int[] { R.id.textFavoItemTopic, R.id.textFavoItemHour,
				R.id.textFavoItemSpeaker, R.id.textFavoRoom };

		getLoaderManager().initLoader(LOADER_ID, null, this);

		adapter = new SimpleCursorAdapter(getActivity(), R.layout.favo_item,
				null, from, to, NO_FLAGS);

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
				DevcrowdTables.PRESENTATION_FAVOURITE,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.PRESENTATION_ROOM };
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
				DevcrowdContentProvider.CONTENT_URI_JOIN, projection,
				DevcrowdTables.PRESENTATION_FAVOURITE + " =? ",
				new String[] { DevcrowdTables.PRESENTATION_FAVOURITE_FLAG },
				"CAST(" + DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_START
						+ " as datetime) ASC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		DebugLog.d("onLoadFinished rows:" + data.getCount());
		adapter.swapCursor(data);
		if (isAdded()) {
			ProgressUtils.hide(getActivity());
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		DebugLog.d("onLoaderReset");
		adapter.swapCursor(null);
	}
}
