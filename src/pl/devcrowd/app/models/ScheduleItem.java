package pl.devcrowd.app.models;

public class ScheduleItem {

	private String hour;
	private String topic;
	private String speaker;
	
	public ScheduleItem(String hour, String topic, String speaker) {
		this.hour = hour;
		this.topic = topic;
		this.speaker = speaker;
	}
	
	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getSpeaker() {
		return speaker;
	}

	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}

}
	
	
 
