package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
import pl.devcrowd.app.activities.MainActivity;
import pl.devcrowd.app.utils.DebugLog;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SponsorFragment extends Fragment implements OnClickListener{
	
	private ImageView consileon;
	private ImageView blstream;

	private ImageView cognifide;
	private ImageView softwareMill;
	
	private ImageView osWorld;
	private ImageView infoludek;
	private ImageView netCamp;
	private ImageView crossWeb;
	private ImageView klasterIT;
	private ImageView technoPark;
	
	private ImageView wiZUT;
	private ImageView szczecinJUG;
	private ImageView fourDevelopers;
	private OnShowFragment onShowFragment;
	private int fragmentPosition;
	
	private static final String CONSILEON_WEBSITE = "http://www.consileon.pl/pl/";
	private static final String BLSTREAM_WEBSITE = "http://blstream.com/";
	private static final String COGNIFIDE_WEBSITE = "http://www.cognifide.com/";
	private static final String SOFTWAREMILL_WEBSITE = "http://softwaremill.com/";
	private static final String OSWORLD_WEBSITE = "http://osworld.pl/";
	private static final String INFOLUDEK_WEBSITE = "http://www.infoludek.pl/";
	private static final String NETCAMP_WEBSITE = "http://www.netcamp.pl/";
	private static final String CROSSWEB_WEBSITE = "http://crossweb.pl/";
	private static final String KLASTERIT_WEBSITE = "http://www.klaster.it/pl/";
	private static final String TECHNOPARK_WEBSITE = "http://www.technopark-pomerania.pl/pl/";
	private static final String WIZUT_WEBSITE = "http://wi.zut.edu.pl/";
	private static final String SZCZECINJUG_WEBSITE = "http://szjug.pl/";
	private static final String FOUR_DEVELOPERS_WEBSITE = "http://2014.4developers.org.pl/pl/";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sponsors, container, false);
		
		consileon = (ImageView) view.findViewById(R.id.platiniumSponsorConsileon);
		blstream = (ImageView) view.findViewById(R.id.platiniumSponsorBLStream);
		cognifide = (ImageView) view.findViewById(R.id.sponsorCognifide);
		softwareMill = (ImageView) view.findViewById(R.id.sponsorSoftwareMill);
		osWorld = (ImageView) view.findViewById(R.id.patronateOSWorld);
		infoludek = (ImageView) view.findViewById(R.id.patronateInfoludek);
		netCamp = (ImageView) view.findViewById(R.id.patronateNetCamp);
		crossWeb = (ImageView) view.findViewById(R.id.patronateCrossWeb);
		klasterIT = (ImageView) view.findViewById(R.id.patronateKlasterIT);
		technoPark = (ImageView) view.findViewById(R.id.patronateTechnoPark);
		wiZUT = (ImageView) view.findViewById(R.id.partnerWI);
		szczecinJUG = (ImageView) view.findViewById(R.id.partnerJUGSzczecin);
		fourDevelopers = (ImageView) view.findViewById(R.id.partner4Developers);
		
		consileon.setOnClickListener(this);
		blstream.setOnClickListener(this);
		cognifide.setOnClickListener(this);
		softwareMill.setOnClickListener(this);
		osWorld.setOnClickListener(this);
		infoludek.setOnClickListener(this);
		netCamp.setOnClickListener(this);
		crossWeb.setOnClickListener(this);
		klasterIT.setOnClickListener(this);
		technoPark.setOnClickListener(this);
		wiZUT.setOnClickListener(this);
		szczecinJUG.setOnClickListener(this);
		fourDevelopers.setOnClickListener(this);
		
		fragmentPosition = getArguments().getInt(
				MainActivity.FRAGMENT_DRAWER_POSITION);
		
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			onShowFragment = (OnShowFragment) activity;
		} catch (ClassCastException e) {
			String message = activity.toString()
					+ " must implement OnShowFragment";
			DebugLog.e(message);
			throw new IllegalArgumentException(message);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		onShowFragment.onFragmentShowed(fragmentPosition);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.platiniumSponsorConsileon:
				openURL(CONSILEON_WEBSITE);
				break;
				
			case R.id.platiniumSponsorBLStream:
				openURL(BLSTREAM_WEBSITE);
				break;
				
			case R.id.sponsorCognifide:
				openURL(COGNIFIDE_WEBSITE);
				break;
				
			case R.id.sponsorSoftwareMill:
				openURL(SOFTWAREMILL_WEBSITE);
				break;
				
			case R.id.patronateOSWorld:
				openURL(OSWORLD_WEBSITE);
				break;
				
			case R.id.patronateInfoludek:
				openURL(INFOLUDEK_WEBSITE);
				break;
				
			case R.id.patronateNetCamp:
				openURL(NETCAMP_WEBSITE);
				break;
				
			case R.id.patronateCrossWeb:
				openURL(CROSSWEB_WEBSITE);
				break;
				
			case R.id.patronateKlasterIT:
				openURL(KLASTERIT_WEBSITE);
				break;
			
			case R.id.patronateTechnoPark:
				openURL(TECHNOPARK_WEBSITE);
				break;
				
			case R.id.partnerWI:
				openURL(WIZUT_WEBSITE);
				break;
				
			case R.id.partnerJUGSzczecin:
				openURL(SZCZECINJUG_WEBSITE);
				break;
				
			case R.id.partner4Developers:
				openURL(FOUR_DEVELOPERS_WEBSITE);
				break;
				
				default:
					break;
		}
	}
	
	private void openURL(String url){
		Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl).addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(launchBrowser);
	}
}
