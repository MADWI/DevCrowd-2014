package pl.devcrowd.app.adapters;

import java.util.ArrayList;

import pl.devcrowd.app.R;
import pl.devcrowd.app.R.id;
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
	
	public AgendatItemsAdapter(Context context, int resource, ArrayList<AgendaItem> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.resource = resource;
		this.items = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View rowView = (View) inflater.inflate(resource, parent, false);
		TextView hour = (TextView) rowView.findViewById(R.id.textItemHour);
		TextView topic = (TextView) rowView.findViewById(R.id.textItemTopic);
		TextView prelegent = (TextView) rowView.findViewById(R.id.textItemPrelegent);
		
		hour.setText(items.get(position).getHour());
		topic.setText(items.get(position).getTopic());
		prelegent.setText(items.get(position).getPrelegent());
		
		return rowView;
	}
}
