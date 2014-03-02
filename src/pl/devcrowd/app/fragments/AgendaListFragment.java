package pl.devcrowd.app.fragments;

import java.util.ArrayList;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.AgendaDetailsActivity;
import pl.devcrowd.app.adapters.AgendatItemsAdapter;
import pl.devcrowd.app.models.AgendaItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class AgendaListFragment extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(null);
		
		ArrayList<AgendaItem> array = new ArrayList<AgendaItem>();
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new AgendaItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));

		if (isAdded()) {
			AgendatItemsAdapter adapter = new AgendatItemsAdapter(
					getActivity(), R.layout.agenda_item_layout, array);
			setListAdapter(adapter);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (isAdded())
			startActivity(new Intent(getActivity(), AgendaDetailsActivity.class));
	}

}
