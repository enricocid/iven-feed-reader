package com.iven.lfflfeedreader.domparser;

import android.content.res.Resources;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

//Parses an RSS feed and adds the information to a new RSSFeed object.

//Original author Isaac Whitfield
//Extended by EnricoD

public class DOMParser {

    //create a new RSS feed

	private RSSFeed _feed = new RSSFeed();
	public RSSFeed parseXml(String xml) {

        //getting XML content
		URL url = null;
		try {

            //find the new URL from the given URL
			url = new URL(xml);
		} catch (MalformedURLException e1) {

            //throw an exception
			e1.printStackTrace();
		}

        //get the DOM element of the XML file. Below function will parse the XML content and will give you DOM element.
		try {

            //create a new DocumentBuilder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

            //parse the XML
			Document doc = db.parse(new InputSource(url.openStream()));

            //normalize the data
			doc.getDocumentElement().normalize();

            //get each xml child element value by passing element node name

            //get all <item> tags.
			NodeList nl = doc.getElementsByTagName("item");

            //get size of the list
			int length = nl.getLength();

            //looping through all item nodes
			for (int i = 0; i < length; i++) {

				Node currentNode = nl.item(i);
				RSSItem _item = new RSSItem();

                //create a new node of the first item
				NodeList nchild = currentNode.getChildNodes();

                //get size of the child list
				int clength = nchild.getLength();

                //for all the children of a node
				for (int j = 0; j < clength; j++) {

                    //get the name of the child
					Node thisNode = nchild.item(j);
					String theString = null;

                    //if there is at least one child element
					 if (thisNode != null && thisNode.getFirstChild() != null) {

                         //set the string to be the value of the node
				            theString = nchild.item(j).getFirstChild().getNodeValue();
				        }

                    //if the string isn't null
					if (theString != null) {

                        //set the appropriate value
						String nodeName = thisNode.getNodeName();
						if ("title".equals(nodeName)) {
							_item.setTitle(theString);

						}

                        //feed link
						else if ("link".equals(nodeName)) {
								_item.setLink(theString);
                        }

                        //complete description from content:encoded
                        else if ("content:encoded".equals(nodeName)) {

                                org.jsoup.nodes.Document docHtml = Jsoup
                                        .parse(theString);

                             //select images by tag
                             Elements imgEle = docHtml.select("img");

                            //extract the images src from content:encoded
                             String src = imgEle.attr("src");

                            //setImage2() src
                            _item.setImage2(src);

							//set complete description
							_item.setCompleteDescription(theString);

                        //description method is used when complete description returns 'no desc'
                        } else if ("description".equals(nodeName)) {
							_item.setDescription(theString);

							org.jsoup.nodes.Document docHtml = Jsoup
                                    .parse(theString);

							//select images by tag
							Elements imgEle = docHtml.select("img");

                            //get src attribute
                            String src = imgEle.attr("src");

                            //setImage() src
                            _item.setImage(src);

						}

                        //publication date
						else if ("pubDate".equals(nodeName)) {

                            //replace some text inside date
							String formatedDate = theString.replace(" +0000", "");

                            //get locale settings from the phone
							Locale loc = Resources.getSystem().getConfiguration().locale;

                            //change date format
                            SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",  java.util.Locale.US);
                            Date dateObj = curFormater.parse(formatedDate);
                            SimpleDateFormat postFormater = new SimpleDateFormat("EEE, dd.MM.yyyy - HH:mm",  loc);

                            //get the timezone settings from the phone
							String timezoneID = TimeZone.getDefault().getID();

                            //set it dynamically
							postFormater.setTimeZone(TimeZone.getTimeZone(timezoneID));
                            String newDateStr = postFormater.format(dateObj);

                            _item.setDate(newDateStr);
						}
					}
				}

                //add the new item to the RSS feed
				_feed.addItem(_item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

        //return the feed
		return _feed;
	}

}
