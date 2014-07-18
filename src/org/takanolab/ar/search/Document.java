package org.takanolab.ar.search;

public class Document {

	String name = "";
	String description = "";
	String image = "";
	
	public void setName (String name) {
		this.name = name;
	};
	public void setDescription (String description) {
		this.description = description;
	};
	public void setImage (String image) {
		this.image = image;
	};
	public String getName () {
		return this.name;
	} ;
	public String getDescription () {
		return this.description;
	};
	public String getImage () {
		return this.image;
	};
}
