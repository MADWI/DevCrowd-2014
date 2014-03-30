package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HomeFragment extends Fragment implements OnClickListener {

	public interface OnDevCrowdLogoClickListener {
		public void onDevCrowdLogoClick();
	}

	private ImageView imgLogoDevCrowd;
	private OnDevCrowdLogoClickListener onDevCrowdLogoClickListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, container, false);

		imgLogoDevCrowd = (ImageView) view.findViewById(R.id.imgLogoDevCrowd);
		imgLogoDevCrowd.setOnClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onDevCrowdLogoClickListener = (OnDevCrowdLogoClickListener) activity;
		} catch (ClassCastException e) {
			String message = activity.toString()
					+ " must implement OnDevCrowdLogoClickListener";
			DebugLog.e(message);
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imgLogoDevCrowd) {
			onDevCrowdLogoClickListener.onDevCrowdLogoClick();

		}
	}

}
