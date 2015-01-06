package de.fhb.maus.android.mytodoapp.data;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
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

	public String getMaturityDateAsString() {
		Date date = new Date(maturityDate);
		Format format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		return format.format(date);
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

	public void setMaturityDateFromString(String maturityDate) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm dd.MM.yyyy");
		Date d = null;
		try {
			d = format.parse(maturityDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		this.maturityDate = d.getTime();
	}
}
