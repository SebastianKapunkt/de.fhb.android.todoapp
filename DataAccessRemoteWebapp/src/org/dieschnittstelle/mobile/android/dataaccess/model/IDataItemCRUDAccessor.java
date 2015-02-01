package org.dieschnittstelle.mobile.android.dataaccess.model;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;


@Path("/dataitem")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public interface IDataItemCRUDAccessor {
	
	@GET
	public List<TodoItem> readAllItems();
	
	@POST
	public TodoItem createItem(TodoItem item);

	@DELETE
	@Path("/{id}")
	public boolean deleteItem(@PathParam("id") long itemId);

	@PUT
	public TodoItem updateItem(TodoItem item);

}
