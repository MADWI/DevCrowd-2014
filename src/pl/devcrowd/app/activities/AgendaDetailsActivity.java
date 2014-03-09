package pl.devcrowd.app.activities;

import pl.devcrowd.app.R;
import pl.devcrowd.app.dialogs.RateDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
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

public class AgendaDetailsActivity extends ActionBarActivity 
						implements OnClickListener {

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
		setContentView(R.layout.agenda_details);
		
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
		rotateTo0(moreTopic, 0);

		textSpeaker = (TextView) findViewById(R.id.textSpeaker);
		textSpeakerDetails = (TextView) findViewById(R.id.textSpeakerDetails);
		moreSpeaker = (ImageView) findViewById(R.id.imageMoreSpeaker);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		//This will be change
		ratingBar.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					showRateDialog();
				}
				return true;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showRateDialog() {
		DialogFragment newFragment = RateDialog.newInstance();
		newFragment.show(getSupportFragmentManager(), "rate_dialog");
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
	
	private void toggleDetailsVisibility(View textDetails, View moreArrow) {
		if (textDetails.getVisibility() == View.VISIBLE) {
			textDetails.setVisibility(View.GONE);
			rotateTo180(moreArrow, 500);
		} else {
			setAnimation(textDetails,
					android.R.anim.fade_in, 500);
			textDetails.setVisibility(View.VISIBLE);
			rotateTo0(moreArrow, 400);
		}
	}
	
	private void setAnimation(View view, int animation, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				AgendaDetailsActivity.this, animation);
		anim.setDuration(duration);
		view.setAnimation(anim);
	}

	private void rotateTo0(View view, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				AgendaDetailsActivity.this, R.anim.rotate_to_0);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}

	private void rotateTo180(View view, int duration) {
		Animation anim = AnimationUtils.loadAnimation(
				AgendaDetailsActivity.this, R.anim.rotate_to_180);
		anim.setDuration(duration);
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}
}
