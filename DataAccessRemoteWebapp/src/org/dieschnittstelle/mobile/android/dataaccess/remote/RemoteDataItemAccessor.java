package org.dieschnittstelle.mobile.android.dataaccess.remote;

import java.util.ArrayList;
import java.util.List;

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
		itemlist.add(new TodoItem(
				idCount++,
				"Eclipse verbrennen",
				"Schritt 1: Eclipse löschen, Schritt 2: Ecipse download Server Ddos'n, Schritt 3: Android Studio runterladen",
				false, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "Android Studio installieren",
				"2d", true, false, 34534435));
		itemlist.add(new TodoItem(idCount++, "Server Item 3", "3d", false,
				true, 123885834));
		itemlist.add(new TodoItem(idCount++, "Server Item 4", "4d", false,
				false, 50405434));
		itemlist.add(new TodoItem(idCount++, "Server Item 5", "5d", true, true,
				96787745));
		itemlist.add(new TodoItem(idCount++, "Server Item 6", "6d", false,
				true, 1248235));
		itemlist.add(new TodoItem(idCount++, "Server Item 7", "7d", true,
				false, 345982312));
		itemlist.add(new TodoItem(idCount++, "Server Item 8", "8d", true,
				false, 29349234));
		itemlist.add(new TodoItem(idCount++, "Server Item 9", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 10", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 11", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 12", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 13", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 14", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 15", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 16", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 17", "9d", false,
				true, 435294592));
		itemlist.add(new TodoItem(idCount++, "Server Item 18", "9d", false,
				true, 435294592));
	}
}
