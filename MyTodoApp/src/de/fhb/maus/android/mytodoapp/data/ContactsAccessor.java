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

/**
 * Klasse, um einfachen Zugriff auf Kontaktdaten zu ermoeglichen
 *
 */
public class ContactsAccessor {

	protected static String logger = ContactsAccessor.class.getSimpleName();

	private ContentResolver resolver;
	private Activity context;

	public ContactsAccessor(Activity context, ContentResolver resolver) {
		this.resolver = resolver;
		this.context = context;
	}


	/**
	 * Erstellt Liste aller Kontakte.
	 * Liest nur ID und Name aus, keine vollständigen Contact-Objekte
	 * @return
	 */
	public List<Contact> readAllContactsNames() {
		
		List<Contact> contactObjs = new ArrayList<Contact>();

		// Cursor ueber alle Kontakte
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, null, null, null);

		Log.i(logger, "queried contacts...");

		// Iteriere ueber Kontakte und speichere jeweils ID und Name
		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			Contact currentContact = new Contact();

			currentContact.setId(Long.parseLong(contactId));

			Log.i(logger, "got contactId: " + contactId);

			currentContact.setName(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

			contactObjs.add(currentContact);

		}

		cursor.close();

		Log.i(logger, "contacts are: " + contactObjs);

		return contactObjs;
	}
	
	/**
	 * Erstellt zu uebergebenen Kontakt-IDs eine Liste mit vollstaendigen Kontakt-Objekten
	 * @param ids Liste der auszulesenden Kontakte
	 * @return
	 */
	public List<Contact> readContacts(List<Long> ids){
		List<Contact> contactObjs = new ArrayList<Contact>();
		for(long id : ids){
			contactObjs.add(readContact(id));
		}
		return contactObjs;
	}
	
	/**
	 * Erstellt vollstaendiges Kontakt-Objekt zu uebergebener ID
	 * @param id ID des auszulesenden Kontakts
	 * @return
	 */
	public Contact readContact(long id){
		Contact contact = new Contact();
		contact.setId(id);
		
		// Cursor nur ueber gewaehlten Kontakt
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
				null, ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(id)}, null);
		
		while (cursor.moveToNext()) {
			contact.setName(cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
			Log.i(logger, "found "+contact.getName());
		}
		
		
		// Telefonnummern holen
		Cursor phones = resolver.query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
						+ id, null, null);

		while (phones.moveToNext()) {
			String phoneNumber = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			contact.addPhoneNumber(phoneNumber);

			Log.i(logger, "got phoneNumber: " + phoneNumber);
		}
		phones.close();

		
		// Email-Adressen holen
		Cursor emails = resolver.query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = "
						+ id, null, null);
		while (emails.moveToNext()) {
			String emailAddress = emails
					.getString(emails
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));

			Log.i(logger, "got emailAddress: " + emailAddress);

			contact.addEmailAddress(emailAddress);
		}
		emails.close();
		
		//Kontakt-Bild Thumbnail holen
		contact.setThumbnail(readContactPicture(false, id));
		
		return contact;
	}
	
	/**
	 * Gibt Kontakt-Bild, wahlweise hohe Aufloesung oder Thumbnail, als Bitmap zurueck.
	 * Wird kein Bild gefunden, so wird ein Platzhalter-Bild zurückgegeben.
	 * @param preferHighres Bei true wird hohe Aufloesung, bei false Thumbnail zurueckgegeben
	 * @param contactId ID des Kontakts
	 * @return
	 */
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