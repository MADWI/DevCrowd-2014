package pl.devcrowd.app;

public class AgendaItem {

	private String hour;
	private String topic;
	private String prelegent;
	
	public AgendaItem(String hour, String topic, String prelegent) {
		this.hour = hour;
		this.topic = topic;
		this.prelegent = prelegent;
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

	public String getPrelegent() {
		return prelegent;
	}

	public void setPrelegent(String prelegent) {
		this.prelegent = prelegent;
	}

}
	
	
 
