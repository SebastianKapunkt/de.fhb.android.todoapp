package org.dieschnittstelle.mobile.android.dataaccess.model;

import java.io.Serializable;

public class TodoItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 892589568245267958L;

	/**
	 * some static id assignment
	 */
	private static int ID = 0;

	private long id;
	private String name;
	private String description;
	private boolean isDone;
	private boolean isImportant;
	private long maturityDate;

	public TodoItem(long id, String name, String description, boolean isDone,
			boolean isImportant, long maturityDate) {
		this.id = (id == -1 ? ID++ : id);
		this.name = name;
		this.description = description;
		this.isDone = isDone;
		this.isImportant = isImportant;
		this.maturityDate = maturityDate;
	}

	public TodoItem() {
		// just do nothing
	}

	public TodoItem updateFrom(TodoItem item) {
		this.setName(item.getName());
		this.setDescription(item.getDescription());
		this.setDone(item.isDone());
		this.setImportant(item.isImportant());
		this.setMaturityDate(item.getMaturityDate());

		return this;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", name=" + name + ", description="
				+ description + ", isDone=" + isDone + ", isImportant="
				+ isImportant + ", maturityDate=" + maturityDate + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TodoItem)) {
			return false;
		}
		TodoItem other = (TodoItem) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isDone() {
		return isDone;
	}

	public boolean isImportant() {
		return isImportant;
	}

	public long getMaturityDate() {
		return maturityDate;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}

	public void setMaturityDate(long maturityDate) {
		this.maturityDate = maturityDate;
	}
}
