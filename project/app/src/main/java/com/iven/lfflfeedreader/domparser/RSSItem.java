package com.iven.lfflfeedreader.domparser;

import java.io.Serializable;

public class RSSItem implements Serializable {

    //Simple struct class to hold the data for rss item
    //title, link, description, date, images.

    //Original author Isaac Whitfield
    //Extended by EnricoD

    //create the strings we need to store
    private static final long serialVersionUID = 1L;

    //note: do not set null values to avoid exceptions when 'if (item_x.isEmpty())' is used
    private String item_title = "no title";
    private String complete_desc = "no desc";
    private String item_desc = "no desc";
    private String item_date = "no date";
    private String item_image = "no image";
    private String rss_link = "no link";
    private String item_image2 = "no image";

    public String getTitle() {
        return item_title;
    }

    //set 'em all
    void setTitle(String title) {
        item_title = title;
    }

    public String getDescription() {
        return item_desc;
    }

    void setDescription(String description) {
        item_desc = description;
    }

    public String getCompleteDescription() {
        return complete_desc;
    }

    void setCompleteDescription(String complete_description) {
        complete_desc = complete_description;
    }

    public String getDate() {
        return item_date;
    }

    void setDate(String pubdate) {
        item_date = pubdate;
    }

    public String getLink() {
        return rss_link;
    }

    void setLink(String link) {
        rss_link = link;
    }

    public String getImage() {
        return item_image;
    }

    void setImage(String image) {

        item_image = image;
    }

    public String getImage2() {
        return item_image2;
    }

    void setImage2(String image2) {

        item_image2 = image2;
    }
}
