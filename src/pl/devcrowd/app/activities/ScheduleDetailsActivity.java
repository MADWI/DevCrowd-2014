package pl.devcrowd.app.activities;

import java.util.ArrayList;
import java.util.List;

import pl.devcrowd.app.R;
import pl.devcrowd.app.adapters.SpeakersAdapter;
import pl.devcrowd.app.dialogs.RateDialog;
import pl.devcrowd.app.dialogs.RateDialog.OnRatingListener;
import pl.devcrowd.app.models.Speaker;
import pl.devcrowd.app.services.ApiService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleDetailsActivity extends ActionBarActivity implements
		OnClickListener, View.OnTouchListener, OnRatingListener {

	private static final int TEXT_DURATION_TIME_MS = 500;
	private static final int ARROW_DURATION_TIME_MS = 400;
	private static final int ZERO_DURATION_TIME_MS = 0;
	private static final String RATE_DIALOG_TAG = "rate_dialog";

	private RelativeLayout topicCard;
	private ListView speakerList;
	private RelativeLayout rateCard;

	private TextView textTopic;
	private TextView textHour;
	private TextView textTopicDetails;
	private ImageView moreTopic;

	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_details);

		getSupportActionBar().setHomeButtonEnabled(true);
		initUIElements();
		List<Speaker> speakers = new ArrayList<Speaker>();
		speakers.add(new Speaker("Jan\nKowalski", "Opis pelegenta...",
				"http://..."));
		speakers.add(new Speaker("Jan\nKowalski", "Opis pelegenta...",
				"http://..."));
		speakers.add(new Speaker("Jan\nKowalski", "Opis pelegenta...",
				"http://..."));
		SpeakersAdapter adapter = new SpeakersAdapter(this,
				R.layout.speaker_item, speakers);
		speakerList.setAdapter(adapter);
	}

	private void initUIElements() {
		topicCard = (RelativeLayout) findViewById(R.id.topicCard);
		topicCard.setOnClickListener(this);
		speakerList = (ListView) findViewById(R.id.speakersList);
		rateCard = (RelativeLayout) findViewById(R.id.rateCard);

		textTopic = (TextView) findViewById(R.id.textTopic);
		textHour = (TextView) findViewById(R.id.textHour);
		textTopicDetails = (TextView) findViewById(R.id.textTopicDetails);
		moreTopic = (ImageView) findViewById(R.id.imageMoreTopic);
		rotateTo0(moreTopic, ZERO_DURATION_TIME_MS);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		ratingBar.setOnTouchListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		overridePendingTransition(R.anim.slide_right_enter,
				R.anim.slide_right_exit);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showRateDialog() {
		DialogFragment newFragment = RateDialog.newInstance();
		newFragment.show(getSupportFragmentManager(), RATE_DIALOG_TAG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topicCard:
			toggleDetailsVisibility(textTopicDetails, moreTopic);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			showRateDialog();
		}
		return true;
	}

	private void toggleDetailsVisibility(View textDetails, View moreArrow) {
		if (textDetails.getVisibility() == View.VISIBLE) {
			textDetails.setVisibility(View.GONE);
			rotateTo180(moreArrow, TEXT_DURATION_TIME_MS);
		} else {
			setAnimation(textDetails, android.R.anim.fade_in,
					TEXT_DURATION_TIME_MS);
			textDetails.setVisibility(View.VISIBLE);
			rotateTo0(moreArrow, ARROW_DURATION_TIME_MS);
		}
	}

	private void setAnimation(View view, int animation, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				ScheduleDetailsActivity.this, animation);
		anim.setDuration(duration);
		view.setAnimation(anim);
	}

	private void rotateTo0(View view, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				ScheduleDetailsActivity.this, R.anim.rotate_to_0);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}

	private void rotateTo180(View view, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				ScheduleDetailsActivity.this, R.anim.rotate_to_180);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}

	@Override
	public void onSendRatingButtonClick(float topicGrade, float overallGrade,
			String email) {
		ratingBar.setRating((topicGrade + overallGrade) / 2);
		
		// TEST PRESENTATION RATE
		String presentationTitle = "Some title"; //title of current presentation
		asyncRatePresentation(presentationTitle, topicGrade, overallGrade,email);
		//-----------------------		
	}
	
	private void asyncRatePresentation(String presentationTitle, float topicGrade,
			float speakerGrade, String email) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiService.ACTION_RATE_PRESENTATION);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_TITLE, presentationTitle);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_TOPIC_GRADE,topicGrade);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_SPEAKER_GRADE,speakerGrade);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_EMAIL,email);
		startService(intent);
	}
	
}
