package de.fhb.maus.android.mytodoapp.data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.fhb.maus.android.mytodoapp.R;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.util.Log;

public class ContactsAccessor {

	protected static String logger = ContactsAccessor.class.getSimpleName();

	/**
	 * the content resolver that is obtained from an activity
	 */
	private ContentResolver resolver;
	private Activity context;

	public ContactsAccessor(Activity context, ContentResolver resolver) {
		this.resolver = resolver;
		this.context = context;
	}


	public List<Contact> readAllContactsNames() {
		// the list of contact objects
		List<Contact> contactObjs = new ArrayList<Contact>();

		/*
		 * query all contacts
		 */
		// we iterate over the contacts
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);

		Log.i(logger, "queried contacts...");

		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Contact currentContact = new Contact();

			currentContact.setId(Long.parseLong(contactId));

			Log.i(logger, "got contactId: " + contactId);

			// set the contact name
			currentContact.setName(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

			contactObjs.add(currentContact);

		}

		cursor.close();

		Log.i(logger, "contacts are: " + contactObjs);

		return contactObjs;
	}
	
	public List<Contact> readContacts(List<Long> ids){
		List<Contact> contactObjs = new ArrayList<Contact>();
		for(long id : ids){
			contactObjs.add(readContact(id));
		}
		return contactObjs;
	}
	
	public Contact readContact(long id){
		Contact contact = new Contact();
		contact.setId(id);
		
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(id)}, null);
		
		while (cursor.moveToNext()) {
			contact.setName(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
			Log.i(logger, "found "+contact.getName());
		}
		
		
		/*
		 * query the phones for each contact
		 */
		Cursor phones = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
						+ id, null, null);

		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			int phoneType = phones
					.getInt(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA2));

			contact.addPhoneNumber(phoneNumber);

			Log.i(logger, "got phoneNumber: " + phoneNumber + " of type "
					+ phoneType);
		}

		phones.close();

		/*
		 * query the emails for each contact
		 */
		Cursor emails = resolver.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
						+ id, null, null);
		while (emails.moveToNext()) {
			// This would allow you get several email addresses
			String emailAddress = emails
					.getString(emails
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));

			Log.i(logger, "got emailAddress: " + emailAddress);

			contact.addEmailAddress(emailAddress);
		}
		emails.close();
		
		contact.setThumbnail(readContactPicture(false, id));
		
		return contact;
	}
	
	public Bitmap readContactPicture(boolean preferHighres, long contactId){
		Bitmap photo = null;
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
		InputStream is = ContactsContract.Contacts.openContactPhotoInputStream(resolver, contactUri, preferHighres);
		// Falls Kontakt Bild hat, gib dieses zurueck, ansonsten Platzhalter
		if (is != null)
			photo = BitmapFactory.decodeStream(is);
		else{
			photo = BitmapFactory.decodeResource(context.getResources(), R.drawable.contact_placeholder);
		}
		return photo;
	}

}