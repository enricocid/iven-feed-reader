package com.iven.lfflfeedreader.domparser;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class RSSFeed implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<RSSItem> _itemlist;
    private int _itemcount = 0;
	RSSFeed() {
		_itemlist = new Vector<>(0);
	}

	void addItem(RSSItem item) {
		_itemlist.add(item);
		_itemcount++;
	}

	public RSSItem getItem(int location) {
		return _itemlist.get(location);
	}

	public int getItemCount() {

		return _itemcount;

	}

}
