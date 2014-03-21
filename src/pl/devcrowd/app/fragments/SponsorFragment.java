package pl.devcrowd.app.fragments;

import pl.devcrowd.app.R;
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
	
	private ImageView mad;
	
	private static final String consileonWebsite = "http://www.consileon.pl/pl/";
	private static final String BLStreamWebsite = "http://blstream.com/";
	private static final String cognifideWebsite = "http://www.cognifide.com/";
	private static final String softwareMillWebsite = "http://softwaremill.com/";
	private static final String osWorldWebsite = "http://osworld.pl/";
	private static final String infoludekWebsite = "http://www.infoludek.pl/";
	private static final String netcampWebsite = "http://www.netcamp.pl/";
	private static final String crossWebWebsite = "http://crossweb.pl/";
	private static final String klasterITWebsite = "http://www.klaster.it/pl/";
	private static final String technoParkWebsite = "http://www.technopark-pomerania.pl/pl/";
	private static final String wiZUTWebsite = "http://wi.zut.edu.pl/";
	private static final String szczecinJUGWebsite = "http://szjug.pl/";
	private static final String madWebsite = "http://www.mad.zut.edu.pl/";
	
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
		mad = (ImageView) view.findViewById(R.id.createdByMAD);
		
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
		mad.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.platiniumSponsorConsileon:
				openURL(consileonWebsite);
				break;
				
			case R.id.platiniumSponsorBLStream:
				openURL(BLStreamWebsite);
				break;
				
			case R.id.sponsorCognifide:
				openURL(cognifideWebsite);
				break;
				
			case R.id.sponsorSoftwareMill:
				openURL(softwareMillWebsite);
				break;
				
			case R.id.patronateOSWorld:
				openURL(osWorldWebsite);
				break;
				
			case R.id.patronateInfoludek:
				openURL(infoludekWebsite);
				break;
				
			case R.id.patronateNetCamp:
				openURL(netcampWebsite);
				break;
				
			case R.id.patronateCrossWeb:
				openURL(crossWebWebsite);
				break;
				
			case R.id.patronateKlasterIT:
				openURL(klasterITWebsite);
				break;
			
			case R.id.patronateTechnoPark:
				openURL(technoParkWebsite);
				break;
				
			case R.id.partnerWI:
				openURL(wiZUTWebsite);
				break;
				
			case R.id.partnerJUGSzczecin:
				openURL(szczecinJUGWebsite);
				break;
				
			case R.id.createdByMAD:
				openURL(madWebsite);
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
