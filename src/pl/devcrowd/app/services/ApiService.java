package pl.devcrowd.app.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import pl.devcrowd.app.R;
import pl.devcrowd.app.http.HttpHelper;
import pl.devcrowd.app.models.Presentation;
import pl.devcrowd.app.models.Speaker;
import pl.devcrowd.app.parsers.JSONParser;
import pl.devcrowd.app.utils.ContentProviderHelper;
import pl.devcrowd.app.utils.DebugLog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

public class ApiService extends IntentService {

	public static final String ACTION_GET_PRESENTATIONS = "pl.devcrowd.app.ACTION_GET_PRESENTATIONS";
	public static final String ACTION_GET_SPEAKERS = "pl.devcrowd.app.ACTION_GET_SPEAKERS";
	public static final String ACTION_RATE_PRESENTATION = "pl.devcrowd.app.ACTION_RATE_PRESENTATION";
	public static final String RATE_PRESENTATION_EXTRA_TITLE = "presentationTitle";
	public static final String RATE_PRESENTATION_EXTRA_EMAIL = "email";
	public static final String RATE_PRESENTATION_EXTRA_SPEAKER_GRADE = "speakerGrade";
	public static final String RATE_PRESENTATION_EXTRA_TOPIC_GRADE = "topicGrade";

	private static final String RATE_API_ACTION = "action";
	private static final String RATE_API_ACTION_ADD_GRADES = "add_grades";
	private static final String RATE_API_SPEAKER_GRADE = "prelegent_grade";
	private static final String RATE_API_TOPIC_GRADE = "presentation_grade";
	private static final String RATE_API_PRESENTATION_TITLE = "presentation_name";
	private static final String RATE_API_PRESENTATION_EMAIL = "email";
	private static final String RATE_API_SUCCESS = "success";
	private static final float DEFAULT_GRADE = 1.0f;

	private static final String RATING_URL = "http://2014.devcrowd.pl/mad-api/oceny.php";
	private static final String API_URL = "http://2014.devcrowd.pl/mad-api";
	private Handler toastHandler;

	public ApiService() {
		super(ApiService.class.getSimpleName());
		toastHandler = new Handler();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		DebugLog.d("onStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		DebugLog.d("onHandleIntent");
		String intentAction = intent.getAction();
		if (intentAction != null
				&& intentAction.equals(ACTION_GET_PRESENTATIONS)) {
			String response = HttpHelper.getContentFromUrl(API_URL);
			getPresentationsIntoDatabase(response);

		} else if (intentAction != null
				&& intentAction.equals(ACTION_GET_SPEAKERS)) {

			String response = HttpHelper.getContentFromUrl(API_URL);
			getSpeakersIntoDatabase(response);

		} else if (intentAction != null
				&& intentAction.equals(ACTION_RATE_PRESENTATION)) {

			String presentationTitle = intent
					.getStringExtra(RATE_PRESENTATION_EXTRA_TITLE);
			float topicGrade = intent.getFloatExtra(
					RATE_PRESENTATION_EXTRA_TOPIC_GRADE, DEFAULT_GRADE);
			float speakerGrade = intent.getFloatExtra(
					RATE_PRESENTATION_EXTRA_SPEAKER_GRADE, DEFAULT_GRADE);
			String email = intent.getStringExtra(RATE_PRESENTATION_EXTRA_EMAIL);

			sendRatesOnServer(presentationTitle, topicGrade, speakerGrade,
					email);

		} else {
			DebugLog.w("Unknown action");
		}

	}

	private void getPresentationsIntoDatabase(String response) {
		List<Presentation> presentations = JSONParser
				.getPresentationsFromString(response);

		for (Presentation presentation : presentations) {

			if (ContentProviderHelper.presentationExist(getContentResolver(),
					presentation.getTitle())) {
				ContentProviderHelper.updatePresentation(getContentResolver(),
						presentation.getTitle(), presentation);
			} else {
				ContentProviderHelper.addPresenatiton(getContentResolver(),
						presentation);
			}

		}
	}

	private void getSpeakersIntoDatabase(String response) {
		List<Speaker> speakers = JSONParser.getSpeakersFromString(response);

		for (Speaker speaker : speakers) {

			if (ContentProviderHelper.speakerExist(getContentResolver(),
					speaker.getName())) {
				ContentProviderHelper.updateSpeaker(getContentResolver(),
						speaker.getName(), speaker);
			} else {
				ContentProviderHelper.addSpeaker(getContentResolver(), speaker);
			}

		}
	}

	private void sendRatesOnServer(String presentationTitle, float topicGrade,
			float speakerGrade, String email) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(RATING_URL);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair(RATE_API_ACTION,
					RATE_API_ACTION_ADD_GRADES));
			nameValuePairs.add(new BasicNameValuePair(RATE_API_SPEAKER_GRADE,
					String.valueOf(speakerGrade)));
			nameValuePairs.add(new BasicNameValuePair(RATE_API_TOPIC_GRADE,
					String.valueOf(topicGrade)));
			nameValuePairs.add(new BasicNameValuePair(
					RATE_API_PRESENTATION_TITLE, presentationTitle));
			nameValuePairs.add(new BasicNameValuePair(
					RATE_API_PRESENTATION_EMAIL, email));

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			DebugLog.d(nameValuePairs.toString());

			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);

			if (result.contains(RATE_API_SUCCESS)) {
				DebugLog.d(result);

				ContentProviderHelper.updateRating(getContentResolver(),
						presentationTitle, topicGrade, speakerGrade);
				toastHandler.post(new DisplayToast(this,
						getString(R.string.success_voting_toast_text)));
			} else {
				DebugLog.e("Error sendig rates: " + result);
				toastHandler.post(new DisplayToast(this,
						getString(R.string.error_voting_toast_text)));
			}

		} catch (ClientProtocolException e) {
			DebugLog.e("Errir during sending ratings " + e.getMessage());
		} catch (IOException e) {
			DebugLog.e("Errir during sending ratings " + e.getMessage());
		}
	}

	private class DisplayToast implements Runnable {
		private String mText;
		private final Context mContext;

		public DisplayToast(Context context, String text) {
			mContext = context;
			mText = text;
		}

		public void run() {
			Toast.makeText(mContext, mText, Toast.LENGTH_SHORT).show();
		}
	}

}
