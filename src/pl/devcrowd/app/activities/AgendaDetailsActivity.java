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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AgendaDetailsActivity extends ActionBarActivity {

	private RelativeLayout topicCard;
	private RelativeLayout prelegentCard;
	private RelativeLayout rateCard;

	private TextView textTopic;
	private TextView textHour;
	private TextView textTopicDetails;
	private ImageView moreTopic;

	private TextView textPrelegent;
	private TextView textPrelegentDetails;
	private ImageView morePreleg;

	private RatingBar ratingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agenda_details);

		getSupportActionBar().setHomeButtonEnabled(true);

		topicCard = (RelativeLayout) findViewById(R.id.topicCard);
		prelegentCard = (RelativeLayout) findViewById(R.id.prelegentCard);
		rateCard = (RelativeLayout) findViewById(R.id.rateCard);

		textTopic = (TextView) findViewById(R.id.textTopic);
		textHour = (TextView) findViewById(R.id.textHour);
		textTopicDetails = (TextView) findViewById(R.id.textTopicDetails);
		moreTopic = (ImageView) findViewById(R.id.imageMoreTopic);
		rotateTo0(moreTopic, 0);

		textPrelegent = (TextView) findViewById(R.id.textPrelegent);
		textPrelegentDetails = (TextView) findViewById(R.id.textPrelegentDetails);
		morePreleg = (ImageView) findViewById(R.id.imageMorePreleg);

		ratingBar = (RatingBar) findViewById(R.id.ratingBar);

		topicCard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (textTopicDetails.getVisibility() == View.GONE) {
							setAnimation(textTopicDetails,
									android.R.anim.fade_in, 400);
							textTopicDetails.setVisibility(View.VISIBLE);
							rotateTo0(moreTopic, 400);

						} else if (textTopicDetails.getVisibility() == View.VISIBLE) {
							textTopicDetails.setVisibility(View.GONE);
							rotateTo180(moreTopic, 400);
						}
					}
				});
			}
		});

		prelegentCard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (textPrelegentDetails.getVisibility() == View.GONE) {
							setAnimation(textPrelegentDetails,
									android.R.anim.fade_in, 400);
							textPrelegentDetails.setVisibility(View.VISIBLE);
							rotateTo0(morePreleg, 400);
						} else if (textPrelegentDetails.getVisibility() == View.VISIBLE) {
							textPrelegentDetails.setVisibility(View.GONE);
							rotateTo180(morePreleg, 400);
						}
					}
				});

			}
		});

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
}
