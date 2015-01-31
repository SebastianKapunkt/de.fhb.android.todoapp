package org.dieschnittstelle.mobile.android.dataaccess.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dieschnittstelle.mobile.android.dataaccess.model.DataItem;
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
		fillList();
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

		boolean removed = itemlist.remove(new DataItem() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 71193783355593985L;

			@Override
			public long getId() {
				return itemId;
			}
		});

		return removed;
	}

	@Override
	public TodoItem updateItem(TodoItem item) {
		logger.info("updateItem(): " + item);

		return itemlist.get(itemlist.indexOf(item)).updateFrom(item);
	}

	public void fillList(){
		itemlist.add(new TodoItem(idCount++, "1", "1d", false, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "2", "2d", true, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "3", "3d", false, true, 12366456));
		itemlist.add(new TodoItem(idCount++, "4", "4d", false, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "5", "5d", true, true, 12366456));
		itemlist.add(new TodoItem(idCount++, "6", "6d", false, true, 12366456));
		itemlist.add(new TodoItem(idCount++, "7", "7d", true, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "8", "8d", true, false, 12366456));
		itemlist.add(new TodoItem(idCount++, "9", "9d", false, true, 12366456));
	}
}
