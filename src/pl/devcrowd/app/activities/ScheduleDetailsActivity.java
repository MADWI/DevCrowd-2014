package pl.devcrowd.app.activities;

import pl.devcrowd.app.R;
import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.db.DevcrowdTables;
import pl.devcrowd.app.dialogs.RateDialog;
import pl.devcrowd.app.dialogs.RateDialog.OnRatingListener;
import pl.devcrowd.app.overviews.RoundImageView;
import pl.devcrowd.app.services.ApiService;
import pl.devcrowd.app.utils.DebugLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applidium.shutterbug.utils.ShutterbugManager;
import com.applidium.shutterbug.utils.ShutterbugManager.ShutterbugManagerListener;

public class ScheduleDetailsActivity extends ActionBarActivity implements
		OnRatingListener {

	private static final int TEXT_DURATION_TIME_MS = 500;
	private static final int ARROW_DURATION_TIME_MS = 400;
	private static final int ZERO_DURATION_TIME_MS = 0;
	private static final String RATE_DIALOG_TAG = "rate_dialog";

	private RelativeLayout rootView;
	private int viewId = 0;

	public static final String SPEAKERS_COUNT = "SPEAKERS_COUNT";
	public static final String TOPIC_GRADE = "TOPIC_GRADE";
	public static final String SPEAKER_GRADE = "SPEAKER_GRADE";
	private float speakersCount = 0f;
	protected boolean isConnected = false;
	private RatingBar ratingBar;
	private String presentationTopic = "";

	private float topicGrade;
	private float speakerGrade;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.schedule_details);

		getSupportActionBar().setHomeButtonEnabled(true);

		Bundle extras = getIntent().getExtras();
		String id = extras.getString(DevcrowdTables.PRESENTATION_ID);

		rootView = (RelativeLayout) findViewById(R.id.rootView);
		fillPresentationData(id);
		fillSpeakersData(id);
		fillReateDialogCard(id);
	}

	private void fillPresentationData(String id) {
		Cursor cursor = getCursor(DevcrowdContentProvider.CONTENT_URI_PRESENATIONS);
		if (cursor == null) {
			return;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {

				rootView.addView(createTopicCardView(
						getStringValue(cursor,
								DevcrowdTables.PRESENTATION_TITLE),
						getString(R.string.hour)
								+ " "
								+ getStringValue(cursor,
										DevcrowdTables.PRESENTATION_START)
								+ " - "
								+ getStringValue(cursor,
										DevcrowdTables.PRESENTATION_END),
						getStringValue(cursor,
								DevcrowdTables.PRESENTATION_DESCRIPTION)));
				break;
			}
		}
	}

	private void fillSpeakersData(String id) {
		Cursor cursor = getCursor(DevcrowdContentProvider.CONTENT_URI_PRESENATIONS);
		if (cursor == null) {
			return;
		}

		String presentationName = "";
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {
				presentationName = cursor.getString(cursor
						.getColumnIndex(DevcrowdTables.PRESENTATION_TITLE));
				break;
			}
		}

		cursor = getCursor(DevcrowdContentProvider.CONTENT_URI_SPEAKERS);
		if (cursor == null) {
			return;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor
					.getString(
							cursor.getColumnIndex(DevcrowdTables.SPEAKER_COLUMN_PRESENTATION_TITLE))
					.equals(presentationName)) {

				speakersCount += 1f;
				rootView.addView(createSperakerCardView(
						getStringValue(cursor,
								DevcrowdTables.SPEAKER_COLUMN_FOTO),
						parseSpeakerName(getStringValue(cursor,
								DevcrowdTables.SPEAKER_COLUMN_NAME)),
						getStringValue(cursor,
								DevcrowdTables.SPEAKER_COLUMN_DESCRIPTION)));
			}
		}
	}

	private void fillReateDialogCard(String id) {
		Cursor cursor = getCursor(DevcrowdContentProvider.CONTENT_URI_PRESENATIONS);
		if (cursor == null) {
			return;
		}

		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (cursor.getString(
					cursor.getColumnIndex(DevcrowdTables.PRESENTATION_ID))
					.equals(id)) {

				rootView.addView(createRatingCardView(
						getStringValue(cursor,
								DevcrowdTables.PRESENTATION_TOPIC_GRADE),
						getStringValue(cursor,
								DevcrowdTables.PRESENTATION_SPEAKER_GRADE)));

				break;
			}
		}
	}

	private String getStringValue(Cursor cursor, String columnName) {
		String value = "";
		int columnIndex = cursor.getColumnIndex(columnName);
		if (columnIndex >= 0) {
			try {
				value = cursor.getString(columnIndex);
			} catch (IndexOutOfBoundsException e) {
				DebugLog.e(e.toString());
				value = null;
			}

			if (value != null) {
				value = stripHtml(value);
			} else {
				value = "";
			}
		}
		return value;
	}

	private String stripHtml(String html) {
		return Html.fromHtml(html).toString();
	}

	private String parseSpeakerName(String speakerName) {
		StringBuilder builder = new StringBuilder("");
		String[] split = speakerName.split("\\s+");
		for (int i = 0; i < split.length; i++) {
			if (i == (split.length - 1)) {
				builder.append("\n");
			}
			builder.append(split[i] + " ");
		}
		return builder.toString();
	}

	private Cursor getCursor(Uri uri) {
		return getContentResolver().query(uri, null, null, null,
				DevcrowdTables.PRESENTATION_ID + " DESC");
	}

	private View createTopicCardView(String topic, String hour, String content) {
		final View topicCard = getLayoutInflater().inflate(
				R.layout.topic_card_item, null);

		presentationTopic = topic;

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
		final ImageView imageMore = (ImageView) speakerCard
				.findViewById(R.id.imageMoreSpeaker);

		setSpeakerImage(imageSpeaker, url);
		textSpeakerName.setText(name);
		textSpeakerDetails.setText(content);

		speakerCard.setLayoutParams(createLayoutParams(viewId));
		speakerCard.setId(++viewId);
		if (!textSpeakerDetails.getText().toString().equals("")) {
			speakerCard.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					toggleDetailsVisibility(textSpeakerDetails, imageMore);
				}
			});
		} else {
			imageMore.setVisibility(View.GONE);
		}
		return speakerCard;
	}

	private void setSpeakerImage(final ImageView image, String url) {
		ShutterbugManager.getSharedImageManager(this).download(url,
				new ShutterbugManagerListener() {

					@Override
					public void onImageSuccess(ShutterbugManager sm,
							Bitmap bmp, String arg2) {
						image.setImageBitmap(bmp);
					}

					@Override
					public void onImageFailure(ShutterbugManager arg0,
							String arg1) {
						image.setImageResource(R.drawable.head_simple);

					}
				});
	}

	private View createRatingCardView(String topicGradeString,
			String speakerGradeString) {
		View ratingCard = getLayoutInflater().inflate(
				R.layout.rating_card_item, null);
		ratingCard.setLayoutParams(createLayoutParams(viewId));
		ratingCard.setId(++viewId);

		ratingBar = (RatingBar) ratingCard.findViewById(R.id.ratingBar);

		this.topicGrade = Float
				.parseFloat(!topicGradeString.equals("") ? topicGradeString
						: "0");
		this.speakerGrade = Float
				.parseFloat(!speakerGradeString.equals("") ? speakerGradeString
						: "0");

		if (this.topicGrade != 0 && this.speakerGrade != 0) {
			float roundGrade = ((this.topicGrade + this.speakerGrade) / 2);
			ratingBar.setRating(roundGrade);
		}

		ratingBar.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					showRateDialog(topicGrade, speakerGrade);
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

	private void showRateDialog(float topicGrade, float speakerGrade) {
		Bundle speakres = new Bundle();
		speakres.putFloat(SPEAKERS_COUNT, speakersCount);
		speakres.putFloat(TOPIC_GRADE, topicGrade);
		speakres.putFloat(SPEAKER_GRADE, speakerGrade);
		RateDialog.show(this, RATE_DIALOG_TAG, speakres);
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
		if (isConnected) {
			ratingBar.setRating((topicGrade + overallGrade) / 2);
			asyncRatePresentation(presentationTopic, topicGrade, overallGrade,
					email);
			this.topicGrade = topicGrade;
			this.speakerGrade = overallGrade;
		} else {
			Toast.makeText(this,
					getString(R.string.no_internet_send_rates_info),
					Toast.LENGTH_SHORT).show();
		}
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
				isConnected = true;
			} else {
				infoLayout.setVisibility(View.VISIBLE);
				isConnected = false;
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

}
