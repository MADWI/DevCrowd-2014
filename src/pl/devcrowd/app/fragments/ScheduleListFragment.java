package pl.devcrowd.app.fragments;

import java.util.ArrayList;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.ScheduleDetailsActivity;
import pl.devcrowd.app.adapters.ScheduleItemsAdapter;
import pl.devcrowd.app.models.ScheduleItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class ScheduleListFragment extends ListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(null);

		ArrayList<ScheduleItem> array = new ArrayList<ScheduleItem>();
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));
		array.add(new ScheduleItem("10.15\n11.15", "Przyk³adowy temat",
				"Jan Kowalski"));

		if (isAdded()) {
			ScheduleItemsAdapter adapter = new ScheduleItemsAdapter(
					getActivity(), R.layout.schedule_item, array);
			setListAdapter(adapter);
		}		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (isAdded()) {
			startActivity(new Intent(getActivity(), ScheduleDetailsActivity.class));
			getActivity().overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
		}
	}

}
