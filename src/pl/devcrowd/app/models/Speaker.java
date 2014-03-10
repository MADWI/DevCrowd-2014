package pl.devcrowd.app.models;

public class Speaker {

	private String name;
	private String photoUrl;
	private String description;

	public Speaker() {
		this("", "", "");
	}

	public Speaker(String name, String photoUrl, String description) {
		this.setName(name);
		this.setPhotoUrl(photoUrl);
		this.setDescription(description);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return photoUrl;
	}

	/**
	 * @param photoUrl
	 *            the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Speaker [name=" + name + ", photoUrl=" + photoUrl
				+ ", description=" + description + "]";
	}
	
	

}
