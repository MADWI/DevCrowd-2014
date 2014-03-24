package pl.devcrowd.app.activities;

import com.applidium.shutterbug.utils.ShutterbugManager;
import com.applidium.shutterbug.utils.ShutterbugManager.ShutterbugManagerListener;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.dialogs.RateDialog;
import pl.devcrowd.app.dialogs.RateDialog.OnRatingListener;
import pl.devcrowd.app.overviews.RoundImageView;
import pl.devcrowd.app.services.ApiService;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
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
		OnRatingListener {

	private static final int TEXT_DURATION_TIME_MS = 500;
	private static final int ARROW_DURATION_TIME_MS = 400;
	private static final int ZERO_DURATION_TIME_MS = 0;
	private static final String RATE_DIALOG_TAG = "rate_dialog";

	private RelativeLayout rootView;
	private int viewId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_details);
		
		ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.titlebar_background_gradient));
		int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
		TextView title = (TextView) findViewById(titleId);
		title.setTextColor(Color.WHITE);
		
		getSupportActionBar().setHomeButtonEnabled(true);

		Bundle extras = getIntent().getExtras();

		rootView = (RelativeLayout) findViewById(R.id.rootView);
		fillPresentationData(extras.getString(DevcrowdTables.PRESENTATION_ID));
		fillSpeakersData(extras.getString(DevcrowdTables.PRESENTATION_ID));
		/*
		 * TODO Need to add function to fill speakers data
		 */
		rootView.addView(createRatingCardView());

	}

	private void fillPresentationData(String id) {
		Cursor cursor = getCursor();
		if (cursor == null) {
			return;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {
				break;
			}
		}

		rootView.addView(createTopicCardView(
				cursor.getString(cursor
						.getColumnIndex(DevcrowdTables.PRESENTATION_TITLE)),
				getString(R.string.hour)
						+ " "
						+ cursor.getString(cursor
								.getColumnIndex(DevcrowdTables.PRESENTATION_START))
						+ " - "
						+ cursor.getString(cursor
								.getColumnIndex(DevcrowdTables.PRESENTATION_END)),
				cursor.getString(cursor
						.getColumnIndex(DevcrowdTables.PRESENTATION_DESCRIPTION))));
	}

	private void fillSpeakersData(String id) {
		Cursor cursor = getCursor();
		if (cursor == null) {
			return;
		}

		rootView.addView(createSperakerCardView(
				"http://2014.devcrowd.pl/wp-content/themes/business/images/marek_defecinski.jpg",
				"...", "..."));
		
		rootView.addView(createSperakerCardView(
				"http://2014.devcrowd.pl/wp-content/themes/business/images/marek_defecinski.jpg",
				"...", "..."));
		
		rootView.addView(createSperakerCardView(
				"http://2014.devcrowd.pl/wp-content/themes/business/images/marta_owczarczak.jpg",
				"...", "..."));
	}

	private Cursor getCursor() {
		return getContentResolver().query(
				DevcrowdContentProvider.CONTENT_URI_PRESENATIONS, null, null,
				null, DevcrowdTables.PRESENTATION_ID + " DESC");
	}

	private View createTopicCardView(String topic, String hour, String content) {
		final View topicCard = getLayoutInflater().inflate(
				R.layout.topic_card_item, null);

		TextView textTopic = (TextView) topicCard.findViewById(R.id.textTopic);
		TextView textHour = (TextView) topicCard.findViewById(R.id.textHour);
		final TextView textTopicDetails = (TextView) topicCard
				.findViewById(R.id.textTopicDetails);
		final ImageView imageTopic = (ImageView) topicCard
				.findViewById(R.id.imageMoreTopic);
		rotateTo0(imageTopic, ZERO_DURATION_TIME_MS);

		textTopic.setText(topic);
		textHour.setText(hour);
		textTopicDetails.setText(content);

		topicCard.setLayoutParams(createLayoutParams(viewId));
		topicCard.setId(++viewId);
		topicCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleDetailsVisibility(textTopicDetails, imageTopic);
			}
		});
		return topicCard;
	}

	private View createSperakerCardView(String url, String name, String content) {
		final View speakerCard = getLayoutInflater().inflate(
				R.layout.speaker_card_item, null);

		final RoundImageView imageSpeaker = (RoundImageView) speakerCard
				.findViewById(R.id.imgLogoDevCrowd);
		TextView textSpeakerName = (TextView) speakerCard
				.findViewById(R.id.textSpeaker);
		final TextView textSpeakerDetails = (TextView) speakerCard
				.findViewById(R.id.textSpeakerDetails);

		ShutterbugManager.getSharedImageManager(this).download(url,
				new ShutterbugManagerListener() {

					@Override
					public void onImageSuccess(ShutterbugManager sm,
							Bitmap bmp, String arg2) {
						imageSpeaker.setImageBitmap(bmp);
					}

					@Override
					public void onImageFailure(ShutterbugManager arg0,
							String arg1) {
						imageSpeaker.setImageResource(R.drawable.head_simple);
					}
				});
		textSpeakerName.setText(name);
		textSpeakerDetails.setText(content);

		speakerCard.setLayoutParams(createLayoutParams(viewId));
		speakerCard.setId(++viewId);
		speakerCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView imageSpeaker = (ImageView) speakerCard
						.findViewById(R.id.imageMoreSpeaker);
				toggleDetailsVisibility(textSpeakerDetails, imageSpeaker);
			}

		});
		return speakerCard;
	}

	private View createRatingCardView() {
		View ratingCard = getLayoutInflater().inflate(
				R.layout.rating_card_item, null);
		ratingCard.setLayoutParams(createLayoutParams(viewId));
		ratingCard.setId(++viewId);

		RatingBar ratingBar = (RatingBar) ratingCard
				.findViewById(R.id.ratingBar);
		ratingBar.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					showRateDialog();
				}
				return true;
			}
		});
		return ratingCard;
	}

	private RelativeLayout.LayoutParams createLayoutParams(int below) {
		int MARGIN_INI_PX_TOP_BOTTOM = dpToPx(4);
		int MARGIN_INI_PX_RIGHT_LEFT = dpToPx(8);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(MARGIN_INI_PX_RIGHT_LEFT, MARGIN_INI_PX_TOP_BOTTOM,
				MARGIN_INI_PX_RIGHT_LEFT, MARGIN_INI_PX_TOP_BOTTOM);
		if (below > 0)
			params.addRule(RelativeLayout.BELOW, below);
		return params;
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
		// ratingBar.setRating((topicGrade + overallGrade) / 2);

		// TEST PRESENTATION RATE
		String presentationTitle = "Some title"; // title of current
													// presentation
		asyncRatePresentation(presentationTitle, topicGrade, overallGrade,
				email);
		// -----------------------
	}

	private void asyncRatePresentation(String presentationTitle,
			float topicGrade, float speakerGrade, String email) {
		Intent intent = new Intent(this, ApiService.class);
		intent.setAction(ApiService.ACTION_RATE_PRESENTATION);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_TITLE,
				presentationTitle);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_TOPIC_GRADE,
				topicGrade);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_SPEAKER_GRADE,
				speakerGrade);
		intent.putExtra(ApiService.RATE_PRESENTATION_EXTRA_EMAIL, email);
		startService(intent);
	}

	private int dpToPx(int dp) {
		Resources resources = this.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return Math.round(px);
	}

}
