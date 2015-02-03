package de.fhb.maus.android.mytodoapp.data;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;

/**
 * 
 * @author Sebastian Kindt
 *
 */

@SuppressLint("SimpleDateFormat")
public class Todo {

	private long id;
	private String name;
	private String description;
	private boolean isDone;
	private boolean isImportant;
	private long maturityDate;
	private String locationName;
	private double locationLatitude;
	private double locationLongitude;

	public Todo(String name, String description, boolean isDone,
			boolean isImportant, long maturityDate, String locationName, double locationLatitude, double locationLongitude) {
		this.name = name;
		this.description = description;
		this.isDone = isDone;
		this.isImportant = isImportant;
		this.maturityDate = maturityDate;
		this.locationName = locationName;
		this.locationLatitude = locationLatitude;
		this.locationLongitude = locationLongitude;
	}

	public Todo() {
		// just do nothing
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", name=" + name + ", description=" + description
				+ ", isDone=" + isDone + ", isImportant=" + isImportant
				+ ", maturityDate=" + maturityDate
				+ ", locationName=" + locationName
				+ ", locationCoordinates=" + locationLatitude + ","
				+ locationLongitude + "]";
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
	
	public String getLocationName() {
		return locationName;
	}
	
	public LatLng getLocationCoordinates() {
		LatLng locationCoordinates = new LatLng(locationLatitude, locationLongitude);
		return locationCoordinates;
	}
	
	public double getLocationLatitude() {
		return locationLatitude;
	}
	
	public double getLocationLongitude() {
		return locationLongitude;
	}

	//getter to get Date as String
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

	//Setter for case to set Date from String
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
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	
	public void setLocationCoordinates(LatLng locationCoordinates) {
		this.locationLatitude = locationCoordinates.latitude;
		this.locationLongitude = locationCoordinates.longitude;
	}
	
	public void setLocationLatitude(double locationLatitude) {
		this.locationLatitude = locationLatitude;
	}
	
	public void setLocationLongitude(double locationLongitude) {
		this.locationLongitude = locationLongitude;
	}
}
