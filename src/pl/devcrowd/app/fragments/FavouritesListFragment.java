package pl.devcrowd.app.fragments;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.adapters.FavoItemsAdapter;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.models.ScheduleItem;
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
	/*implements LoaderManager.LoaderCallbacks<Cursor>*/{
	
	private FavoItemsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(null);
		getListView().setBackgroundResource(R.drawable.background_gradient);

		List<ScheduleItem> array = new ArrayList<ScheduleItem>();
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Ulubiony temat",
				"Jan Kowalski"));

		if (isAdded()) {
			FavoItemsAdapter adapter = new FavoItemsAdapter(getActivity(),
					R.layout.favo_item, array);
			setListAdapter(adapter);
		}
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
				DevcrowdTables.PRESENTATION_ROOM,
				DevcrowdTables.PRESENTATION_ID };
		// Fields on the UI to which we map
		int[] to = new int[] { R.id.textFavoItemTopic, R.id.textFavoRoom };

//		getLoaderManager().initLoader(0, null, this);

//		adapter = new SimpleCursorAdapter(getActivity(),
//				R.layout.favo_item, null, from, to, 0);
//
//		lista.setAdapter(adapter);
	}
	
//	@Override
//	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//		String[] projection = { DevcrowdTables.PRESENTATION_TITLE,
//				DevcrowdTables.PRESENTATION_ROOM,
//				DevcrowdTables.PRESENTATION_ID,
//				DevcrowdTables.PRESENTATION_FAVOURITE};
//		CursorLoader cursorLoader = new CursorLoader(this.getActivity(),
//				DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, projection, DevcrowdTables.PRESENTATION_FAVOURITE + "=?", new String[]{"ulubione"},
//				DevcrowdTables.PRESENTATION_ID + " DESC");
//		return cursorLoader;
//	}
//
//	@Override
//	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//		adapter.swapCursor(data);
//	}
//
//	@Override
//	public void onLoaderReset(Loader<Cursor> loader) {
//		// data is not available anymore, delete reference
//		adapter.swapCursor(null);
//	}
}
