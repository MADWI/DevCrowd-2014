package pl.devcrowd.app.parsers;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import pl.devcrowd.app.BuildConfig;
import pl.devcrowd.app.models.Speaker;
import pl.devcrowd.app.models.Presentation;

/**
 * JSON Parser class to get useful objects
 * 
 * @author Tofer
 * */
public class JSONParser {

	private static final String TAG = JSONParser.class.getSimpleName();
	private static final String PRESENTATION_TITLE = "title";
	private static final String PRESENTATION_DESCRIPTION = "description";
	private static final String PRESENTATION_ROOM = "room";
	private static final String PRESENTATION_START = "starts";
	private static final String PRESENTATION_END = "ends";
	private static final String PRESENTATION_SPEAKER = "prelegent";

	private static final String SPEAKER_NAME = "name";
	private static final String SPEAKER_PHOTOURL = "photoUrl";
	private static final String SPEAKER_DESCRIPTION = "description";

	/**
	 * parse JSON to get List of Presentation objects
	 * 
	 * @param response
	 *            - JSON String from URL
	 * 
	 * @return List of Presentation objects
	 * */
	public List<Presentation> getPresentationsFromString(final String response) {
		List<Presentation> listOfPresentations = new ArrayList<Presentation>();
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				listOfPresentations
						.add(parsePresentationArray(responseArray, i));
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "JSONException during parse presenations");
			}
		}
		return listOfPresentations;
	}

	/**
	 * parse JSON to get List of Speaker objects
	 * 
	 * @param response
	 *            - JSON String from URL
	 * 
	 * @return List of Speaker objects
	 * */
	public List<Speaker> getSpeakersFromString(final String response) {
		List<Speaker> listOfSpeakers = new ArrayList<Speaker>();
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				listOfSpeakers.add(parseSpeakerObject(responseArray, i));
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "JSONException during get speakers");
			}
		}
		return listOfSpeakers;
	}

	/**
	 * add filled Presentation objects to List
	 * 
	 * @param responseArray
	 *            - JSON String from URL
	 * @param i
	 *            - index of current object
	 * 
	 * */
	private Presentation parsePresentationArray(final JSONArray responseArray, int index) {
		Presentation presentation = new Presentation();
		try {
			JSONObject arrayElement = responseArray.getJSONObject(index);

			presentation.setTitle(getStringFromArray(arrayElement,
					PRESENTATION_TITLE));
			presentation.setDescription(getStringFromArray(arrayElement,
					PRESENTATION_DESCRIPTION));
			presentation.setHourStart(getStringFromArray(arrayElement,
					PRESENTATION_ROOM));
			presentation.setHourEnd(getStringFromArray(arrayElement,
					PRESENTATION_START));
			presentation.setRoom(getStringFromArray(arrayElement,
					PRESENTATION_END));

			JSONObject speakerArray = getArrayFromArray(arrayElement,
					PRESENTATION_SPEAKER);
			presentation.setSpeaker(getStringFromArray(speakerArray,
					SPEAKER_NAME));

		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "JSONException during parse presentation array");
			}
		}
		return presentation;
	}

	/**
	 * add filled Speaker objects to List
	 * 
	 * @param responseArray
	 *            - JSON String from URL
	 * @param i
	 *            - index of current object
	 * 
	 * */
	private Speaker parseSpeakerObject(final JSONArray responseArray, int index) {
		final Speaker speaker = new Speaker();
		try {
			final JSONObject arrayElement = responseArray.getJSONObject(index);

			JSONObject speakerElement = arrayElement
					.getJSONObject(PRESENTATION_SPEAKER);
			speaker.setName(getStringFromArray(speakerElement, SPEAKER_NAME));
			speaker.setPhotoUrl(getStringFromArray(speakerElement,
					SPEAKER_PHOTOURL));
			speaker.setDescription(getStringFromArray(speakerElement,
					SPEAKER_DESCRIPTION));
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.e(TAG, "JSONException during parse speaker object");
			}

		}
		return speaker;
	}

	private String getStringFromArray(final JSONObject arrayElement, final String key)
			throws JSONException {
		return arrayElement.has(key) ? arrayElement.getString(key) : "";
	}

	private JSONObject getArrayFromArray(final JSONObject arrayElement, final String key)
			throws JSONException {
		return arrayElement.has(key) ? arrayElement.getJSONObject(key)
				: new JSONObject();
	}

}
