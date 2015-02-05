package de.fhb.maus.android.mytodoapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.comparator.TodoImportantComparator;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Ueberblick ueber die Todos eines gewaehlten Kontakts
 * 
 * @author Daniel Weis
 * 
 */
public class ContactTodosActivity extends Activity {

	private ListView list;
	private TodoArrayAdapter adapter;
	private Contact contact;
	private MySQLiteHelper db = new MySQLiteHelper(this);
	private ContactsAccessor conAcc;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		
		// Hole Kontakt-ID aus Intent und Kontakt-Daten von ContactsAccessor
		Intent intent = getIntent();
		long contactId = intent.getLongExtra("contact_id", -1);
		conAcc = new ContactsAccessor(this, this.getContentResolver());
		contact = conAcc.readContact(contactId);
		
		setContentView(R.layout.contact_todos);
		
		ImageView contactPic = (ImageView) findViewById(R.id.contact_picture_large);
		TextView contactName = (TextView) findViewById(R.id.contact_todos_contact_name);
		
		// Hole hochaufgeloestes Bild
		Bitmap photo = conAcc.readContactPicture(true, contactId);
		contactPic.setImageBitmap(photo);
		
		contactName.setText(contact.getName());
		
		list = (ListView) findViewById(R.id.contact_todo_list);

		adapter = new TodoArrayAdapter(this, db.getTodosFromContact(contactId));
		adapter.sort(new TodoImportantComparator());
		list.setAdapter(adapter);

	}
	/**
	 * Zurueck zur Todo-Uebersicht
	 * @param item
	 */
	public void gotoTodoOverview(MenuItem item) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_todos_menu, menu);
		return true;
	}

	/**
	 * Ueberschreibe Back-Button Funktion
	 */
	public void onBackPressed() {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
