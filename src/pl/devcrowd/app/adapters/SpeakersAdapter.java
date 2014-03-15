package pl.devcrowd.app.adapters;

import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.models.Speaker;
import pl.devcrowd.app.overviews.RoundImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpeakersAdapter extends ArrayAdapter<Speaker> {

	private final Context context;
	private final int resource;
	private final List<Speaker> items;

	public SpeakersAdapter(Context context, int resource,
			List<Speaker> objects) {
		super(context, resource, objects);

		this.context = context;
		this.resource = resource;
		this.items = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		SpeakerItemHolder holder;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (View) inflater.inflate(resource, parent, false);

			holder = new SpeakerItemHolder();
			holder.name = (TextView) row
					.findViewById(R.id.textSpeaker);
			holder.description = (TextView) row
					.findViewById(R.id.textSpeakerDetails);
			holder.image = (RoundImageView) row
					.findViewById(R.id.imgLogoDevCrowd);
			row.setTag(holder);
		} else {
			holder = (SpeakerItemHolder) row.getTag();
		}

		holder.name.setText(items.get(position).getName());
		holder.description.setText(items.get(position).getDescription());
		holder.image.setImageResource(R.drawable.head_simple);

		return row;
	}

	private static class SpeakerItemHolder {
		public TextView name;
		public TextView description;
		public RoundImageView image;
	}
}
