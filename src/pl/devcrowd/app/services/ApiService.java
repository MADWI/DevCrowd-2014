package pl.devcrowd.app.services;

import java.util.List;

import pl.devcrowd.app.db.DevcrowdContentProvider;
import pl.devcrowd.app.http.HttpHelper;
import pl.devcrowd.app.models.Presentation;
import pl.devcrowd.app.models.Speaker;
import pl.devcrowd.app.parsers.JSONParser;
import pl.devcrowd.app.utils.ContentProviderHelper;
import pl.devcrowd.app.utils.DebugLog;
import android.app.IntentService;
import android.content.Intent;

public class ApiService extends IntentService {

	public static final String ACTION_GET_PRESENTATIONS = "pl.devcrowd.app.ACTION_GET_PRESENTATIONS";
	public static final String ACTION_GET_SPEAKERS = "pl.devcrowd.app.ACTION_GET_SPEAKERS";
	// TODO Test server url
	private static final String API_URL = "http://effectiveheating.co.uk/api/?json";

	public ApiService() {
		super(ApiService.class.getSimpleName());

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

			List<Presentation> presentations = JSONParser
					.getPresentationsFromString(response);

			for (Presentation presentation : presentations) {
				ContentProviderHelper.addPresenatiton(getContentResolver(),
						presentation);
			}

			ContentProviderHelper.notifyChange(getContentResolver(),
					DevcrowdContentProvider.CONTENT_URI_PRESENATIONS);

		} else if (intentAction != null
				&& intentAction.equals(ACTION_GET_SPEAKERS)) {

			String response = HttpHelper.getContentFromUrl(API_URL);
			List<Speaker> speakers = JSONParser.getSpeakersFromString(response);

			for (Speaker speaker : speakers) {
				ContentProviderHelper.addSpeaker(getContentResolver(), speaker);
			}

			ContentProviderHelper.notifyChange(getContentResolver(),
					DevcrowdContentProvider.CONTENT_URI_SPEAKERS);

		} else {
			DebugLog.d("Unknown action");
		}

	}

}
