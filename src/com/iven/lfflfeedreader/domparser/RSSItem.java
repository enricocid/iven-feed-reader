package com.iven.lfflfeedreader.domparser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String item_title = null;
	private String item_desc = null;
	private String item_date = null;
	private String item_image = null;

	void setTitle(String title) {
		item_title = title;
	}

	void setDescription(String description) {
		item_desc = description;
	}

	void setDate(String pubdate) {
		item_date = pubdate;
	}

	void setImage(String image) {
		item_image = image;
	}

	public String getTitle() {
		return item_title;
	}

	public String getDescription() {
		return item_desc;
	}

	public String getDate() {
		return item_date;
	}

	public String getImage() {
		return item_image;
	}

}
