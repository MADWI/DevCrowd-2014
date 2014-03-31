package pl.devcrowd.app.models;

import java.util.ArrayList;

public class Presentation {

	private String hourStart;
	private String hourEnd;
	private String room;
	private String description;
	private String title;
	private ArrayList<String> speakers;
	private String gradeTopic;
	private String gradeSpeaker;
	private String favourite;

	public Presentation() {
		this("", "", "", "", "", null, "", "", "");
	}

	public Presentation(String hourStart, String hourEnd, String room,
			String description, String title, ArrayList<String> speakers, String gradeTopic,
			String gradeSpeaker, String favourite) {
		super();
		this.hourStart = hourStart;
		this.hourEnd = hourEnd;
		this.room = room;
		this.description = description;
		this.title = title;
		this.speakers = speakers;
		this.gradeTopic = gradeTopic;
		this.gradeSpeaker = gradeSpeaker;
		this.favourite = favourite;
	}

	public String getHourStart() {
		return hourStart;
	}

	public void setHourStart(String hourStart) {
		this.hourStart = hourStart;
	}

	public String getHourEnd() {
		return hourEnd;
	}

	public void setHourEnd(String hourEnd) {
		this.hourEnd = hourEnd;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getSpeakers() {
		return speakers;
	}

	public void setSpeakers(ArrayList<String> speakers) {
		this.speakers = speakers;
	}
	
	public String getGradeTopic() {
		return gradeTopic;
	}

	public void setGradeTopic(String gradeTopic) {
		this.gradeTopic = gradeTopic;
	}
	
	public String getGradeSpeaker() {
		return gradeSpeaker;
	}

	public void setSGradeSpeaker(String gradeSpeaker) {
		this.gradeSpeaker = gradeSpeaker;
	}

	public String getFavourite() {
		return favourite;
	}

	public void setFavourite(String favourite) {
		this.favourite = favourite;
	}

	@Override
	public String toString() {
		if (speakers == null || speakers.size() == 0) {
			return "Presentation [empty]";
		} else {
			return "Presentation [hourStart=" + hourStart + ", hourEnd=" + hourEnd
				+ ", room=" + room + ", description=" + description
				+ ", title=" + title + ", speaker=" + speakers.get(0) + ", gradeTopic=" + gradeTopic 
				+ ", gradeSpeaker=" + gradeSpeaker + "]";
		}
	}
	
	

}
