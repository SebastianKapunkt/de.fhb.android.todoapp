package de.fhb.maus.android.mytodoapp.data;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo {

	private long id;
	private String name;
	private String description;
	private boolean isDone;
	private boolean isImportant;
	private long maturityDate;

	public Todo(String name, String description, boolean isDone,
			boolean isImportant, long maturityDate) {
		this.name = name;
		this.description = description;
		this.isDone = isDone;
		this.isImportant = isImportant;
		this.maturityDate = maturityDate;
	}

	public Todo() {
		// just do nothing
	}

	public String convertTime(long time) {
		Date date = new Date(time);
		Format format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
		return format.format(date);
	}

	public long convertTime(String time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
		Date d = null;
		try {
			d = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d.getTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Todo [id=" + id + ", name=" + name + ", description="
				+ description + ", isDone=" + isDone + ", isImportant="
				+ isImportant + ", maturityDate=" + maturityDate + "]";
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the isDone
	 */
	public boolean isDone() {
		return isDone;
	}

	/**
	 * @return the isImportant
	 */
	public boolean isImportant() {
		return isImportant;
	}

	/**
	 * @return the maturityDate
	 */
	public long getMaturityDate() {
		return maturityDate;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param isDone
	 *            the isDone to set
	 */
	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	/**
	 * @param isImportant
	 *            the isImportant to set
	 */
	public void setImportant(boolean isImportant) {
		this.isImportant = isImportant;
	}

	/**
	 * @param maturityDate
	 *            the maturityDate to set
	 */
	public void setMaturityDate(long maturityDate) {
		this.maturityDate = maturityDate;
	}
}
