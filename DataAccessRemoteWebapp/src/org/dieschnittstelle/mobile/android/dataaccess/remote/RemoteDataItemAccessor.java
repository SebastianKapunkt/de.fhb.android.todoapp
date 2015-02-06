package org.dieschnittstelle.mobile.android.dataaccess.remote;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.dieschnittstelle.mobile.android.dataaccess.model.IDataItemCRUDAccessor;
import org.dieschnittstelle.mobile.android.dataaccess.model.TodoItem;

public class RemoteDataItemAccessor implements IDataItemCRUDAccessor {

	protected static Logger logger = Logger
			.getLogger(RemoteDataItemAccessor.class);

	/**
	 * the list of data items, note that the list is *static* as for each client
	 * request a new instance of this class will be created!
	 */
	private static List<TodoItem> itemlist = new ArrayList<TodoItem>();

	/**
	 * we assign the ids here
	 */
	private static long idCount = 0;

	@Override
	public List<TodoItem> readAllItems() {
		if (idCount == 0) {
			fillList();
		}
		logger.info("readAllItems(): " + itemlist);

		return itemlist;
	}

	@Override
	public TodoItem createItem(TodoItem item) {
		logger.info("createItem(): " + item);
		item.setId(idCount++);

		itemlist.add(item);
		return item;
	}

	@Override
	public boolean deleteItem(final long itemId) {
		logger.info("deleteItem(): " + itemId);
		boolean removed = false;
		int i = 0;
		int found = -1;

		for (TodoItem todoItem : itemlist) {
			if (todoItem.getId() == itemId) {
				found = i;
			}
			i++;
		}

		if (found > -1) {
			removed = true;
			itemlist.remove(found);
		}

		return removed;
	}

	@Override
	public TodoItem updateItem(TodoItem item) {
		logger.info("updateItem(): " + item);

		return itemlist.get(itemlist.indexOf(item)).updateFrom(item);
	}

	public void fillList() {
		Random rnd = new Random();
		itemlist.add(new TodoItem(
				idCount++,
				"Eclipse verbrennen",
				"Schritt 1: Eclipse löschen, Schritt 2: Eclipse Download-Server DDOS'n, Schritt 3: Android Studio runterladen",
				false, true, 1423242000000l, "Beteigeuze", 52.41192, 12.53126));
		itemlist.add(new TodoItem(idCount++, "Android Studio installieren",
				"Installieren Sie das hervorragende Android Studio und erfreuen Sie sich an dessen fortgeschrittenen Features", 
				false, true, 1423242060000l, "Stalag 13", 53.0, 13.0));

		for (int i = 0; i < 20; i++) {
			itemlist.add(new TodoItem(idCount++, "Server Item " + i, i
					+ " description", rnd.nextInt(2) == 1 ? true : false, rnd
					.nextInt(2) == 1 ? true : false, rnd
					.nextInt(Integer.MAX_VALUE) + 1422500000000l, "somewhere", (54.0+(i/5)), (14.0+(i/5))));

		}
	}
}
