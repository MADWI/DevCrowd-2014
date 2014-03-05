package pl.devcrowd.app.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.devcrowd.app.models.Prelegent;
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
	private ArrayList<Presentation> listOfPresentations = new ArrayList<Presentation>();

	/**
	 * list of Prelegent objects
	 * */
	private ArrayList<Prelegent> listOfPrelegents = new ArrayList<Prelegent>();

	/**
	 * parse JSON to get ArrayList of Presentation objects
	 * 
	 * @param response
	 *            - JSON String from URL
	 * 
	 * @return ArrayList of Presentation objects
	 * */
	public ArrayList<Presentation> getPresentationFromString(String response) {
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				parsePresentationArray(responseArray, i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listOfPresentations;
	}

	/**
	 * parse JSON to get ArrayList of Prelegent objects
	 * 
	 * @param response
	 *            - JSON String from URL
	 * 
	 * @return ArrayList of Prelegent objects
	 * */
	public ArrayList<Prelegent> getPrelegentsFromString(String response) {
		try {
			JSONArray responseArray = new JSONArray(response);
			for (int i = 0; i < responseArray.length(); i++) {
				parsePrelegentObject(responseArray, i);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listOfPrelegents;
	}

	/**
	 * add filled Presentation objects to ArrayList
	 * 
	 * @param responseArray
	 *            - JSON String from URL
	 * @param i
	 *            - index of current object
	 * 
	 * */
	private void parsePresentationArray(JSONArray responseArray, int i) {
		try {
			String title = null;
			String description = null;
			String room = null;
			String starts = null;
			String ends = null;
			String prelegent = null;
			Presentation presentation = new Presentation();
			JSONObject arrayElement;

			arrayElement = responseArray.getJSONObject(i);

			if (arrayElement.has("title"))
				title = arrayElement.getString("title");
			else
				title = "";
			if (arrayElement.has("description"))
				description = arrayElement.getString("description");
			else
				description = "";
			if (arrayElement.has("room"))
				room = arrayElement.getString("room");
			else
				room = "";
			if (arrayElement.has("starts"))
				starts = arrayElement.getString("starts");
			else
				starts = "";
			if (arrayElement.has("ends"))
				ends = arrayElement.getString("ends");
			else
				ends = "";
			if (arrayElement.has("prelegent")) {
				JSONObject prelegentObject = arrayElement
						.getJSONObject("prelegent");
				prelegent = prelegentObject.getString("name");
			} else {
				prelegent = "";
			}

			presentation.setTitle(title);
			presentation.setDescription(description);
			presentation.setHourStart(starts);
			presentation.setHourEnd(ends);
			presentation.setRoom(room);
			presentation.setPrelegent(prelegent);
			listOfPresentations.add(presentation);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * add filled Prelegent objects to ArrayList
	 * 
	 * @param responseArray
	 *            - JSON String from URL
	 * @param i
	 *            - index of current object
	 * 
	 * */
	private void parsePrelegentObject(JSONArray responseArray, int i) {
		try {
			String name = null;
			String photoPath = null;
			String description = null;
			JSONObject arrayElement = new JSONObject();
			Prelegent prelegent = new Prelegent();

			arrayElement = responseArray.getJSONObject(i);
			if (arrayElement.has("prelegent")) {
				JSONObject prelegentElement = arrayElement
						.getJSONObject("prelegent");
				if (prelegentElement.has("name"))
					name = prelegentElement.getString("name");
				else
					name = "";
				if (prelegentElement.has("description"))
					description = prelegentElement.getString("description");
				else
					description = "";
				if (prelegentElement.has("photoUrl"))
					photoPath = prelegentElement.getString("photoUrl");
				else
					photoPath = "";
			}

			prelegent.setName(name);
			prelegent.setPhotoPath(photoPath);
			prelegent.setDescription(description);
			listOfPrelegents.add(prelegent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
