package pl.devcrowd.app.adapters;

import java.util.ArrayList;

import pl.devcrowd.app.R;
import pl.devcrowd.app.models.AgendaItem;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AgendatItemsAdapter extends ArrayAdapter<AgendaItem> {

	private Context context;
	private int resource;
	private ArrayList<AgendaItem> items;

	public AgendatItemsAdapter(Context context, int resource,
			ArrayList<AgendaItem> objects) {
		super(context, resource, objects);

		this.context = context;
		this.resource = resource;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		final AgendaItemHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (View) inflater.inflate(resource, parent, false);

			holder = new AgendaItemHolder();
			holder.textItemHour = (TextView) row
					.findViewById(R.id.textItemHour);
			holder.textItemTopic = (TextView) row
					.findViewById(R.id.textItemTopic);
			holder.textItemPrelegent = (TextView) row
					.findViewById(R.id.textItemPrelegent);
			row.setTag(holder);
		} else {
			holder = (AgendaItemHolder) row.getTag();
		}

		holder.textItemHour.setText(items.get(position).getHour());
		holder.textItemTopic.setText(items.get(position).getTopic());
		holder.textItemPrelegent.setText(items.get(position).getPrelegent());

		return row;
	}

	static class AgendaItemHolder {
		TextView textItemHour;
		TextView textItemTopic;
		TextView textItemPrelegent;
	}
}
