package pl.devcrowd.app.adapters;

import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.models.ScheduleItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ScheduleItemsAdapter extends ArrayAdapter<ScheduleItem> {

	private final Context context;
	private final int resource;
	private final List<ScheduleItem> items;

	public ScheduleItemsAdapter(Context context, int resource,
			List<ScheduleItem> objects) {
		super(context, resource, objects);

		this.context = context;
		this.resource = resource;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		ScheduleItemHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (View) inflater.inflate(resource, parent, false);

			holder = new ScheduleItemHolder();
			holder.textItemHour = (TextView) row
					.findViewById(R.id.textItemHour);
			holder.textItemTopic = (TextView) row
					.findViewById(R.id.textItemTopic);
			holder.textItemSpeaker = (TextView) row
					.findViewById(R.id.textItemSpeaker);
			row.setTag(holder);
		} else {
			holder = (ScheduleItemHolder) row.getTag();
		}

		holder.textItemHour.setText(items.get(position).getHour());
		holder.textItemTopic.setText(items.get(position).getTopic());
		holder.textItemSpeaker.setText(items.get(position).getSpeaker());

		return row;
	}

	private static class ScheduleItemHolder {
		public TextView textItemHour;
		public TextView textItemTopic;
		public TextView textItemSpeaker;
	}
}
