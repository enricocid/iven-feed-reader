package com.iven.lfflfeedreader.domparser;

import java.io.Serializable;

public class RSSItem implements Serializable {

	private static final long serialVersionUID = 1L;
	private String item_title = null;
	private String item_desc = null;
	private String item_date = null;
	private String item_image = null;
	private String rss_link = null;
	private String item_author = null;

	void setTitle(String title) {
		item_title = title;
	}

	void setDescription(String description) {
		item_desc = description;
	}

	void setDate(String pubdate) {
		item_date = pubdate;
	}

	void setAuthor(String author) {
		item_author = author;
	}

	void setImage(String image) {
		item_image = image;
	}

	void setLink(String link){
		rss_link = link;
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

	public String getAuthor() {
		return item_author;
	}

	public String getLink() {
		return rss_link;
	}

	public String getImage() {
		return item_image;
	}
}
