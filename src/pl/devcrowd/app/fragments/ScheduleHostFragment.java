package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ScheduleHostFragment extends Fragment {
	private FragmentTabHost mTabHost;

	private static final String TEMP_ROOM_NAME_1 = "Sala 126";
	private static final String TEMP_ROOM_NAME_2 = "Sala 226";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mTabHost = new FragmentTabHost(getActivity());

		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.frame_container);

		mTabHost.addTab(mTabHost.newTabSpec(TEMP_ROOM_NAME_1)
				.setIndicator(TEMP_ROOM_NAME_1), ScheduleListFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec(TEMP_ROOM_NAME_2)
				.setIndicator(TEMP_ROOM_NAME_2), ScheduleListFragment.class, null);
		return mTabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}

}
