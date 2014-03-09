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

	/**
	 * list of Presentation objects
	 * */
	private List<Presentation> listOfPresentations = new ArrayList<Presentation>();

	/**
	 * list of Speaker objects
	 * */
	private List<Speaker> listOfSpeakers = new ArrayList<Speaker>();

	/**
	 * parse JSON to get List of Presentation objects
	 * 
	 * @param response
	 *            - JSON String from URL
	 * 
	 * @return List of Presentation objects
	 * */
	public List<Presentation> getPresentationFromString(String response) {
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				parsePresentationArray(responseArray, i);
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.e("getPresentationFromString()", e.toString());
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
	public List<Speaker> getSpeakersFromString(String response) {
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				parseSpeakerObject(responseArray, i);
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.w("getSpeakersFromString()", e.toString());
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
	private void parsePresentationArray(JSONArray responseArray, int i) {
		try {
			JSONObject arrayElement = responseArray.getJSONObject(i);

			String title = getJSONFieldFromObject(arrayElement, "title");
			String description = getJSONFieldFromObject(arrayElement,
					"description");
			String room = getJSONFieldFromObject(arrayElement, "room");
			String starts = getJSONFieldFromObject(arrayElement, "starts");
			String ends = getJSONFieldFromObject(arrayElement, "ends");
			String speaker = getJSONFieldFromArray(arrayElement, "prelegent");

			Presentation presentation = new Presentation();
			presentation.setTitle(title);
			presentation.setDescription(description);
			presentation.setHourStart(room);
			presentation.setHourEnd(starts);
			presentation.setRoom(ends);
			presentation.setSpeaker(speaker);

			listOfPresentations.add(presentation);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * check value under key and return it
	 * 
	 * @param arrayElement
	 *            - object to check
	 * @param key
	 *            - key to check
	 * @param isSpeakerObject
	 *            - JSON key is an Speaker object
	 * 
	 * @return value under key
	 * */
	private String getJSONFieldFromObject(JSONObject arrayElement, String key) {
		String value = null;
		try {
			if (arrayElement.has(key)) {
				value = arrayElement.getString(key);
			} else {
				value = "";
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.w("DevCrowdApp", e.toString());
			}
		}
		return value;
	}

	private String getJSONFieldFromArray(JSONObject arrayElement, String key) {
		String value = null;
		try {
			if (arrayElement.has(key)) {
				JSONObject speakerObject;
				speakerObject = arrayElement.getJSONObject(key);
				value = speakerObject.getString("name");
			} else {
				value = "";
			}
		} catch (JSONException e) {
			if (BuildConfig.DEBUG) {
				Log.w("DevCrowdApp", e.toString());
			}
		}
		return value;
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
	private void parseSpeakerObject(JSONArray responseArray, int i) {
		try {
			Speaker speaker = new Speaker();
			JSONObject arrayElement = responseArray.getJSONObject(i);

			JSONObject speakerElement = arrayElement
					.getJSONObject("prelegent");
			String name = getJSONFieldFromObject(speakerElement, "name");
			String photoPath = getJSONFieldFromObject(speakerElement,
					"description");
			String description = getJSONFieldFromObject(speakerElement,
					"photoUrl");

			speaker.setName(name);
			speaker.setPhotoPath(photoPath);
			speaker.setDescription(description);

			listOfSpeakers.add(speaker);

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
