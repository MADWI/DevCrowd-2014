package pl.devcrowd.app.drawer;

import java.util.List;

import pl.devcrowd.app.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NavigationDrawerListAdapter extends BaseAdapter {

	private final Context context;
	private final List<NavigationDrawerItem> navigationDrawerItems;

	public NavigationDrawerListAdapter(final Context context,
			final List<NavigationDrawerItem> navigationDrawerItems) {
		super();
		this.context = context;
		this.navigationDrawerItems = navigationDrawerItems;
	}

	@Override
	public int getCount() {
		return navigationDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navigationDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View createdView;
		if (convertView == null) {
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			createdView = mInflater.inflate(R.layout.drawer_item, null);
		} else {
			createdView = convertView;
		}
		
		if (((ListView) parent).isItemChecked(position))
			createdView.setBackgroundResource(R.drawable.drawer_item_background_pressed);
		else
			createdView.setBackgroundResource(android.R.color.transparent);
		
		ImageView imgIcon = (ImageView) createdView.findViewById(R.id.icon);
		TextView txtTitle = (TextView) createdView.findViewById(R.id.title);

		imgIcon.setImageResource(navigationDrawerItems.get(position).getIcon());
		txtTitle.setText(navigationDrawerItems.get(position).getTitle());

		return createdView;
	}

}
