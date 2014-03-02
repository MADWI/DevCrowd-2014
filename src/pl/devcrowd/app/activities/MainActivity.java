package pl.devcrowd.app.activities;

import java.util.ArrayList;

import pl.devcrowd.app.R;
import pl.devcrowd.app.dialogs.AboutDialog;
import pl.devcrowd.app.drawer.NavDrawerItem;
import pl.devcrowd.app.drawer.NavDrawerListAdapter;
import pl.devcrowd.app.fragments.AgendaHostFragment;
import pl.devcrowd.app.fragments.FavouritesListFragment;
import pl.devcrowd.app.fragments.HomeFragment;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private static final int DRAWER_HOME_NUM = 0;
	private static final int DRAWER_AGENDA_NUM = 1;
	private static final int DRAWER_FAVOURITES_NUM = 2;
	private static final int DRAWER_SPONSORS_NUM = 3;
	private static final int DRAWER_ABOUT_NUM = 4;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[DRAWER_HOME_NUM],
				navMenuIcons.getResourceId(DRAWER_HOME_NUM, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[DRAWER_AGENDA_NUM],
				navMenuIcons.getResourceId(DRAWER_AGENDA_NUM, -1)));
		navDrawerItems.add(new NavDrawerItem(
				navMenuTitles[DRAWER_FAVOURITES_NUM], navMenuIcons
						.getResourceId(DRAWER_FAVOURITES_NUM, -1)));
		navDrawerItems.add(new NavDrawerItem(
				navMenuTitles[DRAWER_SPONSORS_NUM], navMenuIcons.getResourceId(
						DRAWER_SPONSORS_NUM, -1)));
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[DRAWER_ABOUT_NUM],
				navMenuIcons.getResourceId(DRAWER_ABOUT_NUM, -1)));
		navMenuIcons.recycle();

		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.app_name,

				R.string.app_name) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			displayView(0);
		}

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return true;
	}

	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case DRAWER_HOME_NUM:
			fragment = new HomeFragment();
			break;
		case DRAWER_AGENDA_NUM:
			fragment = new AgendaHostFragment();
			break;
		case DRAWER_FAVOURITES_NUM:
			fragment = new FavouritesListFragment();
			break;
		case DRAWER_SPONSORS_NUM:

			break;
		case DRAWER_ABOUT_NUM:
			DialogFragment newFragment = AboutDialog.newInstance();
			newFragment.show(getSupportFragmentManager(), "rate_dialog");

			return;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);

			// fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
			setSelection(position);

		} else {
			Log.e(TAG, "Error in creating fragment");
		}
	}

	private void setSelection(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
