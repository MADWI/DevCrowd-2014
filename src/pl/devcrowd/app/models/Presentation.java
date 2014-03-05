package pl.devcrowd.app.models;

public class Presentation {
	
	private String hourStart;
	private String hourEnd;
	private String room;
	private String description;
	private String title;
	private String prelegent;
	
	public Presentation() {
		super();
		this.hourStart = null;
		this.hourEnd = null;
		this.room = null;
		this.description = null;
		this.title = null;
		this.prelegent = null;
	}
	
	public Presentation(String hourStart, String hourEnd, String room,
			String description, String title, String prelegent) {
		super();
		this.hourStart = hourStart;
		this.hourEnd = hourEnd;
		this.room = room;
		this.description = description;
		this.title = title;
		this.prelegent = prelegent;
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


	public String getPrelegent() {
		return prelegent;
	}


	public void setPrelegent(String prelegent) {
		this.prelegent = prelegent;
	}
	
}
