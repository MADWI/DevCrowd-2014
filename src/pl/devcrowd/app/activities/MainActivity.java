package pl.devcrowd.app.activities;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevCrowdEGProvider;
import pl.devcrowd.app.db.DevCrowdEGProvider.OnDevCrowdProviderFinish;
import pl.devcrowd.app.dialogs.AboutDialog;
import pl.devcrowd.app.drawer.NavigationDrawerItem;
import pl.devcrowd.app.drawer.NavigationDrawerListAdapter;
import pl.devcrowd.app.fragments.FavouritesListFragment;
import pl.devcrowd.app.fragments.HomeFragment;
import pl.devcrowd.app.fragments.HomeFragment.OnDevCrowdLogoClickListener;
import pl.devcrowd.app.fragments.HomeFragment.OnDevCrowdLogoLongClickListener;
import pl.devcrowd.app.fragments.ScheduleHostFragment;
import pl.devcrowd.app.fragments.SponsorFragment;
import pl.devcrowd.app.utils.DebugLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnDevCrowdLogoClickListener,
		OnDevCrowdLogoLongClickListener, OnDevCrowdProviderFinish {
	private static final int DRAWER_HOME_NUM = 0;
	private static final int DRAWER_SCHEDULE_NUM = 1;
	private static final int DRAWER_FAVOURITES_NUM = 2;
	private static final int DRAWER_SPONSORS_NUM = 3;
	private static final int DRAWER_ABOUT_NUM = 4;
	private static final int NORMAL_MODE = 5;
	private static final int RAGE_MODE = 6;
	private static final int RESOURCE_NOT_DEFINED_INDEX = -1;
	private static final String SAVED_PREFS = "MySave";
	private static final String SAVED_LABEL = "MySaveLabel";
	private static final String ABOUT_DIALOG_TAG = "about_dialog";

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private DevCrowdEGProvider mDevcrowdProvider;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private List<NavigationDrawerItem> navigationDrawerItems;
	private SharedPreferences save;
	private NavigationDrawerListAdapter adapter;
	private String[] navigationMenuTitles;
	private TypedArray navigationMenuIcons;
	private TextView tvScore;
	private int lastPosition;
	private int mode,topCount,count=0;
	private boolean firstFragmentChange = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		save = getSharedPreferences(SAVED_PREFS, 0);
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
		mode = NORMAL_MODE;

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_navigation_drawer, R.string.app_name,

				R.string.app_name) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				if (mode == RAGE_MODE) {
					mDevcrowdProvider.cancelProviderAction();
					saveData();
				}
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
			fragment = new SponsorFragment();
			break;
		case DRAWER_ABOUT_NUM:
			AboutDialog.show(this, ABOUT_DIALOG_TAG);
			setSelection(lastPosition);
			return;
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.popBackStack(String.valueOf(position), FragmentManager.POP_BACK_STACK_INCLUSIVE);
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.frame_container, fragment);
			if (!firstFragmentChange) {
				fragmentTransaction.addToBackStack(String.valueOf(position));
			}
			fragmentTransaction.commit();
			firstFragmentChange = false;
			setSelection(position);
		} else {
			DebugLog.e("Error in creating fragment");
		}
	}

	private void setSelection(int position) {
		mDrawerList.setItemChecked(position, true);
		mDrawerList.setSelection(position);
		setTitle(navigationMenuTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
		lastPosition = position;

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
		if(mDevcrowdProvider!=null) {
			mDevcrowdProvider.cancel();
			mode = NORMAL_MODE;
		}
	}

	private BroadcastReceiver reciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager connectivityManager = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE));
			NetworkInfo currentNetworkInfo = connectivityManager
					.getActiveNetworkInfo();

			RelativeLayout infoLayout = (RelativeLayout) findViewById(R.id.infoLayout);

			if (currentNetworkInfo != null && currentNetworkInfo.isConnected()) {
				infoLayout.setVisibility(View.GONE);
			} else {
				infoLayout.setVisibility(View.VISIBLE);
			}
		}

	};

	@Override
	public void onStart() {
		super.onStart();
		registerReceiver(reciever, new IntentFilter(
				"android.net.conn.CONNECTIVITY_CHANGE"));
	}

	@Override
	public void onStop() {
		super.onStop();
		unregisterReceiver(reciever);
	}

	@Override
	public void onDevCrowdLogoClick() {
		if(mode != RAGE_MODE)
			displayView(DRAWER_SCHEDULE_NUM);
		else
			count++;
	}

	@Override
	public void onDevCrowdLogoLongClick(TextView viewTop, TextView viewBottom) {
		mDevcrowdProvider = new DevCrowdEGProvider(viewTop, viewBottom, this);
		mDevcrowdProvider.setCurrentYearValue();
		tvScore = viewBottom;
		getData();
		mode = RAGE_MODE;
	}
	
	private void saveData() {
		Toast.makeText(this, "Wynik: " + count, Toast.LENGTH_SHORT).show();
		if (count > topCount) {
			SharedPreferences.Editor editor = save.edit();
			editor.putInt(SAVED_LABEL, count);
			editor.apply();
		}
		mode = NORMAL_MODE;
		count = 0;
	}
	
	private void getData() {
		topCount = save.getInt(SAVED_LABEL, 0);
		tvScore.setText("Top score: " + topCount);
	}

	@Override
	public void onDevCrowdProviderFinish() {
		saveData();
	}
}