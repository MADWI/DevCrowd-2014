package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.adapters.FavouritesItemsCursorAdapter;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.utils.DebugLog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FavouritesListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor>, OnRefreshListener {

	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private FavouritesItemsCursorAdapter adapter;
	private ListView list;
	private SwipeRefreshLayout swipeLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getLoaderManager().initLoader(LOADER_ID, null, this);

	}

	@Override
	public void onStart() {
		super.onStart();
		forceRefreshList();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favo_list_view, container, false);

		swipeLayout = (SwipeRefreshLayout) view
				.findViewById(R.id.swipe_container_favo);
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
		setRefreshing(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (isAdded()) {
			Cursor cursor = ((FavouritesItemsCursorAdapter) l.getAdapter())
					.getCursor();
			cursor.moveToPosition(position);

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
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.SPEAKER_COLUMN_NAME };
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
		if (adapter == null) {
			adapter = new FavouritesItemsCursorAdapter(getActivity(), null,
					NO_FLAGS);
			setListAdapter(adapter);
		}
		
		setAdapterData(data);
		setRefreshing(false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		DebugLog.d("onLoaderReset");
		setAdapterData(null);
	}

	@Override
	public void onRefresh() {
		forceRefreshList();
	}

	private void forceRefreshList() {
		if (adapter != null) {
			setRefreshing(true);
			getLoaderManager().restartLoader(LOADER_ID, null, this);
		}
	}

	private void setRefreshing(boolean refreshing) {
		if (swipeLayout != null && isAdded()) {
			swipeLayout.setRefreshing(refreshing);
		}
	}

	private void setAdapterData(Cursor data) {
		if (adapter != null) {
			DebugLog.d("Adapter not null,swaping cursor..");
			adapter.swapCursor(data);
		}
	}
}
