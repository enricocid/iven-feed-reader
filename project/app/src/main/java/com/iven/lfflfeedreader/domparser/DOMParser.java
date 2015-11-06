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

public class DOMParser {

	private RSSFeed _feed = new RSSFeed();
	public RSSFeed parseXml(String xml) {

		URL url = null;
		try {
			url = new URL(xml);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document doc = db.parse(new InputSource(url.openStream()));
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getElementsByTagName("item");
			int length = nl.getLength();

			for (int i = 0; i < length; i++) {
				Node currentNode = nl.item(i);
				RSSItem _item = new RSSItem();

				NodeList nchild = currentNode.getChildNodes();
				int clength = nchild.getLength();

				for (int j = 0; j < clength; j++) {

					Node thisNode = nchild.item(j);
					String theString = null;
					
					 if (thisNode != null && thisNode.getFirstChild() != null) {
				            theString = nchild.item(j).getFirstChild().getNodeValue();
				        }

					if (theString != null) {
						String nodeName = thisNode.getNodeName();
						if ("title".equals(nodeName)) {
							_item.setTitle(theString);

						}

						else if ("link".equals(nodeName)) {
								_item.setLink(theString);

						} else if ("author".equals(nodeName)) {
								_item.setAuthor(theString);
							String formatedAuthor = theString.replace("noreply@blogger.com (","");
							_item.setAuthor(formatedAuthor);
							String formatedAuthor2 = formatedAuthor.replace(")","");
							_item.setAuthor(formatedAuthor2);

                        } else if ("description".equals(nodeName)) {
							_item.setDescription(theString);

							String html = theString;
							org.jsoup.nodes.Document docHtml = Jsoup
									.parse(html);

							Elements imgEle = docHtml.select("img");
							_item.setImage(imgEle.attr("src"));
                            
						}

						else if ("pubDate".equals(nodeName)) {

							String formatedDate = theString.replace(" +0000", "");
							Locale loc = Resources.getSystem().getConfiguration().locale;

                            SimpleDateFormat curFormater = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss",  java.util.Locale.US);
                            Date dateObj = curFormater.parse(formatedDate);
                            SimpleDateFormat postFormater = new SimpleDateFormat("EEE, dd.MM.yyyy - HH:mm",  loc);

							String timezoneID = TimeZone.getDefault().getID();
							
							postFormater.setTimeZone(TimeZone.getTimeZone(timezoneID));
                            String newDateStr = postFormater.format(dateObj);

                            _item.setDate(newDateStr);
						}
					}
				}

				_feed.addItem(_item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return _feed;
	}

}
