package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AgendaHostFragment extends Fragment {
	private FragmentTabHost mTabHost;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mTabHost = new FragmentTabHost(getActivity());

		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.frame_container);

		mTabHost.addTab(mTabHost.newTabSpec("sala_126")
				.setIndicator("Sala 126"), AgendaListFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("sala_226")
				.setIndicator("Sala 226"), AgendaListFragment.class, null);
		return mTabHost;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}

}
