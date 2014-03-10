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

public class FavoItemsAdapter extends ArrayAdapter<ScheduleItem> {

	private Context context;
	private int resource;
	private List<ScheduleItem> items;

	public FavoItemsAdapter(Context context, int resource,
			List<ScheduleItem> objects) {
		super(context, resource, objects);

		this.context = context;
		this.resource = resource;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		FavoItemHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (View) inflater.inflate(resource, parent, false);

			holder = new FavoItemHolder();
			holder.textFavoItemHour = (TextView) row
					.findViewById(R.id.textFavoItemHour);
			holder.textFavoItemTopic = (TextView) row
					.findViewById(R.id.textFavoItemTopic);
			holder.textFavoItemSpeaker = (TextView) row
					.findViewById(R.id.textFavoItemSpeaker);

			holder.textFavoRoom = (TextView) row
					.findViewById(R.id.textFavoRoom);
			row.setTag(holder);
		} else {
			holder = (FavoItemHolder) row.getTag();
		}

		holder.textFavoItemHour.setText(items.get(position).getHour());
		holder.textFavoItemTopic.setText(items.get(position).getTopic());
		holder.textFavoItemSpeaker.setText(items.get(position).getSpeaker());

		return row;
	}

	private static class FavoItemHolder {
		public TextView textFavoItemHour;
		public TextView textFavoItemTopic;
		public TextView textFavoItemSpeaker;
		public TextView textFavoRoom;
	}
}
