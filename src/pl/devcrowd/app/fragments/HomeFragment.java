package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnClickListener,
		OnLongClickListener {

	public interface OnDevCrowdLogoClickListener {
		public void onDevCrowdLogoClick();
	}
	
	public interface OnDevCrowdLogoLongClickListener {
		public void onDevCrowdLogoLongClick(TextView viewTop, TextView viewBottom);
	}

	private ImageView imgLogoDevCrowd;
	private TextView tvTitle, tvSubtitle;
	private OnDevCrowdLogoClickListener onDevCrowdLogoClickListener;
	private OnDevCrowdLogoLongClickListener onDevCrowdLogoLongClickListener;

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
		tvTitle = (TextView) view.findViewById(R.id.txtDevCrowdTitle);
		tvSubtitle = (TextView) view.findViewById(R.id.txtDevCrowdSubtitle);
		imgLogoDevCrowd.setOnClickListener(this);
		imgLogoDevCrowd.setOnLongClickListener(this);

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
		try {
			onDevCrowdLogoLongClickListener = (OnDevCrowdLogoLongClickListener) activity;
		} catch (ClassCastException e) {
			String message = activity.toString()
					+ " must implement OnDevCrowdLogoLongClickListener";
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

	@Override
	public boolean onLongClick(View view) {
		if (view.getId() == R.id.imgLogoDevCrowd) {
			onDevCrowdLogoLongClickListener.onDevCrowdLogoLongClick(tvTitle,tvSubtitle);			
		}
		return true;
	}

}
