package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.services.ApiService;
import pl.devcrowd.app.utils.DebugLog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class FavouritesListFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final int LOADER_ID = 1;
	private static final int NO_FLAGS = 0;
	private static final String IS_FAVOURITE = "ok";
	private SimpleCursorAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (isAdded()) {
			startActivity(new Intent(getActivity(),
					ScheduleDetailsActivity.class));
		}
	}
	
	private void fillData() {
		// Fields from the database (projection)
		// Must include the _id column for the adapter to work
		String[] from = new String[] { DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.SPEAKER_COLUMN_NAME,
				DevcrowdTables.TABLE_PRESENTATIONS + "." + DevcrowdTables.PRESENTATION_ID };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.textFavoItemTopic, R.id.textFavoItemHour, R.id.textFavoItemSpeaker };

		getLoaderManager().initLoader(LOADER_ID, null, this);

		adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.favo_item, null, from, to, NO_FLAGS);

		setListAdapter(adapter);

	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { DevcrowdTables.PRESENTATION_TITLE,
				DevcrowdTables.TABLE_PRESENTATIONS + "." + DevcrowdTables.PRESENTATION_ID,
				DevcrowdTables.PRESENTATION_START,
				DevcrowdTables.SPEAKER_COLUMN_NAME,
				DevcrowdTables.PRESENTATION_FAVOURITE};
		CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
				DevcrowdContentProvider.CONTENT_URI_JOIN, projection,
				DevcrowdTables.PRESENTATION_FAVOURITE + " =? ",
				new String[] { IS_FAVOURITE },
				DevcrowdTables.TABLE_PRESENTATIONS + "."
						+ DevcrowdTables.PRESENTATION_ID + " DESC");
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
}
