package de.fhb.maus.android.mytodoapp.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * some class to represent a contact
 */
public class Contact implements Comparable<Contact> {

	private long id;
	
	private String name;
	
	private List<String> emails = new ArrayList<String>();
	private List<String> phoneNumbers = new ArrayList<String>();
	
	
	public void addEmailAddress(String email) {
		this.emails.add(email);
	}

	public void addPhoneNumber(String phone) {
		this.phoneNumbers.add(phone);
	}
	
	public List<String> getEmails() {
		return this.emails;
	}

	public List<String> getPhoneNumbers() {
		return this.phoneNumbers;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean equals(Object other) {
		if (other.getClass() == this.getClass()) {
			return this.getId() == ((Contact)other).getId();
		}
		return false;
	}
	
	public String toString(){
		return this.getName();
	}

	@Override
	public int compareTo(Contact compareContact) {
		return this.getName().compareTo(compareContact.getName());
	}

}
