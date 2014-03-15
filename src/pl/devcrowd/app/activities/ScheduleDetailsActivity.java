package pl.devcrowd.app.activities;

import pl.devcrowd.app.R;
import pl.devcrowd.app.dialogs.RateDialog;
import pl.devcrowd.app.http.HttpPostData;
import pl.devcrowd.app.interfaces.RatingCallback;
import pl.devcrowd.app.models.Presentation;
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
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleDetailsActivity extends ActionBarActivity implements
		OnClickListener, View.OnTouchListener, RatingCallback {

	private static final int TEXT_DURATION_TIME_MS = 500;
	private static final int ARROW_DURATION_TIME_MS = 400;
	private static final int ZERO_DURATION_TIME_MS = 0;
	private static final String RATE_DIALOG_TAG = "rate_dialog";

	private RelativeLayout topicCard;
	private RelativeLayout speakerCard;
	private RelativeLayout rateCard;

	private TextView textTopic;
	private TextView textHour;
	private TextView textTopicDetails;
	private ImageView moreTopic;

	private TextView textSpeaker;
	private TextView textSpeakerDetails;
	private ImageView moreSpeaker;

	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_details);

		getSupportActionBar().setHomeButtonEnabled(true);
		initUIElements();

	}

	private void initUIElements() {
		topicCard = (RelativeLayout) findViewById(R.id.topicCard);
		topicCard.setOnClickListener(this);
		speakerCard = (RelativeLayout) findViewById(R.id.speakerCard);
		speakerCard.setOnClickListener(this);
		rateCard = (RelativeLayout) findViewById(R.id.rateCard);

		textTopic = (TextView) findViewById(R.id.textTopic);
		textHour = (TextView) findViewById(R.id.textHour);
		textTopicDetails = (TextView) findViewById(R.id.textTopicDetails);
		moreTopic = (ImageView) findViewById(R.id.imageMoreTopic);
		rotateTo0(moreTopic, ZERO_DURATION_TIME_MS);

		textSpeaker = (TextView) findViewById(R.id.textSpeaker);
		textSpeakerDetails = (TextView) findViewById(R.id.textSpeakerDetails);
		moreSpeaker = (ImageView) findViewById(R.id.imageMoreSpeaker);

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
		case R.id.speakerCard:
			toggleDetailsVisibility(textSpeakerDetails, moreSpeaker);
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
	public void userGrades(float topic_grade, float overall_grade) {
		ratingBar.setRating((topic_grade + overall_grade) / 2);
		// TODO
		// pass Presentation object to update
		// probably usage method: updatePresentation(getContentRsolver(),
		// presentation.getTitle(), presentation);

		//test object
		Presentation presentation = new Presentation("10:00", "10:30", "126", "", "testowanie", "Test", String.valueOf(topic_grade), String.valueOf(overall_grade), "0");
		// sending grades to API
		new HttpPostData(this,"add_grades", topic_grade, overall_grade, presentation,
				"dawidglinski@testmad.pl").execute();
	}
}
