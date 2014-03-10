package pl.devcrowd.app.activities;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.dialogs.AboutDialog;
import pl.devcrowd.app.drawer.NavigationDrawerItem;
import pl.devcrowd.app.drawer.NavigationDrawerListAdapter;
import pl.devcrowd.app.fragments.FavouritesListFragment;
import pl.devcrowd.app.fragments.HomeFragment;
import pl.devcrowd.app.fragments.ScheduleHostFragment;
import pl.devcrowd.app.services.ApiService;
import android.content.Intent;
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
	private static final int DRAWER_SCHEDULE_NUM = 1;
	private static final int DRAWER_FAVOURITES_NUM = 2;
	private static final int DRAWER_SPONSORS_NUM = 3;
	private static final int DRAWER_ABOUT_NUM = 4;
	private static final int RESOURCE_NOT_DEFINED_INDEX = -1;
	private static final String ABOUT_DIALOG_TAG = "about_dialog";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private List<NavigationDrawerItem> navigationDrawerItems;
	private NavigationDrawerListAdapter adapter;
	private String[] navigationMenuTitles;
	private TypedArray navigationMenuIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		initNavigationDrawer();

		if (savedInstanceState == null) {
			displayView(DRAWER_HOME_NUM);
		}
		


	}

	private void initNavigationDrawer() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navigationMenuTitles = getResources().getStringArray(
				R.array.navigation_drawer_items);
		navigationMenuIcons = getResources().obtainTypedArray(
				R.array.navigation_drawer_icons);
		navigationDrawerItems = (ArrayList<NavigationDrawerItem>) populateNavigationDrawer();
		navigationMenuIcons.recycle();

		adapter = new NavigationDrawerListAdapter(getApplicationContext(),
				navigationDrawerItems);
		mDrawerList.setAdapter(adapter);

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
		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
	}

	private List<NavigationDrawerItem> populateNavigationDrawer() {
		List<NavigationDrawerItem> drawerItems = new ArrayList<NavigationDrawerItem>();
		addNavigationDrawerItem(DRAWER_HOME_NUM, drawerItems);
		addNavigationDrawerItem(DRAWER_SCHEDULE_NUM, drawerItems);
		addNavigationDrawerItem(DRAWER_FAVOURITES_NUM, drawerItems);
		addNavigationDrawerItem(DRAWER_SPONSORS_NUM, drawerItems);
		addNavigationDrawerItem(DRAWER_ABOUT_NUM, drawerItems);
		return drawerItems;
	}

	private void addNavigationDrawerItem(int itemNumber,
			List<NavigationDrawerItem> drawerItems) {
		drawerItems
				.add(new NavigationDrawerItem(navigationMenuTitles[itemNumber],
						navigationMenuIcons.getResourceId(itemNumber,
								RESOURCE_NOT_DEFINED_INDEX)));
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
		boolean stopProcessingMenu = false;
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			stopProcessingMenu = true;
		}
		return stopProcessingMenu;
	}

	private void displayView(int position) {
		Fragment fragment = null;
		switch (position) {
		case DRAWER_HOME_NUM:
			fragment = new HomeFragment();
			break;
		case DRAWER_SCHEDULE_NUM:
			fragment = new ScheduleHostFragment();
			break;
		case DRAWER_FAVOURITES_NUM:
			fragment = new FavouritesListFragment();
			break;
		case DRAWER_SPONSORS_NUM:
			break;
		case DRAWER_ABOUT_NUM:
			DialogFragment newFragment = AboutDialog.newInstance();
			newFragment.show(getSupportFragmentManager(), ABOUT_DIALOG_TAG);

			return;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);
			fragmentTransaction.commit();
			setSelection(position);

		} else {
			Log.e(TAG, "Error in creating fragment");
		}
	}

	private void setSelection(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navigationMenuTitles[position]);
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
