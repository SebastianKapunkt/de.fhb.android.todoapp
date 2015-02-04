package de.fhb.maus.android.mytodoapp.activities;

import java.io.InputStream;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import de.fhb.maus.android.mytodoapp.R;
import de.fhb.maus.android.mytodoapp.adapter.TodoArrayAdapter;
import de.fhb.maus.android.mytodoapp.comparator.TodoDateComparator;
import de.fhb.maus.android.mytodoapp.comparator.TodoImportantComparator;
import de.fhb.maus.android.mytodoapp.data.Contact;
import de.fhb.maus.android.mytodoapp.data.ContactsAccessor;
import de.fhb.maus.android.mytodoapp.database.MySQLiteHelper;

/**
 * Ueberblick ueber die Todos eines gewaehlten Kontakts
 * 
 * @author Sebastian Kindt, Daniel Weis
 * 
 */
public class ContactTodosActivity extends Activity {

	public static final String SELECTED_TODO = "todo";
	private ListView list;
	private TodoArrayAdapter adapter;
	private Contact contact;
	MySQLiteHelper db = new MySQLiteHelper(this);
	private ContactsAccessor conAcc;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		Intent intent = getIntent();
		long contactId = intent.getLongExtra("contact_id", -1);
		conAcc = new ContactsAccessor(this, this.getContentResolver());
		contact = conAcc.readContact(contactId);
		setContentView(R.layout.contact_todos);
		
		ImageView contactPic = (ImageView) findViewById(R.id.contact_picture_large);
		TextView contactName = (TextView) findViewById(R.id.contact_todos_contact_name);
		
		Bitmap photo = conAcc.readContactPicture(true, contactId);
		contactPic.setImageBitmap(photo);
		contactName.setText(contact.getName());
		
		// get the ListView
		list = (ListView) findViewById(R.id.contact_todo_list);

		// get custom adapter
		adapter = new TodoArrayAdapter(this, db.getTodosFromContact(contactId));

		// set comparator to adapter
		adapter.sort(new TodoImportantComparator());

		// set the custom adapter to the list View
		list.setAdapter(adapter);

	}

	public void gotoTodoOverview(MenuItem item) {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_todos_menu, menu);
		return true;
	}

	// overwrite action of the backbutton from Android
	public void onBackPressed() {
		startActivity(new Intent(this, TodoOverviewActivity.class));
	}
}
